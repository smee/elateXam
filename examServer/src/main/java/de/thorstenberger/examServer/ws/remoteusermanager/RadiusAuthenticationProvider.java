/*

Copyright (C) 2010 Steffen Dienst

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
package de.thorstenberger.examServer.ws.remoteusermanager;

import java.io.IOException;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.service.RoleManager;
import de.thorstenberger.examServer.service.UserExistsException;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.util.StringUtil;
import de.thorstenberger.examServer.ws.remoteusermanager.client.UserBean;

/**
 * Authenticate against a radius server (not challenge based!). See RFC 2865.
 *
 * @author Steffen Dienst
 *
 */
public class RadiusAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider,
    MessageSourceAware {
  private final Log log = LogFactory.getLog(RadiusAuthenticationProvider.class);

  private MessageSourceAccessor messageSourceAccessor;
  private final ConfigManager configManager;
  private final UserManager userManager;
  private final RoleManager roleManager;

  private boolean forcePrincipalAsString = false;

  /**
     *
     */
  public RadiusAuthenticationProvider(final ConfigManager configManager, final UserManager userManager,
      final RoleManager roleManager) {
    this.configManager = configManager;
    this.userManager = userManager;
    this.roleManager = roleManager;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.acegisecurity.providers.AuthenticationProvider#authenticate(org.acegisecurity.Authentication)
   */
  @Override
  public Authentication authenticate(final Authentication authentication)
      throws AuthenticationException {

    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
        messageSourceAccessor.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
        "Only UsernamePasswordAuthenticationToken is supported"));

    if (!configManager.isStudentsLoginEnabled()) {
      throw new AuthenticationServiceException("Login disabled for student role.");
    }

    if (!StringUtils.isEmpty(configManager.getRadiusHost()) && !StringUtils.isEmpty(configManager.getRadiusSharedSecret())) {

      // Determine username
      final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
      final String password = (authentication.getCredentials() == null) ? "NONE_PROVIDED"
          : (String) authentication.getCredentials();

      UserBean userBean;
      try {

        userBean = getRemoteUserInfos(username, password);

        if (!userBean.getRole().equals("student")) {
          throw new AuthenticationServiceException("Only student role allowed.");
        }

        try {

          userManager.getUserByUsername(userBean.getLogin());

        } catch (final UsernameNotFoundException e) {
          final User user = new User();
          user.setEnabled(true);
          user.setUsername(userBean.getLogin());
          user.setFirstName(userBean.getFirstName() == null ? "" : userBean.getFirstName());
          user.setLastName(userBean.getName() == null ? "" : userBean.getName());
          user.setEmail(userBean.getEmail() == null ? "" : userBean.getEmail());
          user.setPassword(StringUtil.encodePassword(userBean.getPassword(), "SHA"));
          user.addRole(roleManager.getRole("student"));
          try {
            userManager.saveUser(user);
          } catch (final UserExistsException e2) {
            // should not happen
            throw new RuntimeException(e2);
          }
        }
      } catch (final AuthenticationServiceException e) {
        throw new BadCredentialsException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Invalid username or password."));
      }

      final UserDetails userDetails = userManager.getUserByUsername(username);

      Object principalToReturn = userDetails;

      if (forcePrincipalAsString) {
        principalToReturn = userDetails.getUsername();
      }

      return createSuccessAuthentication(principalToReturn, authentication, userDetails);

    }

    return null;

  }

  private UserBean getRemoteUserInfos(final String login, final String pwd) throws AuthenticationServiceException {
    final boolean auth = authenticateUser(login, pwd);
    if (auth) {
      // now we know that at least login and password are correct, so we create the user bean
      final UserBean bean = new UserBean(login, login, null, pwd, "student", null);
      return bean;
    } else {
      throw new AuthenticationServiceException("Invalid username/password!");
    }
  }

  private boolean authenticateUser(final String login, final String pwd) {
    final RadiusClient radiusClient = new RadiusClient(configManager.getRadiusHost(), configManager.getRadiusSharedSecret());
    try {
      return radiusClient.authenticate(login, pwd);
    } catch (final IOException e) {
      throw new AuthenticationServiceException("Could not authenticate user!", e);
    } catch (final RadiusException e) {
      throw new AuthenticationServiceException("Could not authenticate user!", e);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.acegisecurity.providers.AuthenticationProvider#supports(java.lang.Class)
   */
  @Override
  public boolean supports(final Class authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
   */
  @Override
  public void setMessageSource(final MessageSource messageSource) {
    this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }

  /*
   * (non-Javadoc)
   *
   * @seeorg.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.
   * acegisecurity.userdetails.UserDetails, org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
   */
  @Override
  protected void additionalAuthenticationChecks(final UserDetails arg0, final UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
  }

  /*
   * (non-Javadoc)
   *
   * @see org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
   * org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
   */
  @Override
  protected UserDetails retrieveUser(final String arg0, final UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
    return null;
  }

  /**
   * @return Returns the forcePrincipalAsString.
   */
  @Override
  public boolean isForcePrincipalAsString() {
    return forcePrincipalAsString;
  }

  /**
   * @param forcePrincipalAsString
   *          The forcePrincipalAsString to set.
   */
  @Override
  public void setForcePrincipalAsString(final boolean forcePrincipalAsString) {
    this.forcePrincipalAsString = forcePrincipalAsString;
  }

}
