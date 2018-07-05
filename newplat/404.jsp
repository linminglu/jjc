<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file ="/common/inc_include.jsp"%>

<html>
<head>
<title>哎呀…您访问的页面不存在 - <c:out value="${slogan}"/></title>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/css/error.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="bg">
	<div class="cont">
		<div class="c1"><img src="/images/errpage/404/01.png" class="img1"/></div>
		<h2>哎呀…您访问的页面不存在</h2>
		<div class="c2"><a href="javascript:history.back();"  class="re">回上一页</a><a href="/login.html" class="home">网站首页</a></div>
	</div>
</div>
</body>
</html>
