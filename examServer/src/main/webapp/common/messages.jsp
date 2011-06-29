<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%-- Error Messages --%>
<logic:messagesPresent>
    <div class="error" id="errorMessages">
        <html:messages id="error">
            <html:img pageKey="icon.warning.img" 
                altKey="icon.warning" styleClass="icon"/>
            <c:out value="${error}" escapeXml="false"/><br/>
        </html:messages>
    </div>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
    <div class="message" id="successMessages">
        <html:messages id="message" message="true">
            <html:img pageKey="icon.information.img" 
                altKey="icon.information" styleClass="icon"/>
            <c:out value="${message}" escapeXml="false"/><br/>
        </html:messages>
    </div>
</logic:messagesPresent>
