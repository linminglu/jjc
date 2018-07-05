<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>远程网络教学平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script src="../js/manager.js?t=3" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	managerObj.drawingResult();
});
</script>
</head>
<body>
<div class="bread">用户管理  &raquo;投注管理
</div>
<html:form action="/managerAction.do?method=betList" method="post">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户名/ID：<html:text property="loginName" name="managerForm"/>
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String loginName =request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
%>
<pg:pager 
	url="managerAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="betList"/>
	<pg:param name="loginName" value="<%=loginName%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>ID</th>
		<th>用户名</th>
		<th>彩种</th>
	    <th>期号</th>
	    <th>投注时间</th>
	    <th>玩法</th>
	    <th>投注项</th>
	    <th>开奖结果</th>
	    <th>投注值</th>
	    <th>赔率</th>
	    <th>中奖</th>
	    <th>亏盈</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="user.userId" />
	    </td>
	    <td align="center">
	        <bean:write name="item" property="user.loginName" />
	    </td>
	    <td align="center">
		    <bean:write name="item" property="gaBetDetail.gameName"/>
		</td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetDetail.sessionNo"/>
	    </td>
	    <td align="center">
	      	<bean:write name="item" property="gaBetDetail.betTime" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetDetail.playName"/>
	    </td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetDetail.betName"/>&nbsp;
	    	<bean:write name="item" property="gaBetDetail.optionTitle"/>
	    </td>
	    <td align="center">
	    	<input class="result" type="hidden" value="<bean:write name="item" property="gaBetDetail.openResult"/>" data="<bean:write name="item" property="gaBetDetail.gameType"/>"/>
	    </td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetDetail.betMoney"/>
	    </td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetDetail.betRate"/>
	    </td>
	    <td align="center">
	        <logic:equal name="item" property="gaBetDetail.betFlag" value="0">
     			<strong><font color ="purple">撤单</font></strong> 
			</logic:equal>
            <logic:equal name="item" property="gaBetDetail.betFlag" value="1">
     		<c:if test="${item.gaBetDetail.winResult =='0'}">
		    	<strong><font color ="blue">未开奖</font></strong> 
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='1'}">
		    	<strong><font color ="red">中奖</font></strong> 
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='2'}">
		    	<strong><font color ="green">未中奖</font></strong>
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='3'}">
		    	<strong><font color ="purple">打和</font></strong> 
		    </c:if>
			</logic:equal>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.winCash" format="#.####"/>
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
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>