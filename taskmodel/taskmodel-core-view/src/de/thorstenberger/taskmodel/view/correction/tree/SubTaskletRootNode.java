/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel.view.correction.tree;

import java.util.ArrayList;
import java.util.List;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.view.tree.DataNode;


/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskletRootNode implements DataNode {

    private List<SubTasklet> subTasklets;
    private String studentLogin;
    private long taskId;
    private String currentlySelectedSubtaskNum;
    
    /**
     * 
     */
    public SubTaskletRootNode( List<SubTasklet> subTasklets, String studentLogin, long taskId, String currentlySelectedSubtaskNum) {
        this.subTasklets = subTasklets;
        this.studentLogin = studentLogin;
        this.taskId = taskId;
        this.currentlySelectedSubtaskNum = currentlySelectedSubtaskNum;
    }
    
    
    /* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getName()
	 */
	public String getName() {
		return "Aufgaben";
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#getSubNodes()
	 */
	public List<DataNode> getSubNodes() {
		
		SubtaskletFolder uncorrected = new SubtaskletFolder( SubtaskletFolder.Type.UNCORRECTED );
		SubtaskletFolder corrected = new SubtaskletFolder( SubtaskletFolder.Type.CORRECTED );
		SubtaskletFolder needsManualCorrection = new SubtaskletFolder( SubtaskletFolder.Type.NEEDSMANUALCORRECTION );
		
		for( SubTasklet subTasklet : subTasklets ){
			
			if( subTasklet.isCorrected() ){
				corrected.addSubNode( new SubtaskletNode( subTasklet.getVirtualSubtaskNumber(),
						studentLogin, taskId, subTasklet.getVirtualSubtaskNumber().equals( currentlySelectedSubtaskNum ) ) );
			}else{
				if( subTasklet.isNeedsManualCorrection() ){
					needsManualCorrection.addSubNode( new SubtaskletNode( subTasklet.getVirtualSubtaskNumber(),
							studentLogin, taskId, subTasklet.getVirtualSubtaskNumber().equals( currentlySelectedSubtaskNum ) ) );
				}else{
					uncorrected.addSubNode( new SubtaskletNode( subTasklet.getVirtualSubtaskNumber(),
							studentLogin, taskId, subTasklet.getVirtualSubtaskNumber().equals( currentlySelectedSubtaskNum ) ) );
				}
			}
			
		}
		
		List<DataNode> ret = new ArrayList<DataNode>();
		ret.add( needsManualCorrection );
		ret.add( corrected );
		ret.add( uncorrected );
		
		return ret;
		
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.view.tree.DataNode#isFolder()
	 */
	public boolean isFolder() {
		return true;
	}


	/**
	 * @return Returns the studentLogin.
	 */
	public String getStudentLogin() {
		return studentLogin;
	}


	/**
	 * @return Returns the taskId.
	 */
	public long getTaskId() {
		return taskId;
	}



}
