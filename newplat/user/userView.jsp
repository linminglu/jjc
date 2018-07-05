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

function send(id){
	window.location.href='storeOrderAction.do?method=sendStore&id='+id;
}
function send(id,con){
	if(confirm("您确定要"+con+"吗？")){
		window.location.href='storeOrderAction.do?method=receive&id='+id;
	}
}

</script>
</head>
<body>
<div class="bread">用户管理  &raquo; 用户详情
</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
 	<tr class="tr-color-body">
		<td colspan="2" class="">
			用户信息
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">ID</th>
		<td>
			<bean:write name="user" property="userId" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">用户名</th>
		<td>
			<bean:write name="user" property="userName" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">用户类型</th>
		<td>
			<c:out value="${userTypeZh}"/>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">注册日期</th>
		<td>
			<bean:write name="user" property="registDateTime" format="yyyy-MM-dd "/>
      	</td>
    </tr>
    <c:if test = "${loginUser.userType==12&&loginUser.agentId==0}">
    <tr class="tr-color-body">
		<th align="right">电话</th>
		<td>
			<bean:write name="user" property="cellPhone"/>
      	</td>
    </tr>
    </c:if>
    <c:if test = "${loginUser.userType==2}">
    <tr class="tr-color-body">
		<th align="right">电话</th>
		<td>
			<bean:write name="user" property="cellPhone"/>
      	</td>
    </tr>
    </c:if>
    <c:if test="${userType=='1' }">
	    <%-- <tr class="tr-color-body">
			<th align="right">余额</th>
			<td>
				<bean:write name="user" property="money" />
	      	</td>
	    </tr> --%>
	     <tr class="tr-color-body">
			<th align="right">余额</th>
			<td>
				<bean:write name="user" property="money" />
	      	</td>
	    </tr>
    </c:if>
   
</table>
<%--<table class="tblist" cellspacing="0" cellpadding="0" border="0">--%>
<%--<logic:notEmpty name="bankList">--%>
<%--		<tr class="tr-color-body">--%>
<%--			<td colspan="3" >--%>
<%--				账户信息--%>
<%--	      	</td>--%>
<%--    	</tr>--%>
<%--		<tr>--%>
<%--			<th>账户</th>--%>
<%--			<th>银行</th>--%>
<%--			<th>开户人姓名</th>--%>
<%--		</tr>--%>
<%--		<logic:iterate id="item" name="bankList" indexId="num">--%>
<%--			<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">--%>
<%--				<td>--%>
<%--					<bean:write name="item" property="bindAccount"/>--%>
<%--				</td>--%>
<%--				<td>--%>
<%--					<bean:write name="item" property="bankName"/>--%>
<%--				</td>--%>
<%--				<td>--%>
<%--					<bean:write name="item" property="bindName"/>--%>
<%--				</td>--%>
<%--			</tr>--%>
<%--		</logic:iterate>--%>
<%--	</logic:notEmpty>--%>
<%--</table>--%>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>