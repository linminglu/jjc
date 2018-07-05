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
function userShow(uid){
	$.post("/user/managerAction.do?method=userinfo", {'userId':uid},
		function(result){
			var code=result.code;
			var data=result.data;
			var msg=result.msg;
			if(code==200){
				var obj=data.obj;
				var uid=obj.uid;
				var loginName=obj.loginName;
				var cellPhone=obj.cellPhone;
				var money=obj.money;
				var registDate=obj.registDate;
				var lastLoginDate=obj.lastLoginDate;
				var lastLoginIp=obj.lastLoginIp;
				var agentName=obj.agentName;
				var userType=obj.userType;
				var userTypeLable='';
				if(userType=='1'){
					userTypeLable='普通用户';
				}else if(userType=='2'||userType=='4'){
					userTypeLable='管理员';
				}else if(userType=='11'){
					userTypeLable='试玩用户';
				}else if(userType=='12'){
					userTypeLable='代理用户';
				}else if(userType=='13'){
					userTypeLable='初级代理';
				}else if(userType=='14'){
					userTypeLable='中级代理';
				}else if(userType=='15'){
					userTypeLable='高级代理';
				}else if(userType=='20'){
					userTypeLable='客服人员';
				}else if(userType=='21'){
					userTypeLable='代理查看';
				}
				var accountNo=obj.accountNo;
				var bankName=obj.bankName;
				var bindName=obj.bindName;
				var html='<table class="layui-table" lay-skin=""><colgroup><col width="100"><col width="200"></colgroup><tbody>';
				html=html+'<tr><td class="font-weight-bold">ID</td><td>'+uid+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">登录名</td><td>'+loginName+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">手机号</td><td>'+cellPhone+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">余额</td><td><span class="red">'+money+'</span></td></tr>';
				html=html+'<tr><td class="font-weight-bold">用户类型</td><td>'+userTypeLable+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">注册时间</td><td>'+registDate+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">登录时间</td><td>'+lastLoginDate+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">登录IP</td><td>'+lastLoginIp+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">上级代理</td><td>'+agentName+'</td></tr>';
				
				html=html+'<tr><td class="font-weight-bold">绑定银行</td><td>'+bankName+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">开户名</td><td>'+bindName+'</td></tr>';
				html=html+'<tr><td class="font-weight-bold">卡号</td><td>'+accountNo+'</td></tr>';
				
				
				html=html+'</tbody></table>';
				var index = layer.open({
					content: html,
					skin: 'layui-layer-lan',
					title: '用户信息',
	     	        btn: ['关闭'] ,
	     	       area: ['350px','400px']
				});
			}else{
				layer.open({title: '提示',content: msg});
			}
 		},"json");
}

</script>
<style type="text/css">
.layui-layer-dialog .layui-layer-content{padding: 10px;}
</style>
</head>
<body>
<div class="bread">快捷管理&raquo;用户管理 
</div>
<html:form action="/managerAction.do?method=userList" method="post">
<%-- <input type="hidden" value="<c:out value='${userType}'/>" name="userType"/> --%>
<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4' || loginUser.userType eq '20'}">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户名/ID
			<html:text style="width: 130px;" property="loginName" name="managerForm"/>
			<span>最后登录IP：<html:text style="width: 130px;" property="ipAddress" name="managerForm"/></span>
			<span>状态：
				<html:select property="status" name="managerForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">有效</html:option>
					<html:option value="0">无效</html:option>
				</html:select>
			</span>
<!-- 			用户类型 -->
<%-- 			<html:select property="userType" name="managerForm"> --%>
<%-- 				<html:option value="">全部</html:option> --%>
<%-- 				<html:option value="1">普通用户</html:option> --%>
<%-- 				<html:option value="2">数据管理员</html:option> --%>
<%-- 				<html:option value="13">初级代理</html:option> --%>
<%-- 				<html:option value="14">中级代理</html:option> --%>
<%-- 				<html:option value="15">高级代理</html:option> --%>
<%-- 				<html:option value="20">客服人员</html:option> --%>
<%-- 				<html:option value="21">财务人员</html:option> --%>
<%-- 			</html:select> --%>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=preAdd&userType=<c:out value="${userType}"/>';"/>
		</td>
	</tr>
