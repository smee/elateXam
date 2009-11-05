<%@ include file="/common/taglibs.jsp"%>

<%
	org.springframework.context.ApplicationContext ctx = 
    org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext( application );
	de.thorstenberger.examServer.service.ConfigManager configManager = (de.thorstenberger.examServer.service.ConfigManager) ctx.getBean( "configManager" );
	pageContext.setAttribute( "loadJava", new Boolean( configManager.isLoadJVMOnStartup() ) );
	pageContext.setAttribute( "serverTitle", configManager.getTitle() );
%>

<head>
    <title><fmt:message key="login.title"/></title>
    <content tag="heading"><fmt:message key="login.heading"/><!--c:out value="${serverTitle}"/--></content>
    <meta name="menu" content="Login"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["theme"]}/layout-1col.css'/>" />
</head>
<body id="login"/>

<font size="+1"><b><c:out value="${serverTitle}"/></b></font>
<br/><br/>
<%-- Include the login form --%>
<jsp:include page="/WEB-INF/pages/loginForm.jsp"/>

<br/><br/><br/>


<c:if test="${loadJava}">
<div align="right" class="copytext">
Java-Applets: <applet code="CheckJavaApplet.class" width="14" height="14" codebase="applet">
</applet>
</div>
</c:if>