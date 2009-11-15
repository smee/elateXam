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
package de.thorstenberger.taskmodel.view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelServices;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;

/**
 * @author Thorsten Berger
 * 
 */
public class SavePageAction extends Action {

    public static void logPostData(final HttpServletRequest request, final Tasklet tasklet) {
        final Map vars = request.getParameterMap();
        final StringBuffer parameters = new StringBuffer();
        final Iterator keys = vars.keySet().iterator();
        while (keys.hasNext()) {
            final String key = (String) keys.next();
            parameters.append(key + "=" + ((String[]) vars.get(key))[0] + "\n");
        }
        tasklet.logPostData("posted parameters:\n" + parameters.toString(), request.getRemoteAddr());
    }

    private final Log log = LogFactory.getLog(SavePageAction.class);

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

        final ActionMessages msgs = new ActionMessages();
        final ActionMessages errors = new ActionMessages();

        int page;
        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
            page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        } catch (final NumberFormatException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.parameter"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        final TaskModelViewDelegateObject delegateObject = (TaskModelViewDelegateObject) TaskModelViewDelegate.getDelegateObject(
                request.getSession().getId(), id);
        if (delegateObject == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("no.session"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        request.setAttribute("ReturnURL", delegateObject.getReturnURL());

        ComplexTasklet ct;

        try {
            ct = (ComplexTasklet) delegateObject.getTasklet();
        } catch (final ClassCastException e1) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("only.complexTasks.supported"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } catch (final TaskApiException e3) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("misc.error", e3.getMessage()));
            saveErrors(request, errors);
            log.error(e3);
            return mapping.findForward("error");
        }

        logPostData(request, ct);

        TaskDef_Complex taskDef;
        try {
            taskDef = (TaskDef_Complex) delegateObject.getTaskDef();
        } catch (final ClassCastException e2) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("only.complexTasks.supported"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } catch (final TaskApiException e3) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("misc.error", e3.getMessage()));
            saveErrors(request, errors);
            log.error(e3);
            return mapping.findForward("error");
        }

        if (!taskDef.isActive()) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("task.inactive"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        try {

            final long hashCode = Long.parseLong(request.getParameter("hashCode"));

            // ...and the following too:
            final List<SubTasklet> subtasklets = ct.getComplexTaskHandlingRoot().getRecentTry().getPage(page).getSubTasklets();

            final List<SubmitData> submitDataList = getSubmitData(request, subtasklets);

            ct.savePage(page, submitDataList, hashCode);
            processInteractiveTasklets(ct, subtasklets, submitDataList, request);

            return mapping.findForward("success");

        } catch (final IllegalStateException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
            saveErrors(request, errors);
            log.info(e);
            return mapping.findForward("error");
        } catch (final ParsingException e1) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("parsing.error"));
            saveErrors(request, errors);
            log.error("Parsing error!", e1);
            return mapping.findForward("error");
        } catch (final NumberFormatException e2) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.parameter"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

    }

    /**
     * task[111]
     * 
     * @param varName
     * @return
     */
    private int getRelativeTaskNo(final String varName) throws ParsingException {
        try {
            return Integer.parseInt(
                    varName.substring(5, varName.indexOf(']')));
        } catch (final NumberFormatException e) {
            throw new ParsingException(e);
        }
    }

    private List<SubmitData> getSubmitData(final HttpServletRequest request, final List<SubTasklet> subtasklets)
            throws ParsingException {
        try {

            final Enumeration varNames = request.getParameterNames();

            // wir erwarten Variablen zu den entsprechenden Tasks
            // wenn das nicht �bereinstimmt, dann ArrayIndexOutOfBoundsException
            final Map[] taskVarMaps = new Map[subtasklets.size()];
            for (int i = 0; i < taskVarMaps.length; i++) {
                taskVarMaps[i] = new HashMap();
            }

            while (varNames.hasMoreElements()) {
                final String varName = (String) varNames.nextElement();

                if (varName.startsWith("task[")) {

                    final int relativeTaskNo = getRelativeTaskNo(varName);

                    if (relativeTaskNo >= 0) {
                        taskVarMaps[relativeTaskNo].
                                put(varName, request.getParameter(varName));
                    }

                }
            }

            final List<SubmitData> ret = new ArrayList<SubmitData>(subtasklets.size());

            for (int i = 0; i < subtasklets.size(); i++) {
                ret.add(i, TaskModelServices.getInstance().getSubTaskView(subtasklets.get(i)).getSubmitData(taskVarMaps[i]));
            }

            return ret;

        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ParsingException(e);
        }

    }

    private void processInteractiveTasklets(final ComplexTasklet ct, final List<SubTasklet> subtasklets,
            final List<SubmitData> submitDatas, final HttpServletRequest request) {
        for (int i = 0; i < subtasklets.size(); i++) {
            final SubTasklet subTasklet = subtasklets.get(i);

            if (request.getParameterMap().containsKey("doAutoCorrection_" + subTasklet.getVirtualSubtaskNumber())) {
                // FIXME: add TaskModelSecurityException being subclassed from
                // SecurityException
                if (!subTasklet.isInteractiveFeedback()) {
                    throw new SecurityException("No interactive feedback allowed for SubTaskDef " + subTasklet.getSubTaskDefId());
                }
                ct.doInteractiveFeedback(subTasklet, submitDatas.get(i));
            }
        }

    }

}
