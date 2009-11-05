/*

Copyright (C) 2007 Steffen Dienst

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
package de.thorstenberger.examServer.webapp.form;

public class TaskForm extends BaseForm {
	private long id;
	private String title;
	private String type;
	private String shortDescription;
	private boolean stopped;

	private boolean showSolutionToStudents;


	public String getTitle() {
		return title;
	}

	/**
	 * @struts.validator type="required"
	 * @return
	 */
	public void setTitle( String title ) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	/**
	 * @struts.validator type="required"
	 * @return
	 */
	public void setType( String type ) {
		this.type = type;
	}


	public boolean isStopped() {
		return stopped;
	}

	public void setStopped( boolean stopped ) {
		this.stopped = stopped;
	}


	public boolean isShowSolutionToStudents() {
		return showSolutionToStudents;
	}

	public void setShowSolutionToStudents( boolean showSolutionToStudents ) {
		this.showSolutionToStudents = showSolutionToStudents;
	}

	public long getId() {
		return id;
	}
	/**
	 * @struts.validator type="required"
	 * @return
	 */
	public void setId( long id ) {
		this.id = id;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription( String shortDescription ) {
		this.shortDescription = shortDescription;
	}

}
