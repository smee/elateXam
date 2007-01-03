<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<title>L&ouml;sung von ${Solution.login}</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">

<jsp:include page="header.jsp" />

<html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
	<%=pageContext.getAttribute("msg")%>
</html:messages>

<html:errors />

<p class="header">L&ouml;sung von ${Solution.login}</p>

<table border="0" cellspacing="2" cellpadding="2" width="790">
  <tr bgcolor="#F2F9FF"> 
    <td valign="top"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"> 
      <a href="${ReturnURL}">&Uuml;bersicht</a></td>
  </tr>
  <tr bgcolor="#F2F9FF"> 
	<td><br>
	<fieldset>
            
      <table>
        <tr>
                <td>Name:</td>
                <td>${Solution.userName}</td>
              </tr>
              <tr> 
                <td>Gesamtpunktzahl:</td>
                <td>${Solution.points}</td>
              </tr>
              <tr> 
                <td>Status:</td>
                <td>${Solution.status}</td>
              </tr>
              <tr> 
                <td>Startzeit der Bearbeitung:</td>
                <td>${Solution.tryStartTime}</td>
              </tr>
            </table>
			</fieldset><br>
	</td>
  </tr>
  <tr> 
    <td valign="top" bgcolor="#F2F9FF">

			<c:forEach items="${SubTasklets}" var="SubTasklet">
				
				<fieldset class="complexTask"><legend>Aufgabe ${SubTasklet.virtualSubTaskletNumber}</legend>

				<table width="100%" cellspacing="0" cellpadding="0"><tr><td align="center" valign="top" class="ComplexTaskHint">
				<c:if test="${SubTasklet.hint != null}">
					${SubTasklet.hint}
				</c:if>
				</td><td valign="top" align="right" class="ComplexTaskHint">
					${SubTasklet.reachablePoints} Punkt<c:if test="${SubTasklet.reachablePoints != 1}">e</c:if>
				</td></tr></table><br><br>

					${SubTasklet.problem}
					
				<br><br>
					${SubTasklet.renderedHTML}
				
				<c:choose>
					<c:when test="${SubTasklet.corrected}">
						<br><br><font color=red>Aufgabe korrigiert, erreichte Punkte: ${SubTasklet.points}</font>
					</c:when>
					<c:otherwise>
						<br><br><font color=red>manuelle Korrektur notwendig</font>
					</c:otherwise>
				</c:choose>
				
				<br></fieldset><br>

				
			</c:forEach>


	</td>
  </tr>
  
  <tr bgcolor="#F2F9FF"> 
	<td><br>
	<fieldset><legend>Kommentierung</legend>
		<c:choose>
			<c:when test="${Solution.canAnnotate}">
				Sie haben hier die Möglichkeit, die Korrektur Ihrer Aufgaben zu kommentieren. Bitte geben Sie zu jedem Kommentar die Nummer der betreffenden Aufgabe an.
				<br><br>
				<form method="post" action="<html:rewrite action="/saveStudentAnnotation"/>">
					<input type="hidden" name="id" value="${Solution.taskId}"/>
					<div align="center">
					<textarea name="studentAnnotation" cols="80" rows="15" !onChange="setModified()" style="background-color: #FFFFDD;">${Solution.actualAnnotation}</textarea>
					<input type="submit" name="save" value="Speichern"/>
					</div>
				</form>
			</c:when>
			<c:otherwise>Sie können erst kommentieren, wenn alle Aufgaben korrigiert wurden.</c:otherwise>
		</c:choose>
		<br/>
		<c:if test="${Solution.haveAnnotations}">
			<br/>
			<b>Bestätigte frühere Kommentare:</b><br/><br/>
			<c:forEach items="${Solution.annotations}" var="annotation">
				<div class="newsheader">${annotation.date}</div>
				<div class="newsbody">${annotation.text}</div>
				<br/>
			</c:forEach>
		</c:if>
	</fieldset><br>
	</td>
  </tr>
  
  
</table>


<jsp:include page="footer.jsp" />

</body>

</html:html>
