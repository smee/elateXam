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

/**
 *
 * Defines methods providing information, that should be shown to the user before
 * starting the handling of the task. Methods for the exact problem etc. should be defined
 * in subinterfaces or the implementing class.
 *
 * @author Thorsten Berger
 *
 */
public interface TaskDef {

	public long getId();

	public String getTitle();

	public void setTitle(String title);

	public String getType();

	public String getShortDescription();

	public boolean isStopped();

	public void setStopped(boolean value);

	public Long getDeadline();

	public void setDeadline(Long deadline);

	/**
	 * Evaluates whether this task is currently active depending on
	 * the stopped flag and the deadline.
	 * @return
	 */
	public boolean isActive();

	/**
	 * Denotes whether this task should be visible in the list that shows available
	 * tasks to the student.
	 * @return
	 */
	public boolean isVisible();

	/**
	 * Denotes the taskId of the task that is supposed to be executed after this task.
	 * @return taskId of following task or null if none is defined
	 */
	public Long getFollowingTaskId();

}
