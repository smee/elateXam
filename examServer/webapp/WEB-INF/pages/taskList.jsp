<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <content tag="heading">Hallo <authz:authentication operation="fullName"/>!</content>
    <meta name="menu" content="MainMenu"/>
</head>

<p>In der folgenden Liste finden Sie alle verfügbaren Aufgaben. Klicken Sie auf den Titel der Aufgabe, um sie zu bearbeiten.</p>

	<display:table partialList="false" name="TaskDefs" uid="row" pagesize="30" sort="list" class="table">
		<display:column title="Name&nbsp;&nbsp;&nbsp;" sortable="true">
			<html:link action="/TaskConfig" paramId="taskId" paramName="row" paramProperty="id"><c:out value="${row.title}"/></html:link>
			<c:if test="${!row.active}"> <b>(inaktiv)</b></c:if>
		</display:column>
		<display:column property="type" title="Typ&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="shortDescription" title="Kurzbeschreibung&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column title="L&ouml;schen" sortable="false">
		    <html:link action="/removeTask" paramId="taskId" paramName="row" paramProperty="id"><c:out value="X"/></html:link>
		</display:column>
	</display:table>

<br/>
<br/>
<p><h2>Neue Aufgabe einstellen: </h2></p>
<html:form action="/storeNewTask" method="POST" enctype="multipart/form-data">
<table border="0">
<tr>
<td>Aufgabendatei: </td><td><html:file property="taskDefFile"/></td>
</tr>
</table>
<br/><br/>
<html:submit>Hochladen</html:submit>
</html:form>