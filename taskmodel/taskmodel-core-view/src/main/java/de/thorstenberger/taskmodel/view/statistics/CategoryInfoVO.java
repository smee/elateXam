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

import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public class CategoryInfoVO {

	private String title;
	private boolean ignoreOrderOfBlocks;
	private String id;
	private boolean mixAllSubTasks;
	private Integer tasksPerPage;
	private List<BlockInfoVO> blocks;
	
	/**
	 * @return the blocks
	 */
	public List<BlockInfoVO> getBlocks() {
		return blocks;
	}
	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(List<BlockInfoVO> block) {
		this.blocks = block;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the ignoreOrderOfBlocks
	 */
	public boolean isIgnoreOrderOfBlocks() {
		return ignoreOrderOfBlocks;
	}
	/**
	 * @param ignoreOrderOfBlocks the ignoreOrderOfBlocks to set
	 */
	public void setIgnoreOrderOfBlocks(boolean ignoreOrderOfBlocks) {
		this.ignoreOrderOfBlocks = ignoreOrderOfBlocks;
	}
	/**
	 * @return the mixAllSubTasks
	 */
	public boolean isMixAllSubTasks() {
		return mixAllSubTasks;
	}
	/**
	 * @param mixAllSubTasks the mixAllSubTasks to set
	 */
	public void setMixAllSubTasks(boolean mixAllSubTasks) {
		this.mixAllSubTasks = mixAllSubTasks;
	}
	/**
	 * @return the tasksPerPage
	 */
	public Integer getTasksPerPage() {
		return tasksPerPage;
	}
	/**
	 * @param tasksPerPage the tasksPerPage to set
	 */
	public void setTasksPerPage(Integer tasksPerPage) {
		this.tasksPerPage = tasksPerPage;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	

}
