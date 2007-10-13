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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;

/**
 * @author Thorsten Berger
 *
 */
public class AJAXSaveStudentAnnotationAction extends Action {

	Log log = LogFactory.getLog( AJAXSaveStudentAnnotationAction.class );
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		   ActionMessages msgs = new ActionMessages();
		   ActionMessages errors = new ActionMessages();

		   // FIXME: error handling
		   
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
			
			logPostData( request, ct );
			
			boolean saved;
			String annotation = request.getParameter( "studentAnnotation");
			
			try {
				
				saved = ct.studentAnnotatesCorrection( annotation );
				
			} catch (IllegalStateException e) {
				errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( e.getMessage() ) );
				saveErrors( request, errors );
				log.info( e );
				return mapping.findForward( "error" );
			}
			
			if( saved ){
				msgs.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage("studentAnnotation.successful" ) );
				saveMessages( request, msgs );
			}else{
				errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage("studentAnnotation.no_comment" ) );
				saveErrors( request, errors );				
			}
			
			request.setAttribute( "contentType", "text/plain" );
			request.setAttribute( AjaxResponseServlet.RESPONSE, annotation );
			
			return mapping.findForward( "success" );

		
	}

	
	public static void logPostData( HttpServletRequest request, Tasklet tasklet ){
		Map vars = request.getParameterMap();
		StringBuffer parameters = new StringBuffer();
		Iterator keys = vars.keySet().iterator();
		while( keys.hasNext() ){
			String key = (String) keys.next();
			parameters.append( key + "=" + ((String[])vars.get( key ))[0] + "\n" );
		}
		tasklet.logPostData( "StudentAnnotation: posted parameters:\n" + parameters.toString(), request.getRemoteAddr() );
	}
	

}
