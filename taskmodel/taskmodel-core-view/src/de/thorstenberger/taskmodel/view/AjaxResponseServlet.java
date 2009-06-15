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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

/**
 * @author Thorsten Berger
 *
 */
public class AjaxResponseServlet extends HttpServlet {

	public static final String RESPONSE = "response";
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = (String)request.getAttribute( "contentType" );
		Object resp = request.getAttribute( RESPONSE );
		if( resp instanceof String ){
			response.setContentType( "text/plain" );
			PrintWriter out = response.getWriter();
			out.print( resp );
			out.flush();
		}else if( resp instanceof Document ){
			response.setContentType( "text/xml" );
			XMLOutputter outPutter = new XMLOutputter();
			outPutter.output( (Document)resp, response.getOutputStream() );
		}
		
		super.service(request, response);
	}

	
}
