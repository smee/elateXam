<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="import.moodle.title"/></title>
    <content tag="heading"><fmt:message key="import.moodle.heading"/></content>
    <meta name="menu" content="MainMenu"/>
</head>

<div class="separator">
Hier k&ouml;nnen Sie Mitglieder einer moodle-Gruppe ins Pr&uuml;fungssystem importieren.
</div>

<br/><br/>

<html:form action="/importMoodleUsers.do">
<table border="0">

<c:choose>
  <c:when test="${empty knownCourses}">
    <tr>
      <td>Moodle-Benutzername (Email): </td><td><html-el:text property="userId" size="60"/></td>
    </tr>
    <tr>
      <td>Passwort: </td><td><html-el:password property="password" size="60"/></td>
    </tr>
    <tr>
      <td></td>
      <td><html:submit property="searchCourses">Meine Kurse suchen</html:submit></td>
    </tr>
  </c:when>
  <c:otherwise>
    <html-el:hidden property="userId"/>
    <html-el:hidden property="password"/>
    <tr>
      <td>Bitte wählen Sie:</td>
      <td>
        <c:forEach var="course" items="${knownCourses}">
          <html-el:radio property="courseName" value="${course}"/><c:out value="${course}"/><br/>
        </c:forEach>
      </td>
    </tr>
    <tr>
      <td></td>
      <td><html:submit>Importieren</html:submit></td>
    </tr>
  </c:otherwise>
</c:choose>
</table>
</html:form>
<br/>
<br/>

<c:if test="${not empty importedMembers}">
  <h2>
  Importierte Benutzer:
  </h2>
  <display:table name="importedMembers" cellspacing="0" cellpadding="0" requestURI="" 
      defaultsort="1" id="users" pagesize="25" class="table" export="false">
      <display:column property="memberId" escapeXml="true" sortable="true" titleKey="userForm.username" style="width: 25%"/>
      <display:column property="firstname" escapeXml="true" sortable="true" titleKey="userForm.firstName" style="width: 25%"/>
      <display:column property="lastname" escapeXml="true" sortable="true" titleKey="userForm.lastName" style="width: 34%"/>
      <display:column property="email" sortable="true" titleKey="userForm.email" style="width: 25%" autolink="true" media="html"/>
  </display:table>
</c:if>