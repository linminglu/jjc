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
	document.forms[0].action = "jcTeamAction.do?method=preAdd&Id="+id;
	document.forms[0].submit();
}

function del(id){
	if(confirm("删除战队可能会影响该战队相关的比赛，确定删除？")){
		location = "jcTeamAction.do?method=delete&Id="+id;
	}
}
</script>
</head>
<body>
<div class="bread">竞猜管理  &raquo;队伍列表
</div>
<html:form action="/jcTeamAction.do?method=init">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		队伍名称：<html:text property="jcTeam.title" name="jcTeamForm"/>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
    		&nbsp;<a href="jcTeamAction.do?method=preAdd" class="gbutton">新增</a>
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
	url="jcTeamAction.do" 
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
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>队伍名称</th>
		<th>图片</th>
		<th>一级</th>
		<th>二级</th>
		<th>其他说明</th>
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
	     	<c:if test="${not empty type1List}">
				<c:forEach var="type" items="${type1List}">
					<c:if test="${item.tId1 eq type.tid}">
						<c:out value="${type.title}" />
					</c:if>
				</c:forEach>
			</c:if>
	    </td>
	    <td align="center">
	     	<c:if test="${not empty type2List}">
				<c:forEach var="type" items="${type2List}">
					<c:if test="${item.tId2 eq type.tid}">
						<c:out value="${type.title}" />
					</c:if>
				</c:forEach>
			</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="remarks"/>
	    </td>
		<td align="center">
			<a class="link" href='javascript:void(0);' onclick="modify(<bean:write name="item" property="id"/>);">编辑</a>
			<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
			 /<a class="link" href='javascript:void(0);' onclick="del(<bean:write name="item" property="id"/>);">删除</a>
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