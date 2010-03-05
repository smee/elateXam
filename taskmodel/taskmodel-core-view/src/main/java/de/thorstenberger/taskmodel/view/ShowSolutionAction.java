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
import java.util.LinkedList;
import java.util.List;

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

import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.StudentAnnotation;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelServices;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.view.SubTaskletInfoVO.Correction;

/**
 * @author Thorsten Berger
 *
 */
public class ShowSolutionAction extends Action {

    public static void populateVO(final SolutionInfoVO sivo, final TaskDef_Complex ctd, final ComplexTasklet ct) {

        sivo.setTaskId(ctd.getId());

        // set points
        if (ct.getTaskletCorrection().isCorrected()) {
            final List<SolutionInfoVO.Correction> taskletCorrections = new LinkedList<SolutionInfoVO.Correction>();
            if (ct.getTaskletCorrection().isAutoCorrected()) {
                taskletCorrections.add(sivo.new Correction(null, true, ct.getTaskletCorrection().getAutoCorrectionPoints()));
            } else {
                for (final ManualCorrection mc : ct.getTaskletCorrection().getManualCorrections()) {
                    taskletCorrections.add(sivo.new Correction(mc.getCorrector(), false, mc.getPoints()));
                }
            }
            sivo.setCorrections(taskletCorrections);
        }

        sivo.setStatus(ct.getStatus().getValue());
        try {
            sivo.setTryStartTime(DateUtil.getStringFromMillis(ct.getSolutionOfLatestTry().getStartTime()));
        } catch (final IllegalStateException e) {
            sivo.setTryStartTime("-");
        }

    }

    private final Log log = LogFactory.getLog(ShowSolutionAction.class);

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        final ActionMessages errors = new ActionMessages();

        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (final NumberFormatException e) {
            log.warn("missing taskid");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.parameter"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        final String sessionId = getSessionId(request);

        final TaskModelViewDelegateObject delegateObject = (TaskModelViewDelegateObject) TaskModelViewDelegate.getDelegateObject( sessionId, id);

        if (delegateObject == null) {
            log.warn(String.format("Could not find a DelegateObject for sessionId=%s and taskId=%d!",sessionId,id));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("no.session"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        request.setAttribute("ReturnURL", delegateObject.getReturnURL());

        TaskDef_Complex taskDef;
        try {
            taskDef = (TaskDef_Complex) delegateObject.getTaskDef();
        } catch (final ClassCastException e2) {
            log.warn("DelegateObject has no taskdef!");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("only.complexTasks.supported"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        } catch (final TaskApiException e3) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("misc.error", e3.getMessage()));
            saveErrors(request, errors);
            log.error(e3);
            return mapping.findForward("error");
        }

        if (!taskDef.isShowCorrectionToUsers() && request.getParameter("mockSessionId")==null) {
            log.warn("Trying to render taskdef solution that must not be shown to users!");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("solutions.not.shown"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        final SolutionInfoVO sivo = new SolutionInfoVO();

        sivo.setLogin(delegateObject.getLogin());
        sivo.setUserName(delegateObject.getUserName());

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

        if (ct == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("no.solution.available"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }

        Try latestTry;
        try {
            latestTry = ct.getSolutionOfLatestTry();
        } catch (final IllegalStateException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
            saveErrors(request, errors);
            log.info(e);
            return mapping.findForward("error");
        }

        // add the subtasklets
        final List<SubTaskletInfoVO> stivos = new ArrayList<SubTaskletInfoVO>();
        final List<Page> pages = latestTry.getPages();
        final ViewContext context = new HtmlViewContext(request);

        int i = 0;

        for (final Page page : pages) {

            final List<SubTasklet> subtasklets = page.getSubTasklets();

            for (final SubTasklet subTasklet : subtasklets) {
                final SubTaskletInfoVO stivo = new SubTaskletInfoVO();
                stivo.setHint(subTasklet.getHint());
                stivo.setProblem(ParserUtil.getProblem(subTasklet.getProblem()));
                stivo.setReachablePoints(subTasklet.getReachablePoints());
                stivo.setVirtualSubTaskletNumber(subTasklet.getVirtualSubtaskNumber());
                stivo.setRenderedHTML(TaskModelServices.getInstance().getSubTaskView(subTasklet).getCorrectedHTML(context, i++));
                stivo.setCorrected(subTasklet.isCorrected());
                stivo.setInteractiveFeedback(subTasklet.isInteractiveFeedback());

                if (subTasklet.isCorrected()) {
                    final List<Correction> corrections = new LinkedList<Correction>();
                    if (subTasklet.isAutoCorrected()) {
                        corrections.add(stivo.new Correction(null, true, subTasklet.getAutoCorrection().getPoints()));
                    } else {
                        for (final ManualSubTaskletCorrection msc : subTasklet.getManualCorrections()) {
                            corrections.add(stivo.new Correction(msc.getCorrector(), false, msc.getPoints()));
                        }
                    }
                    stivo.setCorrections(corrections);
                }
                stivo.setNeedsManualCorrectionFlag(subTasklet.isSetNeedsManualCorrectionFlag());
                stivos.add(stivo);
            }

        }

        populateVO(sivo, taskDef, ct);
        request.setAttribute("SubTasklets", stivos);
        request.setAttribute("Solution", sivo);

        if (ct.hasOrPassedStatus(Tasklet.Status.CORRECTED)) {
            sivo.setCanAnnotate(true);
            if (ct.getTaskletCorrection().getStudentAnnotations().size() > 0) {
                if (!ct.getTaskletCorrection().getStudentAnnotations().get(0).isAcknowledged()) {
                    sivo.setActualAnnotation(ct.getTaskletCorrection().getStudentAnnotations().get(0).getText());
                }
            }
        }

        final List<SolutionInfoVO.AnnotationInfoVO> annotations = new ArrayList<SolutionInfoVO.AnnotationInfoVO>();
        for (final StudentAnnotation anno : ct.getTaskletCorrection().getStudentAnnotations()) {
            if (anno.isAcknowledged()) {
                annotations.add(sivo.new AnnotationInfoVO(DateUtil.getStringFromMillis(anno.getDate()), ParserUtil.escapeCR(anno
                        .getText())));
            }
        }
        sivo.setAnnotations(annotations);

        return mapping.findForward("success");

    }

  /**
   * XXX ugly hack to be able to call this action without a current session.
   *
   * @param request
   * @return
   */
  private String getSessionId(final HttpServletRequest request) {
    String sessionId = request.getParameter("mockSessionId");
    if (sessionId == null) {
      sessionId = request.getSession().getId();
    }
    return sessionId;
  }

}
