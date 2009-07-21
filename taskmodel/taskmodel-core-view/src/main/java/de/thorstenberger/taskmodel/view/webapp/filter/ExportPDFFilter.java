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
package de.thorstenberger.taskmodel.view.webapp.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import com.lowagie.text.DocumentException;

/**
 * Adjusted from org.displaytag.filter.ResponseOverrideFilter. ServletFilter
 * that captures html, converts it to pdf.
 * 
 * @author sdienst
 */
public class ExportPDFFilter implements Filter {
    public static final String EXPORTFILENAME = "exportToPdf";
    /**
     * Logger.
     */
    private Log log;

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        // nothing to destroy
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        if (servletRequest.getParameter(EXPORTFILENAME) == null) {
            // don't filter!
            filterChain.doFilter(servletRequest, servletResponse);
        } else {

            // create a buffer for the rendered contents
            final ContentCaptureServletResponse wrapper = new ContentCaptureServletResponse((HttpServletResponse) servletResponse);
            // call the rest of the filter chain
            filterChain.doFilter(servletRequest, wrapper);

            final Document dom = parseXhtml(getRenderedXhtml(wrapper.getContent()));

            // call flying saucer to render xHtml to pdf
            renderPdf(dom, (HttpServletRequest) servletRequest, servletResponse);
        }
    }

    /**
     * Tidy up the html and convert it into standard conform XHTML.
     * 
     * @param html
     *            html document
     * @return equivalent xhtml document
     */
    public String getRenderedXhtml(final String html) {
        // convert HTML to xHTML

        final Tidy tidy = new Tidy();
        tidy.setXHTML(true); // output pure xhtml
        tidy.setQuiet(true); // suppress verbose messages
        tidy.setShowWarnings(false);// suppress warnings
        /*
         * wrap javascript in strings to prevent parsing errors later on
         */
        tidy.setWrapScriptlets(true);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tidy.parse(new ByteArrayInputStream(html.getBytes()), baos);

        return baos.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void init(final FilterConfig filterConfig) {
        log = LogFactory.getLog(ExportPDFFilter.class);
        final String bufferParam = filterConfig.getInitParameter("buffer");
        if (log.isDebugEnabled()) {
            log.debug("bufferParam=" + bufferParam);
        }
        log.info("Filter initialized.");
    }

    /**
     * Parse valid xhtml.
     * 
     * @param xhtml
     * @return
     */
    private Document parseXhtml(final String xhtml) {
        // FIXME xhtmlrenderer crashes on '<' or '>' characters within
        // CDATA, for example script tags
        final InputSource is = new InputSource(new BufferedReader(new StringReader(xhtml)));
        final Document dom = XMLResource.load(is).getDocument();
        return dom;
    }

    /**
     * Render the given xhtml document as pdf and write it to the response
     * outputstream.
     * 
     * @param dom
     *            xhtml document
     * @param request
     * @param response
     * @throws IOException
     *             if the document could not get renderered
     */
    private void renderPdf(final Document dom, final HttpServletRequest request,
            final ServletResponse response) throws IOException {
        final ITextRenderer renderer = new ITextRenderer(80 / 3f, 15);
        renderer.setDocument(dom, HttpUtils.getRequestURL(request).toString());
        renderer.layout();

        response.setContentType("application/pdf");
        // set a appropriate filename
        ((HttpServletResponse) response).setHeader("Content-Disposition", "attachment; filename="
                + request.getParameter(EXPORTFILENAME));

        try {
            renderer.createPDF(response.getOutputStream());
        } catch (final DocumentException e) {
            log.error("Could not render pdf.", e);
            // FIXME should we just return the original content?
            throw new IOException(e.getMessage());
        }
    }
}