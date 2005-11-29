<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<meta http-equiv="expires" content="0">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
	<title>Vorschau: ${SubTasklet.id}</title>
</head>
<body>

	<fieldset class="complexTask"><legend>Aufgabe ${SubTasklet.virtualSubTaskletNumber}</legend>
		<table width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" valign="top" class="ComplexTaskHint">
					<c:if test="${SubTasklet.hint != null}">
						${SubTasklet.hint}
					</c:if>
				</td>
				<td valign="top" align="right" class="ComplexTaskHint">
					${SubTasklet.reachablePoints} Punkt<c:if test="${SubTasklet.reachablePoints != 1}">e</c:if>
				</td>
			</tr>
		</table><br><br>
		<div class="problem">
			${SubTasklet.problem}
		</div>
		<br><br>
			${SubTasklet.renderedHTML}
		<br><br>
		<br>
	</fieldset>
</body>
</html:html>
