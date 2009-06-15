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
package de.thorstenberger.taskmodel.view.statistics;


/**
 * @author Thorsten Berger
 *
 */
public class BlockInfoVO {
	
	private int numberOfSelectedSubTasks;
	private boolean preserveOrder;
	private float pointsPerSubTask;
	private String type;
	private int index;
	
	
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the numberOfSelectedSubTasks
	 */
	public int getNumberOfSelectedSubTasks() {
		return numberOfSelectedSubTasks;
	}
	/**
	 * @param numberOfSelectedSubTasks the numberOfSelectedSubTasks to set
	 */
	public void setNumberOfSelectedSubTasks(int numberOfSelectedSubTasks) {
		this.numberOfSelectedSubTasks = numberOfSelectedSubTasks;
	}
	/**
	 * @return the pointsPerSubTask
	 */
	public float getPointsPerSubTask() {
		return pointsPerSubTask;
	}
	/**
	 * @param pointsPerSubTask the pointsPerSubTask to set
	 */
	public void setPointsPerSubTask(float pointsPerSubTask) {
		this.pointsPerSubTask = pointsPerSubTask;
	}
	/**
	 * @return the preserveOrder
	 */
	public boolean isPreserveOrder() {
		return preserveOrder;
	}
	/**
	 * @param preserveOrder the preserveOrder to set
	 */
	public void setPreserveOrder(boolean preserveOrder) {
		this.preserveOrder = preserveOrder;
	}

	
}
