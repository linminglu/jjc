<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function del(){
	var frm = document.managerForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "请选择删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}

function delUser(url,loginName){
	if(loginName && (loginName=="admin" || loginName=="cuisy" || loginName=="toefladmin" || loginName=="genadmin" 
		 	|| loginName=="testadmin" || loginName=="examadmin"
			|| loginName=="satadmin" || loginName=="ieltsadmin" || loginName=="greadmin" || loginName=="gmatadmin")){
		alert("不能删除此管理账号！");
		return false;
	}
	if(confirm("确认删除用户?")){
		location.href = url;
	}
	
}
function selectAll(flag){
	var selAll = document.managerForm;
	for( var i=0;i<selAll.elements.length;i++ ){
		if( selAll.elements[i].name == 'selectIndex' ){
			selAll.elements[i].checked = flag;
		}
	}
}
function delItem(id,type){
	if(confirm('确实要删除吗？')){
		$.post("managerAction.do?method=delUser&uid="+id+"&userType="+type,
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
				}else{
					alert("删除失败！\n\n提示：数据已应用则不能删除！");
				}
		});	
	}
}
</script>
</head>
<body>
<div class="bread">用户管理  &raquo; 
<c:if test = "${userType==1}">注册用户 </c:if>
<c:if test = "${userType==2}">管理员 </c:if>
<c:if test = "${userType==3}">商家用户 </c:if>
<c:if test = "${userType==12}">代理用户 </c:if>
<c:if test = "${userType==11}">测试用户 </c:if>
</div>
<html:form action="/managerAction.do?method=init" method="post">
<input type="hidden" value="<c:out value='${userType}'/>" name="userType"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户名/ID
			<html:text property="loginName" name="managerForm"/>

			<input type="submit" class="gbutton" value="查询" />
<%--			<c:if test = "${userType == 2}">--%>
<%--				<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=preAdd&userType=<c:out value="${userType}"/>';"/>--%>
<%--			</c:if>--%>
			<c:if test = "${loginUser.userType==2&&category!=2}">
				<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=preAdd&userType=<c:out value="${userType}"/>';"/>
			</c:if>
		   <c:if test = "${loginUser.userType==12&&(loginUser.agentId==0||category==1)}">
			 <input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=preAdd&userType=<c:out value="${userType}"/>';"/>
			</c:if>
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String category =request.getAttribute("category")!=null?(String)request.getAttribute("category"):"";
String agentId =request.getAttribute("agentId")!=null?(String)request.getAttribute("agentId"):"0";
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
	<pg:param name="method" value="init"/>
	<pg:param name="userType" value="<%=userType%>"/>
	<pg:param name="category" value="<%=category%>"/>
	<pg:param name="agentId" value="<%=agentId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<html:form action="/managerAction.do?method=delete" onsubmit="return del();">
<bean:size id="length" name="managerList"/>
<logic:notEmpty name="managerList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>ID</th>
		<th>用户名</th>
		    <c:if test = "${category !=2}">
	    <c:if test = "${userType==1}">
	    <th>余额</th>
	    <th>状态</th>	   	    
	    </c:if>
	    <th>IP</th>
	 	<!-- <th>登陆次数</th>  -->  
	    <c:if test = "${userType==1}">
	    	<th>上级代理</th>
	    	<th>银行名称</th>	
	    	<th>银行卡</th>
	    	<th>持卡人姓名</th>
	    </c:if>
	    </c:if>
	    <th>登录时间</th>
	    <th>注册日期</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="managerList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="userId" />
	    </td>
	    <td align="center">
	    	 <a href="managerAction.do?method=userView&userId=<c:out value='${item.userId}'/>&userType=<c:out value="${userType}"/>" /><c:out value="${item.loginName}"/></a>
	    </td>
	       <c:if test = "${category !=2}">
	    <c:if test = "${userType==1}">
		    <td align="center">
			     	<bean:write name="item" property="money"/>
			</td>
		    <td align="center">
			    <logic:equal name="item" property="status" value="1">
					<font color="green"><strong>有效</strong></font>&nbsp;<a class="button" href='managerAction.do?method=pauseUser&&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${userType}"/>'>关</a>&nbsp;
				</logic:equal>
				<logic:equal name="item" property="status" value="2">
					<font style="color: #666;"><strong>未激活</strong></font> 
				</logic:equal>
				<logic:equal name="item" property="status" value="0">
					<font color="red"><strong>无效</strong></font>&nbsp;<a class="button" href="managerAction.do?method=releaseUser&&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${userType}"/>">开</a>&nbsp;
				</logic:equal>
				<logic:equal name="item" property="status" value="1">
				<!-- <font color="gray">未激活</font> -->
				</logic:equal>
		    </td>
	    </c:if>
	    <td align="center">
	        <bean:write name="item" property="lastLoginIp"/>
	    </td>
	  <!-- 
	    <td align="center">
	        <bean:write name="item" property="browseCount"/>
	    </td>-->
		<c:if test = "${userType==1}">
			<c:if test = "${item.agentName ne null}">
				<td align="center">
			        <bean:write name="item" property="agentName"/>
			    </td>
			</c:if>
			<c:if test = "${item.agentName eq null}">
					<td align="center">
				        无
				    </td>
			</c:if>
		 <td align="center">
		        <bean:write name="item" property="bankName"/>
		    </td>
		    <td align="center">
		        <bean:write name="item" property="accountNo"/>
		    </td>
		    <td align="center">
		        <bean:write name="item" property="bindName"/>
		    </td>
	    </c:if>
	    </c:if>
		<td align="center">
	      	<bean:write name="item" property="lastLoginDate" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
		<td align="center">
	      	<bean:write name="item" property="registDateTime" format="yyyy-MM-dd"/>
		</td>

	   	<td align="center">
	   			<c:if test="${item.userType==12}">
	   				<a href='managerAction.do?method=init&agentId=<bean:write name="item" property="userId"/>&userType=1&category=<c:out value="${category}"/>'>下级会员</a><span>|</span>
