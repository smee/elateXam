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
package de.thorstenberger.examServer.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLEncoder;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.impl.TaskModelViewDelegateObjectImpl;

/**
 * Helper class that can be used to render a pdf for a complextask. PDFExporter assumes that this webapp is running
 * within tomcat with enabled JMX beans. They get queried for a locally accessible http connector without ssl
 * encryption. This happens because the default usage of the examserver should include ssl with client certificates.
 * Because we are using a http request ourselves we would need to have access to these client certificates as well.
 *
 * @author Steffen Dienst
 *
 */
public class PDFExporter {
  protected final static Log log = LogFactory.getLog(PDFExporter.class);

  private final String sessionId;
  private final UserManager usermanager;
  private final TaskManager taskmanager;
  private HttpMethod getPdfMethod;
  private HttpClient httpclient;
  private boolean mockSession;

  /**
   * Default constructor.
   *
   * @param sessionId
   * @param um
   * @param tm
   */
  public PDFExporter(final String sessionId, final UserManager um, final TaskManager tm) {
    this.sessionId = sessionId;
    this.usermanager = um;
    this.taskmanager = tm;

    // FIXME hard coded context path :(
    init(getServerUrl() + "/taskmodel-core-view/showSolution.do", sessionId);
  }

  /**
   * Render pdf outside of a running web session. This relies on a hack: It uses a random mock sessionId as identifier
   * for {@link TaskModelViewDelegate}. This works for taskmodel-core-view's ShowSolutionAction.java only!
   *
   * @param um
   * @param tm
   */
  public PDFExporter(final UserManager um, final TaskManager tm) {
    this(RandomStringUtils.randomAlphanumeric(40), um, tm);
    this.mockSession = true;
  }

  private void init(final String url, final String sessionId) {
    log.trace("url for rendering pdfs: " + url);

    httpclient = new HttpClient();
    getPdfMethod = new GetMethod(url);
    // reuse our current session to make sure the view is able to receive our taskdef
    getPdfMethod.setRequestHeader("Cookie", "JSESSIONID=" + sessionId);

  }

  /**
   * use jmx, query catalina mbean to find non ssl connectors (-Dcom.sun.management.jmxremote) FIXME tomcat specific
   * code
   *
   * @param request
   * @return
   */
  private String getServerUrl() {

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
    log.warn("No mbeans of type '*:type=Connector,*' configured, using default url (assuming non-ssl http connector on port 8080).");
    return "http://localhost:8080";
  }

  /**
   * Check if we can access taskmodel-core-view without https/client certificate. That means, make sure, that we are
   * able to access the view application to render our pdfs.
   *
   * @param request
   * @return
   */
  public boolean isAvailableWithoutCertificate() {
    return getServerUrl() != null;
  }

  /**
   * @param tasklet
   * @param filename
   * @param ostream
   */
  public boolean renderPdf(final Tasklet tasklet, final String filename, final OutputStream ostream) {
    final long taskId = tasklet.getTaskId();
    final String userId = tasklet.getUserId();
    log.trace(String.format("rendering pdf for user='%s' and taskid='%d'", userId, taskId));

    // set delegate object for taskmodel-core-view
    final User user = usermanager.getUserByUsername(userId);
    final TaskModelViewDelegateObject delegateObject = new TaskModelViewDelegateObjectImpl(taskId,
        taskmanager, userId, user.getFirstName() + " " + user.getLastName(), "");

    TaskModelViewDelegate.storeDelegateObject(sessionId, taskId, delegateObject);
    // set request parameters
    String queryString = String.format("id=%d&exportToPdf=%s", taskId, filename);
    // if we do not want to reference the delegate object via our real sessionId, we should tell
    // the SaveAction.java of taskmodel-core-view so
    if (mockSession) {
      queryString += "&mockSessionId=" + sessionId;
    }
    queryString = encode(queryString);
    log.trace("querystring= " + queryString);

    getPdfMethod.setQueryString(queryString);
    try {
      final int responseCode = httpclient.executeMethod(getPdfMethod);
      if (responseCode < 400) {
        ostream.write(getPdfMethod.getResponseBody());
        return true;
      } else {
        log.warn("Could not render pdf, got http response code " + responseCode);
        return false;
      }
    } catch (final HttpException e) {
      log.error("Could not create pdf.", e);
      e.printStackTrace();
    } catch (final IOException e) {
      log.error("Could not create pdf.", e);
      e.printStackTrace();
    } finally {
      TaskModelViewDelegate.removeSession(sessionId);
      getPdfMethod.releaseConnection();
    }
    return false;
  }

  /**
   * Encode http query string using UTF8 encoding. See http://www.w3.org/TR/html40 for details.
   * 
   * @param queryString
   * @return
   */
  private String encode(final String queryString) {
    try {
      return URLEncoder.encode(queryString, "UTF8");
    } catch (final UnsupportedEncodingException e) {
      log.warn("Could not encode http query string '" + queryString + "' with UTF8 encoding. This error should never happen.");
      // return unencoded query
      return queryString;
    }
  }
}
