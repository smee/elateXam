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

import java.io.IOException;
import java.io.InputStream;

import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.impl.AbstractTaskDef;


/**
 * 
 * @author Thorsten Berger
 *
 */
public class TaskDef_UploadImpl extends AbstractTaskDef implements TaskDef_Upload {

	private String problem;
	private int maxUploadableFiles;
	private InputStream resourceInputStream;
	private String resourceFilename;
	private String resourceMimeType;

	/**
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param description
	 * @param deadline
	 * @param stopped
	 */
	public TaskDef_UploadImpl(long id, String title, String shortDescription, Long deadline,
			boolean stopped, Long followingTaskId, boolean visible, String problem, int maxUploadableFiles,
			InputStream resourceInputStream, String resourceFilename, String resourceMimeType ) {
		
		super(id, title, shortDescription, deadline, stopped, followingTaskId, visible);
		
		this.problem = problem;
		this.maxUploadableFiles = maxUploadableFiles;
		this.resourceInputStream = resourceInputStream;
		this.resourceFilename = resourceFilename;
		this.resourceMimeType = resourceMimeType;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getType()
	 */
	public String getType() {
		return TaskContants.TYPE_UPLOAD;
	}
	
	
	public String getProblem(){
		return problem;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.TaskDef_Upload#hasAttachedResource()
	 */
	public boolean hasAttachedResource() {
		try {
			return resourceInputStream != null && resourceInputStream.available() > 0 && resourceFilename != null;
		} catch (IOException e) {
			throw new TaskModelPersistenceException( e );
		}
	}


	public String getResourceFilename(){
		if( resourceFilename == null || resourceFilename.length() == 0 )
			return "task_" + getId();
		return resourceFilename;
	}
	
	public int maxUploadableFiles(){
		return maxUploadableFiles;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.TaskDef_Upload#getResourceAsStream()
	 */
	public InputStream getResourceAsStream() {
		return resourceInputStream;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.TaskDef_Upload#getResourceMimeType()
	 */
	public String getResourceMimeType() {
		return resourceMimeType;
	}


  /* (non-Javadoc)
   * @see de.thorstenberger.taskmodel.TaskDef#reachablePoints()
   */
  public float getReachablePoints() {
    // TODO 
    return 0;
  }
	
	
	

}
