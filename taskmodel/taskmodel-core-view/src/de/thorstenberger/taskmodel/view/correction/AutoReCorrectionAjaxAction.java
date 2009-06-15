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

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletContainer;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.view.AjaxResponseServlet;

/**
 * @author Thorsten Berger
 *
 */
public class AutoReCorrectionAjaxAction extends Action {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionMessages errors = new ActionMessages();

		long id;
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

		if( !delegateObject.isPrivileged() ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "not.privileged" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
	
		
		TaskDef_Complex tdc = (TaskDef_Complex)delegateObject.getTaskDef();

		//		if( "true".equals( request.getParameter( "start" ) ) ) 
//			runReCorrectionThread( delegateObject.getTaskManager().getTaskletContainer(), id );
//		
//		Document responseDocument = new Document( new Element("ReCorrection") );
//		responseDocument.addContent( (new Element( "running" )).setText( "" + running ) );
//		responseDocument.addContent( (new Element( "max" )).setText( "" + maxTasklets ) );
//		responseDocument.addContent( (new Element( "progress" )).setText( "" + progress ) );
//		
//		request.setAttribute( AjaxResponseServlet.RESPONSE, responseDocument );
//

		synchronized(this){
			List<Tasklet> ts;
			try {
				ts = delegateObject.getTaskManager().getTaskletContainer().getTasklets( id );
			} catch (TaskApiException e) {
				throw new TaskModelPersistenceException( e );
			}
			for( Tasklet t : ts ){
				ComplexTasklet ct = (ComplexTasklet)t;
				ct.doAutoCorrection();
			}
		}
		
		
		request.setAttribute( AjaxResponseServlet.RESPONSE, "OK" );
		return mapping.findForward( "success" );
		
	}
	
//	private Executor executor = Executors.newSingleThreadExecutor();
//	private volatile boolean running = false;
//	private int progress = 0;
//	private int maxTasklets = 0;
//	
//	private synchronized void runReCorrectionThread( final TaskletContainer taskletContainer, final long id ){
//		
//		if( !running ){
//			running = true;
//			executor.execute( new Runnable(){
//				
//				/* (non-Javadoc)
//				 * @see java.lang.Runnable#run()
//				 */
//				public void run() {
//					List<Tasklet> ts;
//					try {
//						ts = taskletContainer.getTasklets( id );
//					} catch (TaskApiException e) {
//						throw new TaskModelPersistenceException( e );
//					}
//					progress = 0;
//					maxTasklets = ts.size();
//					for( Tasklet t : ts ){
//						ComplexTasklet ct = (ComplexTasklet)t;
//						ct.doAutoCorrection();
//						progress++;
//					}
//					running = false;
//				}
//				
//			});
//		}
//		
//	}
	
}
