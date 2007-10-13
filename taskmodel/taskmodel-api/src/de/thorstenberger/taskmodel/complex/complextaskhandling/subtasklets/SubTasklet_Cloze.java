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
	 * List with instances of either
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
		
		public void setManualCorrection( String corrector, boolean multiCorrectorMode, boolean correct );
		
		/**
		 * 
		 * @return true if there is either an auto correction or at least one manual correction
		 */
		public boolean isCorrected();
		
		/**
		 * Denotes whether this Gap has been corrected by the given corrector.
		 * Please note that, depending on the correction mode, the corrector parameter can be ignored
		 * (as by the regular single corrector mode).
		 * @param corrector
		 * @return true if the Gap has been corrected by the specified corrector
		 */
		public boolean isCorrectedByCorrector( String corrector );
		
		/**
		 * Denotes whether this Gap is correctly solved as constituted by the corrector.
		 * Please note that, depending on the correction mode, the corrector parameter can be ignored
		 * (as by the regular single corrector mode).
		 * @param corrector
		 * @return
		 * @throws IllegalStateException
		 */
		public boolean isCorrectByCorrector( String corrector ) throws IllegalStateException;
		
		public boolean isAutoCorrected();
		
		public boolean isCorrectByAutoCorrection() throws IllegalStateException;
		
		public List<ManualGapCorrection> getManualCorrections() throws IllegalStateException;
		
		public String[] getCorrectValues();
		
		public String removeLeadingTrailingSpaces( String s );
		
	}
	
	public interface ManualGapCorrection{
		
		public String getCorrector();
		
		public boolean isCorrect();
		
	}

}
