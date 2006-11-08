<%@ include file="/common/taglibs.jsp"%>

<table cellpadding="15" width="100%">
<tr>
<td>

<br/><br/>

<table width="100%">
<tr><th>
<p class="header">
<c:out value="${task.title}"/>
</p>
</th>
</tr>
</table>

<br/>
<br/>

<c:out value="${task.specificDescription}" escapeXml="false"/>

<br>
<br>
<fieldset class="tasks"><legend>Informationen zur Bearbeitung</legend>
        
  <table border="0" cellspacing="2" cellpadding="2">
    <tr> 
      <td>Maximale Bearbeitungszeit:</td>
      <td>
      <c:choose>
      	<c:when test="${task.time != null}">
      		<c:out value="${task.time}"/> min
      	</c:when>
      	<c:otherwise>
      		-
      	</c:otherwise>
      </c:choose>
      </td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    </tr>
    <tr>
      
    <td>Bereits durchgef&uuml;hrte L&ouml;sungsversuche:</td>
      <td><c:out value="${task.usedTries}"/></td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td>Anzahl maximaler L&ouml;sungsversuche:</td>
      <td><c:out value="${task.maxTries}"/></td>
      <td>&nbsp;</td>
    </tr>
  </table>

<br>
</fieldset> <br>

<fieldset class="tasks"><legend>Informationen zur Korrektur</legend>
Korrekturstand des letzten Versuchs:
<c:choose>
	<c:when test="${task.correctionVisible}">
		<a href="/taskmodel-core-view/showSolution.do?id=<c:out value="${task.id}"/>">verfügbar</a>
	</c:when>
	<c:otherwise>
		nicht verfügbar
	</c:otherwise>
</c:choose>
<br>
</fieldset>
<br>

<fieldset class="tasks"><legend>Start</legend>
<table border="0" cellspacing="2" cellpadding="2" width="100%">
  <tr>
    <td valign="top" width="50%">
	<fieldset class="tasks"><legend>Neuer L&ouml;sungsversuch</legend>
      <form method="get" action="/taskmodel-core-view/execute.do">
	<input type="submit" value="Starten"
		<c:choose>
			<c:when test="${!task.canStartNewTry}"> disabled="true"</c:when>
		</c:choose>
	>
	<input type="hidden" name="action" value="ComplexTaskExecute">
	<input type="hidden" name="id" value="<c:out value="${task.id}"/>">
	<input type="hidden" name="todo" value="new">
	<input type="hidden" name="try" value="<c:out value="${task.usedTries + 1}"/>">
	</form>
	<c:choose>
		<c:when test="${task.canContinueTry}">
			&nbsp;
		</c:when>
		<c:when test="${task.startText != null}">
			<c:out value="${task.startText}" escapeXml="false"/>
		</c:when>
		<c:otherwise>
			Starten Sie Ihren L&ouml;sungsversuch erst, wenn Sie sich sicher sind, die Aufgabenstellung bew&auml;ltigen zu k&ouml;nnen.<br/>
			Ihnen wird ein L&ouml;sungsversuch abgezogen.</c:otherwise>
	</c:choose>
	  </fieldset>
	  </td>
    <td valign="top" width="50%">
	<fieldset class="tasks"><legend>L&ouml;sungsversuch fortsetzen</legend>
	    <form method="get" action="/taskmodel-core-view/execute.do">
			<input type="submit" value="Fortsetzen"
				<c:choose>
					<c:when test="${!task.canContinueTry}"> disabled="true"</c:when>
				</c:choose>
			>
			<input type="hidden" name="action" value="ComplexTaskExecute">
			<input type="hidden" name="id" value="<c:out value="${task.id}"/>">
			<input type="hidden" name="todo" value="continue">
			<input type="hidden" name="page" value="1">
		</form>
	<c:if test="${task.canContinueTry}">
		Ihr letzter L&ouml;sungsversuch ist noch aktiv und kann fortgesetzt werden.<br>
		<c:if test="${task.time != null}">
		Abgabezeit: <b><c:out value="${task.ctDeadline}"/></b>
		</c:if>
	</c:if>
	</fieldset>
	</td>
  </tr>
</table>
</fieldset>
<br><br>
<fieldset class="tasks"><legend>Allgemeine Hinweise</legend> 
<p><b>
  Die Bearbeitung</b>: <br>

<ul>
  <li>Mit der Schaltfl&auml;che &quot;Starten&quot; beginnen Sie Ihren L&ouml;sungsversuch.</li>
  <li>Die Bearbeitung der Aufgaben erfolgt seitenweise.</li>
  <li>Ihre Eingaben werden <b>erst dann wirksam</b>, wenn Sie 
  die Schaltfl&auml;che &quot;Speichern&quot; bet&auml;tigt haben, die Sie jeweils 
  unten auf einer Seite finden. Was Sie nicht speichern, geht nach Verlassen einer 
  Seite unwiederbringlich verloren! Sollten Sie einmal das Speichern vor dem Verlassen einer Seite vergessen, 
  erscheint ein entsprechender Warnhinweis. Klicken Sie auf "Abbrechen", um auf der aktuellen Seite zu bleiben 
  und Ihre Änderungen abspeichern zu können.
    </li>
  <li>Wenn Sie sich durch die Klausur bewegen, benutzen Sie bitte nur die Navigationsleiste 
  links oben. Hier k&ouml;nnen Sie auch sehen, welche Seiten Sie bereits teilweise 
  oder vollst&auml;ndig bearbeitet haben und welche noch nicht. <b>Bitte vermeiden 
  Sie unbedingt, die &quot;Gehe zur&uuml;ck&quot;- und 
  &quot;Gehe vor&quot;-Schaltfl&auml;chen Ihres Browsers zu bet&auml;tigen!</b> 
  Das System kann diese Operationen nicht nachvollziehen, weil sie nur auf dem 
  lokalen PC, der gerade vor Ihnen steht, ablaufen.</li>
  <li>Im Rahmen der Bearbeitungszeit k&ouml;nnen Sie auch wieder zu dieser 
    Seite wechseln ("Hauptseite"), sich aus- und wieder einloggen und diesen L&ouml;sungsversuch 
    jederzeit fortsetzen. Allerdings l&auml;uft die Bearbeitungszeit auch dann 
    weiter.</li>
  <li>Die Bearbeitung endet wenn Sie auf &quot;Abgeben&quot; klicken oder die 
    Bearbeitungszeit abl&auml;uft. Alle bis dahin <b>gespeicherten</b> Seiten 
    flie&szlig;en in die Bewertung ein.</li>
