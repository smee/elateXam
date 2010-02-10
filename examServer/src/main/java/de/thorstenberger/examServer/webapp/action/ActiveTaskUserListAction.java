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
/**
 *
 */
package de.thorstenberger.examServer.webapp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.webapp.vo.ActiveUserVO;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletContainer;
import de.thorstenberger.taskmodel.Tasklet.Status;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try.ProgressInformation;

public class ActiveTaskUserListAction extends BaseAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TaskManager manager = (TaskManager)getBean( "TaskManager" );
		TaskletContainer container = (TaskletContainer)getBean( "TaskletContainer" );

		List<TaskDef> allTaskDefs = manager.getTaskDefs();
		
		List<Tasklet> allTasklets = new ArrayList<Tasklet>();
		for(TaskDef td: allTaskDefs)
			allTasklets.addAll(container.getTasklets(td.getId()));
		
		List<ActiveUserVO> vos = new ArrayList<ActiveUserVO>();

		for( Tasklet tasklet : allTasklets ){
			if(tasklet.getStatus() != Status.INPROGRESS)
				continue;

			ActiveUserVO vo = new ActiveUserVO();
        	vo.setTaskId( "" + tasklet.getTaskId() );
        	vo.setTaskTitle(manager.getTaskDef(tasklet.getTaskId()).getTitle());
        	vo.setUsername(tasklet.getUserId() );
        	if(tasklet instanceof ComplexTasklet) {        		
        	    ComplexTasklet ct = (ComplexTasklet) tasklet;
				Try activeTry = ct.getActiveTry();
				
				// count subtasklets
				ProgressInformation progressInformation = activeTry.getProgressInformation();
				int numSubtasklets = progressInformation.getNumOfSubtasklets();
				int numProcessedSubtasklets = progressInformation.getNumOfProcessedSubtasklets();
				vo.setStatus(numProcessedSubtasklets+"/"+numSubtasklets);
				
				if (ct.getComplexTaskDefRoot().hasTimeRestriction()) {
		            final long deadline = ct.getActiveTry().getStartTime() + ct.getActiveTry().getTimeExtension() + ct.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() * 60 * 1000;
		            long remainingMillis = deadline - System.currentTimeMillis();
		            vo.setRemainingMinutes(Long.toString(remainingMillis/60000));
		        } 					
        	}else{
        		vo.setRemainingMinutes("-");
        		vo.setStatus("aktiv");
        	}
        	vos.add(vo);
		}

		request.setAttribute( "ActiveUsers", vos );

		return mapping.findForward( "success" );
	}


}
