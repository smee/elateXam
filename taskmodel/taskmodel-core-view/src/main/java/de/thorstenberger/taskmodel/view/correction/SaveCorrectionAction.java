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
package de.thorstenberger.taskmodel.view.correction;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.taskmodel.CorrectorDelegateObject;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.TaskModelServices;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ParsingException;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.CorrectionSubmitDataImpl;

/**
 * @author Thorsten Berger
 * 
 */
public class SaveCorrectionAction extends Action {

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

        final ActionMessages errors = new ActionMessages();

        long id;
        final String userId = request.getParameter("userId");
        final String selectedSubTaskletNum = request.getParameter("selectedSubTaskletNum");
        SubTasklet selectedSubTasklet = null;

        try {
            id = Long.parseLong(request.getParameter("taskId"));
        } catch (final NumberFormatException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.parameter"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        final CorrectorDelegateObject delegateObject = (CorrectorDelegateObject) TaskModelViewDelegate.getDelegateObject(request
                .getSession().getId(), id);

        if (delegateObject == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("no.session"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        final ComplexTasklet tasklet = (ComplexTasklet) delegateObject.getTaskManager().getTaskletContainer().getTasklet(id,
                userId);

        if (!delegateObject.isPrivileged()) {
            if (!delegateObject.getCorrectorLogin().equals(tasklet.getTaskletCorrection().getCorrector())) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("may.only.correct.assigned.tasklets"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
        }

        final List<Page> pages = tasklet.getSolutionOfLatestTry().getPages();
        final List<SubTasklet> subTasklets = new ArrayList<SubTasklet>();
        for (final Page page : pages) {
            final List<SubTasklet> sts = page.getSubTasklets();
            for (final SubTasklet subTasklet : sts) {
                subTasklets.add(subTasklet);
                if (subTasklet.getVirtualSubtaskNumber().equals(selectedSubTaskletNum)) {
                    selectedSubTasklet = subTasklet;
                }
            }

        }

        CorrectionSubmitData csd;

        try {
            csd = getCorrectionSubmitData(request, selectedSubTasklet);
        } catch (final ParsingException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("parsing.error"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        boolean correctSubtasklet = true;
        if (csd == null) {
            csd = new CorrectionSubmitDataImpl();
            correctSubtasklet = false;
        }

        // populate the CorrectionSubmitData with the annotation and the
        // corrector
        csd.setAnnotation(request.getParameter("annotation"));
        csd.setCorrector(delegateObject.getCorrectorLogin());

        try {
            tasklet.doManualCorrection(correctSubtasklet ? selectedSubTasklet : null, csd);
        } catch (final IllegalStateException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        return mapping.findForward("success");

    }

    private CorrectionSubmitData getCorrectionSubmitData(final HttpServletRequest request, final SubTasklet subtasklet)
            throws ParsingException {

        if (subtasklet == null) {
            return new CorrectionSubmitDataImpl();
        }

        final Enumeration varNames = request.getParameterNames();

        final Map subTaskVarMap = new HashMap();
        while (varNames.hasMoreElements()) {
            final String varName = (String) varNames.nextElement();
            if (varName.startsWith("task[0].")) {
                final String value = request.getParameter(varName);
                if (value != null && value.trim().length() > 0) {
                    subTaskVarMap.put(varName, request.getParameter(varName));
                }
            }
        }

        // no posted vars also imply correction data (e.g. none checkbox
        // activated etc.)
        try {
            return TaskModelServices.getInstance().getSubTaskView(subtasklet).getCorrectionSubmitData(subTaskVarMap);
        } catch (final MethodNotSupportedException e) {
            return null;
        }

    }

}
