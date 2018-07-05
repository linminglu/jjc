<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_file.jsp"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title></title>
</head>
<body>

<h1 style="color:red; text-align:center"><c:out value="${msg}"></c:out></h1>
    <div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
	</div>
</body>
</html>