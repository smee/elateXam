/*

Copyright (C) 2006 Steffen Dienst

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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.GenericBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.PaintCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.PaintSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Paint;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.PaintSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ManualCorrectionType;
import de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;


public class SubTasklet_PaintImpl extends AbstractSubTasklet implements SubTasklet_Paint {

	private PaintTaskBlock paintTaskBlock;
	private PaintSubTaskDef paintSubTaskDef;
	private PaintSubTask paintSubTask;

	/**
	 *
	 */
	public SubTasklet_PaintImpl( Block block, SubTaskDefType paintSubTaskDef, SubTaskType paintSubTask, ComplexTaskDefRoot complexTaskDefRoot ) {
		super( complexTaskDefRoot, block, paintSubTaskDef, paintSubTask  );
		this.paintTaskBlock = (PaintTaskBlock) ((GenericBlockImpl)block).getJaxbTaskBlock();
		this.paintSubTaskDef = (PaintSubTaskDef) paintSubTaskDef;
		this.paintSubTask = (PaintSubTask) paintSubTask;;
	}

	public void doSave(SubmitData submitData) throws IllegalStateException {
		PaintSubmitData psd=(PaintSubmitData) submitData;
		paintSubTask.setTextAnswer(psd.getTextualAnswer());
		paintSubTask.setPictureString(psd.getImageString());
		paintSubTask.setUndoData(psd.getUndoData());
		paintSubTask.setResetted(psd.isResetted());
	}

	@Override
    public boolean isCorrected() {
		return paintSubTask.isSetAutoCorrection() || paintSubTask.isSetManualCorrection();
	}


//	public float getPoints() throws IllegalStateException {
//		if( !isCorrected() )
//			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
//
//		return paintSubTask.getCorrection().getPoints();
//	}

	public void doAutoCorrection() {
		if( isProcessed() == false ){
			setAutoCorrection( 0 );
		} else {
            paintSubTask.setNeedsManualCorrection( true );
        }
	}

	public void doManualCorrection( CorrectionSubmitData csd ){

		if( isAutoCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_AUTO_CORRECTED );

		PaintCorrectionSubmitData pcsd = (PaintCorrectionSubmitData) csd;

		if( pcsd.getPoints() < 0 || pcsd.getPoints() > paintTaskBlock.getConfig().getPointsPerTask() )
			return;

		List<ManualCorrectionType> manualCorrections = paintSubTask.getManualCorrection();
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){

			for( ManualCorrectionType mc : manualCorrections ){
				if( mc.getCorrector().equals( pcsd.getCorrector() ) ){
					mc.setPoints( pcsd.getPoints() );
					return;
				}
			}
			// corrector not found, so create a new ManualCorrection for him
			ManualCorrectionType mc;
			try {
				mc = objectFactory.createManualCorrectionType();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			mc.setCorrector( pcsd.getCorrector() );
			mc.setPoints( pcsd.getPoints() );
			manualCorrections.add( mc );

		}else{

			ManualCorrectionType mc;
			if( manualCorrections.size() > 0 ){
				mc = manualCorrections.get( 0 );
			}else{
				try {
					mc = objectFactory.createManualCorrectionType();
				} catch (JAXBException e) {
					throw new TaskModelPersistenceException( e );
				}
				manualCorrections.add( mc );
			}
			mc.setCorrector( pcsd.getCorrector() );
			mc.setPoints( pcsd.getPoints() );
		}

	}



//
//
//
//	private void setCorrection( float points, boolean auto ){
//		ComplexTaskHandlingType.TryType.PageType.PaintSubTaskType.CorrectionType corr = paintSubTask.getCorrection();
//		if( corr == null ){
//			ObjectFactory of = new ObjectFactory();
//			try {
//				corr = of.createComplexTaskHandlingTypeTryTypePageTypePaintSubTaskTypeCorrectionType();
//				paintSubTask.setCorrection( corr );
//			} catch (JAXBException e) {
//				throw new TaskModelPersistenceException( e );
//			}
//		}
//		corr.setPoints( points );
//		corr.setAutoCorrected( auto );
//	}
//	public void doManualCorrection(CorrectionSubmitData csd) {
//	    PaintCorrectionSubmitData pcsd = (PaintCorrectionSubmitData) csd;
//	    float points = pcsd.getPoints();
//	    if( points>=0 && points <= paintTaskBlock.getConfig().getPointsPerTask() ){
//	        setCorrection( points, false );
//	        paintSubTask.setNeedsManualCorrection( false );
//	    }
//	}


	public int getHash() {
		StringBuffer sb=new StringBuffer();
		sb.append(paintSubTask.getPictureString()).append(paintSubTask.getTextAnswer());
		return sb.toString().hashCode();
	}

	public boolean isProcessed() {

		if( isTextuallyAnsweredByStudent() )
			return true;

		if( isResetted() )
			return false;

    if( paintSubTask.getPictureString() != null && paintSubTask.getPictureString().trim().length() > 0 ){
      String templateImage = "";
      if (paintSubTaskDef.isSetImages()) {
        templateImage = paintSubTaskDef.getImages().getMutableTemplateImage();
      }
      return !paintSubTask.getPictureString().equals(templateImage);
    }

		return false;

//		if( paintSubTask.getPictureString() == null || paintSubTask.getPictureString().trim().length() == 0 )
//			return isTextuallyAnswerdByStudent();
//		else
////			return !paintSubTask.getPictureString().equals( paintSubTaskDef.getImages() == null ? null : paintSubTaskDef.getImages().getMutableTemplateImage() );
//			return !paintSubTask.isResetted() || isTextuallyAnswerdByStudent();
	}


    public void build(long randomSeed) throws TaskApiException {
		paintSubTask.setTextAnswer( "" );
		paintSubTask.setResetted(false);
	}


	public String getTextualAnswer() {
		String answer = paintSubTask.getTextAnswer();
		return answer == null? "":answer;
	}

	private boolean isTextuallyAnsweredByStudent(){
		return paintSubTask.getTextAnswer() != null && paintSubTask.getTextAnswer().trim().length() != 0;
	}

	public int getTextFieldWidth(){
		return paintSubTaskDef.getTextualAnswer()!=null && paintSubTaskDef.getTextualAnswer().isSetTextFieldWidth() ?
					paintSubTaskDef.getTextualAnswer().getTextFieldWidth() : 60;
	}

	public int getTextFieldHeight(){
		return paintSubTaskDef.getTextualAnswer()!=null && paintSubTaskDef.getTextualAnswer().isSetTextFieldHeight() ?
				paintSubTaskDef.getTextualAnswer().getTextFieldHeight() : 10;
	}

	public String getBackgroundPictureString() {
		if( paintSubTaskDef.getImages() == null )
			return null;
		String pic=paintSubTaskDef.getImages().getImmutableBackgroundImage();
		return pic == null ? "" : pic;
	}

	public String getMutablePictureString() {
		if( paintSubTaskDef.getImages() == null )
			return null;
		String pic=paintSubTaskDef.getImages().getMutableTemplateImage();
		return pic == null ? "" : pic;
	}

	public String getUserForegroundString() {
		String pic=paintSubTask.getPictureString();
		if(pic==null || pic.length()==0) {
            pic=getMutablePictureString();
        }
		return pic == null ? "" : pic;
	}

	public String getUndoData() {
		String pic=paintSubTask.getUndoData();
		return pic == null ? "" : pic;
	}

	public boolean isResetted() {
		return paintSubTask.isResetted();
	}

}
