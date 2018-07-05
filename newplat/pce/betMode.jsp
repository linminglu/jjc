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
	openIframeWindow("betSessionAction.do?method=convertEgg","兑换积分",350,260,350,260);
}
//window.onload = pageInit;
</script>
<title>好运来 - 帮学试</title>
</HEAD>
<BODY>
<input type="hidden" value="${modes }" id="modes_2">
<input type="hidden" value="${smoneys }" id="smoneys">
<input type="hidden" value="${cis }" id="cis">
<input type="hidden" value="${rows}" id="rows">
<input type="hidden" id="startbid" value="${startBetSession.betSessionId }">
<input type="hidden" id="endbid" value="${endBetSession.betSessionId }">


<SCRIPT language=javascript type=text/javascript>

var MODES    =document.getElementById("modes_2").value;
var SMONEYS  =document.getElementById("smoneys").value;
var CIS      =document.getElementById("cis").value;
var ROWS     =document.getElementById("rows").value;

var MODES_Array   = MODES.split(",");
var SMONEYS_Array = SMONEYS.split(",");
var CIS_Array     = CIS.split(",");

var sum =0;

//输入数值的时候进行判断
function inputsb(val,num)
{
    var regex=/^[1-9]\d{0,}$/;
    var oldsum,sum,thismoney,sm;
    sum = 0;
    
    for (loop = 0 ; loop < 28 ; loop++)
    {
        sm = document.getElementsByName("SMONEY")[loop].value;
        if(sm==null||sm>100000000||sm=="")
        {
            sm = 0;
            document.getElementsByName("SMONEY")[loop].value =0;

        }
        sum = sum + parseInt(sm);
    }
    document.getElementById("SMONEYSUM").value = sum;
    document.getElementById("SMONEYSUM2").value = sum ; 
}

 
//不同的模式不同的抽奖金额
function Change_Modes(ModesName,Modes)
{
	 document.getElementById("createNew").value=0;//取消新建
     sum =0; 
     Modes=document.getElementById("modes").value;   
     //document.getElementById('MODENAME').value =ModesName;
 
     for (loop = 0 ; loop < 28 ; loop++)
     {
       document.getElementsByName("SMONEY")[loop].value=0;
     }


     for (loop = 0 ; loop < ROWS ; loop++)
     {
        var Modes_temp   = MODES_Array[loop];    //模式
        var SMONEYS_temp = SMONEYS_Array[loop];  //抽奖金额
        var CIS_temp     = CIS_Array[loop];      //哪个数值进行了抽奖
        
        if(Modes==Modes_temp) 
        {    
            document.getElementsByName("SMONEY")[CIS_temp].value = SMONEYS_temp;   
            //总抽奖金额 
            sum = sum+parseInt(SMONEYS_temp); 
        }
      }
      //显示总的积分抽奖数
      document.getElementById("SMONEYSUM").value =sum;
      document.getElementById("SMONEYSUM2").value = sum ;
}



//加法
function AddMONEY(val)
{
    var obj = document.getElementsByName('SMONEY')[val];
    if((obj.value=="")||(obj.value==null))
    {
        document.getElementsByName('SMONEY')[val].value = "0";
    }
    var ciobj = document.getElementsByName('CI')[val];
    var SMONEYSUM = document.getElementById("SMONEYSUM") ;
    var sum = parseInt(SMONEYSUM.value) + 10 ; 
     
    if(obj.value == '0')
    {
        obj.value = 10 ;
    }
    else
    {
        obj.value = parseInt(obj.value) + 10 ;
    }
    SMONEYSUM.value = sum ; 
    document.getElementById("SMONEYSUM2").value = sum ;
    
}




//减法
function DecreaseMONEY(val)
{
    var obj = document.getElementsByName('SMONEY')[val];
    if((obj.value=="")||(obj.value==null))
    {
        document.getElementsByName('SMONEY')[val].value = "0";
    }
    
    var ciobj = document.getElementsByName('CI')[val];
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
        document.getElementById("SMONEYSUM2").value = sum ;
    }    
}

//抽奖倍数算法
function chgbbb(bbb)
{
    var abc = 0;
    var smval,cival;
    for (loop = 0 ; loop < 28 ; loop++)
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
    document.getElementById("SMONEYSUM").value = abc ;
	document.getElementById("SMONEYSUM2").value = abc ;
}

