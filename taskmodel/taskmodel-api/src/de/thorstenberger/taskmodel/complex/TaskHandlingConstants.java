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

	public static final String SUBTASK_NOT_CORRECTED_BY_CORRECTOR = "task.complex.subtask_not_corrected_by_corrector";

	public static final String SUBTASK_NOT_AUTO_CORRECTED = "task.complex.subtask_not_auto_corrected";

	public static final String SUBTASK_AUTO_CORRECTED = "task.complex.subtask_auto_corrected";

	public static final String TIME_EXCEEDED_AUTO_SUBMIT_MADE = "task.complex.time_exceeded_auto_submit_made";

	public static final String SHOW_CORRECTION_NOT_POSSIBLE = "task.complex.show_correction_not_possible";

	public static final String NO_UNCORRECTED_AND_UNASSIGNED_SOLUTIONS_AVAILABLE = "task.no_uncorrected_and_unassigned_solutions_available";

	public static final String CANNOT_CORRECT_TASK_IN_PROGRESS = "task.complex.cannot_correct_task_in_progress";

	public static final String CANNOT_CORRECT_TASK_NOT_SOLVED = "task.cannot_correct_task_not_solved";

	public static final String CANNOT_CORRECT_TASK_NOT_IN_PROGRESS = "task.cannot_correct_task_not_in_progress";

	public static final String STUDENT_CAN_ONLY_ANNOTATE_CORRECTED_TRY = "task.student_can_only_annotate_corrected_try";

	public static final String CORRECTOR_CAN_ONLY_ACKNOWLEDGE_IF_ANNOTATED = "task.corrector_can_only_acknowledge_if_annotated";

//	public static final String CANNOT_MANUALLY_CORRECT_SUBTASK = "task.complex.cannot_manually_correct_subtask";


}
