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
	document.forms[0].action = "fieldAction.do?method=preAdd&fId="+id;
	document.forms[0].submit();
}

function openPre(id){
	if(confirm("确定去开奖？")){
		location = "fieldAction.do?method=preOpen&fId="+id;
	}
}
</script>
</head>
<body>
<div class="bread">竞猜管理  &raquo;比赛局玩法列表
</div>
<html:form action="/fieldAction.do?method=init">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		名称：<html:text property="field.title" name="fieldForm"/>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
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
	url="fieldAction.do" 
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
		<th>赛事名称</th>
		<th>比赛局名称</th>
		<th>玩法|开奖状态|开奖结果</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<c:if test="${not empty matchList}">
				<c:forEach var="match" items="${matchList}">
					<c:if test="${item.mId eq match.mId}">
						<c:out value="${match.title}" />
					</c:if>
				</c:forEach>
			</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="title"/>
	    </td>
	    <td align="center">
			<c:forEach items="${item.playList}" var="play">
				<div><label>|<c:out value="${play.title}" />|</label>&nbsp;&nbsp;&nbsp;<label><c:if test="${play.openStatus==0}">未开奖</c:if></label>&nbsp;&nbsp;&nbsp;<label><c:if test="${play.openStatus==1}">已开奖</c:if></label>&nbsp;&nbsp;&nbsp;<label><c:if test="${play.openStatus==1}">【<c:out value="${play.openResult}"/>】</c:if></label></div>
			</c:forEach>
	    </td>
	    
	    
		<td align="center">
<%-- 			<a class="link" href='javascript:void(0);' onclick="modify(<bean:write name="item" property="fId"/>);">详情</a> --%>
			<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
			 <a class="link" href='javascript:void(0);' onclick="openPre(<bean:write name="item" property="fId"/>);">开奖</a>
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