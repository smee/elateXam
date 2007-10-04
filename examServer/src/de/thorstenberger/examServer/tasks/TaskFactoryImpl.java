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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.model.CorrectorTaskletAnnotationVO;
import de.thorstenberger.examServer.model.StudentTaskletAnnotationVO;
import de.thorstenberger.examServer.model.TaskDefVO;
import de.thorstenberger.examServer.model.TaskletVO;
import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.model.TaskletVO.ManualCorrectionsVO;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.CategoryFilter;
import de.thorstenberger.taskmodel.CorrectorAnnotation;
import de.thorstenberger.taskmodel.CorrectorAnnotationImpl;
import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.StudentAnnotation;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskCategory;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskFilter;
import de.thorstenberger.taskmodel.TaskFilterException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.TaskmodelUtil;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ComplexTaskletImpl;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.TaskDef_ComplexImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.impl.AbstractTaskFactory;
import de.thorstenberger.taskmodel.impl.ManualCorrectionImpl;
import de.thorstenberger.taskmodel.impl.StudentAnnotationImpl;
import de.thorstenberger.taskmodel.impl.TaskletCorrectionImpl;
import de.thorstenberger.taskmodel.impl.UserInfoImpl;

/**
 * @author Thorsten Berger
 * FIXME: move caching into TaskManagerImpl of Taskmodel-core
 */
public class TaskFactoryImpl extends AbstractTaskFactory implements TaskFactory {

	// the taskhandling file is saved under user's home directory
	public static final String COMPLEX_TASKHANDLING_FILE_PREFIX = "complextask_";
	public static final String COMPLEX_TASKHANDLING_FILE_SUFFIX = ".xml";
	public static final String COMPLEX_TASKHANDLING_BACKUP_FILE_SUFFIX = ".bak";

