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
<link href="<%=webContext%>/css/login.css" rel="stylesheet" />
<script type="text/javascript" src="<%=webContext%>/js/common.js"></script>
<script type="text/javascript" src="<%=webContext%>/js/js_z/md5.js"></script>
<script type="text/javascript">
function submitForm(){
	var userName =  document.loginForm.elements["userName"];
	var password = document.loginForm.elements["password2"];
	if(userName.value.length<=0){alert("请输入用户名！");userName.focus();return false;}
	if(password.value.length<=0){alert("请输入密码！");password.focus();return false;}else {
		document.getElementById('password').value=hex_md5(document.getElementById('password2').value).toUpperCase();
	}
	return true;
}
function loadPage(){
	var err = document.getElementById("errors");
	if(err && err.value!=""){
		if(err.value=='browse_not_support'){
			alert("提示：当前浏览器不支持本系统！\n\n支持：Internet Explorer浏览器(IE6、IE7、IE8)。\n\n请使用以上浏览器登录，谢谢！");
		}
		if(err.value=='1'){
			alert("用户名或密码不正确！");
		}
		if(err.value=='-1' || err.value=='expired'){
			alert("账号已过期！");
		}
		if(err.value=='no-learnDays'){
			alert('账号已过期！');
		}
		if(err.value=='2'){
			alert("账号无权限");
		}
		if(err.value=='3'){
			alert("此账号已登录，不能重复登录！");
		}
	}
	document.loginForm.elements["userName"].focus();
}
window.onload = function(){
	loadPage();
};
</script>
<script type="text/javascript">
if( window.top != window.self ){
	//页面是在框架中打开的
	parent.location.reload(); 
}
</script>
</head>

<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="content">
	<header class="title">SIGN IN</header>
	<div class="box">
		<p class="title"><span>Account</span> </p>
		<div class="from">
			<form name="loginForm" target="_top" onsubmit="return submitForm()" method="post" action="<c:url value='/permission/loginAction.do?method=login'/>">
			<div class="item">
				<input class="input" maxlength="16" id="userName" name="userName"/>
				<label class="ico username"></label>
			</div>
			<div class="item">
				<input class="input" type="hidden" maxlength="18" id="password" name="password"/>
				<input class="input" type="password" maxlength="18" id="password2" name="password2"/>
				<label class="ico password"></label>
			</div>
			<div class="item">
				<button type="submit" class="btn btn-748f62 fr" >登录</button>
			</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>
