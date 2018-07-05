<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script>
function load(){
	//window.open("http://csy/cookie.asp?sn=1");
}
window.onload = function(){
	var vNewUrl = window.location.host + window.location.pathname
	//alert(vNewUrl);
}
</script>
<body>

<%
	Runtime lRuntime = Runtime.getRuntime();
	out.println("Free  Memory: "+lRuntime.freeMemory()/1024/1024+"M"); //内容
	out.println("Max   Memory: "+lRuntime.maxMemory()/1024/1024+"M");  
	out.println("Total Memory: "+lRuntime.totalMemory()/1024/1024+"M");
%>


<hr/>
<%
	String localAddr = request.getRemoteAddr();
String port = request.getRemotePort()+"";
out.println(localAddr+":"+port);
%>
<hr/>
<%
	out.println(com.framework.util.StringUtil.getDateTimeStr());
out.println(request.getSession().getAttribute("loginUser"));
%>
<hr/>
<%@page import="com.apps.Constants"%>
<%
	out.println("路径： "+Constants.getTimerOpen("cqssc.initSession.gf"));
%>
</html>