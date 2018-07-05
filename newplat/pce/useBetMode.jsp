<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
 
<html> 
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="../style/base.css" media="all">
<link type="text/css" rel="stylesheet" href="../style/navigation.css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
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
window.onload = pageInit;
</script>
<SCRIPT language=javascript type=text/javascript>

    function saveUseMode(){
    	var bmId=document.getElementById("MODES").value;
    	var startBsId=document.getElementById("STARTISSUE").value;
    	var endBsId=document.getElementById("ENDISSUE").value;
    	var lessNumber=document.getElementById("MINGOLDEGGS").value;
    	var exceedNumber=document.getElementById("MINGOLDEGGS").value;
    	var link = "<%=request.getContextPath()%>/pce/betSessionAction.do?method=saveUseMode&bmId="+bmId+"&startBsId="+startBsId+"&endBsId="+endBsId+"&lessNumber="+lessNumber+"&exceedNumber="+exceedNumber;
    	link += "&userId="+$("userId").value;
    	location.href = link;
    }
    
    
    
</SCRIPT>
</HEAD>
<BODY>
<div id="sub-nav">
	<ul id="userFeedList">
		<li><a href="../xiaonei/p.do?method=init&userId=<bean:write name="userId"/>">词霸</a></li>
		<li><a href="../xiaonei/p.do?method=more&userId=<bean:write name="userId"/>">已练过</a></li>
		<li><a href="../pce/betSessionAction.do?method=getBetSessionList&userId=<bean:write name="userId"/>">幸运游戏</a></li>
		<li class="selected"><a href="#">设置抽奖模式</a></li>
		<li><a target="_blank" href="../xiaonei/help/test.htm" class="help">词霸说明</a></li>
		<li><a target="_blank" href="../pce/help/game.htm" class="help">游戏说明</a></li>
	</ul>
	
</div>

<div id="single-column">

<FORM id=form1 name=form1 action="" method=post>
<input type="hidden" name="userId" id="userId" value="<bean:write name="userId"/>"/>
<DIV class=center>
<DIV class=title>
<SPAN><A href="<%=request.getContextPath() %>/pce/betSessionAction.do?method=preSaveBetModel&userId=<bean:write name="userId"/>">抽奖模式编辑</A></SPAN> 
<SPAN><A href="<%=request.getContextPath() %>/pce/betSessionAction.do?method=preUseMode&userId=<bean:write name="userId"/>">自动抽奖设置</A></SPAN> 
</DIV>
</DIV>
<DIV class=center>
<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
  <TBODY>
  <TR>
    <TD align=left width="90%"   height=25>&nbsp;&nbsp;<STRONG>自动抽奖模式设置</STRONG></TD>
    <TD align=middle width="10%">&nbsp;</TD>
  </TR>
  </TBODY>
</TABLE>
<P>&nbsp;</P>
<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
  <TBODY>
  <TR>
    <TD align=right width="28%" bgColor=#ffffff height=30>
      <P>请选择自动抽奖使用的抽奖模式：</P></TD>
    <TD class=pdl10 width="72%" bgColor=#ffffff>
    <SELECT id=MODES name=MODES>
    <logic:notEmpty name="list">     
      <logic:iterate id="md" name="list">
      	<OPTION value=${md.betModeId }>${md.title }</OPTION>  
      </logic:iterate>
     </logic:notEmpty>
    </SELECT>
    
  
     </TD>
     </TR>
  <TR>
    <TD align=right bgColor=#ffffff height=40>
      <P>自动抽奖开始期数：</P></TD>
    <TD class=pdl10 bgColor=#ffffff>
    <INPUT class=input0 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" 
      id=STARTISSUE  onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
       maxLength=10 value=${startBetSession.betSessionId } name=STARTISSUE> 开始期数须大于等于
       <SPAN class=STYLE3>${startBetSession.betSessionId }</SPAN>
       </TD>
  </TR>
  <TR>
    <TD align=right bgColor=#ffffff height=40>自动抽奖结束期数：</TD>
    <TD class=pdl10 bgColor=#ffffff><INPUT class=input0 
      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))"  id=ENDISSUE 
      onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
      maxLength=10 value=${endBetSession.betSessionId } name=ENDISSUE> 自动抽奖模式设置开始到结束的期数须小于等于
      <SPAN class=STYLE3>${endBetSession.betSessionId }</SPAN> 
      </TD>
  </TR>
  <TR>
    <TD align=right bgColor=#ffffff height=40>账户积分最小值：</TD>
    <TD class=pdl10 bgColor=#ffffff><INPUT class=input0 
      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" id=MINGOLDEGGS 
      onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
      maxLength=9 value=0 name=MINGOLDEGGS> 积分少于此值时自动抽奖结束
     </TD>
   </TR>
   <TR>
    <TD align=right bgColor=#ffffff height=40>账户积分最小值：</TD>
    <TD class=pdl10 bgColor=#ffffff><INPUT class=input0 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" 
      id=MAXGOLDEGGS onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
       maxLength=9 value=0 name=MAXGOLDEGGS> 积分大于此值时自动抽奖结束</TD></TR>     

  <TR>
    <TD align=middle bgColor=#ffffff colSpan=2 height=50>
    <a href="#" onclick="saveUseMode()">
    <IMG onclick="" height=29 src="<%=request.getContextPath()%>/images/submit.gif" width=115 border=0>
    </a>  
    <A href="#" onclick="history.back();">
      <IMG height=29 src="<%=request.getContextPath()%>/images/back2.gif" width=115 border=0>
      </A>
      </TD>
      </TR>
      </TBODY>
      </TABLE>
<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
  <TBODY>
  <TR>
    <TD align=left colSpan=2 height=30><STRONG class=STYLE3>备注：</STRONG></TD></TR>
  <TR>
    <TD width="5%" height=30>&nbsp;</TD>
    <TD style="LINE-HEIGHT: 20px" align=left width="95%">
      <P>自动抽奖模式能帮助您自动进行好运来的抽奖操作，在使用自动抽奖模式前，您需要保存至少一种的抽奖模式！</P>
      <P><SPAN class=STYLE3>抽奖方法</SPAN>：</P>
      <P>1、选择您要进行自动抽奖的抽奖模式。</P>
      <P>2、设置自动抽奖开始与结束的期数。</P>
      <P>3、设置账户积分的最少值，当账户积分少于您设置的最小值时自动停止抽奖。</P>
      <P>4、点击开始抽奖后，自动抽奖程序开始运行。</P>
      <P>5、关机以后自动抽奖仍然会继续！</P>
  <P>6、小金库所存积分，不能用于自动抽奖。</P></TD></TR></TBODY></TABLE></DIV>
</FORM>

</div>
</BODY></HTML>
