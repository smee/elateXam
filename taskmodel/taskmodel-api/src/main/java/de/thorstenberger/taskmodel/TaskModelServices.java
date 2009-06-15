/*

Copyright (C) 2007 Steffen Dienst

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
package de.thorstenberger.taskmodel;

import de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi;
/**
 * This class provides the singleton access to any addon task specific implementations.
 * @author Steffen Dienst
 *
 */
public class TaskModelServices {
	private static final TaskModelServices instance=new TaskModelServices();
	AddonSubtaskletFactoryPerOSGi factory;

	private TaskModelServices() {}

	public static TaskModelServices getInstance() {
		return instance;
	}

	/*
	 * @see de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
	 */
	public AddonSubtaskletFactoryPerOSGi getAddonSubtaskletFactory() {
		return factory;
	}

	/*
	 * @see de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
	 */
	public void setAddonSubtaskletFactory(AddonSubtaskletFactoryPerOSGi f) {
		this.factory=f;
	}
}
