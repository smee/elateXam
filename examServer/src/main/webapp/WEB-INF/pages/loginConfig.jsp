<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="loginConfig.title"/></title>
    <content tag="heading"><fmt:message key="loginConfig.heading"/></content>
    <meta name="menu" content="MainMenu"/>
</head>

<div class="separator">
Hier schalten Sie den Zugang zum Prüfungsserver für Studenten frei. Bitte vergessen Sie nicht, den Zugang nach der Prüfung wieder zu sperren.
</div>

<br/><br/>

<html:form action="saveLoginConfig">
<html:checkbox property="studentsLoginEnabled"/>&nbsp;&nbsp;<b>Studenten-Login aktiv</b><br/>
<html:checkbox property="askForStudentDetails"/>&nbsp;&nbsp;<b>Fragt nach Namen, Matrikelnummer und Fachsemester, falls diese Daten unvollst&auml;ndig sind</b>
<br/><br/>
<html:submit>Speichern</html:submit>
</html:form>