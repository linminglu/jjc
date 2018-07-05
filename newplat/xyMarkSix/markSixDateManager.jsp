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
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script type="text/javascript">
function save(id){
	var startTime= $("#start" + id).val();
	var endTime= $("#end" + id).val();
	if((startTime!=null&&startTime!=""&&startTime.trim()!="")||(endTime!=null&&endTime!=""&&endTime.trim()!="")){
		 if(confirm('确实要修改开始时间为：' + startTime+'结束时间为：' + endTime)){
				$.post("markSixAction.do?method=modifyDate&sessionId="+id+"&startTime="+startTime+"&endTime="+endTime, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								location.reload();
								alert("修改成功！");	
							}else{
								alert("修改失败！");
							}
				});
		}
	}else{
		alert("请输入开始结束时间！");
	}
}
</script>
</head>
<body>
<div class="bread">彩票管理&raquo;修改时间</div>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
%>
<pg:pager 
	url="markSixAction.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="dateManager"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>期号</th>
		<th>开始时间</th>
		<th>结束时间</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="sessionNo"/>
	    </td>
	     <td align="center">
			<bean:write name="item" property="startTime" format="yyyy-MM-dd HH:mm:ss"/>
			<p>修改为：</p>
			<input  id="start<bean:write name="item" property="sessionId"/>" name="endTime"  onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="endTime" format="yyyy-MM-dd HH:mm:ss"/>
	        <p>修改为：</p>
			<input  id="end<bean:write name="item" property="sessionId"/>" name="endTime"  onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
	    </td>
		<td align="center">
			<c:if test="${item.openStatus !='2'}">
				<a href="javascript:save(<bean:write name="item" property="sessionId"/>);">保存</a>
			</c:if>
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