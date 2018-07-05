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
<div class="bread">快三信息管理  &raquo; 投注清单详情</div>
<html:form action="/gaK3Action.do?method=betDetail">
<html:hidden property="startIndex" name="gaK3Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<%-- <span>开始时间：<html:text property="startTime" name="gaK3Form" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endTime" name="gaK3Form" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/> --%>
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String sid=(String)request.getAttribute("sid");
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
	<pg:param name="method" value="betDetail"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="sid" value="<%=sid%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<%-- <c:if test="${action2==1}">
		<th>用户名</th>
		</c:if> --%>
		<th>投注时间</th>
		<th>投注钱数</th>
		<th>投注内容</th>
		<th>中奖结果</th>
		<th>会员收付</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <%-- <c:if test="${action2==1}">
	    <td align="center">
	     	<bean:write name="item" property="user.loginName"/>
	    </td>
	    </c:if> --%>
	    <td align="center">
	     	<bean:write name="item" property="gaK3BetDetail.betTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="gaK3BetDetail.betPoint"/>
	    </td>
	    <td align="center">
			<bean:write name="item" property="gaK3BetDetail.betContent"/>  
	    </td>
	    <td align="center">
	    <logic:equal name="item" property="gaK3Session.openStatus" value="1">
				<font color="red"><strong>未开奖</strong></font>&nbsp;
			</logic:equal>
		    <logic:equal name="item" property="gaK3BetDetail.betFlag" value="0">
				<font color="red"><strong>取消投注</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="gaK3BetDetail.winResult" value="1">
				<font color="green"><strong>中奖</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="gaK3BetDetail.winResult" value="2">
				<font color="green"><strong>未中奖</strong></font>&nbsp;
			</logic:equal>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="gaK3BetDetail.winCash"/>
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