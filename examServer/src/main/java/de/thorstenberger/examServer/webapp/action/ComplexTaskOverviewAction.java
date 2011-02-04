/*

Copyright (C) 2005 Thorsten Berger

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

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.util.DateUtil;
import de.thorstenberger.examServer.webapp.vo.ComplexTaskInfoVO;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.impl.TaskModelViewDelegateObjectImpl;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskOverviewAction extends BaseAction {

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
			throw new RuntimeException( e );
		}

		TaskManager tm = getTaskManager();
		TaskDef td = tm.getTaskDef( taskId );

		TaskDef_Complex ctd;

		try {
			ctd = (TaskDef_Complex)td;
		} catch (ClassCastException e) {
			throw new RuntimeException( "invalid type: \"" + td.getType() + "\", " + e.getMessage() );
		}

    if (ctd == null) {
      saveMessages(request, new ActionMessage("errors.invalidtask", taskId));
      return mapping.findForward("mainMenu");
    }
		ComplexTaskInfoVO tivo = new ComplexTaskInfoVO();
		tivo.setActive( ctd.isActive() );
		if( ctd.getDeadline() != null ) {
      tivo.setDeadline( DateUtil.getStringFromMillis( ctd.getDeadline() ) );
    }
		tivo.setShortDescription( ctd.getShortDescription() );
		tivo.setStopped( ctd.isStopped() );
		tivo.setTitle( ctd.getComplexTaskDefRoot().getTitle() );
		tivo.setType( ctd.getType() );
		tivo.setId( "" + ctd.getId() );

		tivo.setMaxTries( ctd.getComplexTaskDefRoot().getTries() );
		tivo.setTime( ctd.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() );
		tivo.setStartText( ctd.getComplexTaskDefRoot().getStartText() );
		tivo.setShowHandlingHintsBeforeStart( ctd.getComplexTaskDefRoot().isShowHandlingHintsBeforeStart() );
		tivo.setSpecificDescription( ctd.getComplexTaskDefRoot().getDescription() );

		if( request.getUserPrincipal() == null )
      throw new RuntimeException( "Not logged in." );

		String login = request.getUserPrincipal().getName();
		UserManager userManager = (UserManager)getBean( "userManager" );
		User user = userManager.getUserByUsername( login );

		ComplexTasklet tasklet = (ComplexTasklet) tm.getTaskletContainer().getTasklet( taskId, login );
		if( tasklet == null ) {
      tasklet = (ComplexTasklet) tm.getTaskletContainer().createTasklet( taskId, login );
    }

		tivo.setUsedTries( tasklet.getComplexTaskHandlingRoot().getNumberOfTries() );
		tivo.setCanStartNewTry( tasklet.canStartNewTry() );
		tivo.setCanContinueTry( tasklet.canContinueTry() );

		if( tasklet.canContinueTry() ){
			if( tasklet.getComplexTaskDefRoot().hasTimeRestriction() ) {
        tivo.setCtDeadline( DateUtil.getStringFromMillis(
						tasklet.getActiveTry().getStartTime() +tasklet.getActiveTry().getTimeExtension() + ctd.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() * 60 * 1000 ) );
      }
		}

		if( ctd.isShowCorrectionToUsers() && tasklet.hasOrPassedStatus (Tasklet.Status.SOLVED ) ){
			tivo.setCorrectionVisible( true);
		}


		TaskModelViewDelegateObject delegateObject = new TaskModelViewDelegateObjectImpl( taskId,
				tm,
				login, user.getFirstName() + " " + user.getLastName(),
                response.encodeURL(createReturnUrl(request, taskId)),
                ((ConfigManager) getBean("configManager")).getRandomSeed());



//		request.getSession().setAttribute( EPReservedKeys.PRINCIPAL, request.getUserPrincipal() );
//		request.getSession().setAttribute( TaskContants.TASK_MODEL_VIEW_DELEGATE_OBJECT_KEY_PREFIX + taskId, delegateObject );
		String sessionId = request.getSession().getId();
		TaskModelViewDelegate.storeDelegateObject( sessionId, taskId, delegateObject );

		request.setAttribute( "task", tivo );

		return mapping.findForward( "success" );


	}

    /**
     * Create an absolute url that should be used as return url.
     *
     * @param request
     * @param taskId
     * @return
     */
    protected String createReturnUrl(HttpServletRequest request, long taskId) {
        return request.getContextPath() + "/TaskViewFactory.html?taskId=" + taskId;
    }

    /**
     * Get a preconfigured instance of {@link TaskManager}.
     *
     * @return
     */
    protected TaskManager getTaskManager() {
        return (TaskManager)getBean( "TaskManager" );
    }


}
