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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets;

import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;

/**
 * @author Thorsten Berger
 *
 */
public interface SubTasklet_MC extends SubTasklet {

	/**
	 * nur immer eine Antwort richtig
	 */
	public static final String CAT_SINGLESELECT = "singleSelect";
	/**
	 * mehrere Antworten können richtig sein
	 */
	public static final String CAT_MULTIPLESELECT = "multipleSelect";

	public String getMcCategory();
	
	public List<Answer> getAnswers();
	
	public interface Answer{
				
		public String toString();
		
		public boolean isSelected();
		
		public boolean isCorrect();

		public boolean isCorrectlySolvedAnswer();
		
//		public void setSelected( boolean selected );
		
	}

	
}
