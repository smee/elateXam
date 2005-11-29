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

import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.view.tree.AbstractDataNode;
import de.thorstenberger.taskmodel.view.tree.DataNode;

/**
 * @author Thorsten Berger
 *
 */
public class NavigationRootNode extends AbstractDataNode {

	private List<DataNode> categories;

	public NavigationRootNode( ComplexTasklet cth, long taskId, int pageNo ) {
		categories = new ArrayList<DataNode>();
		Try theTry =  cth.getActiveTry();

		List<Page> pages = theTry.getPages();

		CategoryNode currentCategoryNode;

		// build list of category nodes and add their corresponding pages on the fly
		for( Page page : pages ){

			currentCategoryNode = getCategoryNode( page.getCategoryRefId() );

			if( currentCategoryNode == null ){
				currentCategoryNode = new CategoryNode( page.getCategoryRefId(),
						cth.getComplexTaskDefRoot().getCategories().get( page.getCategoryRefId() ).getTitle() );
				categories.add( currentCategoryNode );
			}

			currentCategoryNode.addPage( new PageNode( page.getNumber(), taskId, page.getNumber() == pageNo, page.getProcessStatus() ) );

		}

	}

	private CategoryNode getCategoryNode( String id ){
		for( int i=0; i<categories.size(); i++ ){
			if( ((CategoryNode) categories.get(i) ).getId().equals( id ) )
				return (CategoryNode) categories.get(i);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getName()
	 */
	public String getName() {
		return "";
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
		return categories;
	}

}
