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
package de.thorstenberger.taskmodel.view.correction.tree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.thorstenberger.taskmodel.view.tree.DataNode;
import de.thorstenberger.taskmodel.view.tree.DataNodeFormatter;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectionNodeFormatter implements DataNodeFormatter {

	private String path;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private long taskId;
	private String userId;
	
	public CorrectionNodeFormatter( long taskId, String userId, String path, HttpServletRequest request, HttpServletResponse response ){
		this.taskId = taskId;
		this.path = path;
		this.request = request;
		this.response = response;
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#format(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public String format(DataNode node) {
		if( node instanceof SubtaskletFolder ){
			SubtaskletFolder folder = (SubtaskletFolder)node;
			if( folder.getType() == SubtaskletFolder.Type.CORRECTED )
				return "korrigiert";
			else if( folder.getType() == SubtaskletFolder.Type.NEEDSMANUALCORRECTION )
				return "manuelle Korr. notw.";
			else
				return "nicht korrigiert";
		}else if( node instanceof SubtaskletNode ){
			SubtaskletNode stn = (SubtaskletNode)node;
			String url = response.encodeURL( path + "?taskId=" + taskId + "&userId=" + userId + "&selectedSubTaskletNum=" + stn.getVirtualSubtaskNum() );
			
			return "<a class=\"node\" href=\"" + url + "\">Aufgabe " + stn.getVirtualSubtaskNum() + "</a>" +
					( stn.isCurrentlySelected() ? "<img src=\"" + request.getContextPath() + "/pics/sparkle001bu.gif\">" : "");
			
		}else
			return node.getName();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#isLocked(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public boolean isLocked(DataNode node) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#isVisible(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public boolean isVisible(DataNode node) {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#getFolderIcon(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public String getFolderIcon(DataNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#getLeafIcon(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public String getLeafIcon(DataNode node) {
		return null;
	}

}
