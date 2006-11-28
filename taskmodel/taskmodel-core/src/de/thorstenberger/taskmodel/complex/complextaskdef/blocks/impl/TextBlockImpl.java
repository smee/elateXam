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
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.TextChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.TextSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlock;

/**
 * @author Thorsten Berger
 *
 */
public class TextBlockImpl implements Block {

	public static final String TYPE = "text";
	private TextTaskBlock textTaskBlock;
	private int index;

	/**
	 * @param block
	 */
	public TextBlockImpl(TextTaskBlock block, int index) {
		textTaskBlock = block;
		this.index = index;
	}

	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getIndex()
	 */
	public int getIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getType()
	 */
	public String getType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getSubTaskDefOrChoiceList()
	 */
	public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
		List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();
		
		List subTaskDefOrChoice = textTaskBlock.getTextSubTaskDefOrChoice();
		Iterator it = subTaskDefOrChoice.iterator();
		while( it.hasNext() ){
			Object next = it.next();
			if( next instanceof TextSubTaskDef )
				ret.add( new TextSubTaskDefImpl( (TextSubTaskDef) next ) );
			else if( next instanceof de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice )
				ret.add( new TextChoiceImpl(
						(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlockType.Choice) next ) );
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getNumberOfSelectedSubTasks()
	 */
	public int getNumberOfSelectedSubTasks() {
		return textTaskBlock.getConfig().getNoOfSelectedTasks();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#isPreserveOrder()
	 */
	public boolean isPreserveOrder() {
		return textTaskBlock.getConfig().isPreserveOrder();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getPointsPerSubTask()
	 */
	public float getPointsPerSubTask() {
		return textTaskBlock.getConfig().getPointsPerTask();
	}

	/**
	 * backdoor access to JAXB element
	 * @return Returns the textTaskBlock.
	 */
	public TextTaskBlock getTextTaskBlock() {
		return textTaskBlock;
	}

}
