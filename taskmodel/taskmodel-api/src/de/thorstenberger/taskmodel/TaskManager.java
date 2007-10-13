/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel;

import java.util.List;
import java.util.Locale;



/**
 * @author Thorsten Berger
 *
 */
public interface TaskManager {

	public String getDescription();

	public List<TaskCategory> getCategories() throws MethodNotSupportedException;

	public TaskCategory getCategory( long id ) throws MethodNotSupportedException;

	public List<TaskCategory> getCategories( CategoryFilter categoryFilter ) throws MethodNotSupportedException;

	public void storeTaskCategory( TaskCategory category ) throws MethodNotSupportedException;

	public void deleteCategory(long id_long) throws MethodNotSupportedException;

	public List<TaskDef> getTaskDefs() throws MethodNotSupportedException;

	public List<TaskDef> getTaskDefs( TaskFilter filter ) throws TaskFilterException, MethodNotSupportedException;

	public TaskDef getTaskDef( long id ) throws MethodNotSupportedException;

	public TaskDef getTaskDef( TaskFilter filter, long id ) throws TaskFilterException, MethodNotSupportedException;

	public void storeTaskDef( TaskDef taskDef, long taskCategoryId ) throws TaskApiException;

	public void deleteTaskDef( long id) throws MethodNotSupportedException;

	public TaskletContainer getTaskletContainer();

	public ReportBuilder getReportBuilder();

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
	
	public interface UserAttribute{
		
		public String getKey();
		
		public String getName( Locale locale );
		
	}

}
