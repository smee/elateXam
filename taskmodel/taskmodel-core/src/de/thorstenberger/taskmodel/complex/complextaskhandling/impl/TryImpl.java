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
package de.thorstenberger.taskmodel.complex.complextaskhandling.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType;

/**
 * @author Thorsten Berger
 *
 */
public class TryImpl implements Try {

	private TryType tryType;
	private ComplexTaskFactory complexTaskFactory;
	private ComplexTaskDefRoot complexTaskDefRoot;
	
	/**
	 * 
	 */
	public TryImpl( TryType tryType, ComplexTaskFactory complexTaskFactory, ComplexTaskDefRoot complexTaskDefRoot ) {
		this.tryType = tryType;
		this.complexTaskFactory = complexTaskFactory;
		this.complexTaskDefRoot = complexTaskDefRoot;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.Try#getStartTime()
	 */
	public long getStartTime() {
		return tryType.getStartTime();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.Try#getNumberOfPages()
	 */
	public int getNumberOfPages() {
		return tryType.getPage().size();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.Try#getPage(int)
	 */
	public Page getPage(int pageNo) {
		Iterator it = tryType.getPage().iterator();
		while( it.hasNext() ){
			PageType pageType = (PageType)it.next();
			if( pageType.getNo() == pageNo )
				return new PageImpl( pageType, complexTaskFactory, complexTaskDefRoot );
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.Try#getPages()
	 */
	public List<Page> getPages() {
		List<Page> ret = new ArrayList<Page>();
		Iterator it = tryType.getPage().iterator();
		while( it.hasNext() ){
			PageType pageType = (PageType)it.next();
			ret.add( new PageImpl( pageType, complexTaskFactory, complexTaskDefRoot ) );
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.Try#getProcessPercentage()
	 */
	public float getProcessPercentage() {
		List<Page> pages = getPages();
		int numOfSubTasks = 0;
		int numOfProcessedSubTasks = 0;
		
		for( Page page : pages ){
			List<SubTasklet> subtasklets = page.getSubTasklets();
			numOfSubTasks += subtasklets.size();
			for( SubTasklet subTasklet : subtasklets )
				if( subTasklet.isProcessed() )
					numOfProcessedSubTasks++;
		}
		
		if( numOfSubTasks == 0 )
			return 0;
		
		return (float)numOfProcessedSubTasks / (float) numOfSubTasks;
		
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskFactory#createPage(int, de.thorstenberger.taskmodel.complex.complextaskdef.Category)
	 */
	public Page addNewPage(int pageNumber, Category category) throws TaskApiException {
		
		PageType pageType;
		try {
			pageType = ( new ObjectFactory() ).createComplexTaskHandlingTypeTryTypePageType();
		} catch (JAXBException e) {
			throw new TaskApiException( e );
		}
		pageType.setNo( pageNumber );
		pageType.setCategoryRefID( category.getId() );
		tryType.getPage().add( pageType );
		
		return new PageImpl( pageType, complexTaskFactory, complexTaskDefRoot );
	}

	/**
	 * backdoor access
	 * @return
	 */
	public TryType getTryType(){
		return tryType;
	}

}
