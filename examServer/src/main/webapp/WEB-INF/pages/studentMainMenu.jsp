<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <content tag="heading">Hallo <authz:authentication operation="fullName"/>!</content>
    <meta name="menu" content="MainMenu"/>
</head>
<c:if test="${askForSemester}">
<p>Bitte geben Sie Ihr Fachsemester ein und klicken Sie auf OK!<br>
<form action="<html:rewrite action='/submitStudentsInfo'/>">
Semester: <input type="text" size="7" name="semester" value="<c:out value='${semester}'/>"/> <input type="submit" name="OK" value="OK"/>
</form>
<br/>
</p>
</c:if>
<p>In der folgenden Liste finden Sie alle verfügbaren Aufgaben. Klicken Sie auf den Titel der Aufgabe, um zu ihrer Übersicht zu gelangen.</p>

	<display:table partialList="false" name="TaskDefs" uid="row" pagesize="30" sort="list" class="table">
		<display:column title="Name&nbsp;&nbsp;&nbsp;" sortable="true">
			<html:link action="/TaskViewFactory" paramId="taskId" paramName="row" paramProperty="id"><c:out value="${row.title}"/></html:link>
			<c:if test="${!row.active}"> <b>(inaktiv)</b></c:if>
		</display:column>
		<display:column property="type" title="Typ&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="shortDescription" title="Kurzbeschreibung&nbsp;&nbsp;&nbsp;" sortable="true"/>
	</display:table>