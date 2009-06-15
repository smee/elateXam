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

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskInfoVO {

	private String title;
	private long taskId;
	
	private String login;
	private String userName;
	private String returnURL;
	
	private long remainingTimeMillis;
	private boolean timeRestricted;
	private boolean everythingProcessed;
	
	private int page;
	private int numOfPages;
	private int actualTry;
	private int numOfTries;
	private String tryStartTime;
	private String processPercentage;
	private String deadline;
	private String hashCode;
	private int numOfSubtasklets;
	private int numOfProcessedSubtasklets;
	
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
	 * @return Returns the returnURL.
	 */
	public String getReturnURL() {
		return returnURL;
	}
	/**
	 * @param returnURL The returnURL to set.
	 */
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return Returns the remainingTimeMillis.
	 */
	public long getRemainingTimeMillis() {
		return remainingTimeMillis;
	}
	/**
	 * @param remainingTimeMillis The remainingTimeMillis to set.
	 */
	public void setRemainingTimeMillis(long remainingTimeMillis) {
		this.remainingTimeMillis = remainingTimeMillis;
	}
	/**
	 * @return Returns the everythingProcessed.
	 */
	public boolean isEverythingProcessed() {
		return everythingProcessed;
	}
	/**
	 * @param everythingProcessed The everythingProcessed to set.
	 */
	public void setEverythingProcessed(boolean everythingProcessed) {
		this.everythingProcessed = everythingProcessed;
	}
	/**
	 * @return Returns the timeRestricted.
	 */
	public boolean isTimeRestricted() {
		return timeRestricted;
	}
	/**
	 * @param timeRestricted The timeRestricted to set.
	 */
	public void setTimeRestricted(boolean timeRestricted) {
		this.timeRestricted = timeRestricted;
	}
	/**
	 * @return Returns the numOfPages.
	 */
	public int getNumOfPages() {
		return numOfPages;
	}
	/**
	 * @param numOfPages The numOfPages to set.
	 */
	public void setNumOfPages(int numOfPages) {
		this.numOfPages = numOfPages;
	}
	/**
	 * @return Returns the page.
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page The page to set.
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return Returns the actualTry.
	 */
	public int getActualTry() {
		return actualTry;
	}
	/**
	 * @param actualTry The actualTry to set.
	 */
	public void setActualTry(int actualTry) {
		this.actualTry = actualTry;
	}
	/**
	 * @return Returns the numOfTries.
	 */
	public int getNumOfTries() {
		return numOfTries;
	}
	/**
	 * @param numOfTries The numOfTries to set.
	 */
	public void setNumOfTries(int numOfTries) {
		this.numOfTries = numOfTries;
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
	 * @return Returns the processPercentage.
	 */
	public String getProcessPercentage() {
		return processPercentage;
	}
	/**
	 * @param processPercentage The processPercentage to set.
	 */
	public void setProcessPercentage(String processPercentage) {
		this.processPercentage = processPercentage;
	}
	/**
	 * @return Returns the deadline.
	 */
	public String getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline The deadline to set.
	 */
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return Returns the hashCode.
	 */
	public String getHashCode() {
		return hashCode;
	}
	/**
	 * @param hashCode The hashCode to set.
	 */
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
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
	 * @return the numOfProcessedSubtasklets
	 */
	public int getNumOfProcessedSubtasklets() {
		return numOfProcessedSubtasklets;
	}
	/**
	 * @param numOfProcessedSubtasklets the numOfProcessedSubtasklets to set
	 */
	public void setNumOfProcessedSubtasklets(int numOfProcessedSubtasklets) {
		this.numOfProcessedSubtasklets = numOfProcessedSubtasklets;
	}
	/**
	 * @return the numOfSubtasklets
	 */
	public int getNumOfSubtasklets() {
		return numOfSubtasklets;
	}
	/**
	 * @param numOfSubtasklets the numOfSubtasklets to set
	 */
	public void setNumOfSubtasklets(int numOfSubtasklets) {
		this.numOfSubtasklets = numOfSubtasklets;
	}
	
	
	

}
