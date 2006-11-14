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

import de.thorstenberger.taskmodel.view.SubTaskletInfoVO;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectionInfoVO {

	private String userId;
	private String userName;
	private boolean unregisteredUser;
	private boolean userNameInvisible;
	private long taskId;
	private String status;
	private String points;
	private String correctorLogin;
	private List<String> correctorHistory;
	private String annotation;
	private int numOfTry;
	private String tryStartTime;
	
	private List<AnnotationInfoVO> acknowledgedAnnotations;
	private List<AnnotationInfoVO> nonAcknowledgedAnnotations;
	private boolean canAcknowledge;
	
	private SubTaskletInfoVO subTasklet;

	private Map<String, String> loginAndTaskId;
	
	/**
	 * @return the unregisteredUser
	 */
	public boolean isUnregisteredUser() {
		return unregisteredUser;
	}
	/**
	 * @param unregisteredUser the unregisteredUser to set
	 */
	public void setUnregisteredUser(boolean unregisteredUser) {
		this.unregisteredUser = unregisteredUser;
	}
	/**
	 * @return the userNameInvisible
	 */
	public boolean isUserNameInvisible() {
		return userNameInvisible;
	}
	/**
	 * @param userNameInvisible the userNameInvisible to set
	 */
	public void setUserNameInvisible(boolean userNameInvisible) {
		this.userNameInvisible = userNameInvisible;
	}
	/**
	 * @return the tryStartTime
	 */
	public String getTryStartTime() {
		return tryStartTime;
	}
	/**
	 * @param tryStartTime the tryStartTime to set
	 */
	public void setTryStartTime(String tryStartTime) {
		this.tryStartTime = tryStartTime;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the canAcknowledge.
	 */
	public boolean isCanAcknowledge() {
		return canAcknowledge;
	}
	/**
	 * @param canAcknowledge The canAcknowledge to set.
	 */
	public void setCanAcknowledge(boolean canAcknowledge) {
		this.canAcknowledge = canAcknowledge;
	}
	/**
	 * @return Returns the acknowledgedAnnotations.
	 */
	public List<AnnotationInfoVO> getAcknowledgedAnnotations() {
		return acknowledgedAnnotations;
	}
	/**
	 * @param acknowledgedAnnotations The acknowledgedAnnotations to set.
	 */
	public void setAcknowledgedAnnotations(
			List<AnnotationInfoVO> acknowledgedAnnotations) {
		this.acknowledgedAnnotations = acknowledgedAnnotations;
	}
	/**
	 * @return Returns the nonAcknowledgedAnnotations.
	 */
	public List<AnnotationInfoVO> getNonAcknowledgedAnnotations() {
		return nonAcknowledgedAnnotations;
	}
	/**
	 * @param nonAcknowledgedAnnotations The nonAcknowledgedAnnotations to set.
	 */
	public void setNonAcknowledgedAnnotations(
			List<AnnotationInfoVO> nonAcknowledgedAnnotations) {
		this.nonAcknowledgedAnnotations = nonAcknowledgedAnnotations;
	}
	/**
	 * @return Returns the numOfTry.
	 */
	public int getNumOfTry() {
		return numOfTry;
	}
	/**
	 * @param numOfTry The numOfTry to set.
	 */
	public void setNumOfTry(int numOfTry) {
		this.numOfTry = numOfTry;
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
	/**
	 * @return Returns the annotation.
	 */
	public String getAnnotation() {
		return annotation;
	}
	/**
	 * @param annotation The annotation to set.
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public class AnnotationInfoVO{
		
		private String date;
		private String text;
		
	/**
	 * @param date
	 * @param text
	 */
	public AnnotationInfoVO(String date, String text) {
			super();
			// TODO Auto-generated constructor stub
			this.date = date;
			this.text = text;
		}
		/**
		 * @return Returns the date.
		 */
		public String getDate() {
			return date;
		}
		/**
		 * @param date The date to set.
		 */
		public void setDate(String date) {
			this.date = date;
		}
		/**
		 * @return Returns the text.
		 */
		public String getText() {
			return text;
		}
		/**
		 * @param text The text to set.
		 */
		public void setText(String text) {
			this.text = text;
		}
		
	}
	
	/**
	 * @return Returns the loginAndTaskId.
	 */
	public Map getLoginAndTaskId() {
		if( loginAndTaskId == null ){
			loginAndTaskId = new HashMap<String, String>();
			loginAndTaskId.put( "taskId", "" + getTaskId() );
			loginAndTaskId.put( "userId", getUserId() );
		}
		return loginAndTaskId;
	}
	

}
