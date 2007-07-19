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

import de.thorstenberger.taskmodel.TaskApiException;


/**
 * 
 * SubTasklet aggregates access/control methods for a Subtasklet and the corresponding SubtaskDef
 * 
 * @author Thorsten Berger
 *
 */
public interface SubTasklet {

	public String getSubTaskDefId();
	
	public void addToPage( Page page );

	public void setVirtualSubtaskNumber( String number );
	
	public void doSave( SubmitData submitData ) throws IllegalStateException;
	
	public boolean isCorrected();
	
	public boolean isNeedsManualCorrection();
	
	public float getPoints() throws IllegalStateException;
	
	public void doAutoCorrection();
	
	public void doManualCorrection( CorrectionSubmitData csd );
	
	public float getReachablePoints();
	
	public String getProblem();
	
	public String getHint();
		
	public String getVirtualSubtaskNumber();
	
	public int getHash();
	/**
	 * Returns whether or not this task has been processed by the user.
	 * @return
	 */
	public boolean isProcessed();
	
	public String getCorrectionHint();
	
	/**
	 * Called when the SubTasklet is initially created.
	 * SubTasklet should e.g. create random answers, assignments, texts etc
	 *
	 */
	public void build() throws TaskApiException;

}
