<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
window.onload=function(){
}
</script>
</head>
<body>
<div class="bread">礼品卡管理  &raquo; 礼品卡订单详情
</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			礼品卡订单信息
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单号</th>
      	<td>
      		<bean:write name="cardDTO" property="cardItemOrder.orderNum"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">用户</th>
      	<td>
        	<bean:write name="cardDTO" property="createUser.userName"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">总金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="cardDTO" property="cardItemOrder.totalMoney"/></strong></font>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单状态</th>
      	<td>
     		<logic:equal name="cardDTO" property="cardItemOrder.payStatus" value="1">
     			待付款
			</logic:equal>
     		<logic:equal name="cardDTO" property="cardItemOrder.payStatus" value="2">
     			待发货
			</logic:equal>
			<logic:equal name="cardDTO" property="cardItemOrder.payStatus" value="3">
     			待收货
			</logic:equal>
			<logic:equal name="cardDTO" property="cardItemOrder.payStatus" value="5">
     			已完成
			</logic:equal>
			<logic:equal name="cardDTO" property="cardItemOrder.payStatus" value="6">
     			关闭
			</logic:equal>
      	</td>
    </tr>
   
    <tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			支付信息
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">支付方式</th>
		<td>
			<logic:equal name="cardDTO" property="cardItemOrder.payType" value="1">
     			支付宝
			</logic:equal>
     		<logic:equal name="cardDTO" property="cardItemOrder.payType" value="2">
     			银联
			</logic:equal>
     		<logic:equal name="cardDTO" property="cardItemOrder.payType" value="5">
     			微信
			</logic:equal>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">交易号</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.tradeNo" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">数量</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.num" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">收货人</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.userName" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">联系电话</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.cellPhone" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">收货地址</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.address" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">下单日期</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.createTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">支付日期</th>
		<td>
			<bean:write name="cardDTO" property="cardItemOrder.payTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			套餐信息
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">套餐</th>
		<td>
			<bean:write name="cardDTO" property="cardItem.title" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">面额</th>
		<td>
			<bean:write name="cardDTO" property="cardItem.parValue" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">售价</th>
		<td>
			<bean:write name="cardDTO" property="cardItem.price" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">备注</th>
		<td>
			<bean:write name="cardDTO" property="cardItem.remarks" />
      	</td>
    </tr>
</table>
<table class="tblist" cellspacing="0" cellpadding="0" border="0">
	
<%-- <logic:notEmpty name="list">
			<tr>
				<th>礼品卡</th>
				<th>面额</th>
				<th>售价</th>
				<th>备注</th>
			</tr>
		<logic:iterate id="item" name="list" indexId="num">
			<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
				<td>
					<bean:write name="item" property="title"/>
				</td>
				<td>
					<bean:write name="item" property="parValue"/>
				</td>
				<td>
					<bean:write name="item" property="price"/>
				</td>
				<td>
					<bean:write name="item" property="remarks"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty> --%>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>