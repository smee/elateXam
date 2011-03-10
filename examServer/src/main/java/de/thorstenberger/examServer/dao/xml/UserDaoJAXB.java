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

import static org.apache.commons.lang.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;

import de.thorstenberger.examServer.dao.LookupDao;
import de.thorstenberger.examServer.dao.RoleDao;
import de.thorstenberger.examServer.dao.UserDao;
import de.thorstenberger.examServer.dao.xml.jaxb.Users;
import de.thorstenberger.examServer.dao.xml.jaxb.Users.User;
import de.thorstenberger.examServer.model.Address;
import de.thorstenberger.examServer.model.Role;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.util.StringUtil;
/**
 * @author Thorsten Berger
 *
 */
public class UserDaoJAXB extends AbstractJAXBDao implements UserDao, UserDetailsService {

  private final ExamServerManager examServerManager;
  private final RoleDao roleDao;
  private final LookupDao lookupDao;
  private Users users;

  private final Log log = LogFactory.getLog(UserDaoJAXB.class);

  /**
	 *
	 */
  public UserDaoJAXB(final ExamServerManager examServerManager, final RoleDao roleDao, final LookupDao lookupDao) {
    super("de.thorstenberger.examServer.dao.xml.jaxb", examServerManager.getSystemDir(), "users.xml");
    this.examServerManager = examServerManager;
    this.roleDao = roleDao;
    this.lookupDao = lookupDao;

    if (!existsWorkingFile()) {
      users = objectFactory.createUsers();
      users.setIdCount(2);
      final User user = objectFactory.createUsersUser();
      user.setUsername("admin");
      user.setFirstName("");
      user.setLastName("");
      user.setVersion(0);
      user.setAccountEnabled(true);
      user.setId(1);
      user.setPassword(StringUtil.encodePassword("admin", "SHA"));
      user.setEmail("elatePortal@thorsten-berger.net");
      user.setAccountExpired(false);
      user.setAccountLocked(false);
      user.setCredentialsExpired(false);
      user.getRoleRef().add("admin");
      users.getUser().add(user);
      save(users);
      return;
    } else {
      users = (Users) load();
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.Dao#getObject(java.lang.Class, java.io.Serializable)
   */
  public Object getObject(final Class clazz, final Serializable id) {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.Dao#getObjects(java.lang.Class)
   */
  public List getObjects(final Class clazz) {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.UserDao#getUser(java.lang.Long)
   */
  public synchronized de.thorstenberger.examServer.model.User getUser(final Long userId) {

    final User u = getUserType(userId);
    if (u != null)
      return populateUser(u);

    log.warn("uh oh, user '" + userId + "' not found...");
    throw new ObjectRetrievalFailureException(User.class, userId);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.UserDao#getUsers(de.thorstenberger.examServer.model.User)
   */
  public synchronized List getUsers(final de.thorstenberger.examServer.model.User userTemplate) {

    final List<User> usersList = users.getUser();
    final List<de.thorstenberger.examServer.model.User> ret = new ArrayList<de.thorstenberger.examServer.model.User>();

    final boolean checkRoles = !CollectionUtils.isEmpty(userTemplate.getRoles());

    for (final User userType : usersList) {
      if (checkRoles){
    	  if(hasRole(userType,userTemplate.getRoles())) {
    		ret.add(populateUser(userType));
    	  }
      }else{
    	  ret.add(populateUser(userType));
      }
    }

    return ret;

  }

	/**
	 * @param userType
	 * @param roles
	 * @return
	 */
	private boolean hasRole(User userType, Set<Role> roles) {
		final List<String> roleRefs = userType.getRoleRef();
		for (final Role role : roles) {
			if (!roleRefs.contains(role.getName()))
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.thorstenberger.examServer.dao.UserDao#getUsersMatching(java.lang.String
	 * )
	 */
  @Override
	public List<de.thorstenberger.examServer.model.User> getUsersMatching(
			String filter) {
		final List<User> usersList = users.getUser();
		final List<de.thorstenberger.examServer.model.User> ret = new ArrayList<de.thorstenberger.examServer.model.User>();

		for (final User userType : usersList) {
			if (isEmpty(filter)) {
				ret.add(populateUser(userType));
			} else
 if (containsIgnoreCase(userType.getFirstName(), filter)
					|| containsIgnoreCase(userType.getLastName(), filter)
					|| containsIgnoreCase(userType.getUsername(), filter)
					|| containsIgnoreCase(userType.getEmail(), filter)) {
				ret.add(populateUser(userType));
			}
		}
		return ret;
  }


  private User getUserType(final long userId) {
    final List<User> usersList = users.getUser();
    for (final User user : usersList) {
      if (user.getId() == userId)
        return user;
    }
    return null;
  }

  private User getUserTypeByUsername(final String userName) {
    final List<User> usersList = users.getUser();
    for (final User user : usersList) {
      if (user.getUsername().equals(userName))
        return user;
    }
    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.UserDao#loadUserByUsername(java.lang.String)
   */
  public synchronized UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

    final List<User> usersList = users.getUser();
    for (final User user : usersList) {
      if (user.getUsername().equals(username))
        return populateUser(user);
    }

    throw new UsernameNotFoundException("user '" + username + "' not found...");

  }

  private de.thorstenberger.examServer.model.User populateUser(final User u) {
    final de.thorstenberger.examServer.model.User ret = new de.thorstenberger.examServer.model.User();
    ret.setAccountExpired(u.isAccountExpired());
    ret.setAccountLocked(u.isAccountLocked());

    final Address address = new Address();
    address.setAddress(u.getAddress());
    address.setCity(u.getCity());
    address.setCountry(u.getCountry());
    address.setPostalCode(u.getPostalCode());
    address.setProvince(u.getProvince());
    ret.setAddress(address);

    ret.setCredentialsExpired(u.isCredentialsExpired());
    ret.setEmail(u.getEmail());
    ret.setEnabled(u.isAccountEnabled());
    ret.setFirstName(u.getFirstName());
    ret.setId(u.getId());
    ret.setLastName(u.getLastName());
    ret.setPassword(u.getPassword());
    ret.setPasswordHint(u.getPasswordHint());
    ret.setPhoneNumber(u.getPhoneNumber());
    ret.setUsername(u.getUsername());
    ret.setVersion(u.getVersion());
    ret.setWebsite(u.getWebsite());
    ret.setMatrikel(u.getMatrikel());
    ret.setSemester(u.getSemester());
    final List<String> roles = u.getRoleRef();
    for (final String role : roles) {
      ret.addRole(roleDao.getRoleByName(role));
    }

    return ret;
  }

  private void populateUserType(final User userType, final de.thorstenberger.examServer.model.User user) {

    userType.setAccountExpired(user.isAccountExpired());
    userType.setAccountLocked(user.isAccountLocked());

    final Address address = user.getAddress();
    userType.setAddress(address.getAddress());
    userType.setCity(address.getCity());
    userType.setCountry(address.getCountry());
    userType.setPostalCode(address.getPostalCode());
    userType.setProvince(address.getProvince());

    userType.setCredentialsExpired(user.isCredentialsExpired());
    userType.setEmail(user.getEmail());
    userType.setAccountEnabled(user.isEnabled());
    userType.setFirstName(user.getFirstName());
    userType.setId(user.getId());
    userType.setLastName(user.getLastName());
    userType.setPassword(user.getPassword());
    userType.setPasswordHint(user.getPasswordHint());
    userType.setPhoneNumber(user.getPhoneNumber());
    userType.setUsername(user.getUsername());
    userType.setVersion(user.getVersion());
    userType.setWebsite(user.getWebsite());
    userType.setSemester(user.getSemester());
    userType.setMatrikel(user.getMatrikel());

    final Iterator it = user.getRoles().iterator();
    userType.getRoleRef().clear();
    while (it.hasNext()) {
      final Role role = (Role) it.next();
      userType.getRoleRef().add(role.getName());
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.Dao#removeObject(java.lang.Class, java.io.Serializable)
   */
  public void removeObject(final Class clazz, final Serializable id) {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.UserDao#removeUser(java.lang.Long)
   */
  public synchronized void removeUser(final Long userId) {
    final User userType = getUserType(userId);
    if (userType != null) {
      users.getUser().remove(userType);
      save(users);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.Dao#saveObject(java.lang.Object)
   */
  public void saveObject(final Object o) {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.UserDao#saveUser(de.thorstenberger.examServer.model.User)
   */
  public synchronized void saveUser(final de.thorstenberger.examServer.model.User user) {
    if (log.isDebugEnabled()) {
      log.debug("user's id: " + user.getId());
    }

    User userType = getUserTypeByUsername(user.getUsername());
    if (userType == null) {
      userType = objectFactory.createUsersUser();
      users.getUser().add(userType);
      userType.setId(users.getIdCount());
      user.setId(users.getIdCount());
      user.setVersion(0);
      // FIXME overflow handling
      users.setIdCount(users.getIdCount() + 1);
    } else {
      // FIXME overflow handling
      user.setVersion(user.getVersion() + 1);
    }

    populateUserType(userType, user);

    save(users);

  }

}
