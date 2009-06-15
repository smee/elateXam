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

import de.thorstenberger.taskmodel.view.tree.DataNode;

/**
 * @author Thorsten Berger
 *
 */
public class PageNode implements DataNode {

	private int pageNumber;
	private long taskId;
	private boolean currentlyActivePage;
	private int processStatus;
	
	/**
	 * 
	 */
	public PageNode( int pageNumber, long taskId, boolean currentlyActivePage, int processStatus ) {
		this.pageNumber = pageNumber;
		this.taskId = taskId;
		this.currentlyActivePage = currentlyActivePage;
		this.processStatus = processStatus;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getName()
	 */
	public String getName() {
		return "Page " + pageNumber;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#isFolder()
	 */
	public boolean isFolder() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getSubNodes()
	 */
	public List<DataNode> getSubNodes() {
		return null;
	}

	/**
	 * @return Returns the currentlyActivePage.
	 */
	public boolean isCurrentlyActivePage() {
		return currentlyActivePage;
	}

	/**
	 * @param currentlyActivePage The currentlyActivePage to set.
	 */
	public void setCurrentlyActivePage(boolean currentlyActivePage) {
		this.currentlyActivePage = currentlyActivePage;
	}

	/**
	 * @return Returns the pageNumber.
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber The pageNumber to set.
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return Returns the processStatus.
	 */
	public int getProcessStatus() {
		return processStatus;
	}

	/**
	 * @param processStatus The processStatus to set.
	 */
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
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
	
	

}
