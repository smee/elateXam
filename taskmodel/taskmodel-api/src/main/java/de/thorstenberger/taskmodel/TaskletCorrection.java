/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel;

import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public interface TaskletCorrection {

	/**
	 * 
	 * @return true if there is at least one correction, either one auto-correction or at least one manual correction
	 */
	public boolean isCorrected();
	
	public boolean isAutoCorrected();
	
	public List<ManualCorrection> getManualCorrections();
	
	public void setManualCorrections( List<ManualCorrection> manualCorrections );
	
	public Float getAutoCorrectionPoints();
	
	public void setAutoCorrectionPoints( Float value );
	
	/**
	 * Correctors can comment the overall Tasklet. As there can be several correctors, a List of CorrectorAnnotation
	 * is returned.
	 * @return the corrector's annotations
	 */
	public List<CorrectorAnnotation> getCorrectorAnnotations();
	
	/**
	 * If allowed, students can comment the corrector's marking. If the annotation gets acknowledged by a corrector
	 * the student may be allowed to add another annotation, which implies the List.
	 * @return the student's annotations
	 */
	public List<StudentAnnotation> getStudentAnnotations();
	
	/**
	 * Tasklets have to be assigned to correctors if they need manual correction.
	 * @return the currently assigned corrector
	 */
	public String getCorrector();
	
	public List<String> getCorrectorHistory();
	
	public void setCorrectorAnnotation( String corrector, String value );
	
	/**
	 * If allowed, students can comment the corrector's marking.
	 * @param value the student's annotation
	 */
	public void addStudentAnnotation( String annotation );
	
	public void setCorrector( String value );

	/**
	 * resets all values to their default, be careful with this method
	 *
	 */
	public void reset();

}
