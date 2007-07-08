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



/**
 * @author Thorsten Berger
 *
 */
public interface TaskManager {

	public String getDescription();
	
	public List<TaskCategory> getCategories() throws MethodNotSupportedException;
	
	public List<TaskCategory> getCategories( CategoryFilter categoryFilter ) throws MethodNotSupportedException;
	
	public List<TaskDef> getTaskDefs() throws MethodNotSupportedException;
	
	public List<TaskDef> getTaskDefs( TaskFilter filter ) throws TaskFilterException, MethodNotSupportedException;
	
	public TaskDef getTaskDef( long id ) throws MethodNotSupportedException;
	
	public TaskDef getTaskDef( TaskFilter filter, long id ) throws TaskFilterException, MethodNotSupportedException;
	
	public TaskletContainer getTaskletContainer();
	
	public TaskFactory getTaskFactory();
	
	public ReportBuilder getReportBuilder();

	public TaskCategory addTaskCategory( String name, String description );

	public void deleteCategory(long id_long) throws CategoryException;
	
}
