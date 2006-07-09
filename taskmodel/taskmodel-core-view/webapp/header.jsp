<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<table cellspacing=0 cellpadding=0 border=0 bgcolor="#ffffff" height=100% style="height:100%" align=center>
<tr>
	<td width=35 bgcolor="#cccccc" background="<%= request.getContextPath() %>/pics/lshadow.jpg">&nbsp;</td>
	<td width=1 bgcolor=#000000 !bgcolor="#035E8D"><img src="<%= request.getContextPath() %>/pics/pixel.gif" width=1 height=1><br></td>
	<!-- workaround fuer IE #&%$* -->
    <td width=628 valign=top style="background-image:url(<%= request.getContextPath() %>/pics/header_paedagogik.jpg); background-repeat:repeat-x;" background="<%= request.getContextPath() %>/pics/header_paedagogik.jpg">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="110">&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
      </table>
      <table border=0 width=100% cellpadding=10>
	<tr>
    <td>
	<table border=0 cellspacing=0 cellpadding=0 width=100%>
	 <tr>
	   <td bgcolor="#035E8D"><img src="<%= request.getContextPath() %>/pics/pixel.gif" width=1 height=1></td>
	 </tr>
	</table>
<!-- Ende Header -->