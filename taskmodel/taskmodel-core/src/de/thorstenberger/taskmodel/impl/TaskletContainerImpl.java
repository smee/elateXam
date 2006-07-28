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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.TaskStatistics;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletContainer;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;

/**
 * @author Thorsten Berger
 *
 */
public class TaskletContainerImpl implements TaskletContainer {

	private TaskFactory taskFactory;
	private JCS userObjectCache;
	private static Random r = new Random();
	
	/**
	 * 
	 */
	public TaskletContainerImpl( TaskFactory taskFactory ) {
		this.taskFactory = taskFactory;
		try {
			this.userObjectCache = JCS.getInstance( "userObjectCache" );
		} catch (CacheException e) {
			throw new TaskModelPersistenceException( e );
		}
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#initTasklet(long, java.lang.String)
	 */
	public synchronized Tasklet createTasklet(long taskId, String userId) throws TaskApiException{
		UserObject uo = getUserObj( userId );
		Tasklet t = uo.getTasklet( taskId, true );
		t.update();
		return t;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#getTasklet(long, java.lang.String)
	 */
	public synchronized Tasklet getTasklet(long taskId, String userId) throws TaskApiException{
		UserObject uo = getUserObj( userId );
		Tasklet t = uo.getTasklet( taskId, false );
		if( t != null )
			t.update();
		return t;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#removeTasklet(long, java.lang.String)
	 */
	public synchronized void removeTasklet(long taskId, String userId) throws TaskApiException{
		// TODO Auto-generated method stub

	}
	
	private UserObject getUserObj( String userId ){
		UserObject uo = (UserObject) userObjectCache.get( userId );
		
		if( uo == null ){
			uo = new UserObject( userId, taskFactory );
			try {
				userObjectCache.put( userId, uo );
			} catch (CacheException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		
		return uo;
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#calculateStatistics(long)
	 */
	public TaskStatistics calculateStatistics(long taskId) throws TaskApiException {
		
		List<Tasklet> tasklets = taskFactory.getTasklets( taskId );
		
		int numOfSolutions = 0;
		int numOfCorrectedSolutions = 0;
		
		for( Tasklet tasklet : tasklets ){
			
			if( !tasklet.getStatus().equals( Tasklet.INITIALIZED ) && !tasklet.getStatus().equals( Tasklet.INPROGRESS ) ){
				numOfSolutions++;
				
				if( tasklet.getStatus().equals( Tasklet.CORRECTED ) )
					numOfCorrectedSolutions++;
			}
		}
		
		return new TaskStatisticsImpl( taskId, numOfSolutions, numOfCorrectedSolutions );

		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#assignRandomTaskletToCorrector(long, java.lang.String, java.lang.String)
	 */
	public synchronized void assignRandomTaskletToCorrector(long taskId, String correctorId) throws TaskApiException {

		List<Tasklet> tasklets = taskFactory.getTasklets( taskId );
		
		List<Tasklet> assignableTasklets = new ArrayList<Tasklet>();
		
		for( Tasklet tasklet : tasklets ){
			
			if( tasklet.getStatus().equals( Tasklet.SOLVED ) && tasklet.getTaskletCorrection().getCorrector() == null )
				assignableTasklets.add( tasklet );
			
		}
		
		if( assignableTasklets.size() == 0 )
			throw new TaskApiException( TaskHandlingConstants.NO_UNCORRECTED_AND_UNASSIGNED_SOLUTIONS_AVAILABLE );
		
		assignableTasklets.get( r.nextInt( assignableTasklets.size() ) ).assignToCorrector( correctorId );
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#getTaskletsAssignedToCorrector(long, java.lang.String)
	 */
	public synchronized List<Tasklet> getTaskletsAssignedToCorrector(long taskId, String correctorId, boolean corrected ) throws TaskApiException {
		return taskFactory.getTaskletsAssignedToCorrector( taskId, correctorId, corrected );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#reset()
	 */
	public synchronized void reset() {
		try {
			userObjectCache.destroy();
		} catch (CacheException e) {
			throw new TaskModelPersistenceException( e );
		}
		
	}

	

}
