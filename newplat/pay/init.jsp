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
<script type="text/javascript" src="../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../js/jquery.cookie.js"></script>
<script type="text/javascript" src="../js/move.js"></script>
<link type="text/css" rel="stylesheet" href="../css/mui.min.css"/>
<link type="text/css" rel="stylesheet" href="../css/mqpay.css?t=<%=System.currentTimeMillis()%>" />
<title>支付</title>
<script type="text/javascript">
	function timekeeping(intDiff) {
		var timer = setInterval(function() {
			var day = 0;
			var hour = 0;
			var minute = 0;
			var second = 0;
			
			if(intDiff > 0){
				day = Math.floor(intDiff / (60 * 60 * 24));
				hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
				minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
				second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
			}
			intDiff--;
			
			if (minute <= 9) {
				minute = '0' + minute
			}
			if (second <= 9) {
				second = '0' + second;
			}
			
			if ($.cookie('total') == 0) {
				$.removeCookie('total');
				$("#QRcode .mask").show();
// 				timeout();
				clearInterval(timer);
				if(s == '2'){
					var device = move.getDevice();
					if(device == 'ios' || device == 'android'){
						starup.close();
					}
					return;
				}
				// closeWebPage();
				window.location.href = "/pay/fail";
			}else{
				$.cookie('total', intDiff);
			}
			
			$('#hour_show').html('<s></s>'+hour+'时');
			$('#minute_show').html('<s></s>'+minute+'分');
			$('#second_show').html('<s></s>' + second + '秒');
		}, 1000);
	}
	
	function closeWebPage() {
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("MSIE") > 0) {//close IE
			if (userAgent.indexOf("MSIE 6.0") > 0) {
				window.opener = null;
				window.close();
			} else {
				window.open('', '_top');
				window.top.close();
			}
		} else if (userAgent.indexOf("Firefox") > 0 || userAgent.indexOf("Chrome") > 0) {//close firefox  
			window.location.href = 'about:blank ';
		} else {
			var device = move.getDevice();
			if(device == 'ios' || device == 'android'){
				starup.close();
			}else{
				window.opener = null;
				window.open('', '_self');
				window.close();
			}
		}
	}
	
	function getStatus(){
		var postData = "a=" + $("#a").val();
		var statusTimer = setInterval(function() {
			jQuery.post("/pay/getStatus", postData, function(ret){
				var result = jQuery.parseJSON(ret);
				if(result.code == "200"){
					var data=result.data;
					if(data.status=='2'){
						$.removeCookie('total');
						window.location.href = "/pay/success";
					}
				}else{
					console.log(jdata.code + "=" + jdata.msg);
				}
			});
			if ($.cookie("total") == undefined) {
				clearInterval(statusTimer);
			}
		}, 2000);
	}
	$(function() {
		var code = $("#code").val();
		var msg = $("#msg").val();
		if(code!='200'){
			$('#QRcode .mask').text(msg);
			$('#QRcode .mask').show();
		}
		var s = $("#s").val();//支付场景1.pc2.app
		var g = $("#g").val();//支付类型5.微信1.支付宝
		if(s == '2' && g == '5'){
			$('#paylable').text('微信');
			$('#tipswechat').show();
			$('#tipsalipay').hide();
		}else if(s == '2' && g == '1'){
			$('#paylable').text('支付宝');
			$('#tipsalipay').show();
			$('#tipswechat').hide();
		}
		var intDiff = parseInt($("#t").val());
		timekeeping(intDiff);
		getStatus();
	});
</script>
<script type="text/javascript">
$(document).ready(function(){
	move.hideHeader();
});
</script>
</head>
<body>
<!-- 订单号 -->
<input type="hidden" id="code" value="<%=request.getAttribute("code")%>" />
<input type="hidden" id="msg" value="<%=request.getAttribute("msg")%>" />
<input type="hidden" id="a" value="<%=request.getAttribute("a")%>" />
<input type="hidden" id="t" value="<%=request.getAttribute("t")%>" />
<input type="hidden" id="s" value="<%=request.getAttribute("s")%>" />
<input type="hidden" id="g" value="<%=request.getAttribute("g")%>" />
<div class="main-content">
<!-- 	<header class="mui-bar mui-bar-nav "> -->
<!-- 		<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" href="/game/rechargeList.html"></a> -->
<!-- 		<h1 class="mui-title">支付</h1> -->
<!-- 	</header> -->
		<div class="pay-box">
			<div id="sawtoothbox"></div>
			<div id="payprice">￥<%=request.getAttribute("p")%></div>
			<div id="QRcode">
				<img src="<%=request.getAttribute("QRcodeURL")%>">
				<div class="mask">二维码失效</div>
			</div>
			<div id="paytime">
				<div class="text">距离该订单过期还有</div>
				<div id="timebox">
					<span id="hour_show" class="secc">00时</span>
					<span id="minute_show" class="secc">00分</span>
					<span id="second_show" class="secc">00秒</span>
				</div>
			</div>
			<span class="round" id="leftround"></span>
			<span class="round" id="rightround"></span>
			<div class="scan-box">
				<div class="scan-box-data">
					<div class="scan-ico">
						<img src="../img/mqpay/ico_richscan.png">
					</div>
					<div class="scan-text">
						<p class="text">请使用<span id="paylable"></span>扫一扫</p>
						<p class="text">扫描二维码完成支付</p>
					</div>
				</div>
			</div>
		</div>
		<div class="tips-box" id="tipswechat">
			<div class="title">充值步骤：</div>
			<ul class="tips-cell-box">
				<li class="tips-cell">1.点击截图将自动截图并保存到相册（若图片未保存到相册，请手动截屏保存）</li>
				<li class="tips-cell">2.打开微信，点击扫一扫</li>
				<li class="tips-cell">3.在扫一扫中从相册选取二维码，找到截图，完成充值</li>
			</ul>
		</div>
		<div class="tips-box" id="tipsalipay">
			<div class="title">充值步骤：</div>
			<ul class="tips-cell-box">
				<li class="tips-cell">1.点击截图将自动截图并保存到相册（若图片未保存到相册，请手动截屏保存）</li>
				<li class="tips-cell">2.打开支付宝，点击扫一扫</li>
				<li class="tips-cell">3.在扫一扫中从相册选取二维码，找到截图，完成充值</li>
			</ul>
		</div>
		<div class="copyright-box">
			<p class="text">在付款前请确认收款人账户信息，转账后将立即到达对方账户</p>
<!-- 			<p class="text">版权所有 大面包支付</p> -->
		</div>
	</div>
</body>
</html>