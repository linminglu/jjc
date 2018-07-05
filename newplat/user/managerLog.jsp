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
<%@ include file = "../common/inc_css.jsp"%>
<script language="javascript">
function del(){
	var frm = document.managerForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "please choose删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}
function selectAll(flag){
	var selAll = document.managerForm;
	for( var i=0;i<selAll.elements.length;i++ ){
		if( selAll.elements[i].name == 'selectIndex' ){
			selAll.elements[i].checked = flag;
		}
	}
}
</script>
</head>
<body>
<div class="bread">登陆日志  &raquo; 日志List</div>
<html:form action="/managerAction.do?method=log">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			&nbsp;用户名&nbsp;<html:text property="loginName" name="managerForm"/>&nbsp;&nbsp;
			<span>Start Date：<html:text property="startDate" name="managerForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'en'})" readonly="readonly"/></span>
			<span>End Date：<html:text property="endDate" name="managerForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'en'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="Search" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1; %>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String logType=(String)request.getAttribute("userLog.logType");
String loginName=(String)request.getAttribute("loginName");
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
	<pg:param name="method" value="log"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<html:form action="/managerAction.do?method=delete" onsubmit="return del();">
<bean:size id="length" name="list"/>
	<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th width="5%">NO.</th>
		<th>UserId</th>
		<th>mobile</th>
		<th>Status</th>
	    <th>时间</th>
	    <th>IP</th>
	</tr>
	<logic:iterate id="item" name="list">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
	    <td align="center"><%=number++ %></td>
	    <td align="center"><bean:write name="item" property="userId"/></td>
	    <td  align="center">
	     	<bean:write name="item" property="loginName"/>
	    </td>
	    <td  align="center">
	     	<bean:write name="item" property="actionText"/>
	    </td>
	    <td  align="center">
	     	<bean:write name="item" property="dateTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
		<td  align="center">
	      	<a target="_blank" href="http://www.ip138.com/ips8.asp?ip=<bean:write name="item" property="ipAddress"/>+&action=2"><bean:write name="item" property="ipAddress"/></a>
		</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">No data!</div>
</logic:empty>
</html:form>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

<iframe width=174 height=189 name="gToday:normal:agenda.js" id="gToday:normal:agenda.js" src="../js/ipopeng.htm" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;"></iframe>

</body>
</html:html>