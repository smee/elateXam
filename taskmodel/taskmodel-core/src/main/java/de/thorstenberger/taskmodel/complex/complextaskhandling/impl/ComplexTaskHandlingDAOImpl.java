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
package de.thorstenberger.taskmodel.complex.complextaskhandling.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.util.JAXB2Validation;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskHandlingDAOImpl implements ComplexTaskHandlingDAO {

	private ComplexTaskFactory complexTaskFactory;
	private JAXBContext jc;

	/**
	 *
	 */
	public ComplexTaskHandlingDAOImpl( ComplexTaskFactory complexTaskFactory ) {
		this.complexTaskFactory = complexTaskFactory;
		try {
			jc = JAXBContext.newInstance( "de.thorstenberger.taskmodel.complex.jaxb", this.getClass().getClassLoader() );
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO#getComplexTaskHandlingRoot(java.io.File, de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot)
	 */
	public ComplexTaskHandlingRoot getComplexTaskHandlingRoot(
			InputStream complexTaskletIS, ComplexTaskDefRoot complexTaskDefRoot) {

		ObjectFactory objectFactory = new ObjectFactory();
		ComplexTaskHandling complexTaskHandlingElem;

		BufferedInputStream bis = new BufferedInputStream( complexTaskletIS );
		Unmarshaller unmarshaller;
		try {

			if( bis.available() <= 0 ){
				// create it for the first time
				complexTaskHandlingElem = objectFactory.createComplexTaskHandling();

			}else{

				unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(JAXB2Validation.loadSchema("complexTaskHandling.xsd"));
				complexTaskHandlingElem = (ComplexTaskHandling) unmarshaller.unmarshal( bis );

				// create it for the first time
				if( complexTaskHandlingElem == null ) {
          complexTaskHandlingElem = objectFactory.createComplexTaskHandling();
        }

			}

		} catch (JAXBException e1) {
			throw new TaskModelPersistenceException( e1 );
		} catch( IOException e2 ){
			throw new TaskModelPersistenceException( e2 );
		} finally{
			try {
				bis.close();
			} catch (IOException e) {
				throw new TaskModelPersistenceException( e );
			}
		}

		return new ComplexTaskHandlingRootImpl( complexTaskDefRoot, complexTaskFactory, complexTaskHandlingElem );

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO#save(de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot)
	 */
	public void save(ComplexTaskHandlingRoot complexTaskHandlingRoot, OutputStream complexTaskletOS ) {
		ComplexTaskHandlingRootImpl impl = (ComplexTaskHandlingRootImpl)complexTaskHandlingRoot;
		save( complexTaskletOS, impl.getComplexTaskHandlingElem() );
	}

	private void save( OutputStream complexTaskletOS, ComplexTaskHandling complexTaskHandlingElem ){

		BufferedOutputStream bos = new BufferedOutputStream( complexTaskletOS );

		try {

			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true) );
			marshaller.marshal( complexTaskHandlingElem, bos );

		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		} finally{
			try {
				bos.close();
			} catch (IOException e) {
				throw new TaskModelPersistenceException( e );
			}
		}

	}

}
