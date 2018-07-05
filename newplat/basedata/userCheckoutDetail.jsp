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
<div class="bread">结算明细  &raquo; 结算详情
</div>
<table class="tblist" cellspacing="0" cellpadding="0" border="0">
<logic:notEmpty name="orderList">
			<tr>
				<th>订单号</th>
				<th>名称</th>
				<th>总价</th>
				<th>时间</th>
			</tr>
		<logic:iterate id="item" name="orderList" indexId="num">
			<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
				<td>
					<bean:write name="item" property="orderNum"/>
				</td>
				<td>
					<bean:write name="item" property="title"/>
				</td>
				<td>
					<bean:write name="item" property="totalPrice"/>
				</td>
				<td>
					<bean:write name="item" property="createDate"/>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>