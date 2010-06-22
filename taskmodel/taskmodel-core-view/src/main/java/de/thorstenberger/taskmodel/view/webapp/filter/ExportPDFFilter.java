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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * Adjusted from org.displaytag.filter.ResponseOverrideFilter. ServletFilter that captures html, converts it to pdf.
 *
 * @author sdienst
 */
public class ExportPDFFilter implements Filter {
    public static final String EXPORTFILENAME = "exportToPdf";
    /**
     * Default xslt that doesn't change anything.
     */
    private static final String NOOP_XSLT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
            "<!-- per default copy everything -->\n" +
            "  <xsl:template match=\"@*|node()\">\n" +
            "    <xsl:copy>\n" +
            "      <xsl:apply-templates select=\"@*|node()\" />\n" +
            "    </xsl:copy>\n" +
            "  </xsl:template>\n" +
            "</xsl:stylesheet>";
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ExportPDFFilter.class);
    private String fontPath;

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

            final Document dom = getRenderedXhtml(new String(wrapper.getBinaryContent(),Charset.forName("UTF8")));

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
    public Document getRenderedXhtml(final String html) {
        // convert HTML to xHTML

        final Tidy tidy = new Tidy();
        tidy.setXHTML(true); // output pure xhtml
        tidy.setQuiet(true); // suppress verbose messages
    tidy.setShowWarnings(false);// suppress warnings
        /*
         * wrap javascript in strings to prevent parsing errors later on
         */
        tidy.setWrapScriptlets(true);
        tidy.setBreakBeforeBR(true);// line wrap on br
        tidy.setCharEncoding(Configuration.UTF8);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Charset utf8 = Charset.forName("UTF8");
        final Document xhtml = tidy.parseDOM(new ByteArrayInputStream(html.getBytes(utf8)), baos);

    try {
      return processDocument(xhtml, baos.toString("UTF8"));
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    }

    /**
     * {@inheritDoc}
     */
    public void init(final FilterConfig filterConfig) {
        final String bufferParam = filterConfig.getInitParameter("buffer");
        if (log.isDebugEnabled()) {
            log.debug("bufferParam=" + bufferParam);
        }
        this.fontPath = filterConfig.getServletContext().getRealPath("/WEB-INF/lsansuni.ttf");
        if (this.fontPath == null) {
          log.error("Could not locate lsansuni.ttf font file, is needed for utf-8 glyphs in PDFs!");
        }
        log.info("Filter initialized.");
    }

    /**
     * <ul>
     * <li>Strip &lt;script&gt; elements, because xml characters within these tags lead to invalid xhtml.</li>
     * <li>Xhtmlrenderer does not render form elements right (will probably support real PDF forms in the future), so we
     * need to replace select boxes with simple strings: Replace each select box with a bold string containing the
     * selected option.</li>
     * <li>Replace every input checkbox with [ ] for unchecked or [X] for checked inputs.</li>
     * </ul>
     *
     * @param xhtml
     *            xhtml as {@link Document}
     * @param xhtmlText
     *            string representation of the xhtml parameter
     * @return
     */
    private Document processDocument(final Document xhtml, final String xhtmlText) {
        final String xslt = readFile(this.getClass().getResourceAsStream("adjustforpdfoutput.xslt"));
        final Source xsltSource = new StreamSource(new StringReader(xslt));
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            final DOMResult result = new DOMResult();
            transformer.transform(new DOMSource(xhtml), result);
            return (Document) result.getNode();
        } catch (final TransformerConfigurationException e) {
            log.error("Internal: Wrong xslt configuration", e);
        } catch (final TransformerFactoryConfigurationError e) {
            log.error("Internal: Wrong xslt configuration", e);
        } catch (final TransformerException e) {
            log.error("Internal: Could not strip script tags from xhtml", e);
            e.printStackTrace();
        }
        // fall through in error case: return untransformed xhtml
        return xhtml;
    }

    /**
     * Read filecontents from the given stream.
     *
     * @param resourceAsStream
     * @return
     */
    private String readFile(final InputStream in) {
        if (in == null) {
          log.warn("Could not read xslt transformation instruction from classpath! Won't transform the xhtml.");
          return NOOP_XSLT;
        }

        final StringBuilder sb = new StringBuilder();
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (final IOException e) {
            log.warn("Couldn't load xslt from classpath, can not transform xhtml!", e);
            return NOOP_XSLT;
        }
        return sb.toString();
    }

    /**
     * Render the given xhtml document as pdf and write it to the response outputstream.
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
    System.out.println(fontPath);
    final ITextRenderer renderer = new ITextRenderer(80 / 3f, 15);
    try {
      // FIXME find absolute path to font file
      renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    } catch (DocumentException e1) {
      e1.printStackTrace();
    }
    renderer.setDocument(dom, getLocalURL(request));
    renderer.layout();

    response.setContentType("application/pdf");
    // set an appropriate filename
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

  /**
   * Try to find a nonssl connector to retrieve shared resources like stylesheets, images etc. Fallback: Use request
   * url.
   *
   * @param request
   * @return
   */
  private String getLocalURL(final HttpServletRequest request) {

    final MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
    try {
      for (final Object mbean : beanServer.queryMBeans(new ObjectName("*:type=Connector,*"), null)) {
        final ObjectInstance oi = (ObjectInstance) mbean;
        final Boolean isSecure = (Boolean) beanServer.getAttribute(oi.getObjectName(), "secure");
        final String protocol = (String) beanServer.getAttribute(oi.getObjectName(), "protocol");
        final int port = (Integer) beanServer.getAttribute(oi.getObjectName(), "port");
        if (!isSecure && protocol.startsWith("HTTP")) {
          log.debug(String.format("Using unsecured tomcat connector at port %d", port));
          return "http://localhost:" + port;
        }
      }
    } catch (final MalformedObjectNameException e) {
      log.warn("Could not access JMX mbeans.", e);
    } catch (final NullPointerException e) {
      log.warn("Could not access JMX mbeans.", e);
    } catch (final AttributeNotFoundException e) {
      log.warn("Could not access JMX mbeans.", e);
    } catch (final InstanceNotFoundException e) {
      log.warn("Could not access JMX mbeans.", e);
    } catch (final MBeanException e) {
      log.warn("Could not access JMX mbeans.", e);
    } catch (final ReflectionException e) {
      log.warn("Could not access JMX mbeans.", e);
    }
    String requestURL = request.getRequestURL().toString();
    log.warn("No mbeans of type '*:type=Connector,*' configured, using request url (assuming non-ssl): " + requestURL);
    return requestURL;
  }
}