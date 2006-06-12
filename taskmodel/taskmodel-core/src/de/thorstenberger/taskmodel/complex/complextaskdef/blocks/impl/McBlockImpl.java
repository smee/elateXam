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
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.McChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.McSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlock;

/**
 * @author Thorsten Berger
 *
 */
public class McBlockImpl implements Block {

	McTaskBlock mcTaskBlock;
	
	/**
	 * 
	 */
	public McBlockImpl( McTaskBlock mcTaskBlock ) {
		this.mcTaskBlock = mcTaskBlock;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getSubTaskDefOrChoiceList()
	 */
	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
		List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();
		
		List subTaskDefOrChoice = mcTaskBlock.getMcSubTaskDefOrChoice();
		Iterator it = subTaskDefOrChoice.iterator();
		while( it.hasNext() ){
			Object next = it.next();
			if( next instanceof McSubTaskDef )
				ret.add( new McSubTaskDefImpl( (McSubTaskDef) next ) );
			else if( next instanceof de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlockType.Choice )
				ret.add( new McChoiceImpl(
						(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlockType.Choice) next ) );
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getNumberOfSelectedSubTasks()
	 */
	public int getNumberOfSelectedSubTasks() {
		return mcTaskBlock.getConfig().getNoOfSelectedTasks();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#isPreserveOrder()
	 */
	public boolean isPreserveOrder() {
		return mcTaskBlock.getConfig().isSetPreserveOrder();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getPointsPerSubTask()
	 */
	public float getPointsPerSubTask() {
		return mcTaskBlock.getConfig().getPointsPerTask();
	}
	
	/**
	 * backdoor access to the JAXB element
	 * @return
	 */
	public McTaskBlock getMcTaskBlock(){
		return mcTaskBlock;
	}

}
