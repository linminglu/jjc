<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script type="text/javascript">
function deleteLimit(id){
	if(confirm("确认删除？")){
		window.location="managerAction.do?method=userLimitDelete&id="+id;
	}
}
</script>
<style type="text/css">
.layui-layer-dialog .layui-layer-content{padding: 10px;}
</style>
</head>
<body>
<div class="bread">系统设置&raquo;用户限制设置 
</div>
<html:form action="/managerAction.do?method=userLimitList" method="post">
<%-- <input type="hidden" value="<c:out value='${userType}'/>" name="userType"/> --%>
<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4' || loginUser.userType eq '20'}">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户名：
			<html:text style="width: 130px;" property="loginName" name="managerForm"/>
			
			用户ID：
			<html:text style="width: 130px;" property="userId" name="managerForm"/>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=userLimitPreAdd'"/>
		</td>
	</tr>
</table>
</c:if>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String loginName =request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
String userId =request.getAttribute("userId")!=null?(String)request.getAttribute("userId"):"";
%>
<pg:pager 
	url="managerAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="userLimitList"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<pg:param name="userId" value="<%=userId%>"/>
	
<bean:size id="length" name="managerList"/>
<logic:notEmpty name="managerList">
<form class="layui-form" action="#" lay-filter="reform">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th >用户ID</th>
		<th >用户名</th>
		<th>提款所需打码量(%)</th>
	    <th>提款提示语</th>
	    <th>提款是否需要短信验证</th>
	    <th>时间</th>
	    <th>状态</th>
	    <th >操作</th>
	</tr>
	<logic:iterate id="item" name="managerList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="uid" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="loginName" />
	    </td>
	   	<td align="center" class="tdprice tdright">
		     	<bean:write name="item" property="betMoney"/>
		</td>
		<td>
			<bean:write name="item" property="cashTip"/>
		</td>
		<td align="center">
	    	<c:if test="${item.cashMessage == '1'}">
	    		是
			</c:if>
			<c:if test="${item.cashMessage == '0' || item.cashMessage == null}">
				否
			</c:if>
	    </td>
	    <td>
			<bean:write name="item" property="createTime" format="yyyy-MM-dd HH:mm:SS"/>
		</td>
<!-- 	    <td align="center"> -->
<%-- 	    	<c:if test="${item.betMoneyStatus == '1'}"> --%>
<!-- 	    		是 -->
<%-- 			</c:if> --%>
<%-- 			<c:if test="${item.betMoneyStatus == '0' || item.betMoneyStatus == null}"> --%>
<!-- 				否 -->
<%-- 			</c:if> --%>
<!-- 	    </td> -->
	    <td align="center">
	    	<c:if test="${item.betMoneyStatus == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="item" property="id"/>" id="status<bean:write name="item" property="id"/>"/>
			</c:if>
			<c:if test="${item.betMoneyStatus == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="item" property="id"/>" id="status<bean:write name="item" property="id"/>"/>
			</c:if>
	    </td>

	   	<td align="center">
	   		<a class="link" href="managerAction.do?method=userLimitPreAdd&id=<bean:write name="item" property="id"/>">修改</a>
	   		<a class="link" href="javascript:void(0)" onclick="deleteLimit(<bean:write name="item" property="id"/>)">  删除</a>
	   	</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</form>
	</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	
	form.on('switch(statusSwitch)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("/user/managerAction.do?method=changeUserLimitStatus", 
			{'limitId':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.close(loadObj);
					$('#status'+id).val(val);
				}else{
					layer.close(loadObj);
					//失败的话
					if(val=='1'){
						$('#status'+id).val('0');
						$('#status'+id).removeAttr('checked');
					}else{
						$('#status'+id).val('1');
						$('#status'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</body>
</html>