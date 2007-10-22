/*

Copyright (C) 2007 Steffen Dienst

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
package de.thorstenberger.taskmodel;

/**
 * RuntimeException as parent for all exceptions occuring at runtime
 * @author Steffen Dienst
 *
 */
public class TaskModelRuntimeException extends RuntimeException {

	/**
	 *
	 */
	public TaskModelRuntimeException() {
	}

	/**
	 *
	 * @param message
	 */
	public TaskModelRuntimeException(String message) {
		super(message);
	}

	/**
	 *
	 * @param cause
	 */
	public TaskModelRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public TaskModelRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
