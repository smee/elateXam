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
package de.thorstenberger.taskmodel.complex.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.Choice;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.ClozeBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.MappingBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.McBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.PaintBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.TextBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.ClozeSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.MappingSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.McSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.TextSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.PaintSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.TryImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.SubTasklet_ClozeImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.SubTasklet_MCImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.SubTasklet_MappingImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.SubTasklet_PaintImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.SubTasklet_TextImpl;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.MappingTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.McTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.PaintTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.TextTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.ClozeSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.MappingSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.McSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.PaintSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.TextSubTask;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskFactoryImpl implements ComplexTaskFactory {

	private ObjectFactory objectFactory = new ObjectFactory();
	
	public Block instantiateBlock( Object jaxbBlock, int index ){
		
		if( jaxbBlock instanceof McTaskBlock )
			return new McBlockImpl( (McTaskBlock)jaxbBlock, index );
		else if( jaxbBlock instanceof MappingTaskBlock )
			return new MappingBlockImpl( (MappingTaskBlock)jaxbBlock, index );
		else if( jaxbBlock instanceof ClozeTaskBlock )
			return new ClozeBlockImpl( (ClozeTaskBlock)jaxbBlock, index );
		else if( jaxbBlock instanceof TextTaskBlock )
			return new TextBlockImpl( (TextTaskBlock)jaxbBlock, index );
		else if ( jaxbBlock instanceof PaintTaskBlock)
			return new PaintBlockImpl ((PaintTaskBlock)jaxbBlock, index );
		
		return null;
		
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskFactory#createSubTaskletForSubTaskDef(de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef)
	 */
	public SubTasklet createSubTaskletForSubTaskDef(SubTaskDef subTaskDef, ComplexTaskDefRoot complexTaskDefRoot, String categoryId )
			throws TaskApiException {

		try {
			
			if( subTaskDef instanceof McSubTaskDefImpl ){
				McSubTask mcSubTask = objectFactory.createComplexTaskHandlingTypeTryTypePageTypeMcSubTask();
				mcSubTask.setRefId( subTaskDef.getId() );
				return instantiateSubTasklet( mcSubTask, complexTaskDefRoot, categoryId );
			}else if( subTaskDef instanceof MappingSubTaskDefImpl ){
				MappingSubTask mappingSubTask = objectFactory.createComplexTaskHandlingTypeTryTypePageTypeMappingSubTask();
				mappingSubTask.setRefId( subTaskDef.getId() );
				return instantiateSubTasklet( mappingSubTask, complexTaskDefRoot, categoryId );
			}else if( subTaskDef instanceof ClozeSubTaskDefImpl ){
				ClozeSubTask clozeSubTask = objectFactory.createComplexTaskHandlingTypeTryTypePageTypeClozeSubTask();
				clozeSubTask.setRefId( subTaskDef.getId() );
				return instantiateSubTasklet( clozeSubTask, complexTaskDefRoot, categoryId );
			}else if( subTaskDef instanceof TextSubTaskDefImpl ){
				TextSubTask textSubTask = objectFactory.createComplexTaskHandlingTypeTryTypePageTypeTextSubTask();
				textSubTask.setRefId( subTaskDef.getId() );
				return instantiateSubTasklet( textSubTask, complexTaskDefRoot, categoryId );
			}else if( subTaskDef instanceof PaintSubTaskDefImpl ){
				PaintSubTask paintSubTask = objectFactory.createComplexTaskHandlingTypeTryTypePageTypePaintSubTask();
				paintSubTask.setRefId( subTaskDef.getId() );
				return instantiateSubTasklet( paintSubTask, complexTaskDefRoot, categoryId );
			}
			
		} catch (JAXBException e) {
			throw new TaskApiException();
		}
		
		return null;
	}

	
	public SubTasklet instantiateSubTasklet( Object jaxbSubTask, ComplexTaskDefRoot complexTaskDefRoot, String categoryId ){
		
		if( jaxbSubTask instanceof McSubTask ){
			
			McSubTask mcSubTask = (McSubTask)jaxbSubTask;
			BlockAndSubTaskDef blockAndSubTaskDef = lookupTaskBlockAndSubTaskDef( complexTaskDefRoot, categoryId, mcSubTask.getRefId() );
			return new SubTasklet_MCImpl( blockAndSubTaskDef.getBlock(), (McSubTaskDefImpl)blockAndSubTaskDef.getSubTaskDef(), mcSubTask );
			
		}else if( jaxbSubTask instanceof MappingSubTask ){
			
			MappingSubTask mappingSubTask = (MappingSubTask)jaxbSubTask;
			BlockAndSubTaskDef blockAndSubTaskDef = lookupTaskBlockAndSubTaskDef( complexTaskDefRoot, categoryId, mappingSubTask.getRefId() );
			return new SubTasklet_MappingImpl( blockAndSubTaskDef.getBlock(), (MappingSubTaskDefImpl)blockAndSubTaskDef.getSubTaskDef(), mappingSubTask );
			
		}else if( jaxbSubTask instanceof ClozeSubTask ){
			
			ClozeSubTask clozeSubTask = (ClozeSubTask)jaxbSubTask;
			BlockAndSubTaskDef blockAndSubTaskDef = lookupTaskBlockAndSubTaskDef( complexTaskDefRoot, categoryId, clozeSubTask.getRefId() );
			return new SubTasklet_ClozeImpl( blockAndSubTaskDef.getBlock(), (ClozeSubTaskDefImpl)blockAndSubTaskDef.getSubTaskDef(), clozeSubTask );
			
		}else if( jaxbSubTask instanceof TextSubTask ){
			
			TextSubTask textSubTask = (TextSubTask)jaxbSubTask;
			BlockAndSubTaskDef blockAndSubTaskDef = lookupTaskBlockAndSubTaskDef( complexTaskDefRoot, categoryId, textSubTask.getRefId() );
			return new SubTasklet_TextImpl( blockAndSubTaskDef.getBlock(), (TextSubTaskDefImpl)blockAndSubTaskDef.getSubTaskDef(), textSubTask );
			
		}else if( jaxbSubTask instanceof PaintSubTask ){
			
			PaintSubTask paintSubTask = (PaintSubTask)jaxbSubTask;
			BlockAndSubTaskDef blockAndSubTaskDef = lookupTaskBlockAndSubTaskDef( complexTaskDefRoot, categoryId, paintSubTask.getRefId() );
			return new SubTasklet_PaintImpl( blockAndSubTaskDef.getBlock(), (PaintSubTaskDefImpl)blockAndSubTaskDef.getSubTaskDef(), paintSubTask );
			
		}
		
		return null;
		
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskFactory#createTry(long)
	 */
	public Try createTry( long startTime, ComplexTaskFactory complexTaskFactory, ComplexTaskDefRoot complexTaskDefRoot ) throws TaskApiException{
		TryType tryType;
		try {
			tryType = objectFactory.createComplexTaskHandlingTypeTryType();
		} catch (JAXBException e) {
			throw new TaskApiException( e );
		}
		tryType.setStartTime( startTime );
		// FIXME never used; remove?
		tryType.setSubmitted( false );
		
		Try tryImpl = new TryImpl( tryType, complexTaskFactory, complexTaskDefRoot  );
		return tryImpl;
	}

	
	
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.taskdef.ComplexTaskDefHelper#getMCBlockOfTask(java.lang.String)
	 */
	private ComplexTaskFactoryImpl.BlockAndSubTaskDef lookupTaskBlockAndSubTaskDef( ComplexTaskDefRoot complexTaskDefRoot, String categoryId, String subTaskDefId ){
		
		Category category = complexTaskDefRoot.getCategories().get( categoryId );
		List<Block> blocks = category.getBlocks();
		
		// alle Blöcke in der Kategorie durchlaufen
		for( Block block : blocks ){
			
			List<SubTaskDefOrChoice> subTaskDefsOrChoices = block.getSubTaskDefOrChoiceList();
			for( SubTaskDefOrChoice subTaskDefOrChoice : subTaskDefsOrChoices ){
				
				if( subTaskDefOrChoice instanceof SubTaskDef ){
					if( ((SubTaskDef)subTaskDefOrChoice).getId().equals( subTaskDefId ) )
						return new BlockAndSubTaskDef( block, (SubTaskDef)subTaskDefOrChoice );
					
				}else if( subTaskDefOrChoice instanceof Choice ){
					Choice choice = (Choice)subTaskDefOrChoice;
					List<SubTaskDef> subTaskDefs = choice.getSubTaskDefs();
					for( SubTaskDef subTaskDef : subTaskDefs )
						if( subTaskDef.getId().equals( subTaskDefId ) )
							return new BlockAndSubTaskDef( block, subTaskDef );
				}
				
			}

		}
	
		// no corresponding block found
		return null;
		
	}
	
	private class BlockAndSubTaskDef{
		
		private Block block;
		private SubTaskDef subTaskDef;
		
		public BlockAndSubTaskDef(Block block, SubTaskDef def) {
			this.block = block;
			subTaskDef = def;
		}

		public Block getBlock() {
			return block;
		}

		public SubTaskDef getSubTaskDef() {
			return subTaskDef;
		}
				
	}
	


}
