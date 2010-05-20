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
<tr>
<td>Moodle-Benutzername (Email): </td><td><html:text property="userId" size="60"/></td>
</tr>
<tr>
<td>Passwort: </td><td><html:password property="password" size="60"/></td>
</tr>
<tr>
<td>Kursname: </td><td><html:text property="courseName" size="60"/></td>
</tr>
<tr>
  <td>Ihre Kurse:</td>
  <td><html:submit property="searchCourses">Meine Kurse suchen</html:submit></td>
</tr>
<tr><td></td>
  <td>
    <c:forEach var="course" items="${knownCourses}">
      <c:out value="${course}"/><br/>
    </c:forEach>
  </td>
</tr>
</table>
<br/><br/>
<html:submit>Importieren</html:submit>
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