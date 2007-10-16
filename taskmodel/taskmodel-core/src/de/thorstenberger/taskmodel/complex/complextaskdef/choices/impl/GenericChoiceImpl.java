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
package de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskdef.Choice;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.GenericSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;

public class GenericChoiceImpl implements Choice {

	private Object jaxbChoice;

	public GenericChoiceImpl(Object jaxbChoice) {
		this.jaxbChoice=jaxbChoice;
	}
	
	public List<SubTaskDef> getSubTaskDefs() {
		List<SubTaskDef> ret = new ArrayList<SubTaskDef>();
		Iterator it = getSubTaskDefIterator();
		while( it.hasNext() ){
			ret.add( new GenericSubTaskDefImpl( (SubTaskDefType) it.next() ) );
		}
		return ret;
	}

	private Iterator getSubTaskDefIterator() {
		Method[] methods=jaxbChoice.getClass().getMethods();
		for (Method method : methods) {
			if(method.getName().contains("SubTaskDef")) {
				try {
					return ((List)method.invoke(jaxbChoice)).iterator();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return new Iterator() {
			public boolean hasNext() {
				return false;
			}
			public Object next() {
				return null;
			}
			public void remove() {
			}
		};
	}

}