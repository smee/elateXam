<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="systemConfig.title"/></title>
    <content tag="heading"><fmt:message key="systemConfig.heading"/></content>
    <meta name="menu" content="MainMenu"/>
</head>

<div class="separator">
Hier konfigurieren Sie allgemeine Einstellungen des Pr&uuml;fungsservers.
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
<td>Java-Plugin bereits auf der Login-Seite starten </td><td><html:checkbox property="loadJVMOnStartup"/>&nbsp;&nbsp;Applet auf der Login-Seite startet das Java-Plugin und zeigt bei Erfolg ein H&auml;kchen an</td>
</tr>
<tr>
<td>HTTP-Authentication-URL: </td><td><html:text property="httpAuthURL" size="60"/></td>
</tr>
<tr>
<td>HTTP-Authentication-Mail-Suffix: </td><td><html:text property="httpAuthMail" size="60"/></td>
</tr>
<tr>
<td>Radius-Authentication-Host: </td><td><html:text property="radiusHost" size="60"/></td>
</tr>
<tr>
<td>Radius-Shared-Secret: </td><td><html:password property="radiusSharedSecret" size="60"/></td>
</tr>
<tr>
<td>Radius-Mail-Suffixes (space delimited): </td><td><html:text property="radiusMailSuffixesDelimited" size="60"/></td>
</tr>
</table>
<h3>PDF-Einstellungen</h3>
<div class="separator">
Hier konfigurieren Sie Einstellungen f&uuml;r die PDF-Signaturen und Zeitstempel.
</div>
<br/><br/>
<table border="0">
<tr>
<td>URL des Zeitstempeldienstes: </td><td><html:text property="signatureSettings.timestampServerUrl" size="60"/></td>
</tr>
<tr>
<td>Signierungsort: </td><td><html:text property="signatureSettings.signatureLocation" size="60"/></td>
</tr>
<tr>
<td>EMailkontakt: </td><td><html:text property="signatureSettings.signatureContact" size="60"/></td>
</tr>
<tr>
<td>Signierungsgrund: </td><td><html:text property="signatureSettings.signatureReason" size="60"/></td>
</tr>
<tr>
<td>Pfad zum Keystore (serverseitig): </td><td><html:text property="signatureSettings.keystoreFile" size="60"/></td>
</tr>
<tr>
<td>Keystorepasswort: </td><td><html:password property="signatureSettings.keystorePassword" size="60"/></td>
</tr>
<tr>
<td>Alias der Zertifikatkette: </td><td><html:password property="signatureSettings.certificateChainAlias" size="60"/></td>
</tr>
<tr>
<td>Alias des Privatschl&uuml;ssels: </td><td><html:password property="signatureSettings.keyAlias" size="60"/></td>
</tr>
<tr>
<td>Privatschl&uuml;sselpasswort: </td><td><html:password property="signatureSettings.privateKeyPassword" size="60"/></td>
</tr>
<tr>
<td> <html:submit property="todo">Signatur testen</html:submit> </td>
</tr>
</table>
<br/><br/>
<html:submit>Speichern</html:submit>
</html:form>
<br/>
Archivieren aller Serverdaten: <html:link action="/ArchiveServer">Archiv erstellen</html:link>