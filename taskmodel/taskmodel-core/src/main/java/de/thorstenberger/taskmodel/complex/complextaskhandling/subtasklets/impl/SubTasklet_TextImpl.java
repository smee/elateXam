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

import java.util.List;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.GenericBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.TextCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.TextSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef.Category.TextTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.TextSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ManualCorrectionType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDef;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_TextImpl extends AbstractSubTasklet implements SubTasklet_Text {

	private TextTaskBlock textTaskBlock;
	private TextSubTaskDef textSubTaskDef;
	private TextSubTask textSubTask;

	/**
	 *
	 */
	public SubTasklet_TextImpl( Block block, SubTaskDefType textSubTaskDef, TextSubTask textSubTask, ComplexTaskDefRoot complexTaskDefRoot ) {
		super( complexTaskDefRoot, block, textSubTaskDef, textSubTask );
		this.textTaskBlock = (TextTaskBlock) ((GenericBlockImpl)block).getJaxbTaskBlock();
		this.textSubTaskDef = (TextSubTaskDef) textSubTaskDef;
		this.textSubTask = textSubTask;
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


	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( textSubTask.getRefId() );
		ret.append( getAnswer() );
		ret.append( getVirtualSubtaskNumber() );
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
			setAutoCorrection( 0 );
		} else {
            textSubTask.setNeedsManualCorrection( true );
        }

		// mehr k�nnen wir leider nicht machen...
	}

	public void doManualCorrection( CorrectionSubmitData csd ){

		if( isAutoCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_AUTO_CORRECTED );

		TextCorrectionSubmitData tcsd = (TextCorrectionSubmitData) csd;

		if( tcsd.getPoints() <0 || tcsd.getPoints() > textTaskBlock.getConfig().getPointsPerTask() )
			return;

		List<ManualCorrectionType> manualCorrections = textSubTask.getManualCorrection();
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){

			for( ManualCorrectionType mc : manualCorrections ){
				if( mc.getCorrector().equals( tcsd.getCorrector() ) ){
					mc.setPoints( tcsd.getPoints() );
					return;
				}
			}
			// corrector not found, so create a new ManualCorrection for him
			ManualCorrectionType mc;
      mc = objectFactory.createManualCorrectionType();
			mc.setCorrector( tcsd.getCorrector() );
			mc.setPoints( tcsd.getPoints() );
			manualCorrections.add( mc );

		}else{

			ManualCorrectionType mc;
			if( manualCorrections.size() > 0 ){
				mc = manualCorrections.get( 0 );
			}else{
        mc = objectFactory.createManualCorrectionType();
				manualCorrections.add( mc );
			}
			mc.setCorrector( tcsd.getCorrector() );
			mc.setPoints( tcsd.getPoints() );
		}

	}

//	public float getPoints() throws IllegalStateException{
//		if( !isCorrected() )
//			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
//
//		return textSubTask.getCorrection().getPoints();
//	}


	public boolean isProcessed(){

		if( textSubTask.getAnswer() == null || textSubTask.getAnswer().trim().length() == 0 )
			return false;

		if( getInitialTextFieldValue() != null )
            return !getInitialTextFieldValue().equals( textSubTask.getAnswer() );

		return true;
	}

	public int getTextFieldWidth(){
		return textSubTaskDef.isSetTextFieldWidth() ?
					textSubTaskDef.getTextFieldWidth() : 60;
	}

	public int getTextFieldHeight(){
		return textSubTaskDef.isSetTextFieldHeight() ?
					textSubTaskDef.getTextFieldHeight() : 15;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
    public void build(long randomSeed) throws TaskApiException {
		// nothing to build :)
		// except:
		textSubTask.setAnswer( getInitialTextFieldValue() != null ? getInitialTextFieldValue() : "" );
	}

}
