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
package de.thorstenberger.taskmodel.complex.complextaskhandling;

import java.util.List;

import de.thorstenberger.taskmodel.TaskApiException;


/**
 *
 * SubTasklet aggregates access/control methods for a Subtasklet and the corresponding SubtaskDef
 *
 * @author Thorsten Berger
 *
 */
public interface SubTasklet {

	/**
	 * Every SubTasklet is associated with its SubTaskDef.
	 * @return the associated SubTaskDef of this SubTasklet
	 */
	public String getSubTaskDefId();

	// FIXME: add method getSubTaskDef

	public void addToPage( Page page );

	public void setVirtualSubtaskNumber( String number );

	public void doSave( SubmitData submitData ) throws IllegalStateException;

	/**
	 * Determines the correction status of the SubTasklet.
	 * @return true if the SubTasklet has been corrected automatically or there is _at least one_ manual correction, false otherwise
	 */
	public boolean isCorrected();

	public boolean isNeedsManualCorrection( String corrector );

	/**
	 * Determines whether the flag "needsManualCorrection" is set. The method isNeedsManualCorrection(String corrector) additionally checks
	 * if there is a correction necessary by the given corrector, involving the current correction mode.
	 * @see #isNeedsManualCorrection(String)
	 * @return
	 */
	public boolean isSetNeedsManualCorrectionFlag();

	// public float getPoints() throws IllegalStateException;



	public boolean isAutoCorrected();

	public SubTaskletCorrection getAutoCorrection();

	public List<ManualSubTaskletCorrection> getManualCorrections();

	/**
	 * Denotes the points a particular corrector has given, if the SubTasklet has been corrected manually.
	 * Please note that, depending on the correction mode, the corrector parameter can be ignored
	 * (as by the regular single corrector mode).
	 * @param corrector
	 * @return
	 * @throws IllegalStateException if the SubTasklet has not been corrected by the corrector or at all or if it has been corrected automatically
	 */
	public float getPointsByCorrector( String corrector ) throws IllegalStateException;

	/**
	 * Denotes whether this SubTasklet has been corrected by the given corrector.
	 * Please note that, depending on the correction mode, the corrector parameter can be ignored
	 * (as by the regular single corrector mode).
	 * @param corrector
	 * @return
	 */
	public boolean isCorrectedByCorrector( String corrector );

	public void doAutoCorrection();

	public void doManualCorrection( CorrectionSubmitData csd ) throws IllegalStateException;

	// FIXME: move to SubTaskDef
	public float getReachablePoints();

	// FIXME: move to SubTaskDef
	public String getProblem();

	// FIXME: move to SubTaskDef
	public String getHint();

	public String getVirtualSubtaskNumber();

	public int getHash();

	/**
	 * Returns whether or not this task has been processed by the user.
	 * @return
	 */
	public boolean isProcessed();



	// FIXME: move to SubTaskDef
	/**
	 * Some SubTaskDefs contain hints to advice correctors in how to correct the SubTasklet.
	 * @return correction hint of the associated SubTaskDef
	 */
	public String getCorrectionHint();

	/**
	 * Called when the SubTasklet is initially created.
	 * SubTasklet should e.g. create random answers, assignments, texts etc
	 *
	 */
	public void build() throws TaskApiException;

	/**
	 * Called on SubTasklet creation, but should initialize it for previewing purposes only, i.e.
	 * to preview the SubTasklet in the editor. Usually, this should just forward to {@link #build()}.
	 * In the case of a SubTasklet_MC, it'd make sense to append all answers in order to preview the
	 * whole SubTasklet.
	 * 
	 * @throws TaskApiException
	 */
	public void buildPreview() throws TaskApiException;

	// FIXME: move to SubTaskDef
	/**
	 * Returns whether this subtasklet allows showing interactive feedback including the result of
	 * the automatic correction after saving.
	 */
	public boolean isInteractiveFeedback();

}
