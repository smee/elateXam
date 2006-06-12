/*

Copyright (C) 2006 Thorsten Berger

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
/**
 * 
 */
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets;

import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;

/**
 * @author Thorsten Berger
 *
 */
public interface SubTasklet_Cloze extends SubTasklet {
	
	/**
	 * List mit Instanzen von entweder
	 * - String
	 * - Gap
	 * @return
	 */
	public List getContent();
	
	public interface Gap{		
		
		public int getIndex();
		
		public int getInputLength();
		
		public String getGapValue();
		
		public void setGapValue( String value );
		
		public boolean valueEqualsCorrectContent();
		
		public void setAutoCorrection( boolean correct );
		
		public void setManualCorrection( boolean correct );
		
		public boolean isCorrected();
		
		public boolean isAutoCorrected();
		
		public boolean isCorrect() throws IllegalStateException;
		
		public String[] getCorrectValues();
		
		public String removeLeadingTrailingSpaces( String s );
	}

}
