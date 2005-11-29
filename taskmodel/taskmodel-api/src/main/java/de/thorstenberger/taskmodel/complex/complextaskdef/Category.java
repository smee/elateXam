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
package de.thorstenberger.taskmodel.complex.complextaskdef;

import java.util.List;

/**
 * All tasks of a {@link ComplexTaskDefRoot} belong to a category. These are toplevel containers 
 * that may be used to semantically structure a complex task. Each category consists of several {@link Block}s.
 * 
 * @author Thorsten Berger
 *
 */
public interface Category {

	public String getTitle();
	
	/**
	 * FIXME change xml schema
	 */
	public boolean isIgnoreOrderOfBlocks();
	
	public String getId();
	
	public boolean isMixAllSubTasks();
	
	/**
	 * How many tasks should be rendered to one page? Overrides the setting of {@link ComplexTaskDefRoot#getTasksPerPage()}.
	 * 
	 * @return tasks per page or NULL if not defined for this category (use the document wide setting!)
	 */
	public Integer getTasksPerPage();
	
	/**
	 * @return all blocks of this category
	 */
	public List<Block> getBlocks();
	
	/**
	 * Get a specific block by index.
	 * @param index
	 * @return a block or null if there is none at the given index
	 */
	public Block getBlock( int index );
	
	
}
