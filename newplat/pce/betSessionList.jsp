<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
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
<SCRIPT language=javascript type=text/javascript>
        function chkResult(v,val)
        {
            if (val != "-")
            {
            	var index=v.indexOf("=")+1;
            	var s=v.substr(0,index);
                document.write(s + "<img src='../images/number_"+val+".gif' />");
            }
            else
            {
                document.write("-");
            }
        }
        
        function bet(bsId){
        	var sum=document.getElementById("tsum").value;
        	var id=document.getElementById("bsId").value;
        	if(sum=="0" && bsId==id){
        		alert("本期抽奖已结束");
        		Reload();
        		return;
        	}
        	location.href="../pce/betSessionAction.do?method=preBet&bsId="+bsId;        
        }
        
        
        function chkStatus(status,lid)
        {
            //状态：0抽奖中，1暂停抽奖等待开奖，2已开奖   
            switch (status)
            {
                case '0' :
                {
              
             	 //document.write("<a class='sl' href='../pce/betSessionAction.do?method=preBet&bsId="+lid+"'>抽奖</a>");
             	  //var s="<a class='sl' href=\"javascript:bet('"+lid+"')\">抽奖</a>";
             	  document.write("<span style='color:gray;'>未开奖</span>");
       			  break;
                }
                case '1':
                {
                    //document.write("<a href='../pce/betSessionAction.do?method=getUserBetInfo&bsId="+lid+"'  title='查看我的抽奖结果' style='color:blue'>开奖中</a>")
                    document.write("<span style='color:#EA5246;'>开奖中</span>");
                    break;
                }
                case '2':
                {
                    //document.write("<a href='../pce/betSessionAction.do?method=getUserBetInfo&bsId="+lid+"' title='查看我的抽奖结果' style='color:green'>已开奖</a>")
                   document.write("<span style='color:blue;'>已开奖</span>");
                    break;
                }
            }
        }
        function chkwin(val,lid,status)
        {
            if (val!= '0')
            {
            	if(status=='2'){
               	 //document.write("<a class='a0' href='../pce/betSessionAction.do?method=getBetSessionInfo&bsId="+lid+"'title='查看中奖人名单'> "+val+" </a>");
               		document.write(val);
               	}else{
               		document.write("-");
               	}
            }
            else
            {
                document.write("0");
            }
        }
    </SCRIPT>
