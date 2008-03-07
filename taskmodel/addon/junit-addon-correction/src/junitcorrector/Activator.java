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
package junitcorrector;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import correction.junit.JUnitTestCorrector;

import ch.ethz.iks.r_osgi.RemoteOSGiService;

public class Activator implements BundleActivator {

	public void start( BundleContext context ) throws Exception {
		Hashtable properties = new Hashtable();
		properties.put(RemoteOSGiService.R_OSGi_REGISTRATION,  Boolean.TRUE);

		context.registerService(JUnitTestCorrector.class.getName(),
				new JUnitTestCorrectorImpl(), properties);
	}

	public void stop( BundleContext arg0 ) throws Exception {
	}

}
