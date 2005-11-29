/*

Copyright (C) 2007 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex.complextaskhandling.impl;

import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;

/**
 * @author Thorsten Berger
 *
 */
public class ManualSubTaskletCorrectionImpl implements
		ManualSubTaskletCorrection {

	private String corrector;
	private float points;
	
	/**
	 * @param corrector
	 * @param points
	 */
	public ManualSubTaskletCorrectionImpl(String corrector, float points) {
		this.corrector = corrector;
		this.points = points;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection#getCorrector()
	 */
	public String getCorrector() {
		return corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTaskletCorrection#getPoints()
	 */
	public float getPoints() {
		return points;
	}

}
