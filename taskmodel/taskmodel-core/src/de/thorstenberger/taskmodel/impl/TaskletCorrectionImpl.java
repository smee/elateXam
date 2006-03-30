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
package de.thorstenberger.taskmodel.impl;

import java.util.ArrayList;
import java.util.List;

import de.thorstenberger.taskmodel.TaskletCorrection;

/**
 * @author Thorsten Berger
 *
 */
public class TaskletCorrectionImpl implements TaskletCorrection {

	private Float points;
	private String annotation;
	private String corrector;
	private List<String> correctorHistory;
	
	/**
	 * 
	 */
	public TaskletCorrectionImpl( Float points, String annotation, String corrector, List<String> correctorHistory ) {
		this.points = points;
		this.annotation = annotation;
		this.corrector = corrector;
		this.correctorHistory = correctorHistory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getPoints()
	 */
	public Float getPoints() {
		return points;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getAnnotation()
	 */
	public String getAnnotation() {
		return annotation;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getCorrector()
	 */
	public String getCorrector() {
		return corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getCorrectorHistory()
	 */
	public List<String> getCorrectorHistory() {
		return correctorHistory;
	}

	/**
	 * @param annotation The annotation to set.
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * @param corrector The corrector to set.
	 */
	public void setCorrector(String corrector) {
		this.corrector = corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#addCorrectorToHistory(java.lang.String)
	 */
	public void addCorrectorToHistory(String value) {
		if( correctorHistory == null )
			correctorHistory = new ArrayList<String>();
		
		correctorHistory.add( value );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#setPoints(java.lang.Float)
	 */
	public void setPoints(Float points) throws IllegalArgumentException {
		if( points == null )
			points = null;
		
		if( points < 0 )
			throw new IllegalArgumentException( "Invalid points value (<0)." );
		
		this.points = points;
		
	}

}
