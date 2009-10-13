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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.impl.TaskModelViewDelegateObjectImpl;

/**
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
            // show an error message if tomcat isn't configured appropriately
            if (!isAvailableWithoutCertificate(request)) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("invalid.serverconfig"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            // set response headers to declare pdf content type
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + getBulkFilename(td));
            // locate the taskdef
            TaskDef_Complex ctd;
            try {
                ctd = (TaskDef_Complex) td;
            } catch (final ClassCastException e) {
                throw new RuntimeException("invalid type: \"" + td.getType() + "\", " + e.getMessage());
            }
            // make sure the core-view really prints the task
            ctd.setShowCorrectionToUsers(true);

            // write headers, start streaming to the client
            response.flushBuffer();

            // get all tasklets for the given taskdef
            final List<Tasklet> tasklets = tm.getTaskletContainer().getTasklets(taskId);
            log.info(String.format("Exporting %d pdfs for taskdef \"%s\"", tasklets.size(), ctd.getTitle()));

            renderAllPdfs(tasklets, response.getOutputStream(), request, taskId, userManager, tm);
            return null;
        } else {

            throw new RuntimeException("unsupported type: \"" + td.getType() + "\"");

        }

    }

    private String getBulkFilename(final TaskDef td) {
        return td.getTitle().replaceAll("\\s", "_") + ".zip";
    }

    /**
     * @param client
     * @param cookies
     */
    private String getJSessionId(final Cookie[] cookies) {
        for (final Cookie c : cookies) {
            if (c.getName().toUpperCase().equals("JSESSIONID")) {
                return c.getValue();
            }
        }
        return null;
    }

    private String getServerUrl(final HttpServletRequest request) {
        // use jmx, query catalina mbean to find non ssl connectors (-Dcom.sun.management.jmxremote)
        // FIXME tomcat specific code
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
            System.out.println("==================");
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
        return null;
    }

    /**
     * Check if we can access taskmodel-core-view without https/client certificate. That means, make sure, that we are
     * able to access the view application to render our pdfs.
     * 
     * @param request
     * @return
     */
    private boolean isAvailableWithoutCertificate(final HttpServletRequest request) {
        return getServerUrl(request) != null;
    }

    /**
     * Call the taskmodel-core-view application via http for every user, that has processed the given task. Streams a
     * zip archive with all rendered pdf files to <code>os</code>.
     * 
     * @param tasklets
     *            all tasklets to render
     * @param os
     *            the outputstream the zip shall be written to
     * @param request
     *            http request
     * @param taskId
     *            the id of the taskdef
     * @param userManager
     * @param tm
     * @throws IOException
     */
    private void renderAllPdfs(final List<Tasklet> tasklets, final OutputStream os, final HttpServletRequest request, final long taskId, final UserManager userManager, final TaskManager tm) throws IOException {
        // create zip with all generated pdfs
        final ZipOutputStream zos = new ZipOutputStream(os);
        // initialize web crawler
        final HttpClient http = new HttpClient();
        // use http GET request
        final HttpMethod getPdfMethod = new GetMethod(getServerUrl(request) + "/taskmodel-core-view/showSolution.do");
        // reuse our current session to make sure the view is able to receive our taskdef
        getPdfMethod.setRequestHeader("Cookie", "JSESSIONID=" + getJSessionId(request.getCookies()));
        // fetch pdf for every user/tasklet

        // render a pdf for every user that has a tasklet for the current task
        for (final Tasklet tasklet : tasklets) {
            final String userId = tasklet.getUserId();
            log.trace("exporting pdf for " + userId);

            // set delegate object
            final User user = userManager.getUserByUsername(userId);
            final TaskModelViewDelegateObject delegateObject = new TaskModelViewDelegateObjectImpl(taskId,
                    tm, userId, user.getFirstName() + " " + user.getLastName(), "");

            final String sessionId = request.getSession().getId();
            TaskModelViewDelegate.storeDelegateObject(sessionId, taskId, delegateObject);

            // add new zipentry (for next pdf)
            final String filename = userId + ".pdf";
            final ZipEntry ze = new ZipEntry(filename);
            zos.putNextEntry(ze);
            // fetch the generated pdf from taskmodel-core-view
            // set request parameters
            getPdfMethod.setQueryString(String.format("id=%d&exportToPdf=%s", taskId, filename));
            http.executeMethod(getPdfMethod);
            zos.write(getPdfMethod.getResponseBody());
            getPdfMethod.releaseConnection();
            zos.closeEntry();
        }
        zos.close();
    }
}
