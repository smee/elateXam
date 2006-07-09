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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.view.tree.DataNode;
import de.thorstenberger.taskmodel.view.tree.DataNodeFormatter;

/**
 * @author Thorsten Berger
 *
 */
public class NavigationNodeFormatter implements DataNodeFormatter {

	String path;
	HttpServletRequest request;
	HttpServletResponse response;
	long taskId;
	
	public NavigationNodeFormatter( long taskId, String path, HttpServletRequest request, HttpServletResponse response ){
		this.taskId = taskId;
		this.path = path;
		this.request = request;
		this.response = response;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNodeFormatter#format(de.thorstenberger.taskmodel.view.tree.DataNode)
	 */
	public String format(DataNode node) {
		if( node instanceof PageNode ){
			PageNode pn = (PageNode)node;
			String url = response.encodeURL( path + "?id=" + taskId + "&todo=continue&page=" + pn.getPageNumber() );
			return "<a class=\"node\" href=javascript:leave(\"" + url + "\")>" +
			"Seite " + pn.getPageNumber() + "</a>" +
			( pn.isCurrentlyActivePage() ? "<img src=\"" + request.getContextPath() + "/pics/sparkle001bu.gif\"> " : "" );
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
		
		if( node instanceof PageNode ){
			PageNode pn = (PageNode)node;
			switch( pn.getProcessStatus() ){
				case Page.NOT_PROCESSED : return "page.gif";
				case Page.PARTLY_PROCESSED : return "partlyProcessed.gif";
				case Page.COMPLETELY_PROCESSED : return "processed.gif";
			}
		}
		
		return null;
	}

}
