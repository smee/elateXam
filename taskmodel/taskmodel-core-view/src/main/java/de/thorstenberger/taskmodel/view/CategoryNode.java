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

import java.util.ArrayList;
import java.util.List;

import de.thorstenberger.taskmodel.view.tree.DataNode;

/**
 * @author Thorsten Berger
 *
 */
public class CategoryNode implements DataNode {

	private String categoryName;
	private String id;
	private List<DataNode> pages;
	
	
	/**
	 * 
	 */
	public CategoryNode( String id, String categoryName ) {
		this.id = id;
		this.categoryName = categoryName;
		pages = new ArrayList<DataNode>();
	}

	public void addPage( PageNode p ){
		pages.add( p );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getName()
	 */
	public String getName() {
		return categoryName;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#isFolder()
	 */
	public boolean isFolder() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getSubNodes()
	 */
	public List<DataNode> getSubNodes() {
		return pages;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	
	
}
