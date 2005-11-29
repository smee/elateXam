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
package de.thorstenberger.taskmodel.view.correction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.Tasklet;

/**
 * @author Thorsten Berger
 *
 */
public class UnassignTaskletAction extends Action {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionMessages errors = new ActionMessages();

		long id;
		try {
			id = Long.parseLong( request.getParameter( "taskId" ) );
		} catch (NumberFormatException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "invalid.parameter" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}

		CorrectorDelegateObject delegateObject = (CorrectorDelegateObject)TaskModelViewDelegate.getDelegateObject( request.getSession().getId(), id );

		if( delegateObject == null ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "no.session" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}

		try {
			String[] userIds = request.getParameterValues("userId");
			if( userIds != null ){
				for (String userId : userIds) {
					Tasklet tasklet = delegateObject.getTaskManager().getTaskletContainer().getTasklet( id, userId );
					if(delegateObject.isPrivileged() ||
							(delegateObject.getCorrectorLogin()!=null && delegateObject.getCorrectorLogin().equals( tasklet.getTaskletCorrection().getCorrector())))
						tasklet.unassignFromCorrector();
				}
			}

		} catch (TaskApiException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage() ) );
			saveErrors( request, errors );
			return mapping.findForward( "success" );
		}

		if( "true".equals( request.getParameter( "fromTutorList" ) ) )
			return mapping.findForward( "tutorListSuccess" );
		if( "true".equals( request.getParameter( "fromAllTaskletsList" ) ) )
			return mapping.findForward( "successToAllTaskletsList" );

		// unassign button pushed in the DoCorrection page
		if( delegateObject.isPrivileged() ){	// if admin he can stay on the DoCorrection page
			return mapping.findForward( "success" );
		}else	// tutors are not allowed to correct tasklets not being assigned to them
			return mapping.findForward( "tutorListSuccess" );

	}

}
