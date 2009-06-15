<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Symbol-Legende</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/format.css" type="text/css">
</head>
<body style="background-color: #ffffff;" bgcolor="#FFFFFF" marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
<table width="200" align="left" valign="top" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td width=200 height=150>
      <div align="center">
	  <table border="0" cellspacing="4" cellpadding="2">
  <tr>
            <td valign="top"><img src="<%= request.getContextPath() %>/icons/page.gif" width="18" height="18"></td>
            <td>Seite wurde noch nicht bearbeitet</td>
  </tr>
  <tr>
            <td valign="top"><img src="<%= request.getContextPath() %>/icons/partlyProcessed.gif" width="18" height="18"></td>
            <td>Seite wurde bereits teilweise bearbeitet</td>
  </tr>
  <tr>
            <td valign="top"><img src="<%= request.getContextPath() %>/icons/processed.gif" width="18" height="18"></td>
            <td>Alle Aufgaben auf dieser Seite wurden bearbeitet</td>
  </tr>
</table>
	  <br></div>
    </td>
	</tr>
<tr>
	<td height=9> 
    </td>
</tr>
<tr>
    <td height=21 align="right" valign="middle" bgcolor="#009933"><a href="javascript:window.close();"><b><font color="#FFFFFF">schließen</font></b></a>&nbsp;&nbsp;</td>
</tr>
</table>
</body>
</html>
