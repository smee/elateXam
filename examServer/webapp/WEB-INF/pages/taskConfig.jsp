<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="taskConfig.title"/></title>
    <content tag="heading"><fmt:message key="taskConfig.heading"/></content>
    <meta name="menu" content="MainMenu"/>
</head>

<div class="separator">
Hier konfigurieren Sie die ausgew&auml;lte Aufgabe.
</div>

<br/><br/>

<html:form action="/saveTask">
<html:hidden property="id"/>
<html:hidden property="type"/>
<table border="0">
<tr>
<td>Titel: </td><td><html:text property="title" size="60"/></td>
</tr>
<tr>
<td>Kurzbeschreibung: </td><td><html:text property="shortDescription" size="60"/></td>
</tr>
<tr>
<td>Zeige die Bewertungen an:</td><td><html:checkbox property="showSolutionToStudents"/></td>
</tr>
<tr>
<td>Aufgabe inaktiv:</td><td><html:checkbox property="stopped"/></td>
</tr>
</table>
<br/><br/>
<html:submit>Speichern</html:submit>
</html:form>