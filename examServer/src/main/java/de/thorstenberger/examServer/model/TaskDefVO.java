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
package de.thorstenberger.examServer.model;

/**
 * @author Thorsten Berger
 *
 *@struts.form include-all="true" extends="BaseForm"
 */
public class TaskDefVO {

	private long id;
	private String title;
	private String type;
	private String shortDescription;
	private boolean stopped;
	private Long deadline;
	private boolean visible;
	private Long followingTaskId;

	private boolean showSolutionToStudents;
	private String complexTaskFile;


	public TaskDefVO() {
		this.id=-1; //marker for unused value as id==0 would be valid
	}
	/**
	 * @return Returns the shortDescription.
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	/**
	 * @param shortDescription The shortDescription to set.
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	/**
	 * @return Returns the complexTaskFile.
	 */
	public String getComplexTaskFile() {
		return complexTaskFile;
	}
	/**
	 * @param complexTaskFile The complexTaskFile to set.
	 */
	public void setComplexTaskFile(String complexTaskFile) {
		this.complexTaskFile = complexTaskFile;
	}
	/**
	 * @return Returns the deadline.
	 */
	public Long getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline The deadline to set.
	 */
	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the showSolutionToStudents.
	 */
	public boolean isShowSolutionToStudents() {
		return showSolutionToStudents;
	}
	/**
	 * @param showSolutionToStudents The showSolutionToStudents to set.
	 */
	public void setShowSolutionToStudents(boolean showSolutionToStudents) {
		this.showSolutionToStudents = showSolutionToStudents;
	}
	/**
	 * @return Returns the stopped.
	 */
	public boolean isStopped() {
		return stopped;
	}
	/**
	 * @param stopped The stopped to set.
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the followingTaskId
	 */
	public Long getFollowingTaskId() {
		return followingTaskId;
	}
	/**
	 * @param followingTaskId the followingTaskId to set
	 */
	public void setFollowingTaskId(Long followingTaskId) {
		this.followingTaskId = followingTaskId;
	}
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}



}
