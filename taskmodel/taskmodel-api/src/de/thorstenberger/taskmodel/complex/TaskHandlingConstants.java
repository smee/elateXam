/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex;

import java.util.ResourceBundle;

/**
 * @author Thorsten Berger
 *
 */
public class TaskHandlingConstants {

	public static final String NOT_ACTIVE = "task.not_active";
	
	public static final String TASK_NOT_CORRECTED = "task.not_corrected";
	
	/**
	 * Anzahl maximaler Versuche verbraucht.
	 */
	public static final String TRIES_SPENT = "task.complex.tries_spent";
	
	/**
	 * Ein bereits abgelaufener Versuch kann nicht nochmal gestartet werden.
	 */
	public static final String CANNOT_RESTART_SPENT_TRY = "task.complex.cannot_restart_spent_try";
	
	public static final String NOT_IN_PROGRESS = "task.complex.not_in_progress";
	
	public static final String PAGE_UNKNOWN = "task.complex.page_unknown";
	
	public static final String CANNOT_CONTINUE_TIME_EXCEEDED = "task.complex.cannot_continue_time_exceeded";
	
	public static final String CANNOT_SAVE_TIME_EXCEEDED = "task.complex.cannot_save_time_exceeded";
	
	public static final String SUBMIT_DATA_CORRUPTED = "task.complex.submit_data_corrupted";
	
	public static final String SUBTASK_NOT_CORRECTED = "task.complex.subtask_not_corrected";
	
	public static final String TIME_EXCEEDED_AUTO_SUBMIT_MADE = "task.complex.time_exceeded_auto_submit_made";
	
	public static final String SHOW_CORRECTION_NOT_POSSIBLE = "task.complex.show_correction_not_possible";
	
	public static final String NO_UNCORRECTED_AND_UNASSIGNED_SOLUTIONS_AVAILABLE = "task.no_uncorrected_and_unassigned_solutions_available";
	
	public static final String CANNOT_CORRECT_TASK_IN_PROGRESS = "task.complex.cannot_correct_task_in_progress";
	
	public static final String CANNOT_CORRECT_TASK_NOT_SOLVED = "task.cannot_correct_task_not_solved";
	
	/**
	 * @deprecated as this should be in the presentation layer
	 * @param key
	 * @return
	 */
	public static String getText( String key ){
		
		ResourceBundle bundle = ResourceBundle.getBundle( "de.thorstenberger.taskmodel.view.resources.TaskHandling" );
		return bundle.getString( key );
		
//		if( NOT_ACTIVE.endsWith(key) )
//			return "Diese Aufgaben sind zur Zeit nicht aktiv.";
//		if( TRIES_SPENT.equals(key) )
//			return "Sie haben die Anzahl der maximalen Versuche bereits verbraucht.";
//		if( CANNOT_RESTART_SPENT_TRY.equals(key) )
//			return "Sie können keinen bereits verbrauchten Versuch erneut starten.";
//		if( NOT_IN_PROGRESS.equals(key) )
//			return "Es befindet sich zur Zeit kein Versuch in Bearbeitung.";
//		if( PAGE_UNKNOWN.equals(key) )
//			return "Die angeforderte Seite existiert nicht.";
//		if( CANNOT_CONTINUE_TIME_EXCEEDED.equals(key) )
//			return "Die Bearbeitung kann nicht fortgesetzt werden, da die Bearbeitungszeit bereits abgelaufen ist." +
//			"Die bis zu diesem Zeitpunkt gespeicherten Bearbeitungen " +
//			"wurden automatisch zum Bearbeitungsende abgegeben";
//		if( CANNOT_SAVE_TIME_EXCEEDED.equals(key) )
//			return "Die Seite kann nicht gespeichert werden, da die Bearbeitungszeit bereits abgelaufen ist. " +
//					"Die bis zu diesem Zeitpunkt gespeicherten Bearbeitungen " +
//					"wurden automatisch zum Bearbeitungsende abgegeben";
//		if( SUBMIT_DATA_CORRUPTED.equals(key) )
//			return "Die übermittelten Daten sind inkonsistent.";
//		if( SUBTASK_NOT_CORRECTED.equals(key) )
//			return "Aufgabe noch nicht korrigiert.";
//		if( TIME_EXCEEDED_AUTO_SUBMIT_MADE.equals(key) )
//			return "Die maximale Bearbeitungszeit ist abgelaufen. Die bis zu diesem Zeitpunkt gespeicherten Bearbeitungen " +
//					"wurden automatisch zum Bearbeitungsende abgegeben";
//		if( TASK_NOT_CORRECTED.equals(key) )
//			return "Die Aufgabe wurde noch nicht korrigiert.";
//		if( SHOW_CORRECTION_NOT_POSSIBLE.equals(key) )
//			return "Die Anzeige der Korrekturinformationen ist zur Zeit nicht möglich.";
//		if( NO_UNCORRECTED_AND_UNASSIGNED_SOLUTIONS_AVAILABLE.equals(key) )
//			return "Keine Lösungen verfügbar.";
//		if( CANNOT_CORRECT_TASK_IN_PROGRESS.equals(key) )
//		    return "Lösung kann nicht korrigiert werden, da sie sich noch in Bearbeitung befindet.";
//		if( CANNOT_CORRECT_TASK_NOT_SOLVED.equals(key) )
//		    return "Eine nicht gelöste Aufgabe kann nicht korrigiert werden";
//		
//		return null;
	}
	
	
}
