/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel;

import java.util.List;

/**
 * Arbitrary categorization of {@link TaskDef}s within a hostsystem. The matching of category and taskdef depends
 * on the semantic contents of each.
 * @author Thorsten Berger
 *
 */
public interface TaskCategory {

	/**
	 * Unique id within the host system.
	 * @return
	 */
	public long getId();

	/**
	 * Name of this categoy.
	 * @return
	 */
	public String getName();

	/**
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Get all task definitions of this category.
	 * @return
	 */
	public List<TaskDef> getTaskDefs();

}
