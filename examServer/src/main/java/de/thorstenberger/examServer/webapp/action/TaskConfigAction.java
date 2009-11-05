/*

Copyright (C) 2007 Steffen Dienst

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

import de.thorstenberger.examServer.webapp.form.TaskForm;
import de.thorstenberger.examServer.webapp.vo.TaskInfoVO;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;

/**
 *
 * @author Steffen Dienst
 *
 */
public class TaskConfigAction extends BaseAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception {

		ActionMessages errors = new ActionMessages();

		String selectedTask = request.getParameter("taskId");

		long taskId;

		if( selectedTask == null )
			return mapping.findForward( "noSelection" );

		try {
			taskId = Long.parseLong( selectedTask );
		} catch (NumberFormatException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", e.getMessage() ) );
			saveErrors( request, errors );
			return mapping.findForward( "noSelection" );
		}

		TaskManager tm = (TaskManager)getBean( "TaskManager" );
		TaskDef td = tm.getTaskDef( taskId );

		if( td == null ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", "No such taskdef (any more)!") );
			saveErrors( request, errors );
			return mapping.findForward( "noSelection" );
		}


		TaskForm tf=(TaskForm) form;

		TaskInfoVO tivo=new TaskInfoVO();
		tivo.setId(Long.toString(td.getId()));
		tivo.setTitle(td.getTitle());
		tivo.setType(td.getType());
		Long dl=td.getDeadline();
		tivo.setDeadline(dl==null?"":dl.toString());
		tivo.setShortDescription( td.getShortDescription() );

		tf.setId(td.getId());
		tf.setStopped(td.isStopped());
		tf.setTitle(td.getTitle());
		tf.setType(td.getType());
		tf.setShortDescription( td.getShortDescription() );
		if(td instanceof TaskDef_Complex) {
			tf.setShowSolutionToStudents(((TaskDef_Complex)td).isShowCorrectionToUsers());
		}
		request.setAttribute( "task", tivo );
		return mapping.findForward("success");

	}

}