</ul>
<p><br>
<!--<b>
  Die Aufgaben:</b><br>
  <br>
  Es gibt fünf Aufgabentypen: Multiple-Choice-Fragen, 
  Zuordnungsaufgaben, L&uuml;ckentextaufgaben, Aufgaben mit freiem Text und Aufgaben zur graphischen Darstellung. Lesen 
  Sie die Aufgabenstellung <b>genau</b> durch!<br>
  <br>
<ul>
  <li>Bei den Multiple-Choice-Aufgaben sollen Sie unter insgesamt f&uuml;nf Antwortm&ouml;glichkeiten 
    die richtigen herausfinden und markieren. In jeder Aufgabe ist mindestens 
    eine und es sind h&ouml;chstens vier richtige Antwortm&ouml;glichkeiten enthalten. 
    Aufgaben ohne richtige Antwortm&ouml;glichkeit oder mit f&uuml;nf richtigen 
    Antwortm&ouml;glichkeiten werden nicht gestellt. </li>
  <li>Bei Zuordnungsaufgaben sollen Sie den Elementen einer Ausgangsliste (das 
    k&ouml;nnen Begriffe, Jahreszahlen, Personen, Zitate oder anderes geeignetes 
    Material sein) Elemente einer Zielliste zuordnen. Die Elemente der Zielliste 
    finden Sie als Drop-down-Men&uuml; hinter jedem Element der Ausgangsliste. 
    Die Zuordnungen k&ouml;nnen, m&uuml;ssen aber nicht eineindeutig sein, d.h. 
    es kann vorkommen,dass mehreren Elementen der Ausgangsliste dasselbe Element 
    der Zielliste zugeordnet ist.</li>
  <li>Bei L&uuml;ckentextaufgaben sollen Sie in einem vorgegebenen Text an entsprechender 
    Stelle die richtigen Begriffe oder Phrasen einsetzen. Gro&szlig;- und Kleinschreibung 
    wird bei der Korrektur nicht unterschieden.</li>
  <li>Bei Aufgaben mit freier Antwortm&ouml;glichkeit sollen Sie eine Frage mit 
    eigenst&auml;ndigen Formulierungen unter korrekter Benutzung der in der Vorlesung 
    eingef&uuml;hrten erziehungswissenschaftlichen Fachsprache mit einem kurzen 
    Text beantworten. Bitte formulieren Sie in ganzen S&auml;tzen, Stichworte 
    reichen keinesfalls aus. Wenn Sie &uuml;berhaupt Abk&uuml;rzungen verwenden, 
    dann verwenden Sie nur allgemein gebr&auml;uchliche, wie sie im DUDEN stehen.</li>
  <li>Bei Aufgaben zur graphischen Darstellung sollen Sie eine Fragestellung durch Zeichnen einer Graphik lösen.
  Ihnen stehen dazu ein Zeichenbereich, in dem Sie Linien zeichnen können und ein Textbereich für zusätzliche Erklärungen zur Verfügung.
  Weiterhin gibt es eine Rückgängig-Funktion und einen Radiergummi zum Überarbeiten Ihrer Graphik. Bitte beachten Sie, dass es bei diesem
  Aufgabentyp weniger auf eine schöne als vielmehr auf eine konzeptionelle Darstellung Ihrer Lösung ankommt. Vergeuden Sie keine Zeit mit der Korrektur von verwackelten Linien.<br/>
  Technischer Hinweis: Für diesen Aufgabentyp muss Ihr Browser Java unterstützen. Internet Explorer und Mozilla/Firefox installieren gegebenenfalls ein entsprechendes Plugin automatisch beim ersten
  Aufruf nach. Sie können die Java-Laufzeitumgebung von <a href="http://www.java.com/de/download/index.jsp"/>http://www.java.com/de/download/index.jsp</a> auch selbst installieren.
  </li>
</ul>
<br>-->
<b>Die Bewertung</b>:<br>
<br>
Bewertet werden alle bis zum Bearbeitungsende abgespeicherten Seiten. 
<ul>
  <li>Die maximal erreichbare Punktzahl ist bei jeder Aufgabenstellung angegeben. 
  <li>Die minimale Punktzahl pro Aufgabe sind 0 Punkte. 
  <li>Nicht bearbeitete Aufgaben werden mit 0 Punkten bewertet. 

</ul>
<br/>
</fieldset> 

</td>
</tr>
</table>
<br/>
<br/>