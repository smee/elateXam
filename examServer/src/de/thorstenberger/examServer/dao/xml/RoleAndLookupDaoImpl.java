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
package de.thorstenberger.examServer.dao.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.thorstenberger.examServer.dao.LookupDao;
import de.thorstenberger.examServer.dao.RoleDao;
import de.thorstenberger.examServer.model.Role;

/**
 * @author Thorsten Berger
 *
 */
public class RoleAndLookupDaoImpl implements RoleDao, LookupDao {

	public static final String ADMIN = "admin";
	public static final String STUDENT = "student";
	public static final String TUTOR = "tutor";
	
	private static Map<String, Role> roles;
	
	/**
	 * 
	 */
	public RoleAndLookupDaoImpl() {
		roles = new HashMap<String, Role>();
		Role admin = new Role( ADMIN );
		admin.setId( 1l );
		Role student = new Role( STUDENT );
		student.setId( 2l );
		Role tutor = new Role( TUTOR );
		tutor.setId( 3l );
		
		roles.put( ADMIN, admin );
		roles.put( STUDENT, student );
		roles.put( TUTOR, tutor );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.RoleDao#getRoleByName(java.lang.String)
	 */
	public Role getRoleByName(String rolename) {
		return roles.get( rolename );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.RoleDao#getRoles(de.thorstenberger.examServer.model.Role)
	 */
	public List getRoles(Role role) {
		List ret = new ArrayList();
		Iterator it = roles.values().iterator();
		while( it.hasNext())
			ret.add( it.next() );
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.RoleDao#saveRole(de.thorstenberger.examServer.model.Role)
	 */
	public void saveRole(Role role) {
		throw new UnsupportedOperationException( "cannot save role" );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.RoleDao#removeRole(java.lang.String)
	 */
	public void removeRole(String rolename) {
		throw new UnsupportedOperationException( "cannot remove role" );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.Dao#getObjects(java.lang.Class)
	 */
	public List getObjects(Class clazz) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.Dao#getObject(java.lang.Class, java.io.Serializable)
	 */
	public Object getObject(Class clazz, Serializable id) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.Dao#saveObject(java.lang.Object)
	 */
	public void saveObject(Object o) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.Dao#removeObject(java.lang.Class, java.io.Serializable)
	 */
	public void removeObject(Class clazz, Serializable id) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.LookupDao#getRoles()
	 */
	public List getRoles() {
		List ret = new ArrayList();
		Iterator it = roles.values().iterator();
		while( it.hasNext())
			ret.add( it.next() );
		return ret;
	}

	
	
}
