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
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;

/**
 * @author Thorsten Berger
 *
 */
public abstract class AbstractTasklet implements Tasklet {

	private TaskFactory taskFactory;
	private String userId;
	private long taskId;
	protected String status;
	protected TaskletCorrection taskletCorrection;
	
	/**
	 * 
	 */
	public AbstractTasklet( TaskFactory taskFactory, String userId, long taskId, String status, TaskletCorrection taskletCorrection ) {
		this.taskFactory = taskFactory;
		this.userId = userId;
		this.taskId = taskId;
		this.status = status;
		this.taskletCorrection = taskletCorrection;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getUserId()
	 */
	public String getUserId() {
		return userId;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getTaskId()
	 */
	public long getTaskId() {
		return taskId;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getStatus()
	 */
	public synchronized String getStatus() {
		return status;
	}
	
	protected synchronized void setStatus( String status ){
		if( INITIALIZED.equals( status ) ||
				INPROGRESS.equals( status ) ||
				SOLVED.equals( status ) ||
				CORRECTING.equals( status ) ||
				CORRECTED.equals( status ) ){
			
			this.status = status;
			
		}else{
			
			throw new IllegalArgumentException( "invalid status" );
			
		}
	}
	
	protected synchronized void save() throws TaskApiException{
		taskFactory.storeTasklet( this );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getTaskletCorrection()
	 */
	public synchronized TaskletCorrection getTaskletCorrection() {
		return taskletCorrection;
	}

}
