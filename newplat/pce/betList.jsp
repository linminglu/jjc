<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ include file = "../common/inc_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../css/a.css" type="text/css" media="all">
<link rel="stylesheet" href="../css/aover.css" type="text/css" media="all">
<style type="text/css">
input{padding:0px;}
</style>
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
<SCRIPT language=javascript type=text/javascript>
    var maxnum = 1000000;//最大抽奖金额    function getEggs(betNumber,bsId,toget){
	    if(toget=="1")
	    	//if(parseInt(betNumber)<50){
	    	//	if(confirm("您帐户里的积分数少于500,要立即获取免费积分吗？")){    			
	    	//		location.href="<%=request.getContextPath()%>/pce/betSessionAction.do?method=preGetEggs&bsId="+bsId+"&userId="+$("userId").value+"&t="+TLT.getTime();  		
	    	//	}    	
	    	//}
	    	if(parseInt(betNumber)<=0){
	    		convertEggs();
	    	} 
    }
    
    
    function chkCI(num)
    {
        var betNumber=document.getElementById("betNumber").value;
        var sum = "0";
        var val = document.getElementsByName("SMONEY")[num].value;
        if (document.getElementsByName("CI")[num].checked == false)
        {
            document.getElementsByName("SMONEY")[num].value = "";
            val="0";
        }
        else
        {
            document.getElementsByName("SMONEY")[num].value = "1";
            val="1";
        }
        if ((val==null)||(val==""))
        {
            val ="0";
        }
        for (loop = 0 ; loop < 10 ; loop++)
        {
            if ((loop != num)&&(document.getElementsByName("CI")[loop].checked == true))
            {
                sm = document.getElementsByName("SMONEY")[loop].value;
                if ((sm==null)||(sm==""))
                {
                    sm = "0";
                }
                sum = parseInt(sum) + parseInt(sm);
            }/**/
        }
        
        if(sum>maxnum)
        {
          alert("对不起，总抽奖金额不能超过抽奖上限！");
           return false;
        }
        
        if (parseInt(betNumber)<(parseInt(sum)+parseInt(val)))
        {
            document.getElementById("SMONEYSUM").value = sum;
			document.getElementById("SMONEYSUM2").value = sum;
        }
        else
        {
            document.getElementById("SMONEYSUM").value = parseInt(sum)+parseInt(val);
			document.getElementById("SMONEYSUM2").value = parseInt(sum)+parseInt(val);
        } 
    }
    function AddMONEY(val)
    {
        var obj = document.getElementsByName('SMONEY')[val];
         var betNumber=document.getElementById("betNumber").value;
        if((obj.value=="")||(obj.value==null))
        {
            document.getElementsByName('SMONEY')[val].value = "0";
        }
        var ciobj = document.getElementsByName('CI')[val];
        if (ciobj.checked ==true)
        {
            var SMONEYSUM = document.getElementById("SMONEYSUM") ;
            var sum = parseInt(SMONEYSUM.value) + 10 ;  
            if(sum <=parseInt(betNumber))
            {
                if(obj.value == '0')
                {
                    obj.value = 10 ;
                }
                else
                {
                    obj.value = parseInt(obj.value) + 10 ;
                }
                SMONEYSUM.value = sum ;
            }
            else
            {
                alert("您的余额不足！");
            }
            
            if(sum>maxnum)
            {
              alert("对不起，总抽奖金额不能超过抽奖上限！");
               return false;
            }
        }
	    
    }
    function DecreaseMONEY(val)
    {
        var obj = document.getElementsByName('SMONEY')[val];
        if((obj.value=="")||(obj.value==null))
        {
            document.getElementsByName('SMONEY')[val].value = "0";
        }
        var ciobj = document.getElementsByName('CI')[val];
        if (ciobj.checked ==true)
        {
            var SMONEYSUM = document.getElementById("SMONEYSUM") ;
            var sum = parseInt(SMONEYSUM.value) - 10 ;
            if(obj.value != '0')
            {
                if(obj.value <= 10)
                {
                    obj.value = '0' ;
                }
                else
                {
                    obj.value = parseInt(obj.value) - 10 ;
                }
                SMONEYSUM.value = sum ;
            }
        }	     
    }
    
    function chgsubmit()
    {
        if(document.getElementById("tsum").value=="0"){
        	alert("本期抽奖已结束");
        	return;
        }
        var betNumber=document.getElementById("betNumber").value;
        var abc = 0;
        var smval,cival;
        var detail="";
        for (loop = 0 ; loop < 10 ; loop++)
        {
            if(document.getElementsByName("CI")[loop].checked==true)
            {
                smval = document.getElementsByName("SMONEY")[loop].value;
                if ((smval=="")||(smval == null))
                {
                    smval =="0";
                }
                else
                {
                    abc = parseInt(abc)+parseInt(smval);
                    if(detail=="")detail=detail+loop+":"+smval;
                    else detail=detail+";"+loop+":"+smval;
                }                
            }

        }
        var bsId=document.getElementById("bsId").value;
        var betId=document.getElementById("betId").value;

        
        document.getElementById("SMONEYSUM").value = abc ;
		document.getElementById("SMONEYSUM2").value = abc ;
        var smoneysum = document.getElementById("SMONEYSUM").value;
        
        
        if( parseInt(smoneysum)>maxnum)
        {       
          alert("对不起，总抽奖金额不能超过抽奖上限！");
          return false;
        }
        
        if (abc == "0" )
        {
            window.alert("请先抽奖！");
            return false;
        } 
        else if (parseInt(parseInt(betNumber)) < parseInt(smoneysum))
        {
            alert("您的余额不足！");
            return false;
        }
        else if (window.confirm("确认您抽奖？将扣除您"+smoneysum+"个积分！"))
        {
            for (loop = 0 ; loop < 10 ; loop++)
            {
                if(document.getElementsByName("SMONEY")[loop].disabled==true)
                {
                    document.getElementsByName("SMONEY")[loop].disabled = false;
                    document.getElementsByName("SMONEY")[loop].value="0";
                }
            }
             var link="betSessionAction.do?method=userBet&bsId="+bsId+"&betSum="+abc+"&detail="+detail+"&betId="+betId+"&userId="+$("userId").value;
             document.forms[0].action=link;
             //
             location.href=link;
         }
        else        
        {
            return false;
        }
    }
    function inputsb(val,num)
    {//parseInt
        var regex=/^[1-9]\d{0,}$/;
        var oldsum,sum,thismoney,sm;
        var betNumber=document.getElementById("betNumber").value;
        sum = 0;
        if(!regex.test(val))
        {
            document.getElementsByName("SMONEY")[num].value = "";
            //alert("请输入数字！");
            for (loop = 0 ; loop < 10 ; loop++)
            {
                if ((loop != num)&&(document.getElementsByName("CI")[loop].checked == true))
                {
                    sm = document.getElementsByName("SMONEY")[loop].value;
                    if ((sm==null)||(sm==""))
                    {
                        sm = "0";
                    }
                    sum = parseInt(sum) + parseInt(sm);
                }
            }
            document.getElementById("SMONEYSUM").value = sum;
			document.getElementById("SMONEYSUM2").value = sum;
        }       
        else
        { 
            for (loop = 0 ; loop < 10 ; loop++)
            {
                if ((loop != num)&&(document.getElementsByName("CI")[loop].checked == true))
                {
                    sm = document.getElementsByName("SMONEY")[loop].value;
                    if ((sm==null)||(sm==""))
                    {
                        sm = "0";
                    }
                    sum = parseInt(sum) + parseInt(sm);
                }
            }
            
            if( parseInt(sum)>maxnum)
            {       
              alert("对不起，总抽奖金额不能超过抽奖上限！");
              return false;
            }        
        	
            if (parseInt(parseInt(betNumber))<(parseInt(sum)+parseInt(val)))
            {
                alert("您的余额不足！");
                document.getElementsByName("CI")[parseInt(num)].checked = false;
                document.getElementsByName("SMONEY")[parseInt(num)].value = "";
                document.getElementById("SMONEYSUM").value = sum;
                document.getElementById("SMONEYSUM2").value = sum;
            }
            else
            {
                document.getElementsByName("CI")[parseInt(num)].checked = true;
                document.getElementById("SMONEYSUM").value = parseInt(sum) + parseInt(val) ;
                document.getElementById("SMONEYSUM2").value = parseInt(sum) + parseInt(val) ;
            }
        }
    }
    
	
		
	function chgbbb(bbb)
    {
        var abc = 0;
        var smval,cival;
        for (loop = 0 ; loop < 10 ; loop++)
        {
            if(document.getElementsByName("CI")[loop].checked==true)
            {
                smval = document.getElementsByName("SMONEY")[loop].value;
                if ((smval=="")||(smval == null))
                {
                    smval =="0";
                }
                else
                {
					smval = parseInt(parseInt(smval)*bbb);
					document.getElementsByName("SMONEY")[loop].value = smval;
                    abc = parseInt(abc)+parseInt(smval);
                }                
            }

        }
        document.getElementById("SMONEYSUM").value = abc ;
		document.getElementById("SMONEYSUM2").value = abc ;
        var smoneysum = document.getElementById("SMONEYSUM").value;
        if (abc == "0" )
        {
            window.alert("请先抽奖！");
        } 
    }
    
     
    function FirstModes(modes)
    {
         var   SMONEYS;
         var   sum = 0;
         document.getElementById("SMONEYSUM").value = 0;
         document.getElementById("SMONEYSUM2").value = 0;
         if(modes!="")
         {
            modes = parseInt(modes);
         }
         else
         {
             modes=1;
         }
     
         if(modes==1) //全包
         {
            SMONEYS="1,3,6,10,15,21,28,36,45,55,63,69,73,75,75,73,69,63,55,45,36,28,21,15,10,6,3,1";
            
         }else if(modes==2)//单

         {
            SMONEYS=",1,,1,,1,,1,,1,,1,,75,,73,,63,,45,,28,,15,,6,,1";

         }
         else if(modes==3)//双

         {
             SMONEYS="1,,1,,1,,1,,1,,63,,73,,75,,69,,55,,36,,21,,10,,3,";
            
           
         }else if(modes==4)//大

         {
             SMONEYS=",,,,,,,,,,,,,,75,73,69,63,55,45,36,28,21,15,10,6,3,1";
           
         }else if(modes==5)//小
         {
             SMONEYS="1,3,6,10,15,21,28,36,45,55,63,69,73,75,,,,,,,,,,,,,,";
            
         }else if(modes==6)//中

         {
             SMONEYS=",,,,,,,,,,63,69,73,75,75,73,69,63,,,,,,,,,,";
            
         }else if(modes==7)//边
         {
             SMONEYS="1,3,6,10,15,21,28,36,45,55,,,,,,,,,55,45,36,28,21,15,10,6,3,1";
            
         }
    
        var SMONEYS_Array = SMONEYS.split(",");

        for (loop = 0 ; loop < 10 ; loop++)
        {
            if(document.getElementsByName("CI")[loop].disabled == false)
            {
                document.getElementsByName("SMONEY")[loop].value = "";
                document.getElementsByName("CI")[loop].checked   =false;
            }
            var SMONEYS_temp = SMONEYS_Array[loop];  //抽奖金额
            if(SMONEYS_temp!=""&&document.getElementsByName("CI")[loop].disabled == false)
            {
               document.getElementsByName("CI")[loop].checked=true
               document.getElementsByName("SMONEY")[loop].value = SMONEYS_temp;  
               sum = sum+parseInt(SMONEYS_temp);  //总抽奖金额

             }
        }
        document.getElementById("SMONEYSUM").value = sum;
        document.getElementById("SMONEYSUM2").value = sum;
    }
  </SCRIPT>
