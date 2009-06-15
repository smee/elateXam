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
package de.thorstenberger.taskmodel.impl;

import de.thorstenberger.taskmodel.StudentAnnotation;

/**
 * @author Thorsten Berger
 *
 */
public class StudentAnnotationImpl implements StudentAnnotation {

	private long date;
	private String text;
	private boolean acknowledged;
	
	/**
	 * 
	 */
	public StudentAnnotationImpl( String text, long date, boolean acknowledged ) {
		this.date = date;
		this.text = text;
		this.acknowledged = acknowledged;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.StudentAnnotation#getDate()
	 */
	public long getDate() {
		return date;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.StudentAnnotation#getText()
	 */
	public String getText() {
		return this.text;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.StudentAnnotation#isAcknowledged()
	 */
	public boolean isAcknowledged() {
		return acknowledged;
	}

	/**
	 * @param acknowledged The acknowledged to set.
	 */
	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object o) {
		StudentAnnotation anno = (StudentAnnotation)o;
		if( date > anno.getDate() )
			return -1;
		else if( date < anno.getDate() )
			return 1;
		else
			return 0;
	}
	
	
	
}
