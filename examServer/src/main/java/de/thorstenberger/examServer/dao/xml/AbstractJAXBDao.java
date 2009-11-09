/*

Copyright (C) 2009 Steffen Dienst

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
package de.thorstenberger.examServer.dao.xml;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.xml.jaxb.ObjectFactory;

/**
 * @author Steffen Dienst
 * 
 */
public class AbstractJAXBDao {
    private final Log log = LogFactory.getLog(AbstractJAXBDao.class);
    private final JAXBContext jc;
    protected final File iofile;
    protected ObjectFactory objectFactory;

    public AbstractJAXBDao(final String contextPath, final File serializedXmlFile) {
        this.iofile = serializedXmlFile;
        objectFactory = new ObjectFactory();
        try {
            jc = JAXBContext.newInstance(contextPath);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshall the given xml file and return the corresponding object. Will wrap any exception into a
     * {@link RuntimeException}.
     * 
     * @return the deserialized object
     * @throws RuntimeException
     *             wrapping {@link JAXBException} of {@link FileNotFoundException}
     */
    protected Object load() {
        log.debug(String.format("Trying to load xml package from file '%s'", iofile));

        Unmarshaller unmarshaller;
        try {
            unmarshaller = jc.createUnmarshaller();
            unmarshaller.setValidating(true);
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(iofile));
            return unmarshaller.unmarshal(bis);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes the object into xml. Will wrap any exception into a {@link RuntimeException}.
     * 
     * @param obj
     *            the object to save.
     * @throws RuntimeException
     *             wrapping {@link JAXBException} of {@link IOException}
     */
    protected void save(final Object obj) {
        log.debug(String.format("Trying to save xml package to file '%s'", iofile));
        try {

            final Marshaller marshaller = jc.createMarshaller();
            final Validator validator = jc.createValidator();
            validator.validate(obj);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(iofile));
            marshaller.marshal(obj, bos);

            bos.close();

        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        } catch (final IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}
