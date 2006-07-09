<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="de.thorstenberger.taskmodel.view.tree.DataNode" %>
<%@ page import="de.thorstenberger.taskmodel.view.tree.DataNodeFormatter" %>
<%@ page import="de.thorstenberger.taskmodel.view.tree.TreeBuilder" %>


<script type="text/javascript" src="<%= request.getContextPath() %>/dtree.js"></script>

<%
	DataNode root = (DataNode) request.getAttribute( "rootNode" );
	DataNodeFormatter formatter = (DataNodeFormatter) request.getAttribute( "nodeFormatter" );
	TreeBuilder tb = new TreeBuilder();
	tb.setFormatter( formatter );
%>


<div class="dtree">
<script type="text/javascript">
<!--

		d = new dTree('d');

		<%= tb.getTree( root, request ) %>

		document.write(d);
		
		<c:if test="${expanded}">
			d.openAll();
		</c:if>
		
//-->
</script>
</div>
