<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:html>
<head>
	<title>Korrektur-Übersicht</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
<script type="text/javascript">
function selectAll(x) {
 for(var i=0,l=x.length; i<l; i++){
  if(x[i].type == 'checkbox' && x[i].name=='userId')
   x[i].checked=true
 }
}</script>
</head>

<body bgcolor="#FFFFFF" text="#000000">


<jsp:include page="../header.jsp" />

<div class="correction">

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
<fieldset><legend>Ihnen zugeordnete, noch nicht vollständig korrigierte Lösungen</legend>
	<form method="post" name="unassignForm1" action="<html:rewrite action="/unassignTaskletFromCorrector"/>">
		<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
		<input type="hidden" name="fromTutorList" value="true"/>

	<display:table requestURI="/tutorCorrectionOverview.do" name="Solutions.assignedUncorrectedTasklets" uid="row" pagesize="30" sort="list" class="displaytag">
		<display:column title="" sortable="false">
			<c:choose>
				<c:when test="${row.viewable}">
					<html:link action="/showCorrectionToCorrector" name="row" property="loginAndTaskId"><img src="pics/magnifier.gif" border="0"></html:link>
				</c:when>
				<c:otherwise>&nbsp;-&nbsp;</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="" sortable="false">
			<input type="checkbox" name="userId" value="${row.login}"/>
		</display:column>
		<display:column property="login" title="Login&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="status" title="Status&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="autoCorrectionPoints" title="autom. Korrektur&nbsp;&nbsp;&nbsp;" sortable="true" decorator="de.thorstenberger.taskmodel.view.FloatColumnDecorator"/>

		<% int i = 1; %>
		<c:forEach items="${row.corrections}" var="corr">
			<% pageContext.setAttribute( "i", i ); %>
			<display:column title="Punkte ${i}. Korrektor&nbsp;&nbsp;&nbsp;" sortable="false">
				${corr.points}
			</display:column>
			<% i++; %>
		</c:forEach>
		<% i = 1; %>
		<c:forEach items="${row.corrections}" var="corr">
			<% pageContext.setAttribute( "i", i ); %>
			<display:column title="Name ${i}. Korrektor&nbsp;&nbsp;&nbsp;" sortable="false">
				${corr.corrector}
			</display:column>
			<% i++; %>
		</c:forEach>

		<display:column property="correctorLogin" title="zugeordneter Korrektor&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column title="Zuordnungs-History&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.correctorHistory}
		</display:column>
		<display:column title="Flags&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.flags}
		</display:column>
		<display:column title="Korrigieren&nbsp;&nbsp;&nbsp;" sortable="false">
			<c:choose>
				<c:when test="${row.corrigible}">
					<html:link action="/doCorrection" name="row" property="loginAndTaskId"><small>korrigieren</small></html:link>
				</c:when>
				<c:otherwise>&nbsp;-&nbsp;</c:otherwise>
			</c:choose>
		</display:column>
	</display:table>

	<hr size="1" noshade>
	<a href="javascript:selectAll(unassignForm1)">Alle ausw&auml;hlen</a>
	<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
	    <option value="markierte:" selected="selected">markierte:</option>
	    <option value="unassign">Korrektor-Zuordnung aufheben</option>
	</select>
	</form>

<br><hr size="1" noshade>

<form action="<html:rewrite action="/assignTasklet"/>" method="post">
  Um Aufgaben korrigieren zu k&ouml;nnen, m&uuml;ssen Sie sich diese erst zuordnen
  lassen. Mit dem folgenden Button wird zuf&auml;llig eine L&ouml;sung f&uuml;r
  Sie zur Korrektur ausgew&auml;hlt.<br>
	<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
	<input type="submit" value="Ausw&auml;hlen"/>
</form>

</fieldset> <br>

<c:if test="${Solutions.privileged}">
  <fieldset>
    <legend>Korrekturen verteilen auf</legend>

    <html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
      <%=pageContext.getAttribute("msg")%>
    </html:messages>
    
	<html:form action="/bulkAssignTasklets">
		<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
        <table>
          <tr>
            <logic:iterate id="item" name="correctorsForm" property="availableCorrectors" indexId="counter">
              <td>
              <html:multibox property="selectedCorrectors">
                <bean:write name="item"/>
              </html:multibox>
              <bean:write name="item"/>
              </td>
              <c:if test="${(counter%3) == 2}"></tr><tr></c:if>
            </logic:iterate>
          </tr>
        </table>
      <html:submit value="Zuordnen"/>
    </html:form>
  </fieldset>
</c:if>

