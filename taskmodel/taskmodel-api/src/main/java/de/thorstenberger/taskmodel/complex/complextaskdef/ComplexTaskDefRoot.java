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

import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * This class is the complete task definition. Upon creating a new {@link Try} for a user, he get's a randomly choosen selection of {@link SubTaskDef} to process (@see )
 * @author Thorsten Berger
 *
 */
public interface ComplexTaskDefRoot {

	public enum CorrectionModeType{
		REGULAR,
		
		/**
		 * In this correction mode, only the first n processed Subtasklets wil be corrected and influence the overall result (points).
		 */
		CORRECTONLYPROCESSEDTASKS,
		
		/**
		 * In this correction mode, more than one (human) corrector proceed the correction in order to determine the overall result. 
		 */
		MULTIPLECORRECTORS;
	}
	
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
	
	public Integer getKindnessExtensionTimeInMinutes();

	public Integer getTimeInMinutesWithKindnessExtensionTime();

	public Integer getTimeInMinutesWithoutKindnessExtensionTime();
	
	public boolean isShowHandlingHintsBeforeStart();
	
	public CorrectionMode getCorrectionMode();
	
	
	public interface CorrectionMode{
		
		public CorrectionModeType getType(); 
		
	}
	
}
