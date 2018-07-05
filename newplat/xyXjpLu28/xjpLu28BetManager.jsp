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
function detail(id){
	document.forms[0].action = "xjpLu28Action.do?method=betDetailManager&betDetailId="+id;
	document.forms[0].submit();
}
</script>
</head>
<body>
<html:form action="/xjpLu28Action.do?method=betManager">
<html:hidden property="startIndex" name="xjpLu28Form"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
    	期号:<html:text property="sessionNo"/>	
    	用户名/ID:<html:text property="userName"/>
    		&nbsp;<input type="submit" value="查询" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userName=(String)request.getAttribute("userName");
String sessionNo=(String)request.getAttribute("sessionNo");
%>
<pg:pager 
	url="xjpLu28Action.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="betManager"/>
	<pg:param name="userName" value="<%=userName%>"/>
	<pg:param name="sessionNo" value="<%=sessionNo%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th class="sel">序号</th>
		<th>期号</th>
		<th>ID</th>
		<c:if test = "${loginUser.userType ne '20'}">
		<th>用户名</th>
		</c:if>
		<th>投注选项</th>
		<th>投注名称</th>
		<th>投注时间</th>
		<th>投注金额</th>
		<th>开奖结果</th>
		<th>是否中奖</th>
		<th>中奖金额</th>
		<th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td class="numbers"><%=number++ %></td>
	    <td align="center">
	     	<bean:write name="item" property="gaBetDetail.sessionNo"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="user.userId"/>
	    </td>
	    <c:if test = "${loginUser.userType ne '20'}">
	    <td align="center">
	        <bean:write name="item" property="user.loginName"/>
	    </td>
	    </c:if>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.betName"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.optionTitle"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.betTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.betMoney"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetDetail.openResult"/>
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
			<bean:write name="item" property="gaBetDetail.winCash"/>
	    </td>
	    <td align="center">
	        <a href='javascript:void(0);' onclick="detail(<bean:write name="item" property="gaBetDetail.betDetailId"/>);"  ><img class="xtbImg" src="../images/view.png"/></a>
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