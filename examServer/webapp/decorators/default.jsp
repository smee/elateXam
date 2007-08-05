<%@ include file="/common/taglibs.jsp"%>
<html>
    <head>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/></title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["theme"]}/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["theme"]}/print.css'/>" />

        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
        <decorator:head/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>

    <div id="page">
        <jsp:include page="/common/header.jsp"/>
        <div id="header" class="clearfix">
        </div>

        <div id="content" class="clearfix">
            <div id="main">
                <%@ include file="/common/messages.jsp" %>
                <h1><decorator:getProperty property="page.heading"/></h1>
                <decorator:body/>
            </div>

            <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
            <c:if test="${currentMenu == 'AdminMenu'}">
            <div id="sub">
                <menu:useMenuDisplayer name="Velocity" config="WEB-INF/classes/cssVerticalMenu.vm" permissions="rolesAdapter">
                    <menu:displayMenu name="AdminMenu"/>
                </menu:useMenuDisplayer>
            </div>
            </c:if>

            <div id="nav">
                <div class="wrapper">
                    <h2 class="accessibility">Navigation</h2>
                    <jsp:include page="/WEB-INF/pages/menu.jsp"/>
                </div>
                <hr />
            </div><!-- end nav -->
        </div>

        <div id="footer" class="clearfix">
        </div>
        <jsp:include page="/common/footer.jsp"/>
    </div>
</body>
</html>
