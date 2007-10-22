/*

Copyright (C) 2006 Steffen Dienst

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
 * @author Steffen Dienst
 */
package junittask.subtasklet;

public class SandboxClassLoader extends ClassLoader{
	private boolean restricted;

	public SandboxClassLoader(ClassLoader parent) {
		super(parent);
		this.restricted=true;
	}

	@Override
	protected synchronized Class<?> loadClass(String c, boolean resolve)
	throws ClassNotFoundException{
		//System.out.println(c);
		if(restricted && (
				c.startsWith("java.io") ||
				c.startsWith("java.nio") ||
				c.startsWith("java.rmi") ||
				c.startsWith("java.lang.reflect") ||
				c.startsWith("java.lang.Thread") ||
				c.startsWith("java.lang.System") ||
				c.startsWith("java.lang.ClassLoader") ||
				c.startsWith("java.lang.Runtime") ||
				c.startsWith("java.lang.Process") ||
				c.startsWith("java.security") ||
				c.startsWith("de.elateportal") ||
				c.startsWith("de.thorstenberger") ||
				c.startsWith("java.sql") ||
				c.startsWith("java.awt") ||
				c.startsWith("java.rmi") ||
				c.startsWith("javax.") ||
				!(c.startsWith("java.") || c.startsWith("groovy."))))
			throw new SecurityException("Class \""+c+"\" is not permitted.");
		return super.loadClass(c,resolve);
	}

	public void setRestricted(boolean b) {
		this.restricted=b;
	}
}