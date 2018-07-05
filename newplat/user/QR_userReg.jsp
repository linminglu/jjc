<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<!DOCTYPE html>
<html lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="keywords" content="龙之彩">
<meta name="description" content="龙之彩">
<title>龙之彩 </title>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/md5.js"></script>
<style type="text/css">
html{font-size: 62.5%;}
body{background-color: #fff;}   
html,body{pading:0;margin:0;
font-family: 'Century Gothic', "Microsoft YaHei", Trebuchet MS, Arial,
		Verdana, Tahoma, "DejaVu Sans", "Liberation sans",
		"Bitstream Vera Sans", sans-serif;font-size: 14px;  }
h1,h2,h3,h4,h5,ul,p,form{padding:0px;margin:0px;}
.fl{float: left;}
.fr{float: right;}
.header-box{position: relative;}
.page-header{width: 100%;height: 45px;line-height: 45px;background-color: #D01130;text-align: center;position: fixed;top: 0;z-index: 9999;}
.page-header .title{  font-size: 1.7rem;color: #FAFAFA;}

.reg-box{width: 90%;margin: 70px auto;overflow: hidden;}
.reg-box .lable{color: #666;}
.text-line{margin: 10px 0;position:relative;}
.reg-box .text,.reg-box .text-code{height: 38px;line-height: 38px;font-size: 1.2rem;border: 1px solid #ddd;border-radius:3px;width: 93%; padding-left:3%;}
.reg-box .text{}
.reg-box .text-code{}
.btn-send-sms{height:40px;background-color: #D31030;color:#fff;font-size: 1rem;border: 0;border-radius:5px;float: right;padding: 2px 5px;position:absolute;right: 0;top: 1px; }

.backgroundhui{background-color: #999;}
.backgroundred{background-color: #D31030;}

#sendsms{cursor:pointer;width: 90px;}
#btn-reg{cursor:pointer;height:40px;background-color: #D31030;color:#fff;font-size: 1rem;border: 0;border-radius:5px;width: 100%;}
</style>
<script type="text/javascript">
function formSubmit(){
	var userName =  document.mobileForm.elements["loginName"];
	var password = document.mobileForm.elements["password2"];
	var incode = document.mobileForm.elements["inpcode"];
	if(userName.value.length<=0){alert("请输入手机号！");userName.focus();return false;}
	if(userName.value.length!=11){alert("请输入正确的手机号！");userName.focus();return false;}
	if(incode.value.length<=0){alert("请填写验证码");incode.focus();return false;}
	if(password.value.length<=0){alert("请输入密码！");password.focus();return false;
	}else {
		document.getElementById('password').value=hex_md5(document.getElementById('password2').value).toUpperCase();
	}
	$("#btn-reg").html("注册中...");
	$("#btn-reg").attr('disabled',true);
	return true;
}

function sendSms(){
	var mobile=jQuery("#mobile").val().trim();
	if(mobile.length==11){
		jQuery("#sendsms").attr('disabled',true);
		jQuery("#sendsms").removeClass("backgroundred").addClass('backgroundhui');
		$.post("../api/sms_send?type=1&mobile="+ mobile, 
			function(data) {
				if (data.code == "200") {
					runTimer(120);
				} else {
					jQuery("#sendsms").attr('disabled',false);
					jQuery("#sendsms").removeClass("backgroundhui").addClass('backgroundred');
					alert(data.msg);
				}
		}, "json");
	}else{
		alert('请输入正确的手机号！');
	}
}

//消息提示
var tipBox = {
	alert:function(msg){
		
	},
	init:function(){
		if(!tipBox.obj){
			
		}
	}
};

var timeObj = {};
timeObj.ing = 0;
function runTimer(sec){
	window.clearInterval(timeObj.timer);
	timeObj.ing = sec;
	timeObj.timer = window.setInterval(function(){
		timeObj.ing--;
		if(timeObj.ing>0){
			jQuery("#sendsms").html(timeObj.ing+"s");
			jQuery("#sendsms").attr('disabled',true);
			jQuery("#sendsms").removeClass("backgroundred").addClass('backgroundhui');
		}else{
			window.clearInterval(timeObj.timer);
			jQuery("#sendsms").html("获取验证码");
			jQuery("#sendsms").attr('disabled',false);
			jQuery("#sendsms").removeClass("backgroundhui").addClass('backgroundred');
		}
	},1000);
}
</script>
</head>
<body>
<header class="header-box">
	<div class="page-header">
		<p class="title">注册</p>
	</div>
</header>
<div class="content">
	<form name="mobileForm"  method="post"  action="/m/reg" onsubmit="return formSubmit();" >
	<input type="hidden" name="inviteCode" value="<c:out value="${inviteCode}"/>"/>
	<input type="hidden" id="errCode" value="<c:out value="${errCode}"/>"/>
	<input type="hidden" id="errMsg" value="<c:out value="${errMsg}"/>"/>
	<div class="reg-box">
		<p class="lable">请输入手机号验证并注册</p>
		<p class="text-line">
			<input type="text" id="mobile" name="loginName" placeholder="手机号" class="text" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')" value="<c:out value="${loginName}"/>"/>
		</p>
		<p class="text-line">
			<input  type="text" name="inpcode" placeholder="验证码" class="text-code" maxlength="6" onkeyup="value=value.replace(/[^\d]/g,'')" value="<c:out value="${inpcode}"/>"/>
			<button type="button" value="获取验证码" class="btn-send-sms" id="sendsms" onclick="sendSms();">获取验证码</button>
		</p>
		<p class="text-line">
			<input type="text" name="password2" id="password2" placeholder="请输入密码" class="text" maxlength="16" />
			<input type="hidden" name="password" id="password"  class="text" maxlength="16" />
		</p>
		<p class="text-line">
			<button id="btn-reg" type="submit">注册</button>			
		</p>
	</div>
	</form>
</div>
<script type="text/javascript">
	var errCode = $("#errCode");
	var errMsg = $("#errMsg");
	if(errCode && errCode.val().length>1){
		alert(errMsg.val());
	}
</script>
</body>
</html>