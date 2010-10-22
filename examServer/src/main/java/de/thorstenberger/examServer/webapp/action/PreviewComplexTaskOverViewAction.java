/*

Copyright (C) 2010 Steffen Dienst

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
package de.thorstenberger.examServer.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskletContainer;

/**
 * Return a readonly instance of taskmanager to enable previewing of complextasks without persisting tasklets.
 *
 * @author Steffen Dienst
 *
 */
public class PreviewComplexTaskOverViewAction extends ComplexTaskOverviewAction {

    /**
     *
     */
    public PreviewComplexTaskOverViewAction() {
    }

    @Override
    protected TaskManager getTaskManager() {
        return (TaskManager) getBean("PreviewTaskManager");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaskletContainer tc = (TaskletContainer) getBean("PreviewTaskletContainer");
        // reset the cache each time.
        tc.reset();
        return super.execute(mapping, form, request, response);
    }

    @Override
    protected String createReturnUrl(HttpServletRequest request, long taskId) {
        return request.getContextPath() + "/PreviewTaskViewFactory.html?taskId=" + taskId;
    }

}
