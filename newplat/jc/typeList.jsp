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
function modify(id){
	document.forms[0].action = "typeAction.do?method=preAdd&tid="+id;
	document.forms[0].submit();
}

function del(id){
	if(confirm("删除分类可能会影响该分类相关的队伍或赛事，确定删除？")){
		location = "typeAction.do?method=delete&tid="+id;
	}
}
</script>
</head>
<body>
<div class="bread">竞猜管理  &raquo;分类列表
</div>
<html:form action="/typeAction.do?method=init">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		标题：<html:text property="type.title" name="typeForm"/>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
    		&nbsp;<a href="typeAction.do?method=preAdd" class="gbutton">新增</a>
		</td>
	</tr>
</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("totalCount")!=null?(String)request.getAttribute("totalCount"):"0";
String title = (String)request.getAttribute("title");
%>
<pg:pager 
	url="typeAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="init"/>
	<pg:param name="title" value="<%=title%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<form class="layui-form" action="#" lay-filter="reform">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>分类名称</th>
		<th>图片</th>
		<th>等级</th>
		<th>状态</th>
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
	     	<img src="..<bean:write name="item" property="img"/>"  width="50" height="50">
	    </td>
	    <td align="center">
	    	<c:if test="${item.type==1}">一级</c:if>
	     	<c:if test="${item.type==2}">二级</c:if>
	    </td>
	    <td align="center">
	     	<c:if test="${item.status == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="1" data="<bean:write name="item" property="tid"/>" id="status<bean:write name="item" property="tid"/>"/>
			</c:if>
			<c:if test="${item.status == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="statusSwitch" value="0" data="<bean:write name="item" property="tid"/>" id="status<bean:write name="item" property="tid"/>"/>
			</c:if>
	    </td>
		<td align="center">
			<a class="link" href='javascript:void(0);' onclick="modify(<bean:write name="item" property="tid"/>);">编辑</a>
			<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
			 /<a class="link" href='javascript:void(0);' onclick="del(<bean:write name="item" property="tid"/>);">删除</a>
			</c:if>
		</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</form>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="/template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	
	form.on('switch(statusSwitch)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("typeAction.do?method=changeStatus", 
			{'tid':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.close(loadObj);
					$('#status'+id).val(val);
				}else{
					layer.close(loadObj);
					//失败的话
					if(val=='1'){
						$('#status'+id).val('0');
						$('#status'+id).removeAttr('checked');
					}else{
						$('#status'+id).val('1');
						$('#status'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</body>
</html:html>