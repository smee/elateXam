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

import de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactories;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory;
import de.thorstenberger.taskmodel.view.SubTaskView;
import de.thorstenberger.taskmodel.view.SubTaskViewFactory;

/**
 * This class provides the singleton access to any addon task specific
 * implementations.
 * 
 * @author Steffen Dienst
 * 
 */
public class TaskModelServices implements SubTaskViewFactory {
    private static final TaskModelServices instance = new TaskModelServices();

    public static TaskModelServices getInstance() {
        return instance;
    }

    AddonSubtaskletFactories factory;

    private SubTaskViewFactory delegate;

    private TaskModelServices() {
    }

    /*
     * @see
     * de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
     */
    public AddonSubtaskletFactories getAddonSubtaskletFactory() {
        return factory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.thorstenberger.taskmodel.view.SubTaskViewFactory#getSubTaskView(de
     * .thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet)
     */
    public SubTaskView getSubTaskView(final SubTasklet subTasklet) {
        if (delegate != null) {
            final SubTaskView view = delegate.getSubTaskView(subTasklet);
            if (view != null) {
                return view;
            }
        }
        if (subTasklet instanceof AddOnSubTasklet) {
            final AddOnSubTasklet aost = (AddOnSubTasklet) subTasklet;
            final AddonSubTaskViewFactory factory = TaskModelServices.getInstance().getAddonSubtaskletFactory()
                    .getSubTaskViewFactory(aost.getAddOnType());
            if (factory != null) {
                return factory.getSubTaskView(aost);
            }
        }
        return null;
    }

    /*
     * @see
     * de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
     */
    public void setAddonSubtaskletFactory(final AddonSubtaskletFactories f) {
        this.factory = f;
    }

    /**
     * Set the default factory to use for rendering subtasklets.
     * 
     * @param factory
     */
    public void setSubTaskViewFactory(final SubTaskViewFactory factory) {
        this.delegate = factory;
    }

}
