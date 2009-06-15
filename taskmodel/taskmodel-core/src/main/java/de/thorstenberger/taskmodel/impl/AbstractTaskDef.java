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
package de.thorstenberger.taskmodel.impl;

import de.thorstenberger.taskmodel.TaskDef;

/**
 * @author Thorsten Berger
 *
 */
public abstract class AbstractTaskDef implements TaskDef {

	private long id;
	private String title;
	private String shortDescription;
	private boolean stopped;
	private Long deadline;
	private Long followingTaskId;
	protected boolean visible;

	/**
	 *
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param deadline
	 * @param stopped
	 * @param followingTaskId
	 */
	public AbstractTaskDef( long id, String title, String shortDescription, Long deadline, boolean stopped, Long followingTaskId, boolean visible ) {
		this.id = id;
		this.title = title;
		this.shortDescription = shortDescription;
		this.deadline = deadline;
		this.stopped = stopped;
		this.followingTaskId = followingTaskId;
		this.visible = visible;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getId()
	 */
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getTitle()
	 */
	public String getTitle() {
		return title;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getShortDescription()
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#setShortDescription()
	 */
	public void setShortDescription(String sd) {
		this.shortDescription=sd;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#isStopped()
	 */
	public boolean isStopped() {
		return stopped;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getDeadline()
	 */
	public Long getDeadline() {
		return deadline;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#isActive()
	 */
	public boolean isActive() {

		if( isStopped() )
			return false;

		Long deadline = getDeadline();
		if( deadline != null )
			return System.currentTimeMillis() < deadline;

		return true;
	}

	public boolean isVisible() {
		return visible;
	}


	public Long getFollowingTaskId() {
		return followingTaskId;
	}


	public void setDeadline(Long deadline) {
		this.deadline=deadline;
	}


	public void setStopped(boolean value) {
		this.stopped=value;
	}


	public void setTitle(String title) {
		this.title=title;
	}

}
