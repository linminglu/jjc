<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.framework.util.ParamUtils"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content=""> 
<meta content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0"> 
<meta content="telephone=no" name="format-detection">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta http-equiv="X-UA-Compatible" content="edge">
<title>确认投注</title>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/mui/mui.min.js"></script>
<script type="text/javascript" src="js/move.js"></script>
<link type="text/css" rel="stylesheet" href="css/mui/mui.min.css" />
<link type="text/css" rel="stylesheet" href="css/base.css" />
</head>
<body>
	<div class="main-content">
		<%
String t = ParamUtils.getParameter(request, "t");//游戏类型
String m = ParamUtils.getParameter(request, "m");//加密
String url="";
if(ParamUtils.chkString(t)){
	if(t.equals("0")){//三分彩
		url="/api/bj3_bet";
	}else if(t.equals("1")){
		url="/api/bjPk10_bet";
	}else if(t.equals("2")){
		url="/api/xjpLu28_bet";
	}else if(t.equals("3")){
		url="/api/cqSsc_bet";
	}else if(t.equals("4")){
		url="/api/bjLu28_bet";
	}else if(t.equals("5")){
		url="/api/gdK10_bet";
	}else if(t.equals("6")){
		url="/api/tjSsc_bet";
	}else if(t.equals("7")){
		url="/api/xjSsc_bet";
	}else if(t.equals("8")){
		url="/api/poker_bet";
	}else if(t.equals("9")){
		url="/api/gdPick11_bet";
	}else if(t.equals("10")){
		url="/api/jsK3_bet";
	}else if(t.equals("11")){
		url="/api/gxK10_bet";
	}else if(t.equals("12")){
		url="/api/markSix_bet";
	}
}
%>
<script type="text/javascript">
$.ajax({
	type: "POST",
	url:move.server+'<%=url%>',
	dataType:'json',
	data:{
		m:'<%=m%>'
	},
	success: function(ret){
		var code=ret.code;
		var msg=ret.msg;
		if(code=='200'){
			mui.alert('投注成功',document.title, function() {
				window.location.href='openapp.longzhicai://betlist';//投注记录
			});
		}else{
			mui.alert(msg,document.title, function() {
				window.location.href='openapp.longzhicai://home';//首页
			});
		}
	},
	error: function (jqXHR, textStatus, errorThrown) {
	}
});
</script>
	</div>
</body>
</html>