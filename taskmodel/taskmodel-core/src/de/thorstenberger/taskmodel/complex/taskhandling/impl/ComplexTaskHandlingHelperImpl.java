/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex.taskhandling.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskHandlingHelperImpl implements ComplexTaskHandlingHelper {

	private File xmlComplexTaskHandlingfile;
	private JAXBContext jc;
	private ComplexTaskHandlingType complexTaskHandlingElem;
	private ObjectFactory objectFactory;
	
	/**
	 * 
	 */
	public ComplexTaskHandlingHelperImpl( File xmlComplexTaskHandlingFile ) {
		this.xmlComplexTaskHandlingfile = xmlComplexTaskHandlingFile;
		objectFactory = new ObjectFactory();
		
		try {
			
			jc = JAXBContext.newInstance( "de.thorstenberger.taskmodel.complex.jaxb" );
			
			// create it for the first time
			if( xmlComplexTaskHandlingFile == null || !xmlComplexTaskHandlingFile.exists() || xmlComplexTaskHandlingFile.length() == 0 ){
				complexTaskHandlingElem = objectFactory.createComplexTaskHandling();
				save();
				return;
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
			complexTaskHandlingElem = (ComplexTaskHandlingType) unmarshaller.unmarshal( bis );
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

		
	}
	
	public void save(){
		
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream( new FileOutputStream( xmlComplexTaskHandlingfile ) );
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
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper#getNumberOfTries()
	 */
	public int getNumberOfTries(){
		return complexTaskHandlingElem.getTry().size();
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper#addTry(long)
	 */
	public ComplexTaskHandlingType.TryType addTry( long time ){
		ComplexTaskHandlingType.TryType newTry;
		try {
			newTry = objectFactory.createComplexTaskHandlingTypeTryType();
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		
		newTry.setStartTime( time );
		newTry.setSubmitted( false );
		complexTaskHandlingElem.getTry().add( newTry );
		
		return newTry;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper#addPage(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType, int, java.lang.String)
	 */
	public ComplexTaskHandlingType.TryType.PageType addPage( ComplexTaskHandlingType.TryType tryElem,
								int pageNumber,
								String categoryRefId ){
		
		ComplexTaskHandlingType.TryType.PageType newPage;
		try {
			newPage = objectFactory.createComplexTaskHandlingTypeTryTypePageType();
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
		
		newPage.setNo( pageNumber );
		newPage.setCategoryRefID( categoryRefId );
		tryElem.getPage().add( newPage );
		
		return newPage;
}

	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper#getRecentTry()
	 */
	public ComplexTaskHandlingType.TryType getRecentTry(){
		List tries = complexTaskHandlingElem.getTry();
		if( tries.size()!=0 )
			return (ComplexTaskHandlingType.TryType) tries.get( tries.size() -1 );
		else
			return null;
	}
	

}
