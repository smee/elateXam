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
package de.elatePortal.autotool;

import de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionModeType;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.AddonSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.AddonSubTask;

public class AutotoolAddOnSubTaskletFactoryImpl implements
		AddOnSubTaskletFactory {

	public AddOnSubTasklet createAddOnSubTasklet( Object subTaskDef, Object subTask, CorrectionModeType correctionMode, float reachablePoints) {
		return new SubTasklet_AutotoolImpl((AddonSubTaskDef)subTaskDef,(AddonSubTask)subTask, correctionMode, reachablePoints);
	}

	public String getAddonTaskType() {
		return "autotool";
	}

}
