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
function del(){
	var frm = document.newsForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "请选择删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}

function delUser(url,loginName){
	if(loginName && (loginName=="admin" || loginName=="cuisy" || loginName=="toefladmin" || loginName=="genadmin" 
		 	|| loginName=="testadmin" || loginName=="examadmin"
			|| loginName=="satadmin" || loginName=="ieltsadmin" || loginName=="greadmin" || loginName=="gmatadmin")){
		alert("不能删除此管理账号！");
		return false;
	}
	if(confirm("确认删除用户?")){
		location.href = url;
	}
	
}

function delItem(id,type){
	if(confirm('确实要删除吗？')){
		$.post("newsAction.do?method=delUser&uid="+id+"&userType="+type,
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
				}else{
					alert("删除失败！\n\n提示：数据已应用则不能删除！");
				}
		});	
	}
}
</script>
</head>
<body>
<div class="bread">广告/通知管理&raquo;咨询列表</div>
<html:form action="/newsAction.do?method=initNews" method="post">

<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			新闻名称
			<html:text property="title" name="newsForm"/>
			类型
	        <span>
	      	<html:select property="tid" name="newsForm" >
	      			<logic:notEmpty name="typeList">
	      				<html:option value="">请选择</html:option>
	      				<html:options property="tid" collection="typeList" labelProperty="title"/>
	      			</logic:notEmpty>
	      	</html:select>
			</span>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='newsAction.do?method=preAddNews&action=add';"/>
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
%>
<pg:pager 
	url="newsAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initNews"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<form class="layui-form" lay-filter="reform">
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th width="100">前缀类型</th>
		<th>标题</th>
		<th>所属类别</th>
		<th width="60">状态</th>
	    <th>作者</th>
	    <th width="120">发布时间</th>
	    <th width="40">操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
		   <td align="center">
	        <bean:write name="item" property="newsInfo.type"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="newsInfo.title" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="typeTitle"/>
	    </td>
	    <td align="center">
	    	<c:if test="${item.newsInfo.status == '1'}">
				<input type="checkbox" checked name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeStatus" value="1" data="<bean:write name="item" property="newsInfo.nid"/>" id="news<bean:write name="item" property="newsInfo.nid"/>"/>
			</c:if>
			<c:if test="${item.newsInfo.status == '0'}">
				<input type="checkbox" name="switch" lay-skin="switch" lay-text="开|关" lay-filter="changeStatus" value="0" data="<bean:write name="item" property="newsInfo.nid"/>" id="news<bean:write name="item" property="newsInfo.nid"/>"/>
			</c:if>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="newsInfo.author"/>
	   </td>
	   <td align="center">
	      	<bean:write name="item" property="newsInfo.submitTime" format="yyyy-MM-dd HH:mm"/>
	   </td>
	   <td align="center">
	   		<a href='newsAction.do?method=preAddNews&action=modify&id=<bean:write name="item" property="newsInfo.nid"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   </td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="list">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
</form>
<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>
<script type="text/javascript">
layui.use('element', function(){
});
layui.use('form', function(){
	var form = layui.form;
	form.on('switch(changeStatus)', function(data){
		var loadObj = layer.load();
		var obj=data.elem;
		var id=$(obj).attr('data');
		var checked=data.elem.checked;		
		var val='0';
		if(checked){
			val='1';
		}
		$.post("newsAction.do?method=changeStatus", 
			{'id':id},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.close(loadObj);
					$('#news'+id).val(val);
				}else{
					layer.close(loadObj);
					//失败的话
					if(val=='1'){
						$('#news'+id).val('0');
						$('#news'+id).removeAttr('checked');
					}else{
						$('#news'+id).val('1');
						$('#news'+id).attr('checked',true);
					}
					form.render('checkbox');
					layer.alert(data.msg);
				}
		}, "json");
	});  
});
</script>
</body>
</html>