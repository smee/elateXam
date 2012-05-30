/*

Copyright (C) 2007 Thorsten Berger

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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionModeType;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.ManualSubTaskletCorrectionImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.jaxb.AutoCorrectionType;
import de.thorstenberger.taskmodel.complex.jaxb.ManualCorrectionType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;

public abstract class AbstractSubTasklet implements SubTasklet{
	protected float reachablePoints;
	protected final CorrectionModeType correctionMode;
	protected SubTaskDefType jaxbSubTaskDef;
	protected SubTaskType subTaskType;

	protected ObjectFactory objectFactory = new ObjectFactory();

	/**
	 * @param complexTaskDefRoot
	 */
	public AbstractSubTasklet( SubTaskDefType subTaskDefImpl, SubTaskType subTaskType, CorrectionModeType correctionMode, float reachablePoints) {
		this.correctionMode=correctionMode;
		this.jaxbSubTaskDef=subTaskDefImpl;
		this.subTaskType=subTaskType;
	}
	/*
	 * (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#addToPage(de.thorstenberger.taskmodel.complex.complextaskhandling.Page)
	 */
	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
    pageImpl.getPage().getMcSubTaskOrClozeSubTaskOrTextSubTask().add(subTaskType);
	}
	/*
	 * (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getCorrectionHint()
	 */
	public String getCorrectionHint() {
		return jaxbSubTaskDef.getCorrectionHint();
	}
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getHint()
	 */
	public String getHint() {
		return jaxbSubTaskDef.getHint();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isAutoCorrected()
	 */
	public boolean isAutoCorrected() {
		return getAutoCorrection() != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isCorrected()
	 */
	public boolean isCorrected() {
		if( getAutoCorrection() != null )
			return true;
		List<ManualSubTaskletCorrection> mcs = getManualCorrections();
		if( mcs != null && mcs.size() > 0 )
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getPointsByCorrector(java.lang.String)
	 */
	public float getPointsByCorrector(String corrector)
			throws IllegalStateException {

		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		if( isAutoCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_AUTO_CORRECTED );

		List<ManualSubTaskletCorrection> mcs = getManualCorrections();

		// if multiple corrector mode
		if( correctionMode == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			for( ManualSubTaskletCorrection mc : mcs )
				if( mc.getCorrector().equals( corrector ) )
					return mc.getPoints();
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED_BY_CORRECTOR );
		}else{
			if( mcs.size() <= 0 )
				throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
			return mcs.get( 0 ).getPoints();
		}


	}
		/*
	 * (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getReachablePoints()
	 */
	public float getReachablePoints(){
		return this.reachablePoints;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isCorrectedByCorrector(java.lang.String)
	 */
	public boolean isCorrectedByCorrector(String corrector) {

		List<ManualSubTaskletCorrection> corrections = getManualCorrections();
		if( correctionMode == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			if( corrections != null) {
                for( ManualSubTaskletCorrection mstc : corrections )
					if( mstc.getCorrector().equals( corrector ) )
						return true;
            }
			return false;
		} else
            return corrections != null && corrections.size() > 0;

	}
		/*
	 * (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getVirtualSubtaskNumber()
	 */
	public String getVirtualSubtaskNumber() {
		return subTaskType.getVirtualNum();
	}
		/*
	 * (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#setVirtualSubtaskNumber(java.lang.String)
	 */
	public void setVirtualSubtaskNumber(String number) {
		subTaskType.setVirtualNum(number);
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getProblem()
	 */
	public String getProblem() {
		return jaxbSubTaskDef.getProblem() == null? "":jaxbSubTaskDef.getProblem();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getSubTaskDefId()
	 */
	public String getSubTaskDefId() {
		return jaxbSubTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isNeedsManualCorrection(java.lang.String)
	 */
	public boolean isNeedsManualCorrection(String corrector) {
		if( !isSetNeedsManualCorrectionFlag() ) // the flag has to be set in any case for a return value of true
			return false;
		if( !isCorrected() ) // if there has not been at least one (manual) correction, the flag is the decisive factor
			return true;

		if( correctionMode == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){

			if( isAutoCorrected() ) // should not happen, as the needsManualCorrection is only set if no auto-correction has been possible
				return false;
			else{

				List<ManualSubTaskletCorrection> mcs = getManualCorrections();
				for( ManualSubTaskletCorrection mc : mcs )
					if( mc.getCorrector().equals( corrector ) )
						return false; // ok, the corrector already corrected the SubTasklet

				return true; // no, the corrector did not correct the SubTasklet, so (according to the flag) return true
			}

		}else{
			if( isAutoCorrected() ) // should not happen, as the needsManualCorrection is only set if no auto-correction has been possible
				return false;
			else
				return getManualCorrections().size() <= 0;
		}
	}
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getAutoCorrection()
	 */
	public SubTaskletCorrection getAutoCorrection() throws IllegalStateException {
		if ( subTaskType.isSetAutoCorrection() )
			return new AutoSubTaskletCorrectionImpl( subTaskType.getAutoCorrection().getPoints() );
		else
			return null;
	}
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getManualCorrections()
	 */
	public List<ManualSubTaskletCorrection> getManualCorrections(){
		if( !subTaskType.isSetManualCorrection() )
			return Collections.EMPTY_LIST;
		List<ManualSubTaskletCorrection> ret = new LinkedList<ManualSubTaskletCorrection>();
		List<ManualCorrectionType> mcs = subTaskType.getManualCorrection();
		for( ManualCorrectionType mc : mcs ) {
            ret.add( new ManualSubTaskletCorrectionImpl( mc.getCorrector(), mc.getPoints() ) );
        }
		return ret;
	}
	public boolean isInteractiveFeedback() {
		return jaxbSubTaskDef.isSetInteractiveFeedback() && jaxbSubTaskDef.isInteractiveFeedback();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isSetNeedsManualCorrectionFlag()
	 */
	public boolean isSetNeedsManualCorrectionFlag() {
    return subTaskType.isSetNeedsManualCorrection() && subTaskType.isNeedsManualCorrection();
	}

  /**
   * @param points
   */
  protected void setAutoCorrection(float points) {
    AutoCorrectionType corr = subTaskType.getAutoCorrection();
    if (corr == null) {
      ObjectFactory of = new ObjectFactory();
      corr = of.createAutoCorrectionType();
      subTaskType.setAutoCorrection(corr);
    }
    corr.setPoints(points);
  }
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#buildPreview()
	 */
	public void buildPreview() throws TaskApiException {
		// just forward to build(), override if special care is needed for preview purposes
        build(System.nanoTime());
	}


}
