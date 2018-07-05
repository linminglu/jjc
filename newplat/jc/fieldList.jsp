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

function del(id){
	if(confirm("删除比赛局可能会影响该比赛局相关的比赛，确定删除？")){
		location = "fieldAction.do?method=delete&fId="+id;
	}
}
</script>
</head>
<body>
<div class="bread">竞猜管理  &raquo;比赛局列表
</div>
<html:form action="/fieldAction.do?method=init">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
		<span>
			赛事： 
			<html:select property="field.mId" name="fieldForm" styleClass="sele select" style="width:174px;">;		
				<html:option value="">全部</html:option>			
				<logic:notEmpty name="matchList">
      				<html:options collection="matchList"  property="mId" labelProperty="title"/>
	      		</logic:notEmpty>
			</html:select>
			</span>
		比赛局名称：<html:text property="field.title" name="fieldForm"/>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
    		&nbsp;<a href="fieldSetting.jsp" class="gbutton">新增</a>
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
		<th>状态</th>
		<th>开始投注时间</th>
		<th>结束时间</th>
		<th>玩法</th>
		<th>投注项</th>
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
	     	<c:if test="${item.status==1}">有效</c:if>
	     	<c:if test="${item.status==0}">无效</c:if>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="startDate" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	     	<bean:write name="item" property="endDate" format="yyyy-MM-dd HH:mm"/>
	    </td>
	    <td align="center">
	     	
	    </td>
	    <td align="center">
	     	
	    </td>
	    
		<td align="center">
			<a class="link" href='javascript:void(0);' onclick="modify(<bean:write name="item" property="fId"/>);">编辑</a>
			<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
			 /<a class="link" href='javascript:void(0);' onclick="del(<bean:write name="item" property="fId"/>);">删除</a>
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