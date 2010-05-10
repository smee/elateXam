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
package de.thorstenberger.examServer.webapp.action.userimport;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.RandomStringUtils;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.RoleManager;
import de.thorstenberger.examServer.service.UserExistsException;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.webapp.action.BaseAction;
import de.thorstenberger.examServer.ws.opal.Member;

/**
 * Add imported members as users into our own user management system.
 *
 * @author Steffen Dienst
 *
 */
public abstract class AbstractImportMembersAction extends BaseAction {

  /**
   * Add imported members as users into our own user management system.
   * 
   * @param members
   *          opal members
   * @return all successfully imported users
   */
  protected List<Member> storeImportedUsers(final List<Member> members) {
    final UserManager um = (UserManager) getBean("userManager");
    final RoleManager rm = (RoleManager) getBean("roleManager");
    final List<Member> importedMembers = new ArrayList<Member>();

    for (final Member member : members) {
      try {
        um.getUserByUsername(member.getMemberId());
      } catch (final UsernameNotFoundException e) {
        final User user = new User();
        user.addRole(rm.getRole("student"));
        user.setUsername(member.getMemberId());
        user.setFirstName(member.getFirstname());
        user.setLastName(member.getLastname());
        // use random password, needs to be changed manually
        // or not used at all (i.e. use remote authentication)
        user.setPassword(RandomStringUtils.randomAlphanumeric(10));
        try {
          um.saveUser(user);
          importedMembers.add(member);
        } catch (final UserExistsException e1) {
          log.error("User exists, this error should never occur!", e1);
        }
      }
    }
    return importedMembers;
  }
}