	public static final String USER_ATTRIBUTE_SEMESTER = "user.student-info.semester";
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskDef(long)
	 */
	public void deleteTaskDef(long id) throws MethodNotSupportedException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCategory(long)
	 */
	public TaskCategory getCategory(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#storeTaskCategory(de.thorstenberger.taskmodel.TaskCategory)
	 */
	public void storeTaskCategory(TaskCategory category) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#storeTaskDef(de.thorstenberger.taskmodel.TaskDef, long)
	 */
	public void storeTaskDef(TaskDef taskDef, long taskCategoryId) throws TaskApiException {
		// TODO Auto-generated method stub
		
	}

	private List<String> availableTypes;
	
	private ExamServerManager examServerManager;
	private UserManager userManager;
	
	private TaskDefDao taskDefDao;
	private TaskHandlingDao taskHandlingDao;
	private ComplexTaskDefDAO complexTaskDefDAO;
	private ComplexTaskHandlingDAO complexTaskHandlingDAO;
	private ComplexTaskBuilder complexTaskBuilder;
	
	private Log log = LogFactory.getLog( "TaskLogger" );
	
	
	private List<TaskDef> taskDefCache = null;
	
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
	public synchronized TaskDef getTaskDef(long taskId) {

		List<TaskDef> taskDefs = getTaskDefs();
		Iterator it = taskDefs.iterator();
		while( it.hasNext() ){
			TaskDef td = (TaskDef)it.next();
			if( td.getId() == taskId )
				return td;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs()
	 */
	public synchronized List<TaskDef> getTaskDefs() {
		
		if( taskDefCache == null ){
		
			List<TaskDefVO> taskDefVOs = taskDefDao.getTaskDefs();
			
			taskDefCache = new ArrayList<TaskDef>();
			
			for( TaskDefVO t : taskDefVOs ){
				
				TaskDef_ComplexImpl tdci;
				try {
					tdci = new TaskDef_ComplexImpl( t.getId(), t.getTitle(),
							t.getShortDescription(), t.getDeadline(), t.isStopped(), t.getFollowingTaskId(), complexTaskDefDAO, new FileInputStream( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.TASKDEFS + File.separatorChar + t.getComplexTaskFile() ) );
				} catch (FileNotFoundException e) {
					throw new TaskModelPersistenceException( e );
				}
				tdci.setShowCorrectionToUsers( t.isShowSolutionToStudents() );
				tdci.setVisible( t.isVisible() );
				taskDefCache.add( tdci );
				
			}
			
		}
		
		return taskDefCache;
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
		
		// corrector annotations
		List<CorrectorAnnotation> cas = new LinkedList<CorrectorAnnotation>();
		List<CorrectorTaskletAnnotationVO> ctavos = taskletVO.getCorrectorAnnotations();
		if( ctavos != null && ctavos.size() > 0 ){
			for( CorrectorTaskletAnnotationVO ctavo : ctavos )
				cas.add( new CorrectorAnnotationImpl( ctavo.getCorrector(), ctavo.getText() ) );
		}
		// student annotations
		List<StudentAnnotation> studentAnnotations = new ArrayList<StudentAnnotation>();
		for( StudentTaskletAnnotationVO tavo : taskletVO.getStudentAnnotations() )
			studentAnnotations.add( new StudentAnnotationImpl( tavo.getText(), tavo.getDate(), tavo.isAcknowledged() ) );
		
		// manual corrections
		List<ManualCorrection> mcs = new LinkedList<ManualCorrection>();
		List<ManualCorrectionsVO> mcvos = taskletVO.getManualCorrections();
		if( mcvos != null && mcvos.size() > 0 ){
			for( ManualCorrectionsVO mcvo : mcvos )
				mcs.add( new ManualCorrectionImpl( mcvo.getCorrector(), mcvo.getPoints() ) );
		}
		
		TaskletCorrection correction =
			new TaskletCorrectionImpl( taskletVO.getAutoCorrectionPoints(), cas,
										taskletVO.getCorrectorLogin(), taskletVO.getCorrectorHistory(), studentAnnotations, mcs );			
			
		FileInputStream fis;
		try {
			if( !complexTaskHandlingFile.exists() )
				complexTaskHandlingFile.createNewFile();
			fis = new FileInputStream( complexTaskHandlingFile );
		}catch( IOException e ){
			throw new TaskModelPersistenceException( e );
		}
		ComplexTasklet tasklet =
			new ComplexTaskletImpl( this, complexTaskBuilder, taskletVO.getLogin(), taskDefVO.getId(), 
					TaskmodelUtil.getStatus( taskletVO.getStatus() ), taskletVO.getFlags(), correction, (TaskDef_Complex)getTaskDef( taskDefVO.getId() ), complexTaskHandlingDAO, fis );
		
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
		taskletVO.setStatus( Tasklet.Status.INITIALIZED.getValue() );
		taskletVO.setAutoCorrectionPoints( null );
		taskletVO.setFlags( new LinkedList<String>() );
		taskletVO.setStudentAnnotations( new LinkedList<StudentTaskletAnnotationVO>() );
		taskletVO.setCorrectorAnnotations( new LinkedList<CorrectorTaskletAnnotationVO>() );
		taskletVO.setManualCorrections( new LinkedList<ManualCorrectionsVO>() );
		
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
			// potential NPE, see lines below
			// but has no influence due to createTasklet()
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
		
		if( objectsDiffer( taskletVO.getStatus(), tasklet.getStatus().getValue() ) ){
			taskletVO.setStatus( tasklet.getStatus().getValue() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getCorrectorLogin(), tasklet.getTaskletCorrection().getCorrector() ) ){
			taskletVO.setCorrectorLogin( tasklet.getTaskletCorrection().getCorrector() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getAutoCorrectionPoints(), tasklet.getTaskletCorrection().getAutoCorrectionPoints() ) ){
			taskletVO.setAutoCorrectionPoints( tasklet.getTaskletCorrection().getAutoCorrectionPoints() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getCorrectorHistory(), tasklet.getTaskletCorrection().getCorrectorHistory() ) ){
			taskletVO.setCorrectorHistory( tasklet.getTaskletCorrection().getCorrectorHistory() );
			changed = true;
		}
		
		if( objectsDiffer( taskletVO.getFlags(), tasklet.getFlags() ) ){
			taskletVO.setFlags( tasklet.getFlags() );
			changed = true;
		}
		
		// student annotations
		if( taskletVO.getStudentAnnotations().size() != tasklet.getTaskletCorrection().getStudentAnnotations().size() ){
			taskletVO.setStudentAnnotations( copyStudentAnnotations( tasklet.getTaskletCorrection().getStudentAnnotations() ) );
			changed = true;
		}else{
			for( int i = 0; i < tasklet.getTaskletCorrection().getStudentAnnotations().size(); i++ ){
				StudentAnnotation a = tasklet.getTaskletCorrection().getStudentAnnotations().get( i );
				StudentTaskletAnnotationVO tavo = taskletVO.getStudentAnnotations().get( i );
				if( objectsDiffer( a.getText(), tavo.getText() ) || objectsDiffer( a.getDate(), tavo.getDate() ) || objectsDiffer( a.isAcknowledged(), tavo.isAcknowledged() ) ){
					taskletVO.setStudentAnnotations( copyStudentAnnotations( tasklet.getTaskletCorrection().getStudentAnnotations() ) );
					changed = true;
					break;
				}
			}		
		}
		
		// corrector annotations
		if( taskletVO.getCorrectorAnnotations().size() != tasklet.getTaskletCorrection().getCorrectorAnnotations().size() ){
			taskletVO.setCorrectorAnnotations( copyCorrectorAnnotations( tasklet.getTaskletCorrection().getCorrectorAnnotations() ) );
			changed = true;
		}else{
			for( int i = 0; i < tasklet.getTaskletCorrection().getCorrectorAnnotations().size(); i++ ){
				CorrectorAnnotation a = tasklet.getTaskletCorrection().getCorrectorAnnotations().get( i );
				CorrectorTaskletAnnotationVO tavo = taskletVO.getCorrectorAnnotations().get( i );
				if( objectsDiffer( a.getText(), tavo.getText() ) || objectsDiffer( a.getCorrector(), tavo.getCorrector() ) ){
					taskletVO.setCorrectorAnnotations( copyCorrectorAnnotations( tasklet.getTaskletCorrection().getCorrectorAnnotations() ) );
					changed = true;
					break;
				}
			}
		}
		
		// manual corrections
		if( taskletVO.getManualCorrections().size() != tasklet.getTaskletCorrection().getManualCorrections().size() ){
			taskletVO.setManualCorrections( copyManualCorrections(taskletVO, tasklet.getTaskletCorrection().getManualCorrections() ) );
			changed = true;
		}else{
			for( int i = 0; i < tasklet.getTaskletCorrection().getManualCorrections().size(); i++ ){
				ManualCorrection m = tasklet.getTaskletCorrection().getManualCorrections().get( i );
				ManualCorrectionsVO mcvo = taskletVO.getManualCorrections().get( i );
				if( objectsDiffer( m.getCorrector(), mcvo.getCorrector() ) || objectsDiffer( m.getCorrector(), m.getPoints() ) ){
					taskletVO.setManualCorrections( copyManualCorrections( taskletVO, tasklet.getTaskletCorrection().getManualCorrections() ) );
					changed = true;
					break;
				}
			}
		}
		
		
		if( tasklet instanceof ComplexTasklet ){
			
			// get the taskHandling xml file!
			File homeDir = new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.HOME + File.separatorChar + tasklet.getUserId() );
			String pathOfCTHfile = homeDir.getAbsolutePath() + File.separatorChar +  COMPLEX_TASKHANDLING_FILE_PREFIX + tasklet.getTaskId() + COMPLEX_TASKHANDLING_FILE_SUFFIX;
			File complexTaskHandlingFile = new File( pathOfCTHfile );
			
			ComplexTasklet ct = (ComplexTasklet)tasklet;
			try {
				File backup = new File( pathOfCTHfile + COMPLEX_TASKHANDLING_BACKUP_FILE_SUFFIX );
				backup.delete();
				complexTaskHandlingFile.renameTo( backup );
				complexTaskHandlingFile = new File( pathOfCTHfile );
				complexTaskHandlingDAO.save( ct.getComplexTaskHandlingRoot(), new FileOutputStream( complexTaskHandlingFile ) );
			} catch (FileNotFoundException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		
		if( changed )
			taskHandlingDao.saveTasklet( taskletVO );

	}
	
	private List<StudentTaskletAnnotationVO> copyStudentAnnotations( List<StudentAnnotation> annotations ){
		List<StudentTaskletAnnotationVO> ret = new LinkedList<StudentTaskletAnnotationVO>();
		for( StudentAnnotation a : annotations )
			ret.add( new StudentTaskletAnnotationVO( a.getText(), a.getDate(), a.isAcknowledged() ) );
		return ret;
	}
	
	private List<CorrectorTaskletAnnotationVO> copyCorrectorAnnotations( List<CorrectorAnnotation> annotations ){
		List<CorrectorTaskletAnnotationVO> ret = new LinkedList<CorrectorTaskletAnnotationVO>();
		for( CorrectorAnnotation a : annotations )
			ret.add( new CorrectorTaskletAnnotationVO( a.getCorrector(), a.getText() ) );
		return ret;
	}
	
	private List<ManualCorrectionsVO> copyManualCorrections( TaskletVO taskletVO, List<ManualCorrection> manualCorrections ){
		List<ManualCorrectionsVO> ret = new LinkedList<ManualCorrectionsVO>();
		for( ManualCorrection mc : manualCorrections ){
			ret.add( taskletVO.new ManualCorrectionsVO( mc.getCorrector(), mc.getPoints() ) );
		}
		return ret;
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
		ret.setUserAttribute( USER_ATTRIBUTE_SEMESTER, user.getPhoneNumber() );
		
		return ret;
		
	}
	
	

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#availableUserAttributeKeys()
	 */
	public List<UserAttribute> availableUserAttributes() {
		List<UserAttribute> ret = new LinkedList<UserAttribute>();
		ret.add( new UserAttributeImpl( USER_ATTRIBUTE_SEMESTER, "Semester" ) ); // TODO externalize String
		return ret;
	}
	
	public class UserAttributeImpl implements UserAttribute{
		
		private String key;
		private String name;
		/**
		 * @param key
		 * @param name
		 */
		public UserAttributeImpl(String key, String name) {
			super();
			this.key = key;
			this.name = name;
		}
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @return the name
		 */
		public String getName( Locale locale ) {
			return name;
		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#addTaskCategory(java.lang.String, java.lang.String)
	 */
	public TaskCategory addTaskCategory(String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskCategory(long)
	 */
	public void deleteTaskCategory(long id) throws MethodNotSupportedException {
		// TODO Auto-generated method stub
		throw new MethodNotSupportedException();
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
