<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>注册成功 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>

<script type="text/javascript">
$(document).ready(function(){
	window.setInterval("run();", 1000);
});
function run(){
	var s = $("#mm");
	if(s.text() == 1){
		window.location.href='/home.html';
		return false;
	}
	s.text(s.text() * 1 - 1);
}

</script>
</head>
<body>
<div class="hdover">
	<div class="topline"></div>
	<div class="hdline">
		<div class="header">
			<div class="hdlogo">
				<h1>
					<a href="/">
						<img src="../images/gz_03.png"/>
					</a>
				</h1>
			</div>
			<div class="hdregbar">
				<p><input type="button" value="" onclick="window.open('/','_blank');" class="btnReturn"/></p>
			</div>
			<div class="c"></div>
		</div>
	</div>
</div>
<div class="reger">
	<div class="regerw">
		<h2></h2>
		<div class="regbox">
			<div class="rbinfo">
				<div class="rbinfow">
					<div class="rbform" style="line-height: 25px; font-size: 16px; color: #666; height: 430px;">
						恭喜您注册成功！<span id="mm">3</span>秒后跳转到主页。
						<br/>如果不能跳转请点击<a href="/" style="color: #f00;">这里</a>.				
					</div>
				</div>
			</div>
			<div class="rblogin">
				<div class="rbloginw">
					<h3></h3>
					<div class="greenBtn" style="margin: 10px;">
							<a class=""  title="登录" href="/login.html">登录</a>
					</div>
					<div class="rblogintip">已有果子联盟账号，请直接登录</div>
				</div>
			</div>
			<div class="c"></div>
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
