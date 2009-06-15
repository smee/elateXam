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
package de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata;

import java.util.HashMap;
import java.util.Map;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;

/**
 * @author Thorsten Berger
 *
 */
public class ClozeSubmitData implements SubmitData {

	Map gapValues;
	private int forVirtualSubtaskNumber;
	
	/**
	 * @param relativeNumber
	 */
	public ClozeSubmitData() {
		gapValues = new HashMap();
	}
	
	public void setGapValue( int index, String value ){
		gapValues.put( new Integer( index ), value );
	}
	
	public String getGapValue( int index ){
		String ret = (String) gapValues.get( new Integer(index) );
		if( ret == null )
			return "";
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData#getForVirtualSubtaskNumber()
	 */
	public int getForVirtualSubtaskNumber() {
		return forVirtualSubtaskNumber;
	}

}
