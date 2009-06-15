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

import de.thorstenberger.taskmodel.TaskManager.UserAttribute;


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

	public TaskCategory getCategory(long id);

	public List<TaskCategory> getCategories();

	public List<TaskCategory> getCategories( CategoryFilter categoryFilter );

	public void storeTaskCategory( TaskCategory category );

	public void deleteTaskCategory( long id ) throws MethodNotSupportedException;

//	public List<TaskDef> getTaskDefsByCategory( TaskCategory taskCategory );

	public TaskDef getTaskDef( long taskId );

	public List<TaskDef> getTaskDefs();

	public List<TaskDef> getTaskDefs( TaskFilter filter ) throws TaskFilterException;

	public void storeTaskDef( TaskDef taskDef, long taskCategoryId ) throws TaskApiException;

	public void deleteTaskDef(long id) throws MethodNotSupportedException;
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

	/**
	 *
	 * @param login
	 * @return the UserInfo instance or null if no such user exists
	 */
	public UserInfo getUserInfo( String login );
	
	/**
	 * This method should return a list of UserInfo objects that determine all
	 * correctors/tutors that are able to manually correct Tasklets. Needed when
	 * assigning Tasklets to correctors etc.
	 * @return
	 */
	public List<UserInfo> getCorrectors();
	
	/**
	 * Users can have several user attributes being provided by the host system.
	 * @return list of user attributes in P3P notation that will be provided by this TaskFactory implementation 
	 */
	public List<UserAttribute> availableUserAttributes();
	

}
