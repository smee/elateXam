/*

Copyright (C) 2006 Thorsten Berger

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
package de.thorstenberger.taskmodel.view.correction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thorsten Berger
 *
 */
public class TaskletInfoVO {

	private long taskId;
	private String login;
	private String status;
	private String points;
	private String correctorLogin;
	private List<String> correctorHistory;
	
	private Map<String, String> loginAndTaskId;
	
	private boolean corrigible;
	
	
	/**
	 * @return Returns the corrigible.
	 */
	public boolean isCorrigible() {
		return corrigible;
	}
	/**
	 * @param corrigible The corrigible to set.
	 */
	public void setCorrigible(boolean corrigible) {
		this.corrigible = corrigible;
	}
	/**
	 * @return Returns the taskId.
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId The taskId to set.
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return Returns the correctorHistory.
	 */
	public List<String> getCorrectorHistory() {
		return correctorHistory;
	}
	/**
	 * @param correctorHistory The correctorHistory to set.
	 */
	public void setCorrectorHistory(List<String> correctorHistory) {
		this.correctorHistory = correctorHistory;
	}
	/**
	 * @return Returns the correctorLogin.
	 */
	public String getCorrectorLogin() {
		return correctorLogin;
	}
	/**
	 * @param correctorLogin The correctorLogin to set.
	 */
	public void setCorrectorLogin(String correctorLogin) {
		this.correctorLogin = correctorLogin;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return Returns the points.
	 */
	public String getPoints() {
		return points;
	}
	/**
	 * @param points The points to set.
	 */
	public void setPoints(String points) {
		this.points = points;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the loginAndTaskId.
	 */
	public Map getLoginAndTaskId() {
		if( loginAndTaskId == null ){
			loginAndTaskId = new HashMap<String, String>();
			loginAndTaskId.put( "taskId", "" + getTaskId() );
			loginAndTaskId.put( "userId", getLogin() );
		}
		return loginAndTaskId;
	}
	
	
	
}
