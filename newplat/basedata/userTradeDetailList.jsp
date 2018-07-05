<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@ taglib uri="/content" prefix="content"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".pic_show").fancybox(
			{
				'overlayOpacity':0.5,
				'transitionIn'	: 'elastic',
				'transitionOut'	: 'elastic'
			}
		);
});
function del(codeId){
	if(confirm("您确定要删除这条数据吗？")){
		window.location.href='baseDataAction.do?method=delCardRechargeItem&itemId='+codeId;
	}
}


</script>
</head>
<body>
<div class="bread">明细管理&raquo; 资金明细
</div>
<html:form method="post" action="/baseDataAction.do?method=initUserTradeDetail" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
	
			<td>
			 <span>用户：<html:text property="userName" name="baseDataForm"/></span>
			 <span>收支：
				<html:select property="tradeType" name="baseDataForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">收入</html:option>
					<html:option value="2">支出</html:option>
				</html:select>
			</span>
			<span>资金来源：
				<html:select property="cashType" name="baseDataForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">充值</html:option>
					<html:option value="2">卡充</html:option>
					<html:option value="3">余额</html:option>
					<html:option value="7">供求交易</html:option>
					<html:option value="5">提现</html:option>
					<html:option value="6">提现审核返回</html:option>
					<html:option value="8">结算</html:option>
				</html:select>
			</span>
			<span>开始时间：<html:text property="startDate" name="baseDataForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="baseDataForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
				<input type="submit" class="gbutton" value="查询" />
			</td>
	</tr>
</table>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String status=(String)request.getAttribute("status");
String cityId=request.getAttribute("cityId")!=null?(String)request.getAttribute("cityId"):"";
String userName=(String)request.getAttribute("userName");
String tradeType=(String)request.getAttribute("tradeType");
String cashType=(String)request.getAttribute("cashType");
%>
<pg:pager 
	url="baseDataAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initUserTradeDetail"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<pg:param name="userName" value="<%=userName%>"/>
	<pg:param name="tradeType" value="<%=tradeType%>"/>
	<pg:param name="cashType" value="<%=cashType%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
	<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
			<th>用户</th>
			<th>收支</th>
			<th>资金来源</th>
			<th>资金</th>
	    	<th>时间</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
 		<td><c:out value="${item.userName}"/></td>
 		<td>
			<logic:equal name="item" property="userTradeDetail.tradeType" value="1">
		 		收入
		 	</logic:equal>
			<logic:equal name="item" property="userTradeDetail.tradeType" value="2">
				支出
			</logic:equal>
		</td>
		<td>
			<c:out value="${item.moduleType}"/>
		</td>
		<td><strong><c:out value="${item.userTradeDetail.cashMoney}"/></strong></td>
	   	<td>
			<bean:write name="item" property="userTradeDetail.createTime" format="yyyy-MM-dd HH:mm"/>
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
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>