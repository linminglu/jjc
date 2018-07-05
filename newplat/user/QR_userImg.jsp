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

.QRbox{padding:45px 0 0 0;}
.QRbox .h1{text-align:center;font-weight:normal;}
.QRbox .h1 img{width:80px;height:80px;border-radius:50px;vertical-align:middle;}
.QRbox .h1 p{display:inline-block;height:80px;line-height:80px;font-size:1.1em;padding:0 0 0 15px;}
.QRbox .h1 p span{color:#666;padding:0 5px;}
.QRbox .qrimg{text-align:center;padding:85px 0 0 0;}
.QRbox .qrimg img{width:145px;height:145px;}
.QRbox .qrimg p{text-align:center;color:#666;padding:10px;}
</style>

</head>
<body>
<div class="QRbox"><%--
	<div class="h1">
		<p>
			<img src="<%=Constants.getDomainName()%><c:out value='${user.logo}'/>" onerror="this.src='<%=Constants.getDomainName()%>/images/head_icon.png';">
			<span><c:out value='${user.userName}'/></span>
		</p>
	</div>
	--%><div class="qrimg">
		<img src="<%=Constants.getDomainName()%><%=Constants.getFileServerFolder()+"/QRCode/"%><c:out value='${user.loginName}'/>.png">
		<p>扫一扫上面的二维码</p>	
	</div>
</div>
</body>
</html>