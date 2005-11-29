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
package de.thorstenberger.taskmodel.impl;

import java.util.HashMap;
import java.util.Map;

import de.thorstenberger.taskmodel.UserInfo;

/**
 * @author Thorsten Berger
 *
 */
public class UserInfoImpl implements UserInfo {

	private String login;
	private String firstName;
	private String name;
	private String eMail;
	private Map<String, String> userAttributes;
	
	/**
	 * @return Returns the eMail.
	 */
	public String getEMail() {
		return eMail;
	}
	/**
	 * @param mail The eMail to set.
	 */
	public void setEMail(String mail) {
		eMail = mail;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public synchronized void setUserAttribute( String key, String value ){
		if( userAttributes == null )
			userAttributes = new HashMap<String, String>();
		userAttributes.put( key, value );
		
	}
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.UserInfo#getUserAttributeValue(java.lang.String)
	 */
	public synchronized String getUserAttributeValue(String key) {
		if( userAttributes == null )
			return null;
		return userAttributes.get( key );
	}


}
