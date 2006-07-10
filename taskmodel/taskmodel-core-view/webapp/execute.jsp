<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
	<title>Bearbeitung: ${Task.title}</title>
</head>
<body>

<jsp:include page="header.jsp" />

<!--
Scripts by Daniel Zimmermann and Thorsten Berger
-->

<!--DHTML für die Uhr-->

<script type="text/javascript" src="dhtml.js"></script>

<!--Das eigentliche Script, mit saemtlichen Funktionen-->
<script type="text/javascript">
<!--
var modified = false; //wurde das Dokument veraendert?
var mseconds = ${Task.remainingTimeMillis}; //Zeit uebergeben in Millisekunden
var useTime = ${Task.timeRestricted}; //ist Aufgabe zeitbeschraenkt?
var everythingDone = ${Task.everythingProcessed}; //Sind alle Aufgaben bearbeitet worden --> Abschicken-Button!

function setModified(b)
{
	modified = b;
}

function setModified(){
	modified = true;
}

function leave(ziel)
{
	if (this.modified)
	{
		if ( confirm("Sie haben Ihre Änderungen an dieser Seite noch nicht gespeichert.\n\nOK - Änderungen verwerfen und Seite verlassen\nAbbrechen - Seite noch nicht verlassen") )
			window.location.href = ziel;
		else
			return;
	}
	else
		window.location.href = ziel;
}

function send()
{

	if (this.modified){
		if( !confirm("Sie haben Ihre Änderungen an dieser Seite noch nicht gespeichert.\n\nOK - Änderungen verwerfen und abgeben\nAbbrechen - zurück zur Bearbeitung") )
			return false;
	}

	var warning = "";

	if (!this.everythingDone){
		warning = "\n\nSie haben noch nicht alle Aufgaben bearbeitet. Wollen Sie wirklich abgeben?";
	}
	
	return confirm("Mit der Abgabe Ihrer Aufgaben beenden Sie die Bearbeitung." + warning );

}

function nutzeUhr()
{
	if (this.useTime)
		timer1()
	else return;
}

function timer1()
{
	var remaining = Math.floor( this.mseconds >= 0 ? this.mseconds / 60000 : 0 );
	var rt = "noch " + remaining + " min verbleibend";
	if (DHTML) setCont("id","Uhr",null,rt); //id-Attribut dessen Name Uhr ist
//	if ( DHTML && remaining == 0 )
//				setCont("id", "Warnung", null, "weniger als eine Minute verbleibend");
	this.mseconds = this.mseconds - 1000;

	window.setTimeout('timer1()',1000);
}

function checkedLink(name, target, linkClass)
{
	document.write("<a class=" +linkClass+ " href=javascript:leave('" +target+ "')>" +name+ "</a>");
}

function fenster(file,breite,hoehe) {
                var wf ="";
                wf = wf + "width="+breite;
                wf = wf + ",height="+hoehe;
                wf = wf + ",resizable=" + "no";
                wf = wf + ",scrollbars=" + "no";
                wf = wf + ",menubar=" + "no";
                wf = wf + ",toolbar=" + "no";
                wf = wf + ",directories=" + "no";
                wf = wf + ",location=" + "no";
                wf = wf + ",status=" + "no";
                wf = wf + ",screenX=" + "10";
                wf = wf + ",screenY=" + "10";
                var auf ="";
                auf = file;
                window.open(auf,"help",wf);
}

Function.prototype.andThen=function(g){
	var f = this;
	return function(){
		f();g();
	}
}

function PreSaveManager(){
	this.callback = function() {};
	this.registerCallback = function(callbackFunction) {
		this.callback = (this.callback).andThen(callbackFunction);
	}
}

var preSaveManager = new PreSaveManager();

//-->
</script>

<br><br>

