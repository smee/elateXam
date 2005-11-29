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
package junittask.subtasklet;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceEvent;
import ch.ethz.iks.r_osgi.RemoteServiceListener;
import ch.ethz.iks.r_osgi.URI;

import correction.junit.JUnitTestCorrector;
import correction.junit.JUnitTestResult;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.addon.AddOnSubTaskletFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.AddonSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.AddonSubTask;

public class JUnitAddOnSubTaskletFactoryImpl implements
		AddOnSubTaskletFactory {

	JUnitTestCorrector corr;

	public JUnitAddOnSubTaskletFactoryImpl(BundleContext context, String remoteUrl) {
		corr = new CorrectorServiceTrackerProxy(context, remoteUrl);
	}

	public AddOnSubTasklet createAddOnSubTasklet(ComplexTaskDefRoot root, Block block, Object subTaskDef, Object subTask) {
		//System.out.println("[JUnitAddOnSubTaskletFactoryImpl] creating JUnitSubTaskletImpl(...)");
		return new JUnitSubTaskletImpl(root, block,(AddonSubTaskDef)subTaskDef,(AddonSubTask)subTask,corr);
	}

	public String getAddonTaskType() {
		return "junit";
	}

	private class CorrectorServiceTrackerProxy extends ServiceTracker implements JUnitTestCorrector,RemoteServiceListener{

		private JUnitTestCorrector service;
		private RemoteOSGiService ros;

		public CorrectorServiceTrackerProxy(BundleContext context, String clazz,
				ServiceTrackerCustomizer customizer) {
			super( context, clazz, customizer );
			throw new MethodNotSupportedException();
		}

		public CorrectorServiceTrackerProxy(BundleContext context,
				ServiceReference reference, ServiceTrackerCustomizer customizer) {
			super( context, reference, customizer );
			throw new MethodNotSupportedException();
		}

		public CorrectorServiceTrackerProxy(BundleContext context, Filter filter,
				ServiceTrackerCustomizer customizer) {
			super( context, filter, customizer );
			throw new MethodNotSupportedException();
		}

		public CorrectorServiceTrackerProxy(BundleContext context, String remoteUrl){
			super(context, JUnitTestCorrector.class.getName(),null);
			this.open();

		    ServiceReference remoteserviceref = context.getServiceReference( RemoteOSGiService.class.getName() );
		    if( remoteserviceref != null ) {

		    	Dictionary<String, Object> map = new java.util.Hashtable<String, Object>();

		    	map.put(RemoteServiceListener.SERVICE_INTERFACES,new String[] { JUnitTestCorrector.class.getName() });
		    	context.registerService( RemoteServiceListener.class.getName(), this, map );

		    	try{
		    		this.ros = (RemoteOSGiService) context.getService( remoteserviceref );
		    		URI uri = new URI(remoteUrl);
		    		System.out.println("[CorrectorProxy] connecting to "+uri);
		    		ros.connect( uri );
		    	}catch(RemoteOSGiException ex) {
		    		System.err.println("No remote connection: ");
		    		ex.printStackTrace();
		    	}catch(java.io.IOException ex) {
		    		System.err.println("Error while creating remote connection: ");
		    		ex.printStackTrace();
		    	}
		    }
		}

		public JUnitTestResult runUnitTest( String testInterfaceDef,
				String classUnderTest, String junitTestClass, long timeOut ) {

			JUnitTestCorrector tester = (JUnitTestCorrector) getService();
			if( tester == null ) {
				tester = service;
			}
			if( tester == null ) {
				System.out.println("[CorrectorProxy] no corrector service present... ");
				return null;
			}
			else
				return tester.runUnitTest( testInterfaceDef, classUnderTest, junitTestClass, timeOut );
		}

		@Override
		public Object addingService( ServiceReference reference ) {
			System.out.println("[CorrectorProxy] found new tester: "+reference);
			return super.addingService( reference );
		}

		@Override
		public void modifiedService( ServiceReference reference, Object service ) {
			super.modifiedService( reference, service );
			System.out.println("[CorrectorProxy] modified: "+reference);
		}

		@Override
		public void removedService( ServiceReference reference, Object service ) {
			super.removedService( reference, service );
			System.out.println("[CorrectorProxy] removed tester: "+reference);
		}

		public void remoteServiceEvent( RemoteServiceEvent rs ) {
			switch ( rs.getType() ) {
			case RemoteServiceEvent.REGISTERED:
				System.out.println("[CorrectorProxy] found new remote service: "+rs.getRemoteReference());
				this.service = retrieveRemoteService(rs.getRemoteReference().getURI());
				break;
			case RemoteServiceEvent.MODIFIED:
				System.out.println("[CorrectorProxy] modified remote service: "+rs.getRemoteReference());
				break;
			case RemoteServiceEvent.UNREGISTERING:
				System.out.println("[CorrectorProxy] removed remote service: "+rs.getRemoteReference());
				this.service = null;
				break;
			default:
				break;
			}
		}

		public JUnitTestCorrector retrieveRemoteService( URI uri ) {
			if( ros != null) {
				return (JUnitTestCorrector) ros.getRemoteService( ros.getRemoteServiceReference( uri ) );
			}
			return null;
		}
	}


}