function Check()
{
    var sumgoldeggs = document.getElementById("SMONEYSUM").value;
        if(sumgoldeggs<=0){
        alert("请先抽奖");
        return false;
    }
    var detail="";
    for(var i=0;i<28;i++){
    	var num=document.getElementsByName('SMONEY')[i].value;
    	if(num!="0" && num!=""){
    		if(detail=="")detail+=i+":"+num;
    		else detail+=";"+i+":"+num;
    	}    
    }
    var title=document.getElementById("MODENAME").value;
    var bmId=document.getElementById("modes").value;
    if(document.getElementById("createNew").value=="1")bmId=0;//新建模式
    if(bmId==0){
	    if(title==""){
	    	alert("模式名称不能为空");
	    	return false;
	    }
	}
	var form1= document.getElementById("form1");
	var link="";
	//if(document.getElementById("buildNewMode").checked==true){
		var startbid=document.getElementById("startbid").value;
		var startBsId=document.getElementById("STARTISSUE").value;
		if(startBsId<startbid){
			alert("开始期数不能小于"+startbid+"期");
			return false;
		}
		var endbid=document.getElementById("endbid").value;
		var endBsId=document.getElementById("ENDISSUE").value;
		if(endBsId>endbid){
			alert("结束期数不能大于"+endbid+"期");
			return false;
		}			
	    link+="../pce/betSessionAction.do?method=saveBetMode&bmId="+bmId+"&detail="+detail+"&title="+title+"&totalNumber="+sumgoldeggs;
	    link+="&startBsId="+startBsId+"&endBsId="+endBsId;	
	//}else{
	 //   link+="../pce/betSessionAction.do?method=saveBetMode&bmId="+bmId+"&detail="+detail+"&title="+title+"&totalNumber="+sumgoldeggs;
	//}
	link += "&userId="+$("userId").value;
	//alert(link);
    form1.action=encodeURI(link); 
    form1.submit();

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
            //SMONEYS="1,3,6,10,15,21,28,36,45,56,63,68,71,77,77,71,68,63,56,45,36,28,21,15,10,6,3,1";
            SMONEYS="1,3,6,10,15,21,28,36,45,55,63,69,73,75,75,73,69,63,55,45,36,28,21,15,10,6,3,1";
            
         }else if(modes==2)//单
         {
            //SMONEYS=",3,,10,,21,,36,,56,,68,,77,,71,,63,,45,,28,,15,,6,,1";
            SMONEYS="0,3,0,10,0,21,0,36,0,55,0,69,0,75,0,73,0,63,0,45,0,28,0,15,0,6,0,1";

         }
         else if(modes==3)//双
         {
            //SMONEYS="1,,6,,15,,28,,45,,63,,71,,77,,68,,56,,36,,21,,10,,3,";
             SMONEYS="1,0,6,0,15,0,28,0,45,0,63,0,73,0,75,0,69,0,55,0,36,0,21,0,10,0,3,0";
            
           
         }else if(modes==4)//大
         {
            //SMONEYS=",,,,,,,,,,,,,,77,71,68,63,56,45,36,28,21,15,10,6,3,1";
             SMONEYS="0,0,0,0,0,0,0,0,0,0,0,0,0,0,75,73,69,63,55,45,36,28,21,15,10,6,3,1";
           
         }else if(modes==5)//小
         {
             //SMONEYS="1,3,6,10,15,21,28,36,45,56,63,68,71,77,,,,,,,,,,,,,,";
             SMONEYS="1,3,6,10,15,21,28,36,45,55,63,69,73,75,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
            
         }else if(modes==6)//中
         {
            //SMONEYS=",,,,,,,,,,63,68,71,77,77,71,68,63,,,,,,,,,,";
             SMONEYS="0,0,0,0,0,0,0,0,0,0,63,69,73,75,75,73,69,63,0,0,0,0,0,0,0,0,0,0";
            
         }else if(modes==7)//边
         
         {
            //SMONEYS="1,3,6,10,15,21,28,36,45,56,63,,,,,,,63,56,45,36,28,21,15,10,6,3,1";
             SMONEYS="1,3,6,10,15,21,28,36,45,55,0,0,0,0,0,0,0,0,55,45,36,28,21,15,10,6,3,1";
            
         }
    
        var SMONEYS_Array = SMONEYS.split(",");
        var SMONEYS_temp
        for (loop = 0 ; loop < 28 ; loop++)
        {

            SMONEYS_temp = SMONEYS_Array[loop];  
            document.getElementsByName("SMONEY")[loop].value = SMONEYS_temp;//抽奖金额
            sum = sum+parseInt(SMONEYS_temp);  //总抽奖金额

        }
        document.getElementById("SMONEYSUM").value = sum;
        document.getElementById("SMONEYSUM2").value = sum;
    }

