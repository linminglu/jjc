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

function refuse(){
	var frm = document.cardForm;
	var id=frm["itemId"].value.trim();
	var reason=frm["reason"].value.trim();
	if(reason.length==0){
		alert("请填写原因");
		frm["reason"].focus();
		return false;
	}	
	if(confirm("您确定要拒绝吗？一旦确认就不能改变！")){
		window.location.href='cardAction.do?method=refuse&id='+id+'&reason='+reason;
	}
}

</script>
</head>
<body>
<html:form action="/cardAction.do?method=modifyBalance" onsubmit="return formSubmit();" enctype="multipart/form-data">
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
      	<th align="right">订单处理状态</th>
      	<td>
     		<logic:equal name="order" property="payStatus" value="2">
     			未处理
			</logic:equal>
     		<logic:equal name="order" property="payStatus" value="3">
     			已拒绝
			</logic:equal>
			<logic:equal name="order" property="payStatus" value="4">
     			已充值
			</logic:equal>
      	</td>
    </tr>
   <c:if test="${order.payStatus==3}">
   		<tr class="tr-color-body">
      	<th align="right">拒绝原因</th>
      	<td>
			<bean:write name="order"  property="reason" />
      	</td>
    </tr>
   </c:if>
    <tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			收款人信息
      	</td>
    </tr>
  <%--   <tr class="tr-color-body">
		<th align="right">收款银行</th>
		<td>
			<bean:write name="bank" property="bankName" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">收款账号</th>
		<td>
			<bean:write name="bank" property="bankAccount" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">收款人姓名</th>
		<td>
			<bean:write name="bank" property="userName" />
      	</td>
    </tr> --%>

    
	<tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			打款信息
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">打款方式</th>
		<td>
			<c:if test="${order.depositType==0}">
				网银转账
			</c:if>	
			<c:if test="${order.depositType==1}">
				ATM自动柜员机
			</c:if>	
			<c:if test="${order.depositType==2}">
				ATM现金入款
			</c:if>	
			<c:if test="${order.depositType==3}">
				银行柜台转账
			</c:if>	
			<c:if test="${order.depositType==4}">
				银行柜台转账
			</c:if>	
			<c:if test="${order.depositType==5}">
				其他
			</c:if>	
      	</td>
    </tr>
<c:if test="${order.payStatus==2}">
    <tr class="tr-color-body">
      	<th align="right">输入金额</th>
      	<td>
      		<html:text name="cardForm" property="userpoints"  onkeyup="value=value.match( /^[1-9]\d*|0$/)" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">拒绝原因</th>
      	<td>
      		<html:textarea property="reason" name="cardForm" cols="60" rows="5"/>
      		<p class="red">拒绝的时候填写</p>
      	</td>
    </tr>
</c:if>	
</table>

<div class="div-bottom-bar">
	<c:if test="${order.payStatus==2}">
		<input type="submit" class="gbutton" value="确定"/>
		<input type="button" class="gbutton" value="拒绝" onclick="refuse();"/>
	</c:if>	
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
</html:html>