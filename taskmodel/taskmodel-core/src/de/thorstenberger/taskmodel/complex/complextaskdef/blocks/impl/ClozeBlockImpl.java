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
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.ClozeChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.ClozeSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlock;

/**
 * @author Thorsten Berger
 *
 */
public class ClozeBlockImpl implements Block {

	public static final String TYPE = "cloze";
	private ClozeTaskBlock clozeTaskBlock;
	private int index;
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/**
	 * @param block
	 */
	public ClozeBlockImpl(ClozeTaskBlock block, int index) {
		clozeTaskBlock = block;
		this.index = index;
	}
	

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getIndex()
	 */
	public int getIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getSubTaskDefOrChoiceList()
	 */
	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
		List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();
		
		List subTaskDefOrChoice = clozeTaskBlock.getClozeSubTaskDefOrChoice();
		Iterator it = subTaskDefOrChoice.iterator();
		while( it.hasNext() ){
			Object next = it.next();
			if( next instanceof ClozeSubTaskDef )
				ret.add( new ClozeSubTaskDefImpl( (ClozeSubTaskDef) next ) );
			else if( next instanceof de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice )
				ret.add( new ClozeChoiceImpl(
						(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlockType.Choice) next ) );
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getNumberOfSelectedSubTasks()
	 */
	public int getNumberOfSelectedSubTasks() {
		return clozeTaskBlock.getConfig().getNoOfSelectedTasks();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#isPreserveOrder()
	 */
	public boolean isPreserveOrder() {
		return clozeTaskBlock.getConfig().isPreserveOrder();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getPointsPerSubTask()
	 */
	public float getPointsPerSubTask() {
		return clozeTaskBlock.getConfig().getPointsPerTask();
	}

	/**
	 * backdoor access to JAXB element
	 * @return Returns the clozeTaskBlock.
	 */
	public ClozeTaskBlock getClozeTaskBlock() {
		return clozeTaskBlock;
	}

}
