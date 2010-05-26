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
package de.thorstenberger.taskmodel.view.tree;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Thorsten Berger
 *
 */
public class TreeBuilder {

	private DataNodeFormatter formatter;
	private int childIndex = 0;
	private StringBuilder sb;

	public String getTree( DataNode root, HttpServletRequest request ){
		
		sb = new StringBuilder();
		
//		if( rootNodeName == null )
//			rootNodeName = "/";
		
		if( isVisible( root ) )
			sb.append("		d.add(0,-1,'" + formatNodeView( root ) + "');\n" );
		
		if( !isLocked( root ) )
			buildSubNodes( root, 0, request );
		
		return sb.toString();
		 
	}
		
	public DataNodeFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(DataNodeFormatter formatter) {
		this.formatter = formatter;
	}


	/**
	 * 
	 * @param curr
	 * @param parentIndex
	 */
	private void buildSubNodes(DataNode curr, int parentIndex, HttpServletRequest request ){
    final String iconPath = request.getContextPath() + "/icons/";
    List<DataNode> subnodes = curr.getSubNodes();

    if (subnodes == null || subnodes.size() == 0)
      return;

    for (DataNode node : subnodes) {

      if (isVisible(node)) {
        String icon = "";
        String iconOpen = "";

        if (node.isFolder()) {
          icon = iconPath + getFolderIcon(node);
          iconOpen = iconPath + "folderopen.gif";
        } else {
          if (getLeafIcon(node) != null) {
            icon = iconPath + getLeafIcon(node);
            iconOpen = icon;
          }
        }
        // add(id, pid, name, url, title, target, icon, iconOpen, open)
        sb.append(
            String.format("   d.add(%d, %d, '%s', '', '', '', '%s', '%s', %b);\n",
                ++childIndex,
                parentIndex,
                formatNodeView(node),
                icon,
                iconOpen,
                node.isOpen()));
        if (!isLocked(node)) {
          buildSubNodes(node, childIndex, request);
        }
      }
    }
  }
	
	
	private String formatNodeView( DataNode node ){
		if( formatter != null )
			return formatter.format( node );
		else
			return node.getName();
	}
	
	private boolean isLocked( DataNode node ){
		if( formatter != null )
			return formatter.isLocked( node );
		else
			return false;
	}
	
	private boolean isVisible( DataNode node ){
		if( formatter != null )
			return formatter.isVisible( node );
		else
			return true;
	}
	
	public String getFolderIcon( DataNode node ){
		if( formatter != null ){
			
			String fi = formatter.getFolderIcon( node );
			
			if( fi != null )
				return fi;
			else
				return "folder.gif";
			
		}else
			return "folder.gif";		
	}
	
	public String getLeafIcon( DataNode node ){
		if( formatter != null )
			return formatter.getLeafIcon( node );
		else
			return null;
	}
	

}
