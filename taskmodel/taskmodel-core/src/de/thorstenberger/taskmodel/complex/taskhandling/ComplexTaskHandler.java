///*
//
//Copyright (C) 2004 Thorsten Berger
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//*/
//package de.thorstenberger.taskmodel.complex.taskhandling;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.xml.bind.JAXBException;
//
//import manager.UebManagerServer;
//
//import util.RandomUtil;
//import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
//import de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelperImpl;
//import de.thorstenberger.uebman.task.beans.Student;
//import de.thorstenberger.uebman.task.beans.TaskHandler;
//import de.thorstenberger.uebman.xml.PersistenceException;
//import de.thorstenberger.uebman.xml.taskdef.beans.TaskDef_Complex;
//import de.thorstenberger.uebman.xml.taskdef.complex.jaxb.ComplexTaskDefType;
//import de.thorstenberger.uebman.xml.taskhandling.complex.ComplexTaskHandlingHandler;
//import de.thorstenberger.uebman.xml.taskhandling.jaxb.ObjectFactory;
//import de.thorstenberger.uebman.xml.taskhandling.jaxb.TaskHandlingDataType;
//
///**
// * @deprecated
// * Hauptklasse, die den Ablauf eines Complex-Task steuert.
// * 
// * @author Thorsten Berger
// *
// */
//public class ComplexTaskHandler extends TaskHandler {
//
//	// TODO evtl. JAXB entkoppeln, aber wir wollen's vorerst nicht übertreiben...
//	private TaskHandlingDataType.TaskType.ComplexType complexElem;
//	
//	private TaskDef_Complex complexTaskDef;
//	private ComplexTaskDefHelperImpl ctDefHandler;
//	private ComplexTaskHandlingHandler ctHandlingHandler;
//	private SubTaskFactory subtaskFactory;
//	
//	/**
//	 * 
//	 */
//	public ComplexTaskHandler( TaskHandlingDataType.TaskType taskElem, Student student ) {
//		super( taskElem, student );
//		this.complexElem = taskElem.getComplex();
//		complexTaskDef = (TaskDef_Complex) super.task;
//		ctDefHandler = complexTaskDef.getComplexTaskDataHandler();
//		ctHandlingHandler = new ComplexTaskHandlingHandler( complexElem );
//		// kapseln -> Performance
//		
//		updateStatus();
//	}
//	
//	public void updateStatus(){
//		synchronized( student ){
//			
//			if( getStatus().equals( INPROGRESS ) ){
//				
//				if( ctDefHandler.hasTimeRestriction() ){
//					long deadline = getActiveTry().getStartTime() + ctDefHandler.getTimeInMinutesWithKindnessExtensionTime() * 60 * 1000;
//					if( System.currentTimeMillis() > deadline ){
//						submit();
//						return;
//					}
//				}
//				
//				if( !complexTaskDef.isActive() )
//					submit();
//				
//			}				
//			    
//			
//		}
//	}
//	
//	/**
//	 * Kann der Student einen neuen Lösungsversuch starten?
//	 * @return
//	 */
//	public boolean canStartNewTry(){
//		synchronized (student) {
//			if( canContinueTry() )
//				return false;
//			
//			return ( ctHandlingHandler.getNumberOfTries() < ctDefHandler.getTries() ) &&
//																				complexTaskDef.isActive();
//		}
//	}
//
//	
//	/**
//	 * Neuen Versuch starten.
//	 * @param tryNo
//	 * @throws IllegalStateException
//	 */
//	public void startNewTry( int tryNo ) throws IllegalStateException{
//		synchronized( student ){
//			
//			// Student davon abhalten, versehentlich einen neuen Versuch zu starten
//			// falls noch einer in Bearbeitung
//			if( canContinueTry() ){
//				continueLastTry();
//				return;
//			}
//			
//			// Status-Checks
//			if( !complexTaskDef.isActive() )
//				throw new IllegalStateException( TaskHandlingConstants.NOT_ACTIVE );
//			// Anzahl bereits abgesendeter Versuche:
//			if( ctHandlingHandler.getNumberOfTries() >= ctDefHandler.getTries() )
//				throw new IllegalStateException( TaskHandlingConstants.TRIES_SPENT );
//			// Sicherheitsabfrage, dass auch wirklich ein neuer Versuch gestartet werden soll
//			if( tryNo <= ctHandlingHandler.getNumberOfTries() )
//				throw new IllegalStateException( TaskHandlingConstants.CANNOT_RESTART_SPENT_TRY );
//			
//			TaskHandlingDataType.TaskType.ComplexType.TryType newTry =
//					ctHandlingHandler.addTry( System.currentTimeMillis() );
//
//			// Ok, falls ein schon vorgefertigter Aufgaben-Master vorliegt, dann den verwenden,
//			// sonst neu erstellen
//			MasterFactory.Master master = MasterFactory.getInstance().getClonedMaster( task.getId() );
//			TaskHandlingDataType.TaskType.ComplexType.TryType.PageType[] newPages;
//			
//			if( master != null)
//				newPages =
//					master.getPages();
//			else
//				newPages = generateNewTry( getSubTaskFactory(), ctDefHandler );
//			
//			
//			for( int i=0; i<newPages.length; i++ )
//				newTry.getPage().add( newPages[i] );
//			
//			// ans Ende, sonst wird auch bei Exceptions, die beim Zusammenstellen der Aufgaben autreten,
//			// ein inkonsistenter Zustand gespeichert
//			setStatus( INPROGRESS );
//			
//			// und schließlich speichern
//			save();
//			
//			
//			// TODO TRY zurückgeben
//			
//		}
//	}
//	
//	/**
//	 * Kann die Bearbeitung fortgesetzt werden?
//	 * @return
//	 */
//	public boolean canContinueTry(){
//		synchronized(student){
//			if( getStatus().equals( INPROGRESS ) )
//				return true;
//			else
//				return false;
//		}
//	}
//	
//	public void continueLastTry( ) throws IllegalStateException{
//		synchronized(student){
//			if( !canContinueTry() )
//				throw new IllegalStateException( TaskHandlingConstants.CANNOT_CONTINUE_TIME_EXCEEDED );
//			
//			
//			// TODO TRY zurückgeben
//			// ok, mehr ist erstmal nicht zu tun
//			
//		}
//	}
//
//	/**
//	 * Überprüft, ob die entspr. Seite gespeichert werden kann.
//	 * @param pageNo
//	 * @param hashcode
//	 * @throws IllegalStateException
//	 */
//	public void canSavePage( int pageNo, long hashcode ) throws IllegalStateException{
//		synchronized(student){
//			
//			if( !canContinueTry() )
//				throw new IllegalStateException( TaskHandlingConstants.CANNOT_SAVE_TIME_EXCEEDED );
//			if( hashcode != getHash( pageNo ) )
//				throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
//			
//		}		
//	}
//	
//	/**
//	 * Bearbeitete Seite abspeichern.
//	 * @param pageNo
//	 * @param submitData
//	 */
//	public void savePage( int pageNo, SubmitData[] submitData, long hashcode ) throws IllegalStateException{
//		synchronized(student){
//			
//			if( !canContinueTry() )
//				throw new IllegalStateException( TaskHandlingConstants.CANNOT_SAVE_TIME_EXCEEDED );
//			if( hashcode != getHash( pageNo ) )
//				throw new IllegalStateException( TaskHandlingConstants.SUBMIT_DATA_CORRUPTED );
//			
//			SubTask[] subtasks = getSubTasks( pageNo );
//			
//			for( int i=0; i<subtasks.length; i++ )
//				subtasks[i].doSave( submitData[i] );
//			
//			
//			// gaaaaanz wichtig ;)
//			save();
//			
//		}
//	}
//	
//	/**
//	 * Lösungsversuch abgeben und, soweit möglich, bewerten lassen.
//	 * @throws IllegalStateException
//	 */
//	public void submit() throws IllegalStateException{
//		synchronized(student){
//			
//			// falls nicht in Bearbeitung, dann einfach abbrechen,
//			// d.h. wenn die Bearbeitung vorher schon automatisch wegen Zeitüberschreitung
//			// gestoppt (und korrigiert) wurde, wird dies dem Student mitgeteilt
//			if( !canContinueTry() )
//				throw new IllegalStateException( TaskHandlingConstants.TIME_EXCEEDED_AUTO_SUBMIT_MADE );
//			
//			// automatische Vor-Korrektur durchführen
//			
//			boolean allCorrected = true;
//			float points = 0;
//			
//			Page[] pages = getActiveTry().getPages();
//			
//			for( int i=0; i<pages.length; i++ ){
//				
//				SubTask[] subtasks = pages[i].getSubTasks();
//				
//				for( int j=0; j<subtasks.length; j++ ){
//			
//					subtasks[j].doAutoCorrection();
//					if( !subtasks[j].isCorrected() )
//						allCorrected = false;
//					else
//						points += subtasks[j].getPoints();
//					
//				}
//			}
//			
//			if( allCorrected ){
//				setPoints( points );
//				setStatus( CORRECTED );
//			}else
//				setStatus( SOLVED );
//			
//			
//			save();
//			
//		}
//	}
//	
//	public void doManualCorrection( SubTask actualSubtask, CorrectionSubmitData csd ) throws IllegalStateException{
//	    synchronized(student){
//	        
//	        if( canContinueTry() )
//	            throw new IllegalStateException( TaskHandlingConstants.CANNOT_CORRECT_TASK_IN_PROGRESS );
//	        if( getStatus().equals( NOTSOLVED ) )
//	            throw new IllegalStateException( TaskHandlingConstants.CANNOT_CORRECT_TASK_NOT_SOLVED );
//	        
//	        actualSubtask.doManualCorrection( csd );
//
//	        
//			boolean allCorrected = true;
//			float points = 0;
//			
//			Page[] pages = getSolutionOfLatestTry().getPages();
//			
//			for( int i=0; i<pages.length; i++ ){
//				
//				SubTask[] subtasks = pages[i].getSubTasks();
//				
//				for( int j=0; j<subtasks.length; j++ ){
//			
//					if( !subtasks[j].isCorrected() )
//						allCorrected = false;
//					else
//						points += subtasks[j].getPoints();
//					
//				}
//			}
//			
//			if( allCorrected ){
//				setPoints( points );
//				setStatus( CORRECTED );
//			}
//			
//			
//	        save();
//	        
//	    }
//	}
//	
//	public ComplexTaskDefHelperImpl getCtDefHandler() {
//		return ctDefHandler;
//	}
//	
//	public ComplexTaskHandlingHandler getCtHandlingHandler() {
//		return ctHandlingHandler;
//	}
//	
//	/**
//	 * @deprecated da JAXB-Klassen gekapselt werden sollten -> demnächst private
//	 * @return
//	 */
//	public TaskHandlingDataType.TaskType.ComplexType.TryType getCurrentlyActiveTry() throws IllegalStateException{
//		synchronized(student){
//			
//			if( !getStatus().equals( INPROGRESS ) )
//				throw new IllegalStateException( TaskHandlingConstants.NOT_IN_PROGRESS );
//			
//			return ctHandlingHandler.getRecentTry();
//			
//		}
//	}
//	
//	/**
//	 * Returniert den zur Zeit aktiven Versuch.
//	 * @return
//	 * @throws IllegalStateException falls gerade kein Versuch aktiv
//	 */
//	public Try getActiveTry() throws IllegalStateException{
//		synchronized(student){
//			
//			if( !getStatus().equals( INPROGRESS ) )
//				throw new IllegalStateException( TaskHandlingConstants.NOT_IN_PROGRESS );
//			
//			// TODO dirty hack
//			// entkoppeln von JAXB
//			return new Try( ctHandlingHandler.getRecentTry(), ctHandlingHandler.getNumberOfTries(), getSubTaskFactory() );
//			
//		}
//	}
//	
//	/**
//	 * Gibt den letzten (nicht unbedingt aktiven) Versuch zur Korrektur und Einsichtnahme zurück.
//	 * @return
//	 */
//	public Try getSolutionOfLatestTry() throws IllegalStateException{
//		synchronized(student){
//			
//			// TODO
////			if( !getStatus().equals( CORRECTED ) && !getStatus().equals( SOLVED ) )
////				throw new IllegalStateException( TaskHandlingConstants.SHOW_CORRECTION_NOT_POSSIBLE );
//		    
//		    TaskHandlingDataType.TaskType.ComplexType.TryType recentTry = ctHandlingHandler.getRecentTry();
//		    
//		    if( recentTry == null )
//		        return null;
//		    
//			return new Try( recentTry, ctHandlingHandler.getNumberOfTries(), getSubTaskFactory() );
//			
//		}
//	}
//	
//	/**
//	 * Returniert alle Aufgaben, die sich auf der angegebenen Seite befinden.
//	 * @param page
//	 * @return
//	 */
//	public SubTask[] getSubTasks( int page ){
//		synchronized(student){
//			
//			if( !getStatus().equals( INPROGRESS ) )
//				throw new IllegalStateException( TaskHandlingConstants.NOT_IN_PROGRESS );
//			
//			SubTaskFactory subTaskFactory = new SubTaskFactory( ctDefHandler );
//			SubTask[] ret = subTaskFactory.getSubTasks( getCurrentlyActiveTry(), page );
//			
//			if( ret == null )
//				throw new IllegalStateException( TaskHandlingConstants.PAGE_UNKNOWN );
//			
//			return ret;
//			
//		}
//	}
//	
//	private SubTaskFactory getSubTaskFactory(){
//		if( subtaskFactory == null )
//			subtaskFactory = new SubTaskFactory( ctDefHandler );
//		return subtaskFactory;
//	}
//		
//	/**
//	 * Liefert hash über die gesamte Seite.
//	 * Später Auslagerung in "Page".
//	 * @param page
//	 * @return
//	 */
//	public long getHash( int page ){
//		synchronized(student ){
//			
//			long hash = 0;
//			SubTask[] subtasks = getSubTasks( page );
//			for( int i=0; i<subtasks.length; i++ )
//				hash += subtasks[i].getHash();
//			
//			return hash;
//		}
//	}
//	
//	public static TaskHandlingDataType.TaskType.ComplexType.TryType.PageType[] generateNewTry( SubTaskFactory subTaskFactory,
//			ComplexTaskDefHelperImpl ctDefHandler ){
//		
//		List ret = new ArrayList();
//		ObjectFactory objectFactory = new ObjectFactory();
//		// mit Aufgaben füllen
//		
////		SubTaskFactory subTaskFactory = getSubTaskFactory();
//		
//		List cats = ctDefHandler.getComplexTask().getCategory();
//		Iterator catIt = cats.iterator();
//		
//		int globalTasksPerPage = ctDefHandler.getComplexTask().getConfig().getTasksPerPage();
//		int pageNo = 1;
//		int taskEnum = 1;
//		
//		while( catIt.hasNext() ){
//			
//			ComplexTaskDefType.CategoryType cat =
//					(ComplexTaskDefType.CategoryType) catIt.next();
//			
//			// alle Aufgaben der Kategorie zusammenstellen,
//			// der wohl umfangreichste Teil hier...
//			SubTask[] subtasks = subTaskFactory.constructSubTasks( cat );
//			
//			// Anzahl Seiten, die der Kategorie zugeordnet werden
//			int tasksPerPageForCategory;
//			if( cat.isSetTasksPerPage() )
//				tasksPerPageForCategory = cat.getTasksPerPage();
//			else
//				tasksPerPageForCategory = globalTasksPerPage;
//			
//			int pages = (int) Math.ceil( (double)subtasks.length / (double)tasksPerPageForCategory );
//			TaskHandlingDataType.TaskType.ComplexType.TryType.PageType page;
//			int j = 0;
//			
//			// Einfüge-Reihenfolge
//			int[] insertOrder;
//			if( cat.isMixAllSubTasks() )	// alle Aufgaben zufällig mischen
//				insertOrder = RandomUtil.getPermutation( subtasks.length );
//			else							// sonst natürliche Reihenfolge beibehalten
//				insertOrder = SubTaskFactory.getStandardOrder( subtasks.length );
//			
//			
//			for( int i=0; i<pages; i++ ){
//				
////				page = ctHandlingHandler.addPage( newTry, pageNo, cat.getId() );
//				
//				try {
//					page = objectFactory.createTaskHandlingDataTypeTaskTypeComplexTypeTryTypePageType();
//				} catch (JAXBException e) {
//					throw new PersistenceException( e );
//				}
//				
//				page.setNo( pageNo );
//				page.setCategoryRefID( cat.getId() );
//				
//				for( int k=0; k<tasksPerPageForCategory; k++ ){
//					subtasks[ insertOrder[ j ] ].setVirtualNum( "" + (taskEnum++) );
//					subtasks[ insertOrder[j++] ].addToPage( page );
//					if( j >= subtasks.length )
//						break;
//				}
//				pageNo++;
//				
//				ret.add( page );
//			}
//			
//		}
//		
//		TaskHandlingDataType.TaskType.ComplexType.TryType.PageType[] pages = new TaskHandlingDataType.TaskType.ComplexType.TryType.PageType[ ret.size() ];
//		for( int i=0; i<pages.length; i++ )
//			pages[ i ] = (TaskHandlingDataType.TaskType.ComplexType.TryType.PageType) ret.get( i );
//		
//		return pages;
//		
//	}
//
//    public TaskDef_Complex getComplexTaskDef() {
//        return complexTaskDef;
//    }
//}
