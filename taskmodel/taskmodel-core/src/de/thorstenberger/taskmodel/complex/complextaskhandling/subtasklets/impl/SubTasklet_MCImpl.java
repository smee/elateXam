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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.McBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.McSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.McSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.McSubTask;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_MCImpl implements SubTasklet_MC {

	private Block block;
	private McTaskBlock mcTaskBlock;
	private McSubTaskDef mcSubTaskDef;
	private McSubTask mcSubTask;
	
	/**
	 * 
	 */
	public SubTasklet_MCImpl( Block block, McSubTaskDefImpl mcSubTaskDefImpl, McSubTask mcSubTask ) {
		this.block = block;
		this.mcTaskBlock = ((McBlockImpl)block).getMcTaskBlock();
		this.mcSubTaskDef = mcSubTaskDefImpl.getMcSubTaskDef();
		this.mcSubTask = mcSubTask;
	}

	public String getMcCategory(){
		return mcSubTaskDef.getCategory();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getSubTaskDefId()
	 */
	public String getSubTaskDefId() {
		return mcSubTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#addToPage(de.thorstenberger.taskmodel.complex.complextaskhandling.Page)
	 */
	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
		pageImpl.getPageType().getMcSubTaskOrClozeSubTaskOrTextSubTask().add( mcSubTask );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#setVirtualSubtaskNumber(java.lang.String)
	 */
	public void setVirtualSubtaskNumber(String number) {
		mcSubTask.setVirtualNum( number );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#doSave(de.thorstenberger.taskmodel.complex.oldtaskhandling.SubmitData)
	 */
	public void doSave(SubmitData submitData) throws IllegalStateException {
		McSubmitData mcSubmitData = (McSubmitData) submitData;
		boolean sstest = false;
		boolean ss = getMcCategory().equals( CAT_SINGLESELECT );
		
		List<Answer> answers = getAnswers();
		for( int i = 0; i < answers.size(); i++ ){
			AnswerImpl answerImpl = (AnswerImpl)answers.get( i );
			if( mcSubmitData.isSelected( i ) ){
				answerImpl.setSelected( true );
				if( ss ){
					// es darf nur eine Antwort bei Single Select ausgewählt sein
					if ( sstest )
						throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
					sstest = true;
				}
			}else{
				answerImpl.setSelected( false );
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isCorrected()
	 */
	public boolean isCorrected() {
		return mcSubTask.getCorrection() != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getPoints()
	 */
	public float getPoints() throws IllegalStateException {
		if( mcSubTask.getCorrection() == null )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return mcSubTask.getCorrection().getPoints();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#doAutoCorrection()
	 */
	public void doAutoCorrection() {
		
		float pointsPerTask = block.getPointsPerSubTask();
		
		// Single Select
		if( mcSubTaskDef.getCategory().equals( CAT_SINGLESELECT ) ){

			if( isCompletelyCorrectSolved() ){
				setCorrection( pointsPerTask );
				return;
			}else{
				setCorrection( 0 );
				return;
			}

		// Multiple Select
		}else{
			
			int numOfSelectedAnswers = getNumOfSelectedAnswers();
			int minCorrect = Math.max( mcSubTaskDef.isSetMinCorrectAnswers() ? mcSubTaskDef.getMinCorrectAnswers() : 1 , 1);
			
			if( numOfSelectedAnswers < minCorrect ){
				setCorrection( 0 );
				return;
			}
			
			if( mcSubTaskDef.isSetMaxCorrectAnswers() && numOfSelectedAnswers > mcSubTaskDef.getMaxCorrectAnswers() ){
				setCorrection( 0 );
				return;
			}
			
			float points = pointsPerTask;
			
			// Bewertung abh. vom Korrekturschema:
			if( mcTaskBlock.getMcConfig().getRegular() != null ){
				
				// kein Unterschied zw. richtigen und falschen Antworten
				float negativePoints = mcTaskBlock.getMcConfig().getRegular().getNegativePoints();
				points -=  negativePoints * getNumOfIncorrectSolvedAnswers() ;
				
			}else{
				
				// richtige und falsche Antworten werden unterschiedlich behandelt
				float correctAnswerNegativePoints = mcTaskBlock.getMcConfig().getDifferent().getCorrectAnswerNegativePoints();
				float incorrectAnswerNegativePoints = mcTaskBlock.getMcConfig().getDifferent().getIncorrectAnswerNegativePoints();
				
				List<Answer> answers = getAnswers();
				for( Answer answer : answers )
					if( answer.isCorrect() && !answer.isCorrectlySolvedAnswer() )
						points -= correctAnswerNegativePoints;
					else if( !answer.isCorrect() && !answer.isCorrectlySolvedAnswer() )
						points -= incorrectAnswerNegativePoints;
				
			}

			if( points < 0 )
				points = 0;
			
			setCorrection( points );
			
		}

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#doManualCorrection(de.thorstenberger.taskmodel.complex.oldtaskhandling.CorrectionSubmitData)
	 */
	public void doManualCorrection(CorrectionSubmitData csd) {
		// intentionelly left blank
		// nothing to do "by hand"
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getReachablePoints()
	 */
	public float getReachablePoints() {
		return block.getPointsPerSubTask();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getProblem()
	 */
	public String getProblem() {
		return mcSubTaskDef.getProblem();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getHint()
	 */
	public String getHint() {
		return mcSubTaskDef.getHint();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getVirtualSubtaskNumber()
	 */
	public String getVirtualSubtaskNumber() {
		return mcSubTask.getVirtualNum();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getHash()
	 */
	public int getHash() {
		StringBuffer ret = new StringBuffer();
		
		ret.append( mcSubTask.getRefId() );
		List answers = mcSubTask.getAnswer();
		for( int i=0; i<answers.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer =
				(ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType) answers.get( i );
			ret.append( answer.getRefId() );
			ret.append( answer.isSelected() );
		}
		
		return ret.toString().hashCode();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isProcessed()
	 */
	public boolean isProcessed() {
		return getNumOfSelectedAnswers() > 0;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getCorrectionHint()
	 */
	public String getCorrectionHint() {
		// hint for manual correction not necessary
		return null;
	}
	
	public List<Answer> getAnswers(){
		List answers = mcSubTask.getAnswer();
		List<Answer> ret = new ArrayList<Answer>( answers.size() );
		
		List correctAnswerDefs = mcSubTaskDef.getCorrect();
		List incorrectAnswerDefs = mcSubTaskDef.getIncorrect();
		
		for( int i=0; i<answers.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer =
				(ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType) answers.get( i );
			
			boolean found = false;
			
			// korrekte Antwort?
			for( int j=0; j<correctAnswerDefs.size(); j++ ){
				McSubTaskDefType.CorrectType correctAnswerDef =
					(McSubTaskDefType.CorrectType) correctAnswerDefs.get( j );
				if( correctAnswerDef.getId().equals( answer.getRefId() ) ){
					ret.add( i, new AnswerImpl( this, correctAnswerDef, answer ) );
					found = true;
					break;
				}
			}
			
			if( found )
				continue;
			
			// falsche Antwort?
			for( int j=0; j<incorrectAnswerDefs.size(); j++ ){
				McSubTaskDefType.IncorrectType incorrectAnswerDef =
					(McSubTaskDefType.IncorrectType) incorrectAnswerDefs.get( j );
				if( incorrectAnswerDef.getId().equals( answer.getRefId() ) ){
					ret.add( i, new AnswerImpl( this, incorrectAnswerDef, answer ) );
					found = true;
					break;
				}
			}
			
			// wenn nicht gefunden, dann haben wir'n ernstes Problem...
			if( !found )
				throw new TaskModelPersistenceException( "Daten-Inkonsistenz: AnswerDef nicht gefunden." );
			
		}
		
		return ret;
		
	}
	
	public class AnswerImpl implements SubTasklet_MC.Answer{
		
		private SubTasklet_MC mcSubTasklet;
		private McSubTaskDefType.CorrectType correctAnswerDef;
		private McSubTaskDefType.IncorrectType incorrectAnswerDef;
		private ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer;
		private boolean correct;
		
		AnswerImpl( SubTasklet_MC mcSubTasklet,
				McSubTaskDefType.CorrectType correctAnswerDef,
				ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer ){
			
			correct = true;
			this.mcSubTasklet = mcSubTasklet;
			this.correctAnswerDef = correctAnswerDef;
			this.incorrectAnswerDef = null;
			this.answer = answer;
		}
		
		AnswerImpl( SubTasklet_MC mcSubTasklet,
				McSubTaskDefType.IncorrectType incorrectAnswerDef,
				ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer ){
			
			correct = false;
			this.mcSubTasklet = mcSubTasklet;
			this.incorrectAnswerDef = incorrectAnswerDef;
			this.correctAnswerDef = null;
			this.answer = answer;
		}
		
		public String toString(){
			if( correct )
				return correctAnswerDef.getValue();
			else
				return incorrectAnswerDef.getValue();
		}
		
		public String getId() {
			if( correct )
				return correctAnswerDef.getId();
			else
				return incorrectAnswerDef.getId();
		}

		public boolean isSelected(){
			return answer.isSelected();
		}
		
		public boolean isCorrect() {
			return correct;
		}

		public boolean isCorrectlySolvedAnswer(){
			if( isSelected() && !correct )
				return false;
			if( !isSelected() && correct )
				return false;
			
			return true;
		}
		
		void setSelected( boolean selected ){
			answer.setSelected( selected );
		}
		
	}
	
	private boolean isCompletelyCorrectSolved(){
		List<Answer> answers = getAnswers();
		for( Answer answer : answers )
			if( !answer.isCorrectlySolvedAnswer() )
				return false;
			
		return true;
	}
	
	private int getNumOfIncorrectSolvedAnswers(){
		int ret = 0;
		List<Answer> answers = getAnswers();
		for( Answer answer : answers )
			if( !answer.isCorrectlySolvedAnswer() )
				ret++;
			
		return ret;
	}
	
	private void setCorrection( float points ){
		ComplexTaskHandlingType.TryType.PageType.McSubTaskType.CorrectionType corr = mcSubTask.getCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypeMcSubTaskTypeCorrectionType();
				mcSubTask.setCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
	}
	
	private int getNumOfSelectedAnswers(){
		int ret = 0;
		List<Answer> answers = getAnswers();
		for( Answer answer : answers )
			if( answer.isSelected() )
				ret++;
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
	public void build() throws TaskApiException {
		SubTasklet_MCBuilder builder = new SubTasklet_MCBuilder();
		try {
			builder.constructAnswersForMCSubTask( mcSubTask, mcSubTaskDef );
		} catch (JAXBException e) {
			throw new TaskApiException( e );
		}
	}
	
	
	

}
