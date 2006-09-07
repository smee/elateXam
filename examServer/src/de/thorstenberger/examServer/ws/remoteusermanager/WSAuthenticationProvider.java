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
package de.thorstenberger.examServer.ws.remoteusermanager;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
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
import de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager;
import de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerException;
import de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerServiceLocator;
import de.thorstenberger.examServer.ws.remoteusermanager.client.UserBean;

/**
 * @author Thorsten Berger
 *
 */
public class WSAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

	private MessageSourceAccessor messageSourceAccessor;
	private ConfigManager configManager;
	private UserManager userManager;
	private UserDetailsService userDetailsService;
	private RoleManager roleManager;
	
	private boolean forcePrincipalAsString = false;
	
	/**
	 * 
	 */
	public WSAuthenticationProvider( ConfigManager configManager, UserManager userManager, UserDetailsService userDetailsService, RoleManager roleManager ) {
		this.configManager = configManager;
		this.userManager = userManager;
		this.userDetailsService = userDetailsService;
		this.roleManager = roleManager;
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.providers.AuthenticationProvider#authenticate(org.acegisecurity.Authentication)
	 */
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
	            messageSourceAccessor.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
	                "Only UsernamePasswordAuthenticationToken is supported"));

		if( !configManager.isStudentsLoginEnabled() )
			throw new AuthenticationServiceException( "Login disabled for student role." );
		
		if( configManager.getRemoteUserManagerURL() != null ){
		
	        // Determine username
	        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
	        String password = (authentication.getCredentials() == null) ? "NONE_PROVIDED" : (String)authentication.getCredentials();
			
	        UserBean userBean;
            try {
            	
				userBean = getRemoteUserInfos( configManager.getRemoteUserManagerURL(), username, password );
				
				if( !userBean.getRole().equals( "student" ) )
					throw new AuthenticationServiceException( "Only student role allowed." );
				
				
				try{
					
					userDetailsService.loadUserByUsername( userBean.getLogin() );
					
				}catch( UsernameNotFoundException e ){
					
					User user = new User();
					user.setEnabled( true );
					user.setUsername( userBean.getLogin() );
					user.setFirstName( userBean.getSurname() == null ? "" : userBean.getSurname() );
					user.setLastName( userBean.getName() == null ? "" : userBean.getName() );
					user.setEmail( userBean.getEmail() == null ? "" : userBean.getEmail() );
					user.setPassword( StringUtil.encodePassword( userBean.getPassword(), "SHA" ) );
					user.addRole( roleManager.getRole( "student" ) );
					try {
						userManager.saveUser( user );
					} catch (UserExistsException e2) {
						// should not happen
						throw new RuntimeException( e2 );
					}
				}
				
			} catch (AuthenticationServiceException e) {
				throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials", "Invalid username or password."));
			}
			
			UserDetails userDetails = userDetailsService.loadUserByUsername( username );
	        
	        Object principalToReturn = userDetails;

	        if (forcePrincipalAsString) {
	            principalToReturn = userDetails.getUsername();
	        }

	        return createSuccessAuthentication(principalToReturn, authentication, userDetails);
			
		}
		

	        return null;
		
	}

	private UserBean getRemoteUserInfos( String url, String login, String pwd ) throws AuthenticationServiceException{
        try {
			RemoteUserManagerServiceLocator loc = new RemoteUserManagerServiceLocator();
			loc.setRemoteUserManagerEndpointAddress( url );
			RemoteUserManager rum = loc.getRemoteUserManager();
			return rum.getUserData( login, pwd );
		} catch (RemoteUserManagerException e) {
			throw new AuthenticationServiceException( e.getMessage(), e );
		} catch (RemoteException e) {
			throw new AuthenticationServiceException( e.getMessage(), e );
		} catch (ServiceException e) {
			throw new AuthenticationServiceException( e.getMessage(), e );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.acegisecurity.providers.AuthenticationProvider#supports(java.lang.Class)
	 */
	public boolean supports(Class authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSourceAccessor = new MessageSourceAccessor( messageSource );
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.acegisecurity.userdetails.UserDetails, org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected UserDetails retrieveUser(String arg0, UsernamePasswordAuthenticationToken arg1) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return Returns the forcePrincipalAsString.
	 */
	public boolean isForcePrincipalAsString() {
		return forcePrincipalAsString;
	}

	/**
	 * @param forcePrincipalAsString The forcePrincipalAsString to set.
	 */
	public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
		this.forcePrincipalAsString = forcePrincipalAsString;
	}

}
