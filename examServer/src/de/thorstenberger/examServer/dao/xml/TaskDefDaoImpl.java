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
package de.thorstenberger.examServer.dao.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.xml.jaxb.ObjectFactory;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefs;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefsType.TaskDefType;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefsType.TaskDefType.ComplexTaskDefType;
import de.thorstenberger.examServer.model.TaskDefVO;
import de.thorstenberger.examServer.service.ExamServerManager;

/**
 *
 * TODO add mutator methods and synchronize
 *
 * @author Thorsten Berger
 *
 */
public class TaskDefDaoImpl implements TaskDefDao {

	private ExamServerManager examServerManager;
	private JAXBContext jc;
	private TaskDefs taskDefs;
	private File taskDefsFile;
	private AtomicLong crntId;

	private Log log = LogFactory.getLog( TaskDefDaoImpl.class );

	/**
	 *
	 */
	public TaskDefDaoImpl( ExamServerManager examServerManager ) {

		this.examServerManager = examServerManager;

		try { // JAXBException

			jc = JAXBContext.newInstance( "de.thorstenberger.examServer.dao.xml.jaxb" );

			taskDefsFile = new File( examServerManager.getRepositoryFile().getAbsolutePath() + File.separatorChar + ExamServerManager.SYSTEM + File.separatorChar + "taskdefs.xml" );

			if( !taskDefsFile.exists() ){
				ObjectFactory oF = new ObjectFactory();
				taskDefs = oF.createTaskDefs();
				this.crntId=new AtomicLong(0);
				save();
				return;
			}


			// wenn vorhanden, dann auslesen
			Unmarshaller unmarshaller;
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setValidating( true );
			BufferedInputStream bis = new BufferedInputStream( new FileInputStream( taskDefsFile ) );
			taskDefs = (TaskDefs) unmarshaller.unmarshal( bis );
			this.crntId=new AtomicLong(findMostRecentId(taskDefs));


		} catch (JAXBException e) {
			throw new RuntimeException( e );
		}catch (IOException e1){
			throw new RuntimeException( e1 );
		}


	}

	private long findMostRecentId( TaskDefs tds ) {
		long max = -1;
		Iterator it = tds.getTaskDef().iterator();
		while( it.hasNext() ){
			TaskDefType taskDef = (TaskDefType)it.next();
			max=Math.max( max, taskDef.getId() );
		}
		return max;
	}

	private void save(){
		try {

			Marshaller marshaller = jc.createMarshaller();
			Validator validator = jc.createValidator();
			validator.validate( taskDefs );
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true) );
			BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( this.taskDefsFile ) );
			marshaller.marshal( taskDefs, bos );

			bos.close();

		} catch (JAXBException e) {
			throw new RuntimeException( e );
		} catch( IOException e1 ){
			throw new RuntimeException( e1 );
		}

	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.tasks.TaskDefDao#getTaskDefs()
	 */
	public List<TaskDefVO> getTaskDefs() {

		List<TaskDefVO> ret = new ArrayList<TaskDefVO>();

		Iterator it = taskDefs.getTaskDef().iterator();
		while( it.hasNext() ){
			TaskDefType taskDef = (TaskDefType)it.next();

			TaskDefVO taskDefVO = new TaskDefVO();
			taskDefVO.setType( "complex" );
			taskDefVO.setTitle( taskDef.getTitle() );
			taskDefVO.setShortDescription( taskDef.getShortDescription() );
			taskDefVO.setStopped( taskDef.isStopped() );
			taskDefVO.setId( taskDef.getId() );
			taskDefVO.setDeadline( taskDef.getDeadline() == 0 ? null : taskDef.getDeadline() );
			taskDefVO.setVisible( taskDef.isVisible() );
			if( taskDef.isSetFollowingTaskId() )
				taskDefVO.setFollowingTaskId( taskDef.getFollowingTaskId() );

			taskDefVO.setShowSolutionToStudents( taskDef.getComplexTaskDef().isShowSolutionToStudents() );
			taskDefVO.setComplexTaskFile( taskDef.getComplexTaskDef().getComplexTaskFile() );

			ret.add( taskDefVO );

		}
		return ret;

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.tasks.TaskDefDao#getTaskDef(long)
	 */
	public TaskDefVO getTaskDef( long id ) {

		Iterator it = taskDefs.getTaskDef().iterator();
		while( it.hasNext() ){
			TaskDefType taskDef = (TaskDefType)it.next();

			if( taskDef.getId() == id ){

				TaskDefVO taskDefVO = new TaskDefVO();
				taskDefVO.setType( "complex" );
				taskDefVO.setTitle( taskDef.getTitle() );
				taskDefVO.setShortDescription( taskDef.getShortDescription() );
				taskDefVO.setStopped( taskDef.isStopped() );
				taskDefVO.setId( taskDef.getId() );
				taskDefVO.setDeadline( taskDef.getDeadline() == 0 ? null : taskDef.getDeadline() );
				taskDefVO.setVisible( taskDef.isVisible() );
				if( taskDef.isSetFollowingTaskId() )
					taskDefVO.setFollowingTaskId( taskDef.getFollowingTaskId() );

				taskDefVO.setShowSolutionToStudents( taskDef.getComplexTaskDef().isShowSolutionToStudents() );
				taskDefVO.setComplexTaskFile( taskDef.getComplexTaskDef().getComplexTaskFile() );

				return taskDefVO;

			}


		}

		return null;

	}

	public TaskDefVO storeTaskDef( TaskDefVO td ) {
		if(td.getId() >=0 ) {
			//update
			Iterator it = taskDefs.getTaskDef().iterator();
			while( it.hasNext() ){
				TaskDefType taskDef = (TaskDefType)it.next();
				if(taskDef.getId()==td.getId()) {
					try{
						BeanUtils.copyProperties( taskDef, td );
					}catch( Exception e ){
						log.error( "Exception on BeanUtils.copyProperties().", e );
					}
					break;
				}
			}
		}else {
			//set new id
			td.setId( this.crntId.incrementAndGet() );
			try{
				ObjectFactory oF = new ObjectFactory();

				TaskDefType tdt = oF.createTaskDefsTypeTaskDefType();
				BeanUtils.copyProperties( tdt, td );

				ComplexTaskDefType ctdt = oF.createTaskDefsTypeTaskDefTypeComplexTaskDefType();
				ctdt.setComplexTaskFile( td.getComplexTaskFile() );
				ctdt.setShowSolutionToStudents( td.isShowSolutionToStudents() );
				tdt.setComplexTaskDef( ctdt );

				taskDefs.getTaskDef().add( tdt );
			}catch( JAXBException e ){
				throw new RuntimeException(e);
			}catch( IllegalAccessException e ){
				throw new RuntimeException(e);
			}catch( InvocationTargetException e ){
				throw new RuntimeException(e);
			}
		}
		save();
		return td;
	}
}