//function ModeSave()
//{
// form1.target="_blank";
// form1.action="/play/pg28modesave.aspx";
//}
function DeleteMode()
{
	var bmId=document.getElementById("modes").value;
	if(bmId!="" && bmId!="0"){
	  if(window.confirm("是否确认删除您当前的抽奖模式？"))location.href="<%=request.getContextPath()%>/pce/betSessionAction.do?method=deleteMode&bmId="+bmId+"&userId="+$("userId").value+"&t="+TLT.getTime(); 	
	}
}
</SCRIPT>

<input type="hidden" value="0" id="createNew">

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
					<div class="bqc_on">自动抽奖</div>
					<!-- <div onmouseout="this.className='bqc_of';" onmouseover="this.className='bqc_of_mo';" onclick="javascript:window.location.href='betSessionAction.do?method=getBestEggsBet';" class="bqc_of">排行榜</div>
					<div class="l bqc_end"></div>
					<div class="l mt5 ml12">
						<a href="javascript:convertEggs();" class="sl">兑换积分</a>
						<span class="cd">|</span>
						<a target="_blank" href="../pce/help/game.htm" class="sl">游戏说明</a>
					</div> -->
				</div>
				
				<div class="mleft_">
					<div class="p0_10"><div class="tsk2">你可以创建一个抽奖模式，系统会自动为你抽奖(有效期一天)，如果你手动抽奖后，自动抽奖将会取消！</div></div>
					<div class="p5_10">
						
						<div id="single-column">
						
						<FORM id=form1 onSubmit="return Check()" action="" method=post>
						<input type="hidden" name="userId" id="userId" value="<bean:write name="userId"/>"/>
						
						<!-- 
						<DIV class=center>
						<DIV class=title>
						<SPAN><A href="<%//=request.getContextPath() %>/pce/betSessionAction.do?method=preSaveBetModel&userId=<bean:write name="userId"/>">抽奖模式编辑</A></SPAN> 
						<SPAN><A href="<%//=request.getContextPath() %>/pce/betSessionAction.do?method=preUseMode&userId=<bean:write name="userId"/>">自动抽奖设置</A></SPAN> 
						</DIV>
						</DIV>
						 -->
						
						<DIV class=center>
						<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list" width="100%">
						  <TBODY>
						  <TR>
						    <TD class=touzhu align=left bgColor=#f4f4f4 height=""><STRONG class=a5>抽奖模式：</STRONG>
						    
						    <SELECT id=modes onchange=Change_Modes(this.options[this.selectedIndex].text,this.selectedIndex) name=modes>
						    <logic:notEmpty name="list">     
						      <logic:iterate id="md" name="list">
						      	<OPTION value=${md.betModeId }>${md.title }</OPTION>  
						      </logic:iterate>
						     </logic:notEmpty>
						    </SELECT>
						        <a class="sl" href="javascript:void(0)" onclick="DeleteMode()">删除当前模式</a>
						        <span class="cd">|</span>
						        <a class="sl" href="javascript:void(0)" onclick="preCreateNewMode()">新建抽奖模式</a>
						    </TD>
						    <td align="right" bgColor=#f4f4f4>
						    	<logic:notEmpty name="betMode">
								<div class="p5_0 red tar">当前已启用自动投模式：<strong>${betMode.title}</strong>
								<input style="margin-left:15px;" type="button" value="取消自动抽奖" onclick="if(confirm('确认取消自动抽奖？')){location.href='betSessionAction.do?method=cancelAutoBet';}"/>
								</div>
								</logic:notEmpty>
								<logic:empty name="betMode">
								<div class="p5_0 gray tar">自动抽奖模式未启用</div>
								</logic:empty>
						    </td>
						  </TR>
						  <TR id="newModeName" style="display:none">
						    <TD class=touzhu align=left bgColor=#f4f4f4><STRONG  class=a5>模式名称：</STRONG>
						    <INPUT class=input2 id=MODENAME maxLength=8 value="" name=MODENAME>
						    <script type="text/javascript">
						    	function setMode(chk){
						    		if(chk.checked==true){
						    			document.getElementById("divMode").style.display="";
						    		}else{
						    			document.getElementById("divMode").style.display="none";
						    		}
						    	}
						    	
						    	function preCreateNewMode(){
						    	
								       for (loop = 0 ; loop < 28 ; loop++)
								     {
								       document.getElementsByName("SMONEY")[loop].value=0;
								     }
								     document.getElementById("newModeName").style.display="";
								     document.getElementById("MODENAME").value="";
								     document.getElementById("MODENAME").focus();
								     document.getElementById("SMONEYSUM").value = 0;
						             document.getElementById("SMONEYSUM2").value = 0;
						             document.getElementById("createNew").value ="1";
								     
						    	}
						    	
						    </script>
						    
						    
						    
						  </TD>
						  <td align="right" bgColor=#f4f4f4></td>
						  </TR>
						  
						  </TBODY>
						  </TABLE>
						<div class="pce-mode" style="margin:10px 0 5px 0;">
							 <div><strong>开始期数：</strong>
							 	<INPUT class=input0 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" id=STARTISSUE  onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" maxLength=10 value="${startBetSession.betSessionId }" name=STARTISSUE id="STARTISSUE"> 开始期数须大于等于<SPAN class=STYLE3>${startBetSession.betSessionId }</SPAN>
							 </div>
							 <div><strong>结束期数：</strong>
							 	<INPUT class=input0 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" id=ENDISSUE onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" maxLength=10 value="${endBetSession.betSessionId }" name=ENDISSUE id="ENDISSUE"> 结束期数须小于等于<SPAN class=STYLE3>${endBetSession.betSessionId }</SPAN>
							 </div>
						</div>  
						<div class="pce-mode">
							<strong>标准抽奖模式：</strong>
						    <a class="sl" href="#" onclick="FirstModes(1)">全包</a>
						    <a class="sl" href="#" onclick="FirstModes(2)">单</a>
						    <a class="sl" href="#" onclick="FirstModes(3)">双</a>
						    <a class="sl" href="#" onclick="FirstModes(4)">大</a>
						    <a class="sl" href="#" onclick="FirstModes(5)">小</a>
						    <a class="sl" href="#" onclick="FirstModes(6)">中</a>
						    <a class="sl" href="#" onclick="FirstModes(7)">边</a>
							
							<INPUT id="b05" onclick="chgbbb(0.5);" type="button" value="0.5倍" name="b05"> 
							<INPUT id="b08" onclick="chgbbb(0.8);" type="button" value="0.8倍" name="b08">
							<INPUT id="b12" onclick="chgbbb(1.2);" type="button" value="1.2倍" name="b12"> 
							<INPUT id="b15" onclick="chgbbb(1.5);" type="button" value="1.5倍" name="b15"> 
							<INPUT id="b20" onclick="chgbbb(2);" type="button" value="2倍" name="b20">
							<INPUT id="Button1" onclick="chgbbb(10);" type="button" value="10倍" name="b20"> 
						    <INPUT class="input1" id="SMONEYSUM2" readOnly="readonly" value="0" name="SMONEYSUM2" style="width: 5em;">
						    <INPUT type="button" onclick="Check()" value="保存自动抽奖模式" id="submit1">
						</div>
						<TABLE align="center" border="0" cellpadding="0" cellspacing="1" class="pce-tbl">
						  <TBODY>
						  
						  <TR>
						    <TD align=middle width="34%" 
						    background=<%=request.getContextPath()%>/images/bg.gif bgColor=#ffffff 
						    height=28>预测结果</TD>
						    <TD align=middle width="66%" 
						    background=<%=request.getContextPath()%>/images/bg.gif 
						    bgColor=#ffffff>抽奖</TD></TR>
						    
						   <%for(int i=0;i<28;i++){ %>
						     <TR>
						    <TD align=middle bgColor=#ffffff height=30><IMG src="<%=request.getContextPath()%>/images/number_<%=i %>.gif"></TD>
						    <TD align=middle bgColor=#ffffff><INPUT class=input2 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" 
						      onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');inputsb(this.value,<%=i %>);" 
						      size=15 value=0 name=SMONEY> 
						      <INPUT class=input3 onclick=AddMONEY(<%=i %>); type=button value=+ name=Add> 
						      <INPUT class=input3 onclick=DecreaseMONEY(<%=i %>); type=button value=- name=Decrease> 
						    </TD></TR>
						   <%} %>    
						 
						  <TR>
						    <TD align=center bgColor=#f4f4f4 colSpan=4 height=30><SPAN 
						      class=STYLE1>我的总抽奖额：</SPAN> <INPUT class=input1 id=SMONEYSUM readOnly 
						      value=0 name=SMONEYSUM></TD></TR>
						  <TR>
						    <TD align=center bgColor=#ffffff colSpan=4 height=38><INPUT id=submit2 onclick=Check() type=button value="保存自动抽奖模式" name=submit2>&nbsp;&nbsp;&nbsp;&nbsp;</TD></TR></TBODY></TABLE><!--内容结束--></DIV>
						<TABLE align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
						  <TBODY>
						  <TR>
						    <TD><IMG height=11 
						      src="<%=request.getContextPath()%>/images/YIndexPageBottom.gif" 
						  width=800></TD></TR></TBODY></TABLE>
						</FORM>
						<SCRIPT language=javascript type=text/javascript>
						//默认状态应该选择第一个抽奖模式
						
						Change_Modes('${initMode.title}','${initMode.betModeId}')
						</SCRIPT>
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

</BODY>
</HTML>
