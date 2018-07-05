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
	document.forms[0].action = "gaAction.do?method=preAdd&action=modif&id="+id;
	document.forms[0].submit();
}
function changeStatus(id,status){
	$.post("gaAction.do?method=changeStatus&id="+ id , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				if(status=='1'){
					$('#notice'+id).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'0\');">开</a>');
				}else{
					$('#notice'+id).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'1\');">关</a>');
				}
			}
	});	
}
</script>
</head>
<body>
<div class="bread">彩票管理&raquo;开奖API网址</div>
</div>
<html:form action="/gaAction.do?method=initGameUrl">
<%--<table class="tblist" cellpadding="0" cellspacing="0" border="0">--%>
<%--	<tr class="moon">--%>
<%--		<td>			--%>
<%--					标题：<html:text property="title" name="noticeForm"/>--%>
<%--			<span>开始时间：<html:text property="startDate" name="noticeForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>--%>
<%--			<span>结束时间：<html:text property="endDate" name="noticeForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>--%>
<%--    		&nbsp;<input type="submit" value="查询" class="gbutton"/>--%>
<%--    		&nbsp;<a href="gaAction.do?method=preAdd" class="gbutton">新增</a>--%>
<%--		</td>--%>
<%--	</tr>--%>
<%--</table>--%>
</html:form>
<%
int number=1;
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
//String startDate=(String)request.getAttribute("startDate");
//String endDate=(String)request.getAttribute("endDate");
//String adType=(String)request.getAttribute("adType");
%>
<pg:pager 
	url="gaAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="initGameUrl"/>

	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>类型</th>
		<th>游戏名称</th>
		<th>开彩网API</th>
		<th>彩票控API</th>
		<th>当前使用</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	      <td align="center">
	     	 <c:if test = "${item.playCate eq '1'}">
		  		官方
		  	</c:if>
		    <c:if test = "${item.playCate eq '2'}">
		  		信用
		  	</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="gameTitle"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="kaicaiUrl" />
	    </td>
	     <td align="center">
	     	<bean:write name="item" property="caipiaoUrl" />
	    </td>
	    <td align="center">
		  	<c:if test = "${item.urlSwitch eq '1'}">
		  		开彩网
		  	</c:if>
		    <c:if test = "${item.urlSwitch eq '2'}">
		  		彩票控
		  	</c:if>
	  	</td>
		<td align="center">
			<a href='javascript:void(0);' onclick="modify(<bean:write name="item" property="infoId"/>);"><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
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