<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@ taglib uri="/content" prefix="content"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>海豚彩票</title>
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
function del(createUserIndentId){
	if(confirm("您确定要删除这条数据吗？")){
		window.location.href='cardAction.do?method=saveJs&createUserIndentId='+createUserIndentId;
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
		document.forms[0].action = "cardAction.do?method=initViewList";
		document.forms[0].submit();
	}
}
function excel(){
	if(confirm("您确定要导出这批数据吗？")){
		document.forms[0].action = "cardAction.do?method=initROrder&flag=1";
		document.forms[0].submit();
	}
}
function sele(){
		document.forms[0].action = "cardAction.do?method=initROrder";
		document.forms[0].submit();
}
function send(id,con){
	if(confirm("您确定要"+con+"吗？一旦发货就不能改变！")){
		window.location.href='cardAction.do?method=deliver&id='+id;
	}
}
</script>
</head>
<body>
<div class="bread">充值/提现管理 &raquo;充值订单</div>
<html:form method="post" action="/cardAction.do?method=initROrder" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户:
			<span><html:text property="userName" name="cardForm"/></span>
			订单号:
			<span><html:text property="orderNum" name="cardForm"/></span>
			付款状态:
			<span>
				<html:select property="payStatus" name="cardForm" styleClass="sele">
					<html:option value="">全部</html:option>
					<html:option value="1">未付款</html:option>
					<html:option value="2">已付款</html:option>
				</html:select>
			</span>
			<span>开始时间：<html:text property="startDate" name="cardForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="cardForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>		
			<input type="button" class="gbutton" value="查询"  onclick="sele();"/>
		</td>
	</tr>
		
</table>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String userName=(String)request.getAttribute("userName");
String xfStatus=(String)request.getAttribute("xfStatus");
String payStatus=(String)request.getAttribute("payStatus");
String createUserId=(String)request.getAttribute("createUserId");
String type=(String)request.getAttribute("type");
String cityId=request.getAttribute("cityId")!=null?(String)request.getAttribute("cityId"):"";
%>
<pg:pager 
	url="cardAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initROrder"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="userName" value="<%=userName%>"/>
	<pg:param name="xfStatus" value="<%=xfStatus%>"/>
	<pg:param name="payStatus" value="<%=payStatus%>"/>
	<pg:param name="createUserId" value="<%=createUserId%>"/>
	<pg:param name="type" value="<%=type%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<logic:notEmpty name="cardOrderList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
	  <%-- <c:if test="${createUser.createUserType == '4' || createUser.createUserType == '2'}">
		<th class="sel">全部</th>
	 </c:if> --%>
		<th>序号</th>
		<th>订单号</th>
	   <!--  <th>状态</th> -->
		<th>用户</th>
		<th>标题</th>
		<th>支付方式</th>
	    <th>付款</th>
	    <th>付款金额</th>
	    <th>下单时间</th>
	    <th>付款时间</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="cardOrderList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	 	<td><%-- <a href='cardAction.do?method=viewcreateUserDd&createUserIndentId=<c:out value="${item.cardRechargeOrder.createUserIndentId}"/>'> --%>
	 	<c:out value="${item.cardRechargeOrder.orderNum}"/><!-- </a> --></td>
	 	<%-- <td>
	 		<logic:equal name="item" property="cardRechargeOrder.payStatus" value="1"><strong><font color ="green">有效</font></strong></logic:equal>
	 		<logic:equal name="item" property="cardRechargeOrder.payStatus" value="0"><strong><font color ="red">无效</font></strong></logic:equal>
	 	</td> --%>
	 	<td>
	 		<logic:empty name="item" property="createUser.loginName"><c:out value="${item.createUser.userName}"/></logic:empty>
			<logic:notEmpty name="item" property="createUser.loginName"><c:out value="${item.createUser.loginName}"/></logic:notEmpty>
	 	</td>
	 	<td><content:out maxLength="20"><c:out value="${item.cardRechargeItem.title}"/></content:out></td>
	 	<td>
	 		<c:if test="${item.cardRechargeOrder.payType==1}">支付宝</c:if>
	 		<c:if test="${item.cardRechargeOrder.payType==2}">银联</c:if>
	 		<c:if test="${item.cardRechargeOrder.payType==3}">线下支付</c:if>
	 		<c:if test="${item.cardRechargeOrder.payType==4}">积分</c:if>
	 		<c:if test="${item.cardRechargeOrder.payType==5}">微信</c:if>
	 	</td>
	 	<td>
            <strong><font color ="red"><c:if test="${item.cardRechargeOrder.payStatus==1}">未付款</c:if></font></strong>
	 		<c:if test="${item.cardRechargeOrder.payStatus==2}">已付款</c:if>
	 		<c:if test="${item.cardRechargeOrder.payStatus==3}">已拒绝</c:if>
	 		<c:if test="${item.cardRechargeOrder.payStatus==4}">已充值</c:if>
	 	</td>
	 	<td><strong><font color ="bule"><logic:notEmpty name="item" property="cardRechargeOrder.totalMoney"><bean:write name="item" property="cardRechargeOrder.totalMoney"/></logic:notEmpty></font></strong>&nbsp;</td>	
	 		
	 	<td><bean:write name="item" property="cardRechargeOrder.createTime" format="yyyy-MM-dd HH:mm:ss" />&nbsp;</td>
	 	<td><bean:write name="item" property="cardRechargeOrder.payTime" format="yyyy-MM-dd HH:mm:ss" />&nbsp;</td>
	 	<td>
		 	<a href='cardAction.do?method=viewCardRechargeOrder&orderId=<c:out value="${item.cardRechargeOrder.orderId}"/>'>
		 		详情/充值
		 	</a>
	 	</td>
	 	
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="cardOrderList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>