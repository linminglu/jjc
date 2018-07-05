<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
</head>
<body>
<div class="bread">下线代理  &raquo;
</div>
<html:form action="/agentAction.do?method=subMemberList" method="post">
<input type="hidden" value="<c:out value='${userType}'/>" name="userType"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			 <span>会员等级：
				<html:select property="userType" name="agentForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">正式用户</html:option>
					<html:option value="2">测试用户</html:option>
					<html:option value="3">代理用户</html:option>
				</html:select>
			</span>
			会员
			<html:text property="userName" name="agentForm"/>
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userName = (String)request.getAttribute("userName");
String userType = (String)request.getAttribute("userType");
%>
<pg:pager 
	url="agentAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="subMemberList"/>
	<pg:param name="userName" value="<%=userName %>" />
	<pg:param name="userType" value="<%=userType %>" />
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th>会员账号</th>
		<th>真实姓名</th>
		<th>用户类型</th>
	    <th>上一级代理</th>
	    <th>最近登录</th>
	    <th>当前余额</th>
	    <th>状态</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
	    <td align="center">
	        <bean:write name="item" property="loginName" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="realName" />
	    </td>
	    
	    <td align="center">
	    <logic:equal name="item" property="userType" value="1">
	    </logic:equal>
	        <c:if test = "${item.userType==12}">
		              代理用户
	        </c:if>	
	        <c:if test = "${item.userType==1}">
		             正式用户
	        </c:if>
	        <c:if test = "${item.userType==11}">
		             测试用户
	        </c:if>
	    </td>
	    <td align="center">
		    <c:out value="${agentName}"/>
		</td>
		<td align="center">
	      	<bean:write name="item" property="lastLoginDate" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
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
	   	<td align="center">
	   		<input type="button" class="button" value="充值" onclick="location='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>';"/>
	   		<%-- <a href='managerAction.do?method=preModify&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		<c:if test="${userPermission=='superAdmin'}">
	   		<a href='../tluser/profile.do?method=basic&&uid=<bean:write name="item" property="userId"/>'>修改2</a>
	   		</c:if>
	   		<c:if test = "${userType=='1'||userType=='9'||userType=='10'}">
	   			<a href="#" onclick="delUser('managerAction.do?method=delete&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>','<bean:write name="item" property="loginName"/>')" ><img class="xtbImg ico-del" src="../images/trash.png"/></a>
	   		</c:if>
	   		<%if(Constants.chkFuncConfig("system.func.type.manage")){ %>
	   		<c:if test="${adminUserType eq '2'}">
			<a href="javascript:delItem(<bean:write name="item" property="userId"/>,<c:out value="${userType}"/>);"><img class="xtbImg ico-del" src="../images/trash.png"/></a>
			</c:if>
			<%} %> --%>
	   	</td>
	</tr>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>