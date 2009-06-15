/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel.impl;

import java.util.List;
import java.util.Map;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;

/**
 * @author Thorsten Berger
 *
 */
public abstract class AbstractTasklet implements Tasklet {

	protected TaskFactory taskFactory;
	private String userId;
	private Status status;
	private List<String> flags;
	protected TaskletCorrection taskletCorrection;
	
	protected TaskDef taskDef;
	
	private Map<String, String> properties;
	
	/**
	 * 
	 */
	public AbstractTasklet( TaskFactory taskFactory, String userId, TaskDef taskDef, Status status, List<String> flags, TaskletCorrection taskletCorrection, Map<String, String> properties ) {
		this.taskFactory = taskFactory;
		this.userId = userId;
		this.taskDef = taskDef;
		this.status = status;
		this.flags = flags;
		this.taskletCorrection = taskletCorrection;
		this.properties = properties;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getUserId()
	 */
	public String getUserId() {
		return userId;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getTaskId()
	 */
	public long getTaskId() {
		return taskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getStatus()
	 */
	public synchronized Status getStatus() {
		return status;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#hasOrPassedStatus(java.lang.String)
	 */
	public synchronized boolean hasOrPassedStatus(Status status) {		
		return this.status.getOrder() >= status.getOrder();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getFlags()
	 */
	public List<String> getFlags() {
		return flags;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#addFlag(java.lang.String)
	 */
	public void addFlag(String flag) {
		if( !flags.contains( flag ) )
			flags.add( flag );
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#removeFlag(java.lang.String)
	 */
	public void removeFlag(String flag) {
		while( flags.contains( flag ) )
			flags.remove( flag );
	}

	/**
	 * Mandatory check whether the TaskDef is currently active.
	 */
	protected synchronized void checkActive() throws IllegalStateException{
		if( !taskDef.isActive() )
			throw new IllegalStateException( TaskHandlingConstants.NOT_ACTIVE );
	}
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#assignToCorrector(java.lang.String)
	 */
	public synchronized void assignToCorrector(String correctorId) throws TaskApiException {
		
		if( correctorId == null )
			throw new NullPointerException();
		
		if( !hasOrPassedStatus( Status.SOLVED ) )
			throw new TaskApiException( "Cannot assign Tasklet without at least status \"solved\" to corrector." );
		
		if( getTaskletCorrection().getCorrector() != null ){
			getTaskletCorrection().getCorrectorHistory().add( getTaskletCorrection().getCorrector() );
		}
			
		getTaskletCorrection().setCorrector( correctorId );
		if( getStatus() == Status.SOLVED )
			setStatus( Status.CORRECTING );
		
		// don't forget to save
		save();
	}

	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#unassignFromCorrector()
	 */
	public void unassignFromCorrector() throws TaskApiException {

		if( getTaskletCorrection().getCorrector() == null )
			throw new TaskApiException( "Tasklet not assigned to any corrector." );

		// move current corrector to history
		getTaskletCorrection().getCorrectorHistory().add( getTaskletCorrection().getCorrector() );
		
		getTaskletCorrection().setCorrector( null );
		
		// don't forget to save
		save();
		
	}

	protected synchronized void setStatus( Status status ){
		this.status = status;
	}
	
	/**
	 * saves the Tasklet in the persistent store
	 * @throws TaskApiException
	 */
	protected synchronized void save() throws TaskApiException{
		taskFactory.storeTasklet( this );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getTaskletCorrection()
	 */
	public synchronized TaskletCorrection getTaskletCorrection() {
		return taskletCorrection;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#logPostData(java.lang.String, java.lang.String)
	 */
	public void logPostData(String msg, String ip) {
		taskFactory.logPostData( msg, this, ip );		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#logPostData(java.lang.String, java.lang.Throwable, java.lang.String)
	 */
	public void logPostData(String msg, Throwable throwable, String ip) {
		taskFactory.logPostData( msg, throwable, this, ip );		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return properties.get( key );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String key, String value) {
		if( value == null ){
			if( properties.containsKey( key ) )
				properties.remove( key );
		}else
			properties.put( key, value );
			
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.Tasklet#getProperties()
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
}
