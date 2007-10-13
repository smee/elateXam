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
package de.thorstenberger.taskmodel.complex.complextaskdef.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskDefRootImpl implements ComplexTaskDefRoot {

	private ComplexTaskDef complexTaskDef;
	private ComplexTaskFactory complexTaskFactory;

	/**
	 *
	 */
	public ComplexTaskDefRootImpl( ComplexTaskDef complexTaskDef, ComplexTaskFactory complexTaskFactory ) {
		this.complexTaskDef = complexTaskDef;
		this.complexTaskFactory = complexTaskFactory;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getCategories()
	 */
	public Map<String, Category> getCategories() {
		Map<String, Category> ret = new HashMap<String, Category>();
		Iterator it = complexTaskDef.getCategory().iterator();
		while( it.hasNext() ){
			CategoryType category = (CategoryType) it.next();
			ret.put( category.getId(), new CategoryImpl( complexTaskFactory, category ) );
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getCategoriesList()
	 */
	public List<Category> getCategoriesList() {
		List<Category> ret = new ArrayList<Category>();
		Iterator it = complexTaskDef.getCategory().iterator();
		while( it.hasNext() ){
			CategoryType category = (CategoryType) it.next();
			ret.add( new CategoryImpl( complexTaskFactory, category ) );
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getDescription()
	 */
	public String getDescription() {
		return complexTaskDef.getDescription();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getStartText()
	 */
	public String getStartText() {
		return complexTaskDef.getStartText();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getTasksPerPage()
	 */
	public int getTasksPerPage() {
		return complexTaskDef.getConfig().getTasksPerPage();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getTimeInMinutesWithKindnessExtensionTime()
	 */
	public Integer getTimeInMinutesWithKindnessExtensionTime() {
		if( !hasTimeRestriction() )
			return null;

		if( complexTaskDef.getConfig().isSetKindnessExtensionTime() )
			return complexTaskDef.getConfig().getTime() + complexTaskDef.getConfig().getKindnessExtensionTime();
		else
			return complexTaskDef.getConfig().getTime();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getTimeInMinutesWithoutKindnessExtensionTime()
	 */
	public Integer getTimeInMinutesWithoutKindnessExtensionTime() {
		if( !hasTimeRestriction() )
			return null;

		return complexTaskDef.getConfig().getTime();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getTitle()
	 */
	public String getTitle() {
		return complexTaskDef.getTitle();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getTries()
	 */
	public int getTries() {
		return complexTaskDef.getConfig().getTries();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#hasTimeRestriction()
	 */
	public boolean hasTimeRestriction() {
		return complexTaskDef.getConfig().isSetTime() && complexTaskDef.getConfig().getTime() > 0;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getKindnessExtensionTimeInMinutes()
	 */
	public Integer getKindnessExtensionTimeInMinutes() {
		if( !complexTaskDef.getConfig().isSetKindnessExtensionTime() )
			return null;
		else
			return complexTaskDef.getConfig().getKindnessExtensionTime();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#isShowHandlingHintsBeforeStart()
	 */
	public boolean isShowHandlingHintsBeforeStart() {
		if( !complexTaskDef.isSetShowHandlingHintsBeforeStart() )
			return true;
		else
			return complexTaskDef.isShowHandlingHintsBeforeStart();
	}
	
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot#getCorrectionMode()
	 */
	public CorrectionMode getCorrectionMode() {
		
		if( complexTaskDef.getConfig().isSetCorrectionMode() ){
			
			if( complexTaskDef.getConfig().getCorrectionMode().isSetRegular() )
				return new RegularCorrectionMode();
			else if( complexTaskDef.getConfig().getCorrectionMode().isSetCorrectOnlyProcessedTasks() )
				return new CorrectOnlyProcessedTasksCorrectionMode( complexTaskDef.getConfig().getCorrectionMode().getCorrectOnlyProcessedTasks() );
			else if( complexTaskDef.getConfig().getCorrectionMode().isSetMultipleCorrectors() )
				return new MultipleCorrectorsCorrectionMode( complexTaskDef.getConfig().getCorrectionMode().getMultipleCorrectors() );
			
		}
		
		return new RegularCorrectionMode();
	}
	
	public class RegularCorrectionMode implements CorrectionMode{

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionMode#getType()
		 */
		public CorrectionModeType getType() {
			return ComplexTaskDefRoot.CorrectionModeType.REGULAR;
		}
		
	}
	
	public class CorrectOnlyProcessedTasksCorrectionMode implements CorrectionMode{
		
		private int first_n_tasks;

		/**
		 * @param first_n_tasks
		 */
		public CorrectOnlyProcessedTasksCorrectionMode( int first_n_tasks ) {
			this.first_n_tasks = first_n_tasks;
		}

		/**
		 * @return the first_n_tasks
		 */
		public int getFirst_n_tasks() {
			return first_n_tasks;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionMode#getType()
		 */
		public CorrectionModeType getType() {
			return ComplexTaskDefRoot.CorrectionModeType.CORRECTONLYPROCESSEDTASKS;
		}
		
	}
	
	public class MultipleCorrectorsCorrectionMode implements CorrectionMode{

		private int correctors;
		
		/**
		 * @param correctors
		 */
		public MultipleCorrectorsCorrectionMode(int correctors) {
			super();
			this.correctors = correctors;
		}

		/**
		 * @return the correctors
		 */
		public int getCorrectors() {
			return correctors;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionMode#getType()
		 */
		public CorrectionModeType getType() {
			return ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS;
		}
		
	}
	

	public ComplexTaskDef getJAXBContent() {
		return complexTaskDef;
	}



}
