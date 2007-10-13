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
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.MappingSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Mapping;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskView_Mapping extends SubTaskView {

	private SubTasklet_Mapping mappingSubTasklet;
	
	/**
	 * 
	 */
	public SubTaskView_Mapping( SubTasklet_Mapping mappingSubTasklet ) {
		this.mappingSubTasklet = mappingSubTasklet;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getRenderedHTML(int)
	 */
	public String getRenderedHTML(HttpServletRequest request, int relativeTaskNumber) {
		return getRenderedHTML( null, relativeTaskNumber, false );
	}
	
	private String getRenderedHTML( HttpServletRequest request, int relativeTaskNumber, boolean corrected ) {
		StringBuffer ret = new StringBuffer();
		
		SubTasklet_Mapping.Concept[] concepts = mappingSubTasklet.getConcepts();
		SubTasklet_Mapping.Assignment[] assignments = mappingSubTasklet.getAssignments();
		
		ret.append("<table border=\"0\" cellspacing=\"2\" cellpadding=\"2\">\n");
		
		for( int i=0; i<concepts.length; i++ ){
			ret.append("<tr><td>");
			ret.append( concepts[i].getConceptName() );
			ret.append("</td>\n<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\n<td>");
			ret.append("<select name=\"task[" + relativeTaskNumber + "].concept_" + i + "\" onChange=\"setModified()\"" +
						( corrected ? "disabled=\"disabled\"" : "" ) + ">\n");

			if( concepts[i].getAssignment() == null )
				ret.append( "	<option selected=\"selected\" value=\"null\"></option>\n" );
			else
				ret.append( "	<option value=\"null\"></option>\n" );
			
			
			for( int j=0; j<assignments.length; j++ ){
					
				if( assignments[j].equals( concepts[i].getAssignment() ) )
					ret.append( "	<option selected=\"selected\" value=\"" + j + "\">" + assignments[j].getAssignmentName() + "</option>\n" );
				else
					ret.append( "	<option value=\"" + j + "\">" + assignments[j].getAssignmentName() + "</option>\n" );
				
			}
			ret.append("</select>&nbsp;\n");
			if( corrected ){
			    ret.append( getSymbolForCorrectedAssignment( request, concepts[i] ) );
			    if( !concepts[i].isCorrectlyAssigned() ){
			    	ret.append( "<ul style=\"color: #B11915;\">\n" );
			    	List<SubTasklet_Mapping.Assignment> cas = concepts[i].getCorrectAssignments();
			    	for( SubTasklet_Mapping.Assignment ca : cas )
			    		ret.append( "<li style=\"font-size: 90%;\">" + ca.getAssignmentName() + "</li>\n" );
			    	ret.append( "</ul>\n" );
			    }
			}
			ret.append("</td></tr>");
			if( i != concepts.length -1 )
				ret.append("<tr><td colspan=3>&nbsp;</td></tr>");
		}
		
		ret.append("</table>");
		
		
		return ret.toString();
	}
	
	public String getCorrectedHTML( HttpServletRequest request, int relativeTaskNumber ){
		return getRenderedHTML( request, -1, true );
	}

	public String getCorrectionHTML( String actualCorrector, HttpServletRequest request ){
	    return null;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.services.student.task.complex.SubTaskView#getSubmitData(java.util.Map, int)
	 */
	public SubmitData getSubmitData(Map postedVarsForTask)
			throws ParsingException {
		
		MappingSubmitData ret = new MappingSubmitData();
		Set varNames = postedVarsForTask.keySet();
		Iterator it = varNames.iterator();
		
		while( it.hasNext() ){
			String varName = (String) it.next();
			String myPart = getMyPart( varName );
			
			int conceptIndex = getConceptIndex( myPart );
			ret.setAssignment( conceptIndex, (String) postedVarsForTask.get( varName ) );
		}
		
		ret.constructAssignments();
		
		return ret;
	}
	
	public CorrectionSubmitData getCorrectionSubmitData( Map postedVars ) throws ParsingException, MethodNotSupportedException{
	    throw new MethodNotSupportedException();
	}
	
	private int getConceptIndex( String part ) throws ParsingException{
		try {
			
			StringTokenizer st = new StringTokenizer(part, "_");
			st.nextToken();	// concept
			return Integer.parseInt( st.nextToken() );	// Nummer
			
		} catch (NumberFormatException e) {
			throw new ParsingException( e );
		} catch (NoSuchElementException e1){
			throw new ParsingException( e1 );
		}
	}
	
	private String getSymbolForCorrectedAssignment( HttpServletRequest request , SubTasklet_Mapping.Concept concept ){
		
		if( concept.isCorrectlyAssigned() )
			return "<img src=\"" + request.getContextPath() + "/pics/true.gif\">";
		else
			return "<img src=\"" + request.getContextPath() + "/pics/false.gif\">";
		
	}

}
