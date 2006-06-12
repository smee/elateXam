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

import java.io.File;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.impl.AbstractTaskDef;

/**
 * @author Thorsten Berger
 *
 */
public class TaskDef_ComplexImpl extends AbstractTaskDef implements TaskDef_Complex {

	private File xmlTaskDefFile;
	private boolean showCorrectionToUsers;
	
	private ComplexTaskDefDAO complexTaskDefDAO;
	private ComplexTaskDefRoot complexTaskDefRoot;
	
	/**
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param description
	 * @param deadline
	 * @param stopped
	 */
	public TaskDef_ComplexImpl(long id, String title, String shortDescription, Long deadline, boolean stopped, ComplexTaskDefDAO complexTaskDefDAO ) {
		super(id, title, shortDescription, deadline, stopped);

		this.complexTaskDefDAO = complexTaskDefDAO;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.TaskDef_Complex#getXmlTaskDefFile(java.io.File)
	 */
	public File getXmlTaskDefFile() {
		return xmlTaskDefFile;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.TaskDef_Complex#isShowCorrectionToUsers()
	 */
	public boolean isShowCorrectionToUsers() {
		return showCorrectionToUsers;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getType()
	 */
	public String getType() {
		return TaskContants.TYPE_COMPLEX;
	}

	/**
	 * @param showCorrectionToUsers The showCorrectionToUsers to set.
	 */
	public void setShowCorrectionToUsers(boolean showCorrectionToUsers) {
		this.showCorrectionToUsers = showCorrectionToUsers;
	}

	/**
	 * @param xmlTaskDefFile The xmlTaskDefFile to set.
	 */
	public void setXmlTaskDefFile(File xmlTaskDefFile) {
		this.xmlTaskDefFile = xmlTaskDefFile;
	}

	/**
	 * @return Returns the complexTaskDefRoot.
	 */
	public ComplexTaskDefRoot getComplexTaskDefRoot() {
		try {
			if( complexTaskDefRoot == null )
				complexTaskDefRoot = complexTaskDefDAO.getComplexTaskDefRoot( xmlTaskDefFile );
		} catch (TaskApiException e) {
			throw new TaskModelPersistenceException( e );
		}
		return complexTaskDefRoot;
	}

	



}
