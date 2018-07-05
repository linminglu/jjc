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

</head>
<body>
<div class="bread">快三信息管理  &raquo; 投注清单</div>
<html:form action="/gaK3Action.do?method=initBetList" >
<html:hidden property="startIndex" name="gaK3Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon" id="lname">
		<td>
			用户名:
			<%-- <html:select property="userId" name="gaK3Form" onchange="changeSchedule(this.value);">
				<html:option value="">所有用户</html:option>
	      	<logic:notEmpty name="userList">
      			<html:options  collection="userList"  property="userId" labelProperty="loginName" />
	      	</logic:notEmpty>
      		</html:select> --%>
      		<html:text property="loginName" name="gaK3Form"/>
			<span>开始时间：<html:text property="startTime" name="gaK3Form" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endTime" name="gaK3Form" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
		
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startTime=(String)request.getAttribute("startTime");
String endTime=(String)request.getAttribute("endTime");
String userId=(String)request.getAttribute("userId");
String loginName=(String)request.getAttribute("loginName");
%>
<pg:pager 
	url="gaK3Action.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initBetList"/>
	<pg:param name="startTime" value="<%=startTime%>"/>
	<pg:param name="endTime" value="<%=endTime%>"/>
	<pg:param name="userId" value="<%=userId%>"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>投注期号</th>
		<th>投注数量</th>
		<th>投注钱数</th>
		<th>会员实收</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<a href ="javascript:void(0);" onclick="betDetail(<bean:write name="item" property="sessionId"/>)" ><bean:write name="item" property="sessionNo"/></a>
	    <td align="center">
	     	<logic:empty name="item" property="userCount">
	    		0
		    </logic:empty>
		    <logic:notEmpty name="item" property="userCount">
		     	<bean:write name="item" property="userCount"/>
		    </logic:notEmpty>
	    </td>
	    <td align="center">
	     	<logic:empty name="item" property="pointCount">
		    	0
		    </logic:empty>
		    <logic:notEmpty name="item" property="pointCount">
		     	<bean:write name="item" property="pointCount"/>
		    </logic:notEmpty>
	    </td>
	    <td align="center">
	     	<logic:empty name="item" property="sessionCash">
		    	0
		    </logic:empty>
		    <logic:notEmpty name="item" property="sessionCash">
		     	<bean:write name="item" property="sessionCash"/>
		    </logic:notEmpty>
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
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
var frm = document.gaK3Form;
function formSubmit(){
	if(frm["userId"].value == ""){
		frm["userId"].focus();
	}
	if(frm["loginName"].value == ""){
		frm["loginName"].focus();
	}
}

function betDetail(sessionId){
		window.location.href='gaK3Action.do?method=betUserList&sessionId='+sessionId;
	/* var loginName = frm["loginName"].value;
	if(loginName == "" || loginName == null){
	}else{
		window.location.href='gaK3Action.do?method=betDetail&sid='+sessionId;
	}  */
}
/*
var uid;
function changeSchedule(userId){
	uid=userId;
}
function betDetail(sessionId){

	if(uid =="" || uid == null && frm["userId2"].value == "" || frm["userId2"].value == null){
		alert("请选择用户");
		return false;
	}
	if(frm["userId2"].value != "" && frm["userId2"].value != null){
		uid = frm["userId2"].value;
	}
	window.location.href='gaK3Action.do?method=betDetail&sid='+sessionId+"&id="+uid;
} */

</script>
</body>
</html:html>