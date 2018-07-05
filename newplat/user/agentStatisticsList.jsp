<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
</head>
<body>
<div class="bread">统计管理&raquo;代理统计
</div>
<html:form action="/managerAction.do?method=agentStatisticsList" method="post">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>类型：
				<html:select property="userType" name="managerForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="13">初级代理</html:option>
					<html:option value="14">中级代理</html:option>
					<html:option value="15">高级代理</html:option>
				</html:select>
			</span>
            <span>会员：<html:text property="loginName" name="managerForm"/></span>
			<span>开始时间：<html:text property="startDate" name="managerForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="managerForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>		
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userName = (String)request.getAttribute("userName");
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
	<pg:param name="method" value="agentStatisticsList"/>
	<pg:param name="userName" value="<%=userName %>" />
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>ID</th>
		<th>用户名</th>
	    <th>总打码</th>
	    <th>总中奖</th>
	    <th>输赢</th>
	    <th>下线会员</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="userId" />
	    </td>
	    <td align="center">
	    	 <bean:write name="item" property="userName" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="totalBet"/>
	    </td>
		<td align="center">
	        <bean:write name="item" property="winCash"/>
	    </td>
		<td align="center">
	        <bean:write name="item" property="paperMoney"/>
	    </td>
		<td align="center">
			<a href='managerAction.do?method=subUserList&agentId=<bean:write name="item" property="userId"/>'><bean:write name="item" property="number"/></a>
	    </td>
	</tr>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>