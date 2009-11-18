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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.transaction.file.ResourceManagerException;

import de.thorstenberger.examServer.dao.AbstractTransactionalFileIO;
import de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;

/**
 * Transactional taskhandling file io for every user.
 * @author Steffen Dienst
 *
 */
public class UserComplexTaskHandlingDAOImpl extends AbstractTransactionalFileIO
    implements UserComplexTaskHandlingDAO {
	
  // the taskhandling file is saved under user's home directory
  public static final String COMPLEX_TASKHANDLING_FILE_PREFIX = "complextask_";
  public static final String COMPLEX_TASKHANDLING_FILE_SUFFIX = ".xml";
  
	private final ComplexTaskHandlingDAO cthDao;

	/**
	 * @param workingDirectory
	 */
	public UserComplexTaskHandlingDAOImpl(ExamServerManager esm, ComplexTaskHandlingDAO cthDao) {
		super(esm.getHomeDir().getAbsolutePath());
		this.cthDao = cthDao;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO#load(java.lang.String, de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot)
	 */
	public ComplexTaskHandlingRoot load(String username,String taskId, ComplexTaskDefRoot complexTaskDefRoot) {
		String txId = startTransaction();
		try {
			String resource = createResourceName(username, taskId);
			// make sure, the file exists
			if(!getFRM().resourceExists(resource)) {
				getFRM().createResource(txId, resource);
			}
			
	    InputStream inputStream = getFRM().readResource(txId,resource);
	    ComplexTaskHandlingRoot complexTaskHandlingRoot = cthDao.getComplexTaskHandlingRoot(inputStream, complexTaskDefRoot);
	    inputStream.close();
	   
	    commitTransaction(txId);
	    return complexTaskHandlingRoot;
    } catch (ResourceManagerException e) {
    	rollback(txId, e);
    } catch (IOException e) {
    	rollback(txId, e);
    }
    return null;
	}

	private String createResourceName(String username, String taskId) {
	  return username+"/"+COMPLEX_TASKHANDLING_FILE_PREFIX + taskId + COMPLEX_TASKHANDLING_FILE_SUFFIX;
  }

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO#save(java.lang.String, de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot)
	 */
	public void save(String username,String taskId, ComplexTaskHandlingRoot complexTaskHandlingRoot) {
		String txId = startTransaction();
		OutputStream outputStream;
    try {
    	String resource = createResourceName(username, taskId);
    	// create dir + file if they don't exist yet
    	if(!getFRM().resourceExists(resource))
    		getFRM().createResource(txId, resource);
    	// delegate writing taskhandling
	    outputStream = getFRM().writeResource(txId, resource);
		  cthDao.save(complexTaskHandlingRoot, outputStream);
		  
		  commitTransaction(txId);
    } catch (ResourceManagerException e) {
    	throw new TaskModelPersistenceException(String.format("Could not persist tasklet informations for user '%s' on taskid '%s'",username,taskId),e);
    }

	}

}
