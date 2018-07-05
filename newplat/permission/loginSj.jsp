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
<title>SIGN IN</title>
<link href="../css/login.css" rel="stylesheet" />
<script type="text/javascript" src="<%=webContext%>/js/common.js"></script>
<script type="text/javascript">
function submitForm(){
	var userName =  document.loginForm.elements["userName"];
	var password = document.loginForm.elements["password"];
	if(userName.value.length<=0){alert("请输入用户名！");userName.focus();return false;}
	if(password.value.length<=0){alert("请输入密码！");password.focus();return false;}
	
	return true;	
}
function loadPage(){
	var err = document.getElementById("errors");
	if(err && err.value!=""){
		if(err.value=='1'){
			alert("用户名或密码不正确！");
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
	<header class="title">SIGN IN</header>
	<div class="box">
		<p class="title"><span>Account</span> </p>
		<div class="from">
			<form name="loginForm"  onsubmit="return submitForm()" method="post" action="<c:url value='/permission/loginAction.do?method=loginSj'/>">
			<div class="item">
				<input class="input" maxlength="16" id="userName" name="userName"/>
				<label class="ico username"></label>
			</div>
			<div class="item">
				<input class="input" type="password" maxlength="18" id="password" name="password"/>
				<label class="ico password"></label>
			</div>
			<div class="item">
				<button type="submit" class="btn btn-748f62 fr" >Sign in</button>
			</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>