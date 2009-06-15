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
package de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata;

import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectionSubmitDataImpl implements
		CorrectionSubmitData {

	private String annotation;
	private String corrector;
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData#getCorrector()
	 */
	public String getCorrector() {
		return corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData#setCorrector(java.lang.String)
	 */
	public void setCorrector(String corrector) {
		this.corrector = corrector;
	}

	/**
	 * @param annotation The annotation to set.
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData#getAnnotation()
	 */
	public String getAnnotation() {
		return annotation;
	}

}
