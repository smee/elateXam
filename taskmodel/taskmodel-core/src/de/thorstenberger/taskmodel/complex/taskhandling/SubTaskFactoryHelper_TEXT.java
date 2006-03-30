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

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDefType;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskFactoryHelper_TEXT {

	private ComplexTaskDefHelper ctDefHandler;
	private ObjectFactory taskHandlingobjectFactory;
	
	/**
	 * 
	 */
	public SubTaskFactoryHelper_TEXT( ComplexTaskDefHelper ctDefHandler ) {
		this.ctDefHandler = ctDefHandler;
		taskHandlingobjectFactory = new ObjectFactory();
	}
	
	public List constructSubTasksOfTEXTTaskBlock( ComplexTaskDefType.CategoryType.TextTaskBlock textBlock ){
		List ret = new ArrayList();
		
		List allSubTasksOrChoices = textBlock.getTextSubTaskDefOrChoice();
		// Anzahl der schlieﬂlich angezeigten Aufgaben berechnen:
		int numOfTasks = Math.min( allSubTasksOrChoices.size(), textBlock.getConfig().getNoOfSelectedTasks() );
		
		// Auswahl-Reihenfolge der Aufgaben
		int[] selectOrder = new int[0];
		
		if( textBlock.getConfig().isPreserveOrder() ){
			// zuf‰llig Aufgaben ausw‰hlen, deren Reihenfolge aber gleich bleibt	
			int[] tmpOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
			selectOrder = new int[ numOfTasks ];
			System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
			Arrays.sort( selectOrder );										// und sortieren

		}else
			// sonst komplett zuf‰llige Permutation
			selectOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );

		ComplexTaskHandlingType.TryType.PageType.TextSubTask newTextSubTask;
		
		// ok, jetzt bestimmte Anzahl an Aufgaben aussuchen
		for( int i=0; i<numOfTasks; i++){
			
			// Aufgaben-Definition aussuchen
			TextSubTaskDefType textSubTaskDef = null;
			Object currentSubTaskDefOrChoice = allSubTasksOrChoices.get( selectOrder[i] );
			
			// muss eine der beiden Instanzen sein ... sonst NPE!
			if( currentSubTaskDefOrChoice instanceof TextSubTaskDefType )
				textSubTaskDef = (TextSubTaskDefType) currentSubTaskDefOrChoice;
			else if( currentSubTaskDefOrChoice instanceof ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice ){
				textSubTaskDef = getSubTaskDefFromChoice( 
						(ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice) currentSubTaskDefOrChoice );
			}

			// neues JAXB-Element f¸r Aufgabe erzeugen
			try {
				newTextSubTask = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeTextSubTask();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			
			newTextSubTask.setRefId( textSubTaskDef.getId() );
			newTextSubTask.setAnswer( "" );
			
			// und ausgew‰hlte Aufgabe einf¸gen
			ret.add( i, new SubTask_TEXT( textBlock, textSubTaskDef, newTextSubTask) );
		}
		
		
		return ret;
	}

	private TextSubTaskDefType getSubTaskDefFromChoice( ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice choice ){
		
		List textSubTaskDefs = choice.getTextSubTaskDef();
		TextSubTaskDefType ret = (TextSubTaskDefType)
									textSubTaskDefs.get( RandomUtil.getInt( textSubTaskDefs.size() ) );
		
		return ret;
		
	}

	
	public SubTask_TEXT instantiateSubTask_TEXT( ComplexTaskHandlingType.TryType.PageType.TextSubTask textSubTask ){
		
		ComplexTaskDefType.CategoryType.TextTaskBlock block =
												ctDefHandler.getTEXTBlockOfTask( textSubTask.getRefId() );
		
		TextSubTaskDefType textSubTaskDef =
												ctDefHandler.getTEXTTaskDef( block, textSubTask.getRefId() );
		if( textSubTask == null )
			throw new TaskModelPersistenceException( "SubTaskDef " + textSubTask.getRefId() + " not found" );
		
		return new SubTask_TEXT( block, textSubTaskDef, textSubTask );

	}
	
}
