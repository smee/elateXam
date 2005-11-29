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
package de.thorstenberger.examServer.webapp.vo;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskInfoVO extends TaskInfoVO{

	private String specificDescription;
	private Integer time;
	private int usedTries;
	private Integer maxTries;
	private String startText;
	private boolean canStartNewTry;
	private boolean canContinueTry;
	private String ctDeadline;
	private boolean correctionVisible;
	private boolean showHandlingHintsBeforeStart;
	
	/**
	 * @return Returns the maxTries.
	 */
	public Integer getMaxTries() {
		return maxTries;
	}
	/**
	 * @param maxTries The maxTries to set.
	 */
	public void setMaxTries(Integer maxTries) {
		this.maxTries = maxTries;
	}
	/**
	 * @return Returns the specificDescription.
	 */
	public String getSpecificDescription() {
		return specificDescription;
	}
	/**
	 * @param specificDescription The specificDescription to set.
	 */
	public void setSpecificDescription(String specificDescription) {
		this.specificDescription = specificDescription;
	}
	/**
	 * @return Returns the startText.
	 */
	public String getStartText() {
		return startText;
	}
	/**
	 * @param startText The startText to set.
	 */
	public void setStartText(String startText) {
		this.startText = startText;
	}
	/**
	 * @return Returns the time.
	 */
	public Integer getTime() {
		return time;
	}
	/**
	 * @param time The time to set.
	 */
	public void setTime(Integer time) {
		this.time = time;
	}
	/**
	 * @return Returns the usedTries.
	 */
	public int getUsedTries() {
		return usedTries;
	}
	/**
	 * @param usedTries The usedTries to set.
	 */
	public void setUsedTries(int usedTries) {
		this.usedTries = usedTries;
	}
	/**
	 * @return Returns the canStartNewTry.
	 */
	public boolean isCanStartNewTry() {
		return canStartNewTry;
	}
	/**
	 * @param canStartNewTry The canStartNewTry to set.
	 */
	public void setCanStartNewTry(boolean canStartNewTry) {
		this.canStartNewTry = canStartNewTry;
	}
	/**
	 * @return Returns the canContinueTry.
	 */
	public boolean isCanContinueTry() {
		return canContinueTry;
	}
	/**
	 * @param canContinueTry The canContinueTry to set.
	 */
	public void setCanContinueTry(boolean canContinueTry) {
		this.canContinueTry = canContinueTry;
	}
	/**
	 * @return Returns the ctDeadline.
	 */
	public String getCtDeadline() {
		return ctDeadline;
	}
	/**
	 * @param ctDeadline The ctDeadline to set.
	 */
	public void setCtDeadline(String ctDeadline) {
		this.ctDeadline = ctDeadline;
	}
	/**
	 * @return Returns the canSeeCorrection.
	 */
	public boolean isCorrectionVisible() {
		return correctionVisible;
	}
	/**
	 * @param canSeeCorrection The canSeeCorrection to set.
	 */
	public void setCorrectionVisible(boolean canSeeCorrection) {
		this.correctionVisible = canSeeCorrection;
	}
	/**
	 * @return the showHandlingHintsBeforeStart
	 */
	public boolean isShowHandlingHintsBeforeStart() {
		return showHandlingHintsBeforeStart;
	}
	/**
	 * @param showHandlingHintsBeforeStart the showHandlingHintsBeforeStart to set
	 */
	public void setShowHandlingHintsBeforeStart(boolean showHandlingHintsBeforeStart) {
		this.showHandlingHintsBeforeStart = showHandlingHintsBeforeStart;
	}
	
	
	
	
	

}
