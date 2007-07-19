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
package de.thorstenberger.taskmodel.view.correction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.Annotation;
import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.view.DateUtil;
import de.thorstenberger.taskmodel.view.ParserUtil;
import de.thorstenberger.taskmodel.view.SubTaskViewFactory;
import de.thorstenberger.taskmodel.view.SubTaskletInfoVO;

/**
 * @author Thorsten Berger
 *
 */
public class ShowCorrectionToCorrectorAction extends Action {

	private Log log = LogFactory.getLog( ShowCorrectionToCorrectorAction.class );
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		   ActionMessages errors = new ActionMessages();

			long id;
			String userId = request.getParameter( "userId" );
			
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
			request.setAttribute( "ReturnURL", delegateObject.getReturnURL() );

			TaskDef_Complex taskDef;		
			try {
				taskDef = (TaskDef_Complex) delegateObject.getTaskDef();
			} catch (TaskApiException e3) {
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", e3.getMessage() ) );
				saveErrors( request, errors );
				log.error( e3 );
				return mapping.findForward( "error" );
			}
			
			ComplexTasklet tasklet = (ComplexTasklet)delegateObject.getTaskManager().getTaskletContainer().getTasklet( id, userId );
			
			if( !delegateObject.isPrivileged() ){
				if( !delegateObject.getCorrectorLogin().equals( tasklet.getTaskletCorrection().getCorrector() ) ){
					errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "may.only.see.assigned.tasklets" ) );
					saveErrors( request, errors );
					return mapping.findForward( "error" );
				}
			}
			
			CorrectionInfoVO civo = new CorrectionInfoVO();
			civo.setTaskId( id );
			civo.setUserId( userId );
			civo.setPoints( tasklet.getTaskletCorrection().getPoints() != null ? "" + tasklet.getTaskletCorrection().getPoints() : "-" );
			civo.setStatus( tasklet.getStatus().getValue() );
			civo.setCorrectorLogin( tasklet.getTaskletCorrection().getCorrector() );
			civo.setCorrectorHistory( tasklet.getTaskletCorrection().getCorrectorHistory() );
			civo.setAnnotation( ParserUtil.escapeCR( tasklet.getTaskletCorrection().getCorrectorAnnotation() ) );
			civo.setNumOfTry( tasklet.getComplexTaskHandlingRoot().getNumberOfTries() );
			try {
				civo.setTryStartTime( DateUtil.getStringFromMillis( tasklet.getSolutionOfLatestTry().getStartTime() ) );
			} catch (IllegalStateException e) {
				civo.setTryStartTime( "-" );
			}
			UserInfo ui = delegateObject.getTaskManager().getTaskFactory().getUserInfo( userId );
			if( ui == null ){
				civo.setUnregisteredUser( true );
			}else{
				if( delegateObject.isPrivileged() )
					civo.setUserName( ui.getFirstName() + " " + ui.getName() );
				else{
					civo.setUserNameInvisible( true );
				}
			}

			List<CorrectionInfoVO.AnnotationInfoVO> acknowledgedAnnotations = new ArrayList<CorrectionInfoVO.AnnotationInfoVO>();
			List<CorrectionInfoVO.AnnotationInfoVO> nonAcknowledgedAnnotations = new ArrayList<CorrectionInfoVO.AnnotationInfoVO>();
			for( Annotation anno : tasklet.getTaskletCorrection().getStudentAnnotations() )
				if( anno.isAcknowledged() )
					acknowledgedAnnotations.add( civo.new AnnotationInfoVO( DateUtil.getStringFromMillis( anno.getDate() ), ParserUtil.escapeCR( anno.getText() ) ) );
				else
					nonAcknowledgedAnnotations.add( civo.new AnnotationInfoVO( DateUtil.getStringFromMillis( anno.getDate() ), ParserUtil.escapeCR( anno.getText() ) ) );
			civo.setAcknowledgedAnnotations( acknowledgedAnnotations );
			civo.setNonAcknowledgedAnnotations( nonAcknowledgedAnnotations );
			
			Try latestTry;
			try {
				latestTry = tasklet.getSolutionOfLatestTry();
			} catch (IllegalStateException e) {
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage() ) );
				saveErrors( request, errors );
				log.info( e );
				return mapping.findForward( "error" );
			}		
			
			// add the subtasklets
			List<SubTaskletInfoVO> stivos = new ArrayList<SubTaskletInfoVO>();
			List<Page> pages = latestTry.getPages();
			int i = 0;
			for( Page page : pages ){
				List<SubTasklet> subtasklets = page.getSubTasklets();
				for( SubTasklet subTasklet : subtasklets ){
					SubTaskletInfoVO stivo = new SubTaskletInfoVO();
					stivo.setHint( subTasklet.getHint() );
					stivo.setProblem( ParserUtil.getProblem( subTasklet.getProblem() ) );
					stivo.setReachablePoints( subTasklet.getReachablePoints() );
					stivo.setVirtualSubTaskletNumber( subTasklet.getVirtualSubtaskNumber() );
					stivo.setRenderedHTML( SubTaskViewFactory.getSubTaskView( subTasklet ).getCorrectedHTML( request, i++ ) );
					stivo.setCorrected( subTasklet.isCorrected() );
					stivo.setNeedsManualCorrection( subTasklet.isNeedsManualCorrection() );
					if( subTasklet.isCorrected() )
						stivo.setPoints( "" + subTasklet.getPoints() );
					stivos.add( stivo );
				}
			}
			
			request.setAttribute( "SubTasklets", stivos );
			request.setAttribute( "Correction", civo );
	
			return mapping.findForward( "success" );
			
	}

	

}
