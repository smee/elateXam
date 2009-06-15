<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:html>
<head>
	<title>Korrektur</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
</head>

<body bgcolor="#FFFFFF" text="#000000">

<jsp:include page="../header.jsp" />

<p class="header">L&ouml;sung von ${Correction.userId}</p>


<table border="0" cellspacing="2" cellpadding="2" width="790">

<form method="post" action="<html:rewrite action="/saveCorrection"/>">
							<input type="hidden" name="userId" value="${Correction.userId}"/>
							<input type="hidden" name="taskId" value="${Correction.taskId}"/>
							<input type="hidden" name="selectedSubTaskletNum" value="${Correction.subTasklet.virtualSubTaskletNumber}"/>

  <tr bgcolor="#F2F9FF"> 
    <td valign="top"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"> 
    	<html:link action="/tutorCorrectionOverview" paramId="taskId" paramName="Correction" paramProperty="taskId">Korrektur-Übersicht</html:link>
	</td>
	<td valign="top" align="right"><html:link action="/showCorrectionToCorrector" name="Correction" property="loginAndTaskId"><img src="pics/magnifier.gif" border="0" hspace="5">Gesamtansicht</html:link>
	</td>
  </tr>
  <tr> 
    <td width="155" valign="top" bgcolor="#F2F9FF"> <fieldset><legend>Aufgaben</legend> 
      <img src="<%= request.getContextPath() %>/pics/pixel.gif" width="155" height="1"><br>

	<jsp:include page="../dhtmlTree.jsp"/>

 </fieldset> </td>
    <td valign="top" bgcolor="#F2F9FF">
    
    	<c:if test="${Correction.subTasklet != null}">

			<fieldset class="complexTask"><legend>Korrektur - Aufgabe ${Correction.subTasklet.virtualSubTaskletNumber}</legend>

				<table width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" valign="top" class="ComplexTaskHint">
							<c:if test="${Correction.subTasklet.hint != null}">
								${Correction.subTasklet.hint}
							</c:if>
						</td>
						<td valign="top" align="right" class="ComplexTaskHint">
							${Correction.subTasklet.reachablePoints} Punkt<c:if test="${Correction.subTasklet.reachablePoints != 1}">e</c:if>
						</td>
					</tr>
				</table>
				<br>
				<br>
				<div class="problem">
					${Correction.subTasklet.problem}
				</div>
				
				<br><br>

				<c:choose>
					<c:when test="${Correction.subTasklet.correctionHTML != null}">
					        	
							${Correction.subTasklet.correctionHTML}
							
							<br><hr size="1" noshade><div align="right">
							<br>
							</div>
							
					</c:when>
					<c:otherwise>			
							${Correction.subTasklet.correctedHTML}
					</c:otherwise>				
				</c:choose>					


				<c:if test="${Correction.subTasklet.corrected}">

						<br/><br/><font color="red">Aufgabe korrigiert. Korrekturen: 
							<table border="0">
							<c:forEach items="${Correction.subTasklet.corrections}" var="stc">
								<tr>
									<c:choose>
										<c:when test="${stc.auto}">
											<td>automatische Korrektur:</td><td>${stc.points}</td>
										</c:when>
										<c:otherwise>
											<td>${stc.corrector}</td><td>${stc.points}</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach>
							</table>
						</font>
				</c:if>
				
				<c:if test="${Correction.subTasklet.correctionHint != null}">
					<br><br><b>Lösungserwartung:</b><br>
				    ${Correction.subTasklet.correctionHint}
				    <br>				
				</c:if>				
				
				<br>
		</fieldset>
		<br>
	</c:if>

 </td>
  </tr>
  <tr> 
    <td colspan="2" valign="top" bgcolor="#F2F9FF">
    <div class="correction">
	<table border="0" cellspacing="2" cellpadding="2">

  <tr>
          <td valign="top"><fieldset><legend>Korrektur-Bemerkungen</legend> 
          
		<textarea name="annotation" cols="50" rows="7" class="annotation">${Correction.currentCorrectorAnnotation}</textarea>
          <br>
          <br>
			<b>Weitere Annotationen:</b><br/><br/>
			<c:forEach items="${Correction.otherCorrectorAnnotations}" var="annotation">
				<div class="newsheader">${annotation.corrector}</div>
				<div class="newsbody">${annotation.annotation}</div>
				<br/>
			</c:forEach>   

      </fieldset>
          
      </td>
          <td valign="top"> <fieldset><legend>Daten</legend> 
            <table border="0" cellspacing="2" cellpadding="2">
              <tr> 
                <td>Status:</td>
                <td>${Correction.status}</td>

              </tr>
              <tr> 
                <td valign="top">Punkte:</td>
                <td>
                
                	<table border="0">
						<c:forEach items="${Correction.corrections}" var="corr">
							<tr>
								<c:choose>
									<c:when test="${corr.auto}">
										<td>automatische Korrektur:</td><td>${corr.points}</td>
									</c:when>
									<c:otherwise>
										<td>${corr.corrector}</td><td>${corr.points}</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</table>
                
                
                </td>
              </tr>
              <tr> 
                <td>Versuch:</td>
                <td>${Correction.numOfTry}</td>
              </tr>
              <tr>
                <td>Aktuell zugeordneter Korrektor:</td>
                <td>${Correction.correctorLogin}</td>
              </tr>
              <tr>
                <td>Korrektoren-History:</td>
                <td>${Correction.correctorHistory}</td>

              </tr>
            </table>
			</fieldset><br>
            <fieldset><legend>Speichern</legend>
            <div align="center">
	            <input type="submit" name="submit" value="Korrektur speichern">
            </div>
            <br>
            </fieldset> <br>

          </td>
  </tr>
