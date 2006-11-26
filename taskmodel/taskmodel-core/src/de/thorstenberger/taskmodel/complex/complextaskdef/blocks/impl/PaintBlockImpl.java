package de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskdef.choices.impl.PaintChoiceImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.PaintSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public class PaintBlockImpl implements Block {

	public static final String TYPE = "paint";
	private PaintTaskBlock paintTaskBlock;
		
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getType()
	 */
	public String getType() {
		return TYPE;
	}

		/**
		 * @param block
		 */
		public PaintBlockImpl(PaintTaskBlock block) {
			paintTaskBlock = block;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getSubTaskDefOrChoiceList()
		 */
		public List<SubTaskDefOrChoice> getSubTaskDefOrChoiceList() {
			List<SubTaskDefOrChoice> ret = new ArrayList<SubTaskDefOrChoice>();
			
			List subTaskDefOrChoice = paintTaskBlock.getPaintSubTaskDefOrChoice();
			Iterator it = subTaskDefOrChoice.iterator();
			while( it.hasNext() ){
				Object next = it.next();
				if( next instanceof PaintSubTaskDef )
					ret.add( new PaintSubTaskDefImpl( (PaintSubTaskDef) next ) );
				else if( next instanceof de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlockType.Choice )
					ret.add( new PaintChoiceImpl(
							(de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlockType.Choice) next ) );
			}
			
			return ret;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getNumberOfSelectedSubTasks()
		 */
		public int getNumberOfSelectedSubTasks() {
			return paintTaskBlock.getConfig().getNoOfSelectedTasks();
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#isPreserveOrder()
		 */
		public boolean isPreserveOrder() {
			return paintTaskBlock.getConfig().isPreserveOrder();
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.Block#getPointsPerSubTask()
		 */
		public float getPointsPerSubTask() {
			return paintTaskBlock.getConfig().getPointsPerTask();
		}

		/**
		 * backdoor access to JAXB element
		 * @return Returns the paintTaskBlock.
		 */
		public PaintTaskBlock getPaintTaskBlock() {
			return paintTaskBlock;
		}

	}
