/*

Copyright (C) 2007 Thorsten Berger

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
package de.thorstenberger.examServer.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.webapp.form.StudentDetailsForm;

/**
 * @author Thorsten Berger
 *
 */
public class StudentsInfoSubmitAction extends BaseAction {

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.webapp.action.BaseAction#execute(org.apache.struts.action.ActionMapping,
   * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    final ActionMessages messages = new ActionMessages();

    final UserManager userManager = (UserManager) getBean("userManager");
    final User user = userManager.getUserByUsername(request.getUserPrincipal().getName());

    final StudentDetailsForm userForm = (StudentDetailsForm) form;

    // run validation rules on this form
    final ActionMessages errors = form.validate(mapping, request);
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    user.setFirstName(userForm.getFirstName());
    user.setLastName(userForm.getLastName());
    user.setSemester(userForm.getSemester());
    user.setMatrikel(userForm.getMatrikel());
    userManager.saveUser(user);
    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("studentsInfo.saved"));

    saveMessages(request, messages);

    return mapping.findForward("success");
  }

}
