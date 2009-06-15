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
import java.util.LinkedList;
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
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.ClozeCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.ClozeSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.ManualGapCorrection;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskView_CLOZE extends SubTaskView {

	private SubTasklet_Cloze clozeSubTasklet;

	/**
	 *
	 */
	public SubTaskView_CLOZE( SubTasklet_Cloze clozeSubTasklet ) {
		this.clozeSubTasklet = clozeSubTasklet;
	}

	/**
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getRenderedHTML(int)
	 */
	public String getRenderedHTML(ViewContext context, int relativeTaskNumber) {
		StringBuffer ret = new StringBuffer();

		List content = clozeSubTasklet.getContent();

//		ret.append("<br>");

		for( int i=0; i<content.size(); i++ ){
			if( content.get( i ) instanceof String ){
				ret.append( content.get( i ) );
			}else if( content.get( i ) instanceof SubTasklet_Cloze.Gap ){
				SubTasklet_Cloze.Gap gap = (SubTasklet_Cloze.Gap) content.get( i );
				ret.append( " &nbsp;<input type=\"text\" name=\"task[" + relativeTaskNumber + "].gap_" +
						gap.getIndex() + "\" size=\"" + gap.getInputLength() + "\" value=\"" +
								escapeQuotes( gap.getGapValue() ) + "\" onChange=\"setModified()\"" +
										">&nbsp;" );
			}
		}


		return ret.toString();
	}

	public String getCorrectedHTML( ViewContext context, int relativeTaskNumber ){
		StringBuffer ret = new StringBuffer();

		List content = clozeSubTasklet.getContent();
		HttpServletRequest request=(HttpServletRequest) context.getViewContextObject();

		for( int i=0; i<content.size(); i++ ){
			if( content.get( i ) instanceof String ){
				ret.append( content.get( i ) );
			}else if( content.get( i ) instanceof SubTasklet_Cloze.Gap ){
				SubTasklet_Cloze.Gap gap = (SubTasklet_Cloze.Gap) content.get( i );
				ret.append( "&nbsp;<b>\"" + gap.getGapValue() + "\"</b>&nbsp;" );
				if( gap.isCorrected() ){

					List<Correction> corrections = new LinkedList<Correction>();
					if( gap.isAutoCorrected() ){
						corrections.add( new Correction( null, true, gap.isCorrectByAutoCorrection() ) );
					}else{
						List<ManualGapCorrection> gcs = gap.getManualCorrections();
						for( ManualGapCorrection gc : gcs ){
							corrections.add( new Correction( gc.getCorrector(), false, gc.isCorrect() ) );
						}
					}

					for( Correction correction : corrections ){
						String corrector = correction.isAuto() ? "autom." : correction.getCorrector();
				        ret.append( "<span class=\"Cloze_CorrectorLabel\">" + corrector + ": </span><img src=\"" + request.getContextPath() + "/pics/" + correction.isCorrect() + ".gif\">&nbsp;&nbsp;" );
					}
				}else
				    ret.append( "<img src=\"" + request.getContextPath() + "/pics/questionmark.gif\">" );
				ret.append("&nbsp;\n");
			}
		}

		return ret.toString();

	}

	public String getCorrectionHTML( String actualCorrector, ViewContext context ){
		StringBuffer ret = new StringBuffer();

		List content = clozeSubTasklet.getContent();
		HttpServletRequest request=(HttpServletRequest) context.getViewContextObject();

		for( int i=0; i<content.size(); i++ ){
			if( content.get( i ) instanceof String ){
				ret.append( content.get( i ) );
			}else if( content.get( i ) instanceof SubTasklet_Cloze.Gap ){
			    ret.append("<br><br>\n");
			    SubTasklet_Cloze.Gap gap = (SubTasklet_Cloze.Gap) content.get( i );
				ret.append( "&nbsp;&nbsp;<b class=\"Cloze_Gap\">\"" + gap.getGapValue() + "\"</b>&nbsp;&nbsp;" );
				if( gap.isCorrected() ){

					if( gap.isAutoCorrected() ){
						ret.append( "<span class=\"Cloze_CorrectorLabel\">" + "autom." + ": </span><img src=\"" + request.getContextPath() + "/pics/" + gap.isCorrectByAutoCorrection() + ".gif\">&nbsp;&nbsp;" );
					}else{
						List<ManualGapCorrection> gcs = gap.getManualCorrections();

						for( ManualGapCorrection gc : gcs )
							ret.append( "<span class=\"Cloze_CorrectorLabel\">" + gc.getCorrector() + ": </span><img src=\"" + request.getContextPath() + "/pics/" + gc.isCorrect() + ".gif\">&nbsp;&nbsp;" );
					}

				}else
				    ret.append( "<img src=\"" + request.getContextPath() + "/pics/questionmark.gif\">" );


				if( !gap.isAutoCorrected() ){
				    ret.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\" name=\"task[0].gap_" + gap.getIndex() + "_correct\"" );
				    if( gap.isCorrectedByCorrector( actualCorrector ) && gap.isCorrectByCorrector( actualCorrector ) )
				        ret.append(" checked");
				    ret.append(" value=\"true\">");
				}
				ret.append("<br>");
				ret.append("&nbsp;&nbsp;[ ");

				String[] correct = gap.getCorrectValues();
				for( int j=0; j<correct.length - 1; j++ )
				    ret.append( correct[j] + " | " );

				ret.append( correct[ correct.length - 1] + " ]<br><br>");
			}
		}


		return ret.toString();

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getSubmitData(java.util.Map, int)
	 */
	public SubmitData getSubmitData(Map postedVarsForTask )
			throws ParsingException {
		ClozeSubmitData ret = new ClozeSubmitData();
		Set varNames = postedVarsForTask.keySet();
		Iterator it = varNames.iterator();

		while( it.hasNext() ){
			String varName = (String) it.next();
			String myPart = getMyPart( varName );

			int gapIndex = getGapIndex( myPart );
			ret.setGapValue( gapIndex, (String)postedVarsForTask.get( varName ) );

		}

		return ret;

	}


	public CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException{
	    ClozeCorrectionSubmitData ret = new ClozeCorrectionSubmitData();

		Set varNames = postedVars.keySet();
		Iterator it = varNames.iterator();

		while( it.hasNext() ){
			String varName = (String) it.next();

			int gapIndex = getGapIndex( getMyPart( varName ) );
			if( ( (String)postedVars.get( varName ) ).equals("true") )
			    ret.setCorrect( gapIndex );
		}

	    return ret;
	}


	private int getGapIndex( String part ) throws ParsingException{
		try {
			StringTokenizer st = new StringTokenizer( part, "_");
			st.nextToken();
			return Integer.parseInt( st.nextToken() );
		} catch (NumberFormatException e) {
			throw new ParsingException( e );
		} catch (NoSuchElementException e1) {
			throw new ParsingException( e1 );
		}
	}

	private String escapeQuotes( String s ){
		if( s.indexOf('"') != -1 )
			return (new String( s )).replaceAll( "\\\"", "&quot;" );
		else return s;
	}

	private class Correction{
		private String corrector;
		private boolean auto;
		private boolean correct;
		/**
		 * @param corrector
		 * @param auto
		 * @param points
		 */
		public Correction(String corrector, boolean auto, boolean correct ) {
			super();
			this.corrector = corrector;
			this.auto = auto;
			this.correct = correct;
		}
		/**
		 * @return the auto
		 */
		public boolean isAuto() {
			return auto;
		}
		/**
		 * @param auto the auto to set
		 */
		public void setAuto(boolean auto) {
			this.auto = auto;
		}
		/**
		 * @return the corrector
		 */
		public String getCorrector() {
			return corrector;
		}
		/**
		 * @param corrector the corrector to set
		 */
		public void setCorrector(String corrector) {
			this.corrector = corrector;
		}
		/**
		 * @return the correct
		 */
		public boolean isCorrect() {
			return correct;
		}
		/**
		 * @param correct the correct to set
		 */
		public void setCorrect(boolean correct) {
			this.correct = correct;
		}

	}
}
