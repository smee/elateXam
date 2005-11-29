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
package de.thorstenberger.taskmodel.view.correction.tree;

import java.util.List;

import de.thorstenberger.taskmodel.view.tree.AbstractDataNode;
import de.thorstenberger.taskmodel.view.tree.DataNode;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubtaskletNode extends AbstractDataNode {

    private String virtualSubtaskNum;
    private String studentLogin;
    private long taskId;
    private boolean currentlySelected;

    /**
     *
     */
    public SubtaskletNode( String virtualSubtaskNum, String studentLogin, long taskId, boolean currentlySelected ) {
        this.virtualSubtaskNum = virtualSubtaskNum;
        this.studentLogin = studentLogin;
        this.taskId = taskId;
        this.currentlySelected = currentlySelected;
    }

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getName()
	 */
	public String getName() {
		return virtualSubtaskNum;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getSubNodes()
	 */
	public List<DataNode> getSubNodes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#isFolder()
	 */
	public boolean isFolder() {
		return false;
	}

	/**
	 * @return Returns the currentlySelected.
	 */
	public boolean isCurrentlySelected() {
		return currentlySelected;
	}

	/**
	 * @return Returns the studentLogin.
	 */
	public String getStudentLogin() {
		return studentLogin;
	}

	/**
	 * @return Returns the taskId.
	 */
	public long getTaskId() {
		return taskId;
	}

	/**
	 * @return Returns the virtualSubtaskNum.
	 */
	public String getVirtualSubtaskNum() {
		return virtualSubtaskNum;
	}



}
