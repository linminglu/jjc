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
<script language="javascript" src="../js/platjs.js"></script>
<script type="text/javascript">
function exp(){
	if(confirm("您确定要导出这张表数据吗？")){
		document.forms[0].action = "cashAction.do?method=applyCashExport";
		document.forms[0].submit();
		document.forms[0].action = "cashAction.do?method=init";
	}
}
</script>
</head>
<body>
<div class="bread">充值/提现管理 &raquo;提现管理</div>
<html:form method="post" action="/cashAction.do?method=init" >
<input type="hidden" name="pager.offset" value="<c:out value="${startIndex}"/>"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			  用户名/ID：
			 <html:text property="userName" name="cashForm"/>
			 <span>申请时间从：<html:text property="startDate" name="cashForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>到：<html:text property="endDate" name="cashForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			审核状态<html:select property="userApplyCash.auditStatus" name="cashForm">
				<html:option value="">请选择</html:option>
				<html:option value="0">待审核</html:option>
				<html:option value="1">已通过</html:option>
				<html:option value="2">未通过</html:option>
			</html:select>

			&nbsp;<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" name="delBtn" value="导出" onclick="exp();">
		</td>
	</tr>
</table>
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String status=(String)request.getAttribute("status");
String userName=(String)request.getAttribute("userName");
String bindName=(String)request.getAttribute("bindName");
String bankName=(String)request.getAttribute("bankName");
String bindAccount=(String)request.getAttribute("bindAccount");



%>
<pg:pager 
	url="cashAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="userName" value="<%=userName%>"/>
	<pg:param name="bindName" value="<%=bindName%>"/>
	<pg:param name="bankName" value="<%=bankName%>"/>
	<pg:param name="bindAccount" value="<%=bindAccount%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<bean:size id="length" name="list"/>
	<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		    <th width="60">ID</th>
			<th>用户名</th>
			<th>银行</th>
			<th>开户名</th>
	    	<th>账户</th>
	    	<th width="80">提现金额</th>
	    	<th width="120">申请时间</th>
	    	<th width="120">审核时间</th>
	    	<th width="50">审核状态</th>
	    	<th width="50">操作</th>
	    </tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	 		<%-- <td><a href='cashAction.do?method=view&id=<c:out value="${item.id}"/>'><content:out maxLength="20"><c:out value="${item.title}"/></content:out></a></td> --%>
 		<td>
 			<a class="link" href="../../cash/allCashDetailList.jsp?userId=<bean:write name="item" property="user.userId"/>" target="fraContent">
 				<bean:write name="item" property="user.userId"/>
 			</a>
 		</td>
 		<td><bean:write name="item" property="user.userName"/></td>
 		<td><bean:write name="item" property="userApplyCash.bankName"/></td>
 		<td><bean:write name="item" property="userApplyCash.userName"/></td>
 		<td><bean:write name="item" property="userApplyCash.accountNo"/></td> 		
 		<td class="tdprice tdright"><bean:write name="item" property="userApplyCash.cashMoney"/></td>
 		<td><bean:write name="item" property="userApplyCash.createTime" format="yyyy-MM-dd HH:mm "/></td>
 		<td><bean:write name="item" property="userApplyCash.auditTime" format="yyyy-MM-dd HH:mm"/></td>
 		<td>
 			<c:if test="${item.userApplyCash.auditStatus eq 0}">
	 			<strong>待审核</strong>
	 		</c:if>
	 		<c:if test="${item.userApplyCash.auditStatus eq 1}">
	 			<strong><font color ="green">已通过</font></strong>
	 		</c:if>
	 		<c:if test="${item.userApplyCash.auditStatus eq 2}">
	 			<strong><font color ="red">未通过</font></strong>
	 		</c:if>
 		</td>
 		 <td>
			<c:if test="${item.userApplyCash.auditStatus eq 0 }">
			  <a class="link" href="../cash/cashAction.do?method=auditing&id=<c:out value='${item.userApplyCash.applyCashId}'/>"><strong>审核</strong></a>
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
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>