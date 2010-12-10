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
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.ResourceManagerException;

import de.thorstenberger.examServer.dao.AbstractTransactionalFileIO;
import de.thorstenberger.examServer.dao.xml.jaxb.ObjectFactory;

/**
 * DAO that accesses jaxb xml files within a transaction. Every call to {@link #load()} and {@link #save(Object)}
 * happens atomically with gurantee
 *
 * @author Steffen Dienst
 *
 */
public class AbstractJAXBDao extends AbstractTransactionalFileIO {
    final Log log = LogFactory.getLog(AbstractJAXBDao.class);
    private final JAXBContext jc;
    protected ObjectFactory objectFactory;

    private final String xmlFileName;

    public AbstractJAXBDao(final String jaxbContextPath, final File workingDirectory, final String xmlFileName) {
        super(workingDirectory.getAbsolutePath());
        this.xmlFileName = xmlFileName;
        objectFactory = new ObjectFactory();
        try {
            jc = JAXBContext.newInstance(jaxbContextPath);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Does the file we are reading from/writing to exist?
     *
     * @return
     */
    protected boolean existsWorkingFile() {
        return new File(this.workingPath, xmlFileName).exists();
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
        log.debug(String.format("Trying to load xml package from file '%s'", workingPath + "/" + xmlFileName));
        // start new file transaction

        try {
            // deserialize the xml
            Unmarshaller unmarshaller;
            unmarshaller = jc.createUnmarshaller();
            final BufferedInputStream bis = new BufferedInputStream(getFRM().readResource(xmlFileName));
            final Object obj = unmarshaller.unmarshal(bis);
            bis.close();
            // finish the transaction
            return obj;

        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        } catch (final ResourceManagerException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
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
        log.debug(String.format("Trying to save xml package to file '%s'", workingPath + "/" + xmlFileName));
        final String txId = startTransaction();
        try {

            final Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            final BufferedOutputStream bos = new BufferedOutputStream(getFRM().writeResource(txId, this.xmlFileName));
            marshaller.marshal(obj, bos);

            bos.close();
            commitTransaction(txId);
        } catch (final JAXBException e) {
            rollback(txId, e);
            throw new RuntimeException(e);
        } catch (final IOException e) {
            rollback(txId, e);
            throw new RuntimeException(e);
        } catch (final ResourceManagerException e) {
            rollback(txId, e);
            throw new RuntimeException(e);
        }
    }
}
