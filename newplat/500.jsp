<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<html>
<head>
<title>出错了!!- <c:out value="${slogan}"/></title>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/css/error.css" rel="stylesheet" type="text/css"/>
<%  
    response.setStatus(200);   
%> 
</head>
<body>

<div class="bg">
	<div class="cont">
		<div class="c1"><img src="/images/errpage/404/01.png" class="img1"></div>
		<h2>出错了....请稍后再次访问或联系管理员</h2>
		<div class="c2"><a href="#" onclick="history.back()" class="re">回上一页</a><a href="/login.html" class="home">网站首页</a></div>
		<div class="c3"><a href="/" class="c3">果子联盟</a>提醒您 - 您可能输入了错误的网址，或者该网页已删除或移动</div>
	</div>
</div>
</body>
</html>