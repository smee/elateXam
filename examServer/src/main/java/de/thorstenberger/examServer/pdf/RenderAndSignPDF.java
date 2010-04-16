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
package de.thorstenberger.examServer.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.SignatureException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.lowagie.text.DocumentException;

import de.thorstenberger.examServer.pdf.signature.SignPdf;
import de.thorstenberger.examServer.pdf.signature.SignatureInfos;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;

/**
 * Background process for creating a pdf for a {@link ComplexTasklet}. Signs it using a private certificate and adds a
 * cryptographic timestamp to prove its creation date. See {@link SignPdf} for details.
 *
 * @author Steffen Dienst
 *
 */
public class RenderAndSignPDF implements Runnable {
  /**
   * FIXME There is a problem when using jcl in one webapp and slf4j in another AND using threads. Maybe we should start
   * to migrate the examserver completely to slf4j? <br/>
   * see http://articles.qos.ch/classloader.html <br/>
   * We'll use log4j instead of JCL in this case.
   */
  private final static Logger log = LogManager.getLogger(RenderAndSignPDF.class);

  private final String userId;
  private final long taskId;
  private final ApplicationContext applicationContext;

  /**
   * @param taskId
   * @param userId
   * @param applicationContext
   */
  public RenderAndSignPDF(final long taskId, final String userId, final ApplicationContext applicationContext) {
    this.taskId = taskId;
    this.userId = userId;
    this.applicationContext = applicationContext;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Runnable#run()
   */
  public void run() {
    // create a mock session id of lenght 30
    final UserManager um = getBean("userManager");
    final TaskManager tm = getBean("TaskManager");


    try {
      final Tasklet tasklet = tm.getTaskletContainer().getTasklet(taskId, userId);

      log.info(String.format("rendering, signing and timestamping pdf for user '%s' and taskId '%d', new status is '%s'", userId, taskId, tasklet.getStatus().getValue()));
      // create output file for this tasklet status and user
      final ExamServerManager esm = getBean("examServerManager");
      final File directory = new File(esm.getHomeDir(), userId);
      final String filename = userId + "-" + taskId + "-" + tasklet.getStatus() + ".pdf";

      final File tempFile = File.createTempFile("pdf", "pdf");
      tempFile.deleteOnExit();
      final FileOutputStream fos = new FileOutputStream(tempFile);
      // create pdf
      final PDFExporter pdfExporter = new PDFExporter(um, tm);
      pdfExporter.renderPdf(tasklet, filename, fos);
      fos.close();
      // create unique filename, prevent overwriting existing files
      File destFile = new File(directory, filename);
      int counter = 0;
      while (destFile.exists()) {
        destFile = new File(directory, String.format("version-%d-%s", counter, filename));
        counter++;
      }
      log.info("Using filename " + destFile.getName());
      if (!signAndTimestamp(tempFile, destFile)) {
        log.warn("An error occured on signing/timestamping the pdf, using unsigned pdf instead.");
        FileUtils.copyFile(tempFile, destFile);
      }
      // cleanup
      tempFile.delete();
    } catch (final TaskApiException e) {
      log.error(String.format("Could not create pdf for user=%s and taskId=%d", userId, taskId), e);
    } catch (final FileNotFoundException e) {
      log.error(String.format("Could not create pdf for user=%s and taskId=%d", userId, taskId), e);
    } catch (final IOException e) {
      log.error(String.format("Could not create pdf for user=%s and taskId=%d", userId, taskId), e);
    }
  }

  /**
   * Fetch a timestamp from a remote timestamp server and sign the pdf with our private key.
   *
   * @param pdfIn
   * @param pdfOut
   * @return
   */
  private boolean signAndTimestamp(final File pdfIn, final File pdfOut) {
    final SignatureInfos infos = ((ConfigManager) getBean("configManager")).getPDFSignatureInfos();
    FileInputStream instream = null;
    FileOutputStream outstream = null;
    try {
      instream = new FileInputStream(pdfIn);
      outstream = new FileOutputStream(pdfOut);
      // sign and timestamp via SignPdf.java
      SignPdf.signAndTimestamp(instream, outstream, infos);
      return true;
    } catch (final FileNotFoundException e) {
      log.error("Could not find file.", e);
    } catch (final SignatureException e) {
      log.error("Could not sign pdf.", e);
    } catch (final KeyStoreException e) {
      log.error("Could not read key or certificate from keystore.", e);
    } catch (final IOException e) {
      log.error("Error on file access.", e);
    } catch (final DocumentException e) {
      log.error("Could not read pdf.", e);
    }finally{

      try {
      if(instream!=null) {
          instream.close();
      }
      if(outstream!=null) {
        outstream.close();
      }
      } catch (final IOException e) {
        log.warn("Could not close filestreams to pdf files.", e);
      }
    }
    return false;
  }

  /**
   * @param name
   * @return
   */
  <T> T getBean(final String name) {
    try {
    return (T) applicationContext.getBean(name);
    } catch (final BeansException be) {
      log.error(String.format("Could not fetch bean named '%s'!", name), be);
      return null;
    }
  }

}