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
package de.thorstenberger.taskmodel.complex.addon;

import de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory;

/**
 * This interface specifies the methods needed for any kind of addon task.
 * Any kind of plugin implementation should be usable.
 * @author Steffen Dienst
 *
 */
public interface AddonSubtaskletFactoryPerOSGi {

	/**
	 * Get the subtaskview factory for the addon task.
	 * @param id the id for the addon task, @see de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory
	 * @return
	 */
	public AddonSubTaskViewFactory getSubTaskViewFactory(String id);

	/**
	 * Get the subtasklet factory for the addon task.
	 * @param id the id for the addon task, @see de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory
	 * @return
	 */
	public AddOnSubTaskletFactory getSubTaskletFactory(String id);

	/**
	 * Hook for shutting down this addon task plugin facility.
	 * @throws Exception
	 */
	public void shutDown() throws Exception;
}