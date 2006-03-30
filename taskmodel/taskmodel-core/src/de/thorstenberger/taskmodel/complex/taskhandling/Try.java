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

import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Try {

	private ComplexTaskHandlingType.TryType tryElem;
	private int tryIndex;
	private SubTaskFactory factory;
	
	/**
	 * 
	 */
	public Try( ComplexTaskHandlingType.TryType tryElem, int tryIndex, SubTaskFactory factory ) {
		this.tryElem = tryElem;
		this.tryIndex = tryIndex;
		this.factory = factory;
	}

	public int getNumberOfPages(){
		return tryElem.getPage().size();
	}
	
	public long getStartTime(){
		return tryElem.getStartTime();
	}
	
	public Page getPage( int pageNo ){
		List pages = tryElem.getPage();
		for( int i=0; i<pages.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType page =
				(ComplexTaskHandlingType.TryType.PageType) pages.get( i );
			if( page.getNo() == pageNo )
				return new Page( page, tryElem, factory );
		}
		
		return null;
	}
	
	public Page addPage( ComplexTaskHandlingType.TryType tryElem,
																					int pageNumber,
																						String categoryRefId ){
		ComplexTaskHandlingType.TryType.PageType newPage;
		ObjectFactory objectFactory = new ObjectFactory();
		try {
			newPage = objectFactory.createComplexTaskHandlingTypeTryTypePageType();
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		
		newPage.setNo( pageNumber );
		newPage.setCategoryRefID( categoryRefId );
		tryElem.getPage().add( newPage );
		
		return new Page( newPage, tryElem, factory );
	}

	public Page[] getPages(){
		List pages = tryElem.getPage();
		Page ret[] = new Page[ pages.size() ];
		for( int i=0; i<pages.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType page =
				(ComplexTaskHandlingType.TryType.PageType) pages.get( i );
			ret[ i ] = new Page( page, tryElem, factory );
		}
		return ret;
	}
	
	public float getProcessPercentage(){
		Page[] pages = getPages();
		int numOfSubTasks = 0;
		int numOfProcessedSubTasks = 0;
		
		for( int i=0; i<pages.length; i++ ){
			SubTask[] subtasks = pages[i].getSubTasks();
			numOfSubTasks += subtasks.length;
			for( int j=0; j<subtasks.length; j++ )
				if( subtasks[j].isProcessed() )
					numOfProcessedSubTasks++;
		}
		
		if( numOfSubTasks == 0 )
			return 0;
		
		return (float)numOfProcessedSubTasks / (float) numOfSubTasks;
		
	}
	
	public int getTryIndex() {
		return tryIndex;
	}
}
