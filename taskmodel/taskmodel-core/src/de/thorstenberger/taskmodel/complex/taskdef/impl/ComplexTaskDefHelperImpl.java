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
package de.thorstenberger.taskmodel.complex.taskdef.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDefType;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComplexTaskDefHelperImpl implements ComplexTaskDefHelper {

	private ComplexTaskDef complexTask;
	
	/**
	 * 
	 */
	public ComplexTaskDefHelperImpl( File complexTaskFile ) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance( "de.thorstenberger.taskmodel.complex.jaxb" );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setValidating( true );
			BufferedInputStream bis = new BufferedInputStream( new FileInputStream( complexTaskFile ) );
			complexTask = (ComplexTaskDef) unmarshaller.
				unmarshal( bis );
			bis.close();

		} catch (JAXBException e1) {
			throw new TaskModelPersistenceException( e1 );
		} catch (IOException e2) {
			throw new TaskModelPersistenceException( e2 );
		}

	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getComplexTask()
	 */
	public ComplexTaskDef getComplexTask(){
		return complexTask;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTries()
	 */
	public int getTries(){
		return complexTask.getConfig().getTries();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTitle()
	 */
	public String getTitle(){
		return complexTask.getTitle();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getDescription()
	 */
	public String getDescription(){
		return complexTask.getDescription();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getStartText()
	 */
	public String getStartText(){
		return complexTask.getStartText();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getCategoryTitle(java.lang.String)
	 */
	public String getCategoryTitle( String catId ){
		List cats = complexTask.getCategory();
		for( int i=0; i<cats.size(); i++ )
			if( ((ComplexTaskDefType.CategoryType) cats.get(i)).getId().equals( catId ) )
				return ((ComplexTaskDefType.CategoryType) cats.get(i)).getTitle();
			
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTasksPerPage()
	 */
	public int getTasksPerPage(){
		return complexTask.getConfig().getTasksPerPage();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#hasTimeRestriction()
	 */
	public boolean hasTimeRestriction(){
		return complexTask.getConfig().isSetTime() && complexTask.getConfig().getTime() > 0;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTimeInMinutesWithKindnessExtensionTime()
	 */
	public Integer getTimeInMinutesWithKindnessExtensionTime(){
		if( !hasTimeRestriction() )
			return null;
		
		if( complexTask.getConfig().isSetKindnessExtensionTime() )
			return complexTask.getConfig().getTime() + complexTask.getConfig().getKindnessExtensionTime();
		else
			return complexTask.getConfig().getTime();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTimeInMinutesWithoutKindnessExtensionTime()
	 */
	public Integer getTimeInMinutesWithoutKindnessExtensionTime(){
		if( !hasTimeRestriction() )
			return null;
		
		return complexTask.getConfig().getTime();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getMCBlockOfTask(java.lang.String)
	 */
	public ComplexTaskDefType.CategoryType.McTaskBlock getMCBlockOfTask( String id ){
		List cats = complexTask.getCategory();
		// alle Kategorien durchlaufen
		for( int i=0; i<cats.size(); i++ ){
			
			ComplexTaskDefType.CategoryType cat = (ComplexTaskDefType.CategoryType)cats.get( i );
			List taskBlocks = cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlock();
			
			// alle Blöcke in der Kategorie durchlaufen
			for( int j=0; j<taskBlocks.size(); j++ ){
				
				// wenn MC-Block gefunden, dann durchsuchen
				if( taskBlocks.get( j ) instanceof ComplexTaskDefType.CategoryType.McTaskBlock ){
					ComplexTaskDefType.CategoryType.McTaskBlock mcBlock =
						(ComplexTaskDefType.CategoryType.McTaskBlock)taskBlocks.get( j );
					List mcTasksOrChoices = mcBlock.getMcSubTaskDefOrChoice();
					
					// Block durchsuchen
					for( int k=0; k<mcTasksOrChoices.size(); k++ ){
						
						// es ist eine Aufgabendefinition
						if( mcTasksOrChoices.get( k ) instanceof McSubTaskDefType ){
							
							McSubTaskDefType mcTask =
								(McSubTaskDefType) mcTasksOrChoices.get( k );
							
							if( mcTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
								return mcBlock;
							
						// es ist ein exclusive choice
						}else if( mcTasksOrChoices.get( k ) instanceof ComplexTaskDefType.CategoryType.McTaskBlockType.Choice ){
							
							ComplexTaskDefType.CategoryType.McTaskBlockType.Choice choice =
								(ComplexTaskDefType.CategoryType.McTaskBlockType.Choice) mcTasksOrChoices.get( k );
							
							// und im choice wieder alle Aufgaben durchlaufen
							List mcTasks = choice.getMcSubTaskDef();
							for( int l=0; l<mcTasks.size(); l++ ){
								McSubTaskDefType mcTask =
									(McSubTaskDefType) mcTasks.get( l );
								
								if( mcTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
									return mcBlock;								
							}
							
						}
						

						
					}
					
				}
				
			}
		}
		
		// keine gefunden
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getMCTaskDef(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlockType, java.lang.String)
	 */
	public McSubTaskDefType getMCTaskDef( ComplexTaskDefType.CategoryType.McTaskBlockType block, String id ){
		List tasksOrChoices = block.getMcSubTaskDefOrChoice();
		
		// Block durchlaufen
		for( int i=0; i<tasksOrChoices.size(); i++ ){
			
			// es ist eine Aufgabendefinition
			if( tasksOrChoices.get( i ) instanceof McSubTaskDefType ){
				
				McSubTaskDefType mcTask = 
					(McSubTaskDefType) tasksOrChoices.get( i );
				
				if( mcTask.getId().equals( id ) )	// Aufgabendefinition gefunden
					return mcTask;
			
			// es ist ein exclusive choice
			}else if( tasksOrChoices.get( i ) instanceof ComplexTaskDefType.CategoryType.McTaskBlockType.Choice ){
				ComplexTaskDefType.CategoryType.McTaskBlockType.Choice choice =
					(ComplexTaskDefType.CategoryType.McTaskBlockType.Choice) tasksOrChoices.get( i );
				
				// und im choice alle Aufgaben durchlaufen
				List tasks = choice.getMcSubTaskDef();
				for( int j=0; j<tasks.size(); j++ ){
					McSubTaskDefType mcTask = 
						(McSubTaskDefType) tasks.get( j );
					
					if( mcTask.getId().equals( id ) )	// Aufgabendefinition gefunden
						return mcTask;					
				}
			}
			

		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getCLOZEBlockOfTask(java.lang.String)
	 */
	public ComplexTaskDefType.CategoryType.ClozeTaskBlock getCLOZEBlockOfTask( String id ){
		List cats = complexTask.getCategory();
		for( int i=0; i<cats.size(); i++ ){
			
			ComplexTaskDefType.CategoryType cat = (ComplexTaskDefType.CategoryType)cats.get( i );
			List taskBlocks = cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlock();
			// alle Blöcke durchlaufen
			for( int j=0; j<taskBlocks.size(); j++ ){
				// Cloze Block gefunden
				if( taskBlocks.get( j ) instanceof ComplexTaskDefType.CategoryType.ClozeTaskBlock ){
					ComplexTaskDefType.CategoryType.ClozeTaskBlock clozeBlock =
						(ComplexTaskDefType.CategoryType.ClozeTaskBlock)taskBlocks.get( j );

					List clozeTasksOrChoices = clozeBlock.getClozeSubTaskDefOrChoice();
					
					// Block durchsuchen
					for( int k=0; k<clozeTasksOrChoices.size(); k++ ){
						
						// es ist eine Aufgabendefinition
						if( clozeTasksOrChoices.get( k ) instanceof ClozeSubTaskDefType ){
							
							ClozeSubTaskDefType clozeTask =
								(ClozeSubTaskDefType) clozeTasksOrChoices.get( k );
							
							if( clozeTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
								return clozeBlock;
							
						// es ist ein exclusive choice
						}else if( clozeTasksOrChoices.get( k ) instanceof ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice ){
							
							ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice choice =
								(ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice) clozeTasksOrChoices.get( k );
							
							// und im choice wieder alle Aufgaben durchlaufen
							List clozeTasks = choice.getClozeSubTaskDef();
							for( int l=0; l<clozeTasks.size(); l++ ){
								ClozeSubTaskDefType clozeTask =
									(ClozeSubTaskDefType) clozeTasks.get( l );
								
								if( clozeTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
									return clozeBlock;								
							}
							
						}
						
					}
					
				}
				
			}
		}
		
		// keine gefunden
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getCLOZETaskDef(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlock, java.lang.String)
	 */
	public ClozeSubTaskDefType getCLOZETaskDef( ComplexTaskDefType.CategoryType.ClozeTaskBlock block, String id ){
		List tasksOrChoices = block.getClozeSubTaskDefOrChoice();
		
		// Block durchlaufen
		for( int i=0; i<tasksOrChoices.size(); i++ ){
			
			// es ist eine Aufgabendefinition
			if( tasksOrChoices.get( i ) instanceof ClozeSubTaskDefType ){
				
				ClozeSubTaskDefType clozeTask = 
					(ClozeSubTaskDefType) tasksOrChoices.get( i );
				
				if( clozeTask.getId().equals( id ) )	// Aufgabendefinition gefunden
					return clozeTask;
			
			// es ist ein exclusive choice
			}else if( tasksOrChoices.get( i ) instanceof ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice ){
				ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice choice =
					(ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice) tasksOrChoices.get( i );
				
				// und im choice alle Aufgaben durchlaufen
				List tasks = choice.getClozeSubTaskDef();
				for( int j=0; j<tasks.size(); j++ ){
					ClozeSubTaskDefType clozeTask = 
						(ClozeSubTaskDefType) tasks.get( j );
					
					if( clozeTask.getId().equals( id ) )	// Aufgabendefinition gefunden
						return clozeTask;					
				}
			}
			

		}
		return null;

	}

	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTEXTBlockOfTask(java.lang.String)
	 */
	public ComplexTaskDefType.CategoryType.TextTaskBlock getTEXTBlockOfTask( String id ){
		List cats = complexTask.getCategory();
		for( int i=0; i<cats.size(); i++ ){
			
			ComplexTaskDefType.CategoryType cat = (ComplexTaskDefType.CategoryType)cats.get( i );
			List taskBlocks = cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlock();
			
			for( int j=0; j<taskBlocks.size(); j++ ){
				
				// Text Block gefunden
				if( taskBlocks.get( j ) instanceof ComplexTaskDefType.CategoryType.TextTaskBlock ){
					ComplexTaskDefType.CategoryType.TextTaskBlock textBlock =
						(ComplexTaskDefType.CategoryType.TextTaskBlock)taskBlocks.get( j );
					
					List textTasksOrChoices = textBlock.getTextSubTaskDefOrChoice();
					
					// Block durchsuchen
					for( int k=0; k<textTasksOrChoices.size(); k++ ){
						
						// es ist eine Aufgabendefinition
						if( textTasksOrChoices.get( k ) instanceof TextSubTaskDef ){
							
							TextSubTaskDefType textTask =
								(TextSubTaskDefType) textTasksOrChoices.get( k );
							
							if( textTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
								return textBlock;
							
						// es ist ein exclusive choice
						}else if( textTasksOrChoices.get( k ) instanceof ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice ){
							
							ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice choice =
								(ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice) textTasksOrChoices.get( k );
							
							// und im choice wieder alle Aufgaben durchlaufen
							List textTasks = choice.getTextSubTaskDef();
							for( int l=0; l<textTasks.size(); l++ ){
								TextSubTaskDefType textTask =
									(TextSubTaskDefType) textTasks.get( l );
								
								if( textTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
									return textBlock;								
							}
							
						}
						

						
					}
					
				}
				
			}
		}
		
		// keine gefunden
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getTEXTTaskDef(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlock, java.lang.String)
	 */
	public TextSubTaskDefType getTEXTTaskDef( ComplexTaskDefType.CategoryType.TextTaskBlock block, String id ){
		List tasksOrChoices = block.getTextSubTaskDefOrChoice();

		// Block durchlaufen
		for( int i=0; i<tasksOrChoices.size(); i++ ){
			
			// es ist eine Aufgabendefinition
			if( tasksOrChoices.get( i ) instanceof TextSubTaskDef ){
				
				TextSubTaskDefType textTask = 
					(TextSubTaskDefType) tasksOrChoices.get( i );
				
				if( textTask.getId().equals( id ) )	// Aufgabendefinition gefunden
					return textTask;
			
			// es ist ein exclusive choice
			}else if( tasksOrChoices.get( i ) instanceof ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice ){
				ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice choice =
					(ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice) tasksOrChoices.get( i );
				
				// und im choice alle Aufgaben durchlaufen
				List tasks = choice.getTextSubTaskDef();
				for( int j=0; j<tasks.size(); j++ ){
					TextSubTaskDefType textTask = 
						(TextSubTaskDefType) tasks.get( j );
					
					if( textTask.getId().equals( id ) )	// Aufgabendefinition gefunden
						return textTask;					
				}
			}
			

		}
		return null;
	}

	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getMAPPINGBlockOfTask(java.lang.String)
	 */
	public ComplexTaskDefType.CategoryType.MappingTaskBlock getMAPPINGBlockOfTask( String id ){
		List cats = complexTask.getCategory();
		for( int i=0; i<cats.size(); i++ ){
			
			ComplexTaskDefType.CategoryType cat = (ComplexTaskDefType.CategoryType)cats.get( i );
			List taskBlocks = cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlock();
			
			for( int j=0; j<taskBlocks.size(); j++ ){
				// Block gefunden
				if( taskBlocks.get( j ) instanceof ComplexTaskDefType.CategoryType.MappingTaskBlock ){
					ComplexTaskDefType.CategoryType.MappingTaskBlock mappingBlock =
						(ComplexTaskDefType.CategoryType.MappingTaskBlock) taskBlocks.get( j );

					List mappingTasksOrChoices = mappingBlock.getMappingSubTaskDefOrChoice();
					// Block durchsuchen
					for( int k=0; k<mappingTasksOrChoices.size(); k++ ){
						
						// es ist eine Aufgabendefinition
						if( mappingTasksOrChoices.get( k ) instanceof MappingSubTaskDefType ){
							
							MappingSubTaskDefType mappingTask =
								(MappingSubTaskDefType) mappingTasksOrChoices.get( k );
							
							if( mappingTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
								return mappingBlock;
							
						// es ist ein exclusive choice
						}else if( mappingTasksOrChoices.get( k ) instanceof ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice ){
							
							ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice choice =
								(ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice) mappingTasksOrChoices.get( k );
							
							// und im choice wieder alle Aufgaben durchlaufen
							List mappingTasks = choice.getMappingSubTaskDef();
							for( int l=0; l<mappingTasks.size(); l++ ){
								MappingSubTaskDefType mappingTask =
									(MappingSubTaskDefType) mappingTasks.get( l );
								
								if( mappingTask.getId().equals( id ) )	// Aufgabe gefunden, also aktuellen Block zurückgeben
									return mappingBlock;								
							}
							
						}
						

						
					}

					
				}
				
			}
		}
		
		// keine gefunden
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getMAPPINGTaskDef(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.MappingTaskBlock, java.lang.String)
	 */
	public MappingSubTaskDefType getMAPPINGTaskDef( ComplexTaskDefType.CategoryType.MappingTaskBlock block, String id ){
		List tasksOrChoices = block.getMappingSubTaskDefOrChoice();
		
		// Block durchlaufen
		for( int i=0; i<tasksOrChoices.size(); i++ ){
			
			// es ist eine Aufgabendefinition
			if( tasksOrChoices.get( i ) instanceof MappingSubTaskDefType ){
				
				MappingSubTaskDefType mappingTask = 
					(MappingSubTaskDefType) tasksOrChoices.get( i );
				
				if( mappingTask.getId().equals( id ) )	// Aufgabendefinition gefunden
					return mappingTask;
			
			// es ist ein exclusive choice
			}else if( tasksOrChoices.get( i ) instanceof ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice ){
				ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice choice =
					(ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice) tasksOrChoices.get( i );
				
				// und im choice alle Aufgaben durchlaufen
				List tasks = choice.getMappingSubTaskDef();
				for( int j=0; j<tasks.size(); j++ ){
					MappingSubTaskDefType mappingTask = 
						(MappingSubTaskDefType) tasks.get( j );
					
					if( mappingTask.getId().equals( id ) )	// Aufgabendefinition gefunden
						return mappingTask;					
				}
			}
			

		}
		return null;
	}

}
