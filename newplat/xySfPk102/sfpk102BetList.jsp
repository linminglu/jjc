<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
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
<html:form action="/sfPk102Action.do?method=betList">
<html:hidden property="startIndex" name="sfPk102Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
    		期号:<html:text property="sessionNo"/>	
    		<!--
			<span>开始时间：<html:text property="startDate" name="sfPk102Form" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="sfPk102Form" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		-->
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String sessionNo=(String)request.getAttribute("sessionNo");
%>
<pg:pager 
	url="sfPk102Action.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="betList"/>
	<pg:param name="sessionNo" value="<%=sessionNo%>"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<c:if test ="${not empty sfPk102Form.sfPk102Dto}">
	<tr style="background:#ddd">
		<td></td>
		<td>合计</td>
		<td><bean:write name="sfPk102Form" property="sfPk102Dto.totalPoint"/></td>
		<td><bean:write name="sfPk102Form" property="sfPk102Dto.winCash"/></td>
		<td><bean:write name="sfPk102Form" property="sfPk102Dto.payoff"/></td>
	</tr>
	</c:if>
	<tr>
		<th class="sel">序号</th>
		<th>期号</th>
		<!--
		<th>开始时间</th>
		<th>结束时间</th>
		-->
		<th>投注金额</th>
		<th>用户中奖</th>
		<th>盈亏</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="sessionNo"/>
	    </td>
	    <!--
	    <td align="center">
	        <bean:write name="item" property="startTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="endTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    -->
	    <td align="center">
	        <bean:write name="item" property="totalPoint"/>
	    </td>
	    <td align="center">
			<bean:write name="item" property="winCash"/>
	    </td>
	    	    <td align="center">
			<bean:write name="item" property="payoff"/>
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