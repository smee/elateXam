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
/**
 *
 */
package de.thorstenberger.taskmodel.complex;

import java.io.InputStream;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionModeType;
import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefRootImpl.CorrectOnlyProcessedTasksCorrectionMode;
import de.thorstenberger.taskmodel.impl.AbstractTaskDef;

/**
 * @author Thorsten Berger
 *
 */
public class TaskDef_ComplexImpl extends AbstractTaskDef implements TaskDef_Complex {

	private boolean showCorrectionToUsers;

	private ComplexTaskDefRoot complexTaskDefRoot;

	/**
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param description
	 * @param deadline
	 * @param stopped
	 */
	public TaskDef_ComplexImpl(long id, String title, String shortDescription, Long deadline, boolean stopped, Long followingTaskId, ComplexTaskDefDAO complexTaskDefDAO, InputStream complexTaskIS, boolean showCorrectionToUsers, boolean visible ) {
    this(id, title, shortDescription, deadline, stopped, followingTaskId, showCorrectionToUsers, visible, load(complexTaskDefDAO, complexTaskIS));
	}

  private static ComplexTaskDefRoot load(ComplexTaskDefDAO complexTaskDefDAO, InputStream complexTaskIS) {
    try {
      return complexTaskDefDAO.getComplexTaskDefRoot(complexTaskIS);
    } catch (TaskApiException e) {
      throw new TaskModelPersistenceException(e);
    }
  }

  public TaskDef_ComplexImpl(long id, String title, String shortDescription, Long deadline, boolean stopped,
      Long followingTaskId, boolean showCorrectionToUsers, boolean visible, ComplexTaskDefRoot complexTaskDefRoot) {
    super(id, title, shortDescription, deadline, stopped, followingTaskId, visible);
    this.showCorrectionToUsers = showCorrectionToUsers;
    this.visible = visible;
    this.complexTaskDefRoot = complexTaskDefRoot;
  }


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.TaskDef_Complex#isShowCorrectionToUsers()
	 */
	public boolean isShowCorrectionToUsers() {
		return showCorrectionToUsers;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.TaskDef#getType()
	 */
	public String getType() {
		return TaskContants.TYPE_COMPLEX;
	}

	/**
	 * @param showCorrectionToUsers The showCorrectionToUsers to set.
	 */
	public void setShowCorrectionToUsers(boolean showCorrectionToUsers) {
		this.showCorrectionToUsers = showCorrectionToUsers;
	}

	/**
	 * @return Returns the complexTaskDefRoot.
	 */
	public ComplexTaskDefRoot getComplexTaskDefRoot() {
		return complexTaskDefRoot;
	}

  /* (non-Javadoc)
   * @see de.thorstenberger.taskmodel.TaskDef#reachablePoints()
   */
  public float getReachablePoints() {
    if(this.complexTaskDefRoot.getCorrectionMode().getType()== CorrectionModeType.CORRECTONLYPROCESSEDTASKS){
      return countReachablePoints(complexTaskDefRoot, ((CorrectOnlyProcessedTasksCorrectionMode)complexTaskDefRoot.getCorrectionMode()).getFirst_n_tasks());
    }else{
      return countReachablePoints(complexTaskDefRoot, Integer.MAX_VALUE);
    }
    
  }

  /**
   * @param root
   * @param first_n_tasks
   * @return
   */
  private float countReachablePoints(ComplexTaskDefRoot root, int n) {
    int count = 0;
    float score = 0;
    for (Category cat : root.getCategoriesList()) {
      for(Block block: cat.getBlocks()){
        int tasksFromBlock = Math.min( n - count ,block.getNumberOfSelectedSubTasks());
        count += tasksFromBlock;
        score += tasksFromBlock * block.getPointsPerSubTask();
      }
    }
    return score;
  }



}
