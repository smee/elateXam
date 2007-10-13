<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
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
	<html:link action="/tutorCorrectionOverview" paramId="taskId" paramName="Solutions" paramProperty="taskId">Korrektur-Übersicht</html:link>
	</td>
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
<fieldset><legend>Alle Lösungen</legend>

	<form method="post" name="unassignForm" action="<html:rewrite action="/unassignTaskletFromCorrector"/>">
		<input type="hidden" name="taskId" value="${Solutions.taskId}"/>
		<input type="hidden" name="fromAllTaskletsList" value="true"/>
	
		<display:table requestURI="/viewAllTasklets.do" name="Solutions.allTasklets" uid="row" pagesize="30" sort="list" class="displaytag">
			<display:column title="" sortable="false">
				<c:choose>
					<c:when test="${row.viewable}">
						<html:link action="/showCorrectionToCorrector" name="row" property="loginAndTaskId"><img src="pics/magnifier.gif" border="0"></html:link>
					</c:when>
					<c:otherwise>&nbsp;-&nbsp;</c:otherwise>
				</c:choose>
			</display:column>
			<display:column title="" sortable="false">
				<c:if test="${row.correctorLogin!=null}">
					<input type="checkbox" name="userId" value="${row.login}"/>
				</c:if>
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
		<a href="javascript:selectAll(unassignForm)">Alle ausw&auml;hlen</a>
		<select name="submit_mult" onchange="this.form.submit();" style="margin: 0 3em 0 3em;">
		    <option value="markierte:" selected="selected">markierte:</option>
		    <option value="unassign">Korrektor-Zuordnung aufheben</option>
		</select>
	</form>
	
</fieldset>

</div>
<jsp:include page="../footer.jsp" />

</body>

</html:html>