<title>好运来 - 帮学试</title>
</head>
<BODY onload="getEggs('${betNumber },','${bs.betSessionId }','${getEggs}');">

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
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=myBet';" class="bqc_of">我的抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=preSaveBetModel';" class="bqc_of">自动抽奖</div>
					<div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">排行榜</div>
					<div class="bqc_on">抽奖</div>
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

						<input type="hidden" value="${betNumber }" id="betNumber">
						<FORM id=form1 method="post" action="">
						<input type="hidden" id="userId" value="<bean:write name="userId"/>"/>
						
						<INPUT id=bsId type=hidden value="${bs.betSessionId }" >
						<INPUT id=betId type=hidden value="${bet.betId}" >
						<INPUT id=pvs type=hidden value="${pvs }">
						<INPUT id=cis type=hidden value="${cis }">
						<INPUT id=smoneys type=hidden value="${smoneys }">
						
						<div class="">
							<div class="l bold">我的积分：<span class="points-span">${betNumber }</span>，当前第${bs.betSessionId }期，总抽奖数${bs.betTotalNumber }注，开奖时间：<bean:write name="bs" property="betDate" format="yyyy-MM-dd HH:mm:ss"/></span>
							</div>
							<div class="r tar"></div>
							<div class="c"></div>
						</div>
						<div class="pce-mode">
							<strong>标准抽奖模式：</strong>
						   <!--  <a class="sl" href="#" onclick="FirstModes(1)">全包</a> -->
						    <a class="sl" href="#" onclick="FirstModes(2)">单</a>
						    <a class="sl" href="#" onclick="FirstModes(3)">双</a>
						    <!-- <a class="sl" href="#" onclick="FirstModes(4)">大</a>
						    <a class="sl" href="#" onclick="FirstModes(5)">小</a>
						    <a class="sl" href="#" onclick="FirstModes(6)">中</a>
						    <a class="sl" href="#" onclick="FirstModes(7)">边</a> -->
							
							<INPUT id="b05" onclick="chgbbb(1);" type="button" value="1倍" name="b05"> 
							<INPUT id="b08" onclick="chgbbb(2);" type="button" value="2倍" name="b08">
							<INPUT id="b12" onclick="chgbbb(3);" type="button" value="3倍" name="b12"> 
							<INPUT id="b15" onclick="chgbbb(5);" type="button" value="5倍" name="b15"> 
							<INPUT id="b20" onclick="chgbbb(8);" type="button" value="8倍" name="b20">
							<INPUT id="Button1" onclick="chgbbb(10);" type="button" value="10倍" name="b20"> 
						    <INPUT class="input1" id="SMONEYSUM2" readOnly="readonly" value="0" name="SMONEYSUM2" style="width: 5em;">
						    <INPUT type="button" onclick="chgsubmit()" value="确认抽奖" name=submit>
						</div>
						<DIV class=center><!--内容开始-->
						
						<TABLE align="center" border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
						  <TBODY>
						  <TR>
						    <TD align=center width="24%" background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff height=28>预测结果</TD>
						    <TD style="WIDTH: 174px" align=center background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff>当前赔率</TD>
						    <TD align=center width="18%"  background=<%=request.getContextPath()%>/images/bg.gif  bgColor=#ffffff>请选择</TD>
						    <TD align=center width="39%" background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff>抽奖</TD>
						 </TR>
						 
						     
						<logic:iterate id="re" name="result">
						  <TR>
						    <TD align=center bgColor=#ffffff height=30><IMG src="<%=request.getContextPath()%>/images/number_${re}.gif"></TD>
						    <TD style="WIDTH: 174px" align=center bgColor=#ffffff><INPUT class=pv readOnly name=pv></TD>
						    <TD align=center bgColor=#ffffff><INPUT onclick=chkCI('${re }'); type=checkbox value=1 name=CI></TD>
						    <TD align=center bgColor=#ffffff>
						    	<INPUT onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');inputsb(this.value,'${re }');" size=15 value=0 name=SMONEY disabled="disabled">
						    	<!-- <INPUT class=input3 onclick=AddMONEY('${re }'); type=button value=+ name=Add>
						    	<INPUT class=input3 onclick=DecreaseMONEY('${re }'); type=button value=- name=Decrease>  -->
						    </TD>
						 </TR> 
						</logic:iterate>
						    
						  <TR>
						    <TD align=center bgColor=#f4f4f4 colSpan=4 height=30><SPAN 
						      class=STYLE1>我的总抽奖额：</SPAN> <INPUT class=input1 id=SMONEYSUM readOnly 
						      value=0 name=SMONEYSUM></TD>
						  </TR>
						  <TR>
						    <TD align=middle bgColor=#ffffff colSpan=4 height=38>
						    	<INPUT type=button onclick="chgsubmit()" value="确认抽奖"　>
						    </TD>
						  </TR>
						  </TBODY>
						</TABLE>
						
					</div>
					
				</div>
				
				<div class="c"></div>
			</div>
		</div>
	</div>
	<div class="c"></div>
