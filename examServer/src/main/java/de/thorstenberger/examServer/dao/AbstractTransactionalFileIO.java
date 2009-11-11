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
package de.thorstenberger.examServer.dao;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.file.ResourceManagerSystemException;
import org.apache.commons.transaction.util.CommonsLoggingLogger;

import com.google.common.collect.Maps;

/**
 * Provide transactional file io functionality. Every access to any file within the directory specified in the
 * constructor has to happen via calls to the {@link FileResourceManager} returned by {@link #getFRM()}. No other ways
 * of accessing file contents of that directory are allowed, else the ACID properties won't be guranteed!
 * 
 * <p>
 * Usage: Writing access
 * <ol>
 * <li>String txId = {@link #startTransaction()}</li>
 * <li>call (write|copy|move)Resource(txId,"filename") on {@link #getFRM()}</li>
 * <li>{@link #commitTransaction(String)} or {@link #rollback(String, Throwable)} for every exception</li>
 * </ol>
 * <p>
 * Reading only:
 * <ol>
 * <li>call readResource("filename") on {@link #getFRM()} without explicit start/commitTransaction calls</li>
 * <li>make sure to close the stream returned by readResource(String)!</li>
 * </ol>
 * 
 * @author Steffen Dienst
 * 
 */
public class AbstractTransactionalFileIO {
    final Log log = LogFactory.getLog(AbstractTransactionalFileIO.class);
    /**
     * Map of directories to {@link FileResourceManager} instances. This way we have exactly one manager
     * per directory.
     */
    private static final Map<String, FileResourceManager> resourceManagers = Maps.newHashMap();
    protected final String workingPath;

    /**
     * Provide file transactions for the given directory.
     * 
     * @param workingDirectory
     */
    public AbstractTransactionalFileIO(final String workingDirectory) {
        this.workingPath = workingDirectory;
        initFileTransactionsForDirectory(workingDirectory);
    }

    /**
     * Commit the changes to the current working directory atomically.
     * 
     * @param txId
     *            id of the current transaction
     */
    protected void commitTransaction(final String txId) {
        final FileResourceManager frm = getFRM();
        try {
            frm.prepareTransaction(txId);
            frm.commitTransaction(txId);
        } catch (final ResourceManagerException e) {
            rollback(txId, e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Get the current {@link FileResourceManager}. Use it for accessing file resources.
     * 
     * @return
     */
    protected FileResourceManager getFRM() {
        return resourceManagers.get(this.workingPath);
    }

    /**
     * Create a new file transaction manager for the given directory.
     * 
     * @param path
     *            directory
     */
    private synchronized void initFileTransactionsForDirectory(final String path) {
        if (resourceManagers.get(path) == null) {
            resourceManagers.put(path, new FileResourceManager(path, path + "_dirty", false, new CommonsLoggingLogger(log)));
            try {
                resourceManagers.get(path).start();
            } catch (final ResourceManagerSystemException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    /**
     * Rollback all changes to the current working directory.
     * 
     * @param txId
     *            id of the current transaction
     * @param e
     *            cause of the rollback, will get wrapped in a {@link RuntimeException} and rethrown
     */
    protected void rollback(final String txId, final Throwable e) {
        try {
            getFRM().rollbackTransaction(txId);
        } catch (final ResourceManagerException e1) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Start a new transaction on the current working directory.
     * 
     * @return an unique transaction id
     */
    protected String startTransaction() {
        final FileResourceManager frm = resourceManagers.get(workingPath);
        try {
            final String txId = frm.generatedUniqueTxId();
            frm.startTransaction(txId);
            return txId;
        } catch (final ResourceManagerSystemException e) {
            throw new RuntimeException("Could not start new transaction on directory " + workingPath, e);
        } catch (final ResourceManagerException e) {
            throw new RuntimeException("Could not start new transaction on directory " + workingPath, e);
        }
    }

}