<fieldset><legend>fertige Korrekturen</legend>

	<form method="post" name="unassignForm2" action="<html:rewrite action="/unassignTaskletFromCorrector"/>">
		<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
		<input type="hidden" name="fromTutorList" value="true"/>
		
	<display:table requestURI="/tutorCorrectionOverview.do" name="Solutions.assignedCorrectedTasklets" uid="row" pagesize="30" sort="list" class="displaytag">
		<display:column title="" sortable="false">
			<c:choose>
				<c:when test="${row.viewable}">
					<html:link action="/showCorrectionToCorrector" name="row" property="loginAndTaskId"><img src="pics/magnifier.gif" border="0"></html:link>
				</c:when>
				<c:otherwise>&nbsp;-&nbsp;</c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="" sortable="false">
			<input type="checkbox" name="userId" value="${row.login}"/>
		</display:column>
		<display:column property="login" title="Login&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="status" title="Status&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column property="autoCorrectionPoints" title="autom. Korrektur&nbsp;&nbsp;&nbsp;" sortable="true" decorator="de.thorstenberger.taskmodel.view.FloatColumnDecorator"/>

		<% int i = 1; %>
		<c:forEach items="${row.corrections}" var="corr">
			<% pageContext.setAttribute( "i", i ); %>
			<display:column title="Punkte ${i}. Korrektor&nbsp;&nbsp;&nbsp;" sortable="false">
				${corr.points}
			</display:column>
			<% i++; %>
		</c:forEach>
		<% i = 1; %>
		<c:forEach items="${row.corrections}" var="corr">
			<% pageContext.setAttribute( "i", i ); %>
			<display:column title="Name ${i}. Korrektor&nbsp;&nbsp;&nbsp;" sortable="false">
				${corr.corrector}
			</display:column>
			<% i++; %>
		</c:forEach>

		<display:column property="correctorLogin" title="zugeordneter Korrektor&nbsp;&nbsp;&nbsp;" sortable="true"/>
		<display:column title="Zuordnungs-History&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.correctorHistory}
		</display:column>
		<display:column title="Flags&nbsp;&nbsp;&nbsp;" sortable="false">
			${row.flags}
		</display:column>
		<display:column title="Korrigieren&nbsp;&nbsp;&nbsp;" sortable="false">
			<c:choose>
				<c:when test="${row.corrigible}">
					<html:link action="/doCorrection" name="row" property="loginAndTaskId"><small>korrigieren</small></html:link>
				</c:when>
				<c:otherwise>&nbsp;-&nbsp;</c:otherwise>
			</c:choose>
		</display:column>
	</display:table>
	
	<hr size="1" noshade>
	<a href="javascript:selectAll(unassignForm2)">Alle ausw&auml;hlen</a>
	<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
	    <option value="markierte:" selected="selected">markierte:</option>
	    <option value="unassign">Korrektor-Zuordnung aufheben</option>
	</select>
	</form>

</fieldset><br>
<c:if test="${Solutions.privileged}">
	<fieldset><legend>Administrationsfunktionen</legend>
	<ul>
	  <li><html:link action="/viewAllTasklets" paramId="taskId" paramName="Solutions" paramProperty="taskId">Gesamtliste</html:link><br><br></li>
	  <li><img src="<%= request.getContextPath() %>/pics/excel.gif" align="bottom"> <html:link href="excelReport/report_${Solutions.taskId}.xls">Gesamtliste als Excel-Datei</html:link><br><br></li>
	  <li><html:link action="/complexTaskStructure" paramId="taskId" paramName="Solutions" paramProperty="taskId">Strukturübersicht mit Detailauswertung</html:link><br><br></li>
	  <li><html:link action="/correctionSettings" paramId="taskId" paramName="Solutions" paramProperty="taskId">Korrektur-Einstellungen/Funktionen</html:link></li>
	</ul>

	</fieldset><br/>
</c:if>

<fieldset><legend>Kommentierungsfunktion</legend>
Bearbeiter können Ihre Korrektur kommentieren. Um darauf zu reagieren, sortieren Sie die Liste Ihrer Korrekturen <c:if test="${Solutions.privileged}">oder die Gesamtliste </c:if>nach
der Statusspalte. Kommentierte Lösungen haben den Status <b>"annotated"</b>. In der Korrektursicht sehen Sie dann unten den/die Kommentar(e) des Bearbeiters. Dass Sie diese gelesen haben,
bestätigen Sie mit dem Button "Lesebestätigung setzen", woraufhin der Status auf <b>"annotation_acknowledged"</b> gesetzt wird.
</fieldset>


</div>
<jsp:include page="../footer.jsp" />

</body>

</html:html>
