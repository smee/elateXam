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
import java.util.Map;

/**
 * @author Thorsten Berger
 *
 */
public interface ComplexTaskDefRoot {

	/**
	 * Returns Categories hashed by id in a Map for fast lookup.
	 * Note that the order of values in a HashMap is not preserved.
	 * @return 
	 */
	public Map<String, Category> getCategories();
	
	/**
	 * 
	 * @return Categories in correctly preserved order
	 */
	public List<Category> getCategoriesList();
	
	public int getTries();

	public String getTitle();

	public String getDescription();

	public String getStartText();

	public int getTasksPerPage();

	public boolean hasTimeRestriction();

	public Integer getTimeInMinutesWithKindnessExtensionTime();

	public Integer getTimeInMinutesWithoutKindnessExtensionTime();
	
}
