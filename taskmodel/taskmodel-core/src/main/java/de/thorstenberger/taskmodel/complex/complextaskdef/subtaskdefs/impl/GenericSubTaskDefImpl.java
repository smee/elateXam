/*

Copyright (C) 2006 Steffen Dienst

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
package de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl;

import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;

public class GenericSubTaskDefImpl implements SubTaskDef {

	private SubTaskDefType jaxbSubTaskDef;

	public GenericSubTaskDefImpl(SubTaskDefType jaxbSubTaskDef) {
		this.jaxbSubTaskDef=jaxbSubTaskDef;

	}
	public String getId() {
		if(jaxbSubTaskDef.isSetId())
			return jaxbSubTaskDef.getId();
		else
			return null;
	}
	/**
	 * backdoor access to JAXB element
	 * @return Returns the mappingSubTaskDef.
	 */
	public SubTaskDefType getJaxbSubTaskDef() {
		return jaxbSubTaskDef;
	}
	public boolean isTrash() {
		return jaxbSubTaskDef.isTrash();
	}
	public String getHint() {
		return jaxbSubTaskDef.getHint();
	}
	public String getProblem() {
		return jaxbSubTaskDef.getProblem();
	}
}