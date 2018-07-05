<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">

</script>
</head>
<body>
<div class="bread">订单管理  &raquo; 订单详情
</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
 <tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			支付信息
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">支付方式</th>
		<td>
			<logic:equal name="gaOrder" property="payType" value="1">
     			支付宝
			</logic:equal>
     		<logic:equal name="gaOrder" property="payType" value="2">
     			银联
			</logic:equal>
			<logic:equal name="gaOrder" property="payType" value="5">
     			微信
			</logic:equal>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">交易号</th>
		<td>
			<bean:write name="gaOrder" property="tradeNo" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">支付日期</th>
		<td>
			<bean:write name="gaOrder" property="payTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			订单信息
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单号</th>
      	<td>
      		<bean:write name="gaOrder" property="orderNum"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">用户</th>
      	<td>
        	<c:out value="${userName }"></c:out>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">总金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="gaOrder" property="money"/></strong></font>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单状态</th>
      	<td>
     		<logic:equal name="gaOrder" property="payStatus" value="0">
     			<ord-notpay>未付款</ord-notpay>
			</logic:equal>
     		<logic:equal name="gaOrder" property="payStatus" value="1">
     			<ord-pay>已付款</ord-pay>
			</logic:equal>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			快三投注信息
      	</td>
    </tr>
</table>
<table class="tblist" cellspacing="0" cellpadding="0" border="0">
<logic:notEmpty name="list">
			<tr>
				<th>期号</th>
				<th>投注内容</th>
				<th>投注金额</th>
			</tr>
		<logic:iterate id="item" name="list" indexId="num">
			<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
				<td>
					<bean:write name="item" property="sessionNo"/>
				</td>
				<td>
					<bean:write name="item" property="betValue"/>
				</td>
				<td>
					<bean:write name="item" property="totalPoint"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
   <!--  <a href='storeOrderAction.do?method=init' class="gbutton" >返回</a> -->
</div>
</body>
</html:html>