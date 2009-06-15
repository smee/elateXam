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
package de.thorstenberger.taskmodel.view.statistics;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskStructureAction extends Action {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionMessages errors = new ActionMessages();

		long id;
		try {
			id = Long.parseLong( request.getParameter( "taskId" ) );
		} catch (NumberFormatException e) {
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "invalid.parameter" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
		
		CorrectorDelegateObject delegateObject = (CorrectorDelegateObject)TaskModelViewDelegate.getDelegateObject( request.getSession().getId(), id );

		if( delegateObject == null ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "no.session" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
		request.setAttribute( "ReturnURL", delegateObject.getReturnURL() );

		if( !delegateObject.isPrivileged() ){
			errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "not.privileged" ) );
			saveErrors( request, errors );
			return mapping.findForward( "error" );
		}
	
		
		TaskDef_Complex tdc = (TaskDef_Complex)delegateObject.getTaskDef();
		
		RootInfoVO rivo = new RootInfoVO();
		rivo.setTaskId( tdc.getId() );
		rivo.setDescription( tdc.getComplexTaskDefRoot().getDescription() );
		rivo.setKindnessExtensionTime( tdc.getComplexTaskDefRoot().getKindnessExtensionTimeInMinutes() );
		rivo.setStartText( tdc.getComplexTaskDefRoot().getStartText() );
		rivo.setTasksPerPage( tdc.getComplexTaskDefRoot().getTasksPerPage() );
		rivo.setTimeInMinutesWithoutKindnessExtensionTime( tdc.getComplexTaskDefRoot().getTimeInMinutesWithoutKindnessExtensionTime() );
		rivo.setTimeRestriction( tdc.getComplexTaskDefRoot().hasTimeRestriction() );
		rivo.setTitle( tdc.getComplexTaskDefRoot().getTitle() );
		rivo.setTries( tdc.getComplexTaskDefRoot().getTries() );
		List<CategoryInfoVO> civos = new ArrayList<CategoryInfoVO>();
		
		for( Category category : tdc.getComplexTaskDefRoot().getCategoriesList() ){
			CategoryInfoVO civo = new CategoryInfoVO();
			civo.setId( category.getId() );
			civo.setIgnoreOrderOfBlocks( category.isIgnoreOrderOfBlocks() );
			civo.setMixAllSubTasks( category.isMixAllSubTasks() );
			civo.setTasksPerPage( category.getTasksPerPage() );
			civo.setTitle( category.getTitle() );
			
			List<BlockInfoVO> bivos = new ArrayList<BlockInfoVO>();
			for( Block block : category.getBlocks() ){
				BlockInfoVO bivo = new BlockInfoVO();
				bivo.setIndex( block.getIndex() );
				bivo.setNumberOfSelectedSubTasks( block.getNumberOfSelectedSubTasks() );
				bivo.setPointsPerSubTask( block.getPointsPerSubTask() );
				bivo.setPreserveOrder( block.isPreserveOrder() );
				bivo.setType( block.getType() );
				bivos.add( bivo );
			}
			civo.setBlocks( bivos );
			civos.add( civo );
		}
		
		rivo.setCategories( civos );
		
		request.setAttribute( "Root", rivo );			
		return mapping.findForward( "success" );
		
	}
	
}
