<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="activeTaskUsers.title"/></title>
    <content tag="heading"><fmt:message key="activeTaskUsers.title"/></content>
    <meta name="menu" content="AdminMenu"/>
</head>
<body id="activeUsers"/>

<p><fmt:message key="activeTaskUsers.message"/></p>

<div class="separator"></div>

<input type="button" onclick="location.href='<html:rewrite forward="mainMenu"/>'" value="<fmt:message key="button.done"/>"/>

<display:table name="ActiveUsers" id="user" cellspacing="0" cellpadding="0"
    defaultsort="1" class="table" pagesize="50" requestURI="">
  
    <display:column property="username" escapeXml="true" style="width: 30%" titleKey="userForm.username" sortable="true"/>
    <display:column property="taskTitle" escapeXml="true" style="width: 30%" titleKey="activeTaskUsers.taskttitle" sortable="true"/>
    <display:column property="timeExtension" escapeXml="true" style="width: 30%" titleKey="activeTaskUsers.timeextension" sortable="true"/>
    <display:column titleKey="activeTaskUsers.increaseTime" sortable="true">
		<html:link action="/increaseTime" name="user" property="loginAndTaskId"><fmt:message key="activeTaskUsers.increaseLink"/></html:link>
	</display:column>
</display:table>
