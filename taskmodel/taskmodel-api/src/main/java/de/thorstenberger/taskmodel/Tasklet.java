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
package de.thorstenberger.taskmodel;

import java.util.List;
import java.util.Map;

/**
 * A tasklet resembles a {@link TaskDef} instance that a user is currently working on.
 *
 * @author Thorsten Berger
 *
 */
public interface Tasklet {


	/**
	 * The tasklet lifecycle is determined by its status:
	 *
	 * INITIALIZED: tasklet new instantiated and nothing done yet, student can start the tasklet
	 * INPROGRESS: solving the tasklet is in progress
	 * SOLVED: tasklet solved, no more progress
	 * CORRECTING: a corrector or the system is currently correcting the tasklet, corrector may also annotate the student's solution
	 * CORRECTED: the complete tasklet has been corrected, student may also annotate the corrector's marking
	 * ANNOTATED: student annotated the corrector's marking
	 * ANNOTATION_ACKNOWLEDGED: corrector acknowledged the student's annotation and possibly adjusted the marking
	 *
	 */
	public enum Status{

		INITIALIZED( 0, "initialized" ),
		INPROGRESS( 1, "in_progress" ),
		SOLVED( 2, "processed" ),
		CORRECTING( 3, "correcting" ),
		CORRECTED( 4, "corrected" ),
		ANNOTATED( 5, "annotated" ),
		ANNOTATION_ACKNOWLEDGED( 6, "annotation_acknowledged" );

		private final int order;
		private final String value;
		public int getOrder(){
			return order;
		}
		@Override
    public String toString(){
			return value;
		}
		public String getValue(){
			return value;
		}

		Status( int order, String value ){
			this.order = order;
			this.value = value;
		}

	}

	public static final String FLAG_HAS_CORRECTOR_ANNOTATION = "has_corrector_annotation";
	public static final String FLAG_HAS_STUDENT_ANNOTATION = "has_student_annotation";


	/**
	 * Check if this tasklet is still within the maximum allowed processing time. If not,
	 * this tasklet gets submitted. This prevents lurking around infinitely.
	 */
	public void update();

	public String getUserId();

	public long getTaskId();

	public Status getStatus();

	public boolean hasOrPassedStatus( Status status );

	public TaskletCorrection getTaskletCorrection();

	public List<String> getFlags();

	public void addFlag( String flag );

	public void removeFlag( String flag );

	/**
	 * Gets a property of the tasklet identified by its key. The value is always non-null.
	 * @param key
	 * @return the string representation of the property's value or null if does not exist
	 */
	public String getProperty( String key );

	/**
	 * Sets a property of the tasklet. Remove by setting value == null.
	 * @param key
	 * @param value
	 */
	public void setProperty( String key, String value );
	
	/**
	 * Returns the backing properties Map.
	 * @return
	 */
	public Map<String, String> getProperties();
	
	/**
	 * Assigns the tasklet to the mentioned corrector.
	 * @param correctorId the corrector id/login
	 * @throws TaskApiException if Tasklet cannot be assigned due to the status not being {@link Status.SOLVED}.
	 */
	public void assignToCorrector( String correctorId ) throws TaskApiException;

	/**
	 * Unassigns the tasklet from the currently assigned corrector.
	 * @throws TaskApiException if tasklet is not assigned
	 */
	public void unassignFromCorrector() throws TaskApiException;

	public void logPostData(String msg, String ip);

	public void logPostData(String msg, Throwable throwable, String ip);

}
