<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ include file = "../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html> 
<head>
<link rel="stylesheet" href="../css/a.css" type="text/css" media="all">
<link rel="stylesheet" href="../css/aover.css" type="text/css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/dialog.js"></script>
<script type="text/javascript">
function repay(betNum,ugId,ugCount){
	if(parseInt(betNum)<parseInt(ugCount)){
		alert("您的积分余额不够!");
		return;
	}
	location.href="betSessionAction.do?method=repayEggs&ugId="+ugId;	
}

function pageInit(){
	updateLink();
}
function updateLink(){
	var as = document.getElementsByTagName("a");
	if(as){
		var tt = TLT.getTime();
		for(var i=0;i<as.length;i++){
			if(as[i].href && as[i].href.length>5){
				var href = as[i].href;
				as[i].href = href + "&t="+tt;
			}
		}
	}
}
function convertEggs(){
	openIframeWindow("betSessionAction.do?method=convertEgg","兑换积分",350,260);
}
//window.onload = pageInit;
</script>	
<title>好运来 - 帮学试</title>
</head>
 <%
	Integer total=request.getAttribute("total")!=null?(Integer)request.getAttribute("total"):new Integer(0);
	int totalCount = 0;	
	if(total != null)totalCount=total.intValue();	
	String count = String.valueOf(totalCount);
%>  
 
<body>
<pg:pager 
		url="betSessionAction.do" 
	    index="<%=index%>"
	    items="<%=totalCount%>"
	    maxPageItems="<%= maxPageItems %>"
	    maxIndexPages="<%= maxIndexPages %>"
	    isOffset="<%= isOffset %>"
	    export="offset,currentPageNumber=pageNumber" scope="request"> 
	    <pg:param name="method" value="getBetSessionInfo"/>
	    <pg:param name="bsId" value="${bsId}"/>	

<div id="head"><c:import url="../include/hd.jsp"/></div>
<div id="main">
	<div class="m1 wl1"><c:import url="../include/app.jsp"/></div>
	<div class="m2 wr1">
		<div id="r2_2">
		
			<div id="r3">
				<div class="l"><strong class="f14">好运来</strong></div>
			  	<div class="r"><a class="sl" href="javascript:window.history.back();"><<返回上一页</a></div>
				<div class="c"></div>
			</div>
		
			<div class="p10">
				<div class="bqc_bg">
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBetSessionList';" class="bqc_of">游戏首页</div>
					<div class="bqc_on">我的账户</div>
					<div class="l bqc_end"></div>
					<div class="l mt5 ml12">
						<a href="javascript:convertEggs();" class="sl">兑换积分</a>
						<span class="cd">|</span>
						<a target="_blank" href="../pce/help/game.htm" class="sl">游戏说明</a>
					</div>
				</div>
				
				<div class="mleft">
					<div class="p10">
						
						<div id="single-column">

						 <TABLE align="center" border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
							  <TBODY>
							   <tr class="tr-head">
							  	<TD colspan="5" align=left height=25><STRONG class=style1>您当前的积分总数：${ubet.betNumber}</STRONG>，下面是您借取的积分记录：</TD>
							  </tr>
							 
							   <tr class="tr-head">  
									<TH width="10%" background=<%=request.getContextPath()%>/images/bg.gif >序号</TD>
									<TH width="20%" background=<%=request.getContextPath()%>/images/bg.gif>获取记录</TD>
									<TH width="20%" background=<%=request.getContextPath()%>/images/bg.gif>积分数量</TD>
									<TH width="20%" background=<%=request.getContextPath()%>/images/bg.gif>获取时间</TD>
									<TH width="20%" background=<%=request.getContextPath()%>/images/bg.gif>备&nbsp;&nbsp;&nbsp;&nbsp;注</TD>
							  </TR>
							  <%int i=0; %>
								
							<logic:iterate id="ug" name="list" indexId="ind">	
							<pg:item>
							  <tr class="tr-body"> 
								<TD height=25><%=++i%></TD>
								<TD>低于50获取</TD>
								<TD>${ug.eggCount}<IMG src="<%=request.getContextPath()%>/images/egg.gif"></TD>
								<TD><bean:write name="ug" property="getEggDate" format="yyyy-MM-dd HH:mm:ss"/></TD>
								<TD>
									<logic:equal value="0" name="ug" property="isrepay">
										<a href="#" onclick="javascript:repay('${ubet.betNumber}','${ug.userGetEggsId}','${ug.eggCount }')">现在偿还</a>
									</logic:equal>			
									<logic:equal value="1" name="ug" property="isrepay">			
										已偿还
						
									</logic:equal>
								</TD>
							  </TR>
							</pg:item>  
							  </logic:iterate>
						</TABLE>
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

