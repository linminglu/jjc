<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ include file = "../common/inc_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html> 
<head>
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
<BODY>

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
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=myBet';" class="bqc_of">我的抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=preSaveBetModel';" class="bqc_of">自动抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">排行榜</div>
					<div class="bqc_on">查看</div>
					<div class="l bqc_end"></div>
					<div class="l mt5 ml12">
						<a href="javascript:convertEggs();" class="sl">兑换积分</a>
						<span class="cd">|</span>
						<a target="_blank" href="../pce/help/game.htm" class="sl">游戏说明</a>
					</div>
				</div>
				
				<div class="mleft_">
					<div class="p10">
						
						<div id="single-column">
						
						<div>
							<div class="tac bold">当前第${bs.betSessionId }期，总抽奖数${bs.betTotalNumber }注，开奖时间：<bean:write name="bs" property="betDate" format="yyyy-MM-dd HH:mm:ss"/></div>
						</div>
						<DIV class=center><!--内容开始-->
						<TABLE align="center" border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
						  <TBODY>
						  <TR>
						    <TD align=center width="24%" background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff height=28>抽奖</TD>
						    <TD align=center width="19%" background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff>当前赔率</TD>
						    <TD align=center width="39%" background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff>您的抽奖结果</TD>
						  </TR>
						<logic:iterate id="re" name="list">
						<logic:equal value="${re.betDigital }" name="bs" property="betCorrectDitigal">
						
						
						  <TR>
						    <TD align=center bgColor=#ffffe1 height=30><IMG src="<%=request.getContextPath()%>/images/number_${re.betDigital}.gif"></TD>    
						    <TD align=center bgColor=#ffffe1><INPUT class=pv readOnly value="${re.betRates }" name=pv></TD> 
						    <TD align=center bgColor=#ffffe1>
						    	<logic:equal value="0" name="re" property="flg">
						    	<INPUT class=pv readOnly size=15 value="" name=SMONEY> &nbsp;
						  	  </logic:equal>
						   	 <logic:notEqual value="0" name="re" property="flg">
						    	<INPUT class=pv readOnly size=15 value="${re.flg }" name=SMONEY> &nbsp;
						  	  </logic:notEqual>    
						    </TD>
						  </TR>
						</logic:equal>
						<logic:notEqual value="${re.betDigital }" name="bs" property="betCorrectDitigal">
						  <TR>
						    <TD align=center bgColor=#ffffff height=30><IMG src="<%=request.getContextPath()%>/images/number_${re.betDigital}.gif"></TD>    
						    <TD align=center bgColor=#ffffff><INPUT class=pv readOnly value="${re.betRates }" name=pv></TD> 
						    <TD align=center bgColor=#ffffff>
						    	<logic:equal value="0" name="re" property="flg">
						    	<INPUT class=pv readOnly size=15 value="" name=SMONEY> &nbsp;
						  	  </logic:equal>
						   	 <logic:notEqual value="0" name="re" property="flg">
						    	<INPUT class=pv readOnly size=15 value="${re.flg }" name=SMONEY> &nbsp;
						  	  </logic:notEqual>    
						    </TD>
						  </TR>
						</logic:notEqual>
						</logic:iterate>  
						</TBODY>
						</TABLE>
						<!--内容结束-->
						</DIV>
						
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

</BODY></HTML>