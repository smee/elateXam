<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <content tag="heading"><fmt:message key="common.hello"/> <authz:authentication operation="fullName"/>!</content>
    <meta name="menu" content="MainMenu"/>
</head>
<p><fmt:message key="mainMenu.complete.data"/><br>
<html:form action="/submitStudentsInfo" styleId="userForm" onsubmit="return validateUserForm(this)">
<html:hidden property="id"/>
<ul>
    <li>
        <div>
            <div class="left">
                <examServer:label styleClass="desc" key="userForm.firstName"/>
                <html:errors property="firstName"/>
                <html:text styleClass="text medium" property="firstName" styleId="firstName" maxlength="50"/>
            </div>
            <div>
                <examServer:label styleClass="desc" key="userForm.lastName"/>
                <html:errors property="lastName"/>
                <html:text styleClass="text medium" property="lastName" styleId="lastName" maxlength="50"/>
            </div>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <examServer:label styleClass="desc" key="userForm.matrikel"/>
                <html:errors property="matrikel"/>
                <html:text styleClass="text medium" property="matrikel" styleId="matrikel"/>
            </div>
            <div>
                <examServer:label styleClass="desc" key="userForm.semester"/>
                <html:errors property="semester"/>
                <html:text styleClass="text medium" property="semester" styleId="semester"/>
            </div>
        </div>
    </li>
</ul>    
<input type="submit" name="OK" value="OK"/>
</html:form>
