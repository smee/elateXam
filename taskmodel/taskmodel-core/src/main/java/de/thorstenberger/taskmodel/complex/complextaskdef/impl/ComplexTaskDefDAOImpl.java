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
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef;
import de.thorstenberger.taskmodel.util.JAXB2Validation;
import de.thorstenberger.taskmodel.util.JAXBUtils;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskDefDAOImpl implements ComplexTaskDefDAO {
  public static final Log logger = LogFactory.getLog(ComplexTaskDefDAOImpl.class);
	private ComplexTaskFactory complexTaskFactory;
	private JAXBContext jc;
	/**
	 * @param factory
	 */
	public ComplexTaskDefDAOImpl(ComplexTaskFactory factory) {
		complexTaskFactory = factory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO#getComplexTaskDefRoot(java.io.File)
	 */
	public ComplexTaskDefRoot getComplexTaskDefRoot( InputStream complexTaskIS ) throws TaskApiException {
		ComplexTaskDef complexTask;
		JAXBContext jc = createJAXBContext();
		Unmarshaller unmarshaller=null;
		try {
			unmarshaller = JAXBUtils.getJAXBUnmarshaller(jc);
      // enable validation
      unmarshaller.setSchema(JAXB2Validation.loadSchema("complexTaskDef.xsd"));

			BufferedInputStream bis = new BufferedInputStream( complexTaskIS );
			complexTask = (ComplexTaskDef) unmarshaller.
				unmarshal( bis );
			bis.close();

		} catch (UnmarshalException e) {
		  throw new TaskApiException(e.getLinkedException());
		} catch (JAXBException e1) {
			throw new TaskApiException( e1 );
		} catch (IOException e2) {
			throw new TaskApiException( e2 );
    }finally{
      if(unmarshaller!=null)
        JAXBUtils.releaseJAXBUnmarshaller(jc, unmarshaller);
    }

		return new ComplexTaskDefRootImpl( complexTask, complexTaskFactory );
	}

  /**
   * Lazily create a jaxb context. Does not need to be synchronized.
   * @return
   * @throws TaskApiException
   */
  private JAXBContext createJAXBContext()
			throws TaskApiException {
		if(jc == null)
  		try {
  			jc = JAXBContext.newInstance( "de.thorstenberger.taskmodel.complex.jaxb", this.getClass().getClassLoader() );
  		} catch (IllegalArgumentException e) {
  			throw new TaskApiException( e );
  		} catch (JAXBException e) {
  			throw new TaskModelPersistenceException( e );
  		}
		return jc;
	}

	public void save(ComplexTaskDefRoot ctdr, OutputStream os) throws TaskApiException {
		BufferedOutputStream bos = new BufferedOutputStream( os );

		Marshaller marshaller = null;
		try {
		  JAXBContext jc=createJAXBContext();
		  marshaller=JAXBUtils.getJAXBMarshaller(jc);
			Validator validator = jc.createValidator();
			ComplexTaskDef ctd=((ComplexTaskDefRootImpl)ctdr).getJAXBContent();
			validator.validate( ctd );
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true) );
			marshaller.marshal( ctd, bos );

		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		} finally{
			try {
				bos.close();
			} catch (IOException e) {
				throw new TaskModelPersistenceException( e );
			}
      if(marshaller!=null)
	      JAXBUtils.releaseJAXBMarshaller(jc, marshaller);
		}

	}

}
