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
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType.ConceptType;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskFactoryHelper_MAPPING {

	private ComplexTaskDefHelper ctDefHandler;
	private ObjectFactory taskHandlingobjectFactory;
	
	/**
	 * 
	 */
	public SubTaskFactoryHelper_MAPPING( ComplexTaskDefHelper ctDefHandler ) {
		this.ctDefHandler = ctDefHandler;
		taskHandlingobjectFactory = new ObjectFactory();
	}
	
	
	public List constructSubTasksOfMAPPINGTaskBlock( ComplexTaskDefType.CategoryType.MappingTaskBlock mappingBlock ){
		List ret = new ArrayList();
		
		List allSubTasksOrChoices = mappingBlock.getMappingSubTaskDefOrChoice();
		// Anzahl der schlieﬂlich angezeigten Aufgaben berechnen:
		int numOfTasks = Math.min( allSubTasksOrChoices.size(), mappingBlock.getConfig().getNoOfSelectedTasks() );
		
		// Auswahl-Reihenfolge der Aufgaben
		int[] selectOrder = new int[0];
		
		if( mappingBlock.getConfig().isPreserveOrder() ){
			// zuf‰llig Aufgaben ausw‰hlen, deren Reihenfolge aber gleich bleibt	
			int[] tmpOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
			selectOrder = new int[ numOfTasks ];
			System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
			Arrays.sort( selectOrder );										// und sortieren
		}else
			// sonst komplett zuf‰llige Permutation
			selectOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );

		ComplexTaskHandlingType.TryType.PageType.MappingSubTask newMappingSubTask;
		
		// ok, jetzt bestimmte Anzahl an Aufgaben aussuchen
		for( int i=0; i<numOfTasks; i++){
			
			// Aufgaben-Definition aussuchen
			MappingSubTaskDefType mappingSubTaskDef = null;
			Object currentSubTaskDefOrChoice = allSubTasksOrChoices.get( selectOrder[i] );
			
			// muss eine der beiden Instanzen sein ... sonst NPE!
			if( currentSubTaskDefOrChoice instanceof MappingSubTaskDefType )
				mappingSubTaskDef = (MappingSubTaskDefType) currentSubTaskDefOrChoice;
			else if( currentSubTaskDefOrChoice instanceof ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice ){
				mappingSubTaskDef = getSubTaskDefFromChoice( 
						(ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice) currentSubTaskDefOrChoice );
			}
			
			// neues JAXB-Element f¸r Aufgabe erzeugen
			try {
				newMappingSubTask = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMappingSubTask();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			
			newMappingSubTask.setRefId( mappingSubTaskDef.getId() );
			
			// TODO
			addAssignments( newMappingSubTask, mappingSubTaskDef );
			
			// und ausgew‰hlte Aufgabe einf¸gen
			ret.add( i, new SubTask_Mapping( mappingBlock, mappingSubTaskDef, newMappingSubTask) );
		}
		
		
		return ret;
	}

	private MappingSubTaskDefType getSubTaskDefFromChoice( ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice choice ){
		
		List mappingSubTaskDefs = choice.getMappingSubTaskDef();
		MappingSubTaskDefType ret = (MappingSubTaskDefType)
									mappingSubTaskDefs.get( RandomUtil.getInt( mappingSubTaskDefs.size() ) );
		
		return ret;
		
	}

	
	/**
	 * initiale Zuordnungen (null) erzeugen
	 * @param newMappingSubTask
	 * @param mappingSubTaskDef
	 */
	private void addAssignments( ComplexTaskHandlingType.TryType.PageType.MappingSubTask newMappingSubTask,
							MappingSubTaskDefType mappingSubTaskDef ){

		List concepts = mappingSubTaskDef.getConcept();
		for( int i=0; i<concepts.size(); i++ ){
			ConceptType concept = (ConceptType) concepts.get( i );
			
			try {
				ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType newConcept =
					taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMappingSubTaskTypeConceptType();
				newMappingSubTask.getConcept().add( newConcept );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			
		}
	}
	
	public SubTask_Mapping instantiateSubTask_MAPPING( ComplexTaskHandlingType.TryType.PageType.MappingSubTask mappingSubTask ){
		
		ComplexTaskDefType.CategoryType.MappingTaskBlock block =
												ctDefHandler.getMAPPINGBlockOfTask( mappingSubTask.getRefId() );
		
		MappingSubTaskDefType mappingSubTaskDef =
												ctDefHandler.getMAPPINGTaskDef( block, mappingSubTask.getRefId() );
		if( mappingSubTask == null )
			throw new TaskModelPersistenceException( "SubTaskDef " + mappingSubTask.getRefId() + " not found" );
		
		return new SubTask_Mapping( block, mappingSubTaskDef, mappingSubTask );

	}
	
}