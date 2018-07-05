<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>龙之彩</title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
window.onload=function(){
}

function formSubmit(){
	var frm = document.cardForm;
	var point=frm["userpoints"].value.trim();
	if(frm["userpoints"].value.trim().length==0){
		alert("请输入金额");
		frm["userpoints"].focus();
		return false;
	}	
	if(confirm("确认充值?"+"金额："+point)){
		return true;
	}
	return false;
}

</script>
</head>
<body>
<html:form action="/cardAction.do?method=modifyPayStatus" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="cardForm" property="user.userId"/>
<html:hidden name="cardForm" property="itemId"/>
<html:hidden name="cardForm" property="cardRechargeOrder.orderNum"/>
<html:hidden name="cardForm" property="cardRechargeOrder.totalMoney"/>

<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			订单信息
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单号</th>
      	<td>
      		<bean:write name="order" property="orderNum"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">用户</th>
      	<td>
        	<bean:write name="user" property="loginName"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">订单金额</th>
      	<td>
        	<font color="red"><strong><bean:write name="order" property="totalMoney"/></strong></font>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">下单日期</th>
		<td>
			<bean:write name="order" property="createTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">支付方式</th>
	 	<td>	 	
	 		<c:if test="${order.payType==1}">支付宝</c:if>
	 		<c:if test="${order.payType==2}">银联</c:if>
	 		<c:if test="${order.payType==3}">线下支付</c:if>
	 		<c:if test="${order.payType==4}">积分</c:if>
	 		<c:if test="${order.payType==5}">微信</c:if>
	 	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">订单处理状态</th>
      	<td>
      	    <logic:equal name="order" property="payStatus" value="1">
     			未付款
			</logic:equal>
     		<logic:equal name="order" property="payStatus" value="2">
     			已付款
			</logic:equal>
      	</td>
    </tr>
</table>

<div class="div-bottom-bar">
	<c:if test="${order.payStatus==1}">
		<input type="submit" class="gbutton" value="改为已付款"/>
	</c:if>	
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
</html:html>