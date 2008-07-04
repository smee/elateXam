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

import junittask.subtasklet.SubTasklet_JUnit;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.AddOnSubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.view.SubTaskView;
import de.thorstenberger.taskmodel.view.ViewContext;

public class SubTaskView_JUnit extends SubTaskView {
	private static int CORRECTION_RTN = -1;
	private SubTasklet_JUnit just;

	public SubTaskView_JUnit(AddOnSubTasklet tasklet) {
		this.just=(SubTasklet_JUnit)tasklet;
	}

	public String getRenderedHTML( ViewContext context, int relativeTaskNumber) {
		return getRenderedHTML( relativeTaskNumber, false );
	}

	public String getRenderedHTML(int relativeTaskNumber, boolean corrected) {
		StringBuffer ret = new StringBuffer();

		String textAreaID="junitTextAreaId"+relativeTaskNumber;

		if(!corrected)//enable syntax highlighting
			ret.append("<script src=\"/taskmodel-core-view/codepress/codepress.js\" type=\"text/javascript\"></script>");

		ret.append("<div align=\"center\">\n");
		ret.append("<textarea" +
				" id=\""+textAreaID+"\"");

		if(!corrected)//enable syntax highlighting
			ret.append(" class=\"codepress java linenumbers-on autocomplete-off\"");

		ret.append(" name=\"task[" + relativeTaskNumber + "].junit\"" +
				" cols=\"" + just.getTextFieldWidth() +
				"\" rows=\"" + just.getTextFieldHeight() +
				"\" onChange=\"setModified()" +
				"\"" + ( corrected ? "disabled=\"disabled\"" : "" ) +
		">\n");

		ret.append( just.getClassDef() );
		ret.append("</textarea></div>\n");

		ret.append( "<input type=\"hidden\" id=\"task_" + relativeTaskNumber + ".code\" name=\"task[" + relativeTaskNumber + "].code\">\n" );

		if(corrected) {//render correction results
			ret.append("<div class=\"problem\">\n");
			ret.append("JUnit-Ausgaben:<br><br>");
			ret.append(escapeCR(escapeHTML(just.getJUnitResults())));//TODO escape html/xml
			ret.append("</div><br>");
		}else{//save code from code editor into hidden input
			ret.append( "<script type=\"text/javascript\">\n" );
			ret.append( " var preSave_task_" + relativeTaskNumber + " = function(){\n" );
			ret.append( " document.getElementById(\"task_" + relativeTaskNumber + ".code\").value = "+textAreaID+".getCode();\n" );
			ret.append( "};\n" );
			ret.append( "preSaveManager.registerCallback( preSave_task_" + relativeTaskNumber + " );\n" );
			ret.append( "</script>\n" );
		}
		return ret.toString();

	}

	public String getCorrectedHTML( ViewContext context, int relativeTaskNumber ){
		return getRenderedHTML( CORRECTION_RTN, true );
	}

	public String getCorrectionHTML( String actualCorrector, ViewContext context ){
	    StringBuffer ret = new StringBuffer();
	    ret.append( getRenderedHTML( CORRECTION_RTN, true ) );

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
