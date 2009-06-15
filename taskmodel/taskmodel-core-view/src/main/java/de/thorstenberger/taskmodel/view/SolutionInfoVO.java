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
package de.thorstenberger.taskmodel.view;

import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public class SolutionInfoVO {

	private String login;
	private String userName;
	private String status;
	private String tryStartTime;
	
	private long taskId;
	
	private boolean canAnnotate;
	private String actualAnnotation;
	private List<AnnotationInfoVO> annotations;
	private List<Correction> corrections;
	
	
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
	 * @return Returns the actualAnnotation.
	 */
	public String getActualAnnotation() {
		return actualAnnotation;
	}
	/**
	 * @param actualAnnotation The actualAnnotation to set.
	 */
	public void setActualAnnotation(String actualAnnotation) {
		this.actualAnnotation = actualAnnotation;
	}
	/**
	 * @return Returns the annotations.
	 */
	public List<AnnotationInfoVO> getAnnotations() {
		return annotations;
	}
	/**
	 * @param annotations The annotations to set.
	 */
	public void setAnnotations(List<AnnotationInfoVO> annotations) {
		this.annotations = annotations;
	}
	/**
	 * @return Returns the canAnnotate.
	 */
	public boolean isCanAnnotate() {
		return canAnnotate;
	}
	/**
	 * @param canAnnotate The canAnnotate to set.
	 */
	public void setCanAnnotate(boolean canAnnotate) {
		this.canAnnotate = canAnnotate;
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
	 * @return Returns the tryStartTime.
	 */
	public String getTryStartTime() {
		return tryStartTime;
	}
	/**
	 * @param tryStartTime The tryStartTime to set.
	 */
	public void setTryStartTime(String tryStartTime) {
		this.tryStartTime = tryStartTime;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	public boolean isHaveAnnotations(){
		return annotations != null && annotations.size() > 0;
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
	
public class Correction{
		
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
