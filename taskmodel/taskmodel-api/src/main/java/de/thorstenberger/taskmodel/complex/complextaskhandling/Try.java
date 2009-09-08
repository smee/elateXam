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
package de.thorstenberger.taskmodel.complex.complextaskhandling;

import java.util.List;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;

/**
 * Each {@link ComplexTasklet} might get processed by a user several times (according to the value of {@link ComplexTaskDefRoot#getTries()}). Each attempt is a {@link Try}.
 * @author Thorsten Berger
 *
 */
public interface Try {
	
	public int getNumberOfPages();
	
	public long getStartTime();
	
	/**
	 * 
	 * @param pageNo, the page number, starting with 1
	 * @return
	 */
	public Page getPage( int pageNo );
	
	public List<Page> getPages();
	
	public ProgressInformation getProgressInformation();
	
	public Page addNewPage(int pageNumber, Category category) throws TaskApiException;
	
	public SubTasklet lookupSubTasklet( SubTaskDef subTaskDef );
	
	/**
	 * Informations about the processing state. If the user gave an answer, a {@link SubTasklet} is marked as processed (@see {@link SubTasklet#isProcessed()}.
	 */
	public static interface ProgressInformation{
		
		public int getNumOfSubtasklets();
		
		public int getNumOfProcessedSubtasklets();
		
		public float getProgressPercentage();
		
	}
	
}
