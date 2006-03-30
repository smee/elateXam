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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
public class SubTaskFactory {

//	public static final String TYPE_MC = "mctask";
//	public static final String TYPE_CLOZE = "clozetask";
//	public static final String TYPE_TEXT = "texttask";
	
	private static Random r = new Random();
	private ObjectFactory taskHandlingobjectFactory;
	
	private SubTaskFactoryHelper_MC mcHelper;
	private SubTaskFactoryHelper_CLOZE clozeHelper;
	private SubTaskFactoryHelper_TEXT textHelper;
	private SubTaskFactoryHelper_MAPPING mappingHelper;
	
	private ComplexTaskDefHelper ctDefHandler;
	
	/**
	 * 
	 */
	public SubTaskFactory( ComplexTaskDefHelper ctdh ) {
		taskHandlingobjectFactory = new ObjectFactory();
		this.ctDefHandler = ctdh;
		mcHelper = new SubTaskFactoryHelper_MC( ctDefHandler );
		clozeHelper = new SubTaskFactoryHelper_CLOZE( ctDefHandler );
		textHelper = new SubTaskFactoryHelper_TEXT( ctDefHandler );
		mappingHelper = new SubTaskFactoryHelper_MAPPING( ctDefHandler );
	}
	
	/**
	 * Factory-Methode: Aus einer Kategorie bestimmte Aufgaben unterschiedlicher Typen zusammenstellen.
	 * Diese sollten dann an ihre entsprechende Postion im JAXB-Baum, der den Bearbeitungsstand
	 * speichert, eingehangen werden.
	 * @param cat
	 * @return List mit Instanzen von SubTask, die noch nicht in den JAXB-Baum eingehängt worden sind.
	 */
	public SubTask[] constructSubTasks( ComplexTaskDefType.CategoryType cat ){
		
		List blocks = cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlock();
		Iterator blocksIt = blocks.iterator();
		
		List ret = new ArrayList();
		Object block;
		
		while( blocksIt.hasNext() ){
			
			block = blocksIt.next();
			if( block instanceof ComplexTaskDefType.CategoryType.McTaskBlock )
				ret.addAll( mcHelper.constructSubTasksOfMCTaskBlock(
							(ComplexTaskDefType.CategoryType.McTaskBlock) block ) );
			else if( block instanceof ComplexTaskDefType.CategoryType.ClozeTaskBlock )
				ret.addAll( clozeHelper.constructSubTasksOfCLOZETaskBlock(
						(ComplexTaskDefType.CategoryType.ClozeTaskBlock) block ) );
			else if( block instanceof ComplexTaskDefType.CategoryType.TextTaskBlock )
				ret.addAll( textHelper.constructSubTasksOfTEXTTaskBlock(
						(ComplexTaskDefType.CategoryType.TextTaskBlock) block ) );
			else if( block instanceof ComplexTaskDefType.CategoryType.MappingTaskBlock )
				ret.addAll( mappingHelper.constructSubTasksOfMAPPINGTaskBlock(
						(ComplexTaskDefType.CategoryType.MappingTaskBlock) block ) );
			// TODO andere Block-Typen
			
		}
		
		SubTask[] retArray = new SubTask[ ret.size() ];
		for( int i=0; i<retArray.length; i++ )
			retArray[i] = (SubTask) ret.get( i );
		
		return retArray;
	}
	

	public SubTask[] getSubTasks( ComplexTaskHandlingType.TryType tryElem, int page ){
		List pages = tryElem.getPage();
		Iterator it = pages.iterator();
		
		while( it.hasNext() ){
			ComplexTaskHandlingType.TryType.PageType pageElem =
				(ComplexTaskHandlingType.TryType.PageType) it.next();
			if( pageElem.getNo() == page )
				return getSubTasks( pageElem );
		}
		return null;
	}
	
	private SubTask[] getSubTasks( ComplexTaskHandlingType.TryType.PageType page ){
		List subtasks = page.getMcSubTaskOrClozeSubTaskOrTextSubTask();
		Iterator taskIt = subtasks.iterator();
		Object subtask;
		List ret = new ArrayList();
		
		while( taskIt.hasNext() ){
			subtask = taskIt.next();
			
			if( subtask instanceof ComplexTaskHandlingType.TryType.PageType.McSubTask )
				ret.add( mcHelper.instantiateSubTask_MC( (ComplexTaskHandlingType.TryType.PageType.McSubTask) subtask ) );
			else if( subtask instanceof ComplexTaskHandlingType.TryType.PageType.ClozeSubTask )
				ret.add( clozeHelper.instantiateSubTask_CLOZE( (ComplexTaskHandlingType.TryType.PageType.ClozeSubTask) subtask ) );
			else if( subtask instanceof ComplexTaskHandlingType.TryType.PageType.TextSubTask )
				ret.add( textHelper.instantiateSubTask_TEXT( (ComplexTaskHandlingType.TryType.PageType.TextSubTask) subtask ) );
			else if( subtask instanceof ComplexTaskHandlingType.TryType.PageType.MappingSubTask )
				ret.add( mappingHelper.instantiateSubTask_MAPPING( (ComplexTaskHandlingType.TryType.PageType.MappingSubTask) subtask ) );
			// TODO Factory erweitern
			
		}
		
		SubTask[] retArray = new SubTask[ ret.size() ];
		for( int i=0; i<retArray.length; i++ )
			retArray[ i ] = (SubTask) ret.get( i );
		
		return retArray;
	}
	
	
	public static int[] getStandardOrder( int length ){
		int[] ret = new int[ length ];
		for( int i=0; i<length; i++ )
			ret[i] = i;
		return ret;
	}
	


}
