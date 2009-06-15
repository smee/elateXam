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
package de.thorstenberger.taskmodel.view.statistics;

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
		int indexOfLastSlash = uri.lastIndexOf( '/' );
		String fileName = uri.substring( indexOfLastSlash + 1, uri.length() - ".xls".length() );
		
		
		int startOfTaskId = fileName.indexOf( '_' ) + 1;
			if( startOfTaskId == -1 )
				throw new ServletException( "Invalid URI (taskId missing)!" );
		
		int endOfTaskId = fileName.indexOf( ',', startOfTaskId );
		if( endOfTaskId == -1 )
			endOfTaskId = fileName.length();
		
		String idString = 
			fileName.substring( startOfTaskId, endOfTaskId );
		
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
			if( fileName.startsWith( "report" ) ){
				
				delegateObject.getTaskManager().getReportBuilder().createExcelBinary( id, out );
				
			}else if( fileName.startsWith( "MCBlockReport" ) ){
				
				int startOfCategoryId = endOfTaskId + 1;
				int endOfCategoryId = fileName.indexOf( ',', startOfCategoryId );
				String categoryId = fileName.substring( startOfCategoryId, endOfCategoryId );
				int startOfBlockIndex = endOfCategoryId + 1;
				int endOfBlockIndex = fileName.length();
				int blockIndex;
				try {
					blockIndex = Integer.parseInt( fileName.substring( startOfBlockIndex, endOfBlockIndex ) );
				} catch (NumberFormatException e) {
					throw new ServletException( "Invalid Parameter!" );
				}
				delegateObject.getTaskManager().getReportBuilder().createExcelAnalysisForBlock( id, categoryId, blockIndex, out );
				
			}else
				response.sendError( 404 );
			
		} catch (TaskApiException e) {
			throw new ServletException( e );
		}
		
	}



}
