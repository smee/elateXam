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

import java.util.List;

import de.thorstenberger.taskmodel.view.SubTaskletInfoVO;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectionInfoVO {

	private String userId;
	private long taskId;
	private String status;
	private String points;
	private String correctorLogin;
	private List<String> correctorHistory;
	
	private SubTaskletInfoVO subTasklet;
	
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
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the corrector.
	 */
	public String getCorrectorLogin() {
		return correctorLogin;
	}
	/**
	 * @param corrector The corrector to set.
	 */
	public void setCorrectorLogin(String corrector) {
		this.correctorLogin = corrector;
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
	 * @return Returns the subTasklet.
	 */
	public SubTaskletInfoVO getSubTasklet() {
		return subTasklet;
	}
	/**
	 * @param subTasklet The subTasklet to set.
	 */
	public void setSubTasklet(SubTaskletInfoVO subTasklet) {
		this.subTasklet = subTasklet;
	}
	
	
	

}
