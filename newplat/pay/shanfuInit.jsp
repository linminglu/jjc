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
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/move.js"></script>
<link type="text/css" rel="stylesheet" href="css/mqpay.css?t=<%=System.currentTimeMillis()%>" />
<title>闪付支付</title>
</head>
<body onload="document.form1.submit()">
<!-- <body> -->
<form id="form1" name="form1" method="post" action="http://gw.sfvipgate.com/v4.aspx">
<!-- 订单号 -->
<input type="hidden" name="MemberID" value="<%=request.getAttribute("MemberID")%>" />
<input type="hidden" name="TerminalID" value="<%=request.getAttribute("TerminalID")%>" />
<input type="hidden" name="InterfaceVersion" value="<%=request.getAttribute("InterfaceVersion")%>" />
<input type="hidden" name="KeyType" value="<%=request.getAttribute("KeyType")%>" />
<input type="hidden" name="PayID" value="<%=request.getAttribute("PayID")%>" />
<input type="hidden" name="TradeDate" value="<%=request.getAttribute("TradeDate")%>" />
<input type="hidden" name="TransID" value="<%=request.getAttribute("TransID")%>"/>
<input type="hidden" name="OrderMoney" value="<%=request.getAttribute("OrderMoney")%>" />
<input type="hidden" name="NoticeType" value="<%=request.getAttribute("NoticeType")%>" />
<input type="hidden" name="PageUrl" value="<%=request.getAttribute("PageUrl")%>" />
<input type="hidden" name="ReturnUrl" value="<%=request.getAttribute("ReturnUrl")%>" />
<input type="hidden" name="Md5Sign" value="<%=request.getAttribute("Md5Sign")%>" />
</form>
</body>
</html>