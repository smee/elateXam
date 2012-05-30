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
 * Each block has {@link SubTaskDef}s and {@link Choice}s. The algorithm that creates the 
 * @author Thorsten Berger
 *
 */
public interface Block {

	/**
	 * @return a string denoting this block's subtasks type (mc, cloze, text etc.)
	 */
	public String getType();

	/**
	 * @return the index of the block in its category, starts with 0 for the first block
	 */
	public int getIndex();

	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList();

	public int getNumberOfSelectedSubTasks();

	public boolean isPreserveOrder();

	public float getPointsPerSubTask();
	
}
