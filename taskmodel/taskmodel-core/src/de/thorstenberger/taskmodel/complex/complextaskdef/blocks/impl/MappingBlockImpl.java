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
package de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.MappingChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.MappingSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.MappingSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.MappingTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.MappingTaskBlockType.Choice;

/**
 * @author Thorsten Berger
 *
 */
public class MappingBlockImpl implements Block {

	MappingTaskBlock mappingTaskBlock;

	/**
	 * @param block
	 */
	public MappingBlockImpl(MappingTaskBlock block) {
		mappingTaskBlock = block;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getSubTaskDefOrChoiceList()
	 */
	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
		List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();
		
		List subTaskDefOrChoice = mappingTaskBlock.getMappingSubTaskDefOrChoice();
		Iterator it = subTaskDefOrChoice.iterator();
		while( it.hasNext() ){
			Object next = it.next();
			if( next instanceof MappingSubTaskDef )
				ret.add( new MappingSubTaskDefImpl( (MappingSubTaskDef) next ) );
			else if( next instanceof Choice )
				ret.add( new MappingChoiceImpl( (Choice) next ) );
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getNumberOfSelectedSubTasks()
	 */
	public int getNumberOfSelectedSubTasks() {
		return mappingTaskBlock.getConfig().getNoOfSelectedTasks();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#isPreserveOrder()
	 */
	public boolean isPreserveOrder() {
		return mappingTaskBlock.getConfig().isPreserveOrder();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getPointsPerSubTask()
	 */
	public float getPointsPerSubTask() {
		return mappingTaskBlock.getConfig().getPointsPerTask();
	}

	/**
	 * backdoor access to JAXB element
	 * @return Returns the mappingTaskBlock.
	 */
	public MappingTaskBlock getMappingTaskBlock() {
		return mappingTaskBlock;
	}

	
}
