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
package de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.thorstenberger.taskmodel.TaskModelRuntimeException;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.GenericChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.GenericSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.ConfigType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.TaskBlockType;

public class GenericBlockImpl implements Block{
	private String type;
	private TaskBlockType jaxbTaskBlock;
	private final int index;

	public GenericBlockImpl(TaskBlockType jaxbTaskBlock, int index) {
		this.jaxbTaskBlock=jaxbTaskBlock;
		this.index=index;
		this.type=getTypeNameFromClassName(jaxbTaskBlock.getClass().getName());
	}

	private String getTypeNameFromClassName(String name) {
		return name.substring(name.lastIndexOf('$')+1,name.indexOf("TaskBlock")).toLowerCase();
	}

	public int getIndex() {
		return index;
	}


	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
		List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();

		List subTaskDefOrChoice = getJaxbSubTaskDefOrChoice();
		Iterator it = subTaskDefOrChoice.iterator();
		while( it.hasNext() ){
			Object next = it.next();
			if( next.getClass().getName().endsWith("SubTaskDefImpl") )
				ret.add( new GenericSubTaskDefImpl( (SubTaskDefType) next ) );
			else if( next.getClass().getName().endsWith("TaskBlockType.Choice"))
				ret.add( new GenericChoiceImpl( next ) );
		}

		return ret;
	}

	private List getJaxbSubTaskDefOrChoice() {
		Method[] methods=jaxbTaskBlock.getClass().getMethods();
		for (Method method : methods) {
			if(method.getName().startsWith( "get" ) && method.getName().endsWith("SubTaskDefOrChoice"))
				try {
					return (List) method.invoke(jaxbTaskBlock);
				} catch (Exception e) {
					throw new TaskModelRuntimeException(e);
				}
		}
		return Collections.EMPTY_LIST;
	}

	public String getType() {
		return type;
	}

	public int getNumberOfSelectedSubTasks() {
		return getConfig().getNoOfSelectedTasks();
	}

	private ConfigType getConfig() {
		return jaxbTaskBlock.getConfig();
	}

	public float getPointsPerSubTask() {
		return getConfig().getPointsPerTask();
	}

	public boolean isPreserveOrder() {
		return getConfig().isPreserveOrder();
	}
	/**
	 * backdoor access to JAXB element
	 * @return Returns the jaxbTaskBlock.
	 */
	public Object getJaxbTaskBlock() {
		return jaxbTaskBlock;
	}

	public float getReachablePoints() {
		return getConfig().getPointsPerTask();
	}

}
