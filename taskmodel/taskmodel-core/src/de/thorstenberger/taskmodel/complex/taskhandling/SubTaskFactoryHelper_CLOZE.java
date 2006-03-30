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
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskFactoryHelper_CLOZE {

	private ComplexTaskDefHelper ctDefHandler;
	private ObjectFactory taskHandlingobjectFactory;
	
	/**
	 * 
	 */
	public SubTaskFactoryHelper_CLOZE( ComplexTaskDefHelper ctDefHandler ) {
		this.ctDefHandler = ctDefHandler;
		taskHandlingobjectFactory = new ObjectFactory();
	}
	
	public List constructSubTasksOfCLOZETaskBlock( ComplexTaskDefType.CategoryType.ClozeTaskBlock clozeBlock ){
		List ret = new ArrayList();
		
		List allSubTasksOrChoices = clozeBlock.getClozeSubTaskDefOrChoice();
		// Anzahl der schlieﬂlich angezeigten Aufgaben berechnen:
		int numOfTasks = Math.min( allSubTasksOrChoices.size(), clozeBlock.getConfig().getNoOfSelectedTasks() );
		
		// Auswahl-Reihenfolge der Aufgaben
		int[] selectOrder = new int[0];
		
		if( clozeBlock.getConfig().isPreserveOrder() ){
			// zuf‰llig Aufgaben ausw‰hlen, deren Reihenfolge aber gleich bleibt	
			int[] tmpOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
			selectOrder = new int[ numOfTasks ];
			System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
			Arrays.sort( selectOrder );										// und sortieren
		}else
			// sonst komplett zuf‰llige Permutation
			selectOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );

		ComplexTaskHandlingType.TryType.PageType.ClozeSubTask newClozeSubTask;
		
		// ok, jetzt bestimmte Anzahl an Aufgaben aussuchen
		for( int i=0; i<numOfTasks; i++){
			
			// Aufgaben-Definition aussuchen
			ClozeSubTaskDefType clozeSubTaskDef = null;
			Object currentSubTaskDefOrChoice = allSubTasksOrChoices.get( selectOrder[i] );
			
			// muss eine der beiden Instanzen sein ... sonst NPE!
			if( currentSubTaskDefOrChoice instanceof ClozeSubTaskDefType )
				clozeSubTaskDef = (ClozeSubTaskDefType) currentSubTaskDefOrChoice;
			else if( currentSubTaskDefOrChoice instanceof ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice ){
				clozeSubTaskDef = getSubTaskDefFromChoice( 
						(ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice) currentSubTaskDefOrChoice );
			}

			// neues JAXB-Element f¸r Aufgabe erzeugen
			try {
				newClozeSubTask = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeClozeSubTask();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			
			newClozeSubTask.setRefId( clozeSubTaskDef.getId() );
			
			addGaps( newClozeSubTask, clozeSubTaskDef );
			
			// und ausgew‰hlte Aufgabe einf¸gen
			ret.add( i, new SubTask_CLOZE( clozeBlock, clozeSubTaskDef, newClozeSubTask) );
		}
		
		
		return ret;
	}

	private ClozeSubTaskDefType getSubTaskDefFromChoice( ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice choice ){
		
		List clozeSubTaskDefs = choice.getClozeSubTaskDef();
		ClozeSubTaskDefType ret = (ClozeSubTaskDefType)
									clozeSubTaskDefs.get( RandomUtil.getInt( clozeSubTaskDefs.size() ) );
		
		return ret;
		
	}

	private void addGaps( ComplexTaskHandlingType.TryType.PageType.ClozeSubTask newClozeSubTask,
							ClozeSubTaskDefType clozeSubTaskDef ){
		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		for( int i=0; i<content.size(); i++ ){
			Object token = content.get( i );
			if( token instanceof ClozeSubTaskDefType.ClozeType.Gap ){
				ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType gap;
				try {
					gap = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeClozeSubTaskTypeGapType();
					gap.setGapValue( "" );
					gap.setCorrected( false );
					newClozeSubTask.getGap().add( gap );
				} catch (JAXBException e) {
					throw new TaskModelPersistenceException( e );
				}
			}
		}
	}
	
	public SubTask_CLOZE instantiateSubTask_CLOZE( ComplexTaskHandlingType.TryType.PageType.ClozeSubTask clozeSubTask ){
		
		ComplexTaskDefType.CategoryType.ClozeTaskBlock block =
												ctDefHandler.getCLOZEBlockOfTask( clozeSubTask.getRefId() );
		
		ClozeSubTaskDefType clozeSubTaskDef =
												ctDefHandler.getCLOZETaskDef( block, clozeSubTask.getRefId() );
		if( clozeSubTask == null )
			throw new TaskModelPersistenceException( "SubTaskDef " + clozeSubTask.getRefId() + " not found" );
		
		return new SubTask_CLOZE( block, clozeSubTaskDef, clozeSubTask );

	}
	
}
