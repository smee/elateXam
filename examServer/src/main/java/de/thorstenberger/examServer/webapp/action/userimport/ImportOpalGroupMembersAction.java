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
package de.thorstenberger.examServer.webapp.action.userimport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.RoleManager;
import de.thorstenberger.examServer.service.UserExistsException;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.webapp.action.BaseAction;
import de.thorstenberger.examServer.webapp.form.ImportOpalGroupMembersForm;
import de.thorstenberger.examServer.ws.opal.GroupMembersRequestElement;
import de.thorstenberger.examServer.ws.opal.GroupMembersWS_Service;
import de.thorstenberger.examServer.ws.opal.Member;
import de.thorstenberger.examServer.ws.opal.MemberList;

/**
 * Import user details from a OPAL group via the webservice implemented by BPS GmbH, Chemnitz for their hosted Olat
 * instance for saxonian universities.
 *
 * @author Steffen Dienst
 *
 */
public class ImportOpalGroupMembersAction extends BaseAction {
	@Override
	public ActionForward execute(
	    final ActionMapping mapping,
	    final ActionForm form,
	    final HttpServletRequest request,
	    final HttpServletResponse response)
	    throws Exception {

    final ImportOpalGroupMembersForm opalForm = (ImportOpalGroupMembersForm) form;

    if (!StringUtils.isEmpty(opalForm.getUserId()) && !StringUtils.isEmpty(opalForm.getGroupId())) {
      final List<Member> members = fetchGroupMembers(opalForm.getUserId(), opalForm.getGroupId());
      // storeImportedUsers(members);

      request.setAttribute("importedMembers", members);
    }

    return mapping.findForward("success");
	}

  /**
   * Add imported members as users into our own user management system.
   *
   * @param members
   *          opal members
   */
  private void storeImportedUsers(final List<Member> members) {
    final UserManager um = (UserManager) getBean("userManager");
    final RoleManager rm = (RoleManager) getBean("roleManager");

    for (final Member member : members) {
      try {
        um.getUserByUsername(member.getMemberId());
      } catch (final UsernameNotFoundException e) {
        final User user = new User();
        user.addRole(rm.getRole("student"));
        user.setUsername(member.getMemberId());
        user.setFirstName(member.getFirstname());
        user.setLastName(member.getLastname());

        try {
          um.saveUser(user);
        } catch (final UserExistsException e1) {
          log.error("User exists, this error should never occur!", e1);
        }
      }
    }
  }

  /**
   * Fetch all members of the given group via webservice.
   *
   * @param userId
   * @param groupId
   * @return
   */
  private List<Member> fetchGroupMembers(final String userId, final String groupId) {
    final GroupMembersWS_Service groupService = new GroupMembersWS_Service();
    final GroupMembersRequestElement req = new GroupMembersRequestElement();
    req.setOwnerId(userId);
    req.setGroupId(groupId);

    final MemberList groupMembers = groupService.getGroupMembersWSSOAP().getGroupMembers(req);
    return groupMembers.getMember();
  }

}
