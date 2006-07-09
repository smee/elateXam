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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;

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
			File xmlComplexTaskHandlingFile, ComplexTaskDefRoot complexTaskDefRoot) {

		ObjectFactory objectFactory = new ObjectFactory();
		ComplexTaskHandling complexTaskHandlingElem;
		
		try {
			
			// create it for the first time
			if( xmlComplexTaskHandlingFile == null || !xmlComplexTaskHandlingFile.exists() || xmlComplexTaskHandlingFile.length() == 0 ){
				complexTaskHandlingElem = objectFactory.createComplexTaskHandling();
				save( xmlComplexTaskHandlingFile, complexTaskHandlingElem );
				return new ComplexTaskHandlingRootImpl( complexTaskDefRoot, complexTaskFactory, complexTaskHandlingElem );
			}
			
		} catch (IllegalArgumentException e) {
			throw new TaskModelPersistenceException( e );
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		
		
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream( new FileInputStream( xmlComplexTaskHandlingFile ) );
		} catch (FileNotFoundException e3) {
			throw new TaskModelPersistenceException( e3 );
		}
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setValidating( true );
			complexTaskHandlingElem = (ComplexTaskHandling) unmarshaller.unmarshal( bis );
//			bis.close();

		} catch (JAXBException e1) {
			throw new TaskModelPersistenceException( e1 );
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
	public void save(ComplexTaskHandlingRoot complexTaskHandlingRoot, File xmlComplexTaskHandlingFile ) {
		ComplexTaskHandlingRootImpl impl = (ComplexTaskHandlingRootImpl)complexTaskHandlingRoot;
		save( xmlComplexTaskHandlingFile, impl.getComplexTaskHandlingElem() );
	}

	private void save( File xmlComplexTaskHandlingFile, ComplexTaskHandling complexTaskHandlingElem ){
		
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream( new FileOutputStream( xmlComplexTaskHandlingFile ) );
		} catch (FileNotFoundException e2) {
			throw new TaskModelPersistenceException( e2 );
		}
		
		try {
			
			Marshaller marshaller = jc.createMarshaller();
			Validator validator = jc.createValidator();
			validator.validate( complexTaskHandlingElem );
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true) );
			marshaller.marshal( complexTaskHandlingElem, bos );
			
//			bos.close();
			
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
