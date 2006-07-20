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

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;

/**
 * @author Thorsten Berger
 *
 */
public class CommitAction extends Action {

	Log log = LogFactory.getLog( CommitAction.class );
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
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
			
			TaskModelViewDelegateObject delegateObject = TaskModelViewDelegate.getDelegateObject( request.getSession().getId(), id );
				if( delegateObject == null ){
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "no.session" ) );
				saveErrors( request, errors );
				return mapping.findForward( "error" );
			}
			request.setAttribute( "ReturnURL", delegateObject.getReturnURL() );
			
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
			
			SavePageAction.logPostData( request, ct );
			
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
			
			if( !taskDef.isActive() ){
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "task.inactive" ) );
				saveErrors( request, errors );
				return mapping.findForward( "error" );
			}

			
			
			
			// finally, commit the whole Task
			try {
				
				ct.submit();
				
			} catch (IllegalStateException e) {
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage() ) );
				saveErrors( request, errors );
				log.info( e );
				return mapping.findForward( "error" );
			}
		
		return mapping.findForward( "success" );
	}



}
