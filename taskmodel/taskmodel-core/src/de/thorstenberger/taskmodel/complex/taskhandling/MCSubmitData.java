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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MCSubmitData extends SubmitData{
	
	private List selectedAnswers = new ArrayList();
	
	public MCSubmitData(){
	}
	
	public void setSelected( int answerIndex ){
		selectedAnswers.add( new Integer( answerIndex ) );
	}
	
	public boolean isSelected( int index ){
		// contains verwendet equals...
		return selectedAnswers.contains( new Integer(index) );
	}
	
}