<table border="0" cellspacing="0" cellpadding="5" width="790">
  <tr>
      
    <td bgcolor="#F2F9FF" class="complexTaskNav"> 
      <div align="left"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16" align="absbottom"> 
        <script type="text/javascript">
		 <!--
			checkedLink("Hauptseite", "${ReturnURL}", "ComplexTaskLink"); 
		 //-->
		</script>
		  <noscript>
		  	<a href="${ReturnURL}" class="ComplexTaskLink">Hauptseite</a>
		  </noscript>
		</div>
    </td>
	  
    <td bgcolor="#F2F9FF" class="complexTaskNav">&nbsp;</td>
	  
    <td bgcolor="#F2F9FF" class="complexTaskNav"> 
      <div align="right">Seite ${Task.page}&nbsp;/&nbsp;${Task.numOfPages}&nbsp;&nbsp;<img src="<%= request.getContextPath() %>/pics/pages.gif" width="11" height="12" align="absmiddle"></div>
    </td>
  </tr>

  <tr> 
    <td valign="top" bgcolor="#F2F9FF" width="170" class="complexTaskNav"> 
	<img src="<%= request.getContextPath() %>/pics/pixel.gif" width="170" height="1">
	<br>
      <span class="ComplexNavHeadline">
      Navigation</span><br>
      <img src="<%= request.getContextPath() %>/pics/pixel.gif" width="1" height="4"><br>
      <fieldset><jsp:include page="dhtmlTree.jsp"/><br></fieldset> 
      <div align="right"> 
        <table border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td bgcolor="#E1F4FF">
			<a class="ComplexTaskNavLegend" href="javascript:fenster('NavigationHelp.jsp',200,180)">
			<img src="<%= request.getContextPath() %>/icons/small/help.gif" width="14" height="14" align="absbottom" border="0">Symbol-Legende
			</a>
			</td>
            <td>&nbsp;&nbsp;&nbsp;</td>
          </tr>
        </table>
      </div>
	  <br>
<br>

      <br>
	<span class="ComplexNavHeadline">Bearbeitung</span> <br>
      <br>
      <table border="0" cellspacing="2" cellpadding="0" align="center">
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top">Versuch:</td>
          <td class="ComplexTaskSubmitText">${Task.actualTry} / ${Task.numOfTries}</td>
        </tr>
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top" colspan="2" bgcolor="#FFFFFF"> 
            <img src="<%= request.getContextPath() %>/pics/pixel.gif" width="1" height="2"></td>
        </tr>
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top">Start:</td>
          <td class="ComplexTaskSubmitText">${Task.tryStartTime}</td>
        </tr>
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top" colspan="2" bgcolor="#FFFFFF">
		  <img src="<%= request.getContextPath() %>/pics/pixel.gif" width="1" height="2"></td>
        </tr>
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top">bearbeitet:</td>
          <td class="ComplexTaskSubmitText">${Task.processPercentage}</td>
        </tr>
      </table>

	<span class="ComplexNavHeadline"><br><br>
      Abgabe</span><br>
      <br>
      <table border="0" cellspacing="0" cellpadding="2" align="center">
        <tr> 
          <td class="ComplexTaskSubmitText" valign="top">bis: ${Task.deadline}</td>
        </tr>
        <tr> 
          <td class="ComplexTaskSubmitText"><div id="Uhr" class="ComplexTaskSubmitText">&nbsp;</div>
 		<script type="text/javascript">
		 <!--
			nutzeUhr();
		 //-->
		</script>		  
		  </td>
        </tr>
      </table>
      <br>
      <br>
      <form method="post" action="<html:rewrite action="/commit"/>" onSubmit=" return send() ">
   	    <input type="hidden" name="id" value="${Task.taskId}">
        <div align="center"> 
          <input type="submit" name="submit" value="Abgeben">
        </div>
      </form>



    </td>
    <td width="20">&nbsp;</td>
    <td width="600" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td valign="top">${Task.userName}<br>
            ${Task.login}</td>
          <td valign="top" align="right" class="headerblau">
            ${Task.title}
          </td>
        </tr>
      </table>
      <br><!--<div id="Warnung" style="color: red;">&nbsp;</div>-->
      <br>
      <br>
      <form method="post" action="<html:rewrite action="/savePage"/>" onSubmit="return preSaveManager.callback();">
	    <input type="hidden" name="hashCode" value="${Task.hashCode}">
	    <input type="hidden" name="id" value="${Task.taskId}">
	    <!-- continue after saving -->
	    <input type="hidden" name="todo" value="continue">

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
			<br></fieldset><br>
			
		</c:forEach>


		<br>
        <br>
        <hr size="1" noshade>
        <br>
        <table width="100%">
          <tr> 
            <td valign="top"> <br>
            </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td valign="top"> 
              <p align="right"> 
			  	<input type="hidden" name="page" value="${Task.page}">
                <input type="submit" name="save" value="Speichern">
                <br>
                <br>
                Diese Seite abspeichern.<br>
              </p>
              </td>
          </tr>
        </table>
       </form>
    </td>
  </tr>
</table>

<jsp:include page="footer.jsp" />

</body>
</html:html>
