<%@page import="org.json.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.framework.util.ParamUtils"%>
<%@ page import="help.pay.ShanFuPayUtil"%>
<%@ page import="com.apps.util.Util"%>
<%@ page import="com.card.model.CardRechargeOrder"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.card.CardConstants"%>
<%@ page import="help.pay.AmxmyPayUtil"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Date"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="com.apps.util.HttpRequest"%>
<%@ page import="com.apps.util.JsonUtil"%>
<%@ page import="com.card.service.ICardService"%>
<%@page import="com.framework.service.IServiceLocator"%>
<%@page import="com.framework.service.impl.ServiceLocatorImpl"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
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
<title>艾米森交易结果</title>
<link type="text/css" rel="stylesheet" href="../css/mqpay.css" />
</head>
<body>
	<div class="main-content">
		<%
String url="http://api.amxmy.top/trade/query";
IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
ICardService cardService = (ICardService) ServiceLocatorImpl.getInstance().getService("cardService");
String orderNo = ParamUtils.getParameter(request, "a");// 商户 ID
Map<String, String> map=new HashMap<String, String>();
map.put("src_code",AmxmyPayUtil.srcCode);
if(ParamUtils.chkString(orderNo)){
	map.put("out_trade_no",orderNo);
}
String sign=AmxmyPayUtil.sign(map);
map.put("sign",sign);
String result = HttpRequest.sendPost(url, map);
JSONObject obj = new JSONObject(result);
String code = obj.getString("respcd");
String msg = obj.getString("respmsg");
JSONArray data=obj.getJSONArray("data");
Log log = LogFactory.getLog(getClass());
log.error("____________amxmypay result::"+result);
if (code.equals("0000")) {// 查询成功
	for (int i = 0; i < data.length(); i++) {
		JSONObject obj2=data.getJSONObject(i);
		String status=obj2.getString("order_status");
		String tradeNo=obj2.getString("trade_no");
		String factMoney=obj2.getString("total_fee");
		String orderNo=obj2.getString("out_trade_no");
	if("3".equals(status)){//支付成功
		CardRechargeOrder order=cardService.getRechargeOrderByOrderNum(orderNo);
		BigDecimal payPrice=order.getPayPrice();
		String s=ParamUtils.BigFormatIntString(payPrice);
		BigDecimal factPrice=new BigDecimal(Integer.valueOf(factMoney));
		if(new BigDecimal(s).compareTo(factPrice)==0){
			order.setPayStatus(CardConstants.ORDER_PAY_SUCCESS);
			order.setPayTime(new Date());
			order.setTradeNo(tradeNo);
			cardService.updateUserRecharge(order);
		}
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