</table>

	 </div>
	  </td>
  </tr>
  
</form>

  <tr bgcolor="#F2F9FF"> 
    <td colspan="2" valign="top">

      <fieldset><legend>Kommentare des Bearbeiters</legend>

		<b>Unbestätigt:</b><br/>
			<c:forEach items="${Correction.nonAcknowledgedAnnotations}" var="annotation">
				<div class="newsheader">${annotation.date}</div>
				<div class="newsbody">${annotation.text}</div>
				<br/>
			</c:forEach>
		<c:if test="${Correction.canAcknowledge}">
			<div align="right">
				<form method="post" action="<html:rewrite action="/acknowledge"/>">
					<input type="hidden" name="userId" value="${Correction.userId}"/>
					<input type="hidden" name="taskId" value="${Correction.taskId}"/>
					<input type="hidden" name="selectedSubTaskletNum" value="${Correction.subTasklet.virtualSubTaskletNumber}"/>
					<input type="submit" name="acknowledged" value="Lesebestätigung setzen"/>
				</form>
			</div>
		</c:if>

		<br/><br/><b>Bestätigt:</b><br/>
			<c:forEach items="${Correction.acknowledgedAnnotations}" var="annotation">
				<div class="newsheader">${annotation.date}</div>
				<div class="newsbody">${annotation.text}</div>
				<br/>
			</c:forEach>          

          <br/><br/>

      </fieldset>
      <br/>
    
    </td>
  </tr>
  
  <tr bgcolor="#F2F9FF"> 
    <td colspan="2" valign="top">

      <fieldset><legend>Korrektoren-Zuordnung</legend>

		<table border="0" cellspacing="10">
			<tr>
				<td>
					<form method="post" action="<html:rewrite action="/unassignTaskletFromCorrector"/>">
												<input type="hidden" name="userId" value="${Correction.userId}"/>
												<input type="hidden" name="taskId" value="${Correction.taskId}"/>
						<input type="submit" name="unassign" value="Zuordnung aufheben" 
							<c:if test="${Correction.correctorLogin == null}">disabled="disabled"</c:if>
						/>
					</form>
				</td>
				<td>
					<form method="post" action="<html:rewrite action="/assignTasklet"/>">
						<input type="hidden" name="userId" value="${Correction.userId}"/>
						<input type="hidden" name="taskId" value="${Correction.taskId}"/>

						<select name="new_corrector" style="margin: 0 1em 0 10em;">
						    <option value="___no_selection" selected="selected">neuer Korrektor:</option>
						    <c:forEach items="${Correction.availableCorrectors}" var="corrector">
							    <option value="${corrector}"
								    <c:if test="${Correction.correctorLogin == corrector}">disabled="disabled"</c:if>
							    >${corrector}</option>
						    </c:forEach>
						</select>
						<input type="submit" name="assign" value="Zuordnen"/>
					</form>
				</td>
			</tr>
		</table>

          <br/>

      </fieldset>
      <br/>
    
    </td>
  </tr>

</table>




<jsp:include page="../footer.jsp" />

</body>

</html:html>
