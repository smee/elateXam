<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <content tag="heading"><fmt:message key="common.hello"/> <authz:authentication operation="fullName"/>!</content>
    <meta name="menu" content="MainMenu"/>
</head>
<p><fmt:message key="mainMenu.student"/></p>

	<display:table partialList="false" name="TaskDefs" uid="row" pagesize="30" sort="list" class="table">
		<display:column title="Name&nbsp;&nbsp;&nbsp;" sortable="true">
			<html:link action="/TaskViewFactory" paramId="taskId" paramName="row" paramProperty="id"><c:out value="${row.title}"/></html:link>
			<c:if test="${!row.active}"> <b>(inaktiv)</b></c:if>
		</display:column>
		<display:column property="type" title="Typ&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="shortDescription" title="Kurzbeschreibung&nbsp;&nbsp;&nbsp;" sortable="true"/>
	</display:table>