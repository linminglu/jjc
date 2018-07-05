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
<script type="text/javascript">

</script>
</head>
<body>
<div class="bread">快三信息管理  &raquo; 历史开奖记录</div>
<html:form action="/gaK3Action.do?method=initLotteryResults">
<html:hidden property="startIndex" name="gaK3Form"/>

<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>开始时间：<html:text property="startTime" name="gaK3Form" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endTime" name="gaK3Form" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>开始期数：<html:text property="sessionNoStart" name="gaK3Form" onkeyup="value=value.match( /^[1-9]\d*|0$/)" /></span>
			<span>结束期数：<html:text property="sessionNoEnd" name="gaK3Form" onkeyup="value=value.match( /^[1-9]\d*|0$/)" /></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startTime=(String)request.getAttribute("startTime");
String endTime=(String)request.getAttribute("endTime");
String sessionNoStart=(String)request.getAttribute("sessionNoStart");
String sessionNoEnd=(String)request.getAttribute("sessionNoEnd");
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
	<pg:param name="method" value="initLotteryResults"/>
	<pg:param name="startTime" value="<%=startTime%>"/>
	<pg:param name="endTime" value="<%=endTime%>"/>
	<pg:param name="sessionNoStart" value="<%=sessionNoStart%>"/>
	<pg:param name="sessionNoEnd" value="<%=sessionNoEnd%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>期数</th>
		<th>开奖日期</th>
		<th>开奖结果</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="sessionNo"/>
	    </td>
	    <td>
	    	<bean:write name="item" property="openTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="openResult"/>
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