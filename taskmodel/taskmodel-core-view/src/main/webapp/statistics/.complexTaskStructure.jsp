<html><head>
	<title>Strukturelle Übersicht von &quot;${Root.title}&quot;</title>
	<link rel="stylesheet" href="/format.css" type="text/css"/>
</head><body bgcolor="#FFFFFF" text="#000000">

<jsp:include page="../header.jsp"/>

<div class="correction">

<html:messages message="true" id="msg" header="messages.header" footer="messages.footer">
	&lt;%=pageContext.getAttribute(&quot;msg&quot;)%&gt;
</html:messages>

<html:errors/>

<p class="header">Strukturelle Übersicht von &quot;${Root.title}&quot;</p>

<table border="0" cellspacing="2" cellpadding="2" width="790">
  <tr bgcolor="#F2F9FF"> 
    <td valign="middle"><img src="<%= request.getContextPath() %>/pics/exit.gif" width="20" height="16"/> 
		<a paramId="taskId" paramProperty="taskId" action="/tutorCorrectionOverview" paramName="Root">Korrektur-Übersicht</a>
	</td>
  </tr>
  <tr bgcolor="#F2F9FF"> 
	<td><br/>
	<fieldset>

		Titel: <b>${Root.title}</b>
		<br/><br/>
		<ul>
			<c:forEach items="${Root.categories}" var="category">
				
				<li>Kategorie &quot;<b>${category.title}</b>&quot; (${category.id})
					<ul>
						<c:forEach items="${category.blocks}" var="block">
							
							<li>Block ${block.index} vom Typ &quot;<b>${block.type}</b>&quot;
								<ul>
									<c:if test="${block.type == "mc"}">
										<li><img src="<%= request.getContextPath() %>/pics/excel.gif" align="bottom"/> <a href="excelReport/MCBlockReport_${Root.taskId},${category.id},${block.index}.xls">MC-Auswertung</a></li>
									</c:if>
								</ul>
							</li>
							<br/>
					
						</c:forEach>
					</ul>
				</li>
				<br/>
			</c:forEach>
        </ul>    
	</fieldset><br/>
	<fieldset>
                                                                                                                               
        <chart:chart id="line" title="Punkte-Histogramm" type="histogram" xaxislabel="Gesamtpunktzahl" xaxisinteger="true" yaxislabel="Häufigkeit">
            <chart:data>
                <chart:producer id="stats"/>
            </chart:data>
        </chart:chart>
        <p/>
        <chart:img chartid="line" renderer="cewolf" width="600" height="500"/>
        <p/>
        <chart:chart id="boxes" title="Punkteverteilung pro Aufgabentyp" type="box"> 
            <chart:data>
                <chart:producer id="boxes"/>
            </chart:data>
        </chart:chart>
        <p/>
        <chart:img chartid="boxes" renderer="cewolf" width="600" height="500"/>
	</fieldset>
	</td>
  </tr>
  
</table>


</div>

<jsp:include page="../footer.jsp"/>

</body></html>