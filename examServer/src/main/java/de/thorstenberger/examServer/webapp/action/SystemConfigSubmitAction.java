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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import de.thorstenberger.examServer.pdf.signature.SignPdf;
import de.thorstenberger.examServer.service.ConfigManager;
import de.thorstenberger.examServer.webapp.form.SystemConfigForm;

/**
 * @author Thorsten Berger
 *
 */
public class SystemConfigSubmitAction extends BaseAction {

	/* (non-Javadoc)
	 * @see de.thorstenberger.examServer.webapp.action.BaseAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {


		final SystemConfigForm scf = (SystemConfigForm)form;
		final ConfigManager configManager = (ConfigManager)getBean( "configManager" );

		configManager.setTitle( scf.getTitle() );
		configManager.setRemoteUserManagerURL( scf.getRemoteUserManagerURL() );
		configManager.setLoadJVMOnStartup( scf.isLoadJVMOnStartup() );
		configManager.setHTTPAuthURL( scf.getHttpAuthURL() );
		configManager.setHTTPAuthMail( scf.getHttpAuthMail() );
		configManager.setRadiusHost( scf.getRadiusHost() );
		configManager.setRadiusSharedSecret( scf.getRadiusSharedSecret() );
		configManager.setPDFSignatureInfos( scf.getSignatureSettings() );

		configManager.toggleFlag( "askForSemester", scf.isAskForStudentDetails() );

    if ("Signatur testen".equals(scf.getTodo())) {
      return createSignedPDF(mapping, request, response, configManager);
    }

    final ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage( "systemConfig.saved" ) );
		saveMessages( request, messages );

		return mapping.findForward( "success" );
	}

  /**
   * @param mapping
   * @param request
   * @param response
   * @param configManager
   * @return
   */
  private ActionForward createSignedPDF(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response, final ConfigManager configManager) {
    final ActionErrors errors = new ActionErrors();
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();

      final Document pdf = new Document();
      PdfWriter.getInstance(pdf, baos);
      pdf.open();
      pdf.add(new Paragraph("Signaturtest."));
      pdf.close();

      final InputStream pdfIn = new ByteArrayInputStream(baos.toByteArray());
      baos.reset();
      SignPdf.signAndTimestamp(pdfIn, baos, configManager.getPDFSignatureInfos());
      // write signed pdf to response
      response.setContentType("application/pdf");
      // set an appropriate filename
      response.setHeader("Content-Disposition", "attachment; filename=signaturetest.pdf");
      response.getOutputStream().write(baos.toByteArray());
      return null;
    } catch (final DocumentException e) {
      addError(errors, e);
    } catch (final IOException e) {
      addError(errors, e);
    } catch (final SignatureException e) {
      addError(errors, e);
    } catch (final KeyStoreException e) {
      addError(errors, e);
    }
    saveMessages(request, errors);
    return mapping.findForward("success");
  }

  /**
   * @param errors
   * @param e
   */
  private void addError(final ActionErrors errors, final Exception e) {
    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("systemConfig.signature.error", e.getMessage()));
    log.warn("Could not test pdf signature settings.", e);
  }

}
