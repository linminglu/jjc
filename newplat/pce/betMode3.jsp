<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
 
<html> 
<head>
<LINK href="<%=request.getContextPath() %>/css/main.css" type=text/css rel=stylesheet>


</HEAD>
<BODY>
<%--<input type="text" value="${modes }" id="modes_2">
<input type="text" value="${smoneys }" id="smoneys">
<input type="text" value="${cis }" id="cis">
<input type="text" value="${rows}" id="rows">


--%>
<p>&nbsp;&nbsp;</p>
<input type="hidden" value="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,5,5,6,6,6,6,6,6,6,6,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7" id="modes_2">
<input type="hidden" value="1,3,6,10,15,21,28,36,45,55,63,69,73,75,75,73,69,63,55,45,36,28,21,15,10,6,3,1,3,10,21,36,55,69,75,73,63,45,28,15,6,1,1,6,15,28,45,63,73,75,69,55,36,21,10,3,75,73,69,63,55,45,36,28,21,15,10,6,3,1,1,3,6,10,15,21,28,36,45,55,63,69,73,75,63,69,73,75,75,73,69,63,1,3,6,10,15,21,28,36,45,55,55,45,36,28,21,15,10,6,3,1" id="smoneys">
<input type="hidden" value="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,1,3,5,7,9,11,13,15,17,19,21,23,25,27,0,2,4,6,8,10,12,14,16,18,20,22,24,26,14,15,16,17,18,19,20,21,22,23,24,25,26,27,0,1,2,3,4,5,6,7,8,9,10,11,12,13,10,11,12,13,14,15,16,17,0,1,2,3,4,5,6,7,8,9,18,19,20,21,22,23,24,25,26,27" id="cis">
<input type="hidden" value="112" id="rows">


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
     sum =0; 
    // Modes=document.getElementById("modes").value;   
     document.getElementById('MODENAME').value =ModesName;
 
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
    var detail="";
    for(var i=0;i<28;i++){
    	var num=document.getElementsByName('SMONEY')[i].value;
    	if(num!="0" && num!=""){
    		if(detail=="")detail+=i+":"+num;
    		else detail+=";"+i+":"+num;
    	}    
    }
    var bmId=document.getElementById("modes").value;
    if(document.getElementById("buildNewMode").checked==true)bmId=0;
    var title=document.getElementById("MODENAME").value;
    if(title==""){
    	alert("模式名称不能为空");
    	return false;
    }

    var form1= document.getElementById("form1");
    var link="<%=request.getContextPath()%>/pce/betSessionAction.do?method=saveBetMode&bmId="+bmId+"&detail="+detail+"&title="+title+"&totalNumber="+sumgoldeggs;
    alert("提交路径："+link);
    form1.action=link;  
    if(sumgoldeggs<=0){
        alert("请先抽奖");
        return false;
    }
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
	  if(window.confirm("是否确认删除您当前的抽奖模式？"))location.href="<%=request.getContextPath()%>/pce/betSessionAction.do?method=deleteMode&bmId="+bmId; 	
	}
}
</SCRIPT>
<script language="javascript">
var modeNames=["全包模式","单数模式","双数模式","大数模式","小数模式","中间模式","边数模式"];
var modeId=[1,2,3,4,5,6,7];
</script>
<FORM id=form1 onSubmit="return Check()" action="" method=post>
<DIV class=center><!--内容开始--><BR>
<TABLE cellSpacing=1 cellPadding=0 width="95%" bgColor=#ffcc66 border=0>
  <TBODY>
  <TR>
    <TD class=touzhu align=right width="23%" bgColor=#ffffff height=30>
    <STRONG class=a5>选择一个自动抽奖模式：</STRONG>
    </TD>
    <TD class=pd5 align=left width="75%" bgColor=#ffffff>
    <SELECT id=modes onchange=Change_Modes(this.options[this.selectedIndex].text,this.selectedIndex) name=modes>
    </SELECT>
    <script>
    	var obj=document.getElementById("modes");
    	for(var i=0;i<modeNames.length;i++){
    		obj.options[i]=new Option(modeNames[i],modeId[i]);
    	}    
    </script>    
    <%--
    <logic:notEmpty name="list">     
      <logic:iterate id="md" name="list">
      	<OPTION value=${md.betModeId }>${md.title }</OPTION>  
      </logic:iterate>
     </logic:notEmpty>
    --%>
   &nbsp;&nbsp;&nbsp;&nbsp;
     <SPAN class=STYLE1>总抽奖额：</SPAN><INPUT class=input1 id=SMONEYSUM2 readOnly value=0 name=SMONEYSUM2>
   </TD>
 </TR> 
 <TR>
    <TD class=touzhu align=right bgColor=#ffffff width="23%" height=60 rowspan="2">
      <STRONG class=a5>设置自动抽奖应用期数：</STRONG>
    </TD>
    <TD class=pdl10 bgColor=#ffffff>开始期数:
	    <INPUT class=input0 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))" 
	      id=STARTISSUE  onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
	       maxLength=10 value=${startBetSession.betSessionId } name=STARTISSUE> 开始期数须大于等于
	       <SPAN class=STYLE3>${startBetSession.betSessionId }</SPAN>
    </TD>
  </TR>
  <TR>
    <TD class=pdl10 bgColor=#ffffff>结束期数:
    <INPUT class=input0 
      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/\D/g,''))"  id=ENDISSUE 
      onkeyup="if(event.keyCode !=37 &amp;&amp; event.keyCode != 39) value=value.replace(/\D/g,'');" 
      maxLength=10 value=${endBetSession.betSessionId } name=ENDISSUE> 结束期数须小于等于
      <SPAN class=STYLE3>${endBetSession.betSessionId }</SPAN> 
      </TD>
  </TR>
  </TBODY>
  </TABLE>
<P>&nbsp;</P>
<TABLE cellSpacing=0 cellPadding=0 width="95%" border=0>
  <TBODY>
  <TR>
    <TD style="FONT-SIZE: 13px" align=left>&nbsp;&nbsp;<STRONG 
      class=font_red>抽奖模式的详细情况：</STRONG></TD></TR></TBODY></TABLE>
<TABLE cellSpacing=1 cellPadding=0 width="95%" bgColor=#ffb91f border=0>
  <TBODY>
    <INPUT class=input2 id=MODENAME maxLength=8 value="" type="hidden" name=MODENAME> 
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
	    </TD>
	    </TR>
   <%} %>    
 
  <TR>
    <TD align=center bgColor=#f4f4f4 colSpan=4 height=30><SPAN 
      class=STYLE1>总抽奖额：</SPAN> <INPUT class=input1 id=SMONEYSUM readOnly 
      value=0 name=SMONEYSUM>
      </TD>
  </TR>
  <TR>
    <TD align=center bgColor=#ffffff colSpan=4 height=38>
    <INPUT id=submit2 onclick=Check() type=button value="保存"　 name=submit2>&nbsp;&nbsp;&nbsp;&nbsp;
    </TD>
    </TR>
    </TBODY>
    </TABLE>
    <!--内容结束-->
    </DIV>
<TABLE cellSpacing=0 cellPadding=0 width=800 align=center border=0>
  <TBODY>
  <TR>
    <TD><IMG height=11 src="<%=request.getContextPath()%>/images/YIndexPageBottom.gif" width=800>
  </TD>
  </TR>
  </TBODY>
  </TABLE>
</FORM>
<SCRIPT language=javascript type=text/javascript>
//默认状态应该选择第一个抽奖模式
Change_Modes('全包模式','0')
</SCRIPT>
</BODY></HTML>
