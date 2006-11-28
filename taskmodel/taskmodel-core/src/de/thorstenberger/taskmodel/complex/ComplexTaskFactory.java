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
package de.thorstenberger.taskmodel.complex;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * @author Thorsten Berger
 *
 */
public interface ComplexTaskFactory {
	
	/**
	 * 
	 * @param block
	 * @param index the index determing the position of the block in its category, starts with 0 for the first block
	 * @return
	 */
	public Block instantiateBlock( Object block, int index );
	
	public SubTasklet createSubTaskletForSubTaskDef( SubTaskDef subTaskDef, ComplexTaskDefRoot complexTaskDefRoot, String categoryId ) throws TaskApiException;
	
	public SubTasklet instantiateSubTasklet( Object subTask, ComplexTaskDefRoot complexTaskDefRoot, String categoryId );
	
	public Try createTry( long startTime, ComplexTaskFactory complexTaskFactory, ComplexTaskDefRoot complexTaskDefRoot ) throws TaskApiException;
	
}
