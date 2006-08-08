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

/**
 * @author Thorsten Berger
 *
 */
public class TutorSolutionsInfoVO {

	private long taskId;
	private int count;
	private int correctedCount;
	private String correctedCountPercent;
	private List<TaskletInfoVO> assignedUncorrectedTasklets;
	private List<TaskletInfoVO> assignedCorrectedTasklets;
	private int assignedCount;
	
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
	 * @return Returns the correctedCount.
	 */
	public int getCorrectedCount() {
		return correctedCount;
	}
	/**
	 * @param correctedCount The correctedCount to set.
	 */
	public void setCorrectedCount(int correctedCount) {
		this.correctedCount = correctedCount;
	}
	/**
	 * @return Returns the correctedCountPercent.
	 */
	public String getCorrectedCountPercent() {
		return correctedCountPercent;
	}
	/**
	 * @param correctedCountPercent The correctedCountPercent to set.
	 */
	public void setCorrectedCountPercent(String correctedCountPercent) {
		this.correctedCountPercent = correctedCountPercent;
	}
	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return Returns the assignedUncorrectedTasklets.
	 */
	public List<TaskletInfoVO> getAssignedUncorrectedTasklets() {
		return assignedUncorrectedTasklets;
	}
	/**
	 * @param assignedUncorrectedTasklets The assignedUncorrectedTasklets to set.
	 */
	public void setAssignedUncorrectedTasklets(
			List<TaskletInfoVO> assignedUncorrectedTasklets) {
		this.assignedUncorrectedTasklets = assignedUncorrectedTasklets;
	}
	/**
	 * @return Returns the assignedCorrectedTasklets.
	 */
	public List<TaskletInfoVO> getAssignedCorrectedTasklets() {
		return assignedCorrectedTasklets;
	}
	/**
	 * @param assignedCorrectedTasklets The assignedCorrectedTasklets to set.
	 */
	public void setAssignedCorrectedTasklets(
			List<TaskletInfoVO> assignedCorrectedTasklets) {
		this.assignedCorrectedTasklets = assignedCorrectedTasklets;
	}
	/**
	 * @return Returns the assignedCount.
	 */
	public int getAssignedCount() {
		return assignedCount;
	}
	/**
	 * @param assignedCount The assignedCount to set.
	 */
	public void setAssignedCount(int assignedCount) {
		this.assignedCount = assignedCount;
	}
	
	

}
