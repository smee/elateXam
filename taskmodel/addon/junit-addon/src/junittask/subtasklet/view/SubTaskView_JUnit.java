/*

Copyright (C) 2006 Steffen Dienst

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
package junittask.subtasklet.view;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junittask.subtasklet.SubTasklet_JUnit;

import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.view.SubTaskView;
import de.thorstenberger.taskmodel.view.ViewContext;

public class SubTaskView_JUnit extends SubTaskView {

	private SubTasklet_JUnit just;

	public SubTaskView_JUnit(AddOnSubTasklet tasklet) {
		this.just=(SubTasklet_JUnit)tasklet;
	}

	public String getRenderedHTML( ViewContext context, int relativeTaskNumber) {
		return getRenderedHTML( relativeTaskNumber, false );
	}

	public String getRenderedHTML(int relativeTaskNumber, boolean corrected) {
		StringBuffer ret = new StringBuffer();


		ret.append("<div align=\"center\">\n");
		ret.append("<textarea name=\"task[" + relativeTaskNumber + "].junit\" cols=\"" +
						just.getTextFieldWidth() + "\" rows=\"" + just.getTextFieldHeight() + "\" onChange=\"setModified()\"" +
						( corrected ? "disabled=\"disabled\"" : "" ) + ">\n");
		ret.append( just.getClassDef() );
		ret.append("</textarea></div>\n");
		if(corrected) {
			ret.append("<div class=\"problem\">\n");
			ret.append("JUnit-Ausgaben:<br><br>");
			ret.append(escapeCR(escapeHTML(just.getJUnitResults())));//TODO escape html/xml
			ret.append("</div><br>");
		}
		return ret.toString();

	}

	public String getCorrectedHTML( ViewContext context, int relativeTaskNumber ){
		return getRenderedHTML( -1, true );
	}

	public String getCorrectionHTML( String actualCorrector, ViewContext context ){
	    StringBuffer ret = new StringBuffer();
	    ret.append( getRenderedHTML( -1, true ) );

	    ret.append(getCorrectorPointsInputString(actualCorrector, "junit", just));

	    return ret.toString();
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getSubmitData(java.util.Map, int)
	 */
	public SubmitData getSubmitData(Map postedVarsForTask)
			throws ParsingException {

		Iterator it = postedVarsForTask.values().iterator();
		if( it.hasNext() )
			return new JUnitSubmitData( (String) it.next() );
		else
			throw new ParsingException();

		}

	public CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException{
	    Iterator it = postedVars.values().iterator();
	    if( it.hasNext() ){
	        float points;
            try {
                points = NumberFormat.getInstance().parse( (String) it.next() ).floatValue();
            } catch (ParseException e) {
                throw new ParsingException( e );
            }
            return new JUnitCorrectionSubmitData( points );
	    }else
	        throw new ParsingException();
	}

    private String escapeCR( String text ){
    	if( text == null )
    		return null;
        return text.replaceAll( "\n", "<br>" );
    }

    private String escapeHTML( String text ){
    	if( text == null )
    		return null;
        return text.replaceAll( "<" , "&lt;" ).
        				replaceAll( ">" , "&gt;" );
    }

}
