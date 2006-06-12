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
package de.thorstenberger.taskmodel.complex.complextaskdef.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskDefDAOImpl implements ComplexTaskDefDAO {

	private ComplexTaskFactory complexTaskFactory;

	/**
	 * @param factory
	 */
	public ComplexTaskDefDAOImpl(ComplexTaskFactory factory) {
		complexTaskFactory = factory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO#getComplexTaskDefRoot(java.io.File)
	 */
	public ComplexTaskDefRoot getComplexTaskDefRoot( File complexTaskFile ) throws TaskApiException {
		JAXBContext jc;
		ComplexTaskDef complexTask;
		try {
			jc = JAXBContext.newInstance( "de.thorstenberger.taskmodel.complex.jaxb" );
		} catch (IllegalArgumentException e) {
			throw new TaskApiException( e );
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setValidating( true );
			BufferedInputStream bis = new BufferedInputStream( new FileInputStream( complexTaskFile ) );
			complexTask = (ComplexTaskDef) unmarshaller.
				unmarshal( bis );
			bis.close();

		} catch (JAXBException e1) {
			throw new TaskModelPersistenceException( e1 );
		} catch (IOException e2) {
			throw new TaskModelPersistenceException( e2 );
		}
		
		return new ComplexTaskDefRootImpl( complexTask, complexTaskFactory );
	}

}
