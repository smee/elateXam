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

	public Float getPoints();
	
	public String getCorrectorAnnotation();
	
	/**
	 * If allowed, students can comment the corrector's marking.
	 * @return the student's annotation
	 */
	public List<Annotation> getStudentAnnotations();
	
	public String getCorrector();
	
	public List<String> getCorrectorHistory();
	
	public void setPoints( Float points ) throws IllegalArgumentException;
	
	public void setCorrectorAnnotation( String value );
	
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
