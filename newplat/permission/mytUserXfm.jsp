<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/inc_include.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta content="telephone=no" name="format-detection">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta http-equiv="X-UA-Compatible" content="edge">
<title>激活消费码</title>
<link href="../css/login.css" rel="stylesheet" />
<!-- <script type="text/javascript" src="../js/common.js"></script> -->
<script type="text/javascript">
function submitForm(){
	var userName =  document.loginForm.elements["userName"];
	if(userName.value.length<=0){alert("请输入消费码！");userName.focus();return false;}
	
	return true;	
}
function loadPage(){
	var err = document.getElementById("errors");
	if(err && err.value!=""){
		if(err.value=='4'){
			alert("消费码激活成功！");
		}else if(err.value == '2'){
			alert("此消费码之前已激活，请确认！");
		}else if(err.value == '3'){
			alert("消费码已过期！");
		}else{
			alert("无效消费码，请确认！");
		}
	}
	document.loginForm.elements["userName"].focus();
}
window.onload = function(){
	loadPage();
};
</script>
</head>
<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="content">
	<header class="title">激活消费码</header>
	<div class="box">
		<p class="title"><span>消费码</span> </p>
		<div class="from">
			<form name="loginForm" target="_top" onsubmit="return submitForm()" method="post" action="<c:url value='/permission/loginAction.do?method=saveXfm'/>">
			<div class="item">
				<input class="input" maxlength="16" id="userName" name="userName" value="<c:out value="${userName}"/>"/>
			</div>
			<div class="item">
				<a class="btn btn-grey fr" href="/sj.html">退出</a>
				<button type="submit" class="btn btn-748f62 fr" >激活</button>
			</div>
			</form>
		</div>
	</div>
</div>
	
</body>
</html>