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
package de.thorstenberger.taskmodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thorsten Berger
 *
 */
public class TaskModelViewDelegate {

	private static Map<String, Map<Long, TaskModelViewDelegateObject>> sessions = 
			Collections.synchronizedMap( new HashMap<String, Map<Long, TaskModelViewDelegateObject>>() );
	
	public static void storeDelegateObject( String sessionId, long taskId, TaskModelViewDelegateObject delegateObject ){
		Map<Long, TaskModelViewDelegateObject> session = sessions.get( sessionId );
		if( session == null ){
			session = new HashMap<Long, TaskModelViewDelegateObject>();
			sessions.put( sessionId, session );
		}
		session.put( taskId, delegateObject );
	}
	
	public static TaskModelViewDelegateObject getDelegateObject( String sessionId, long taskId ){
		Map<Long, TaskModelViewDelegateObject> session = sessions.get( sessionId );
		if( session == null )
			return null;
		else
			return session.get( taskId );
	}
	
	public static void removeSession( String sessionId ){
		sessions.remove( sessionId );
	}

}
