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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;

/**
 * @author Thorsten Berger
 *
 */
public class ExcelReportServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

		String uri = request.getRequestURI();
		if( !uri.endsWith( ".xls" ) )
			throw new ServletException( "Invalid URI!" );
		int indexOfUnderscore = uri.lastIndexOf( '_' );
		if( indexOfUnderscore == -1 )
			throw new ServletException( "Invalid URI (taskId missing)!" );
		String idString = 
			uri.substring( indexOfUnderscore + 1 , uri.length() - ".xls".length() );
		
		long id;
		try {
			id = Long.parseLong( idString );
		} catch (NumberFormatException e) {
			throw new ServletException( "Invalid Parameter!" );
		}
			
		CorrectorDelegateObject delegateObject = (CorrectorDelegateObject)TaskModelViewDelegate.getDelegateObject( request.getSession().getId(), id );

		if( delegateObject == null ){
			throw new ServletException( "No Session!" );
		}

		if( !delegateObject.isPrivileged() ){
			throw new ServletException( "Not privileged!" );
		}
			
		response.setContentType( "application/msexcel" );
		OutputStream out = response.getOutputStream();
			
		try {
			delegateObject.getTaskManager().getReportBuilder().createExcelBinary( id, out );
		} catch (TaskApiException e) {
			throw new ServletException( e );
		}
		
	}



}
