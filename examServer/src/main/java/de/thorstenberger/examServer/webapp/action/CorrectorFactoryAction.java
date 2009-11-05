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
package de.thorstenberger.examServer.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.impl.CorrectorDelegateObjectImpl;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectorFactoryAction extends BaseAction {

	public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws Exception {
		
		long taskId;
		
		try {
			taskId = Long.parseLong( request.getParameter( "taskId" ) );
		} catch (NumberFormatException e) {
			throw new RuntimeException( "Invalid parameter taskId! " + e.getMessage() );
		}
		
		TaskManager tm = (TaskManager)getBean( "TaskManager" );
		TaskDef td = tm.getTaskDef( taskId );
		
		if( td.getType().equals( TaskContants.TYPE_COMPLEX ) ){

			TaskDef_Complex ctd;
			
			try {
				ctd = (TaskDef_Complex)td;
			} catch (ClassCastException e) {
				throw new RuntimeException( "invalid type: \"" + td.getType() + "\", " + e.getMessage() );
			}
			
			if( request.getUserPrincipal() == null ){
				throw new RuntimeException( "Not logged in." );
			}
			
			String login = request.getUserPrincipal().getName();
			UserManager userManager = (UserManager)getBean( "userManager" );
			User user = userManager.getUserByUsername( login );
			String returnURL = response.encodeURL( request.getContextPath() + "/tutorMainMenu.html" );

			boolean privileged = request.isUserInRole( "admin" );
			
			CorrectorDelegateObject delegateObject = new CorrectorDelegateObjectImpl( login, user.getFirstName() + " " + user.getLastName(), tm, ctd, privileged, returnURL );
			String sessionId = request.getSession().getId();
			TaskModelViewDelegate.storeDelegateObject( sessionId, taskId, delegateObject );
			
			response.sendRedirect( response.encodeRedirectURL( "/taskmodel-core-view/tutorCorrectionOverview.do?taskId=" + taskId ) );
			
			return null;
			
		}else{
			
			throw new RuntimeException( "unsupported type: \"" + td.getType() + "\"" );
			
		}
	}

}
