<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>远程网络教学平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script src="../js/manager.js?t=3" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	managerObj.drawingResult();
});
</script>
</head>
<body>
<div class="bread">统计管理 &raquo;用户盈亏统计
</div>
<html:form action="/managerAction.do?method=userMoneyReport" method="post">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		    <span>用户名/ID：<html:text property="loginName" name="managerForm"></html:text></span>
			用户类型
			<html:select property="userType" name="managerForm">
				<html:option value="">全部</html:option>
				<html:option value="1">普通用户</html:option>
				<html:option value="2">数据管理员</html:option>
				<html:option value="13">初级代理</html:option>
				<html:option value="14">中级代理</html:option>
				<html:option value="15">高级代理</html:option>
				<html:option value="20">客服人员</html:option>
				<html:option value="21">财务人员</html:option>
			</html:select>
			<span>时间范围：<html:text property="startDate" name="managerForm" styleClass="Wdate" styleId="startTime" onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/>
			——&nbsp;&nbsp;<html:text property="endDate" name="managerForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
		    <input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String loginName =request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
String startDate =request.getAttribute("startDate")!=null?(String)request.getAttribute("startDate"):"";
String endDate =request.getAttribute("endDate")!=null?(String)request.getAttribute("endDate"):"";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
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
	<pg:param name="method" value="userMoneyReport"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="userType" value="<%=userType%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	 <c:if test ="${not empty managerForm.userMoneyDTO}">
	<tr style="background:#ddd">
		<td></td>
		<td></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.userName"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.totalRechage"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.totalDrawMoney"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.bet"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.win"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.fanshui"/></td>
		<td><bean:write name="managerForm" property="userMoneyDTO.huodong"/></td>
<%-- 		<td><bean:write name="managerForm" property="userMoneyDTO.commission"/></td> --%>
		<td><bean:write name="managerForm" property="userMoneyDTO.totalPayoff"/></td>
<!-- 		<td></td> -->
<!-- 		<td></td> -->
	</tr>
	</c:if>
	<tr>
		<th class="sel">序号</th>
		<th>ID</th>
		<th>用户名</th>
		<th>充值</th>
		<th>提款</th>
	    <th>下注</th>
	    <th>中奖</th>
	    <th>返水</th>
	    <th>活动</th>
<!-- 	    <th>佣金</th> -->
	    <th>盈亏</th>
<!-- 	    <th>下线会员</th> -->
<!-- 	    <th>操作</th> -->
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="userId" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userName" />
	    </td>
	    <td align="center" class="tdprice tdright">
	      	<bean:write name="item" property="totalRechage" format="0.00"/>
		</td>
	    <td align="center" class="tdprice tdright">
		    <bean:write name="item" property="totalDrawMoney" format="0.00"/>
		</td>
	    <td align="center" class="tdprice tdright">
	    	<bean:write name="item" property="bet" format="0.00"/>
	    </td>
	    <td align="center" class="tdprice tdright">
	    	<bean:write name="item" property="win" format="0.00"/>
	    </td>
	    <td align="center" class="tdprice tdright">
	    	<bean:write name="item" property="fanshui" format="0.00"/>
	    </td>
	    <td align="center" class="tdprice tdright">
	    	<bean:write name="item" property="huodong" format="0.00"/>
	    </td>
<!-- 	    <td align="center" class="tdprice tdright"> -->
<%-- 	    	<bean:write name="item" property="commission" format="0.00"/> --%>
<!-- 	    </td> -->
	    <td align="center" class="tdprice tdright">
	        <bean:write name="item" property="totalPayoff" format="0.00"/>
	    </td>
<!-- 	    <td align="center"> -->
<%-- 	    	<a href='managerAction.do?method=subUserList&agentId=<bean:write name="item" property="userId"/>'><bean:write name="item" property="memberNum"/></a> --%>
<!-- 	    </td> -->
<!-- 	    <td align="center"> -->
<%-- 			<a class="link" href='managerAction.do?method=subUserMoneyReportList&agentId=<bean:write name="item" property="userId"/>'>下级盈亏明细</a> --%>
<!-- 	    </td> -->
	</tr>
	</pg:item>
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