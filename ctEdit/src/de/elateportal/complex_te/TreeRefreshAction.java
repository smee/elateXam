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
package de.elateportal.complex_te;

import com.jaxfront.core.rule.Action;
import com.jaxfront.core.type.Type;

/**
 * @author Thorsten Berger
 *
 */
public class TreeRefreshAction implements Action {

	/**
	 * 
	 */
	public TreeRefreshAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.jaxfront.core.rule.Action#perform(com.jaxfront.core.type.Type)
	 */
	public void perform(Type arg0) {
		System.out.println( "repaint" );
		ComplexTE.getInstance().getEditorPanel().repaint();

	}

	public void perform(Type arg0, Type arg1) {
		System.out.println( "repaint" );
		ComplexTE.getInstance().getEditorPanel().repaint();
		
	}

}
