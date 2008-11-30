/*

Copyright (C) 2006 Thorsten Berger

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
/**
 * 
 */
package de.thorstenberger.taskmodel.complex.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.Choice;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskBuilderImpl implements ComplexTaskBuilder {

	protected ComplexTaskFactory complexTaskFactory;
	
	public ComplexTaskBuilderImpl( ComplexTaskFactory complexTaskFactory ){
		this.complexTaskFactory = complexTaskFactory;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskBuilder#getComplexTaskFactory()
	 */
	public ComplexTaskFactory getComplexTaskFactory() {
		return complexTaskFactory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskBuilder#buildSubTaskletsFromCategory(de.thorstenberger.taskmodel.complex.complextaskdef.Category)
	 */
	public List<SubTasklet> buildSubTaskletsFromCategory(Category category, ComplexTaskDefRoot complexTaskDefRoot ) {
		List<SubTasklet> ret = new ArrayList<SubTasklet>();
		List<Block> blocks = category.getBlocks();
		List<List<SubTasklet>> subTaskletsOfBlocks = new ArrayList<List<SubTasklet>>();
		
		boolean mixAll = category.isMixAllSubTasks();
		boolean ignoreOrderOfBlocks = category.isIgnoreOrderOfBlocks();
		
		for( Block block : blocks ){
			subTaskletsOfBlocks.add( buildSubTaskletsFromBlock( block, complexTaskDefRoot, category.getId() ) );
		}
		
		if( !mixAll && !ignoreOrderOfBlocks ){
			
			for( List<SubTasklet> subTaskletsOfBlock : subTaskletsOfBlocks )
				for( SubTasklet subTasklet : subTaskletsOfBlock )
					ret.add( subTasklet );
			
		}else if( !mixAll && ignoreOrderOfBlocks ){
			
			int[] blockSelectOrder = RandomUtil.getPermutation( subTaskletsOfBlocks.size() );
			for( int i = 0; i < blockSelectOrder.length; i++ ){
				List<SubTasklet> subTaskletsOfBlock = subTaskletsOfBlocks.get( blockSelectOrder[i] );
				for( SubTasklet subTasklet : subTaskletsOfBlock )
					ret.add( subTasklet );
			}
		}else if( mixAll ){
			
			List<SubTasklet> allSubTasklets = new ArrayList<SubTasklet>();
			for( List<SubTasklet> subTaskletsOfBlock : subTaskletsOfBlocks )
				allSubTasklets.addAll( subTaskletsOfBlock );
			
			int[] selectOrder = RandomUtil.getPermutation( allSubTasklets.size() );
			for( int i = 0; i < selectOrder.length; i++ )
				ret.add( allSubTasklets.get( selectOrder[ i ] ) );
			
		}
		
		return ret;
		
	}



	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskBuilder#buildSubTaskletsFromBlock(de.thorstenberger.taskmodel.complex.complextaskdef.Block)
	 */
	public List<SubTasklet> buildSubTaskletsFromBlock(Block block, ComplexTaskDefRoot complexTaskDefRoot, String categoryId ) {
		
		List<SubTasklet> ret = new ArrayList<SubTasklet>();
		
		List<SubTaskDefOrChoice> allSubTasksOrChoices = block.getSubTaskDefOrChoiceList();
		
		// remove trash SubTaskDefs
		List<SubTaskDefOrChoice> toRemove = new LinkedList<SubTaskDefOrChoice>();
		for( SubTaskDefOrChoice stdoc : allSubTasksOrChoices ){
			if( ( stdoc instanceof SubTaskDef ) && ((SubTaskDef)stdoc).isTrash() )
				toRemove.add( stdoc );
		}
		for( SubTaskDefOrChoice tr : toRemove )
			allSubTasksOrChoices.remove( tr );
		
		
		
		// Anzahl der schlieﬂlich angezeigten Aufgaben berechnen:
		int numOfTasks = Math.min( allSubTasksOrChoices.size(), block.getNumberOfSelectedSubTasks() );
		
		// Auswahl-Reihenfolge der Aufgaben
		int[] selectOrder = new int[0];
		
		if( block.isPreserveOrder() ){
			// zuf‰llig Aufgaben ausw‰hlen, deren Reihenfolge aber gleich bleibt	
			int[] tmpOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
			selectOrder = new int[ numOfTasks ];
			System.arraycopy( tmpOrder, 0, selectOrder, 0, numOfTasks );	// hinten abschneiden
			Arrays.sort( selectOrder );										// und sortieren
			
		}else
			// sonst komplett zuf‰llige Permutation
			selectOrder = RandomUtil.getPermutation( allSubTasksOrChoices.size() );
		
		
		SubTasklet newSubTasklet;
		
		// ok, jetzt bestimmte Anzahl an Aufgaben aussuchen
		for( int i=0; i<numOfTasks; i++){
			
			// Aufgaben-Definition aussuchen
			SubTaskDef subTaskDef = null;
			Object currentSubTaskDefOrChoice = allSubTasksOrChoices.get( selectOrder[i] );
			
			// muss eine der beiden Instanzen sein ... sonst NPE!
			if( currentSubTaskDefOrChoice instanceof SubTaskDef )
				subTaskDef = (SubTaskDef) currentSubTaskDefOrChoice;
			else if( currentSubTaskDefOrChoice instanceof Choice ){
				subTaskDef = getSubTaskDefFromChoice( (Choice) currentSubTaskDefOrChoice );
			}
			
			try {
				newSubTasklet = complexTaskFactory.createSubTaskletForSubTaskDef( subTaskDef, complexTaskDefRoot, categoryId );
				// new:
				newSubTasklet.build();	// important to correctly initialize SubTasklet
			} catch (TaskApiException e) {
				throw new TaskModelPersistenceException( e );
			}
			
			ret.add( i, newSubTasklet );
		}
		
		
		return ret;

	}
	
	private SubTaskDef getSubTaskDefFromChoice( Choice choice ){
		List<SubTaskDef> subTaskDefs = choice.getSubTaskDefs();
		return subTaskDefs.get( RandomUtil.getInt( subTaskDefs.size() ) );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskBuilder#generateTry(de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot)
	 */
	public Try generateTry(ComplexTaskDefRoot complexTaskDefRoot, long startTime ) {
		
		Try newTry;
		try {
			newTry = complexTaskFactory.createTry( startTime, complexTaskFactory, complexTaskDefRoot );
		} catch (TaskApiException e1) {
			throw new TaskModelPersistenceException( e1 );
		}
		
		List<Category> categories = complexTaskDefRoot.getCategoriesList();
		
		int globalTasksPerPage = complexTaskDefRoot.getTasksPerPage();
		int pageNo = 1;
		int taskEnum = 1;
		
		for( Category category : categories ){
			
			// alle Aufgaben der Kategorie zusammenstellen,
			// der wohl umfangreichste Teil hier...
			List<SubTasklet> subTasklets = buildSubTaskletsFromCategory( category, complexTaskDefRoot );
			
			// Anzahl Seiten, die der Kategorie zugeordnet werden
			int tasksPerPageForCategory;
			if( category.getTasksPerPage() != null )
				tasksPerPageForCategory = category.getTasksPerPage();
			else
				tasksPerPageForCategory = globalTasksPerPage;
			
			int pages = (int) Math.ceil( (double)subTasklets.size() / (double)tasksPerPageForCategory );
			Page page;
			int j = 0;		
			
			for( int i=0; i < pages; i++ ){
				
								
				try {
					page = newTry.addNewPage( pageNo, category );
				} catch (TaskApiException e) {
					throw new TaskModelPersistenceException( e );
				}
				
				for( int k=0; k < tasksPerPageForCategory; k++ ){
					SubTasklet subTasklet = subTasklets.get( j++ );
					subTasklet.setVirtualSubtaskNumber( "" + taskEnum++ );
					subTasklet.addToPage( page );
					if( j >= subTasklets.size() )
						break;
				}
				pageNo++;
				
			}
			
		}
		
		return newTry;

		
	}
	
	

}
