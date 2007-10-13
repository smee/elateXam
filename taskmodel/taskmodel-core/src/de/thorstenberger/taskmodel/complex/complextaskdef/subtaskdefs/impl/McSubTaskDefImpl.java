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
package de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl;

import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef;

/**
 * @author Thorsten Berger
 *
 */
public class McSubTaskDefImpl implements SubTaskDef {

	private McSubTaskDef mcSubTaskDef;
	
	/**
	 * 
	 */
	public McSubTaskDefImpl( McSubTaskDef mcSubTaskDef ) {
		this.mcSubTaskDef = mcSubTaskDef;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef#getId()
	 */
	public String getId() {
		return mcSubTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef#getHint()
	 */
	public String getHint() {
		return mcSubTaskDef.getHint();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef#getProblem()
	 */
	public String getProblem() {
		return mcSubTaskDef.getProblem();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef#isTrash()
	 */
	public boolean isTrash() {
		return mcSubTaskDef.isTrash();
	}

	/**
	 * backdoor access to JAXB element
	 * @return Returns the mcSubTaskDef.
	 */
	public McSubTaskDef getMcSubTaskDef() {
		return mcSubTaskDef;
	}
	
	
	
}