<script type="text/javascript">
function pageInit(){
	updateLink();
}
function convertEggs(){
	openIframeWindow("betSessionAction.do?method=convertEgg","兑换积分",350,260);
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
//window.onload = pageInit;
</script>
    
 <%
	Integer total=request.getAttribute("total")!=null?(Integer)request.getAttribute("total"):new Integer(0);
	String userId=request.getAttribute("userId")!=null?((Integer)request.getAttribute("userId")).toString():"0";
	int totalCount = 0;	
	if(total != null)totalCount=total.intValue();	
	String count = String.valueOf(totalCount);
%> 
    
</head>
<body>
<pg:pager 
		url="betSessionAction.do" 
	    index="<%=index%>"
	    items="<%=totalCount%>"
	    maxPageItems="<%= maxPageItems %>"
	    maxIndexPages="<%= maxIndexPages %>"
	    isOffset="<%= isOffset %>"
	    export="offset,currentPageNumber=pageNumber" scope="request"> 
	    <pg:param name="method" value="getBetSessionList"/>
	    <pg:param name="userId" value="<%=userId %>"/>
	    
<input type="hidden" value="${times }" id="times">
<input type="hidden" value="${bs.betSessionNo}" id="bsId">
<input type="hidden" value="" id="tsum">
<bean:define id="totalLength" value="<%=count%>"/>

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
					<div class="bqc_on">好运来</div>
					<!-- <div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=myBet';" class="bqc_of">我的抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=preSaveBetModel';" class="bqc_of">自动抽奖</div>
					-->
					<!-- <div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">排行榜</div>-->
					
					<div class="l bqc_end"></div>
					
					<!-- <div class="l mt5 ml12">
						<a href="javascript:convertEggs();" class="sl">兑换积分</a>
						<span class="cd">|</span>
						<a target="_blank" href="../pce/help/game.htm" class="sl">游戏说明</a>
					</div>  -->
				</div>
				
				
				<div class="mleft_">
					<div class="p10">
						<div class="p5">
							<%-- <div class="l"><strong>我的积分</strong>：<span class="points-span"><strong>${betNumber}</strong></span></div> --%>
							<div class="r tar"><strong class="red"><span id="otime"></span></strong>
							<logic:notEmpty name="betMode">
								<span class="red" style="margin-left:200px;">已启用自动投模式</span>
							</logic:notEmpty>
							</div>
							<div class="c"></div>
						</div>
	
						<table border="0" cellpadding="0" cellspacing="1" class="pce-tbl" width="100%">
							  <tbody>
							   <tr>
									<th height="28" background=../images/bg.gif>期号</th>
									<th  background=../images/bg.gif>开奖时间</th>
									<th  background=../images/bg.gif>开奖结果</th>
									<th  background=../images/bg.gif>抽奖总人数</th>
									<th  background=../images/bg.gif>积分总数</th>			
									<th  background=../images/bg.gif>中奖人数</th>
									<!-- <th  background=../images/bg.gif>中奖统计</th> -->
									<th  background=../images/bg.gif>状态</th>	
							  </tr>
								
							<logic:iterate id="bs" name="list">
							<pg:item>	
							  <tr>
								<td height="25">${bs.betSessionNo}</td>
								<td><bean:write name="bs" property="betDate" format="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
								
								<logic:notEqual value="2" name="bs" property="status">
									<SCRIPT language=javascript type=text/javascript>chkResult('','-');</SCRIPT>
								</logic:notEqual>
								<logic:equal value="2" name="bs" property="status">
									<SCRIPT language=javascript type=text/javascript>chkResult('${bs.betResult}','${bs.betCorrectDitigal}');</SCRIPT>
								</logic:equal>		
								
								</td>    
								
								<td>${bs.betNumber }</td>
								<td>${bs.betTotalNumber }<IMG src="../images/egg.gif"></td>
								<td>
									<logic:equal value="0" name="bs" property="status">
										-
									</logic:equal>
									<logic:notEqual value="0" name="bs" property="status">
										<SCRIPT language=javascript type=text/javascript>chkwin('${bs.betPersonNumber }','${bs.betSessionId }','${bs.status}');</SCRIPT>
									</logic:notEqual>
								      
						      	</td>
						      	<%-- <td>${bs.betEarnNumber}<IMG src="../images/egg.gif"></td> --%>
								<td>    						
						      		<SCRIPT type="text/javascript">chkStatus('${bs.status }','${bs.betSessionId }');</SCRIPT>		
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
<SCRIPT language=javascript type=text/javascript>

var sum=document.getElementById("times").value; 
var ISSUE =document.getElementById("bsId").value;
var str; 
var newstimer;

function cDate(oSum) 
{ 
   str="第"+ISSUE+"期开奖还剩"+oSum+"秒"
   return str; 
} 

function submitForm() 
{  
    if(sum<=0) 
    { 
      clearInterval(newstimer);
      document.getElementById("tsum").value="0"; 
      document.getElementById("otime").innerHTML="第"+ISSUE+"期开奖还剩"+0+"秒";  
      //Reload();
      //location.reload();
    } 
    else
    {
        sum=sum-1;
        cDate(sum);
        document.getElementById("otime").innerHTML = str;
        document.getElementById("tsum").value=sum;
    }
} 
function Reload()
{
  clearInterval(newstimer)
  var id=document.getElementById("bsId").value; 
  var sum=document.getElementById("tsum").value;
  location.href="../pce/betSessionAction.do?method=getBetSessionList&bsId="+id+"&sum="+sum;
}

if(ISSUE){
	//newstimer=setInterval("submitForm()",1000) ;
}

</SCRIPT>	
</html>

