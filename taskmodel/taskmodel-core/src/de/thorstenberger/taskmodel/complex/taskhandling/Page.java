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

import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Page {

	public static final int NOT_PROCESSED = 0;
	public static final int PARTLY_PROCESSED = 1;
	public static final int COMPLETELY_PROCESSED = 2;
	
	private ComplexTaskHandlingType.TryType.PageType pageElem;
	private ComplexTaskHandlingType.TryType tryElem;
	private SubTaskFactory factory;
	
	/**
	 * 
	 */
	public Page( ComplexTaskHandlingType.TryType.PageType pageElem,
			ComplexTaskHandlingType.TryType tryElem,
			SubTaskFactory factory ) {
		this.pageElem = pageElem;
		this.tryElem = tryElem;
		this.factory = factory;
	}
	
	public int getNumber(){
		return pageElem.getNo();
	}
	
	public int getProcessStatus(){
		SubTask[] subtasks = getSubTasks();
		int numOfProcessedSubTasks = 0;
		
		for( int i=0; i<subtasks.length; i++ )
			if( subtasks[i].isProcessed() )
				numOfProcessedSubTasks++;
		
		if( numOfProcessedSubTasks == 0 )
			return NOT_PROCESSED;
		else if( numOfProcessedSubTasks == subtasks.length )
			return COMPLETELY_PROCESSED;
		else
			return PARTLY_PROCESSED;
	}
	
	/**
	 * Returniert alle Aufgaben, die sich auf dieser Seite befinden.
	 * @param page
	 * @return
	 */
	public SubTask[] getSubTasks(){
		return factory.getSubTasks( tryElem, getNumber() );
	}
	
	public String getCategoryRefId(){
		return pageElem.getCategoryRefID();
	}

	public void setCategoryRefId( String id ){
		pageElem.setCategoryRefID( id );
	}

}
