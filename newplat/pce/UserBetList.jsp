<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file = "../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html> 
	<head>
		<title>好运来 - 帮学试</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="../css/a.css" type="text/css" media="all">
		<link rel="stylesheet" href="../css/aover.css" type="text/css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/dialog.js"></script>
<script type="text/javascript">
function convertEggs(){
	openIframeWindow("betSessionAction.do?method=convertEgg","兑换积分",350,260);
}
</script>
<%
	String totalCount =request.getAttribute("totalCount")!=null?request.getAttribute("totalCount")+"":"0";
%> 
    
</head>
<body>
<pg:pager 
	url="betSessionAction.do" 
    index="<%=index%>"
    items="<%=Integer.parseInt(totalCount)%>"
    maxPageItems="<%= maxPageItems %>"
    maxIndexPages="<%= maxIndexPages %>"
    isOffset="<%= isOffset %>"
    export="offset,currentPageNumber=pageNumber" scope="request"> 
    <pg:param name="method" value="myBet"/>
    <bean:define id="totalLength" value="<%=totalCount%>"/>
	    

<div id="head"><c:import url="../include/hd.jsp"/></div>
<div id="main">
	<div class="m1 wl1"><c:import url="../include/app.jsp"/></div>
	<div class="m2 wr1">
		<div id="r2_2">
		
			<div id="r3">
				<div class="l"><strong class="f14">好运来</strong></div>
			  	<div class="r"><a class="sl" href="javascript:window.history.back();">返回上一页</a></div>
				<div class="c"></div>
			</div>
		
			<div class="p10">
				<div class="bqc_bg">
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBetSessionList';" class="bqc_of">游戏</div>
					<div class="bqc_on">我的抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=preSaveBetModel';" class="bqc_of">自动抽奖</div>
					<!--  <div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">排行榜</div>
					<div class="l bqc_end"></div>
					<div class="l mt5 ml12">
						<a href="javascript:convertEggs();" class="sl">兑换积分</a>
						<span class="cd">|</span>
						<a target="_blank" href="../pce/help/game.htm" class="sl">游戏说明</a>
					</div>-->
				</div>
				
				<div class="mleft_">
					<div class="p10">
						<div class="p5">
							<div class="l"><strong>我的积分</strong>：<span class="points-span"><strong><c:choose><c:when test="${userBet==null || userBet.betNumber==null}">0</c:when><c:otherwise><c:out value="${userBet.betNumber}"/></c:otherwise></c:choose></strong></span></div>
							<div class="r tar"></div>
							<div class="c"></div>
						</div>
	
						<table border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
							  <tbody>
							   <tr>
									<th height="28" background="../images/bg.gif">期号</td>
									<th  background="../images/bg.gif">抽奖额</td>
									<th  background="../images/bg.gif">开奖结果</td>
									<th  background="../images/bg.gif">状态</td>
									<th  background="../images/bg.gif">获得积分</td>			
									<th  background="../images/bg.gif">详情</td>
							  </tr>
								
							<logic:iterate id="item" name="betList">
							<pg:item>	
							  <tr onmouseover="this.style.backgroundColor='#efefef'" onmouseout="this.style.backgroundColor=''">
								<td height="25"><c:out value="${item.bet.betSessionId}"/></td>
								<td><c:out value="${item.bet.totalNumber}"/><img src="../images/egg.gif"></td>
								<td><c:if test="${item.betSession.status=='0' || item.betSession.status=='1'}">-</c:if>
								<c:if test="${item.betSession.status=='2'}"><span class="red"><c:out value="${item.bet.betResult}"/></span></c:if>
								</td>
								<td><c:if test="${item.betSession.status=='0'}"><span class="red">竞猜中</span></c:if>
								<c:if test="${item.betSession.status=='1'}"><span class="blue">开奖中</span></c:if>
								<c:if test="${item.betSession.status=='2'}"><span class="green">已开奖</span></c:if>
								</td>
								<td>
								<c:choose><c:when test="${item.bet.betNumber>0}"><c:out value="${item.bet.betNumber}"/></c:when><c:otherwise>0</c:otherwise></c:choose><img src="../images/egg.gif"></td>
								<td><c:if test="${item.betSession.status=='2' || item.betSession.status=='1'}">
								<a class="sl" href="betSessionAction.do?method=getUserBetInfo&bsId=<c:out value="${item.bet.betSessionId}"/>">查看</a>
								</c:if>
								<c:if test="${item.betSession.status=='0'}">
								<a class="sl" href="betSessionAction.do?method=preBet&bsId=<c:out value="${item.bet.betSessionId}"/>">查看</a>
								</c:if>
								</td>
							  </TR>
							  </pg:item>
							  </logic:iterate>
							  </TBODY>
						</table>
						<input type="hidden" value="" id="sumtimes">
						  		
						<jsp:include page="/template/pagination_template.jsp" flush="true"/>
						</pg:pager>
						
						</div>
						
					</div>
					
				</div>
				
				<div class="c"></div>
			</div>
		</div>
	</div>
	<div class="c"></div>
</div>

<div id="b"><c:import url="../include/footer.jsp"/></div>

</body>
</html>

