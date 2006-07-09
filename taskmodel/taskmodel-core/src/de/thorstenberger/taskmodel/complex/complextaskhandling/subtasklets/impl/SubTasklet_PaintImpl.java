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

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.PaintBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.PaintSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.PaintSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Paint;
import de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.PaintSubTask;

public class SubTasklet_PaintImpl implements SubTasklet_Paint {

	private Block block;
	private PaintTaskBlock paintTaskBlock;
	private PaintSubTaskDef paintSubTaskDef;
	private PaintSubTask paintSubTask;
	
	/**
	 * 
	 */
	public SubTasklet_PaintImpl( Block block, PaintSubTaskDefImpl paintSubTaskDefImpl, PaintSubTask paintSubTask ) {
		this.block = block;
		this.paintTaskBlock = ((PaintBlockImpl)block).getPaintTaskBlock();
		this.paintSubTaskDef = paintSubTaskDefImpl.getPaintSubTaskDef();
		this.paintSubTask = paintSubTask;
	}

	public String getSubTaskDefId() {
		return paintSubTaskDef.getId();
	}

	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
		pageImpl.getPageType().getMcSubTaskOrClozeSubTaskOrTextSubTask().add( paintSubTask );
	}

	public void setVirtualSubtaskNumber(String number) {
		paintSubTask.setVirtualNum( number );
	}

	public void doSave(SubmitData submitData) throws IllegalStateException {
		PaintSubmitData psd=(PaintSubmitData) submitData;
		paintSubTask.setTextAnswer(psd.getTextualAnswer());
		paintSubTask.setPictureString(psd.getImageString());
	}

	public boolean isCorrected() {
		return paintSubTask.getCorrection() != null;
	}

	public float getPoints() throws IllegalStateException {
		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return paintSubTask.getCorrection().getPoints();
	}

	public void doAutoCorrection() {
		// TODO Auto-generated method stub

	}

	public void doManualCorrection(CorrectionSubmitData csd) {
		// TODO Auto-generated method stub

	}

	public float getReachablePoints() {
		return paintTaskBlock.getConfig().getPointsPerTask();
	}

	public String getProblem() {
		return paintSubTaskDef.getProblem() == null ? "" : paintSubTaskDef.getProblem();
	}

	public String getHint() {
		return paintSubTaskDef.getHint();
	}

	public String getVirtualSubtaskNumber() {
		return paintSubTask.getVirtualNum();
	}

	public int getHash() {
		StringBuffer sb=new StringBuffer();
		sb.append(paintSubTask.getPictureString()).append(paintSubTask.getTextAnswer());
		return sb.toString().hashCode();
	}

	public boolean isProcessed() {
		//TODO
		return false;
	}

	public String getCorrectionHint() {
	    return paintSubTaskDef.getCorrectionHint();
	}

	public void build() throws TaskApiException{
		paintSubTask.setTextAnswer( "" );
	}

	public String getPictureString() {
		return paintSubTask.getPictureString();
	}

	public String getTextualAnswer() {
		return paintSubTask.getTextAnswer();
	}

	public int getTextFieldWidth(){
		return paintSubTaskDef.getTextualAnswer()!=null && paintSubTaskDef.getTextualAnswer().isSetTextFieldWidth() ?
					paintSubTaskDef.getTextualAnswer().getTextFieldWidth() : 60;
	}
	
	public int getTextFieldHeight(){
		return paintSubTaskDef.getTextualAnswer()!=null && paintSubTaskDef.getTextualAnswer().isSetTextFieldHeight() ?
				paintSubTaskDef.getTextualAnswer().getTextFieldHeight() : 15;
	}

}