</table>
</c:if>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String loginName =request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
String ipAddress =request.getAttribute("ipAddress")!=null?(String)request.getAttribute("ipAddress"):"";
String accountNo =request.getAttribute("accountNo")!=null?(String)request.getAttribute("accountNo"):"";
String status =request.getAttribute("status")!=null?(String)request.getAttribute("status"):"";
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
	<pg:param name="method" value="userList"/>
	<pg:param name="userType" value="<%=userType%>"/>
	<pg:param name="ipAddress" value="<%=ipAddress%>"/>
	<pg:param name="accountNo" value="<%=accountNo%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="accountNo" value="<%=accountNo%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="managerList"/>
<logic:notEmpty name="managerList">
<form class="layui-form" action="#" lay-filter="reform">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th width="40">ID</th>
		<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
			<th>用户名</th>
		</c:if>
		<th width="75">类型</th>
	    <th>余额</th>
	    <th width="50">状态</th>	   	    
	    <th>IP</th>
	 	<!-- <th>登陆次数</th>  -->  
<%-- 	    <c:if test = "${userType==1}"> --%>
<!-- 	    	<th>上级代理</th> -->
<!-- 	    	<th>银行名称</th>	 -->
<!-- 	    	<th>银行卡</th> -->
<!-- 	    	<th>持卡人姓名</th> -->
<%-- 	    </c:if> --%>
	    <th width="120">登录时间</th>
	    <th width="80">注册日期</th>
	    <th width="280">操作</th>
	</tr>
	<logic:iterate id="item" name="managerList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="userId" />
	    </td>
	    <c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
	    <td align="center">
<%-- 	    	 <a href="managerAction.do?method=userView&userId=<c:out value='${item.userId}'/>&userType=<c:out value="${userType}"/>" /><c:out value="${item.loginName}"/></a> --%>
 			<a class="link" href="javascript:userShow(<c:out value='${item.userId}'/>);"><c:out value="${item.loginName}"/></a>
	    </td>
	    </c:if>
	    <td align="center">
	       	<logic:equal name="item" property="userType" value="1">
					<font color="black"><strong>普通用户</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="2">
					<font color="black"><strong>数据管理员</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="13">
					<font color="green"><strong>初级代理</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="14">
					<font color="green"><strong>中级代理</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="15">
					<font color="green"><strong>高级代理</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="userType" value="20">
					<font color="black"><strong>客服人员</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="userType" value="21">
					<font color="black"><strong>代理查看</strong></font>&nbsp;
			</logic:equal>
	    </td>
	   	<td align="center" class="tdprice tdright">
		     	<bean:write name="item" property="money"/>
		</td>
		
		<td align="center">
	    	<c:if test="${item.status == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="item" property="userId"/>" id="status<bean:write name="item" property="userId"/>"/>
			</c:if>
			<c:if test="${item.status == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="item" property="userId"/>" id="status<bean:write name="item" property="userId"/>"/>
			</c:if>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="lastLoginIp"/>
	    </td>
	  <!-- 
	    <td align="center">
	        <bean:write name="item" property="browseCount"/>
	    </td>-->
<%-- 		<c:if test = "${userType==1}"> --%>
<%-- 			<c:if test = "${item.agentName ne null}"> --%>
<!-- 				<td align="center"> -->
<%-- 			        <bean:write name="item" property="agentName"/> --%>
<!-- 			    </td> -->
<%-- 			</c:if> --%>
<%-- 			<c:if test = "${item.agentName eq null}"> --%>
<!-- 					<td align="center"> -->
<!-- 				        无 -->
<!-- 				    </td> -->
<%-- 			</c:if> --%>
<!-- 			<td align="center"> -->
<%-- 			       <bean:write name="item" property="bankName"/> --%>
<!-- 			</td> -->
<!-- 			<td align="center"> -->
<%-- 			    <bean:write name="item" property="accountNo"/> --%>
<!-- 			</td> -->
<!-- 			<td align="center"> -->
<%-- 			    <bean:write name="item" property="bindName"/> --%>
<!-- 			</td> -->
<%-- 	    </c:if> --%>
		<td align="center">
	      	<bean:write name="item" property="lastLoginDate" format="yyyy-MM-dd HH:mm"/>
		</td>
		<td align="center">
	      	<bean:write name="item" property="registDateTime" format="yyyy-MM-dd"/>
		</td>

	   	<td align="center">
	   		<a class="link" href='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>'>充值</a>
			|<a class="link" href='../agent/betDetailList.jsp?userId=<bean:write name="item" property="userId"/>'>投注明细</a>
	   		<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
	   			|<a class="link" href='managerAction.do?method=preModifyUser&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'>编辑</a>
	   		</c:if>
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
		$.post("/user/managerAction.do?method=changeStatus", 
			{'userId':id},
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