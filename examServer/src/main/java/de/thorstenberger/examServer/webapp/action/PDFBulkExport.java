/*

Copyright (C) 2009 Steffen Dienst

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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.examServer.pdf.PDFExporter;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.Tasklet.Status;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;

/**
 * Render a pdf for every existing {@link Tasklet} of a {@link TaskDef} using the pdf filter of taskmodel-core-view.
 *
 * @author Steffen Dienst
 *
 */
public class PDFBulkExport extends BaseAction {
  @Override
  public ActionForward execute(
      final ActionMapping mapping,
      final ActionForm form,
      final HttpServletRequest request,
      final HttpServletResponse response)
  throws Exception {

    final ActionMessages errors = new ActionMessages();

    // locate the taskdef to use
    long taskId;
    try {
      taskId = Long.parseLong(request.getParameter("taskId"));
    } catch (final NumberFormatException e) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.parameter"));
      saveErrors(request, errors);
      return mapping.findForward("error");
    }

    final TaskManager tm = (TaskManager) getBean("TaskManager");
    final TaskDef td = tm.getTaskDef(taskId);
    final UserManager userManager = (UserManager) getBean("userManager");

    if (request.getUserPrincipal() == null) {
      throw new RuntimeException("Not logged in.");
    }
    // we only know how to handle complextasks yet
    if (td.getType().equals(TaskContants.TYPE_COMPLEX)) {
      // initialize web crawler
      final PDFExporter pdfExporter = new PDFExporter(userManager, tm);

      // show an error message if tomcat isn't configured appropriately
      if (!pdfExporter.isAvailableWithoutCertificate()) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.serverconfig"));
        saveErrors(request, errors);
        return mapping.findForward("error");
      }
      // set response headers to declare pdf content type
      response.setContentType("application/zip");
      response.setHeader("Content-Disposition", "attachment; filename=" + getBulkFilename(td));
      // locate the taskdef
      TaskDef_Complex ctd;
      try {
        ctd = (TaskDef_Complex) td;
      } catch (final ClassCastException e) {
        throw new RuntimeException("invalid type: \"" + td.getType() + "\", " + e.getMessage());
      }
        // write headers, start streaming to the client
        response.flushBuffer();

        // get all tasklets for the given taskdef
        final List<Tasklet> tasklets = tm.getTaskletContainer().getTasklets(taskId);
        log.info(String.format("Exporting %d pdfs for taskdef \"%s\"", tasklets.size(), ctd.getTitle()));

        renderAllPdfs(tasklets, response.getOutputStream(), pdfExporter);
        return null;
    } else {
      throw new RuntimeException("unsupported type: \"" + td.getType() + "\"");
    }

  }

  private String getBulkFilename(final TaskDef td) {
    return td.getTitle().replaceAll("\\s", "_") + ".zip";
  }

  /**
   * Call the taskmodel-core-view application via http for every user, that has processed the given task. Streams a zip
   * archive with all rendered pdf files to <code>os</code>.
   *
   * @param tasklets
   *          all tasklets to render
   * @param os
   *          the outputstream the zip shall be written to
   * @param pdfExporter
   * @throws IOException
   */
  private void renderAllPdfs(final List<Tasklet> tasklets, final OutputStream os, final PDFExporter pdfExporter) throws IOException {
    // create zip with all generated pdfs
    final ZipOutputStream zos = new ZipOutputStream(os);

    // fetch pdf for every user/tasklet
    // render a pdf for every user that has a tasklet for the current task
    for (final Tasklet tasklet : tasklets) {
      final String userId = tasklet.getUserId();

      if (!tasklet.hasOrPassedStatus(Status.INPROGRESS)) {
        log.info(String.format("Skipping PDF for user %s, last try has no contents.",userId));
        continue;
      }
      log.debug("exporting pdf for " + userId);

      // add new zipentry (for next pdf)
      if (!addGeneratedPDFS(tasklet, userId, zos)) {
        final String filename = userId + ".pdf";
        final ZipEntry ze = new ZipEntry(filename);
        zos.putNextEntry(ze);
        // fetch the generated pdf from taskmodel-core-view
        pdfExporter.renderPdf(tasklet, filename, zos);
        // close this zipentry
        zos.closeEntry();
      }
    }
    zos.close();
  }

  /**
   * @param tasklet
   * @param userId
   * @param zos
   * @return
   * @throws IOException
   */
  private boolean addGeneratedPDFS(final Tasklet tasklet, final String userId, final ZipOutputStream zos) throws IOException {
    final ExamServerManager esm = (ExamServerManager) getBean("examServerManager");
    boolean wroteAtLeastOneFile = false;
    // find all existing pdfs for this user and this taskId
    for (final File pdfFile : new File(esm.getHomeDir(), userId).listFiles(getPdfFileFilter(tasklet, userId))) {
      log.info(String.format("adding existing pdf '%s'", pdfFile.getName()));

      wroteAtLeastOneFile = true;
      // add pdf to zip
      final ZipEntry ze = new ZipEntry(pdfFile.getName());
      zos.putNextEntry(ze);
      final InputStream is = new FileInputStream(pdfFile);
      IOUtils.copy(is, zos);
      is.close();
    }
    return wroteAtLeastOneFile;
  }

  /**
   * @param tasklet
   * @param userId
   * @return
   */
  private FileFilter getPdfFileFilter(final Tasklet tasklet, final String userId) {
    return new FileFilter() {
      public boolean accept(final File pathname) {
        return pathname.getName().contains(userId + "-" + tasklet.getTaskId() + "-");
      }
    };
  }
}
