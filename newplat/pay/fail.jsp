<%@ page language="java" contentType="text/html; charset=UTF-8" %>
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
<title>支付失败</title>
<link type="text/css" rel="stylesheet" href="css/mqpay.css" />
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/move.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	timerObj.rest = 1;
	timerxiazhu = window.setInterval(function(){timerObj.init(timerObj.rest,'timer-fail');},timerObj.interval);
	var r = $("#r").val();
	if(r != ""){
// 		window.location.href = r;
	}
	var device = move.getDevice();
	if(device=='other'){
		window.location.href = "/user/finance/";
	}
});
</script>
</head>
<body>
<input type="hidden" id="r" value="<%=request.getAttribute("r")%>" />
	<div class="main-content">
		<div class="pay-status-box">
			<div class="succ-img">
				<img src="img/mqpay/ico_fail.png">
			</div>
			<div class="succ-text">抱歉，支付失败</div>
		</div>
	</div>
</body>
</html>