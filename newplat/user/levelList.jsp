<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function delLevel(id){
	var t = confirm("确认删除？");
	if(t==true){
		window.location = "managerAction.do?method=userLevelDel&id="+id;
	}
}
</script>
</head>
<body>
<div class="bread">等级管理
</div>
<html:form action="/managerAction.do?method=userLevelList" method="post">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			等级名称
			<html:text property="agentName" name="managerForm"/>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=userLevelPreAdd';"/>
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
%>
<pg:pager 
	url="managerAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="levelList"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="levelList"/>
<logic:notEmpty name="levelList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>等级</th>
		<th>名称</th>
	    <th>成长积分</th>
	    <th>跳级奖励</th>	   	    
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="levelList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="level" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="levelName" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="point" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="award" />
	    </td>
	    <td align="center">
	        <a class="link" href="managerAction.do?method=userLevelPreAdd&id=<bean:write name="item" property="id" />">修改</a>
	        &nbsp;&nbsp;
	        <a class="link" onclick="delLevel(<bean:write name="item" property="id" />)" href="javascript:void(0)">删除</a>
	    </td>
	    
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="levelList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>