<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
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
<div class="bread">快三信息管理  &raquo; 订单列表</div>
<html:form action="/gaK3Action.do?method=initOrder">
<html:hidden property="startIndex" name="gaK3Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			订单号
			<html:text property="orderNum" name="gaK3Form"/>
			用户
			<html:text property="loginName" name="gaK3Form"/>
			订单状态
			<span>
				<html:select property="payStatus" name="gaK3Form" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="0">未付款</html:option>
					<html:option value="1">已付款</html:option>
				</html:select>
			</span>
			<span>开始时间：<html:text property="startTime" name="gaK3Form" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endTime" name="gaK3Form" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1; %>	
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String loginName=(String)request.getAttribute("loginName");
String payStatus=(String)request.getAttribute("payStatus");
%>
<pg:pager 
	url="gaK3Action.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initOrder"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<pg:param name="payStatus" value="<%=payStatus%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>订单号</th>
		<th>用户</th>
		<th>昵称</th>
		<th>下单时间</th>
		<th>总金额</th>
		<th>支付方式</th>
		<th>状态</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<a href='gaK3Action.do?method=view&id=<bean:write name="item" property="gaOrder.orderId"/>'><bean:write name="item" property="gaOrder.orderNum"/></a>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="loginName"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="userName"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="gaOrder.createTime" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="gaOrder.money"/>
	    </td>
	    <td align="center">
		    <c:if test="${item.gaOrder.payType =='1'}">
		    	支付宝
		    </c:if>
		    <c:if test="${item.gaOrder.payType =='2'}">
		     	银联
		    </c:if>
		    <c:if test="${item.gaOrder.payType =='5'}">
		   		微信
		    </c:if>
	    </td>
	    <td align="center">
		    <c:if test="${item.gaOrder.payStatus =='0'}">
		    	<ord-notpay>未付款</ord-notpay>
		    </c:if>
		    <c:if test="${item.gaOrder.payStatus =='1'}">
		     	<ord-pay>已付款</ord-pay>
		    </c:if>
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