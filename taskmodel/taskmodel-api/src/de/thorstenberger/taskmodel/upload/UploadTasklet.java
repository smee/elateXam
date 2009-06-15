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

import de.thorstenberger.taskmodel.Tasklet;

/**
 * @author Thorsten Berger
 *
 */
public interface UploadTasklet extends Tasklet {

	public static final String PROP_UPLOAD_FILENAME = "upload_filename";
	public static final String PROP_UPLOAD_CONTENTTYPE = "upload_contentType";
	public static final String PROP_UPLOAD_REVISION = "upload_revision";
	
	public boolean uploaded();
	
	public boolean correctionUploaded();
	
	public UploadFile getUploadFile() throws IllegalStateException;
	
	public UploadFile getCorrectionUploadFile() throws IllegalStateException;
	
	/**
	 * Triggered by uploading a new file by the student.
	 * @param is
	 * @param fileName
	 * @param contentType
	 * @throws IllegalStateException
	 */
	public void doUpload( InputStream is, String fileName, String contentType ) throws IllegalStateException;
	
	/**
	 * Triggered by uploading a file with correction information by the corrector.
	 * @param is
	 * @param fileName
	 * @param contentType
	 * @throws IllegalStateException
	 */
	public void doCorrectionUpload( InputStream is, String fileName, String contentType, String corrector ) throws IllegalStateException;
	
	public interface UploadFile{
		
		public InputStream getPersistenceStoreInputStream();
		
		/**
		 * Just needed to persist the (uploaded) file in the persistence store. After persisting by the TaskFactory,
		 * the content of the file can be retrieved by {@link #getPersistenceStoreInputStream()}.
		 * @return
		 */
		public InputStream getTemporaryUploadInputStream();
		
		public String getFilename();
		
		public String getContentType();
		
		/**
		 * Revision denotes the number of times a new file has been uploaded, starting with 0.
		 * @return the file's revision, starting with 0.
		 */
		public int getRevision();
		
		/**
		 * Allows the TaskFactory to inject the persistenceStoreInputStream after persisting.
		 * @param is
		 */
		public void setPersistenceStoreInputStream( InputStream is );
	}
	
}
