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

package de.elatePortal.autotool.view;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.elatePortal.autotool.SubTasklet_Autotool;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.view.SubTaskView;


public class SubTaskView_Autotool extends SubTaskView{

	private SubTasklet_Autotool autotoolSubTasklet;

	/**
	 *
	 */
	public SubTaskView_Autotool( SubTasklet_Autotool autotoolSubTasklet ) {
		this.autotoolSubTasklet = autotoolSubTasklet;
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getRenderedHTML(int)
	 */
	public String getRenderedHTML( HttpServletRequest request, int relativeTaskNumber) {
		return getRenderedHTML( relativeTaskNumber, false );
	}

	public String getRenderedHTML(int relativeTaskNumber, boolean corrected) {
		StringBuffer ret = new StringBuffer();

		// workaround: textarea nicht disabled
		//corrected = false;

		ret.append("<div align=\"left\">\n");
		ret.append("<textarea name=\"task[" + relativeTaskNumber + "].autotool\" cols=\"" +
						autotoolSubTasklet.getTextFieldWidth() + "\" rows=\"" + autotoolSubTasklet.getTextFieldHeight() + "\" onChange=\"setModified()\"" +
						( corrected ? "disabled=\"disabled\"" : "" ) + ">\n");
		ret.append( corrected?autotoolSubTasklet.getLastCorrectedAnswer():autotoolSubTasklet.getAnswer() );
		ret.append("</textarea></div>\n");

		if(corrected) {
			ret.append("<div class=\"problem\">\n");
			ret.append("Autotoolantwort:<br><br>");
			ret.append(autotoolSubTasklet.getAutotoolGradeDoc());
			ret.append("</div><br>");
		}

		return ret.toString();

	}

	public String getCorrectedHTML( HttpServletRequest request, int relativeTaskNumber ){
		return getRenderedHTML( -1, true );
	}

	public String getCorrectionHTML(String actualCorrector, HttpServletRequest request ){
	    StringBuffer ret = new StringBuffer();
	    ret.append( getRenderedHTML( -1, true ) );

	    ret.append(getCorrectorPointsInputString(actualCorrector, "autotool", autotoolSubTasklet));

	    return ret.toString();
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getSubmitData(java.util.Map, int)
	 */
	public SubmitData getSubmitData(Map postedVarsForTask)
	throws ParsingException {

		Iterator it = postedVarsForTask.keySet().iterator();
		while( it.hasNext() ) {
			String key=(String) it.next();
			if( getMyPart( key ).equals( "autotool" ) )
				return new AutotoolSubmitData( (String) postedVarsForTask.get(key) );
		}
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
            return new AutotoolCorrectionSubmitData( points );
	    }else
	        throw new ParsingException();
	}

}

