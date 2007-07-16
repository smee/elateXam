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

package de.thorstenberger.taskmodel.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try.ProgressInformation;

/**
 * 
 * @author Thorsten Berger
 *
 */
public class ExecuteAction extends org.apache.struts.action.Action {
    
	private Log log = LogFactory.getLog( ExecuteAction.class );
	
	// TODO Locale dependance
	private NumberFormat nf = NumberFormat.getPercentInstance();

    
    public ExecuteAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
	   ActionMessages msgs = new ActionMessages();
	   ActionMessages errors = new ActionMessages();

		int page;
		long id;
		try {
			id = Long.parseLong( request.getParameter( "id" ) );
			page = Integer.parseInt( request.getParameter("page")==null ? "1" : request.getParameter("page") );
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

		ComplexTaskInfoVO ctivo = new ComplexTaskInfoVO();
		
		ctivo.setLogin( delegateObject.getLogin() );
		ctivo.setUserName( delegateObject.getUserName() );
		ctivo.setReturnURL( delegateObject.getReturnURL() );
		
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
		

		if( !taskDef.isActive() ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "task.inactive" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
    	
		
		String todo = request.getParameter( "todo" );

		try {
		
			if( "new".equals( todo ) ){		// new try
				ct.startNewTry( Integer.parseInt( request.getParameter( "try" ) ) );
				// TODO logging
				log.info("Student starts new try.");
				
			}else if( "continue".equals( todo ) ){	// continue try
				ct.continueLastTry();
				log.info("Student continues the try.");
				
			}else{
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "invalid.parameter" ) );
				saveErrors( request, errors );
				return mapping.findForward( "error" );
			}
		
		
		} catch (IllegalStateException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage() ) );
			saveErrors( request, errors );
			log.info( e );
			return mapping.findForward( "error" );
		} catch (NumberFormatException e1) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "invalid.parameter" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
    	
		populateVO( ctivo, taskDef, ct, page );
		request.setAttribute( "Task", ctivo );
		
		// add the navigation menu
		NavigationRootNode nrn = new NavigationRootNode( ct, id, page );
		request.setAttribute( "rootNode", nrn );
		NavigationNodeFormatter nnf = new NavigationNodeFormatter( id, request.getContextPath() + mapping.findForward( "execute" ).getPath(), request, response );
		request.setAttribute( "nodeFormatter", nnf );
		request.setAttribute( "expanded", true );
		
		
		// add the subtasklets
		List<SubTasklet> subtasklets = ct.getActiveTry().getPage( page ).getSubTasklets();
		List<SubTaskletInfoVO> stivos = new ArrayList<SubTaskletInfoVO>();
		int i = 0;
		for( SubTasklet subTasklet : subtasklets ){
			SubTaskletInfoVO stivo = new SubTaskletInfoVO();
			stivo.setHint( subTasklet.getHint() );
			stivo.setProblem( ParserUtil.getProblem( subTasklet.getProblem() ) );
			stivo.setReachablePoints( subTasklet.getReachablePoints() );
			stivo.setVirtualSubTaskletNumber( subTasklet.getVirtualSubtaskNumber() );
			stivo.setRenderedHTML( SubTaskViewFactory.getSubTaskView( subTasklet ).getRenderedHTML( request, i++ ) );
			stivos.add( stivo );
		}
		request.setAttribute( "SubTasklets", stivos );
		
		
    	return mapping.findForward( "success" );
    	
    }
    
    
    private void populateVO( ComplexTaskInfoVO ctivo, TaskDef_Complex ctd, ComplexTasklet ct, int page ){
    	
    	ctivo.setTitle( ctd.getTitle() );
    	ctivo.setTaskId( ctd.getId() );
    	
		if( ct.getComplexTaskDefRoot().hasTimeRestriction() ){
			long deadline = ct.getActiveTry().getStartTime() +
								ct.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() * 60 * 1000;
			ctivo.setRemainingTimeMillis( deadline - System.currentTimeMillis() );
		}else
			ctivo.setRemainingTimeMillis( -1 );
		
		ctivo.setTimeRestricted( ct.getComplexTaskDefRoot().hasTimeRestriction() );
		ProgressInformation pi = ct.getActiveTry().getProgressInformation();
		ctivo.setEverythingProcessed( pi.getProgressPercentage() == 1 );
		
		ctivo.setPage( page );
		ctivo.setNumOfPages( ct.getActiveTry().getNumberOfPages() );
		ctivo.setActualTry( ct.getComplexTaskHandlingRoot().getNumberOfTries() );
		ctivo.setNumOfTries( ctd.getComplexTaskDefRoot().getTries() );
		ctivo.setTryStartTime( DateUtil.getStringFromMillis( ct.getActiveTry().getStartTime() ) );
		ctivo.setProcessPercentage( nf.format( pi.getProgressPercentage() ) );
		ctivo.setNumOfSubtasklets( pi.getNumOfSubtasklets() );
		ctivo.setNumOfProcessedSubtasklets( pi.getNumOfProcessedSubtasklets() );

		if( ct.getComplexTaskDefRoot().hasTimeRestriction() )
			ctivo.setDeadline( DateUtil.getStringFromMillis(
					ct.getActiveTry().getStartTime() + ct.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() * 60 * 1000 ) );
		else
			ctivo.setDeadline( "-" );
		
		ctivo.setHashCode( "" + ct.getActiveTry().getPage( page ).getHash() );
		

    }
    

}