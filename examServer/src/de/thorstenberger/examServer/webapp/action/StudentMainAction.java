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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.webapp.vo.TaskDefVO;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;

/**
 * @author Thorsten Berger
 *
 */
public class StudentMainAction extends BaseAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		TaskManager taskManager = (TaskManager)getBean( "TaskManager" );
		
		List<TaskDef> taskDefs = taskManager.getTaskDefs();
		
		List<TaskDefVO> tdvos = new ArrayList<TaskDefVO>();
		
		for( TaskDef taskDef : taskDefs ){
			
			if( !taskDef.isVisible() )
				continue;
			
			TaskDefVO tdvo = new TaskDefVO();
        	tdvo.setId( "" + taskDef.getId() );
        	tdvo.setTitle( taskDef.getTitle() );
        	tdvo.setShortDescription( taskDef.getShortDescription() );
        	tdvo.setType( taskDef.getType() );
//        	if( taskDef.getDeadline() != null )
//        		tdvo.setDeadline( DateUtil.getStringFromMillis( taskDef.getDeadline() ) );
        	tdvo.setStopped( taskDef.isStopped() );
        	tdvo.setActive( taskDef.isActive() );
        	tdvos.add( tdvo );
		}
		
		request.setAttribute( "TaskDefs", tdvos );
		
		ConfigManager configManager = (ConfigManager)getBean( "configManager" );
		if( configManager.isSetFlag( "askForSemester" ) ){
			UserManager userManager = (UserManager)getBean( "userManager" );
			User user = userManager.getUserByUsername( request.getUserPrincipal().getName() );
			request.setAttribute( "askForSemester", Boolean.TRUE );
			request.setAttribute( "semester", user.getPhoneNumber() );
		}
		return mapping.findForward( "success" );
	}

	
}
