/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex.taskhandling;

import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SubTask {

	private String id;
	
	/**
	 * 
	 */
	public SubTask( String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public abstract float getReachablePoints();
	
	public abstract String getProblem();
	
	public abstract String getHint();
	
	public abstract void addToPage( ComplexTaskHandlingType.TryType.PageType page );
	
	/**
	 * TODO
	 * was: modifier package
	 * @param virtualNum
	 */
	public abstract void setVirtualNum( String virtualNum );
	
	public abstract String getVirtualNum();
	
	public abstract int getHash();
	
	public abstract void doSave( SubmitData submitData ) throws IllegalStateException;
	
	public abstract void doAutoCorrection();
	
	public abstract void doManualCorrection( CorrectionSubmitData csd );
	
	public abstract boolean isCorrected();
	
	public abstract float getPoints() throws IllegalStateException;
	
	public abstract boolean isProcessed();
	
	public abstract String getCorrectionHint();
}
