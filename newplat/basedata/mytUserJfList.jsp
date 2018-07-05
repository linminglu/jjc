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
function del(userIndentId){
	if(confirm("您确定要删除这条数据吗？")){
		window.location.href='baseDataAction.do?method=saveJs&userIndentId='+userIndentId;
	}
}
function selectAll(index, f){
	var o = document.getElementsByName("selectItems");
    if(o!=null && o.length>0){
    	var b = index * 10;
  		var e = b + 20;
  		e = e > o.length ? o.length : e;
  		for (i=b; i<e; i++){
 			o[i].checked = f.checked ? true : false;
    	}
    }else{
   		var f = document.forms[0];
  		for (i=0;i<f.elements.length;i++){  	
    		if (f.elements[i].name=="selIndex"){
    			f.elements[i].checked = f;
    		}
    	}
  		document.forms[0].elements["all"].checked = f;	
    }
}
function js(){
	var items = document.getElementsByName("selectItems");
	var selCount = 0;
	if(items && items.length>0){
		for(var i=0;i<items.length;i++){
			if(items[i].checked){
				selCount++;
			}
		}
	}
	if(selCount==0){
		alert("请选择要结算的数据！");
		return false;
	}
	if(confirm("您确定要结算这批数据吗？")){
		document.forms[0].action = "baseDataAction.do?method=initViewList";
		document.forms[0].submit();
	}
}
function excel(){
	if(confirm("您确定要导出这批数据吗？")){
		document.forms[0].action = "baseDataAction.do?method=init&flag=1";
		document.forms[0].submit();
	}
}
function sele(){
		document.forms[0].action = "baseDataAction.do?method=init";
		document.forms[0].submit();
}
function send(id,con){
	window.location.href='baseDataAction.do?method=sendSupply&id='+id;
}
function sendPay(id,con){
	if(confirm("您确定要"+con+"吗？一旦"+con+"就不能改变！")){
		window.location.href='baseDataAction.do?method=deliverPay&id='+id;
	}
}
</script>
</head>
<body>
<div class="bread">订单管理 &raquo; 订单管理
</div>
<html:form method="post" action="/baseDataAction.do?method=init" >
<html:hidden property="moduleType" name="baseDataForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<%-- <tr class="moon">
		<td>
			用户:
			<span><html:text property="name" name="baseDataForm"/></span>
			标题:
			<span><html:text property="title" name="baseDataForm"/></span>
			付款状态:
			<span>
				<html:select property="xfStatus" name="baseDataForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="2">已付款</html:option>
					<html:option value="1">未付款</html:option>
				</html:select>
			</span>			
			<span>订单开始时间：<html:text property="startDate" name="baseDataForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>订单结束时间：<html:text property="endDate" name="baseDataForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<input type="button" class="gbutton" value="查询"  onclick="sele();"/>
		</td>
	</tr> --%>
</table>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String name=(String)request.getAttribute("name");
String xfStatus=(String)request.getAttribute("xfStatus");
String ddStatus=(String)request.getAttribute("ddStatus");
String title=(String)request.getAttribute("title");
String userId=(String)request.getAttribute("userId");
String type=(String)request.getAttribute("type");
String action=(String)request.getAttribute("action");
String payType=(String)request.getAttribute("payType");
String id=(String)request.getAttribute("id");
String cityId=request.getAttribute("cityId")!=null?(String)request.getAttribute("cityId"):"";
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
	<pg:param name="method" value="init"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="name" value="<%=name%>"/>
	<pg:param name="xfStatus" value="<%=xfStatus%>"/>
	<pg:param name="ddStatus" value="<%=ddStatus%>"/>
	<pg:param name="title" value="<%=title%>"/>
	<pg:param name="userId" value="<%=userId%>"/>
	<pg:param name="type" value="<%=type%>"/>
	<pg:param name="action" value="<%=action%>"/>
	<pg:param name="payType" value="<%=payType%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<pg:param name="id" value="<%=id%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<logic:notEmpty name="userJfList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th>序号</th>
		<th>订单号</th>
		<th>用户</th>
		<th>标题</th>
		<th>金额</th>
		<th>交易类型</th>
	    <th>状态</th>
	    <th>手续费</th>
		<th>结算金额</th>
	    <th>结算状态</th>
	    <th>付款时间</th>
	</tr>
	<logic:iterate id="item" name="userJfList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		
		<td class="numbers"><%=num.intValue()+1 %></td>
	 	<td><a href='../myt/mytUserManagerAction.do?method=viewUserDd&userIndentId=<c:out value="${item.mytUserIndent.userIndentId}"/>'><c:out value="${item.mytUserIndent.orderNum}"/></a></td>
	 	
	 	<td>
	 		<logic:empty name="item" property="userName"><c:out value="${item.loginName}"/></logic:empty>
			<logic:notEmpty name="item" property="userName"><c:out value="${item.userName}"/></logic:notEmpty>
	 	</td>
	 	<td><content:out maxLength="20"><c:out value="${item.title}"/></content:out></td>
	 	<td>
	 		<c:out value="${item.mytUserIndent.money}"/>
	 	</td>
	 	<td>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="1">
	 			在线支付
	 		</logic:equal>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="2">
	 			积分支付
	 		</logic:equal>
	 	</td>
	 	<td>
	 		<c:if test="${item.mytUserIndent.payStatus==1}">
	 			<strong><font color ="red">待付款</font></strong>
	 		</c:if>
	 		<c:if test="${item.mytUserIndent.payStatus==2}">
	 			<strong><font color ="green">已付款</font></strong>
	 		</c:if>
	 		<c:if test="${item.mytUserIndent.payStatus==3}">
	 			<strong><font color ="green">已完成</font></strong>
	 		</c:if>
	 		<c:if test="${item.mytUserIndent.payStatus==5}">
	 			<strong><font color ="green">已完成</font></strong>
	 		</c:if>
	 		<c:if test="${item.mytUserIndent.payStatus==6}">
	 			<strong><font color ="green">关闭</font></strong>
	 		</c:if>
	 	</td>
	 	<td>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="1" >
	 			<c:out value="${item.mytUserIndent.serviceMoney}"/>
	 		</logic:equal>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="2">
	 			
	 		</logic:equal>
	 	</td>
	 	<td>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="1" >
	 			<c:out value="${item.mytUserIndent.finalMoney}"/>
	 		</logic:equal>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="2">			
	 		</logic:equal> 		
	 	</td>
	 	<td>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="1">
	 			<c:if test="${item.mytUserIndent.isCheckout==1}">
	 				<strong><font color ="green">已结算</font></strong>
	 			</c:if>
	 			<c:if test="${item.mytUserIndent.isCheckout!=1}">
	 				<strong><font color ="green">未结算</font></strong>
	 			</c:if>
	 		</logic:equal>
	 		<logic:equal name="item" property="mytUserIndent.moneyType" value="2">
	 		</logic:equal>
	 	</td>
	 	<td><bean:write name="item" property="mytUserIndent.payTime" format="yyyy-MM-dd HH:mm" />&nbsp;</td>
	 	
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="userJfList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>