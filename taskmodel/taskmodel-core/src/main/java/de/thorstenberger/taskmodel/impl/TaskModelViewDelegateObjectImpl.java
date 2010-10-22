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

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;

/**
 * @author Thorsten Berger
 *
 */
public class TaskModelViewDelegateObjectImpl implements TaskModelViewDelegateObject {

	final private String login;
	final private String userName;
	final private long taskId;
	final private TaskManager taskManager;
	final private String returnURL;
	final private long randomSeed;



	/**
	 * @param login
	 * @param taskDef
	 * @param tasklet
	 * @param userName
	 */
    public TaskModelViewDelegateObjectImpl(long taskId, TaskManager taskManager, String login, String userName, String returnURL) {
        this(taskId, taskManager, login, userName, returnURL, System.nanoTime());
    }

    /**
     * 
     * @param taskId
     * @param taskManager
     * @param login
     * @param userName
     * @param returnURL
     * @param randomSeed
     *            seed to use for all random generation of content
     */
	public TaskModelViewDelegateObjectImpl(long taskId, TaskManager taskManager, String login, String userName, String returnURL, long randomSeed) {
		this.taskId = taskId;
		this.taskManager = taskManager;
		this.login = login;
		this.userName = userName;
		this.returnURL = returnURL;
		this.randomSeed = randomSeed;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getLogin()
	 */
	public String getLogin() {
		return login;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getUserName()
	 */
	public String getUserName() {
		return userName;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getTaskDef()
	 */
	public TaskDef getTaskDef() throws TaskApiException{
		return taskManager.getTaskDef( taskId );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getTaskId()
	 */
	public long getTaskId() {
		return taskId;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getTasklet()
	 */
	public Tasklet getTasklet() throws TaskApiException{
		return taskManager.getTaskletContainer().getTasklet( taskId, login );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskModelViewDelegateObject#getReturnURL()
	 */
	public String getReturnURL() {
		return returnURL;
	}

    public long getRandomSeed() {
        return randomSeed;
    }

}
