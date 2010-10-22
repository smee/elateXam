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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.GenericBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.MappingSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Mapping;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.MappingTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.MappingSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_MappingImpl extends AbstractSubTasklet implements SubTasklet_Mapping {

	private MappingTaskBlock mappingTaskBlock;
	private MappingSubTaskDef mappingSubTaskDef;
	private MappingSubTask mappingSubTask;

	/**
	 *
	 */
	public SubTasklet_MappingImpl( Block block, SubTaskDefType mappingSubTaskDef, SubTaskType mappingSubTask, ComplexTaskDefRoot complexTaskDefRoot ) {
		super( complexTaskDefRoot, block, mappingSubTaskDef, mappingSubTask  );
		this.mappingTaskBlock = (MappingTaskBlock) ((GenericBlockImpl)block).getJaxbTaskBlock();
		this.mappingSubTaskDef = (MappingSubTaskDef) mappingSubTaskDef;
		this.mappingSubTask = (MappingSubTask) mappingSubTask;;
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
			if( assignm != null ) {
                ret.append( assignm.getId() );
            }
		}

		return ret.toString().hashCode();
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isSetNeedsManualCorrectionFlag()
	 */
	@Override
    public boolean isSetNeedsManualCorrectionFlag() {
		return false;
	}

	public void doSave(SubmitData submitData) throws IllegalStateException {
		try {

			MappingSubmitData mappingSubmitData = (MappingSubmitData) submitData;
			Integer[] indexAssignments = mappingSubmitData.getAssignments();
			Concept[] concepts = getConcepts();
			Assignment[] assignments =  getAssignments();

			for( int i=0; i<indexAssignments.length; i++ ){

				if( indexAssignments[i] == null ) {
                    concepts[ i ].setAssignment( null );
                } else {
                    concepts[ i ].setAssignment( assignments[ indexAssignments[i].intValue() ].getId() );
                }

			}

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
		}
	}

	public void doAutoCorrection() {

		float points = mappingTaskBlock.getConfig().getPointsPerTask();
		float negativePoints = mappingTaskBlock.getMappingConfig().getNegativePoints();
		boolean noneProcessed = true;

		Concept[] concepts = getConcepts();

		for( int i=0; i<concepts.length; i++ ){

			if( concepts[i].getAssignment() != null ){
				noneProcessed = false;

				if( !concepts[i].isCorrectlyAssigned() ) {
                    points -= negativePoints;
                }

			}
            else {
                points -= negativePoints;	// nicht zugeordnet als falsch bewertet
            }

		}

		if( points < 0 || noneProcessed ) {
            points = 0;
        }

		setAutoCorrection( points );

	}

	public void doManualCorrection( CorrectionSubmitData csd ){

	}


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

			ret[ i ] = new ConceptImpl( i, concept, conceptDef, findAssignment( concept.getAssigned() ) );

		}

		return ret;

	}

	public Assignment[] getAssignments(){
		List assignments = mappingSubTaskDef.getAssignment();
		Assignment[] ret = new Assignment[ assignments.size() ];
		for( int i=0; i<ret.length; i++ ) {
            ret[i] = new AssignmentImpl(
					(MappingSubTaskDefType.AssignmentType) assignments.get( i ) );
        }
		return ret;
	}

	private Assignment findAssignment( String id ){
		if( id==null )
			return null;

		List assignments = mappingSubTaskDef.getAssignment();
		for( int i=0; i<assignments.size(); i++ ){
			MappingSubTaskDefType.AssignmentType assignment =
				(MappingSubTaskDefType.AssignmentType) assignments.get( i );
			if( assignment.getId().equals( id ) )
				return new AssignmentImpl( assignment );
		}

		return null;

	}

	public class ConceptImpl implements Concept{

		private int index;
		private ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType concept;
		private MappingSubTaskDefType.ConceptType conceptDef;
		private Assignment assignment;

		public ConceptImpl( int index,
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

			List correctAssignments = conceptDef.getCorrectAssignmentID();
//			String correctAssignment = conceptDef.getCorrectAssignmentID();
//			if( getAssignment().getId().equals( correctAssignment ))
//				return true;
			for( int i=0; i<correctAssignments.size(); i++ ){
				if( getAssignment().getId().equals( correctAssignments.get( i ) ) )
					return true;
			}

			return false;

		}

		public List<Assignment> getCorrectAssignments(){
			List<Assignment> ret = new LinkedList<Assignment>();
			List<String> correctAssignmentIDs = conceptDef.getCorrectAssignmentID();
			for( String caid : correctAssignmentIDs ) {
                ret.add( findAssignment( caid ) );
            }
			return ret;
		}

	}

	public class AssignmentImpl implements Assignment{

		private MappingSubTaskDefType.AssignmentType assignment;

		public AssignmentImpl( MappingSubTaskDefType.AssignmentType assignment ){
			this.assignment = assignment;
		}

		public String getAssignmentName(){
			return assignment.getName();
		}

		public String getId(){
			return assignment.getId();
		}

		@Override
        public boolean equals( Object o ){
			if( o == null )
				return false;

			if( o instanceof Assignment )
				if( ((Assignment)o).getId().equals( getId() ) )
					return true;

			return false;
		}

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
    public void build(long randomSeed) throws TaskApiException {
		try {
			addAssignments( mappingSubTask, mappingSubTaskDef );
		} catch (JAXBException e) {
			throw new TaskApiException( e );
		}
	}

	/**
	 * initiale Zuordnungen (null) erzeugen
	 * @param newMappingSubTask
	 * @param mappingSubTaskDef
	 */
	private void addAssignments( ComplexTaskHandlingType.TryType.PageType.MappingSubTask newMappingSubTask,
							MappingSubTaskDefType mappingSubTaskDef ) throws JAXBException{

		ObjectFactory objectFactory = new ObjectFactory();
		List concepts = mappingSubTaskDef.getConcept();
		for( int i=0; i<concepts.size(); i++ ){

			ComplexTaskHandlingType.TryType.PageType.MappingSubTaskType.ConceptType newConcept =
					objectFactory.createComplexTaskHandlingTypeTryTypePageTypeMappingSubTaskTypeConceptType();
			newMappingSubTask.getConcept().add( newConcept );


		}
	}

}
