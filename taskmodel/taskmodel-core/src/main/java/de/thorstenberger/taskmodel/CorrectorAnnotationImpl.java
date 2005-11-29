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
package de.thorstenberger.taskmodel;

/**
 * @author Thorsten Berger
 *
 */
public class CorrectorAnnotationImpl implements CorrectorAnnotation {

	private String corrector;
	private String text;
	
	
	
	/**
	 * @param corrector
	 * @param text
	 */
	public CorrectorAnnotationImpl(String corrector, String text) {
		super();
		this.corrector = corrector;
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorAnnotation#getCorrector()
	 */
	public String getCorrector() {
		return corrector;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorAnnotation#getText()
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.CorrectorAnnotation#setText(java.lang.String)
	 */
	public void setText(String value) {
		this.text = value;
	}

}
