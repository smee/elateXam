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
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTask_MC extends SubTask {

	// folgende zwei Aufgaben-Typen kann es bei MC-Aufgaben geben:
	
	/**
	 * nur immer eine Antwort richtig
	 */
	public static final String CAT_SINGLESELECT = "singleSelect";
	/**
	 * mehrere Antworten können richtig sein
	 */
	public static final String CAT_MULTIPLESELECT = "multipleSelect";
	
	private McSubTaskDefType mcSubTaskDef;
	private ComplexTaskHandlingType.TryType.PageType.McSubTask mcSubTask;
	private ComplexTaskDefType.CategoryType.McTaskBlock block;
	
	/**
	 * @param id
	 */
	public SubTask_MC( ComplexTaskDefType.CategoryType.McTaskBlock block,
							McSubTaskDefType mcSubTaskDef,
							ComplexTaskHandlingType.TryType.PageType.McSubTask mcSubTask) {
		
		super( mcSubTask.getRefId() );
		this.mcSubTaskDef = mcSubTaskDef;
		this.mcSubTask = mcSubTask;
		this.block = block;
	}
	
	public void addToPage( ComplexTaskHandlingType.TryType.PageType page ){
		page.getMcSubTaskOrClozeSubTaskOrTextSubTask().add( mcSubTask );
	}
	
	public float getReachablePoints(){
		return block.getConfig().getPointsPerTask();
	}
	
	public String getProblem(){
		return mcSubTaskDef.getProblem();
	}
	
	/**
	 * DONE Meldungen entkoppeln, sollten nichts im model zu suchen haben
	 */
	public String getHint(){
		return mcSubTaskDef.getHint();
	}
	
	public String getVirtualNum(){
		return mcSubTask.getVirtualNum();
	}
	
	public void setVirtualNum( String virtualNum ){
		mcSubTask.setVirtualNum( virtualNum );
	}
	
	public String getCategory(){
		return mcSubTaskDef.getCategory();
	}
	
	public int getHash(){
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
	
	public boolean isProcessed(){
		return getNumOfSelectedAnswers() > 0;
	}
	
	public Answer[] getAnswers(){
		List answers = mcSubTask.getAnswer();
		Answer[] ret = new Answer[ answers.size() ];
		
		List correctAnswerDefs = mcSubTaskDef.getCorrect();
		List incorrectAnswerDefs = mcSubTaskDef.getIncorrect();
		
		for( int i=0; i<answers.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer =
				(ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType) answers.get( i );
			
			// korrekte Antwort?
			for( int j=0; j<correctAnswerDefs.size(); j++ ){
				McSubTaskDefType.CorrectType correctAnswerDef =
					(McSubTaskDefType.CorrectType) correctAnswerDefs.get( j );
				if( correctAnswerDef.getId().equals( answer.getRefId() ) ){
					ret[i] = new Answer( this, correctAnswerDef, answer );
					break;
				}
			}
			
			if( ret[i] != null )
				continue;
			
			// falsche Antwort?
			for( int j=0; j<incorrectAnswerDefs.size(); j++ ){
				McSubTaskDefType.IncorrectType incorrectAnswerDef =
					(McSubTaskDefType.IncorrectType) incorrectAnswerDefs.get( j );
				if( incorrectAnswerDef.getId().equals( answer.getRefId() ) ){
					ret[i] = new Answer( this, incorrectAnswerDef, answer );
					break;
				}
			}
			
			// wenn nicht gefunden, dann haben wir'n ernstes Problem...
			if( ret[i] == null )
				throw new TaskModelPersistenceException("Daten-Inkonsistenz: AnswerDef nicht gefunden.");
			
		}
		
		return ret;
		
	}
	
	public void doSave( SubmitData submitData ) throws IllegalStateException{
		MCSubmitData mcSubmitData = (MCSubmitData) submitData;
		boolean sstest = false;
		boolean ss = getCategory().equals( CAT_SINGLESELECT );
		
		Answer[] answers = getAnswers();
		for( int i=0; i<answers.length; i++ ){
			
			if( mcSubmitData.isSelected( i ) ){
				answers[i].setSelected( true );
				if( ss ){
					// es darf nur eine Antwort bei Single Select ausgewählt sein
					if ( sstest )
						throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
					sstest = true;
				}
			}else{
				answers[i].setSelected( false );
			}
			
		}
	}
	
	/**
	 * Korrigiert die Lösung und setzt die Punkte im "correction"-Element der Aufgabe.
	 */
	public void doAutoCorrection(){
		
		float pointsPerTask = block.getConfig().getPointsPerTask();
		
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
			if( block.getMcConfig().getRegular() != null ){
				
				// kein Unterschied zw. richtigen und falschen Antworten
				float negativePoints = block.getMcConfig().getRegular().getNegativePoints();
				points -=  negativePoints * getNumOfIncorrectSolvedAnswers() ;
				
			}else{
				
				// richtige und falsche Antworten werden unterschiedlich behandelt
				float correctAnswerNegativePoints = block.getMcConfig().getDifferent().getCorrectAnswerNegativePoints();
				float incorrectAnswerNegativePoints = block.getMcConfig().getDifferent().getIncorrectAnswerNegativePoints();
				
				Answer[] answers = getAnswers();
				for( int i=0; i<answers.length; i++ )
					if( answers[i].isCorrect() && !answers[i].isCorrectlySolvedAnswer() )
						points -= correctAnswerNegativePoints;
					else if( !answers[i].isCorrect() && !answers[i].isCorrectlySolvedAnswer() )
						points -= incorrectAnswerNegativePoints;
				
			}

			if( points < 0 )
				points = 0;
			
			setCorrection( points );
			
		}
		
	}
	
	public void doManualCorrection( CorrectionSubmitData csd ){
	    
	}
	
	private boolean isCompletelyCorrectSolved(){
		Answer[] answers = getAnswers();
		for( int i=0; i<answers.length; i++ )
			if( !answers[i].isCorrectlySolvedAnswer() )
				return false;
			
		return true;
	}
	
	private int getNumOfIncorrectSolvedAnswers(){
		Answer[] answers = getAnswers();
		int ret = 0;
		for( int i=0; i<answers.length; i++ )
			if( !answers[i].isCorrectlySolvedAnswer() )
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
		Answer[] answers = getAnswers();
		int ret = 0;
		for( int i=0; i<answers.length; i++ )
			if( answers[i].isSelected() )
				ret++;
		return ret;
	}
	
	public boolean isCorrected(){
		return mcSubTask.getCorrection() != null;
	}
	
	public float getPoints() throws IllegalStateException{
		
		if( mcSubTask.getCorrection() == null )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return mcSubTask.getCorrection().getPoints();
	}
	
	public String getCorrectionHint(){
	    return null;
	}
	
	
	public class Answer{
		
		private SubTask_MC mcsubTask;
		private McSubTaskDefType.CorrectType correctAnswerDef;
		private McSubTaskDefType.IncorrectType incorrectAnswerDef;
		private ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer;
		private boolean correct;
		
		Answer( SubTask_MC mcsubTask,
				McSubTaskDefType.CorrectType correctAnswerDef,
				ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer ){
			
			correct = true;
			this.mcsubTask = mcsubTask;
			this.correctAnswerDef = correctAnswerDef;
			this.incorrectAnswerDef = null;
			this.answer = answer;
		}
		
		Answer( SubTask_MC mcsubTask,
				McSubTaskDefType.IncorrectType incorrectAnswerDef,
				ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType answer ){
			
			correct = false;
			this.mcsubTask = mcsubTask;
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
	
}
