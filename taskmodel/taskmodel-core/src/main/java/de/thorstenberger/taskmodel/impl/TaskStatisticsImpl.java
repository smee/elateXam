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
package de.thorstenberger.taskmodel.impl;

import de.thorstenberger.taskmodel.TaskStatistics;

/**
 * @author Thorsten Berger
 *
 */
public class TaskStatisticsImpl implements TaskStatistics {

	private long taskId;
	private int numOfSolutions;
	private int numOfCorrectedSolutions;
	private int numOfAssignedSolutions;

	
	
	/**
	 * @param taskId
	 * @param numOfSolutions
	 * @param numOfCorrectedSolutions
	 * @param numOfAssignedSolutions
	 */
	public TaskStatisticsImpl(long taskId, int numOfSolutions, int numOfCorrectedSolutions, int numOfAssignedSolutions) {
		this.taskId = taskId;
		this.numOfSolutions = numOfSolutions;
		this.numOfCorrectedSolutions = numOfCorrectedSolutions;
		this.numOfAssignedSolutions = numOfAssignedSolutions;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskStatistics#getNumOfCorrectedSolutions()
	 */
	public int getNumOfCorrectedSolutions() {
		return numOfCorrectedSolutions;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskStatistics#getNumOfSolutions()
	 */
	public int getNumOfSolutions() {
		return numOfSolutions;
	}
	

	/**
	 * @return Returns the numOfAssignedSolutions.
	 */
	public int getNumOfAssignedSolutions() {
		return numOfAssignedSolutions;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskStatistics#getTaskId()
	 */
	public long getTaskId() {
		return taskId;
	}

}
