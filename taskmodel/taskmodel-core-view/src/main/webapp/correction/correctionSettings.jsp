<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:html>
<head>
	<title>Korrektur-Einstellungen</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">

    <style type="text/css">
        @import "http://o.aolcdn.com/dojo/0.9.0/dijit/themes/tundra/tundra.css";
        @import "http://o.aolcdn.com/dojo/0.9.0/dojo/dojo.css"
    </style>
    <script type="text/javascript" src="http://o.aolcdn.com/dojo/0.9.0/dojo/dojo.xd.js"
            djConfig="parseOnLoad: true"></script>
    <script type="text/javascript">
            dojo.require("dijit.ProgressBar");
            dojo.require("dojo.parser");
    </script>
     
  <script type="text/javascript" src="http://o.aolcdn.com/dojo/0.9.0/dojo/dojo.xd.js"></script>
  <script type="text/javascript">
    function startReCorrection() {
    	dojo.byId("ReCorrectionButton").disabled="disabled";
	  dijit.byId("jsProgress").update({indeterminate: true});
      dojo.xhrPost( {
        url: '<html:rewrite action="/ajaxAutoReCorrection" paramId="taskId" paramName="taskId"/>', 
        handleAs: "text",
        timeout: 5000,
        
        load: function(response, ioArgs) {
		  alert( "OK, alle Tasklets automatisch nachkorrigiert." );
		  dijit.byId("jsProgress").update({indeterminate: false});
	    	dojo.byId("ReCorrectionButton").disabled=null;
          return response;
        },

        error: function(response, ioArgs) {
          console.error("HTTP status code: ", ioArgs.xhr.status);
		  alert( "Fehler: " + ioArgs.xhr.status );
          return response;
          }
        });
      }
  </script>
  

</head>

<body bgcolor="#FFFFFF" text="#000000" class="tundra">

<jsp:include page="../header.jsp"/>

<div class="correction">

<html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
	<%=pageContext.getAttribute("msg")%>
</html:messages>

<html:errors />

<p class="header">Korrektur-Einstellungen</p>

<table border="0" cellspacing="2" cellpadding="2" width="790">
  <tr bgcolor="#F2F9FF"> 
    <td valign="middle"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"> 
		<html:link action="/tutorCorrectionOverview" paramId="taskId" paramName="taskId">Korrektur-Übersicht</html:link>
	</td>
  </tr>
  <tr bgcolor="#F2F9FF"> 
	<td><br>
	<fieldset>
		<legend>Automatische Nachkorrektur</legend>
		
			<div dojoType="dijit.ProgressBar"
	                   id="jsProgress"
	                   indeterminate="false"
	                   style="width: 300px;"></div>
	        <input type="button" value="Starten" id="ReCorrectionButton" onclick="startReCorrection();" />
	        <div id="countOff"></div>

    
	</fieldset><br>
  
 <c:if test="${privileged}">
  <fieldset>
    <legend>Korrekturen verteilen auf</legend>

    <html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
      <%=pageContext.getAttribute("msg")%>
    </html:messages>
    
  <html:form action="/bulkAssignTasklets">
    <input type="hidden" name="taskId" value="${taskId}"/>
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
  
	</td>
  </tr>
  
</table>


</div>

<jsp:include page="../footer.jsp" />

</body>

</html:html>
