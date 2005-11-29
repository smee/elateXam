/*

Copyright (C) 2009 Steffen Dienst

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
package de.thorstenberger.examServer.webapp.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steffen Dienst
 *
 */
public class ActiveUserVO {
	private String username;
	private String taskTitle;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private HashMap<String, String> loginAndTaskId;
	
	public String getTaskTitle() {
  	return taskTitle;
  }
	public void setTaskTitle(String taskTitle) {
  	this.taskTitle = taskTitle;
  }
	public String getUsername() {
  	return username;
  }
	public void setUsername(String username) {
  	this.username = username;
  }
	public String getTaskId() {
  	return taskId;
  }
	public void setTaskId(String taskId) {
  	this.taskId = taskId;
  }
	public String getRemainingMinutes() {
  	return remainingMinutes;
  }
	public void setRemainingMinutes(String minutes) {
  	this.remainingMinutes = minutes;
  }
	public Map getLoginAndTaskId() {
		if( loginAndTaskId == null ){
			loginAndTaskId = new HashMap<String, String>();
			loginAndTaskId.put( "taskId", "" + getTaskId() );
			loginAndTaskId.put( "userId", getUsername() );
		}
		return loginAndTaskId;
	}
	private String taskId;
	private String remainingMinutes;
}
