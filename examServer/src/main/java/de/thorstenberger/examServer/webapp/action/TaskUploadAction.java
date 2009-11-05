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

import de.thorstenberger.examServer.tasks.TaskFactoryImpl;
import de.thorstenberger.examServer.webapp.form.TaskDefUploadForm;
import de.thorstenberger.taskmodel.TaskFactory;

public class TaskUploadAction extends BaseAction {

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception {

		TaskDefUploadForm tduf = (TaskDefUploadForm) form;

		TaskFactory tf = (TaskFactory) getBean( "TaskFactory" );

		if( tf instanceof TaskFactoryImpl) {
			long taskId = ((TaskFactoryImpl)tf).storeNewTaskDef(
					tduf.getTaskDefFile().getFileName(),
					tduf.getTaskDefFile().getFileData());
			ActionForward af = mapping.findForward("success");
			return new ActionForward( af.getPath() + "?taskId=" + taskId, true );
		}else {

			return mapping.findForward( "error" );
		}
	}

}
