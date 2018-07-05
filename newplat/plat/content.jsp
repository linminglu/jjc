<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ram.model.User"%>
<%@ page import="com.apps.Constants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
</head>
<body>
<%
User user=(User)session.getAttribute("loginUser");
String userType=user.getUserType();
if(userType.equals(Constants.USER_TYPE_ADMIN)){
	response.sendRedirect(Constants.getWebappContext() + "/user/managerAction.do?method=userList");
}else if(userType.equals(Constants.USER_TYPE_ADMIN_SHANGJIA)){
// 	response.sendRedirect(Constants.getWebappContext() + "/order/orderAction.do?method=init");
}else{
	//response.sendRedirect(Constants.getWebappContext() + "/");
}
%>
</body>
</html>