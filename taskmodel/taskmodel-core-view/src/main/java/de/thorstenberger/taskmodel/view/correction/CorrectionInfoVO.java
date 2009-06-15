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
	private String correctorLogin;
	private List<String> correctorHistory;
	private int numOfTry;
	private String tryStartTime;
	private List<Correction> corrections;
	
	private String currentCorrectorAnnotation;
	private List<CorrectorAnnotation> otherCorrectorAnnotations;
	private List<AnnotationInfoVO> acknowledgedAnnotations;
	private List<AnnotationInfoVO> nonAcknowledgedAnnotations;
	private boolean canAcknowledge;
	
	private SubTaskletInfoVO subTasklet;

	private Map<String, String> loginAndTaskId;
	
	private List<String> availableCorrectors;
	
	
	
	/**
	 * @return the availableCorrectors
	 */
	public List<String> getAvailableCorrectors() {
		return availableCorrectors;
	}
	/**
	 * @param availableCorrectors the availableCorrectors to set
	 */
	public void setAvailableCorrectors(List<String> availableCorrectors) {
		this.availableCorrectors = availableCorrectors;
	}
	/**
	 * @return the corrections
	 */
	public List<Correction> getCorrections() {
		return corrections;
	}
	/**
	 * @param corrections the corrections to set
	 */
	public void setCorrections(List<Correction> corrections) {
		this.corrections = corrections;
	}
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
	 * @return the otherCorrectorAnnotations
	 */
	public List<CorrectorAnnotation> getOtherCorrectorAnnotations() {
		return otherCorrectorAnnotations;
	}
	/**
	 * @param otherCorrectorAnnotations the otherCorrectorAnnotations to set
	 */
	public void setOtherCorrectorAnnotations(
			List<CorrectorAnnotation> correctorAnnotations) {
		this.otherCorrectorAnnotations = correctorAnnotations;
	}
	/**
	 * @return the currentCorrectorAnnotation
	 */
	public String getCurrentCorrectorAnnotation() {
		return currentCorrectorAnnotation;
	}
	/**
	 * @param currentCorrectorAnnotation the currentCorrectorAnnotation to set
	 */
	public void setCurrentCorrectorAnnotation(String currentCorrectorAnnotation) {
		this.currentCorrectorAnnotation = currentCorrectorAnnotation;
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
	
	public class CorrectorAnnotation{
		private String corrector;
		private String annotation;
		/**
		 * @param corrector
		 * @param annotation
		 */
		public CorrectorAnnotation(String corrector, String annotation) {
			super();
			this.corrector = corrector;
			this.annotation = annotation;
		}
		/**
		 * @return the annotation
		 */
		public String getAnnotation() {
			return annotation;
		}
		/**
		 * @param annotation the annotation to set
		 */
		public void setAnnotation(String annotation) {
			this.annotation = annotation;
		}
		/**
		 * @return the corrector
		 */
		public String getCorrector() {
			return corrector;
		}
		/**
		 * @param corrector the corrector to set
		 */
		public void setCorrector(String corrector) {
			this.corrector = corrector;
		}
		
	}
	
	public static class Correction{
		
		private String corrector;
		private boolean auto;
		private float points;
		/**
		 * @param corrector
		 * @param points
		 */
		public Correction(String corrector, boolean auto, float points) {
			this.corrector = corrector;
			this.auto = auto;
			this.points = points;
		}
		/**
		 * @return the corrector
		 */
		public String getCorrector() {
			return corrector;
		}
		/**
		 * @param corrector the corrector to set
		 */
		public void setCorrector(String corrector) {
			this.corrector = corrector;
		}
		/**
		 * @return the auto
		 */
		public boolean isAuto() {
			return auto;
		}
		/**
		 * @param auto the auto to set
		 */
		public void setAuto(boolean auto) {
			this.auto = auto;
		}
		/**
		 * @return the points
		 */
		public float getPoints() {
			return points;
		}
		/**
		 * @param points the points to set
		 */
		public void setPoints(float points) {
			this.points = points;
		}
		
	}

	

}
