package activator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.elatePortal.autotool.AutotoolAddOnSubTaskletFactoryImpl;
import de.elatePortal.autotool.view.AutotoolSubTaskViewFactory;
import de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory;
import de.thorstenberger.taskmodel.view.AddonSubTaskViewFactory;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {

		context.registerService(
				AddOnSubTaskletFactory.class.getName(),
				new AutotoolAddOnSubTaskletFactoryImpl(),
				null);

		context.registerService(
				AddonSubTaskViewFactory.class.getName(),
				new AutotoolSubTaskViewFactory(),
				null);

	}

	public void stop(BundleContext context) throws Exception {
	}

}
