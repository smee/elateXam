/*

Copyright (C) 2009 Steffen Dienst

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

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.service.RoleManager;
import de.thorstenberger.examServer.service.UserExistsException;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.util.StringUtil;

/**
 * @author Steffen Dienst
 * 
 */
public class FreeForAllAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements
        AuthenticationProvider, MessageSourceAware {

    private MessageSourceAccessor messageSourceAccessor;
    private final ConfigManager configManager;
    private final UserManager userManager;
    private final RoleManager roleManager;

    private boolean forcePrincipalAsString = false;

    /**
	 * 
	 */
    public FreeForAllAuthenticationProvider(final ConfigManager configManager, final UserManager userManager,
            final RoleManager roleManager) {
        this.configManager = configManager;
        this.userManager = userManager;
        this.roleManager = roleManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.
     * acegisecurity.userdetails.UserDetails, org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected void additionalAuthenticationChecks(final UserDetails arg0, final UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
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
        User user = null;
        try {
            user = userManager.getUserByUsername(authentication.getName());
            // if we know this user, make sure the password matches
            if (!user.getPassword().equals(StringUtil.encodePassword((String) authentication.getCredentials(), "SHA"))) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials", "Invalid username or password."));
            }
        } catch (final UsernameNotFoundException e) {
            // doesn't matter, we'll create him in a second...
        }
        if (user == null) {
            // create a new user with the entered username/password
            user = new User();
            user.setEnabled(true);
            final String name = authentication.getName();
            user.setUsername(name);
            user.setFirstName(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            user.setLastName("Mustermann");
            user.setPassword(StringUtil.encodePassword((String) authentication.getCredentials(), "SHA"));
            user.addRole(roleManager.getRole("student"));
            try {
                userManager.saveUser(user);
            } catch (final UserExistsException e2) {
                // should not happen
                throw new RuntimeException(e2);
            }
        }

        final UserDetails userDetails = userManager.getUserByUsername(user.getUsername());

        Object principalToReturn = userDetails;

        if (forcePrincipalAsString) {
            principalToReturn = userDetails.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, userDetails);

    }

    /**
     * @return Returns the forcePrincipalAsString.
     */
    @Override
    public boolean isForcePrincipalAsString() {
        return forcePrincipalAsString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
     * org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected UserDetails retrieveUser(final String arg0, final UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param forcePrincipalAsString
     *            The forcePrincipalAsString to set.
     */
    @Override
    public void setForcePrincipalAsString(final boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
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
     * @see org.acegisecurity.providers.AuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports(final Class authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
