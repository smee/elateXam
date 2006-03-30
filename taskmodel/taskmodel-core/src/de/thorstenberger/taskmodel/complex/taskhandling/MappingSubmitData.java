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
package de.thorstenberger.taskmodel.complex.taskhandling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MappingSubmitData extends SubmitData {

	Map assignments;
	private Integer[] completeAssignments;
	
	/**
	 * @param relativeNumber
	 */
	public MappingSubmitData() {
		assignments = new HashMap();
	}
	
	public void setAssignment( int conceptIndex, String assignmentIndex ){
		assignments.put( new Integer( conceptIndex), assignmentIndex );
	}
	
	public void constructAssignments() throws ParsingException{
		
		try {
			Set concepts = assignments.keySet();
			Integer[] ret = new Integer[ concepts.size() ];
			Iterator it = concepts.iterator();
			
			while( it.hasNext() ){
				Integer conceptIndex = (Integer) it.next();
				String assignmentIndex = (String) assignments.get( conceptIndex );
				if( assignmentIndex.equals("null") )
					ret[ conceptIndex.intValue() ] = null;
				else
					ret[ conceptIndex.intValue() ] = new Integer( assignmentIndex );	
			}
			
			this.completeAssignments = ret;
			
		} catch (NumberFormatException e) {
			throw new ParsingException( e );
		} catch ( ArrayIndexOutOfBoundsException e1) {
			throw new ParsingException( e1 );
		}
	}
	
	public Integer[] getAssignments() throws IllegalStateException{
		if( completeAssignments == null )
			throw new IllegalStateException();
		
		return completeAssignments;
	}

}
