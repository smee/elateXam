/**
 * 
 */
package de.thorstenberger.examServer.tasks;

import java.util.Arrays;
import java.util.Collection;

import de.elatePortal.autotool.AutotoolAddOnSubTaskletFactoryImpl;
import de.elatePortal.autotool.view.AutotoolSubTaskViewFactory;
import de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory;
import de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi;
import de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory;

/**
 * Simple hardcoded calls to addon tasks, at the moment only autotool.
 * @author Steffen Dienst
 *
 */
public class HardWiredAddonFactory implements AddonSubtaskletFactoryPerOSGi {

	private final Collection<? extends AddOnSubTaskletFactory> taskletfactories;
	private final Collection<? extends AddonSubTaskViewFactory> viewfactories;

	public HardWiredAddonFactory() {
		this.taskletfactories =  Arrays.asList(new AutotoolAddOnSubTaskletFactoryImpl());
		this.viewfactories = Arrays.asList(new AutotoolSubTaskViewFactory());
	}
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi#getSubTaskViewFactory(java.lang.String)
	 */
	public AddonSubTaskViewFactory getSubTaskViewFactory(String id) {
		for(AddonSubTaskViewFactory viewfactory: viewfactories)
		  if(viewfactory.getAddonTaskType().equals(id))
			  return viewfactory;
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi#getSubTaskletFactory(java.lang.String)
	 */
	public AddOnSubTaskletFactory getSubTaskletFactory(String id) {
		for(AddOnSubTaskletFactory taskletfactory: taskletfactories)
		  if(taskletfactory.getAddonTaskType().equals(id))
			  return taskletfactory;
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.addon.AddonSubtaskletFactoryPerOSGi#shutDown()
	 */
	public void shutDown() throws Exception {
		// nothing to do, this is no osgi framework...
	}

}
