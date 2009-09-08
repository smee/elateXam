/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex;

import java.util.List;

import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;

/**
 * A complex tasklet is a composite tasklet. It contains several {@link SubTasklet}s distributed on {@link Page}s.
 * @author Thorsten Berger
 *
 */
public interface ComplexTasklet extends Tasklet {

	public boolean canStartNewTry();

	public void startNewTry( int tryNo ) throws IllegalStateException;

	public boolean canContinueTry();

	public void continueLastTry( ) throws IllegalStateException;

	public void canSavePage( int pageNo, long hashcode ) throws IllegalStateException;

	public void savePage( int pageNo, List<SubmitData> submitData, long hashcode ) throws IllegalStateException;

	public void submit() throws IllegalStateException;

	public void doManualCorrection( SubTasklet actualSubtasklet, CorrectionSubmitData csd ) throws IllegalStateException;

	public void doInteractiveFeedback( SubTasklet actualSubtasklet, SubmitData csd ) throws IllegalStateException;

	public ComplexTaskDefRoot getComplexTaskDefRoot();

	public ComplexTaskHandlingRoot getComplexTaskHandlingRoot();

	/**
	 * Returniert den zur Zeit aktiven Versuch.
	 * @return
	 * @throws IllegalStateException falls gerade kein Versuch aktiv
	 */
	public de.thorstenberger.taskmodel.complex.complextaskhandling.Try getActiveTry() throws IllegalStateException;

	/**
	 * Gibt den letzten (nicht unbedingt aktiven) Versuch zur Korrektur und Einsichtnahme zurück.
	 * TODO: rename method, the term "solution" is misleading in this case
	 * @return
	 */
	public de.thorstenberger.taskmodel.complex.complextaskhandling.Try getSolutionOfLatestTry() throws IllegalStateException;

	public boolean studentAnnotatesCorrection( String annotation ) throws IllegalStateException;

	public void acknowledgeStudentAnnotation() throws IllegalStateException;

	/**
	 * This method triggers the auto correction of all SubTasklets. After auto-correction, the overall points
	 * are calculated and set. The method's purpose is to support re-correction if the ComplexTaskDef has been changed
	 * and these changes influence the overall points.
	 * @throws IllegalStateException
	 */
	public void doAutoCorrection() throws IllegalStateException;

}