</div>

<div id="b"><c:import url="../include/footer.jsp"/></div>

<SCRIPT language=javascript type=text/javascript>  
   var smoneyval = "0";
   var cival = "0";
   var smoneys=document.getElementById("smoneys").value;
   var cis=document.getElementById("cis").value;
   var pvs=document.getElementById("pvs").value;
   var smoneys_Array = smoneys.split(",");
   var cis_Array = cis.split(",");
   var pvs_Array = pvs.split(",");
   for (loop = 0 ; loop < 10 ; loop++)
   {
        cival = cis_Array[loop];
        if (cival == "1")
        {
            document.getElementsByName("CI")[loop].checked = false;
            document.getElementsByName("CI")[loop].disabled = true;
            document.getElementsByName("SMONEY")[loop].disabled = true; 
            document.getElementsByName("Add")[loop].disabled = true;
            document.getElementsByName("Decrease")[loop].disabled = true;            
        } 
        smoneyval = smoneys_Array[loop]; 
        if ((smoneyval==null)||(smoneyval=="")||(smoneyval=="0"))
        {
            smoneyval="";
        }
        document.getElementsByName("SMONEY")[loop].value = smoneyval;
        document.getElementsByName("pv")[loop].value = pvs_Array[loop];
   }
</SCRIPT>

