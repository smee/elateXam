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
import java.util.Collections;
import java.util.List;

import de.thorstenberger.taskmodel.Annotation;
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
	private List<Annotation> studentAnnotations;
	
	/**
	 * 
	 */
	public TaskletCorrectionImpl( Float points, String annotation, String corrector, List<String> correctorHistory, List<Annotation> studentAnnotations ) {
		this.points = points;
		this.annotation = annotation;
		this.corrector = corrector;
		this.correctorHistory = correctorHistory;
		this.studentAnnotations = studentAnnotations;
		Collections.sort( studentAnnotations );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getPoints()
	 */
	public synchronized Float getPoints() {
		return points;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getAnnotation()
	 */
	public synchronized String getCorrectorAnnotation() {
		return annotation;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getCorrector()
	 */
	public synchronized String getCorrector() {
		return corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getCorrectorHistory()
	 */
	public synchronized List<String> getCorrectorHistory() {
		if( correctorHistory == null )
			correctorHistory = new ArrayList<String>();
		return correctorHistory;
	}

	/**
	 * @param annotation The annotation to set.
	 */
	public synchronized void setCorrectorAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * @param corrector The corrector to set.
	 */
	public synchronized void setCorrector(String corrector) {
		this.corrector = corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#setPoints(java.lang.Float)
	 */
	public synchronized void setPoints(Float points) throws IllegalArgumentException {
		if( points == null ){
			this.points = null;
			return;
		}
		
		if( points < 0 )
			throw new IllegalArgumentException( "Invalid points value (<0)." );
		
		this.points = points;
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#addStudentAnnotation(java.lang.String)
	 */
	public void addStudentAnnotation( String annotation ) {
		Annotation a = new AnnotationImpl( annotation, System.currentTimeMillis(), false );
		studentAnnotations.add( 0, a );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getStudentAnnotations()
	 */
	public List<Annotation> getStudentAnnotations() {
		return studentAnnotations;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#reset()
	 */
	public synchronized void reset() {
		points = null;
		annotation = null;
		corrector = null;
		correctorHistory = new ArrayList<String>();
		studentAnnotations = new ArrayList<Annotation>();		
	}

	
	
}
