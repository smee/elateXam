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
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.TextCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.TextSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskView_TEXT extends SubTaskView {

	private SubTasklet_Text textSubTasklet;

	/**
	 *
	 */
	public SubTaskView_TEXT( SubTasklet_Text textSubTasklet ) {
		this.textSubTasklet = textSubTasklet;
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getRenderedHTML(int)
	 */
	@Override
  public String getRenderedHTML( ViewContext context, int relativeTaskNumber) {
		return getRenderedHTML( relativeTaskNumber, false );
	}

	public String getRenderedHTML(int relativeTaskNumber, boolean corrected) {
		StringBuffer ret = new StringBuffer();

		ret.append("<div align=\"center\">\n");
		ret.append("<textarea name=\"task[" + relativeTaskNumber + "].text\" cols=\"" +
						textSubTasklet.getTextFieldWidth() + "\" rows=\"" + textSubTasklet.getTextFieldHeight() + "\" onChange=\"setModified()\"" +
            "readonly=" + (corrected ? "\"true\"" : "\"false\"") + ">\n");
		ret.append( textSubTasklet.getAnswer() );
		ret.append("</textarea></div>\n");

		return ret.toString();

	}

	@Override
  public String getCorrectedHTML( ViewContext context, int relativeTaskNumber ){
		return getRenderedHTML( -1, true );
	}

	@Override
  public String getCorrectionHTML( String actualCorrector, ViewContext context ){
	    StringBuffer ret = new StringBuffer();
	    ret.append( getRenderedHTML( -1, true ) );

	    ret.append(getCorrectorPointsInputString(actualCorrector, "text", textSubTasklet));

	    return ret.toString();
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getSubmitData(java.util.Map, int)
	 */
	@Override
  public SubmitData getSubmitData(Map postedVarsForTask)
			throws ParsingException {

		Iterator it = postedVarsForTask.values().iterator();
		if( it.hasNext() )
			return new TextSubmitData( (String) it.next() );
		else
			throw new ParsingException();

		}

	@Override
  public CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException{
	    Iterator it = postedVars.values().iterator();
	    if( it.hasNext() ){
	        float points;
            try {
                points = NumberFormat.getInstance().parse( (String) it.next() ).floatValue();
            } catch (ParseException e) {
                throw new ParsingException( e );
            }
            return new TextCorrectionSubmitData( points );
	    }else
	        throw new ParsingException();
	}

}
