/*

Copyright (C) 2012 Steffen Dienst

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
package de.thorstenberger.examServer.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.webapp.listener.StartupListener;
import de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory;
import de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi;
import de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory;

/**
 * Load addon subtasklet implementations via java.util.ServiceLoader (JRE 1.6+)
 * 
 * @author Steffen Dienst
 * 
 */
public class ServiceLoaderAddonFactory implements AddonSubtaskletFactoryPerOSGi {
	private static final Log LOG = LogFactory.getLog(ServiceLoaderAddonFactory.class);
	private final Map<String, AddonSubTaskViewFactory> viewFactories = new HashMap<String, AddonSubTaskViewFactory>();
	private final Map<String, AddOnSubTaskletFactory> taskletFactories = new HashMap<String, AddOnSubTaskletFactory>();

	public ServiceLoaderAddonFactory() {
		for (AddonSubTaskViewFactory f : ServiceLoader
				.load(AddonSubTaskViewFactory.class)) {
			viewFactories.put(f.getAddonTaskType(), f);
			LOG.info("found AddonSubTaskViewFactory for " + f.getAddonTaskType());
		}

		for (AddOnSubTaskletFactory f : ServiceLoader
				.load(AddOnSubTaskletFactory.class)) {
			taskletFactories.put(f.getAddonTaskType(), f);
			LOG.info("found AddOnSubTaskletFactory for" + f.getAddonTaskType());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
	 * #getSubTaskViewFactory(java.lang.String)
	 */
	@Override
	public AddonSubTaskViewFactory getSubTaskViewFactory(String id) {
		return viewFactories.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
	 * #getSubTaskletFactory(java.lang.String)
	 */
	@Override
	public AddOnSubTaskletFactory getSubTaskletFactory(String id) {
		return taskletFactories.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi
	 * #shutDown()
	 */
	@Override
	public void shutDown() throws Exception {
		viewFactories.clear();
		taskletFactories.clear();
	}

}
