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
package de.thorstenberger.taskmodel.view.statistics;

import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public class RootInfoVO {
	
	private long taskId;
	private List<CategoryInfoVO> categories;
	private int tries;
	private String title;
	private String description;
	private String startText;
	private int tasksPerPage;
	private boolean timeRestriction;
	private Integer timeInMinutesWithoutKindnessExtensionTime;
	private Integer kindnessExtensionTime;
	
	
	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the categories
	 */
	public List<CategoryInfoVO> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<CategoryInfoVO> categories) {
		this.categories = categories;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the kindnessExtensionTime
	 */
	public Integer getKindnessExtensionTime() {
		return kindnessExtensionTime;
	}
	/**
	 * @param kindnessExtensionTime the kindnessExtensionTime to set
	 */
	public void setKindnessExtensionTime(Integer kindnessExtensionTime) {
		this.kindnessExtensionTime = kindnessExtensionTime;
	}
	/**
	 * @return the startText
	 */
	public String getStartText() {
		return startText;
	}
	/**
	 * @param startText the startText to set
	 */
	public void setStartText(String startText) {
		this.startText = startText;
	}
	/**
	 * @return the tasksPerPage
	 */
	public int getTasksPerPage() {
		return tasksPerPage;
	}
	/**
	 * @param tasksPerPage the tasksPerPage to set
	 */
	public void setTasksPerPage(int tasksPerPage) {
		this.tasksPerPage = tasksPerPage;
	}
	/**
	 * @return the timeInMinutesWithoutKindnessExtensionTime
	 */
	public Integer getTimeInMinutesWithoutKindnessExtensionTime() {
		return timeInMinutesWithoutKindnessExtensionTime;
	}
	/**
	 * @param timeInMinutesWithoutKindnessExtensionTime the timeInMinutesWithoutKindnessExtensionTime to set
	 */
	public void setTimeInMinutesWithoutKindnessExtensionTime(
			Integer timeInMinutesWithoutKindnessExtensionTime) {
		this.timeInMinutesWithoutKindnessExtensionTime = timeInMinutesWithoutKindnessExtensionTime;
	}
	/**
	 * @return the timeRestriction
	 */
	public boolean isTimeRestriction() {
		return timeRestriction;
	}
	/**
	 * @param timeRestriction the timeRestriction to set
	 */
	public void setTimeRestriction(boolean timeRestriction) {
		this.timeRestriction = timeRestriction;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the tries
	 */
	public int getTries() {
		return tries;
	}
	/**
	 * @param tries the tries to set
	 */
	public void setTries(int tries) {
		this.tries = tries;
	}
	
	

}
