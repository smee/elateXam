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
package de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;

public class PaintSubmitData implements SubmitData {

	private String imageString;
	private String textualAnswer;
	private String undoData;
	private boolean isResetted;


	public PaintSubmitData(String imageString, String undoData, boolean isResetted,String textualAnswer) {
		this.imageString=imageString;
		this.textualAnswer=textualAnswer;
		this.undoData=undoData;
		this.isResetted=isResetted;
	}

	public String getTextualAnswer() {
		return textualAnswer;
	}

	public String getImageString() {
		return imageString;
	}
	public String getUndoData() {
		return undoData;
	}
	public boolean isResetted() {
		return isResetted;
	}
}