<%--	   				<c:if test="${loginUser.agentId>0}">--%>
<%--	   					<input type="button" class="button" value="报表" 	onclick="location='../agent/agentAction.do?method=agentReport&lookType=1&userId=<bean:write name="item" property="userId"/>&usType=<c:out value="${item.userType}"/>';"/>--%>
<%--	   				</c:if>--%>
<%--	   				<c:if test="${loginUser.agentId==0}">	</c:if>--%>
	   					<input type="button" class="button" value="报表" 	onclick="location='../agent/agentAction.do?method=agentReport&userId=<bean:write name="item" property="userId"/>&usType=<c:out value="${item.userType}"/>';"/>
	   			</c:if>
			  <c:if test="${item.userType==1}">
			   <c:if test = "${loginUser.userType==2}">
			  		<input type="button" class="button" value="充值" 	onclick="location='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			  		<input type="button" class="button" value="扣款" 	onclick="location='managerAction.do?method=preModifyMoney&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			  	</c:if>
			  	<input type="button" class="button" value="报表" 	onclick="location='../agent/agentAction.do?method=agentReport&userId=<bean:write name="item" property="userId"/>&usType=<c:out value="${item.userType}"/>';"/>
			  	<input type="button" class="button" value="信用投注明细" onclick="location='../agent/agentAction.do?method=betDetailList&userId=<bean:write name="item" property="userId"/>';"/>
			  	<input type="button" class="button" value="官方投注明细" onclick="location='../agent/agentAction.do?method=gfBetDetailList&userId=<bean:write name="item" property="userId"/>';"/>
			  </c:if>
			  <c:if test="${item.userType==11}">
			 	  <input type="button" class="button" value="充值" onclick="location='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			 	  <input type="button" class="button" value="扣款" onclick="location='managerAction.do?method=preModifyMoney&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			 	  <input type="button" class="button" value="报表" onclick="location='../agent/agentAction.do?method=agentReport&userId=<bean:write name="item" property="userId"/>&usType=<c:out value="${item.userType}"/>';"/>
			  	  <input type="button" class="button" value="信用投注明细" onclick="location='../agent/agentAction.do?method=betDetailList&userId=<bean:write name="item" property="userId"/>';"/>
			  	  <input type="button" class="button" value="官方投注明细" onclick="location='../agent/agentAction.do?method=gfBetDetailList&userId=<bean:write name="item" property="userId"/>';"/>
			  </c:if>
			<c:if test = "${loginUser.userType==2}">
	   			<a href='managerAction.do?method=preModify&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		</c:if>
	   		<c:if test = "${loginUser.userType==12&&loginUser.agentId==0}">
	   			<a href='managerAction.do?method=preModify&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		</c:if>
	   		<%--<c:if test="${userPermission=='superAdmin'}">
	   		<a href='../tluser/profile.do?method=basic&&uid=<bean:write name="item" property="userId"/>'>修改2</a>
	   		</c:if>
	   		<c:if test = "${userType=='1'||userType=='9'||userType=='10'}">
	   			<a href="#" onclick="delUser('managerAction.do?method=delete&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>','<bean:write name="item" property="loginName"/>')" ><img class="xtbImg ico-del" src="../images/trash.gif"/></a>
	   		</c:if>
	   		<%if(Constants.chkFuncConfig("system.func.type.manage")){ %>
	   		<c:if test="${adminUserType eq '2'}">
			<a href="javascript:delItem(<bean:write name="item" property="userId"/>,<c:out value="${userType}"/>);"><img class="xtbImg ico-del" src="../images/trash.gif"/></a>
			</c:if>
			<%} %> --%>
	   	</td>

	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

</html:form>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>