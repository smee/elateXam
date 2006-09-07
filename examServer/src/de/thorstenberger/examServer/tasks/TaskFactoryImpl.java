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
package de.thorstenberger.examServer.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.model.TaskDefVO;
import de.thorstenberger.examServer.model.TaskletVO;
import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.CategoryFilter;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskCategory;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskFilter;
import de.thorstenberger.taskmodel.TaskFilterException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ComplexTaskletImpl;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.TaskDef_ComplexImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.impl.AbstractTaskFactory;
import de.thorstenberger.taskmodel.impl.TaskletCorrectionImpl;
import de.thorstenberger.taskmodel.impl.UserInfoImpl;

/**
 * @author Thorsten Berger
 *
 */
public class TaskFactoryImpl extends AbstractTaskFactory implements TaskFactory {

	// the taskhandling file is saved under user's home directory
	public static final String COMPLEX_TASKHANDLING_FILE_PREFIX = "complextask_";
	public static final String COMPLEX_TASKHANDLING_FILE_SUFFIX = ".xml";
	
	
	private List<String> availableTypes;
	
	private ExamServerManager examServerManager;
	private UserManager userManager;
	
	private TaskDefDao taskDefDao;
	private TaskHandlingDao taskHandlingDao;
	private ComplexTaskDefDAO complexTaskDefDAO;
	private ComplexTaskHandlingDAO complexTaskHandlingDAO;
	private ComplexTaskBuilder complexTaskBuilder;
	
	private Log log = LogFactory.getLog( "TaskLogger" );
	
