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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.Tasklet;

/**
 * @author Thorsten Berger
 *
 */
class UserObject implements Serializable{

	private static final long serialVersionUID = 89234759269256921l;
	
	private String userId;
	private Map<Long, Tasklet> tasklets;
	private TaskFactory taskFactory;
	
	/**
	 * 
	 */
	public UserObject( String userId, TaskFactory taskFactory ) {
		this.userId = userId;
		this.taskFactory = taskFactory;
		tasklets = new HashMap<Long, Tasklet>();
	}
	
	synchronized Tasklet getTasklet( long id, boolean create ) throws TaskApiException, TaskModelPersistenceException{
		if( tasklets.containsKey( id ) ){
			return tasklets.get( id );
		}else{
			Tasklet tasklet = taskFactory.getTasklet( getUserId(), id );
			
			if( tasklet == null && !create )
				return null;
			
			if( tasklet == null && create ){
				tasklet = taskFactory.createTasklet( getUserId(), id );
				checkInitState( tasklet );
			}
				tasklets.put( id, tasklet );
				
			return tasklet;
		}
	}

	private void checkInitState( Tasklet tasklet ) throws TaskModelPersistenceException{
		if( tasklet == null )
			throw new TaskModelPersistenceException(
					new NullPointerException( "TaskFactory created a null Tasklet reference!" ) );
		
		if( !tasklet.getStatus().equals( Tasklet.INITIALIZED ) )
			throw new TaskModelPersistenceException(
					new IllegalStateException( "TaskFactory created a Tasklet with status \"" + tasklet.getStatus() +
							"\" instead of \"" + Tasklet.INITIALIZED + "\"!" ) );
		
		if( tasklet.getTaskletCorrection() == null )
			throw new TaskModelPersistenceException(
					new NullPointerException( "TaskFactory created a Tasklet with a null TaskletCorrection reference!" ) );
		
		if( tasklet.getTaskletCorrection().getPoints() != null )
			throw new TaskModelPersistenceException(
					new IllegalStateException( "TaskFactory created a Tasklet with points assigned!" ) );
	}
	
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	
	

}
