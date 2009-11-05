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

import de.thorstenberger.examServer.webapp.vo.TaskDefVO;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletContainer;
import de.thorstenberger.taskmodel.Tasklet.Status;

/**
 * @author Thorsten Berger
 * 
 */
public class TutorMainAction extends BaseAction {

    /**
     * Count all tasklets for the given taskdef id, that are solved but not
     * corrected yet.
     * 
     * @param id
     * @param taskletContainer
     * @return
     * @throws TaskApiException
     */
    private int countNumberOfUncorrectedTasks(final long id, final TaskletContainer tc) throws TaskApiException {
        int num = 0;
        for (final Tasklet t : tc.getTasklets(id)) {
            if (t.getStatus() == Status.SOLVED) {
                num++;
            }
        }
        return num;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        final TaskManager taskManager = (TaskManager) getBean("TaskManager");

        final List<TaskDef> taskDefs = taskManager.getTaskDefs();

        final List<TaskDefVO> tdvos = new ArrayList<TaskDefVO>();

        final TaskletContainer taskletContainer = (TaskletContainer) getBean("TaskletContainer");
        for (final TaskDef taskDef : taskDefs) {

            final TaskDefVO tdvo = new TaskDefVO();
            tdvo.setId("" + taskDef.getId());
            tdvo.setTitle(taskDef.getTitle());
            tdvo.setShortDescription(taskDef.getShortDescription());
            tdvo.setType(taskDef.getType());
            // if( taskDef.getDeadline() != null )
            // tdvo.setDeadline( DateUtil.getStringFromMillis(
            // taskDef.getDeadline() ) );
            tdvo.setStopped(taskDef.isStopped());
            tdvo.setActive(taskDef.isActive());
            tdvo.setVisible(taskDef.isVisible());
            tdvo.setNumberOfOpenCorrections(countNumberOfUncorrectedTasks(taskDef.getId(), taskletContainer));
            tdvos.add(tdvo);
        }

        request.setAttribute("TaskDefs", tdvos);
        // FIXME sorting with displaytag doesn't work...
        return mapping.findForward("success");
    }

}
