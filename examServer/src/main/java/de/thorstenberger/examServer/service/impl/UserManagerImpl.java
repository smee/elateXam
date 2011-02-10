package de.thorstenberger.examServer.service.impl;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import de.thorstenberger.examServer.dao.UserDao;
import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.UserExistsException;
import de.thorstenberger.examServer.service.UserManager;


/**
 * Implementation of UserManager interface.</p>
 *
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private UserDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }

    /**
     * @see de.thorstenberger.examServer.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String userId) {
        return dao.getUser(new Long(userId));
    }

    /**
     * @see de.thorstenberger.examServer.service.UserManager#getUsers(de.thorstenberger.examServer.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers(user);
    }

    /**
     * @see de.thorstenberger.examServer.service.UserManager#saveUser(de.thorstenberger.examServer.model.User)
     */
    public void saveUser(User user) throws UserExistsException {
    	// if new user, lowercase userId
    	if (user.getVersion() == null) {
            user.setUsername(user.getUsername().toLowerCase());
    	}
        try {
            dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * @see de.thorstenberger.examServer.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String userId) {
        if (log.isDebugEnabled()) {
            log.debug("removing user: " + userId);
        }

        dao.removeUser(new Long(userId));
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) dao.loadUserByUsername(username);
    }

  /*
   * (non-Javadoc)
   * 
   * @see de.thorstenberger.examServer.service.UserManager#getUsers(java.lang.String)
   */
  @Override
  public List getUsers(String substring) {
    return dao.getUsersMatching(substring);
  }
}
