<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:html>
<head>
	<title>Korrektur-Übersicht</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">

<jsp:include page="../header.jsp" />

<p class="header">Korrektur - &Uuml;bersicht</p>

<table border="0" cellspacing="2" cellpadding="2" width="790">
  <tr bgcolor="#F2F9FF"> 
    <td valign="top"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"> 
      <a href="${ReturnURL}">&Uuml;bersicht</a></td>
  </tr>
</table>

<html:errors />

<br/>

<fieldset><legend>Status</legend> 
<table border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td>Anzahl L&ouml;sungen:</td>
    <td>${Solutions.count}</td>

  </tr>
  <tr> 
    <td>Anzahl Korrektoren zugeordnete L&ouml;sungen:</td>
    <td>${Solutions.assignedCount}</td>
  </tr>
  <tr> 
    <td>Anzahl korrigierter L&ouml;sungen:</td>
    <td>${Solutions.correctedCount} (${Solutions.correctedCountPercent})</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>

  </tr>
</table>
</fieldset> 
<br>
<fieldset><legend>Ihnen zugeordnete, noch nicht korrigierte Lösungen</legend> 

	<display:table requestURI="/tutorCorrectionOverview.do" name="Solutions.assignedUncorrectedTasklets" uid="row" pagesize="30" sort="list" class="displaytag">
		<display:column property="login" title="Login&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="status" title="Status&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="points" title="Punkte&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="correctorLogin" title="zugeordneter Korrektor&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column title="Korrektoren-History&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.correctorHistory}
		</display:column>
		<display:column title="Korrigieren&nbsp;&nbsp;&nbsp;" sortable="false">
<!--			<a href="doCorrection.do?taskId=${Solutions.taskId}&userId=${row.login}"><small>korrigieren</small></a>-->
			<html:link action="/doCorrection" name="row" property="loginAndTaskId"><small>korrigieren</small></html:link>
		</display:column>
	</display:table>

<br><hr size="1" noshade>

<form action="<html:rewrite action="/assignTasklet"/>">
  Um Aufgaben korrigieren zu k&ouml;nnen, m&uuml;ssen Sie sich diese erst zuordnen 
  lassen. Mit dem folgenden Button wird zuf&auml;llig eine L&ouml;sung f&uuml;r 
  Sie zur Korrektur ausgew&auml;hlt.<br>
	<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
	<input type="submit" value="Ausw&auml;hlen"/>
</form>

</fieldset> <br>
<fieldset><legend>fertige Korrekturen</legend>

	<display:table requestURI="/tutorCorrectionOverview.do" name="Solutions.assignedCorrectedTasklets" uid="row" pagesize="30" sort="list" class="displaytag">
		<display:column property="login" title="Login&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="status" title="Status&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="points" title="Punkte&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="correctorLogin" title="zugeordneter Korrektor&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column title="Korrektoren-History&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.correctorHistory}
		</display:column>
		<display:column title="Korrigieren&nbsp;&nbsp;&nbsp;" sortable="false">
<!--			<a href="doCorrection.do?taskId=${Solutions.taskId}&userId=${row.login}"><small>korrigieren</small></a>-->
			<html:link action="/doCorrection" name="row" property="loginAndTaskId"><small>korrigieren</small></html:link>
		</display:column>
	</display:table>

</fieldset><br>
<c:if test="${Solutions.privileged}">
	<fieldset><legend>Administrationsfunktionen</legend>
	<ul>
	  <li><html:link action="/viewAllTasklets" paramId="taskId" paramName="Solutions" paramProperty="taskId">Gesamtliste</html:link><br><br>
	  <li><img src="<%= request.getContextPath() %>/pics/excel.gif" align="bottom"> <html:link href="excelReport/report_${Solutions.taskId}.xls">Gesamtliste als Excel-Datei</html:link>
	</ul>
	
	</fieldset><br/>
</c:if>

<fieldset><legend>Kommentierungsfunktion</legend>
Bearbeiter können Ihre Korrektur kommentieren. Um darauf zu reagieren, sortieren Sie die Liste Ihrer Korrekturen <c:if test="${Solutions.privileged}">oder die Gesamtliste </c:if>nach
der Statusspalte. Kommentierte Lösungen haben den Status <b>"annotated"</b>. In der Korrektursicht sehen Sie dann unten den/die Kommentar(e) des Bearbeiters. Dass Sie diese gelesen haben,
bestätigen Sie mit dem Button "Lesebestätigung setzen", woraufhin der Status auf <b>"annotation_acknowledged"</b> gesetzt wird.	
</fieldset>

<jsp:include page="../footer.jsp" />

</body>

</html:html>
