<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@ taglib uri="/content" prefix="content"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
</head>
<body>
<div class="bread">统计管理&raquo;网站总报表</div>
<html:form method="post" action="/agentAction.do?method=report" >
<input type="hidden" value="<c:out value='${userId}'/>"  name="userId"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
			<td>  
			<span>开始时间：<html:text property="startDate" name="agentForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="agentForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
				<input type="submit" class="gbutton" value="查询" />
			</td>
	</tr>
</table>
</html:form>
<table class="tablethree">
	<thead>
		<tr>
			<th colspan="15"><c:out value="${agentForm.startDate}"/>至 <c:out value="${agentForm.endDate}"/> 会员财务报表</th>
		</tr>
		<tr>
			<th colspan="2">直系下级</th>
			<th colspan="1">后台存款</th>
			<th colspan="1">汇款、取款</th>
			<th colspan="3">红利派送</th>
			<th rowspan="2">账户总余额</th>
		</tr>
		<tr>
			<th>代理</th>
			<th>会员</th>
		    <th>正常充值</th>
			<th>提款</th>
			<th>返水</th>
			<th>红包/转盘</th>
			<th>彩金派送</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
			<c:choose>
			<c:when test = "${loginUser.userType==2}">
			  <a href='../user/managerAction.do?method=agentList'><c:out value="${agentNum}"/></a>
			</c:when>
			<c:otherwise>
			  <a href='../agent/agentAction.do?method=agentList'><c:out value="${agentNum}"/></a>
			</c:otherwise>
			</c:choose>
			</td>
			<td>
			<c:choose>
			<c:when test = "${loginUser.userType==2}">
			<a href='../user/managerAction.do?method=userList'><c:out value="${memberNum}"/></a>
			</c:when>
			<c:otherwise>
			<a href='../agent/agentAction.do?method=subUserList&agentId=<c:out value="${userId}"/>'><c:out value="${memberNum}"/></a>
			</c:otherwise>
			</c:choose>
			</td>
			<td><c:out value="${totalRechage}"/></td>
			<td><c:out value="${totalDrawMoney}"/></td>
			<td><c:out value="${fanshui}"/></td>
			<td><c:out value="${luckyMoney}"/></td>
			<td><c:out value="${win}"/></td>
			<td rowspan="2"><c:out value="${money}"/></td>
	  </tr> 
	  <tr>
		 <th colspan="2">入款总计:</th>
		 <td><c:out value="${incomeMoney}"/></td>
		 <th colspan="">出款总计</th>
		<td><c:out value="${outMoney}"/></td>
		 <th colspan="">出入款差:</th>
		<td><c:out value="${rechageDrawPayoff}"/></td>
      </tr>
      <tr>
		 <th colspan="2">其它加款:</th>
		 <td><c:out value="${otherRechage}"/></td>
		 <th colspan="">其它扣款</th>
		<td><c:out value="${otherDecuteMoney}"/></td>
      </tr>
	</tbody>
</table>
<pg:pager > 
<logic:notEmpty name="totalSumDto">
<table class="tableone">
	<thead>
	<tr>
		<th colspan="15"><c:out value="${agentForm.startDate}"/> 至 <c:out value="${agentForm.endDate}"/> 会员投注总计表</th>
	</tr>
	</thead>
	<tbody>
		<tr>
			<td colspan="3"></td>
			<td>
				<table class="tabletow" border="0">
					<thead>
					<tr>
					<th colspan="3"><bean:write name="totalSumDto" property="gameName"/></th>
					</tr>
					<tr>
						<th>下注金额</th>
						<th>派彩</th>
						<th>盈亏</th>	
					</tr>
					</thead>
					<tbody>
						<tr>
						  <td><bean:write name="totalSumDto" property="betMoney2"/></td>
				          <td><bean:write name="totalSumDto" property="winMoney"/></td>
				          <td><bean:write name="totalSumDto" property="payoff"/></td>
						</tr>
					</tbody>
				</table>
			 </td>
			 <td colspan="3"></td>
		 </tr>
	</tbody>
</table>
</logic:notEmpty>
<logic:notEmpty name="gameBetAndWinList">
<table class="tableone">
	<thead>
	<tr>
		<th colspan="15"><c:out value="${agentForm.startDate}"/> 至 <c:out value="${agentForm.endDate}"/> 会员投注报表</th>
	</tr>
	</thead>
	<tbody>
	<%List list = (ArrayList)request.getAttribute("gameBetAndWinList"); 
    	int size = list.size();        	
    	int count=0;
    %>
	<logic:iterate id="item" name="gameBetAndWinList" indexId="number">
	<%
		if((number.intValue() % 3)==0){	
		 count++;	
	%>
	<tr >
	<%
		}
	%>
		<td>
			<table class="tabletow" border="0">
				<thead>
				<tr>
				<th colspan="3"><bean:write name="item" property="gameName"/></th>
				</tr>
				<tr>
					<th>下注金额</th>
					<th>派彩</th>
					<th>盈亏</th>	
				</tr>
				</thead>
				<tbody>
					<tr>
					  <td><bean:write name="item" property="betMoney2"/></td>
			          <td><bean:write name="item" property="winMoney"/></td>
			          <td><bean:write name="item" property="payoff"/></td>
					</tr>
				</tbody>
			</table>
		 </td>
		 <%	
			if(((number.intValue()-2) % 3) ==0){	    
		%>
		</tr>
		<%} %>
	    </logic:iterate>
	</tbody>
</table>
</logic:notEmpty>
</pg:pager>
</body>
<style type="text/css">
.tableone th {
   	color: #378;
    border: 1px solid #c2c2c2;
    border-left: 0;
    border-top: 0;
    display: table-cell;
    vertical-align: inherit;
    padding: 8px 0;
}
.tableone td {
    text-align: center;
}
.tableone {
    border: 1px solid #E9EEF0;
    border-right: 0;
    border-bottom: 0;
    color1: #4C748C;
    width: 100%;
    cellspacing:0;
    border-collapse:collapse;
}
.tabletow{width: 100%;cellspacing:0;border-collapse:collapse;}
.tabletow>tbody>tr>td {
    border-right: 1px solid #c2c2c2; 
    border-bottom: 1px solid #c2c2c2; 
    text-align: center;
    padding: 8px 0;
    width: 33.333333%
}
.tablethree {
    border: 1px solid #E9EEF0;
    border-right: 0;
    border-bottom: 0;
    color1: #4C748C;
    width: 100%;
    cellspacing:0;
    border-collapse:collapse;
}
.tablethree th {
   	color: #378;
    border: 1px solid #c2c2c2;
    border-left: 0;
    border-top: 0;
    display: table-cell;
    vertical-align: inherit;
    padding: 8px 0;
}
.tablethree td {
    text-align: center;
}
.tablethree>tbody>tr>td {
    border-right: 1px solid #c2c2c2; 
    border-bottom: 1px solid #c2c2c2; 
    text-align: center;
    padding: 8px 0;
}
</style>
</html>