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
package de.thorstenberger.taskmodel.upload;

import de.thorstenberger.taskmodel.impl.AbstractTaskDef;


/**
 * TODO implementation deferred
 * @author Thorsten Berger
 *
 */
public class TaskDef_UploadImpl extends AbstractTaskDef implements TaskDef_Upload {


	/**
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param description
	 * @param deadline
	 * @param stopped
	 */
	public TaskDef_UploadImpl(long id, String title, String shortDescription, Long deadline, boolean stopped) {
		super(id, title, shortDescription, deadline, stopped);
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getType()
	 */
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getProblem(){
		return null;
	}
	
	public String getProblemResource(){
		return null;
	}
	
	public int maxUploadableFiles(){
		return 0;
	}

}
