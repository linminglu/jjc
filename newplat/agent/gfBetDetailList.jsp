<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>海豚彩票</title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script type="text/javascript">
function detail(id){
	document.forms[0].action = "agentAction.do?method=betDetailManager&betDetailId="+id;
	document.forms[0].submit();
}

</script>
</head>
<body>
<html:form action="/agentAction.do?method=gfBetDetailList">
<input type="hidden" value="<c:out value='${userId}'/>"  name="userId"/>
<html:hidden property="startIndex" name="agentForm"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>订单号：<html:text property="orderNum" name="agentForm"/></span>
			<span>开始时间：<html:text property="startDate" name="agentForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn',dateFmt: 'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/></span>
			<span>结束时间：<html:text property="endDate" name="agentForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn',dateFmt: 'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/></span>
			<span>
			状态： 
		    <html:select property="winResult" name="agentForm" styleClass="sele select">
			    <html:option value="">全部</html:option>
			    <html:option value="2">未开奖</html:option>
			    <html:option value="5">未中奖</html:option>
			    <html:option value="4">中奖</html:option>
			</html:select>
			</span>
		</td>
	</tr>
	<tr class="moon">
		<td>
			<span>用户名/ID：<html:text property="loginName" name="agentForm"/></span>
			<span>
			彩票类型： 
			<html:select property="gameType" name="agentForm" styleClass="sele select" style="width:174px;">;		
				<html:option value="">全部</html:option>			
				<logic:notEmpty name="gaGfList">
      				<html:options collection="gaGfList"  property="gameType" labelProperty="gameTitle"/>
	      		</logic:notEmpty>
			</html:select>
			</span>
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>
<%int number=1;	
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String orderNum=(String)request.getAttribute("orderNum");
String startDate=(String)request.getAttribute("startDate");
String endDate=(String)request.getAttribute("endDate");
String loginName=request.getAttribute("loginName")!=null?(String)request.getAttribute("loginName"):"";
String winResult=(String)request.getAttribute("winResult");
String gameType=(String)request.getAttribute("gameType");
%>
<pg:pager 
	url="agentAction.do"
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="gfBetDetailList"/>
	<pg:param name="loginName" value="<%=loginName %>"/>
	<pg:param name="winResult" value="<%=winResult %>"/>
	<pg:param name="orderNum" value="<%=orderNum %>"/>
	<pg:param name="startDate" value="<%=startDate %>"/>
	<pg:param name="endDate" value="<%=endDate %>"/>
	<pg:param name="gameType" value="<%=gameType %>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>

<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
        <th>序号</th>
		<th>用户ID</th>
		<th>购买人</th>
		<!-- <th>发起人</th> -->
		<th>彩种</th>
		<th>期号</th>
		<th>开奖结果</th>
		<th width="25%">投注项</th>
		<th>玩法</th>
		<!-- <th>购买份数</th> -->
		<!-- <th>购买金额</th> -->
		<th>中奖金额</th>
		<th>注/总金额</th>
		<th>订单倍数</th>
		<!-- <th>订单总份数</th> -->
		<th>时间</th>
		<th>状态</th>
		<!-- <th>购买类型</th> -->
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
		<%-- <td><%=number++ %></td> --%>
	    <td align="center">
	        <bean:write name="item" property="gaBetSponsor.jointId"/>
	    </td>
	    <td align="center">
	    	<bean:write name="item" property="gaBetSponsor.userId"/>
		<%-- <a class="link" href="agentAction.do?method=gfBetDetail&rid=<c:out value='${item.gaBetPart.rid}'/>&jointId=<c:out value='${item.gaBetSponsor.jointId}'/>" /> --%>
		<%-- <bean:write name="item" property="gaBetSponsor.orderNum"/> --%>
		<!-- </a> -->
	    </td>
	    <td align="center">
	        <bean:write name="item" property="userName"/>
	    </td>
		<!-- <td align="center"> -->
		<%-- <bean:write name="item" property="sponsorName"/> --%>
		<!-- </td> -->
	    <td align="center">
	        <bean:write name="item" property="gaBetSponsor.gameName"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetSponsor.sessionNo"/>
	    </td>
	    <td align="center" style="word-break: break-all;">
	        <bean:write name="item" property="gaBetSponsor.openResult"/>
	    </td>
	    <td style="word-break:break-all;" align="center">
	        <bean:write name="item" property="betOption"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="title"/>
	    </td>
		<!-- <td align="center"> -->
		<%-- <bean:write name="item" property="gaBetPart.buyNum"/> --%>
		<!-- 	    </td> -->
		<!-- 	    <td align="center"> -->
		<%-- 	        <bean:write name="item" property="gaBetPart.betMoney"/> --%>
		<!-- 	    </td> -->
		<td align="center">
	        <bean:write name="item" property="winMoney"/>
	    </td>
	    <td align="center">
	    	<bean:write name="item" property="betMoney"/>/<bean:write name="item" property="gaBetSponsor.money"  format="#"/>
	    </td>
	    <td align="center">
	        <bean:write name="item" property="gaBetSponsor.multiple"/>
	    </td>
<!-- 	    <td align="center"> -->
<%-- 	        <bean:write name="item" property="gaBetSponsor.num"/> --%>
<!-- 	    </td> -->
	    <td align="center">
	        <bean:write name="item" property="gaBetPart.buyTime" format="yyyy-MM-dd HH:mm:ss"/>
	    </td>
	    <td align="center">
     		<c:if test="${item.winResult =='0'}">
		    	<strong><font color ="purple">未完成</font></strong> 
		    </c:if>
	        <c:if test="${item.winResult =='1'}">
		    	<strong>无效</strong> 
		    </c:if>
	        <c:if test="${item.winResult =='2'}">
		    	<strong><font color ="blue">未开奖</font></strong>
		    </c:if>
	        <c:if test="${item.winResult =='4'}">
		    	<strong><font color ="red">中奖</font></strong> 
		    </c:if>
	        <c:if test="${item.winResult =='5'}">
		    	<strong><font color ="green">未中奖</font></strong> 
		    </c:if>
	    </td>
	    <%-- <td align="center">
	        <logic:equal name="item" property="gaBetSponsor.betType" value="0">
     			<strong><font color ="brown">发起代购</font></strong> 
			</logic:equal>
			<logic:equal name="item" property="gaBetSponsor.betType" value="1">
			<c:if test="${item.gaBetPart.behavior ne null}">
				<c:if test="${item.gaBetPart.behavior == '0'}">
		    	    <strong><font color ="black">发起合买</font></strong> 
		        </c:if>
		        <c:if test="${item.gaBetPart.behavior == '1'}">
		    	    <strong><font color ="gray">参与合买</font></strong> 
		        </c:if>
			</c:if>
			<c:if test="${item.gaBetPart.behavior eq null}">
				<c:if test="${item.gaBetSponsor.userId == item.gaBetPart.userId }">
			        <strong><font color ="purple">发起合买</font></strong> 
			    </c:if>
				<c:if test="${item.gaBetSponsor.userId != item.gaBetPart.userId }">
		    	    <strong><font color ="purple">参与合买</font></strong> 
		        </c:if>
			</c:if>
			</logic:equal>	    
	    </td> --%>
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