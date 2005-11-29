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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.McSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskView_MC extends SubTaskView {

	private SubTasklet_MC mcSubTasklet;

	/**
	 *
	 */
	public SubTaskView_MC( SubTasklet_MC mcSubTasklet ) {
		super();
		this.mcSubTasklet = mcSubTasklet;
	}



	public String getRenderedHTML( ViewContext context, int relativeTaskNumber ){
		return getRenderedHTML( context, relativeTaskNumber, false );
	}

	private String getRenderedHTML( ViewContext context, int relativeTaskNumber, boolean corrected ){
		HttpServletRequest request=(HttpServletRequest) context.getViewContextObject();
		StringBuffer ret = new StringBuffer();

		// alle Antworten einf�gen
		ret.append("\n<table>\n");
		List<SubTasklet_MC.Answer> answers = mcSubTasklet.getAnswers();
		for(int j=0; j<answers.size(); j++){

			if( mcSubTasklet.getMcCategory().equals( SubTasklet_MC.CAT_SINGLESELECT ) )
				ret.append("<tr><td nowrap valign=top><input type=\"radio\" name=\"task[" + relativeTaskNumber +
						"].ss\" value=\"" + j + "\"" + checked( answers.get( j ) ) +
						( corrected ? " disabled=\"disabled\"" : "" ) +  " onChange=\"setModified()\">&nbsp;" +
						( corrected && answers.get( j ).isCorrect() ? getSymbolForCorrectedAnswer( request, answers.get( j ) ) : "" ) + "</td>\n");
			else
				ret.append("<tr><td nowrap valign=top><input type=\"checkbox\" name=\"task[" + relativeTaskNumber + "].ms_answer_" + j +
						"\" value=\"selected\"" + checked( answers.get( j ) ) +
						( corrected ? " disabled=\"disabled\"" : "" ) + " onChange=\"setModified()\">&nbsp;" +
						( corrected ? getSymbolForCorrectedAnswer( request, answers.get( j ) ) : "" ) +  "</td>\n");

			ret.append("<td>" + answers.get( j ) + "</td></tr>\n" );
		}
		ret.append("</table>");

		return ret.toString();
	}

	public String getCorrectedHTML( ViewContext context, int relativeTaskNumber ){
		return getRenderedHTML( context, relativeTaskNumber, true );
	}

	private String checked( SubTasklet_MC.Answer answer ){
		if( answer.isSelected() )
			return " checked=\"checked\"";
		else
			return "";
	}

	public String getCorrectionHTML( String actualCorrector, ViewContext context ){
	    return null;
	}


	public SubmitData getSubmitData( Map postedVarsForTask ) throws ParsingException{
		Set varNames = postedVarsForTask.keySet();
		Iterator it = varNames.iterator();
		McSubmitData mcSubmitData = null;

		while( it.hasNext() ){
			String varName = (String) it.next();
			String myPart = getMyPart( varName );

			if( myPart.startsWith("ss") ){

				// bei ss sollte das die einzige Variable sein, die vorkommt
				if( mcSubmitData != null )
					throw new ParsingException();

				mcSubmitData = new McSubmitData();
				mcSubmitData.setSelected(
						Integer.parseInt( (String) postedVarsForTask.get( varName ) ) );
				return mcSubmitData;


			}else if( myPart.startsWith("ms_") ){

				// ok, wenn die erste, dann mcSubmitData anlegen
				if( mcSubmitData == null )
					mcSubmitData = new McSubmitData();

				mcSubmitData.setSelected( getAnswerNoFromString(myPart) );

			}

		}

		// weder ss- noch ms-Variable, also Aufgabe nicht bearbeitet
		if( mcSubmitData == null )
			mcSubmitData = new McSubmitData();
		// TODO un�bersichtlich

		return mcSubmitData;
	}

	public CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException{
	    throw new MethodNotSupportedException();
	}


	private int getAnswerNoFromString( String part ) throws ParsingException{
		try {

			StringTokenizer st = new StringTokenizer(part, "_");
			st.nextToken();	// ms
			st.nextToken();	// answer
			return Integer.parseInt( st.nextToken() );	// Nummer

		} catch (NumberFormatException e) {
			throw new ParsingException( e );
		} catch (NoSuchElementException e1){
			throw new ParsingException( e1 );
		}
	}





//	private String getSymbolForCorrectedAnswer_SS( CorrectionData_MC.MCElementHandler.Task task, CorrectionData_MC.MCElementHandler.Task.Answer answer ){
//		TaskFileHandler tfh = corr.getTaskFileHandler();
//		TaskBean tfh_task = tfh.getTask( task.getId() );
//
//		if( corr.isCorrectAnswerDef( tfh_task, tfh_task.getAnswer( answer.getPrivateID() ) ) ){
//
//			if( corr.isCorrectAnsweredAnswer( task, answer ) )
//				return "<img src=\"" + VM.expandAllVars("UebManager") + "/pics/true.gif\">";
//			else
//				return "<img src=\"" + VM.expandAllVars("UebManager") + "/pics/false.gif\">";
//		}
//
//		return "";
//	}

	private String getSymbolForCorrectedAnswer( HttpServletRequest request , SubTasklet_MC.Answer answer ){

		if( answer.isCorrectlySolvedAnswer() )
			return "<img src=\"" + request.getContextPath() + "/pics/true.gif\">";
		else
			return "<img src=\"" + request.getContextPath() + "/pics/false.gif\">";

	}

}
