<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.ram.model.User"%>
<%@ page import="com.apps.Constants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
</head>
<body>
<logic:empty name="loginUser">
<center>
<script type="text/javascript">
	window.location.href='../permission/loginAction.do?method=init';
</script>
</center>
</logic:empty>
<logic:notEmpty name="loginUser">
<%
response.sendRedirect(Constants.getWebappContext() + "/basedata/baseDataAction.do?method=initSessionList&playCate=2");
%>
</logic:notEmpty>
</body>
</html>