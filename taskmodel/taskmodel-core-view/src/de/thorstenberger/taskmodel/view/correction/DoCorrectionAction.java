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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.view.SubTaskViewFactory;
import de.thorstenberger.taskmodel.view.SubTaskletInfoVO;
import de.thorstenberger.taskmodel.view.correction.tree.CorrectionNodeFormatter;
import de.thorstenberger.taskmodel.view.correction.tree.SubTaskletRootNode;

/**
 * @author Thorsten Berger
 *
 */
public class DoCorrectionAction extends Action {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		   ActionMessages errors = new ActionMessages();

			long id;
			String userId = request.getParameter( "userId" );;
			String selectedSubTaskletNum = request.getParameter( "selectedSubTaskletNum" );
			SubTasklet selectedSubTasklet = null;
			
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

			ComplexTasklet tasklet = (ComplexTasklet)delegateObject.getTaskManager().getTaskletContainer().getTasklet( id, userId );
			
			if( !delegateObject.isPrivileged() ){
				if( !delegateObject.getCorrectorLogin().equals( tasklet.getTaskletCorrection().getCorrector() ) ){
					errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "may.only.correct.assigned.tasklets" ) );
					saveErrors( request, errors );
					return mapping.findForward( "error" );
				}
			}
			
			List<Page> pages = tasklet.getSolutionOfLatestTry().getPages();
			List<SubTasklet> subTasklets = new ArrayList<SubTasklet>();
			for( Page page : pages ){
				List<SubTasklet> sts = page.getSubTasklets();
				for( SubTasklet subTasklet : sts ){
					subTasklets.add( subTasklet );
					if( subTasklet.getVirtualSubtaskNumber().equals( selectedSubTaskletNum ) )
						selectedSubTasklet = subTasklet;
				}
				
			}
			
			SubTaskletRootNode rn = new SubTaskletRootNode( subTasklets, userId, id, selectedSubTaskletNum );
			CorrectionNodeFormatter cnf = new CorrectionNodeFormatter( id, userId, request.getContextPath() + mapping.findForward( "doCorrection" ).getPath(), request, response );
			request.setAttribute( "rootNode", rn );
			request.setAttribute( "nodeFormatter", cnf );
			request.setAttribute( "expanded", true );
			
			CorrectionInfoVO civo = new CorrectionInfoVO();
			civo.setTaskId( id );
			civo.setUserId( userId );
			civo.setPoints( tasklet.getTaskletCorrection().getPoints() != null ? "" + tasklet.getTaskletCorrection().getPoints() : "-" );
			civo.setStatus( tasklet.getStatus() );
			civo.setCorrectorLogin( tasklet.getTaskletCorrection().getCorrector() );
			civo.setCorrectorHistory( tasklet.getTaskletCorrection().getCorrectorHistory() );
			civo.setAnnotation( tasklet.getTaskletCorrection().getAnnotation() );
			
			
			request.setAttribute( "Correction", civo );
			
			
			// SubTasklet selected -> show it
			if( selectedSubTasklet != null ){
				
				SubTaskletInfoVO stivo = new SubTaskletInfoVO();
				stivo.setCorrected( selectedSubTasklet.isCorrected() );
				if( selectedSubTasklet.isCorrected() )
					stivo.setPoints( selectedSubTasklet.getPoints() + "" );
					
				stivo.setHint( selectedSubTasklet.getHint() );
				stivo.setCorrectionHint( selectedSubTasklet.getCorrectionHint() );
				stivo.setProblem( selectedSubTasklet.getProblem() );
				stivo.setReachablePoints( selectedSubTasklet.getReachablePoints() );
				stivo.setVirtualSubTaskletNumber( selectedSubTasklet.getVirtualSubtaskNumber() );
				
				stivo.setCorrectionHTML( SubTaskViewFactory.getSubTaskView( selectedSubTasklet ).getCorrectionHTML( request ) );
				if( stivo.getCorrectionHTML() == null )
					stivo.setCorrectedHTML( SubTaskViewFactory.getSubTaskView( selectedSubTasklet ).getCorrectedHTML( request ) );
				
				civo.setSubTasklet( stivo );				
				
			}
		
		return mapping.findForward( "success" );
	}

	

}
