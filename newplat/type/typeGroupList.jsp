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
function changeStatus(tid,status){
	$.post("typeAction.do?method=changeStatus&tid="+ tid , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#type'+tid).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+tid+',\'0\');">开</a>');
				}else{
					$('#type'+tid).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+tid+',\'1\');">关</a>');
				}
			}
	});	
}

function delItem(id){
	if(confirm('确实要删除吗？')){
		$.post("typeAction.do?method=delType&tid="+id, 
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
<div class="bread">类型管理  &raquo;二级类型列表</div>
<html:form action="/typeAction.do?method=group">
<html:hidden property="column"  name="typeForm" />
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
 
	<tr class="moon">
		<td>
		<c:if test="${infoType==1 }">
			类型
			<html:select property="tid" name="typeForm">
				<html:option value="">请选择</html:option>
	      	<logic:notEmpty name="groupList">
      			<html:options  collection="groupList"  property="tid" labelProperty="title"/>
	      	</logic:notEmpty>
      		</html:select>
      		&nbsp;<input type="submit" value="查询" class="gbutton"/>
      	</c:if>
		<c:if test="${infoType==2 }">
			团购分类
			<html:select property="tid" name="typeForm">
				<html:option value="">请选择</html:option>
	      	<logic:notEmpty name="groupList">
      			<html:options  collection="groupList"  property="tid" labelProperty="title"/>
	      	</logic:notEmpty>
      		</html:select>
      		&nbsp;<input type="submit" value="查询" class="gbutton"/>
      		</c:if>
      		<c:if test="${infoType==1 }">
      		 &nbsp;<a href="typeAction.do?method=create&infoType=1"  class="gbutton">新增</a>
      		 </c:if>
      		<c:if test="${infoType==2 }">
      		 &nbsp;<a href="typeAction.do?method=create&infoType=2"  class="gbutton">新增</a>
      		 </c:if>
		</td>
	</tr>


</table>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String infoType =request.getAttribute("infoType")!=null?(String)request.getAttribute("infoType"):"1";
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
	<pg:param name="method" value="group"/>
	<pg:param name="infoType" value="<%=infoType%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>标题</th>
		<c:if test="${infoType==1 }"><th>类型</th></c:if>
		<c:if test="${infoType==1 }"><th>图片</th></c:if>
		<th>排序</th>
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
	    <c:if test="${infoType==1 }">
		    <td align="center">
		     	<bean:write name="item" property="ptitle"/>
		    </td>
	    </c:if>
	    <c:if test="${infoType==1 }">
		    <td align="center">
		    	<img src="..<bean:write name="item" property="img"/>" class="imgw45">
		    </td>
	    </c:if>
	   <td align="center">
	    	（<bean:write name="item" property="sort"/>）
	    	 <c:if test="${infoType==2 }">
	    	<a href="javascript:if(confirm('确实要向上吗？')){window.location='typeAction.do?method=groupSotr&tid=<bean:write name="item" property="tid"/>&flag=1&infoType=2'}" class="button">上</a>
	    	<a href="javascript:if(confirm('确实要向下吗？')){window.location='typeAction.do?method=groupSotr&tid=<bean:write name="item" property="tid"/>&flag=0&infoType=2'}" class="button">下</a>
	    	</c:if>
	    	<c:if test="${infoType==1 }">
	    	<a href="javascript:if(confirm('确实要向上吗？')){window.location='typeAction.do?method=groupSotr&tid=<bean:write name="item" property="tid"/>&flag=1&infoType=1'}" class="button">上</a>
	    	<a href="javascript:if(confirm('确实要向下吗？')){window.location='typeAction.do?method=groupSotr&tid=<bean:write name="item" property="tid"/>&flag=0&infoType=1'}" class="button">下</a>
	    	</c:if>
	    </td>
	    <td align="center" id="type<bean:write name="item" property="tid"/>">
			 <logic:equal name="item" property="status" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="tid"/>,'1');" href='javascript:void(0);'>关</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="status" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;
				<a class="button" onclick="changeStatus(<bean:write name="item" property="tid"/>,'0');" href="javascript:void(0);">开</a>&nbsp;
			</logic:equal>	    
	    </td>
	    
		<td align="center">
			<c:if test="${infoType==1 }">
			<a href='typeAction.do?method=preModify&action=modif&infoType=1&tid=<bean:write name="item" property="tid"/>'  ><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
			</c:if>
			<c:if test="${infoType==2 }">
			<a href='typeAction.do?method=preModify&action=modif&infoType=2&tid=<bean:write name="item" property="tid"/>'  ><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
			</c:if>
			<%if(Constants.chkFuncConfig("system.func.type.manage")){ %>
			<a href="javascript:delItem(<bean:write name="item" property="tid"/>);"><img class="xtbImg ico-del" src="../images/trash.png"/></a>
			<%} %>
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