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
package de.thorstenberger.taskmodel.view;

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
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * @author Thorsten Berger
 *
 */
public class ShowSolutionAction extends Action {

	private Log log = LogFactory.getLog( ShowSolutionAction.class );
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
	   ActionMessages errors = new ActionMessages();

		long id;
		try {
			id = Long.parseLong( request.getParameter( "id" ) );
		} catch (NumberFormatException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "invalid.parameter" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
		
		TaskModelViewDelegateObject delegateObject = (TaskModelViewDelegateObject)TaskModelViewDelegate.getDelegateObject( request.getSession().getId(), id );

		if( delegateObject == null ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "no.session" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
		request.setAttribute( "ReturnURL", delegateObject.getReturnURL() );
		
		TaskDef_Complex taskDef;		
		try {
			taskDef = (TaskDef_Complex) delegateObject.getTaskDef();
		} catch (ClassCastException e2) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "only.complexTasks.supported" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		} catch (TaskApiException e3) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", e3.getMessage() ) );
			saveErrors( request, errors );
			log.error( e3 );
			return mapping.findForward( "error" );
		}
		
		if( !taskDef.isShowCorrectionToUsers() ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "solutions.not.shown" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}

		SolutionInfoVO sivo = new SolutionInfoVO();
		
		sivo.setLogin( delegateObject.getLogin() );
		sivo.setUserName( delegateObject.getUserName() );
		
		ComplexTasklet ct;
		
		try {
			ct = (ComplexTasklet) delegateObject.getTasklet();
		} catch (ClassCastException e1) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "only.complexTasks.supported" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		} catch (TaskApiException e3) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "misc.error", e3.getMessage() ) );
			saveErrors( request, errors );
			log.error( e3 );
			return mapping.findForward( "error" );
		}
		
		if( ct == null ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "no.solution.available" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}    	
		
		Try latestTry;
		try {
			latestTry = ct.getSolutionOfLatestTry();
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
				if( subTasklet.isCorrected() )
					stivo.setPoints( "" + subTasklet.getPoints() );
				stivos.add( stivo );
			}
			
		}
		
		
		populateVO( sivo, taskDef, ct );
		request.setAttribute( "SubTasklets", stivos );
		request.setAttribute( "Solution", sivo );
		
		if( ct.hasOrPassedStatus( Tasklet.Status.CORRECTED ) ){
			sivo.setCanAnnotate( true );
//			request.setAttribute( "canAnnotate", true );
			if( ct.getTaskletCorrection().getStudentAnnotations().size() > 0 )
//				request.setAttribute( "actualAnnotation", ct.getTaskletCorrection().getStudentAnnotations().get( 0 ).getText() );
				if( !ct.getTaskletCorrection().getStudentAnnotations().get( 0 ).isAcknowledged() )
						sivo.setActualAnnotation( ParserUtil.escapeCR( ct.getTaskletCorrection().getStudentAnnotations().get( 0 ).getText() ) );
		}
		
		List<SolutionInfoVO.AnnotationInfoVO> annotations = new ArrayList<SolutionInfoVO.AnnotationInfoVO>();
		for( Annotation anno : ct.getTaskletCorrection().getStudentAnnotations() )
			if( anno.isAcknowledged() )
				annotations.add( sivo.new AnnotationInfoVO( DateUtil.getStringFromMillis( anno.getDate() ), ParserUtil.escapeCR( anno.getText() ) ) );
		sivo.setAnnotations( annotations );
		
    	return mapping.findForward( "success" );
    	
    }
    
    
    private void populateVO( SolutionInfoVO sivo, TaskDef_Complex ctd, ComplexTasklet ct ){
    	
    	sivo.setTaskId( ctd.getId() );
    	
    	if( ct.getTaskletCorrection().getPoints() != null )
    		sivo.setPoints( "" + ct.getTaskletCorrection().getPoints() );
    	else
    		sivo.setPoints( "-" );
		
		
		sivo.setStatus( ct.getStatus().getValue() );
		try {
			sivo.setTryStartTime( DateUtil.getStringFromMillis( ct.getSolutionOfLatestTry().getStartTime() ) );
		} catch (IllegalStateException e) {
			sivo.setTryStartTime( "-" );
		}
		

    }


}
