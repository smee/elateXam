<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="systemConfig.title"/></title>
    <content tag="heading"><fmt:message key="systemConfig.heading"/></content>
    <meta name="menu" content="MainMenu"/>
</head>

<div class="separator">
Hier konfigurieren Sie allgemeine Einstellungen des Pr�fungsservers.
</div>

<br/><br/>

<html:form action="saveSystemConfig">
<table border="0">
<tr>
<td>Titel: </td><td><html:text property="title" size="60"/></td>
</tr>
<tr>
<td>WebService-URL des RemoteUserManager: </td><td><html:text property="remoteUserManagerURL" size="60"/></td>
</tr>
<tr>
<td>Java-Plugin bereits auf der Login-Seite starten </td><td><html:checkbox property="loadJVMOnStartup"/>&nbsp;&nbsp;Applet auf der Login-Seite startet das Java-Plugin und zeigt bei Erfolg ein H�kchen an</td>
</tr>
<tr>
<td>HTTP-Authentication-URL: </td><td><html:text property="httpAuthURL" size="60"/></td>
</tr>
<tr>
<td>HTTP-Authentication-Mail-Suffix: </td><td><html:text property="httpAuthMail" size="60"/></td>
</tr>
</table>
<br/><br/>
<html:submit>Speichern</html:submit>
</html:form>
<br/>
Archivieren aller Serverdaten: <html:link action="/ArchiveServer">Archiv erstellen</html:link>