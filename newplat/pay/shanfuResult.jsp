<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.framework.util.ParamUtils"%>
<%@ page import="help.pay.ShanFuPayUtil"%>
<%@ page import="com.apps.util.Util"%>
<%@ page import="com.card.model.CardRechargeOrder"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.card.CardConstants"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.apps.util.JsonUtil"%>
<%@ page import="com.card.service.ICardService"%>
<%@page import="com.framework.service.IServiceLocator"%>
<%@page import="com.framework.service.impl.ServiceLocatorImpl"%>
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
<title>闪付交易结果</title>
<link type="text/css" rel="stylesheet" href="../css/mqpay.css" />
</head>
<body>
	<div class="main-content">
		<%
IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
ICardService cardService = (ICardService) ServiceLocatorImpl.getInstance().getService("cardService");
String memberID = ParamUtils.getParameter(request, "MemberID");// 商户 ID
String terminalID = ParamUtils.getParameter(request, "TerminalID");// 终端
																	// ID
String orderNum = ParamUtils.getParameter(request, "TransID");// 订单 ID
String result = ParamUtils.getParameter(request, "Result");// 1：成功 0：失败
String resultDesc = ParamUtils.getParameter(request, "ResultDesc");// 订单结果
String factMoney = ParamUtils.getParameter(request, "FactMoney");// 成功金额
String additionalInfo = ParamUtils.getParameter(request,
		"AdditionalInfo");// 订单附加信息
String succTime = ParamUtils.getParameter(request, "SuccTime");// 交易成功时间
String sign = ParamUtils.getParameter(request, "Md5Sign");// 签名
String message="fail";
// 先做签名校验
String singPara = "MemberID={MEMBERID}|TerminalID={TERMINALID}|TransID={TRANSID}|Result={RESULT}|ResultDesc={RESULTDESC}|FactMoney={FACTMONEY}|AdditionalInfo={ADDITIONALINFO}|SuccTime={SUCCTIME}|Md5Sign={SIGN}";
singPara = singPara.replace("{MEMBERID}", ShanFuPayUtil.partner)
		.replace("{TERMINALID}", ShanFuPayUtil.terminalId)
		.replace("{TRANSID}", orderNum).replace("{RESULT}", result)
		.replace("{RESULTDESC}", resultDesc)
		.replace("{FACTMONEY}", factMoney)
		.replace("{ADDITIONALINFO}", additionalInfo)
		.replace("{SUCCTIME}", succTime).replace("{sign}", ShanFuPayUtil.secretKey);
	String sign2 = Util.encryption(singPara);
if (sign2.equals(sign)) {// 签名相同
	if("1".equals(request)){//支付成功
		%>
		<div class="pay-status-box">
			<div class="succ-img">
				<img src="img/mqpay/ico_success.png">
			</div>
			<div class="succ-text">支付成功</div>
		</div>
		<%
	}
		%>
		<div class="pay-status-box">
			<div class="succ-img">
				<img src="img/mqpay/ico_fail.png">
			</div>
			<div class="succ-text">抱歉，支付失败</div>
		</div>
		<%
}
%>
	</div>
</body>
</html>