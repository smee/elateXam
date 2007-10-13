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

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.PaintBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.PaintSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.TextCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.ManualSubTaskletCorrectionImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.PaintSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Paint;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.PaintSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.PaintSubTaskType.ManualCorrectionType;

public class SubTasklet_PaintImpl extends AbstractSubTasklet implements SubTasklet_Paint {

	private Block block;
	private PaintTaskBlock paintTaskBlock;
	private PaintSubTaskDef paintSubTaskDef;
	private PaintSubTask paintSubTask;
	
	/**
	 * 
	 */
	public SubTasklet_PaintImpl( Block block, PaintSubTaskDefImpl paintSubTaskDefImpl, PaintSubTask paintSubTask, ComplexTaskDefRoot complexTaskDefRoot ) {
		super( complexTaskDefRoot, paintSubTaskDefImpl );
		this.block = block;
		this.paintTaskBlock = ((PaintBlockImpl)block).getPaintTaskBlock();
		this.paintSubTaskDef = paintSubTaskDefImpl.getPaintSubTaskDef();
		this.paintSubTask = paintSubTask;
	}

	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
		pageImpl.getPageType().getMcSubTaskOrClozeSubTaskOrTextSubTask().add( paintSubTask );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getReachablePoints()
	 */
	public float getReachablePoints() {
		return paintTaskBlock.getConfig().getPointsPerTask();
	}

	public void setVirtualSubtaskNumber(String number) {
		paintSubTask.setVirtualNum( number );
	}

	public void doSave(SubmitData submitData) throws IllegalStateException {
		PaintSubmitData psd=(PaintSubmitData) submitData;
		paintSubTask.setTextAnswer(psd.getTextualAnswer());
		paintSubTask.setPictureString(psd.getImageString());
		paintSubTask.setUndoData(psd.getUndoData());
		paintSubTask.setResetted(psd.isResetted());
	}

	public boolean isCorrected() {
		return paintSubTask.isSetAutoCorrection() || paintSubTask.isSetManualCorrection();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getAutoCorrection()
	 */
	public SubTaskletCorrection getAutoCorrection() throws IllegalStateException {
		if ( paintSubTask.isSetAutoCorrection() )
			return new AutoSubTaskletCorrectionImpl( paintSubTask.getAutoCorrection().getPoints() );
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getManualCorrections()
	 */
	public List<ManualSubTaskletCorrection> getManualCorrections() throws IllegalStateException {
		if( !paintSubTask.isSetManualCorrection() )
			return null;
		List<ManualSubTaskletCorrection> ret = new LinkedList<ManualSubTaskletCorrection>();
		List<de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.TextSubTaskType.ManualCorrectionType> mcs = paintSubTask.getManualCorrection();
		for( de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.TextSubTaskType.ManualCorrectionType mc : mcs )
			ret.add( new ManualSubTaskletCorrectionImpl( mc.getCorrector(), mc.getPoints() ) );
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isSetNeedsManualCorrectionFlag()
	 */
	public boolean isSetNeedsManualCorrectionFlag() {
		return paintSubTask.isNeedsManualCorrection();
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
		}else
			paintSubTask.setNeedsManualCorrection( true );
	}
	
	public void doManualCorrection( CorrectionSubmitData csd ){
		
		if( isAutoCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_AUTO_CORRECTED );
		
		TextCorrectionSubmitData tcsd = (TextCorrectionSubmitData) csd;
		
		if( tcsd.getPoints() < 0 || tcsd.getPoints() > paintTaskBlock.getConfig().getPointsPerTask() )
			return;
			
		List<ManualCorrectionType> manualCorrections = paintSubTask.getManualCorrection();
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			
			for( ManualCorrectionType mc : manualCorrections ){
				if( mc.getCorrector().equals( tcsd.getCorrector() ) ){
					mc.setPoints( tcsd.getPoints() );
					return;
				}
			}
			// corrector not found, so create a new ManualCorrection for him
			ManualCorrectionType mc;
			try {
				mc = objectFactory.createComplexTaskHandlingTypeTryTypePageTypePaintSubTaskTypeManualCorrectionType();
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
			mc.setCorrector( tcsd.getCorrector() );
			mc.setPoints( tcsd.getPoints() );
			manualCorrections.add( mc );
			
		}else{
			
			ManualCorrectionType mc;
			if( manualCorrections.size() > 0 ){
				mc = manualCorrections.get( 0 );
			}else{
				try {
					mc = objectFactory.createComplexTaskHandlingTypeTryTypePageTypePaintSubTaskTypeManualCorrectionType();
				} catch (JAXBException e) {
					throw new TaskModelPersistenceException( e );
				}
				manualCorrections.add( mc );
			}
			mc.setCorrector( tcsd.getCorrector() );
			mc.setPoints( tcsd.getPoints() );
		}
		
	}
	
	private void setAutoCorrection( float points ){
		ComplexTaskHandlingType.TryType.PageType.PaintSubTaskType.AutoCorrectionType corr = paintSubTask.getAutoCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypePaintSubTaskTypeAutoCorrectionType();
				paintSubTask.setAutoCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
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

	public String getVirtualSubtaskNumber() {
		return paintSubTask.getVirtualNum();
	}

	public int getHash() {
		StringBuffer sb=new StringBuffer();
		sb.append(paintSubTask.getPictureString()).append(paintSubTask.getTextAnswer());
		return sb.toString().hashCode();
	}

	public boolean isProcessed() {
		if( paintSubTask.getPictureString() == null || paintSubTask.getPictureString().trim().length() == 0 )
			return isTextuallyAnswerdByStudent();
		else
//			return !paintSubTask.getPictureString().equals( paintSubTaskDef.getImages() == null ? null : paintSubTaskDef.getImages().getMutableTemplateImage() );
			return !paintSubTask.isResetted() || isTextuallyAnswerdByStudent();
	}

	public String getCorrectionHint() {
	    return paintSubTaskDef.getCorrectionHint();
	}

	public void build() throws TaskApiException{
		paintSubTask.setTextAnswer( "" );
		paintSubTask.setResetted(false);
	}


	public String getTextualAnswer() {
		String answer = paintSubTask.getTextAnswer();
		return answer == null? "":answer;
	}
	
	private boolean isTextuallyAnswerdByStudent(){
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
		String pic=paintSubTaskDef.getImages().getImmutableBackgroundImage();
		return pic == null ? "" : pic;
	}

	public String getMutablePictureString() {
		String pic=paintSubTaskDef.getImages().getMutableTemplateImage();
		return pic == null ? "" : pic;
	}

	public String getUserForegroundString() {
		String pic=paintSubTask.getPictureString();
		if(pic==null || pic.length()==0)
			pic=getMutablePictureString();
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
