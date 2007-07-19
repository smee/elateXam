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

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.TextBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.TextSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.TextCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.TextSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.TextSubTask;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_TextImpl implements SubTasklet_Text {

	private Block block;
	private TextTaskBlock textTaskBlock;
	private TextSubTaskDef textSubTaskDef;
	private TextSubTask textSubTask;
	
	/**
	 * 
	 */
	public SubTasklet_TextImpl( Block block, TextSubTaskDefImpl textSubTaskDefImpl, TextSubTask textSubTask ) {
		this.block = block;
		this.textTaskBlock = ((TextBlockImpl)block).getTextTaskBlock();
		this.textSubTaskDef = textSubTaskDefImpl.getTextSubTaskDef();
		this.textSubTask = textSubTask;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getSubTaskDefId()
	 */
	public String getSubTaskDefId() {
		return textSubTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#addToPage(de.thorstenberger.taskmodel.complex.complextaskhandling.Page)
	 */
	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
		pageImpl.getPageType().getMcSubTaskOrClozeSubTaskOrTextSubTask().add( textSubTask );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#setVirtualSubtaskNumber(java.lang.String)
	 */
	public void setVirtualSubtaskNumber(String number) {
		textSubTask.setVirtualNum( number );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getVirtualSubtaskNumber()
	 */
	public String getVirtualSubtaskNumber() {
		return textSubTask.getVirtualNum();
	}
	
	public float getReachablePoints(){
		return textTaskBlock.getConfig().getPointsPerTask();
	}
	
	public String getProblem(){
		return textSubTaskDef.getProblem();
	}
	
	public String getHint(){
		return textSubTaskDef.getHint();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text#getInitialTextFieldValue()
	 */
	public String getInitialTextFieldValue() {
		// remove Windows/DOS line breaks,
		// see doSave() downwards
		if( textSubTaskDef.getInitialTextFieldValue() != null )
			return textSubTaskDef.getInitialTextFieldValue().replaceAll( "\r", "" );
		else
			return null;
	}


	public void setVirtualNum( String virtualNum ){
		textSubTask.setVirtualNum( virtualNum );
	}
	
	public String getVirtualNum(){
		return textSubTask.getVirtualNum();
	}
	
	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( textSubTask.getRefId() );		
		ret.append( getAnswer() );
		ret.append( getVirtualNum() );
		return ret.toString().hashCode();
	}
	
	public String getAnswer(){
		return textSubTask.getAnswer();
	}
	
	public void doSave( SubmitData submitData ) throws IllegalStateException{
		TextSubmitData tsd = (TextSubmitData) submitData;
		// remove Windows/DOS line breaks,
		// otherwise comparing the answer to the initial text field value will always return false
		// if the page has been saved without changing the text field by the user
		textSubTask.setAnswer( tsd.getAnswer().replaceAll( "\r", "" ) );
	}
	
	public void doAutoCorrection(){
		if( !isProcessed() ){
			setCorrection( 0 , true);
		}else
			textSubTask.setNeedsManualCorrection( true );
		
		// mehr können wir leider nicht machen...
	}
	
	public void doManualCorrection( CorrectionSubmitData csd ){
	    TextCorrectionSubmitData tcsd = (TextCorrectionSubmitData) csd;
	    float points = tcsd.getPoints();
	    if( points>=0 && points <= textTaskBlock.getConfig().getPointsPerTask() ){
	        setCorrection( points, false );
	        textSubTask.setNeedsManualCorrection( false );
	    }
	}
	
	private void setCorrection( float points, boolean auto ){
		ComplexTaskHandlingType.TryType.PageType.TextSubTaskType.CorrectionType corr = textSubTask.getCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypeTextSubTaskTypeCorrectionType();
				textSubTask.setCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
		corr.setAutoCorrected( auto );
	}

	
	public boolean isCorrected(){
		return textSubTask.getCorrection() != null;
	}
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isNeedsManualCorrection()
	 */
	public boolean isNeedsManualCorrection() {
		return textSubTask.isNeedsManualCorrection();
	}


	public float getPoints() throws IllegalStateException{
		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return textSubTask.getCorrection().getPoints();
	}
	
	public boolean isProcessed(){
		if( getInitialTextFieldValue() != null ){
			if( getInitialTextFieldValue().equals( textSubTask.getAnswer() ) )
				return false;
			else
				return true;
		}else
			return textSubTask.getAnswer() != null && textSubTask.getAnswer().length() > 0;
	}
	
	public int getTextFieldWidth(){
		return textSubTaskDef.isSetTextFieldWidth() ?
					textSubTaskDef.getTextFieldWidth() : 60;
	}
	
	public int getTextFieldHeight(){
		return textSubTaskDef.isSetTextFieldHeight() ?
					textSubTaskDef.getTextFieldHeight() : 15;
	}

	public String getCorrectionHint(){
	    return textSubTaskDef.getCorrectionHint();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
	public void build() throws TaskApiException {
		// nothing to build :)		
		// except:
		textSubTask.setAnswer( getInitialTextFieldValue() != null ? getInitialTextFieldValue() : "" );
	}
	
}
