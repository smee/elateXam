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
package de.thorstenberger.taskmodel.complex;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper;
import de.thorstenberger.taskmodel.complex.taskhandling.ComplexTaskHandlingHelper;
import de.thorstenberger.taskmodel.complex.taskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.taskhandling.Page;
import de.thorstenberger.taskmodel.complex.taskhandling.SubTask;
import de.thorstenberger.taskmodel.complex.taskhandling.SubTaskFactory;
import de.thorstenberger.taskmodel.complex.taskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.taskhandling.Try;
import de.thorstenberger.taskmodel.complex.taskhandling.impl.ComplexTaskHandlingHelperImpl;
import de.thorstenberger.taskmodel.impl.AbstractTasklet;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskletImpl extends AbstractTasklet implements
		ComplexTasklet {

	
	private TaskDef_Complex complexTaskDef;
	private ComplexTaskHandlingHelper ctHandlingHelper;
	private SubTaskFactory subtaskFactory;
	
	/**
	 * @param taskFactory
	 * @param userId
	 * @param taskId
	 * @param status
	 * @param taskletCorrection
	 */
	public ComplexTaskletImpl(TaskFactory taskFactory, String userId,
			long taskId, String status, TaskletCorrection taskletCorrection, TaskDef_Complex complexTaskDef, File xmlTaskHandlingFile ) {
		
		super(taskFactory, userId, taskId, status, taskletCorrection);
		
		this.complexTaskDef = complexTaskDef;
		ctHandlingHelper = new ComplexTaskHandlingHelperImpl( xmlTaskHandlingFile );
		
		update();
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#updateStatus()
	 */
	public synchronized void update() {
			
		if( getStatus().equals( INPROGRESS ) ){
			
			if( complexTaskDef.getComplexTaskDefHelper().hasTimeRestriction() ){
				long deadline = getActiveTry().getStartTime() + complexTaskDef.getComplexTaskDefHelper().getTimeInMinutesWithKindnessExtensionTime() * 60 * 1000;
				if( System.currentTimeMillis() > deadline ){
					submit();
					return;
				}
			}
			
			if( !complexTaskDef.isActive() )
				submit();
			
		}				
			    
			

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#canStartNewTry()
	 */
	public synchronized boolean canStartNewTry() {
		if( canContinueTry() )
			return false;
		
		return ( ctHandlingHelper.getNumberOfTries() < complexTaskDef.getComplexTaskDefHelper().getTries() ) &&
																			complexTaskDef.isActive();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#startNewTry(int)
	 */
	public synchronized void startNewTry(int tryNo) throws IllegalStateException {
		// Student davon abhalten, versehentlich einen neuen Versuch zu starten
		// falls noch einer in Bearbeitung
		if( canContinueTry() ){
			continueLastTry();
			return;
		}
		
		// Status-Checks
		if( !complexTaskDef.isActive() )
			throw new IllegalStateException( TaskHandlingConstants.NOT_ACTIVE );
		// Anzahl bereits abgesendeter Versuche:
		if( ctHandlingHelper.getNumberOfTries() >= complexTaskDef.getComplexTaskDefHelper().getTries() )
			throw new IllegalStateException( TaskHandlingConstants.TRIES_SPENT );
		// Sicherheitsabfrage, dass auch wirklich ein neuer Versuch gestartet werden soll
		if( tryNo <= ctHandlingHelper.getNumberOfTries() )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_RESTART_SPENT_TRY );
		
		ComplexTaskHandlingType.TryType newTry =
				ctHandlingHelper.addTry( System.currentTimeMillis() );

		// Ok, falls ein schon vorgefertigter Aufgaben-Master vorliegt, dann den verwenden,
		// sonst neu erstellen
		// not implemented any more!
//		MasterFactory.Master master = MasterFactory.getInstance().getClonedMaster( task.getId() );
		ComplexTaskHandlingType.TryType.PageType[] newPages;
		
//		if( master != null)
//			newPages =
//				master.getPages();
//		else
			newPages = generateNewTry( getSubTaskFactory(), complexTaskDef.getComplexTaskDefHelper() );
		
		
		for( int i=0; i<newPages.length; i++ )
			newTry.getPage().add( newPages[i] );
		
		// ans Ende, sonst wird auch bei Exceptions, die beim Zusammenstellen der Aufgaben autreten,
		// ein inkonsistenter Zustand gespeichert
		setStatus( INPROGRESS );
		
		// und schließlich speichern
		try {
			save();
		} catch (TaskApiException e) {
			// TODO change interface to be able to throw TaskApiExceptions from this method
			throw new TaskModelPersistenceException( e );
		}
		
		
		// TODO TRY zurückgeben

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#canContinueTry()
	 */
	public synchronized boolean canContinueTry() {
		if( getStatus().equals( INPROGRESS ) )
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#continueLastTry()
	 */
	public synchronized void continueLastTry() throws IllegalStateException {
		if( !canContinueTry() )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_CONTINUE_TIME_EXCEEDED );
		
		
		// TODO TRY zurückgeben
		// ok, mehr ist erstmal nicht zu tun
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#canSavePage(int, long)
	 */
	public synchronized void canSavePage(int pageNo, long hashcode)
			throws IllegalStateException {
		
		if( !canContinueTry() )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_SAVE_TIME_EXCEEDED );
		if( hashcode != getHash( pageNo ) )
			throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#savePage(int, de.thorstenberger.taskmodel.complex.taskhandling.SubmitData[], long)
	 */
	public synchronized void savePage(int pageNo, SubmitData[] submitData, long hashcode)
			throws IllegalStateException {
		
		if( !canContinueTry() )
			throw new IllegalStateException( TaskHandlingConstants.CANNOT_SAVE_TIME_EXCEEDED );
		if( hashcode != getHash( pageNo ) )
			throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
		
		SubTask[] subtasks = getSubTasks( pageNo );
		
		for( int i=0; i<subtasks.length; i++ )
			subtasks[i].doSave( submitData[i] );
		
		
		// gaaaaanz wichtig ;)
		try {
			save();
		} catch (TaskApiException e) {
			// TODO
			throw new TaskModelPersistenceException( e );
		}

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#submit()
	 */
	public synchronized void submit() throws IllegalStateException {
		// falls nicht in Bearbeitung, dann einfach abbrechen,
		// d.h. wenn die Bearbeitung vorher schon automatisch wegen Zeitüberschreitung
		// gestoppt (und korrigiert) wurde, wird dies dem Student mitgeteilt
		if( !canContinueTry() )
			throw new IllegalStateException( TaskHandlingConstants.TIME_EXCEEDED_AUTO_SUBMIT_MADE );
		
		// automatische Vor-Korrektur durchführen
		
		boolean allCorrected = true;
		float points = 0;
		
		Page[] pages = getActiveTry().getPages();
		
		for( int i=0; i<pages.length; i++ ){
			
			SubTask[] subtasks = pages[i].getSubTasks();
			
			for( int j=0; j<subtasks.length; j++ ){
		
				subtasks[j].doAutoCorrection();
				if( !subtasks[j].isCorrected() )
					allCorrected = false;
				else
					points += subtasks[j].getPoints();
				
			}
		}
		
		if( allCorrected ){
			getTaskletCorrection().setPoints( points );
			setStatus( CORRECTED );
		}else
			setStatus( SOLVED );
		
		
		try {
			save();
		} catch (TaskApiException e) {
			throw new TaskModelPersistenceException( e );
		}

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#doManualCorrection(de.thorstenberger.taskmodel.complex.taskhandling.SubTask, de.thorstenberger.taskmodel.complex.taskhandling.CorrectionSubmitData)
	 */
	public synchronized void doManualCorrection(SubTask actualSubtask,
			CorrectionSubmitData csd) throws IllegalStateException {

		if( canContinueTry() )
            throw new IllegalStateException( TaskHandlingConstants.CANNOT_CORRECT_TASK_IN_PROGRESS );
        if( getStatus().equals( INITIALIZED ) )
            throw new IllegalStateException( TaskHandlingConstants.CANNOT_CORRECT_TASK_NOT_SOLVED );
        
        actualSubtask.doManualCorrection( csd );

        
		boolean allCorrected = true;
		float points = 0;
		
		Page[] pages = getSolutionOfLatestTry().getPages();
		
		for( int i=0; i<pages.length; i++ ){
			
			SubTask[] subtasks = pages[i].getSubTasks();
			
			for( int j=0; j<subtasks.length; j++ ){
		
				if( !subtasks[j].isCorrected() )
					allCorrected = false;
				else
					points += subtasks[j].getPoints();
				
			}
		}
		
		if( allCorrected ){
			getTaskletCorrection().setPoints( points );
//			setPoints( points );
			setStatus( CORRECTED );
		}
		
		
        try {
			save();
		} catch (TaskApiException e) {
			throw new TaskModelPersistenceException( e );
		}


	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#save()
	 */
	protected synchronized void save() throws TaskApiException{
		super.save();
		
		// save the JAXB-DOM
		ctHandlingHelper.marshallXML();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getComplexTaskHandlingHelper()
	 */
	public ComplexTaskHandlingHelper getComplexTaskHandlingHelper() {
		return ctHandlingHelper;
	}
	
	

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getComplexTaskDefHelper()
	 */
	public ComplexTaskDefHelper getComplexTaskDefHelper() {
		return complexTaskDef.getComplexTaskDefHelper();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getActiveTry()
	 */
	public synchronized Try getActiveTry() throws IllegalStateException {
		
		if( !getStatus().equals( INPROGRESS ) )
			throw new IllegalStateException( TaskHandlingConstants.NOT_IN_PROGRESS );
		
		// TODO dirty hack
		// entkoppeln von JAXB
		return new Try( ctHandlingHelper.getRecentTry(), ctHandlingHelper.getNumberOfTries(), getSubTaskFactory() );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getHash(int)
	 */
	public synchronized long getHash(int page) {
		long hash = 0;
		SubTask[] subtasks = getSubTasks( page );
		for( int i=0; i<subtasks.length; i++ )
			hash += subtasks[i].getHash();
		
		return hash;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getSolutionOfLatestTry()
	 */
	public synchronized Try getSolutionOfLatestTry() throws IllegalStateException {
		// TODO
//		if( !getStatus().equals( CORRECTED ) && !getStatus().equals( SOLVED ) )
//			throw new IllegalStateException( TaskHandlingConstants.SHOW_CORRECTION_NOT_POSSIBLE );
	    
	    ComplexTaskHandlingType.TryType recentTry = ctHandlingHelper.getRecentTry();
	    
	    if( recentTry == null )
	        return null;
	    
		return new Try( recentTry, ctHandlingHelper.getNumberOfTries(), getSubTaskFactory() );

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTasklet#getSubTasks(int)
	 */
	public synchronized SubTask[] getSubTasks(int page) {
		
		if( !getStatus().equals( INPROGRESS ) )
			throw new IllegalStateException( TaskHandlingConstants.NOT_IN_PROGRESS );
		
		SubTask[] ret = getSubTaskFactory().getSubTasks( ctHandlingHelper.getRecentTry(), page );
		
		if( ret == null )
			throw new IllegalStateException( TaskHandlingConstants.PAGE_UNKNOWN );
		
		return ret;
		
	}
	

	
	public static ComplexTaskHandlingType.TryType.PageType[] generateNewTry( SubTaskFactory subTaskFactory,
			ComplexTaskDefHelper ctDefHandler ){
		
		List ret = new ArrayList();
		ObjectFactory objectFactory = new ObjectFactory();
		// mit Aufgaben füllen
		
		List cats = ctDefHandler.getComplexTask().getCategory();
		Iterator catIt = cats.iterator();
		
		int globalTasksPerPage = ctDefHandler.getComplexTask().getConfig().getTasksPerPage();
		int pageNo = 1;
		int taskEnum = 1;
		
		while( catIt.hasNext() ){
			
			ComplexTaskDefType.CategoryType cat =
					(ComplexTaskDefType.CategoryType) catIt.next();
			
			// alle Aufgaben der Kategorie zusammenstellen,
			// der wohl umfangreichste Teil hier...
			SubTask[] subtasks = subTaskFactory.constructSubTasks( cat );
			
			// Anzahl Seiten, die der Kategorie zugeordnet werden
			int tasksPerPageForCategory;
			if( cat.isSetTasksPerPage() )
				tasksPerPageForCategory = cat.getTasksPerPage();
			else
				tasksPerPageForCategory = globalTasksPerPage;
			
			int pages = (int) Math.ceil( (double)subtasks.length / (double)tasksPerPageForCategory );
			ComplexTaskHandlingType.TryType.PageType page;
			int j = 0;
			
			// Einfüge-Reihenfolge
			int[] insertOrder;
			if( cat.isMixAllSubTasks() )	// alle Aufgaben zufällig mischen
				insertOrder = RandomUtil.getPermutation( subtasks.length );
			else							// sonst natürliche Reihenfolge beibehalten
				insertOrder = SubTaskFactory.getStandardOrder( subtasks.length );
			
			
			for( int i=0; i<pages; i++ ){
				
//				page = ctHandlingHandler.addPage( newTry, pageNo, cat.getId() );
				
				try {
					page = objectFactory.createComplexTaskHandlingTypeTryTypePageType();
				} catch (JAXBException e) {
					throw new TaskModelPersistenceException( e );
				}
				
				page.setNo( pageNo );
				page.setCategoryRefID( cat.getId() );
				
				for( int k=0; k<tasksPerPageForCategory; k++ ){
					subtasks[ insertOrder[ j ] ].setVirtualNum( "" + (taskEnum++) );
					subtasks[ insertOrder[j++] ].addToPage( page );
					if( j >= subtasks.length )
						break;
				}
				pageNo++;
				
				ret.add( page );
			}
			
		}
		
		ComplexTaskHandlingType.TryType.PageType[] pages = new ComplexTaskHandlingType.TryType.PageType[ ret.size() ];
		for( int i=0; i<pages.length; i++ )
			pages[ i ] = (ComplexTaskHandlingType.TryType.PageType) ret.get( i );
		
		return pages;
		
	}

	
	private SubTaskFactory getSubTaskFactory(){
		if( subtaskFactory == null )
			subtaskFactory = new SubTaskFactory( complexTaskDef.getComplexTaskDefHelper() );
		return subtaskFactory;
	}


}
