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
			<html:link action="/doCorrection" name="row" property="loginAndTaskId"><small>korrigieren</small></html:link>
		</display:column>
	</display:table>

</fieldset><br>
<!--fieldset><legend>Dozent</legend>
<ul>
  <li><a href="/UebManager/manager.UebManager?action=ListTaskHandlingData&id=5">Gesamt&uuml;bersicht</a><br><br> 
  <li><a href="/UebManager/manager.UebManager/taskCorrection/overview_task_5.csv">Excel-Tabelle 
zur &Uuml;bersicht</a><br>

</ul>

</fieldset--> 

<jsp:include page="../footer.jsp" />

</body>

</html:html>
