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

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.userdetails.UserDetails;

import de.thorstenberger.examServer.service.ConfigManager;

/**
 * @author Thorsten Berger
 *
 */
public class DAOAuthenticationProvider extends DaoAuthenticationProvider {

	private ConfigManager configManager;

	/**
	 * @param configManager
	 */
	public DAOAuthenticationProvider(ConfigManager configManager) {
		super();
		this.configManager = configManager;
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.providers.dao.DaoAuthenticationProvider#additionalAuthenticationChecks(org.acegisecurity.userdetails.UserDetails, org.acegisecurity.providers.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
		
		if( !configManager.isStudentsLoginEnabled() ){
			GrantedAuthority[] authorities = userDetails.getAuthorities();
			boolean granted = false;
			for( int i = 0; i<authorities.length; i++ ){
				if( authorities[i].getAuthority().equals( "admin" ) )
					granted = true;
				else if( authorities[i].getAuthority().equals( "tutor" ) )
					granted = true;
			}
			
			if( !granted )
				throw new AuthenticationServiceException( "Login disabled for student role." );
		}
		
		super.additionalAuthenticationChecks(userDetails, token);
	}
	
	
	

}