	/**
	 * 
	 */
	public TaskFactoryImpl( ExamServerManager examServerManager, UserManager userManager, TaskDefDao taskDefDao, TaskHandlingDao taskHandlingDao, ComplexTaskDefDAO complexTaskDefDAO, ComplexTaskHandlingDAO complexTaskHandlingDAO, ComplexTaskBuilder complexTaskBuilder ) {

		this.examServerManager = examServerManager;
		this.userManager = userManager;
		
		this.taskDefDao = taskDefDao;
		this.complexTaskDefDAO = complexTaskDefDAO;
		this.taskHandlingDao = taskHandlingDao;
		this.complexTaskHandlingDAO = complexTaskHandlingDAO;
		this.complexTaskBuilder = complexTaskBuilder;
		
		availableTypes = new ArrayList<String>(); 
		availableTypes.add( TaskContants.TYPE_COMPLEX );
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#availableTypes()
	 */
	public List<String> availableTypes() {
		return availableTypes;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCategories()
	 */
	public List<TaskCategory> getCategories() {
		throw new MethodNotSupportedException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCategories(de.thorstenberger.taskmodel.CategoryFilter)
	 */
	public List<TaskCategory> getCategories(CategoryFilter categoryFilter) {
		throw new MethodNotSupportedException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDef(long)
	 */
	public TaskDef getTaskDef(long taskId) {
		TaskDefVO t = taskDefDao.getTaskDef( taskId );
		
		TaskDef_ComplexImpl ret = new TaskDef_ComplexImpl( t.getId(), t.getTitle(), t.getShortDescription(), t.getDeadline(), t.isStopped(), complexTaskDefDAO );
		ret.setShowCorrectionToUsers( t.isShowSolutionToStudents() );
		ret.setXmlTaskDefFile( new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.TASKDEFS + File.separatorChar + t.getComplexTaskFile() ) );
		
		return ret;
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs()
	 */
	public List<TaskDef> getTaskDefs() {
		List<TaskDefVO> taskDefVOs = taskDefDao.getTaskDefs();
		
		List<TaskDef> ret = new ArrayList<TaskDef>();
		
		for( TaskDefVO t : taskDefVOs ){
			
			TaskDef_ComplexImpl tdci = new TaskDef_ComplexImpl( t.getId(), t.getTitle(), t.getShortDescription(), t.getDeadline(), t.isStopped(), complexTaskDefDAO );
			tdci.setShowCorrectionToUsers( t.isShowSolutionToStudents() );
			tdci.setXmlTaskDefFile( new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.TASKDEFS + File.separatorChar + t.getComplexTaskFile() ) );
			ret.add( tdci );
			
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs(de.thorstenberger.taskmodel.TaskFilter)
	 */
	public List<TaskDef> getTaskDefs(TaskFilter filter)
			throws TaskFilterException {
		throw new MethodNotSupportedException();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTasklet(java.lang.String, long)
	 */
	public Tasklet getTasklet(String userId, long taskId) {

		TaskletVO taskletVO = taskHandlingDao.getTasklet( taskId, userId );
		if( taskletVO == null )
			return null;
		TaskDefVO taskDefVO = taskDefDao.getTaskDef( taskId );
		if( taskDefVO == null )
			throw new RuntimeException( "No corresponding taskDef found: " + taskId );
		
		File homeDir = new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.HOME + File.separatorChar + userId );
		File complexTaskHandlingFile = new File( homeDir.getAbsolutePath() + File.separatorChar +  COMPLEX_TASKHANDLING_FILE_PREFIX + taskId + COMPLEX_TASKHANDLING_FILE_SUFFIX );
		
		return instantiateTasklet( taskletVO, taskDefVO, complexTaskHandlingFile );
		
	}
	
	
	
	private Tasklet instantiateTasklet( TaskletVO taskletVO, TaskDefVO taskDefVO, File complexTaskHandlingFile ){
			
			TaskletCorrection correction =
				new TaskletCorrectionImpl( taskletVO.getPoints(), taskletVO.getAnnotation(),
										taskletVO.getCorrectorLogin(), taskletVO.getCorrectorHistory()  );			
			
			ComplexTasklet tasklet =
				new ComplexTaskletImpl( this, complexTaskBuilder, taskletVO.getLogin(), taskDefVO.getId(), 
						taskletVO.getStatus(), correction, (TaskDef_Complex)getTaskDef( taskDefVO.getId() ), complexTaskHandlingDAO, complexTaskHandlingFile );
			
			return tasklet;
			
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#createTasklet(java.lang.String, long)
	 */
	public Tasklet createTasklet(String userId, long taskId)
			throws TaskApiException {
		
		TaskletVO taskletVO = taskHandlingDao.getTasklet( taskId, userId );
		TaskDefVO taskDefVO = taskDefDao.getTaskDef( taskId );
		
		if( taskDefVO == null )
			throw new TaskApiException( "TaskDef " + taskId + " does not exist!" );
		
		if( taskletVO != null )
			throw new TaskApiException( "Tasklet (" + userId + ", " + taskId + ") does already exist!" );
		
		taskletVO = new TaskletVO();
		taskletVO.setLogin( userId );
		taskletVO.setTaskDefId( taskId );
		taskletVO.setStatus( Tasklet.INITIALIZED );
		taskletVO.setPoints( null );
		
		File homeDir = new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.HOME + File.separatorChar + userId );
		if( !homeDir.exists() )
			homeDir.mkdirs();
		File complexTaskHandlingFile = new File( homeDir.getAbsolutePath() + File.separatorChar +  COMPLEX_TASKHANDLING_FILE_PREFIX + taskId + COMPLEX_TASKHANDLING_FILE_SUFFIX );
		
		taskHandlingDao.saveTasklet( taskletVO );
		
		return instantiateTasklet( taskletVO, taskDefVO, complexTaskHandlingFile );
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTasklets(long)
	 */
	public List<Tasklet> getTasklets(long taskId) {
		List<Tasklet> ret = new ArrayList<Tasklet>();
		List<TaskletVO> taskletVOs = taskHandlingDao.getTasklets( taskId );
		TaskDefVO taskDefVO = taskDefDao.getTaskDef( taskId );
		
		for( TaskletVO taskletVO : taskletVOs ){
			
			File homeDir = new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.HOME + File.separatorChar + taskletVO.getLogin() );
			File complexTaskHandlingFile = new File( homeDir.getAbsolutePath() + File.separatorChar +  COMPLEX_TASKHANDLING_FILE_PREFIX + taskId + COMPLEX_TASKHANDLING_FILE_SUFFIX );
			
			ret.add( instantiateTasklet( taskletVO, taskDefVO, complexTaskHandlingFile ) );
		}
		
		return ret;
	}
	

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.impl.AbstractTaskFactory#getUserIdsOfAvailableTasklets(long)
	 */
	@Override
	public List<String> getUserIdsOfAvailableTasklets(long taskId) {
		return taskHandlingDao.getUserIdsOfAvailableTasklets( taskId );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.impl.AbstractTaskFactory#getUserIdsOfTaskletsAssignedToCorrector(long, java.lang.String, boolean)
	 */
	@Override
	public List<String> getUserIdsOfTaskletsAssignedToCorrector(long taskId, String correctorId) {
		return taskHandlingDao.getUserIdsOfTaskletsAssignedToCorrector( taskId, correctorId );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#storeTasklet(de.thorstenberger.taskmodel.Tasklet)
	 */
	public void storeTasklet(Tasklet tasklet) throws TaskApiException {
		
		TaskletVO taskletVO = taskHandlingDao.getTasklet( tasklet.getTaskId(), tasklet.getUserId() );
		
		boolean changed = false;
		
		if( taskletVO == null ){
			taskletVO = new TaskletVO();
			changed = true;
		}
		
		
		if( taskletVO.getTaskDefId() != tasklet.getTaskId() ){
			taskletVO.setTaskDefId( tasklet.getTaskId() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getLogin(), tasklet.getUserId() ) ){
			taskletVO.setLogin( tasklet.getUserId() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getStatus(), tasklet.getStatus() ) ){
			taskletVO.setStatus( tasklet.getStatus() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getCorrectorLogin(), tasklet.getTaskletCorrection().getCorrector() ) ){
			taskletVO.setCorrectorLogin( tasklet.getTaskletCorrection().getCorrector() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getAnnotation(), tasklet.getTaskletCorrection().getAnnotation() ) ){
			taskletVO.setAnnotation( tasklet.getTaskletCorrection().getAnnotation() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getPoints(), tasklet.getTaskletCorrection().getPoints() ) ){
			taskletVO.setPoints( tasklet.getTaskletCorrection().getPoints() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getCorrectorHistory(), tasklet.getTaskletCorrection().getCorrectorHistory() ) ){
			taskletVO.setCorrectorHistory( tasklet.getTaskletCorrection().getCorrectorHistory() );
			changed = true;
		}
		
		if( changed )
			taskHandlingDao.saveTasklet( taskletVO );

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#removeTasklet(java.lang.String, long)
	 */
	public void removeTasklet(String userId, long taskId)
			throws TaskApiException {
		throw new MethodNotSupportedException();
	}
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String, de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(String msg, Tasklet tasklet, String ip) {
		String prefix = tasklet.getUserId() + "@" + ip + ": ";
		log.info( prefix + msg );		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String, java.lang.Throwable, de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(String msg, Throwable throwable, Tasklet tasklet, String ip) {
		String prefix = tasklet.getUserId() + "@" + ip + ": ";
		log.info( prefix + msg, throwable );
	}
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getUserInfo(java.lang.String)
	 */
	public UserInfo getUserInfo(String login) {
		
		User user;
		
		try {
			user = userManager.getUserByUsername( login );
		} catch (UsernameNotFoundException e) {
			return null;
		}
		
		UserInfoImpl ret = new UserInfoImpl();
		ret.setLogin( user.getUsername() );
		ret.setFirstName( user.getFirstName() );
		ret.setName( user.getLastName() );
		ret.setEMail( user.getEmail() );		
		
		return ret;
		
	}

	private boolean objectsDiffer( Object a, Object b ){
		if( a == null && b == null )
			return false;
		if( a == null && b != null )
			return true;
		if( b == null && a != null )
			return true;
		
		return !a.equals( b );
	}

}
