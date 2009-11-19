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
package de.thorstenberger.examServer.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletContainer;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * @author Steffen Dienst
 *
 */
public class IncreaseTimeExtensionAction extends BaseAction {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionMessages errors = new ActionMessages();
		
		long taskId;
		try {
			taskId = Long.parseLong( request.getParameter("taskId") );
		} catch (NumberFormatException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", e.getMessage() ) );
			saveErrors( request, errors );
			return mapping.findForward( "noSelection" );
		}
		String userId = request.getParameter("userId");
		
		TaskManager manager = (TaskManager)getBean( "TaskManager" );
		TaskletContainer container = (TaskletContainer)getBean( "TaskletContainer" );
		
		Tasklet tasklet = container.getTasklet(taskId, userId);
		if(tasklet instanceof ComplexTasklet) {
			ComplexTasklet ct = (ComplexTasklet) tasklet;
			Try activeTry = ct.getActiveTry();
			// increase time by 5 minutes
			// TODO make configurable
			activeTry.setTimeExtension(activeTry.getTimeExtension()+5*60*1000);
			container.storeTasklet(tasklet);
		}
		return mapping.findForward( "success" );
	}
}
