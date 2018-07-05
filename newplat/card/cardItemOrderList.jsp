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
		document.forms[0].action = "cardAction.do?method=initCardItemOrder&flag=1";
		document.forms[0].submit();
	}
}
function sele(){
		document.forms[0].action = "cardAction.do?method=initCardItemOrder";
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
<html:form method="post" action="/cardAction.do?method=initCardItemOrder" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户:
			<span><html:text property="userName" name="cardForm"/></span>
			订单号:
			<span><html:text property="orderNum" name="cardForm"/></span>
			<%-- 卡号:
			<span><html:text property="cardCode" name="cardForm"/></span> --%>
			
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
String ddStatus=(String)request.getAttribute("ddStatus");
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
	<pg:param name="method" value="initCardItemOrder"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="userName" value="<%=userName%>"/>
	<pg:param name="xfStatus" value="<%=xfStatus%>"/>
	<pg:param name="ddStatus" value="<%=ddStatus%>"/>
	<pg:param name="createUserId" value="<%=createUserId%>"/>
	<pg:param name="type" value="<%=type%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<logic:notEmpty name="cardOrderList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th>序号</th>
		<th>订单号</th>
	   <!--  <th>状态</th> -->
		<th>用户</th>
		<!-- <th>卡号</th> -->
		<th>支付方式</th>
	    <th>付款</th>
	    <th>总金额</th>
	     <th>下单时间</th>
	    <th>付款时间</th>
	    <th>消费</th>
	    
	</tr>
	<logic:iterate id="item" name="cardOrderList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		
		<td class="numbers"><%=num.intValue()+1 %></td>
	 	<td><a href='cardAction.do?method=viewCardItemOrder&id=<c:out value="${item.cardItemOrder.orderId}"/>'>
	 	<c:out value="${item.cardItemOrder.orderNum}"/></a></td>
	 	
	 	<td>
	 		<logic:empty name="item" property="createUser.userName"><c:out value="${item.createUser.loginName}"/></logic:empty>
			<logic:notEmpty name="item" property="createUser.userName"><c:out value="${item.createUser.userName}"/></logic:notEmpty>
	 	</td>
	 	<%-- <td><content:out maxLength="20"><c:out value="${item.card.cardCode}"/></content:out></td> --%>
	 	<td>
	 		<c:if test="${item.cardItemOrder.payType==1}">支付宝</c:if>
	 		<c:if test="${item.cardItemOrder.payType==2}">银联</c:if>
	 		<c:if test="${item.cardItemOrder.payType==3}">线下支付</c:if>
	 		<c:if test="${item.cardItemOrder.payType==4}">积分</c:if>
	 		<c:if test="${item.cardItemOrder.payType==5}">微信</c:if>
	 	</td>
	 	<td>
	 		<logic:equal name="item" property="cardItemOrder.payStatus" value="1"><strong><font color ="red">未付款</font></strong></logic:equal>
	 		<logic:notEqual name="item" property="cardItemOrder.payStatus" value="1"><strong><font color ="green">已付款</font></strong></logic:notEqual>
	 		&nbsp;
	 	</td>
	 	<td><strong><font color ="bule"><logic:notEmpty name="item" property="cardItemOrder.totalMoney"><bean:write name="item" property="cardItemOrder.totalMoney"/></logic:notEmpty></font></strong>&nbsp;</td>
	 	<td><strong><bean:write name="item" property="cardItemOrder.createTime" format="yyyy-MM-dd HH:mm:ss" />&nbsp;</strong></td>
	 	<td><strong><bean:write name="item" property="cardItemOrder.payTime" format="yyyy-MM-dd HH:mm:ss" />&nbsp;</strong></td>
	 	
	 	<td align="center">
		     <c:if test="${item.cardItemOrder.payStatus =='1'}">
	    		待付款
	    	</c:if>
	    	<c:if test="${item.cardItemOrder.payStatus =='2'}">
	    		<a href='javascript:void(0);' onclick='send(<bean:write name="item" property="cardItemOrder.orderId"/>,"发货");'>确认发货</a>
	    	</c:if>
	    	<c:if test="${item.cardItemOrder.payStatus =='3'}">
	    		已发货
	    	</c:if>
	    	<c:if test="${item.cardItemOrder.payStatus =='5'}">
	    		已完成
	    	</c:if>
	    	<c:if test="${item.cardItemOrder.payStatus =='6'}">
	    		关闭
	    	</c:if>
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