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

import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTask_Mapping extends SubTask {

	private ComplexTaskDefType.CategoryType.MappingTaskBlock block;
	private MappingSubTaskDefType mappingSubTaskDef;
	private ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType mappingSubTask;
	
	/**
	 * @param id
	 */
	public SubTask_Mapping(ComplexTaskDefType.CategoryType.MappingTaskBlock block,
			MappingSubTaskDefType mappingSubTaskDef,
			ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType mappingSubTask) {
		super( mappingSubTask.getRefId() );
		
		this.block = block;
		this.mappingSubTaskDef = mappingSubTaskDef;
		this.mappingSubTask = mappingSubTask;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getReachablePoints()
	 */
	public float getReachablePoints() {
		return block.getConfig().getPointsPerTask();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getProblem()
	 */
	public String getProblem() {
		return mappingSubTaskDef.getProblem();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getHint()
	 */
	public String getHint() {
		return mappingSubTaskDef.getHint();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#addToPage(de.thorstenberger.uebman.xml.taskhandling.jaxb.ComplexTaskHandlingType.TryType.PageType)
	 */
	public void addToPage(PageType page) {
		page.getMcSubTaskOrClozeSubTaskOrTextSubTask().add( mappingSubTask );

	}

	public void setVirtualNum(String virtualNum) {
		mappingSubTask.setVirtualNum( virtualNum );

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getVirtualNum()
	 */
	public String getVirtualNum() {
		return mappingSubTask.getVirtualNum();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getHash()
	 */
	public int getHash() {
		StringBuffer ret = new StringBuffer();
		ret.append( mappingSubTask.getRefId() );
		Concept[] concepts = getConcepts();
		ret.append( concepts.length );
		for( int i=0; i<concepts.length; i++ ){
			Assignment assignm = concepts[i].getAssignment();
			if( assignm != null )
				ret.append( assignm.getId() );
		}
		
		return ret.toString().hashCode();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#doSave(de.thorstenberger.uebman.task.beans.complexTask.SubmitData)
	 */
	public void doSave(SubmitData submitData) throws IllegalStateException {
		try {
			
			MappingSubmitData mappingSubmitData = (MappingSubmitData) submitData;
			Integer[] indexAssignments = mappingSubmitData.getAssignments();
			Concept[] concepts = getConcepts();
			Assignment[] assignments =  getAssignments();
			
			for( int i=0; i<indexAssignments.length; i++ ){
				
				if( indexAssignments[i] == null )
					concepts[ i ].setAssignment( null );
				else
					concepts[ i ].setAssignment( assignments[ indexAssignments[i].intValue() ].getId() );
				
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
		}
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#doCorrection()
	 */
	public void doAutoCorrection() {
		
		float points = block.getConfig().getPointsPerTask();
		float negativePoints = block.getMappingConfig().getNegativePoints();
		boolean noneProcessed = true;
		
		Concept[] concepts = getConcepts();
		
		for( int i=0; i<concepts.length; i++ ){
			
			if( concepts[i].getAssignment() != null ){
				noneProcessed = false;
				
				if( !concepts[i].isCorrectlyAssigned() )
					points -= negativePoints;
				
			}else
				points -= negativePoints;	// nicht zugeordnet als falsch bewertet
			
		}
		
		if( points < 0 || noneProcessed )
			points = 0;
		
		setCorrection( points );

	}
	
	public void doManualCorrection( CorrectionSubmitData csd ){
	    
	}
	
	private void setCorrection( float points ){
		ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.CorrectionType corr = mappingSubTask.getCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypeMappingSubTaskTypeCorrectionType();
				mappingSubTask.setCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#isCorrected()
	 */
	public boolean isCorrected() {
		return mappingSubTask.getCorrection() != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#getPoints()
	 */
	public float getPoints() throws IllegalStateException {
		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		return mappingSubTask.getCorrection().getPoints();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#isProcessed()
	 */
	public boolean isProcessed() {
		Concept[] concepts = getConcepts();
		for( int i=0; i<concepts.length; i++ )
			if( concepts[i].getAssignment() != null )
				return true;
		
		return false;
	}
	
	public Concept[] getConcepts(){
		List conceptDefs = mappingSubTaskDef.getConcept();
		List concepts = mappingSubTask.getConcept();
		Concept[] ret = new Concept[ conceptDefs.size() ];
		
		for( int i=0; i<concepts.size(); i++ ){
			MappingSubTaskDefType.ConceptType conceptDef = 
				(MappingSubTaskDefType.ConceptType) conceptDefs.get( i );
			ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType concept =
				(ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType) concepts.get( i );
			
			ret[ i ] = new Concept( i, concept, conceptDef, findAssignment( concept.getAssigned() ) );
			
		}
		
		return ret;
		
	}
	
	public Assignment[] getAssignments(){
		List assignments = mappingSubTaskDef.getAssignment();
		Assignment[] ret = new Assignment[ assignments.size() ];
		for( int i=0; i<ret.length; i++ )
			ret[i] = new Assignment(
					(MappingSubTaskDefType.AssignmentType) assignments.get( i ) );
		return ret;
	}
	
	private SubTask_Mapping.Assignment findAssignment( String id ){
		if( id==null )
			return null;
		
		List assignments = mappingSubTaskDef.getAssignment();
		for( int i=0; i<assignments.size(); i++ ){
			MappingSubTaskDefType.AssignmentType assignment =
				(MappingSubTaskDefType.AssignmentType) assignments.get( i );
			if( assignment.getId().equals( id ) )
				return new Assignment( assignment );
		}
		
		return null;
		
	}

	public String getCorrectionHint(){
	    return null;
	}
	
	public class Concept{
		
		private int index;
		private ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType concept;
		private MappingSubTaskDefType.ConceptType conceptDef;
		private Assignment assignment;
		
		public Concept( int index,
				ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType concept,
				MappingSubTaskDefType.ConceptType conceptDef,
				Assignment assignment ){
			
			this.index = index;
			this.concept = concept;
			this.conceptDef = conceptDef;
			this.assignment = assignment;
			
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getConceptName(){
			return conceptDef.getName();
		}
		
		public Assignment getAssignment(){
			return assignment;
		}
		
		public void setAssignment( String assignmentId ){
			concept.setAssigned( assignmentId );
		}
		
		public boolean isCorrectlyAssigned(){
			if( getAssignment() == null )
				return false;
			
//			List correctAssignments = conceptDef.getCorrectAssignmentRefId();
//			List correctAssignments = conceptDef.getCorrectAssignmentIDs();
			String correctAssignment = conceptDef.getCorrectAssignmentID();
			if( getAssignment().getId().equals( correctAssignment ))
				return true;
//			for( int i=0; i<correctAssignments.size(); i++ ){
//				if( getAssignment().getId().equals( (String) correctAssignments.get( i ) ) )
//					return true;
//			}
			
			return false;
			
		}
		
		
	}
	
	public class Assignment{
		
		private MappingSubTaskDefType.AssignmentType assignment;
		
		public Assignment( MappingSubTaskDefType.AssignmentType assignment ){
			this.assignment = assignment;
		}
		
		public String getAssignmentName(){
			return assignment.getName();
		}
		
		public String getId(){
			return assignment.getId();
		}
		
		public boolean equals( Object o ){
			if( o == null )
				return false;
			
			if( o instanceof Assignment )
				if( ((Assignment)o).getId().equals( getId() ) )
					return true;
				
			return false;
		}
		
	}
	
}
