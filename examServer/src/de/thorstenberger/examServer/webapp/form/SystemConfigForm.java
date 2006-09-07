/*

Copyright (C) 2006 Thorsten Berger

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
/**
 * 
 */
package de.thorstenberger.examServer.webapp.form;

/**
 * @author Thorsten Berger
 *
 */
public class SystemConfigForm extends BaseForm {

	private String title;
	private String remoteUserManagerURL;
	private boolean loadJVMOnStartup;
	
	/**
	 * @return Returns the loadJVMOnStartup.
	 */
	public boolean isLoadJVMOnStartup() {
		return loadJVMOnStartup;
	}
	/**
	 * @param loadJVMOnStartup The loadJVMOnStartup to set.
	 */
	public void setLoadJVMOnStartup(boolean loadJVMOnStartup) {
		this.loadJVMOnStartup = loadJVMOnStartup;
	}
	/**
	 * @return Returns the remoteUserManagerURL.
	 */
	public String getRemoteUserManagerURL() {
		return remoteUserManagerURL;
	}
	/**
	 * @param remoteUserManagerURL The remoteUserManagerURL to set.
	 */
	public void setRemoteUserManagerURL(String remoteUserManagerURL) {
		this.remoteUserManagerURL = remoteUserManagerURL;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
