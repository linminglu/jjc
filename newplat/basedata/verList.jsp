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
<script type="text/javascript">
/**
 * 修改
 */
function modify(id){
	document.forms[0].action = "baseDataAction.do?method=preModifyVer&id="+id;
	document.forms[0].submit();
}

function sort(id){
	
}

function changeStatus(id,status){
	$.post("baseDataAction.do?method=setVer&id="+ id+"&status="+status , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#ver'+id).html('<font color="red"><strong>否</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'0\');">是</a>');
				}else{
					$('#ver'+id).html('<font color="green"><strong>是</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'1\');">否</a>');
				}
			}
	});	
}
</script>
</head>
<body>
<div class="bread">系统管理  &raquo; 版本管理
</div>
<html:form action="/baseDataAction.do?method=verInit">
<html:hidden property="startIndex" name="baseDataForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			设备类型
			<html:select property="type" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">IOS</html:option>
				<html:option value="2">Android</html:option>
			</html:select>
			<span>开始时间：<html:text property="startDate" name="baseDataForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="baseDataForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
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
	<pg:param name="method" value="verInit"/>
	<pg:param name="startDate" value="<%=startDate%>"/>
	<pg:param name="endDate" value="<%=endDate%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>版本</th>
		<th>APP版本</th>
		<th>设备类型</th>
		<th>当前版本</th>
		<th>创建时间</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="ver"/>
	    </td>
	    <td align="center">
	     	 <logic:equal name="item" property="appType" value="1">
	     	 	<strong class="color-3bb07f">用户版</strong>	
			</logic:equal>
	     	 <logic:equal name="item" property="appType" value="2">
	     	 	<strong class="color-c34b77">配送版</strong>	
			</logic:equal>
	    </td>
	    <td align="center">
	     	 <logic:equal name="item" property="type" value="1">
	     	 	<strong class="color-3bb07f">IOS</strong>	
			</logic:equal>
	     	 <logic:equal name="item" property="type" value="2">
	     	 	<strong class="color-c34b77">Android</strong>	
			</logic:equal>
	    </td>
	    <td align="center" id="ver<bean:write name="item" property="id"/>">
			 <logic:equal name="item" property="isDef" value="1">
				<font color="green"><strong>是</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="id"/>,'1');" href='javascript:void(0);'>否</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="isDef" value="0">
				<font color="red"><strong>否</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="id"/>,'0');" href="javascript:void(0);">是</a>&nbsp;
			</logic:equal>	    
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="createDate" format="yyyy-MM-dd HH:mm"/>
	    </td>
		<td align="center">
			<a href='javascript:void(0);' onclick="modify(<bean:write name="item" property="id"/>);"  ><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
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
</body>
</html:html>