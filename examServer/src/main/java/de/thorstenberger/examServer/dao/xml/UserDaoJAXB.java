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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;

import de.thorstenberger.examServer.dao.LookupDao;
import de.thorstenberger.examServer.dao.RoleDao;
import de.thorstenberger.examServer.dao.UserDao;
import de.thorstenberger.examServer.dao.xml.jaxb.ObjectFactory;
import de.thorstenberger.examServer.dao.xml.jaxb.Users;
import de.thorstenberger.examServer.dao.xml.jaxb.UsersType.UserType;
import de.thorstenberger.examServer.model.Address;
import de.thorstenberger.examServer.model.Role;
import de.thorstenberger.examServer.model.User;
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
        super("de.thorstenberger.examServer.dao.xml.jaxb",
                new File(examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.SYSTEM
                + File.separatorChar + "users.xml"));
        this.examServerManager = examServerManager;
        this.roleDao = roleDao;
        this.lookupDao = lookupDao;

        try { // JAXBException

            if (!iofile.exists()) {
                final ObjectFactory oF = new ObjectFactory();
                users = oF.createUsers();
                users.setIdCount(2);
                final UserType user = oF.createUsersTypeUserType();
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

        } catch (final JAXBException e) {
            throw new RuntimeException(e);
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
    public synchronized User getUser(final Long userId) {

        final UserType u = getUserType(userId);
        if (u != null) {
            return populateUser(u);
        }

        log.warn("uh oh, user '" + userId + "' not found...");
        throw new ObjectRetrievalFailureException(User.class, userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.thorstenberger.examServer.dao.UserDao#getUsers(de.thorstenberger.examServer.model.User)
     */
    public synchronized List getUsers(final User user) {

        final List<UserType> usersList = users.getUser();
        final List<User> ret = new ArrayList<User>();

        final Set<Role> roles = user.getRoles();
        final boolean checkRoles = roles != null && !roles.isEmpty();

        for (final UserType userType : usersList) {
            boolean passed = true;
            if (checkRoles) {
                final List<String> roleRefs = userType.getRoleRef();
                for (final Role role : roles) {
                    if (!roleRefs.contains(role.getName())) {
                        passed = false;
                    }
                }
            }

            if (passed) {
                ret.add(populateUser(userType));
            }
        }

        return ret;

    }

    private UserType getUserType(final long userId) {
        final List<UserType> usersList = users.getUser();
        for (final UserType user : usersList) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    private UserType getUserTypeByUsername(final String userName) {
        final List<UserType> usersList = users.getUser();
        for (final UserType user : usersList) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
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

        final List<UserType> usersList = users.getUser();
        for (final UserType user : usersList) {
            if (user.getUsername().equals(username)) {
                return populateUser(user);
            }
        }

        throw new UsernameNotFoundException("user '" + username + "' not found...");

    }

    private User populateUser(final UserType u) {
        final User ret = new User();
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

        final List<String> roles = u.getRoleRef();
        for (final String role : roles) {
            ret.addRole(roleDao.getRoleByName(role));
        }

        return ret;
    }

    private void populateUserType(final UserType userType, final User user) {

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
        final UserType userType = getUserType(userId);
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
    public synchronized void saveUser(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }

        UserType userType = getUserTypeByUsername(user.getUsername());
        if (userType == null) {
            try {
                userType = (new ObjectFactory()).createUsersTypeUserType();
                users.getUser().add(userType);
                userType.setId(users.getIdCount());
                user.setId(users.getIdCount());
                user.setVersion(0);
                // FIXME overflow handling
                users.setIdCount(users.getIdCount() + 1);
            } catch (final JAXBException e) {
                throw new RuntimeException(e);
            }
        } else {
            // FIXME overflow handling
            user.setVersion(user.getVersion() + 1);
        }

        populateUserType(userType, user);

        save(users);

    }

}
