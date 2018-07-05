<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>海豚彩票</title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="bread">交易  &raquo; 交易明细</div>
<html:form action="/agentAction.do?method=tradeDetailList">
<html:hidden property="startIndex" name="agentForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			 <span>会员等级：
				<html:select property="userType" name="agentForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">正式用户</html:option>
			<!-- <html:option value="2">测试用户</html:option> -->		
					<html:option value="3">代理用户</html:option>
				</html:select>
			</span>
			<span>类型：
				<html:select property="cashType" name="agentForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">在线充值</html:option>
					<html:option value="14">系统充值</html:option>
					<html:option value="3">线下充值</html:option>
					
					<html:option value="10">购买彩票</html:option>
					<html:option value="21">参与合买</html:option>
					<html:option value="11">方案保底</html:option>
					<html:option value="13">方案撤单</html:option>
					<html:option value="12">保底冻结</html:option>
					<html:option value="17">方案退保</html:option>
					<html:option value="18">中奖彩派</html:option>
					
					<html:option value="20">积分兑换</html:option>
					<html:option value="5">提现</html:option>
					<html:option value="6">提现审核退款</html:option>
				</html:select>
			</span>
            <span>会员：<html:text property="userName" name="agentForm"/></span>
			<span>开始时间：<html:text property="startDate" name="agentForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="agentForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>
<%	
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType = (String)request.getAttribute("userType");
String cashType = (String)request.getAttribute("cashType");
String userName = (String)request.getAttribute("userName");
String startDate = (String)request.getAttribute("startDate");
String endDate = (String)request.getAttribute("endDate");
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
	<pg:param name="method" value="tradeDetailList"/>
	<pg:param name="userType" value="<%=userType %>"/>
	<pg:param name="cashType" value="<%=cashType %>"/>
	<pg:param name="userName" value="<%=userName %>"/>
	<pg:param name="startDate" value="<%=startDate %>"/>
	<pg:param name="endDate" value="<%=endDate %>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
	    <th>序号</th>
		<th>ID</th>
		<th>账号</th>
		<th>类型</th>
		<th>金额</th>
		<th>余额</th>
		<th>操作时间</th>
		<th>明细</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	        <bean:write name="item" property="user.userId"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="user.userName"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userTradeDetail.cashType"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userTradeDetail.cashMoney"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userTradeDetail.userMoney"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userTradeDetail.createTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userTradeDetail.remark"/>
	    </td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</body>
</html:html>