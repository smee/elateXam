/*

Copyright (C) 2004 Thorsten Berger

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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SubTaskView {

	/**
	 * 
	 */
	public SubTaskView() {
	}
	
	public abstract String getRenderedHTML( int relativeTaskNumber );
	
	public abstract String getCorrectedHTML( HttpServletRequest request );
	
	public abstract String getCorrectionHTML( HttpServletRequest request );
	

	/**
	 * 
	 * task[0].answer[1]
	 *
	 */
	public abstract SubmitData getSubmitData( Map postedVarsForTask ) throws ParsingException;
	
	public abstract CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException;
	
	protected String getMyPart( String varName ) throws ParsingException{
		try {
			
			return varName.substring( varName.indexOf( '.' ) + 1, varName.length() );
			
		} catch (StringIndexOutOfBoundsException e) {
			throw new ParsingException( e );
		}
	}
	
}
