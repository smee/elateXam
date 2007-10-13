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

import java.util.List;

import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;

/**
 * @author Thorsten Berger
 *
 */
public abstract class AbstractSubTasklet implements SubTasklet {

	protected ComplexTaskDefRoot complexTaskDefRoot;
	protected SubTaskDef subTaskDef;
	protected ObjectFactory objectFactory = new ObjectFactory();
	
	/**
	 * @param complexTaskDefRoot
	 */
	public AbstractSubTasklet(ComplexTaskDefRoot complexTaskDefRoot, SubTaskDef subTaskDef ) {
		super();
		this.complexTaskDefRoot = complexTaskDefRoot;
		this.subTaskDef = subTaskDef;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getHint()
	 */
	public String getHint() {
		return subTaskDef.getHint();
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
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
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

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isCorrectedByCorrector(java.lang.String)
	 */
	public boolean isCorrectedByCorrector(String corrector) {

		List<ManualSubTaskletCorrection> corrections = getManualCorrections();
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			if( corrections != null)
				for( ManualSubTaskletCorrection mstc : corrections )
					if( mstc.getCorrector().equals( corrector ) )
						return true;
			return false;
		}else{
			return corrections != null && corrections.size() > 0;
		}
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getProblem()
	 */
	public String getProblem() {
		return subTaskDef.getProblem();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getSubTaskDefId()
	 */
	public String getSubTaskDefId() {
		return subTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isNeedsManualCorrection(java.lang.String)
	 */
	public boolean isNeedsManualCorrection(String corrector) {
		if( !isSetNeedsManualCorrectionFlag() ) // the flag has to be set in any case for a return value of true
			return false;
		if( !isCorrected() ) // if there has not been at least one (manual) correction, the flag is the decisive factor
			return true;
		
		if( complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			
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

}
