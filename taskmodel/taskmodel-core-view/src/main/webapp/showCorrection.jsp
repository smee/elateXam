<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<title>L&ouml;sung von ${Solution.login}</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
	<script type="text/javascript"> djConfig = { isDebug: false }; </script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/dojo/dojo.js"></script>
	<script type="text/javascript">
		dojo.require("dojo.widget.Dialog");
		dojo.require("dojo.io.*");
	</script>

	<script type="text/javascript">
		var dlg;

		function init(e) {
			dlg = dojo.widget.byId("DialogContent");
			var btn = document.getElementById("hider");
			dlg.setCloseControl(btn);
			
         	x = new dojo.io.FormBind ({
           		formNode: "studentAnnotationForm",
           		url: '<html:rewrite action="/ajaxSaveStudentAnnotation" paramId="id" paramName="Solution" paramProperty="taskId"/>',
           		load: callBack
         	});
		}
		
		dojo.addOnLoad(init);

       function callBack(type, data, evt) {
          	dojo.byId('studentAnnotationTA').value = data;
          	dlg.hide();
       }
    </script>
    
	<style type="text/css">
		.dojoDialog {
			background : #eee;
			border : 1px solid #999;
			-moz-border-radius : 5px;
			padding : 4px;
		}
	</style>
	
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
	<td><br/>
	<fieldset>
            
      <table>
        <tr>
                <td>Name:</td>
                <td>${Solution.userName}</td>
              </tr>
              <tr> 
                <td valign="top">Gesamtpunktzahl:</td>
                <td>
	                <table border="0">
						<c:forEach items="${Solution.corrections}" var="corr">
							<tr>
								<c:choose>
									<c:when test="${corr.auto}">
										<td>automatische Korrektur:</td><td><font color="red">${corr.points}</font></td>
									</c:when>
									<c:otherwise>
										<td>${corr.corrector}</td><td><font color="red">${corr.points}</font></td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</table>
                </td>
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
			</fieldset><br/>
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
				</td></tr></table><br/><br/>

					${SubTasklet.problem}
					
				<br/><br/>
					${SubTasklet.renderedHTML}
				
				<c:choose>
					<c:when test="${SubTasklet.corrected}">
						<br/><br/><font color="red">Aufgabe korrigiert. Korrekturen: 
							<table border="0">
							<c:forEach items="${SubTasklet.corrections}" var="Correction">
								<tr>
									<c:choose>
										<c:when test="${Correction.auto}">
											<td>automatische Korrektur:</td><td>${Correction.points}</td>
										</c:when>
										<c:otherwise>
											<td>${Correction.corrector}</td><td>${Correction.points}</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach>
							</table>
						</font>
					</c:when>
					<c:when test="${SubTasklet.needsManualCorrectionFlag}">
						<br/><br/><font color="red">manuelle Korrektur notwendig</font>
					</c:when>
				</c:choose>
				
				<br/></fieldset>
					<c:if test="${Solution.canAnnotate}">
						<script type="text/javascript">
							document.write('<a href="javascript: dlg.show()"><img src="icons/tango/accessories-text-editor-small.png" border="0" hspace="5" align="right"/></a>');
						</script>
					</c:if>
					<br/>
					<br/>

				
			</c:forEach>


	</td>
  </tr>
  
  <tr bgcolor="#F2F9FF"> 
	<td>
	<br/><fieldset><legend>Kommentierung</legend>
		<img src="<%= request.getContextPath() %>/icons/tango/accessories-text-editor.png" align="left" hspace="10">
		<c:choose>
			<c:when test="${Solution.canAnnotate}">
				<script type="text/javascript">
					document.write('Sie haben die Möglichkeit, Ihre Aufgaben in einem Textfeld zu kommentieren. Klicken Sie dazu auf das Icon rechts unter jeder Aufgabe. Es erscheint <b>immer dasselbe Textfeld</b>, geben Sie deswegen bitte zu jedem Kommentar die <b>Nummer der betreffenden Aufgabe an</b>.');
				</script>
				<div dojoType="dialog" !dojoType="ModalFloatingPane" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
					Sie haben hier die Möglichkeit, die Korrektur Ihrer Aufgaben zu kommentieren.<br/>Bitte geben Sie zu jedem Kommentar die <b>Nummer der betreffenden Aufgabe</b> an.
					<br/><br/>
					<form id="studentAnnotationForm" action="<html:rewrite action="/saveStudentAnnotation" paramId="id" paramName="Solution" paramProperty="taskId"/>">

						<div align="center">
							<textarea name="studentAnnotation" id="studentAnnotationTA" cols="65" rows="12" style="background-color: #FFFFDD">${Solution.actualAnnotation}</textarea>
							<br/>
							<input type="submit" name="save" value="Speichern"/>
							<script type="text/javascript">
								document.write('<input type="button" id="hider" value="Ausblenden"/>');
							</script>
						</div>
					</form>
				</div>
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
	</fieldset><br/>
	</td>
  </tr>
  
  
</table>


<jsp:include page="footer.jsp" />

</body>

</html:html>
