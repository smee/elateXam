/*

Copyright (C) 2004 Thorsten Berger

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package de.thorstenberger.taskmodel.complex.taskhandling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskFactoryHelper_MC {

	private static Random r = new Random();
	private ObjectFactory taskHandlingobjectFactory;
	
	private ComplexTaskDefHelper ctDefHandler;
	
	Log log = LogFactory.getLog( SubTaskFactoryHelper_MC.class );
	
	/**
	 * 
	 */
	public SubTaskFactoryHelper_MC( ComplexTaskDefHelper ctdh ) {
		taskHandlingobjectFactory = new ObjectFactory();
		this.ctDefHandler = ctdh;
	}
	
	
	
	/**
	 * Aus einem Block von MC-Aufgaben bestimmte Aufgaben aussuchen.
	 * @param mcBlock
	 * @return List mit Instanzen von SubTask_MC
	 */
	List constructSubTasksOfMCTaskBlock( ComplexTaskDefType.CategoryType.McTaskBlock mcBlock ){
		List ret = new ArrayList();
		
		List allSubTasksOrChoices = mcBlock.getMcSubTaskDefOrChoice();
		
		// Anzahl der schlieﬂlich angezeigten Aufgaben berechnen:
		int numOfTasks = Math.min( allSubTasksOrChoices.size(), mcBlock.getConfig().getNoOfSelectedTasks() );
		
		// Auswahl-Reihenfolge der Aufgaben
		int[] selectOrder = new int[0];
		
		if( mcBlock.getConfig().isPreserveOrder() ){
			// zuf‰llig Aufgaben ausw‰hlen, deren Reihenfolge aber gleich bleibt	
			int[] tmpOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
			selectOrder = new int[ numOfTasks ];
			System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
			Arrays.sort( selectOrder );										// und sortieren
			
		}else
			// sonst komplett zuf‰llige Permutation
			selectOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
//		ComplexTaskHandlingType
		
		ComplexTaskHandlingType.TryType.PageType.McSubTask newMcSubTask;
		
		// ok, jetzt bestimmte Anzahl an Aufgaben aussuchen
		for( int i=0; i<numOfTasks; i++){
			
			// Aufgaben-Definition aussuchen
			McSubTaskDefType mcSubTaskDef = null;
			Object currentSubTaskDefOrChoice = allSubTasksOrChoices.get( selectOrder[i] );
			
			// muss eine der beiden Instanzen sein ... sonst NPE!
			if( currentSubTaskDefOrChoice instanceof McSubTaskDefType )
				mcSubTaskDef = (McSubTaskDefType) currentSubTaskDefOrChoice;
			else if( currentSubTaskDefOrChoice instanceof ComplexTaskDefType.CategoryType.McTaskBlockType.Choice ){
				mcSubTaskDef = getSubTaskDefFromChoice( 
						(ComplexTaskDefType.CategoryType.McTaskBlockType.Choice) currentSubTaskDefOrChoice );
			}
			
			// neues JAXB-Element f¸r Aufgabe erzeugen
			try {
				newMcSubTask = (ComplexTaskHandlingType.TryType.PageType.McSubTask)
									taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMcSubTask();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			
			newMcSubTask.setRefId( mcSubTaskDef.getId() );
			
			// und jetzt mˆgliche Antworten einf¸gen
			constructAnswersForMCSubTask( newMcSubTask, mcSubTaskDef );
			
			// und ausgew‰hlte Aufgabe zur¸ckgeben, eingef¸gt wird von ComplexTaskHandler
			ret.add( i, new SubTask_MC( mcBlock, mcSubTaskDef, newMcSubTask) );
		}
		
		
		return ret;
	}
	
	private McSubTaskDefType getSubTaskDefFromChoice( ComplexTaskDefType.CategoryType.McTaskBlockType.Choice choice ){
		
		List mcSubTaskDefs = choice.getMcSubTaskDef();
		McSubTaskDefType ret = (McSubTaskDefType)
									mcSubTaskDefs.get( RandomUtil.getInt( mcSubTaskDefs.size() ) );
		
		return ret;
		
	}
	
	/**
	 * Mˆgliche Antworten zu MC-Aufgaben hinzuf¸gen.
	 * @param newMcSubTask
	 * @param mcSubTaskDef
	 */
	private void constructAnswersForMCSubTask( ComplexTaskHandlingType.TryType.PageType.McSubTask newMcSubTask,
			McSubTaskDefType mcSubTaskDef){

		List correctAnswers = mcSubTaskDef.getCorrect();
		List incorrectAnswers = mcSubTaskDef.getIncorrect();
				
		// sicherheitshalber pr¸fen, d¸rfte aber schon durch XML-Schema ausgeschlossen sein
		if( correctAnswers.size() == 0 || mcSubTaskDef.getDisplayedAnswers() == 0 ){
			log.warn( "Aufgabe " + mcSubTaskDef.getId() + " wird keine Antworten enthalten!" );
			return;
		}

		int numOfCorrectAnswers;
		// nach Kategorie die Anzahl der richtigen Antworten festlegen
		if( mcSubTaskDef.getCategory().equals( SubTask_MC.CAT_SINGLESELECT ) ){
			numOfCorrectAnswers = 1;
		}else{
			
			int wantedMinCorrect = mcSubTaskDef.isSetMinCorrectAnswers() ?
										mcSubTaskDef.getMinCorrectAnswers() : 1;
			
			// mindestens so viele richtige Antworten
			// das kommt daher, dass zu wenig falsche Fragen da sein kˆnnten, um die gew¸nschte
			// Anzahl angezeigter Antworten zu erzeugen
			int minCorr = Math.max( mcSubTaskDef.getDisplayedAnswers() - incorrectAnswers.size(), wantedMinCorrect );
			// und sicherheitshalber beschr‰nken
			minCorr = Math.min( minCorr, correctAnswers.size() );
			minCorr = Math.min( minCorr, mcSubTaskDef.getDisplayedAnswers() );
			
			// maximal so viele richtige Antworten
			int maxCorr = Math.min( correctAnswers.size(), mcSubTaskDef.getDisplayedAnswers() );
			if( mcSubTaskDef.isSetMaxCorrectAnswers() )
				maxCorr = Math.min( maxCorr, mcSubTaskDef.getMaxCorrectAnswers() );
			
			// TODO Algorithmus verifizieren
			if( maxCorr < minCorr )
				maxCorr = minCorr;
			
			// ok, jetzt zuf‰llig mit den berechneten Grenzen festlegen
			numOfCorrectAnswers = r.nextInt( maxCorr - minCorr + 1 ) + minCorr;
			
		}


		// Anzahl der insg. anzuzeigenden Antworten
		int numOfAnswers = Math.min( numOfCorrectAnswers + incorrectAnswers.size() , mcSubTaskDef.getDisplayedAnswers() );		

		// Array der ausgew‰hlten Antworten		
		ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType[] toInsert =
			new ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType[ numOfAnswers ];
		
		// Auswahlreihenfolge der Antworten als zuf‰llige Permutation
		int[] correctAnswersOrder = RandomUtil.getPermutation( correctAnswers.size() );
		int[] incorrectAnswersOrder = RandomUtil.getPermutation( incorrectAnswers.size() );
		
		int[] insertOrder = RandomUtil.getPermutation( toInsert.length );
		int insertIndex = 0;
		
		// korrekte einf¸gen
		for( int i=0; i<numOfCorrectAnswers; i++)
			toInsert[ insertOrder[ insertIndex++ ] ] = createAnswerType( 
					(McSubTaskDefType.CorrectType)
							correctAnswers.get( correctAnswersOrder[i] ) );

		// falsche einf¸gen
		for( int i=0; i < ( numOfAnswers - numOfCorrectAnswers ); i++ )
			toInsert[ insertOrder[ insertIndex++ ] ] = createAnswerType(
					(McSubTaskDefType.IncorrectType)
							incorrectAnswers.get( incorrectAnswersOrder[i] ) );
		
		
		// und jetzt die Antworten in XML entspr. einf¸gen
		List answers = newMcSubTask.getAnswer();
		for( int i=0; i<toInsert.length; i++)
			answers.add( toInsert[i] );
		
	}
	
	/**
	 * neuen AnswerType aus korrekter Antwort erstellen
	 * @param correct
	 * @return
	 */
	private ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType createAnswerType(
						McSubTaskDefType.CorrectType correct ){
		
		ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType ret;
		
		try {
			ret = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMcSubTaskTypeAnswerType();
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		ret.setRefId( correct.getId() );
		ret.setSelected( false );
		
		return ret;
		
	}

	/**
	 * neuen AnswerType aus inkorrekten Antworten erstellen
	 * @param incorrect
	 * @return
	 */
	private ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType createAnswerType(
			McSubTaskDefType.IncorrectType incorrect ){

		ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType ret;
		
		try {
			ret = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMcSubTaskTypeAnswerType();
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		
		ret.setRefId( incorrect.getId() );
		ret.setSelected( false );

		return ret;

	}

	
	public SubTask_MC instantiateSubTask_MC( ComplexTaskHandlingType.TryType.PageType.McSubTask mcSubTask ){
		
		ComplexTaskDefType.CategoryType.McTaskBlock block =
												ctDefHandler.getMCBlockOfTask( mcSubTask.getRefId() );
		
		McSubTaskDefType mcSubTaskDef =
												ctDefHandler.getMCTaskDef( block, mcSubTask.getRefId() );
		if( mcSubTask == null )
			throw new TaskModelPersistenceException( "SubTaskDef " + mcSubTask.getRefId() + " not found" );
		
		return new SubTask_MC( block, mcSubTaskDef, mcSubTask );

	}

}
