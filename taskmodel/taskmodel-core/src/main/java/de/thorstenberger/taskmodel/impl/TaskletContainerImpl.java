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
import java.util.Map;
import java.util.Random;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import de.thorstenberger.taskmodel.ManualCorrection;
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

	private final TaskFactory taskFactory;
	private JCS userObjectCache;
	private static Random r = new Random();

	/**
	 *
	 */
	public TaskletContainerImpl( final TaskFactory taskFactory ) {
        this(taskFactory, "taskmodel-core_userObjectCache");
    }

    /**
     * @param taskFactory
     * @param cacheName
     */
    public TaskletContainerImpl(final TaskFactory taskFactory, String cacheName) {
        this.taskFactory = taskFactory;

        // TODO check for at least 512MB of free memory
//		System.out.println( Runtime.getRuntime().maxMemory() );

        try {
            this.userObjectCache = JCS.getInstance(cacheName);
        } catch (final CacheException e) {
            throw new TaskModelPersistenceException(e);
        }
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#initTasklet(long, java.lang.String)
	 */
	public synchronized Tasklet createTasklet(final long taskId, final String userId) throws TaskApiException{
		final UserObject uo = getUserObj( userId );
		final Tasklet t = uo.getTasklet( taskId, true );
		t.update();
		return t;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#getTasklet(long, java.lang.String)
	 */
	public synchronized Tasklet getTasklet(final long taskId, final String userId) throws TaskApiException{
		final UserObject uo = getUserObj( userId );
		final Tasklet t = uo.getTasklet( taskId, false );
		if( t != null ) {
      t.update();
    }
		return t;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#removeTasklet(long, java.lang.String)
	 */
	public synchronized void removeTasklet(final long taskId, final String userId) throws TaskApiException{
		// TODO Auto-generated method stub

	}

	private UserObject getUserObj( final String userId ){
		UserObject uo = (UserObject) userObjectCache.get( userId );

		if( uo == null ){
			uo = new UserObject( userId, taskFactory );
			try {
				userObjectCache.put( userId, uo );
			} catch (final CacheException e) {
				throw new TaskModelPersistenceException( e );
			}
		}

		return uo;

	}

	private List<Tasklet> getTaskletsHelper( final long taskId ) throws TaskApiException{

		final List<String> userIds = taskFactory.getUserIdsOfAvailableTasklets( taskId );
		final List<Tasklet> ret = new ArrayList<Tasklet>();
		for( final String userId : userIds ) {
      ret.add( getTasklet( taskId, userId ) );
    }

		return ret;
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#getTasklets(long)
	 */
	public synchronized List<Tasklet> getTasklets(final long taskId) throws TaskApiException {
		return getTaskletsHelper( taskId );
	}

	//	private List<Tasklet> getTaskletsAssignedToCorrector( long taskId, String correctorLogin)

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#calculateStatistics(long)
	 */
	public TaskStatistics calculateStatistics(final long taskId) throws TaskApiException {
//		System.out.println( "start calculateStatistics");
//		long start = System.currentTimeMillis();
		final List<Tasklet> tasklets = getTaskletsHelper( taskId );
//		System.out.println( System.currentTimeMillis() - start );
		int numOfSolutions = 0;
		int numOfCorrectedSolutions = 0;
		int numOfAssignedSolutions = 0;

		for( final Tasklet tasklet : tasklets ){

			synchronized (tasklet) {

				if( tasklet.hasOrPassedStatus( Tasklet.Status.SOLVED ) ){
					numOfSolutions++;

					if( tasklet.hasOrPassedStatus( Tasklet.Status.CORRECTED ) ) {
            numOfCorrectedSolutions++;
          }
				}
				if( tasklet.getTaskletCorrection().getCorrector() != null ) {
          numOfAssignedSolutions++;
        }

			}
		}
//		System.out.println( System.currentTimeMillis() - start );
		return new TaskStatisticsImpl( taskId, numOfSolutions, numOfCorrectedSolutions, numOfAssignedSolutions );


	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#assignRandomTaskletToCorrector(long, java.lang.String, java.lang.String)
	 */
	public synchronized void assignRandomTaskletToCorrector(final long taskId, final String correctorId) throws TaskApiException {

		final List<Tasklet> tasklets = getTaskletsHelper( taskId );
		final List<Tasklet> assignableTasklets = new ArrayList<Tasklet>();

		for( final Tasklet tasklet : tasklets ){

			if( ( tasklet.getStatus() == Tasklet.Status.SOLVED || tasklet.getStatus() == Tasklet.Status.CORRECTING ) &&
					tasklet.getTaskletCorrection().getCorrector() == null ){

				final List<ManualCorrection> mcs = tasklet.getTaskletCorrection().getManualCorrections();
				boolean add = true;
				for( final ManualCorrection mc : mcs ) {
          if( mc.getCorrector().equals( correctorId ) ){
						add = false;
						break;
					}
        }
				if( add ) {
          assignableTasklets.add( tasklet );
        }
			}

		}

		if( assignableTasklets.size() == 0 )
            throw new TaskApiException( TaskHandlingConstants.NO_UNCORRECTED_AND_UNASSIGNED_SOLUTIONS_AVAILABLE );

		assignableTasklets.get( r.nextInt( assignableTasklets.size() ) ).assignToCorrector( correctorId );

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#distributeTaskletsToCorrectors(long, java.lang.String[])
	 */
  public Map<String, Integer> distributeTaskletsToCorrectors(final long taskId, final String... correctorIds) throws TaskApiException {

    final List<Tasklet> tasklets = getTaskletsHelper(taskId);
    return new TaskletDistributor().distributeAmong(tasklets, correctorIds);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.taskmodel.TaskletContainer#getTaskletsAssignedToCorrector(long, java.lang.String)
   */
	public synchronized List<Tasklet> getTaskletsAssignedToCorrector(final long taskId, final String correctorId ) throws TaskApiException {

		final List<String> userIds = taskFactory.getUserIdsOfTaskletsAssignedToCorrector( taskId, correctorId );
		final List<Tasklet> ret = new ArrayList<Tasklet>();
		for( final String userId : userIds ) {
      ret.add( getTasklet( taskId, userId ) );
    }

		return ret;

	}



	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#getTaskFactory()
	 */
	public TaskFactory getTaskFactory() {
		return taskFactory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#reset()
	 */
	public synchronized void reset() {
		try {
            userObjectCache.remove();
		} catch (final CacheException e) {
			throw new TaskModelPersistenceException( e );
		}

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#logPostData(java.lang.String, de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(final String msg, final Tasklet tasklet, final String ip) {
		taskFactory.logPostData(msg, tasklet, ip);
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#logPostData(java.lang.String, java.lang.Throwable, de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(final String msg, final Throwable throwable, final Tasklet tasklet, final String ip) {
		taskFactory.logPostData(msg, throwable, tasklet, ip);
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskletContainer#storeTasklet(de.thorstenberger.taskmodel.Tasklet)
	 */
	public void storeTasklet(final Tasklet tasklet) throws TaskApiException {
		taskFactory.storeTasklet(tasklet);
	}



}
