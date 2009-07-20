<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <content tag="heading">Hallo <authz:authentication operation="fullName"/>!</content>
    <meta name="menu" content="MainMenu"/>
</head>

<p>In der folgenden Liste finden Sie alle verfügbaren Aufgaben zur Korrektur. Klicken Sie auf den Titel der Aufgabe, um zur Korrektur-Übersicht zu gelangen.</p>

	<display:table name="TaskDefs" uid="row" pagesize="30" class="table">
		<display:column title="Name&nbsp;&nbsp;&nbsp;" sortable="true">
			<html:link action="/CorrectorFactory" paramId="taskId" paramName="row" paramProperty="id"><c:out value="${row.title}"/></html:link>
			<c:if test="${!row.active && row.visible}"> <b>(inaktiv)</b></c:if>
			<c:if test="${!row.visible && row.active}"> <b>(unsichtbar)</b></c:if>
			<c:if test="${!row.active && !row.visible}"> <b>(inaktiv, unsichtbar)</b></c:if>
		</display:column>
		<display:column property="type" title="Typ&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="shortDescription" title="Kurzbeschreibung&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="numberOfOpenCorrections" title="Anzahl unkorrigierter Pr&uuml;fungen&nbsp;&nbsp;" sortable="true"/>
	</display:table>