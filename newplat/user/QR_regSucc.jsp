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
.reg-box .text,.reg-box .text-code{height: 38px;line-height: 38px;font-size: 1.2rem;border: 1px solid #ddd;border-radius:3px;width: 96%; padding-left:3%;}
.reg-box .text{}
.reg-box .text-code{}
.btn-send-sms{height:40px;background-color: #D31030;color:#fff;font-size: 1rem;border: 0;border-radius:5px;float: right;padding: 2px 5px;position:absolute;right: 0;top: 1px; }

.backgroundhui{background-color: #999;}
.backgroundred{background-color: #D31030;}

#sendsms{cursor:pointer;width: 90px;}
#btn-reg{cursor:pointer;height:40px;background-color: #D31030;color:#fff;font-size: 1rem;border: 0;border-radius:5px;width: 100%;}

.regTipWrap{width:80%;margin:0 auto;padding:85px 0 0 0;}
.regTipBox{padding:5px;border:1px solid #ccc;border-radius:8px;}
.regTipBox h3{padding:10px;background:#eee;text-align:center;border-radius:8px;font-size:1.1em;color:#666;}
.regTipBox p.tipContent{padding:35px 15px;font-size:1.3em;text-align:center;}
</style>

</head>
<body>
<header class="header-box">
	<div class="page-header">
		<p class="title">注册</p>
	</div>
</header>

<c:choose>
<c:when test="${not empty user}">
	<div class="regTipWrap">
		<div class="regTipBox">
			<h3>提示</h3>
			<p class="tipContent">恭喜你注册成功！</p>
			
			<p class="tipBar">
				<button id="btn-reg" onclick="location='http://longzhicai.boguyuan.com/longzhicai/download/'">下载龙之彩</button>			
			</p>
		</div>
	</div>
</c:when>

<c:otherwise>
	<div class="regTipWrap">
		<div class="regTipBox">
			<h3>提示</h3>
			<p class="tipContent">注册失败，请重新注册！</p>
			
			<p class="tipBar">
				<button id="btn-reg" onclick="javascript:history.back()">返回</button>			
			</p>
		</div>
	</div>
</c:otherwise>
</c:choose>

</body>
</html>