<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ include file = "/common/inc_include.jsp"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/a.css" type="text/css" media="all">
<link rel="stylesheet" href="../css/aover.css" type="text/css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/dialog.js"></script>
<script type="text/javascript">
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

<body>

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
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBetSessionList';" class="bqc_of">游戏</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=preSaveBetModel';" class="bqc_of">自动抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">赢得最多</div>
					<div class="bqc_on">输得最多</div>
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

						<input type="hidden" id="userId" value="<bean:write name="userId"/>"/>
						<div class="p5 tac"><strong>领取积分排行榜</strong></div>
						<TABLE align="center" border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
							  <TBODY>
							   <TR >
							  
									<TD width="10%" background=<%=request.getContextPath()%>/images/bg.gif height=28>用户ID</TD>
									<TD width="20%" background=<%=request.getContextPath()%>/images/bg.gif>累计领取积分</TD>
						
							  </TR>
								
							<logic:iterate id="ug" name="list">	
							  <TR  bgColor=#ffffff>
								<TD bgColor=#fffac7 height=28>${ug.userId }</TD>
								<TD bgColor=#ffffff>${ug.getEggSum}<IMG src="<%=request.getContextPath()%>/images/egg.gif"></TD>
							  </TR>
							  </logic:iterate>
							  </TBODY>
						</TABLE>
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

