<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script type="text/javascript">
function changeStatus(paramId,status){
	$.post("paramAction.do?method=changeStatus&paramId="+ paramId , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#type'+paramId).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+paramId+',\'0\');">开</a>');
				}else{
					$('#type'+paramId).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+paramId+',\'1\');">关</a>');
				}
			}
	});	
}

function create(){
	document.forms[0].action = "paramAction.do?method=create";
	document.forms[0].submit();
}
function del(id){
	if(confirm('确实要删除吗？')){
		document.forms[0].action = "paramAction.do?method=del&id="+id;
		document.forms[0].submit();
	}
}
function modify(id){
		document.forms[0].action = "paramAction.do?method=preModify&action=modif&paramId="+id;
		document.forms[0].submit();
}
function sort(id,flag){;
	if(confirm("确认移动？"))
	{
	
		document.forms[0].action = "paramAction.do?method=sort&id="+id+"&flag="+flag;
		document.forms[0].submit();
	}


}
</script>
</head>
<body>
<div class="bread">系统管理  &raquo; 参数列表</div>
<html:form action="/paramAction.do?method=init">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			名称
      		<html:text property="title" name="paramForm" maxlength="20"/>
      		&nbsp;<input type="submit" value="查询" class="gbutton"/><!--
			&nbsp;<input type="button" class="gbutton" value="添加" onclick="create();"/>
		--></td>
	</tr>
</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
%>
<pg:pager 
	url="paramAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>名称</th>
		<th>最小充值</th>
		<th>最大充值</th>
		<th style="width: 400px;">取值</th>
		<th>最高赠送</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="title"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="minRecharge"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="maxRecharge"/>
	    </td>
	    <td align="center">
	    	<div style="width: 420px;">
		    	<bean:write name="item" property="value" />
	    	</div>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="maxPresent"/>
	    </td>
		<td align="center">
			<a class="edit" href="javascript:modify(<bean:write name="item" property="paramId"/>);"><img class="xtbImg ico-edit" src="../images/modify.png"/></a><!--
			<a class="edit" href="javascript:del(<bean:write name="item" property="paramId"/>);"><img class="xtbImg" src="../images/trash.gif"/></a>
		--></td>
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