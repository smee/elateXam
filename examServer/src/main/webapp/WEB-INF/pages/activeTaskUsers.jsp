<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="activeTaskUsers.title"/></title>
    <content tag="heading"><fmt:message key="activeTaskUsers.title"/></content>
    <meta http-equiv="refresh" content="60" />
    <meta name="menu" content="AdminMenu"/>
</head>
<body id="activeUsers"/>

<p><fmt:message key="activeTaskUsers.message"/></p>

<div class="separator"></div>

<input type="button" onclick="location.href='<html:rewrite forward="mainMenu"/>'" value="<fmt:message key="button.done"/>"/>
  
 <br/>
 <br/>
 <h2><fmt:message key="activeUsers.increaseTimeGlobally"/></h2>
  <form method="post" action="<html:rewrite action="/increaseTimeGlobal"/>">
    <select name="taskId">
       <option value=""><fmt:message key="activeUsers.choose"/></option>
      <c:forEach items="${activeTasks}" var="task"> 
        <option value="<c:out value="${task.taskId}"/>"> <c:out value="${task.taskTitle}"/></option>
      </c:forEach>
    </select>
    <input type="submit" value="+5min"/>
  </form>
    
    <br/>
 <h2><fmt:message key="activeUsers.progress"/></h2>    
 
<display:table name="ActiveUsers" id="user" cellspacing="0" cellpadding="0"
    defaultsort="1" class="table" pagesize="50" requestURI="">
  
    <display:column property="username" escapeXml="true" style="width: 20%" titleKey="userForm.username" sortable="true"/>
    <display:column property="taskTitle" escapeXml="true" style="width: 25%" titleKey="activeTaskUsers.taskttitle" sortable="true"/>
    <display:column property="status" escapeXml="true" style="width: 20%" titleKey="activeTaskUsers.status" sortable="true"/>
    <display:column property="remainingMinutes" escapeXml="true" style="width: 20%" titleKey="activeTaskUsers.remainingminutes" sortable="true"/>
    <display:column titleKey="activeTaskUsers.increaseTime" sortable="true">
		<html:link action="/increaseTime" name="user" property="loginAndTaskId"><fmt:message key="activeTaskUsers.increaseLink"/></html:link>
	</display:column>
</display:table>
