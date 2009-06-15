/*

Copyright (C) 2007 Thorsten Berger

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
package de.thorstenberger.taskmodel.upload;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.impl.AbstractTasklet;

/**
 * @author Thorsten Berger
 *
 */
public class UploadTaskletImpl extends AbstractTasklet implements UploadTasklet {

	private UploadFile uploadFile, correctionUploadFile;
	
	/**
	 * @param taskFactory
	 * @param userId
	 * @param taskId
	 * @param status
	 * @param flags
	 * @param taskletCorrection
	 */
	public UploadTaskletImpl(TaskFactory taskFactory, String userId,
			TaskDef_Upload uploadTaskDef, Status status, List<String> flags,
			TaskletCorrection taskletCorrection, Map<String, String> properties) {
		super(taskFactory, userId, uploadTaskDef, status, flags, taskletCorrection, properties);
		
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#doUpload(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	public synchronized void doUpload(InputStream is, String fileName, String contentType)
			throws IllegalStateException {
		
		checkActive();
		
		if( getStatus() == Status.CORRECTING )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_UPLOAD_CORRECTING_TASKLET );
		if( hasOrPassedStatus( Status.CORRECTED ) )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_UPLOAD_TASKLET_CORRECTED );
		
		int revision = -1;
		if( uploadFile != null )
			revision = uploadFile.getRevision();
		
		this.uploadFile = new UploadFileImpl( fileName, contentType, is, revision + 1 );
		setStatus( Status.SOLVED );
		
		try {
			save();
		} catch (TaskApiException e) {
			throw new TaskModelPersistenceException( e );
		}

	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#doCorrectionUpload(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized void doCorrectionUpload(InputStream is, String fileName,
			String contentType, String corrector) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#correctionUploaded()
	 */
	public synchronized boolean correctionUploaded() {
		return hasOrPassedStatus( Status.CORRECTED ) && correctionUploadFile != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#getCorrectionUploadFile()
	 */
	public synchronized UploadFile getCorrectionUploadFile() throws IllegalStateException {
		if( !correctionUploaded() )
			throw new IllegalStateException( "Correction has not been uploaded by corrector." );
		return correctionUploadFile;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#getUploadFile()
	 */
	public synchronized UploadFile getUploadFile() throws IllegalStateException {
		if( !uploaded() )
			throw new IllegalStateException( "Correction has not been uploaded by corrector." );
		return uploadFile;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.upload.UploadTasklet#uploaded()
	 */
	public synchronized boolean uploaded() {
		return hasOrPassedStatus( Status.SOLVED ) && uploadFile != null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#update()
	 */
	public synchronized void update() {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * non-interface method for injecting the upload file
	 *
	 */
	public synchronized void setUploadFile( UploadFile uploadFile ){
		this.uploadFile = uploadFile;
	}
	
	public class UploadFileImpl implements UploadFile{

		private String filename, contentType;
		private InputStream temporaryUploadInputStream, persistenceStoreInputStream;
		private int revision;
		
		/**
		 * @param filename
		 * @param contentType
		 * @param inputStream
		 */
		public UploadFileImpl(String filename, String contentType, InputStream temporaryUploadInputStream, int revision) {
			super();
			this.filename = filename;
			this.contentType = contentType;
			this.temporaryUploadInputStream = temporaryUploadInputStream;
			this.revision = revision;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#getContentType()
		 */
		public String getContentType() {
			return contentType;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#getFilename()
		 */
		public String getFilename() {
			return filename;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#getPersistenceStoreInputStream()
		 */
		public InputStream getPersistenceStoreInputStream() {
			return persistenceStoreInputStream;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#getTemporaryUploadInputStream()
		 */
		public InputStream getTemporaryUploadInputStream() {
			return temporaryUploadInputStream;
		}

		
		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#setPersistenceStoreInputStream(java.io.InputStream)
		 */
		public void setPersistenceStoreInputStream(InputStream is) {
			this.persistenceStoreInputStream = is;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.upload.UploadTasklet.UploadFile#getRevision()
		 */
		public int getRevision() {
			return revision;
		}
		
	}

}
