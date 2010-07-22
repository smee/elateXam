<%@ include file="/common/taglibs.jsp"%>
<%
  org.springframework.context.ApplicationContext ctx = 
    org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext( application );
  de.thorstenberger.examServer.service.ConfigManager configManager = (de.thorstenberger.examServer.service.ConfigManager) ctx.getBean( "configManager" );
  java.util.List<String> mailSuffixes=configManager.getRadiusMailSuffixes();
  pageContext.setAttribute( "mailSuffixes", mailSuffixes);
  pageContext.setAttribute("showMailsuffixes",!org.apache.commons.lang.StringUtils.isEmpty(configManager.getRadiusHost()));
%>
<form method="post" id="loginForm" action="<c:url value="/j_security_check"/>"
    onsubmit="saveUsername(this);adjustUsername(this);return validateForm(this)">
<fieldset>
<ul>
<c:if test="${param.error != null}">
    <li class="error">
        <img src="<c:url value="/images/iconWarning.gif"/>"
            alt="<fmt:message key="icon.warning"/>" class="icon" />
        <fmt:message key="errors.password.mismatch"/>
        <!--c:out value="${sessionScope.ACEGI_SECURITY_LAST_EXCEPTION.message}"/-->
    </li>
</c:if>
    <li>
       <label for="j_username" class="desc">
            <fmt:message key="label.username"/> <span class="req">*</span>
        </label>
        <input type="text" class="text medium" name="j_username" id="j_username" tabindex="1" />
        <!--  render all mail suffixes as drop down-->
        <c:if test="${showMailsuffixes}">
        	<select name="j_suffix">
        		<c:forEach items="${mailSuffixes}" var="suffix">
        		<option value="<c:out value="${suffix}"/>"><c:out value="${suffix}"/></option>
        		</c:forEach>
        	</select>
        </c:if>
    </li>

    <li>
        <label for="j_password" class="desc">
            <fmt:message key="label.password"/> <span class="req">*</span>
        </label>
        <input type="password" class="text medium" name="j_password" id="j_password" tabindex="2" />
    </li>

<c:if test="${appConfig['rememberMeEnabled'] != null}">
    <li>
        <input type="checkbox" class="checkbox" name="rememberMe" id="rememberMe" tabindex="3"/>
        <label for="rememberMe" class="choice"><fmt:message key="login.rememberMe"/></label>
    </li>
</c:if>
    <li>
        <input type="submit" class="button" name="login" value="<fmt:message key="button.login"/>" tabindex="4" />
        <p>

        </p>
    </li>
</ul>
</fieldset>
</form>

<%@ include file="/scripts/login.js"%>
