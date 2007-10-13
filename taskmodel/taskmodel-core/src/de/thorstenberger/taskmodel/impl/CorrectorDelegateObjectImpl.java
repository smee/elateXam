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
package de.thorstenberger.taskmodel.impl;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectorDelegateObjectImpl implements CorrectorDelegateObject {

	private String correctorLogin;
	private String correctorUserName;
	private TaskManager taskManager;
	private TaskDef taskDef;
	private boolean privileged;
	private String returnURL;

	/**
	 * @param correctorLogin
	 * @param correctorUserName
	 * @param taskManager
	 * @param taskDef
	 * @param returnURL
	 */
	public CorrectorDelegateObjectImpl(String correctorLogin, String correctorUserName, TaskManager taskManager, TaskDef taskDef, boolean privileged, String returnURL) {
		this.correctorLogin = correctorLogin;
		this.correctorUserName = correctorUserName;
		this.taskManager = taskManager;
		this.taskDef = taskDef;
		this.privileged = privileged;
		this.returnURL = returnURL;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorDelegateObject#getCorrectorLogin()
	 */
	public String getCorrectorLogin() {
		return correctorLogin;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorDelegateObject#getCorrectorUserName()
	 */
	public String getCorrectorUserName() {
		return correctorUserName;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorDelegateObject#getTaskManager()
	 */
	public TaskManager getTaskManager() {
		return taskManager;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorDelegateObject#getTaskDef()
	 */
	public TaskDef getTaskDef() throws TaskApiException {
		return taskDef;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.DelegateObject#getTaskId()
	 */
	public long getTaskId() {
		return taskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorDelegateObject#isPrivileged()
	 */
	public boolean isPrivileged() {
		return privileged;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.DelegateObject#getReturnURL()
	 */
	public String getReturnURL() {
		return returnURL;
	}

}
