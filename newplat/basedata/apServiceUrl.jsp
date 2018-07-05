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
		window.location.href='baseDataAction.do?method=delRSetMeal&optionId='+codeId;
	}
}
function release(codeId,stat){
	var codeId;
	if(stat=='1'){
		info = "您确定要打开吗？";
	}else{
		info = "您确定要关闭吗？";
	}
	if(confirm(info)){
		window.location.href='baseDataAction.do?method=releaseRechargeCode&codeId='+codeId+"&stat="+stat;
	}
}

</script>
</head>
<body>
<div class="bread">AP服务器地址
</div>
<html:form method="post" action="/baseDataAction.do?method=initIntegral" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
	
			<%-- <td>
			 <span>标题：<html:text property="optionTitle" name="baseDataForm"/></span>
				<input type="submit" class="gbutton" value="查询" />
				<input type="button" class="gbutton" value="添加" onclick="location='baseDataAction.do?method=preAddIntegral&action=add'"/>
			</td> --%>
	</tr>
</table>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String status=(String)request.getAttribute("status");
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
	<pg:param name="method" value="initIntegral"/>
	<pg:param name="status" value="<%=status%>"/>
	<pg:param name="cityId" value="<%=cityId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
	<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
			<th class="sel">序号</th>
			<th>标题</th>
			<th>AP服务地址URL</th>
	    	<th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
		<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
					<td class="numbers"><%=num.intValue()+1 %></td>
			 		<td><c:out value="${item.optionTitle}"/></td>
					<td><c:out value="${item.content}"/></td>
				   	<td>
				   		<a href='baseDataAction.do?method=preAddApUrl&action=modif&optionId=<bean:write name="item" property="optionId"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
				   		<%-- <a href='javascript:void(0);' onclick='del(<bean:write name="item" property="optionId"/>);'><img class="xtbImg ico-del" src="../images/trash.png"/></a> --%>
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