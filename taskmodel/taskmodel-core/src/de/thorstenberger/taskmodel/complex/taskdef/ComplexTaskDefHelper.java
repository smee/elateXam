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
package de.thorstenberger.taskmodel.complex.taskdef;

import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDefType;

/**
 * @author Thorsten Berger
 *
 */
public interface ComplexTaskDefHelper {

	/**
	 * @return
	 */
	public ComplexTaskDef getComplexTask();

	public int getTries();

	public String getTitle();

	public String getDescription();

	public String getStartText();

	public String getCategoryTitle(String catId);

	public int getTasksPerPage();

	public boolean hasTimeRestriction();

	public Integer getTimeInMinutesWithKindnessExtensionTime();

	public Integer getTimeInMinutesWithoutKindnessExtensionTime();

	/**
	 * Findet zu einer Aufgabe (ID) den entsprechenden Aufgaben-Block.
	 * @param id
	 * @return
	 */
	public ComplexTaskDefType.CategoryType.McTaskBlock getMCBlockOfTask(
			String id);

	/**
	 * Findet zu einer Aufgabe (ID) die entsprechende Aufgabendefinition, ausgehend vom geg. Aufgaben-Block.
	 * @param block
	 * @param id
	 * @return
	 */
	public McSubTaskDefType getMCTaskDef(
			ComplexTaskDefType.CategoryType.McTaskBlockType block, String id);

	/**
	 * Findet zu einer Aufgabe (ID) den entsprechenden Aufgaben-Block.
	 * @param id
	 * @return
	 */
	public ComplexTaskDefType.CategoryType.ClozeTaskBlock getCLOZEBlockOfTask(
			String id);

	/**
	 * Findet zu einer Aufgabe (ID) die entsprechende Aufgabendefinition, ausgehend vom geg. Aufgaben-Block.
	 * @param block
	 * @param id
	 * @return
	 */
	public ClozeSubTaskDefType getCLOZETaskDef(
			ComplexTaskDefType.CategoryType.ClozeTaskBlock block, String id);

	/**
	 * Findet zu einer Aufgabe (ID) den entsprechenden Aufgaben-Block.
	 * @param id
	 * @return
	 */
	public ComplexTaskDefType.CategoryType.TextTaskBlock getTEXTBlockOfTask(
			String id);

	/**
	 * Findet zu einer Aufgabe (ID) die entsprechende Aufgabendefinition, ausgehend vom geg. Aufgaben-Block.
	 * @param block
	 * @param id
	 * @return
	 */
	public TextSubTaskDefType getTEXTTaskDef(
			ComplexTaskDefType.CategoryType.TextTaskBlock block, String id);

	/**
	 * Findet zu einer Aufgabe (ID) den entsprechenden Aufgaben-Block.
	 * @param id
	 * @return
	 */
	public ComplexTaskDefType.CategoryType.MappingTaskBlock getMAPPINGBlockOfTask(
			String id);

	/**
	 * Findet zu einer Aufgabe (ID) die entsprechende Aufgabendefinition, ausgehend vom geg. Aufgaben-Block.
	 * @param block
	 * @param id
	 * @return
	 */
	public MappingSubTaskDefType getMAPPINGTaskDef(
			ComplexTaskDefType.CategoryType.MappingTaskBlock block, String id);

}