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
import org.apache.struts.action.ActionMessage;

import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;

/**
 * @author Thorsten Berger
 *
 */
public class TaskViewFactoryAction extends BaseAction {

	@Override
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

    if (td == null) {
      saveMessages(request, new ActionMessage("errors.invalidtask", taskId));
      return mapping.findForward("mainMenu");
    }

		if( td.getType().equals( TaskContants.TYPE_COMPLEX ) ){

			ActionForward caf = mapping.findForward( "complex" );
			return new ActionForward( caf.getPath() + "?taskId=" + taskId, true );

		} else
      throw new RuntimeException( "unsupported type: \"" + td.getType() + "\"" );
	}

}
