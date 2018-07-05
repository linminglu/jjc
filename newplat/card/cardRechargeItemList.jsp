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
		window.location.href='cardAction.do?method=delCardRechargeItem&itemId='+codeId;
	}
}


</script>
</head>
<body>
<div class="bread">充值/提现管理 &raquo;充值套餐</div>
<html:form method="post" action="/cardAction.do?method=initCardRechargeItem" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
	
			<td>
			 <span>充值套餐：<html:text property="title" name="cardForm"/></span>
				<input type="submit" class="gbutton" value="查询" />
				<input type="button" class="gbutton" value="添加" onclick="location='cardAction.do?method=preAddCardRechargeItem&action=add'"/>
			</td>
	</tr>
</table>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String status=(String)request.getAttribute("status");
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
	<pg:param name="method" value="initCardRechargeItem"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="cardList"/>
	<logic:notEmpty name="cardList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
			<th>标题</th>
			<!-- <th>状态</th> -->
			<th>积分</th>
			<th>售价</th>
	    	<th>操作</th>
	</tr>
	<logic:iterate id="item" name="cardList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
 		<td><c:out value="${item.title}"/></td>
 		<%-- <td>
 			<logic:equal name="item" property="rechargeCode.status" value="1">
	    		[<span class="span-open"><strong>已激活</strong></span>]
    			<a class="button" href='javascript:void(0);' onclick='release(<bean:write name="item" property="rechargeCode.codeId"/>,0);' title="点击关" style="width:35px;">关</a>
	    	</logic:equal>
	    	<logic:equal name="item" property="rechargeCode.status" value="0">
	    		[<span class="span-close"><strong>未激活</strong></span>]
	    		<a  class="button" href='javascript:void(0);' onclick='release(<bean:write name="item" property="rechargeCode.codeId"/>,1);' title="点击开" style="width:35px;">开</a>
	    	</logic:equal>
		</td> --%>
		<td><strong><c:out value="${item.parValue}"/></strong></td>
		<td><strong><c:out value="${item.price}"/>元</strong></td>
	   	<td>
	   		<a href='cardAction.do?method=preAddCardRechargeItem&action=modif&itemId=<bean:write name="item" property="itemId"/>'>
	   			<img class="xtbImg ico-edit" src="../images/modify.png"/>
	   		</a>
	   		<a href='javascript:void(0);' onclick='del(<bean:write name="item" property="itemId"/>);'>
	   			<img class="xtbImg ico-del" src="../images/trash.png"/>
	   		</a>
	   	</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="cardList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
</html:form>
</body>
</html>