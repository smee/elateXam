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
import java.util.LinkedList;
import java.util.List;

import de.thorstenberger.taskmodel.CorrectorAnnotation;
import de.thorstenberger.taskmodel.CorrectorAnnotationImpl;
import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.StudentAnnotation;
import de.thorstenberger.taskmodel.TaskletCorrection;

/**
 * @author Thorsten Berger
 *
 */
public class TaskletCorrectionImpl implements TaskletCorrection {

	private List<ManualCorrection> manualCorrections;
	private Float autoCorrectionPoints;
	private List<CorrectorAnnotation> correctorAnnotations;
	private String corrector;
	private List<String> correctorHistory;
	private List<StudentAnnotation> studentAnnotations;
	
	/**
	 * 
	 */
	public TaskletCorrectionImpl( Float autoCorrectionPoints, List<CorrectorAnnotation> correctorAnnotations, String actualCorrector, List<String> correctorHistory, List<StudentAnnotation> studentAnnotations, List<ManualCorrection> manualCorrections ) {
		this.autoCorrectionPoints = autoCorrectionPoints;
		this.correctorAnnotations = correctorAnnotations;
		this.corrector = actualCorrector;
		this.correctorHistory = correctorHistory;
		this.studentAnnotations = studentAnnotations;
		this.manualCorrections = manualCorrections;
		if( studentAnnotations != null )
			Collections.sort( studentAnnotations );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getPoints()
	 */
	public synchronized Float getAutoCorrectionPoints() {
		return autoCorrectionPoints;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#isAutoCorrected()
	 */
	public boolean isAutoCorrected() {
		return getAutoCorrectionPoints() != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#isCorrected()
	 */
	public boolean isCorrected() {
		return getAutoCorrectionPoints() != null || ( getManualCorrections() != null && getManualCorrections().size() > 0 ) ;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getCorrectorAnnotations()
	 */
	public synchronized List<CorrectorAnnotation> getCorrectorAnnotations() {
		return correctorAnnotations;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getManualCorrections()
	 */
	public synchronized List<ManualCorrection> getManualCorrections() {
		return manualCorrections;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#setManualCorrections(java.util.List)
	 */
	public synchronized void setManualCorrections(List<ManualCorrection> manualCorrections) {
		this.manualCorrections = manualCorrections;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#setCorrectorAnnotation(java.lang.String, java.lang.String)
	 */
	public synchronized void setCorrectorAnnotation(String corrector, String value) {
		if( correctorAnnotations == null )
			correctorAnnotations = new LinkedList<CorrectorAnnotation>();
		CorrectorAnnotation correctorAnnotation = null;
		for( CorrectorAnnotation ca : correctorAnnotations ){
			if( ca.getCorrector().equals( corrector ) )
				correctorAnnotation = ca;
		}
		
		if( value != null ){
			if( correctorAnnotation != null )
				correctorAnnotation.setText( value );
			else
				correctorAnnotations.add( new CorrectorAnnotationImpl( corrector, value ) );
		}else if( correctorAnnotation != null )
				correctorAnnotations.remove( correctorAnnotation );
		
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
	 * @param corrector The corrector to set.
	 */
	public synchronized void setCorrector(String corrector) {
		this.corrector = corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#setPoints(java.lang.Float)
	 */
	public synchronized void setAutoCorrectionPoints(Float points) throws IllegalArgumentException {
		if( points == null ){
			this.autoCorrectionPoints = null;
			return;
		}
		
		if( points < 0 )
			throw new IllegalArgumentException( "Invalid points value (<0)." );
		
		this.autoCorrectionPoints = points;
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#addStudentAnnotation(java.lang.String)
	 */
	public synchronized void addStudentAnnotation( String annotation ) {
		StudentAnnotation a = new StudentAnnotationImpl( annotation, System.currentTimeMillis(), false );
		studentAnnotations.add( 0, a );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#getStudentAnnotations()
	 */
	public synchronized List<StudentAnnotation> getStudentAnnotations() {
		return studentAnnotations;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletCorrection#reset()
	 */
	public synchronized void reset() {
		autoCorrectionPoints = null;
		correctorAnnotations = new LinkedList<CorrectorAnnotation>();
		manualCorrections = new LinkedList<ManualCorrection>();
		corrector = null;
		correctorHistory = new LinkedList<String>();
		studentAnnotations = new LinkedList<StudentAnnotation>();		
	}

	
	
}
