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

import java.text.NumberFormat;
import java.util.Map;

import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
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

	/**
	 * @param relativeTaskNumber number of the task on the current html page
	 * @return
	 */
	public abstract String getRenderedHTML( ViewContext context, int relativeTaskNumber );

	public abstract String getCorrectedHTML( ViewContext context, int relativeTaskNumber );

	public abstract String getCorrectionHTML( String actualCorrector, ViewContext context );


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

	protected String getCorrectorPointsInputString(String actualCorrector, String taskTypePrefix, SubTasklet subtasklet) {
		NumberFormat nF = NumberFormat.getNumberInstance();

	    String points = "";
	    if( !subtasklet.isAutoCorrected() ){
	    	
	    	if( subtasklet.isCorrectedByCorrector( actualCorrector ) )
	    		points = nF.format( subtasklet.getPointsByCorrector( actualCorrector ) );
	    	else
	    		points = nF.format( 0 );

		    return "<br><div align=\"right\">Punkte: " +
		    		"<input type=\"text\" name=\"task[0]."+taskTypePrefix+"_points\" size=\"4\" value=\"" + points + "\"></div><br>";

	    }
	    else
	    	return "";
	}

}
