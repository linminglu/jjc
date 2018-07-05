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
<link href="../styleset/bxsplat.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../styleset/bxsSupport.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../css/manager.css" rel="stylesheet" type="text/css" media="all"/>
<script type="text/javascript" src="../js/moocore145.js"></script>
<script type="text/javascript" src="../js/moomore145.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<logic:present name="msg">
<script language="javascript">
alert("恭喜您，推送成功!");


</script>
</logic:present>
</head>
<body>
<div class="bread">系统管理 &raquo; 推送管理 
</div>
<html:form method="post" action="/baseDataAction.do?method=pushInit" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
			<td>
			设备类型
			<html:select property="deviceType" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">IOS+Android</html:option>
				<html:option value="2">Android</html:option>
				<html:option value="3">IOS</html:option>
			</html:select>
			消息类型
			<html:select property="type" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">普通消息</html:option>
				<html:option value="2">网页</html:option>
				<html:option value="3">商家主页</html:option>
			</html:select>
			 <span>标题：<html:text property="title" name="baseDataForm"/></span>
			 <span>开始时间：<html:text property="startDate" name="baseDataForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			 <span>结束时间：<html:text property="endDate" name="baseDataForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			  <input type="submit" class="gbutton" value="查询" />
			  <a class="gbutton" href="javascript:openWindowT('baseDataAction.do?method=prePush', 700, 440, '推送信息');" >推送</a>
			</td>
			</tr>
</table>
<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String title=(String)request.getAttribute("title");
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
	<pg:param name="method" value="pushInit"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<pg:param name="title" value="<%=title%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
			<th>标题</th>
			<th>信息类型</th>
			<th>设备类型</th>
			<th>内容</th> 
			<th>时间</th> 
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	 		<td align="center">
	 			 <logic:equal name="item" property="type" value="1">
	 			    <bean:write name="item" property="title"/>
	 			 </logic:equal>
	 			 <logic:equal name="item" property="type" value="2">
	 			 	 <bean:write name="item" property="title"/>
	 			 </logic:equal>
	 			 <logic:equal name="item" property="type" value="3">
	 			   <a href='baseDataAction.do?method=view&id=<bean:write name="item" property="pushId"/>'> <bean:write name="item" property="title"/></a>
	 			 </logic:equal>
	 		  
	 	    </td>
	 		<td>
	 			 <logic:equal name="item" property="type" value="1">
	 			 	普通消息
	 			 </logic:equal>
	 			 <logic:equal name="item" property="type" value="2">
	 			 <font color="green"><strong>网页</strong></font>&nbsp;
				  <a href="<bean:write name="item" property="url"/>" target="_blank">打开</a>
	 			 </logic:equal>
	 			 <logic:equal name="item" property="type" value="3">
	 			 	商家主页
	 			 </logic:equal>
	 		</td>
	 		<td>
	 			 <logic:equal name="item" property="deviceType" value="1">
	 			 	IOS+Android
	 			 </logic:equal>
	 			 <logic:equal name="item" property="deviceType" value="2">
	 			 	Android
	 			 </logic:equal>
	 			 <logic:equal name="item" property="deviceType" value="3">
	 			 	IOS
	 			 </logic:equal>
	 		</td>
	 		<td>
	 			<bean:write name="item" property="content" />
	 		</td>
	 		<td>
	 			<bean:write name="item" property="createDate" format="yyyy-MM-dd HH:mm"/>
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