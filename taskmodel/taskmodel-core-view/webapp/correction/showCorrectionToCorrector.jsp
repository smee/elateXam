<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<title>L&ouml;sung von ${Solution.login}</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">

<jsp:include page="../header.jsp" />

<html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
	<%=pageContext.getAttribute("msg")%>
</html:messages>

<html:errors />

<p class="header">L&ouml;sung von ${Correction.userId}</p>

<table border="0" cellspacing="2" cellpadding="2" width="790">
  <tr bgcolor="#F2F9FF"> 
    <td valign="top"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"> 
		<html:link action="/tutorCorrectionOverview" paramId="taskId" paramName="Correction" paramProperty="taskId">Korrektur-Übersicht</html:link>
	</td>
	<td valign="top" align="right"><html:link action="/doCorrection" name="Correction" property="loginAndTaskId"><img src="pics/magnifier.gif" border="0" hspace="5">Korrekturansicht</html:link>
	</td>
  </tr>
  <tr bgcolor="#F2F9FF"> 
	<td colspan="2"><br>
	<fieldset>
            
      <table>
        <tr>
                <td>Name:</td>
                <td><c:choose>
               			<c:when test="${Correction.unregisteredUser}"><font color="red">Nicht registrierter Benutzer</font></c:when>
               			<c:when test="${Correction.userNameInvisible}">[Benutzername nicht sichtbar]</c:when>                
                		<c:otherwise>${Correction.userName}</c:otherwise>
	                </c:choose>
                </td>
              </tr>
              <tr> 
                <td>Gesamtpunktzahl:</td>
                <td>${Correction.points}</td>
              </tr>
              <tr> 
                <td>Status:</td>
                <td>${Correction.status}</td>
              </tr>
              <tr> 
                <td>Startzeit der Bearbeitung:</td>
                <td>${Correction.tryStartTime}</td>
              </tr>
            </table>
			</fieldset><br>
	</td>
  </tr>
  <tr> 
    <td valign="top" bgcolor="#F2F9FF" colspan="2">

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
	<td colspan="2"><br>
	<fieldset><legend>Kommentare des Bearbeiters</legend>
		<b>Unbestätigt:</b><br/>
			<c:forEach items="${Correction.nonAcknowledgedAnnotations}" var="annotation">
				<div class="newsheader">${annotation.date}</div>
				<div class="newsbody">${annotation.text}</div>
				<br/>
			</c:forEach>
		<br/><br/><b>Bestätigt:</b><br/>
			<c:forEach items="${Correction.acknowledgedAnnotations}" var="annotation">
				<div class="newsheader">${annotation.date}</div>
				<div class="newsbody">${annotation.text}</div>
				<br/>
			</c:forEach>          

          <br/><br/>

	</fieldset><br>
	<fieldset><legend>Kommentare des Korrektors</legend>

		${Correction.annotation}

        <br/><br/>

	</fieldset><br>
	
	</td>
  </tr>
  
</table>


<jsp:include page="../footer.jsp" />

</body>

</html:html>
