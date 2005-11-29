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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl;

import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.AddonSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;

public abstract class AbstractAddonSubTasklet extends AbstractSubTasklet implements
		AddOnSubTasklet {

	public AbstractAddonSubTasklet(ComplexTaskDefRoot complexTaskDefRoot, Block block,SubTaskDefType subTaskDef, SubTaskType subTaskType) {
		super(complexTaskDefRoot, block, subTaskDef, subTaskType );
	}

	public int getTextFieldWidth(){
		return ((AddonSubTaskDef)this.jaxbSubTaskDef).isSetTextFieldWidth() ?
				((AddonSubTaskDef)this.jaxbSubTaskDef).getTextFieldWidth() : 60;
	}

	public int getTextFieldHeight(){
		return ((AddonSubTaskDef)this.jaxbSubTaskDef).isSetTextFieldHeight() ?
				((AddonSubTaskDef)this.jaxbSubTaskDef).getTextFieldHeight() : 15;
	}

}
