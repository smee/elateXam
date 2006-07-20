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

/**
 * @author Thorsten Berger
 *
 */
public interface Tasklet {

	public static final String INITIALIZED = "initialized";
	public static final String INPROGRESS = "in_progress";
	public static final String SOLVED = "processed";
	public static final String CORRECTING = "correcting";
	public static final String CORRECTED = "corrected";

//	public static final int INITIALIZED = 1;
//	public static final int INPROGRESS = 2;
//	public static final int SOLVED = 3;
//	public static final int CORRECTING = 4;
//	public static final int CORRECTED = 5;

	public void update();
	
	public String getUserId();
	
	public long getTaskId();
	
	public String getStatus();
	
	public TaskletCorrection getTaskletCorrection();
	
	public void logPostData(String msg, String ip);

	public void logPostData(String msg, Throwable throwable, String ip);
	
}
