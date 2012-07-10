<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:html>
<head>
	<meta http-equiv="expires" content="0">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
	<title>Bearbeitung: ${Task.title}</title>
</head>
<body>

<jsp:include page="header.jsp" />
<!--script type="text/javascript" src="/taskmodel-core-view/jquery.js"></script>
<script type="text/javascript" src="/taskmodel-core-view/thickbox.js"></script>
<link rel="stylesheet" href="/taskmodel-core-view/thickbox.css" type="text/css" media="screen" /-->

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

function LeavePageManager(){
	this.callback = function() {};
	this.registerCallback = function(callbackFunction) {
		this.callback = (this.callback).andThen(callbackFunction);
	}
}

var leavePageManager = new LeavePageManager();


function setModified(b){
	modified = b;
}

function setModified(){
	modified = true;
}

function leave(ziel){

	leavePageManager.callback();

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

function send(){

	leavePageManager.callback();

	if (this.modified){
		if( !confirm("Sie haben Ihre Änderungen an dieser Seite noch nicht gespeichert.\n\nOK - Änderungen verwerfen und abgeben\nAbbrechen - zurück zur Bearbeitung") )
			return false;
	}

	if (!this.everythingDone){
		inputValue = prompt("Sie haben noch nicht alle Aufgaben bearbeitet. \nWollen Sie wirklich abgeben? \n\n(Tippen Sie dazu das Wort \"ABGEBEN\")",'');
		return "ABGEBEN"==inputValue;
	}else{
	    return confirm("Mit der Abgabe Ihrer Aufgaben beenden Sie die Bearbeitung. \nSie können diese danach NICHT mehr ändern!" );
	}

}

function nutzeUhr()
{
	if (this.useTime){
		getElem( "id","UhrTop",null).style.display= "block";
		timer1()
	}else return;
}

function timer1()
{
	var remaining_min = Math.floor( this.mseconds >= 0 ? this.mseconds / 60000 : 0 );
	var remaining_sec = Math.floor( this.mseconds >= 0 ? this.mseconds / 1000 : 0 );
	if ( remaining_min < 1 ){
        
		var rt_late = "noch " + format_time(remaining_min,remaining_sec%60)+ " verbleibend";
        
		setCont("id","Uhr",null,rt_late);
		getElem( "id","Uhr",null).style.fontWeight = "bold";
		getElem( "id","Uhr",null).style.visibility = "visible";
		setCont("id","UhrTop",null,format_time(remaining_min,remaining_sec%60));
	}else{
		var rt = "noch " + remaining_min + " min verbleibend";
		setCont("id","Uhr",null,"noch "+remaining_min+" min verbleiben");
		setCont("id","UhrTop",null,format_time(remaining_min,remaining_sec%60));
	}

	if( remaining_min < 5 ){
		getElem( "id","Uhr",null).style.color = "red";
        var uhrTop = getElem( "id","UhrTop",null);
        uhrTop.style.color = "red";
        uhrTop.style.borderLeftColor = "red";
        uhrTop.style.borderTopColor = "red";
        uhrTop.style.borderBottomColor = "red";
        
	}
    if( remaining_min < 3){
		getElem( "id","UhrTop",null).style.backgroundColor = "white";
    }
    

	if( this.mseconds <= 0 ){
		for( var i = 0; i<500; i++ ){
			if( getElem("id","TimeOver_"+i,null) != null ){
				getElem("id","TimeOver_"+i,null).style.visibility = "visible";
			}
			else{
				break;
			}
		}
	}

	this.mseconds = this.mseconds - 1000;

	window.setTimeout('timer1()',1000);
}
function format_time(min,sec){
 var val = "";
 if(min<10)
   val+="0";
 val+=min+":"; 
 if(sec<10)
   val+="0";
 val+=sec;
 return val;
}
function checkedLink(name, target, linkClass)
{
	document.write("<a class=" +linkClass+ " href=javascript:leave('" +escape( target ) + "')>" +name+ "</a>");
}

//-->
</script>


<jsp:include page="language.jsp" />


<br><br>
<table border="0" cellspacing="0" cellpadding="5" width="790">
  <tr>

    <td bgcolor="#F2F9FF" class="complexTaskNav">
      <div align="left">
	  	<a href="${ReturnURL}" class="ComplexTaskLink">
	  	<img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16" align="absbottom" hspace="4px">Hauptseite</a>
		</div>
    </td>

    <td bgcolor="#F2F9FF" class="complexTaskNav">&nbsp;</div></td>

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
			<a class="ComplexTaskNavLegend" href="#" onclick="document.getElementById('navHelp').style.display='block';">
			<img src="<%= request.getContextPath() %>/icons/small/help.gif" width="14" height="14" align="absbottom" border="0">Symbol-Legende
			</a>
			</td>
            <td>&nbsp;&nbsp;&nbsp;</td>
          </tr>
        </table>
      </div>
      <jsp:include page="NavigationHelp.jsp" />
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
          <td class="ComplexTaskSubmitText">${Task.numOfProcessedSubtasklets} / ${Task.numOfSubtasklets}: ${Task.processPercentage}</td>
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
      <br>
      <br>
      <br>
      <form method="post" action="<html:rewrite action="/savePage"/>" onSubmit="return preSaveManager.callback();">
	    <input type="hidden" name="hashCode" value="${Task.hashCode}">
	    <input type="hidden" name="id" value="${Task.taskId}">
	    <!-- continue after saving -->
	    <input type="hidden" name="todo" value="continue">

		<% int i = 0; %>

		<c:forEach items="${SubTasklets}" var="SubTasklet">
			<fieldset class="complexTask"><legend>Aufgabe ${SubTasklet.virtualSubTaskletNumber}</legend>
			<table width="100%" cellspacing="0" cellpadding="0"><tr><td align="center" valign="top" class="ComplexTaskHint">
			<c:if test="${SubTasklet.hint != null}">
				${SubTasklet.hint}
			</c:if>
			</td><td valign="top" align="right" class="ComplexTaskHint">
				${SubTasklet.reachablePoints} Punkt<c:if test="${SubTasklet.reachablePoints != 1}">e</c:if>
			</td></tr></table><br><br><div class="problem">
				${SubTasklet.problem}
			</div>
			<br><br>
				${SubTasklet.renderedHTML}
			<br><br>
			<c:if test="${SubTasklet.interactiveFeedback}">
					<div class="problem" id="taskletFeedback_${SubTasklet.virtualSubTaskletNumber}" style="display:none"><br><br>
						<fieldset>
							<legend>Korrekturinformationen zum letzten Versuch:</legend>
							${SubTasklet.correctedHTML}<br><br>
						</fieldset>
					</div>
					<table width="100%">
						<tr>
						<td valign="top">
            			</td>
            			<td valign="top">
   							<c:if test="${SubTasklet.corrected}">
   								<input alt="#TB_inline?height=500&width=600&inlineId=taskletFeedback_${SubTasklet.virtualSubTaskletNumber}" title="Letzter Korrekturstand" class="thickbox" type="button" value="Korrektur zeigen" />
   							</c:if>
            			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            			<td valign="top">
              				<p align="right">
                				<input type="submit" name="doAutoCorrection_${SubTasklet.virtualSubTaskletNumber}" value="Neu bewerten">
                				<br>
                				<br>
              				</p>
              			</td>
          				</tr>
        			</table>

			</c:if>
			<br></fieldset>
				<div id="TimeOver_<%=i++%>" style="visibility:hidden; color:red; font-size:12px" align="right">Bearbeitungszeit abgelaufen</div>
			<br>

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
