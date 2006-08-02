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

import java.util.List;

/**
 * 
 * TaskFactory is responsible for retrieving and saving TaskDefs and Tasklets depending on a
 * specific server and database implementation.
 * 
 * @author Thorsten Berger
 *
 */
public interface TaskFactory {

	/**
	 * 
	 * @return List of Task Types this TaskFactory can instantiate
	 */
	public List<String> availableTypes();
	
	public List<TaskCategory> getCategories();
	
	public List<TaskCategory> getCategories( CategoryFilter categoryFilter );
	
//	public List<TaskDef> getTaskDefsByCategory( TaskCategory taskCategory );
	
	public TaskDef getTaskDef( long taskId );
	
	public List<TaskDef> getTaskDefs();
	
	public List<TaskDef> getTaskDefs( TaskFilter filter ) throws TaskFilterException;
	
	/**
	 * 
	 * @param userId
	 * @param taskId
	 * @return the instantiated tasklet or null if not existant
	 */
	public Tasklet getTasklet( String userId, long taskId );
	
	public List<Tasklet> getTasklets( long taskId );
	
	public List<String> getUserIdsOfAvailableTasklets( long taskId );
	
	public Tasklet createTasklet( String userId, long taskId ) throws TaskApiException;
	
	public void storeTasklet( Tasklet tasklet ) throws TaskApiException;
	
	public void removeTasklet( String userId, long taskId ) throws TaskApiException;
	
	public void logPostData( String msg, Tasklet tasklet, String ip );
	
	public void logPostData( String msg, Throwable throwable, Tasklet tasklet, String ip );
	
//	public List<Tasklet> getTaskletsAssignedToCorrector( long taskId, String correctorId, boolean corrected );
	public List<String> getUserIdsOfTaskletsAssignedToCorrector( long taskId, String correctorId );

}
