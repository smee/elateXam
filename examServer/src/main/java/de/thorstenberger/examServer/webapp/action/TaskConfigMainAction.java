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
package de.thorstenberger.examServer.webapp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.webapp.form.SeedForm;
import de.thorstenberger.examServer.webapp.vo.TaskDefVO;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;

public class TaskConfigMainAction extends BaseAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    request.setAttribute("TaskDefs", loadTaskDefs());
    loadRandomSettings((SeedForm) form);

    return mapping.findForward("success");
  }

  private void loadRandomSettings(SeedForm form) {
    ConfigManager configManager = (ConfigManager) getBean("configManager");
    // manually set random seed?
    form.setRandomSeed(configManager.getRandomSeed());
    form.setRandomSeedRandom(configManager.isRandomSeedRandom());
  }

  /**
   * @return
   */
  protected List<TaskDefVO> loadTaskDefs() {
    TaskManager taskManager = (TaskManager) getBean("TaskManager");

		List<TaskDef> taskDefs = taskManager.getTaskDefs();

		List<TaskDefVO> tdvos = new ArrayList<TaskDefVO>();

		for (TaskDef taskDef : taskDefs) {

			if (!taskDef.isVisible()) {
				continue;
			}

			TaskDefVO tdvo = new TaskDefVO();
			tdvo.setId("" + taskDef.getId());
			tdvo.setTitle(taskDef.getTitle());
			tdvo.setShortDescription(taskDef.getShortDescription());
			tdvo.setType(taskDef.getType());
			tdvo.setStopped(taskDef.isStopped());
			tdvo.setActive(taskDef.isActive());
			tdvo.setMaxPoints(taskDef.getReachablePoints());
			tdvos.add(tdvo);
		}
    return tdvos;
  }


}
