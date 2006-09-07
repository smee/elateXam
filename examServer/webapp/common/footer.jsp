<%@ include file="/common/taglibs.jsp" %>

<!-- Beginn Footer -->
<!br><!br><!br>
<table border=0 cellspacing=0 cellpadding=0 bgcolor="#035E8D" width=100%>
 <tr>
   <td><img src="<%= request.getContextPath() %>/pics/pixel.gif" width=1 height=1></td>
 </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="copytext">
				    <span class="left">
				        <c:if test="${pageContext.request.remoteUser != null}">
				          <fmt:message key="user.status"/> <authz:authentication operation="fullName"/>
				        </c:if>
				    </span>                
                
                </td>
                <td valign="top" class="copytext" align="right">&nbsp;</td>
              </tr>
            </table>
            <br>
            </td>
          </tr></table>

		</td>
		<td width=1 bgcolor=#000000 !bgcolor="#035E8D"><img src="<%= request.getContextPath() %>/pics/pixel.gif" width=1 height=1><br></td>
		<td width=40 bgcolor="#cccccc" background="<%= request.getContextPath() %>/pics/rshadow.jpg">&nbsp;</td>
</table>