<SCRIPT language=javascript type=text/javascript>  
    //保存要抽奖的
    function lottery()
    {
       var abc=0;
       var smoneyval = "0";
       var cival = "0";
       var smoneys = "" ;
       var cis = "";       
       var smoneys_Array = smoneys.split(",");
       var cis_Array = cis.split(",");
       for (loop = 0 ; loop < 10 ; loop++)
       {
            cival = cis_Array[loop];
            if (cival == "1")
            {
                document.getElementsByName("CI")[loop].checked = true;
                document.getElementsByName("CI")[loop].disabled = false;
                document.getElementsByName("SMONEY")[loop].disabled = false; 
                document.getElementsByName("Add")[loop].disabled = false;
                document.getElementsByName("Decrease")[loop].disabled = false;            
            } 
            smoneyval = smoneys_Array[loop];  
            if ((smoneyval==null)||(smoneyval=="")||(smoneyval=="0"))
            {
                smoneyval="";
            }
            else
            {            
                abc = parseInt(abc)+parseInt(smoneyval);
            }
            document.getElementsByName("SMONEY")[loop].value = smoneyval;
       }
       document.getElementById("SMONEYSUM").value = abc;
	   document.getElementById("SMONEYSUM2").value = abc;
   }
            </SCRIPT>
<!--内容结束--></DIV>

</FORM>
</div>
<input type="hidden" value="${times }" id="times">
<input type="hidden" value="${times }" id="tsum">

	
<SCRIPT language=javascript type=text/javascript>

var sum=document.getElementById("times").value; 
var newstimer;

function submitForm() 
{  
    if(sum<=0) 
    { 
      clearInterval(newstimer);
      document.getElementById("tsum").value="0"; 
    } 
    else
    {
        sum=sum-1;
        document.getElementById("tsum").value=sum;
    }
} 

newstimer=setInterval("submitForm()",1000) ;

</SCRIPT>	
</BODY></HTML>

