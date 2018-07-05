<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">

function saveDefPay(){
	var defPay = $('input[name="defPay"]:checked').val();
    var defPayName = "";
    if(defPay == 1){
    	defPayName = "仁信";
    }else if(defPay == 2){
    	defPayName = "闪付";
    }else{
    	defPayName = "艾米森";
    }
	if(defPay!=null&&defPay!=""&&defPay.trim()!=""){
		 if(confirm('确实要修改默认支付为：' + defPayName)){
				$.post("baseDataAction.do?method=saveDefPay&defPay="+defPay, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								location.reload();
								alert("修改成功！");	
							}else{
								alert("修改失败！");
							}
				});
		}
	}else{
		alert("请选择支付方式！");
	}
}

function DisplayMyTable(i) 
{ 
    if(i==1){ 
        document.getElementById("renXin").style.display="block";
        document.getElementById("shanFu").style.display="none"; 
        document.getElementById("amx").style.display="none"; 
        document.getElementById("btnRenXin").style.background="gray";
        document.getElementById("btnShanFu").style.background="white";
        document.getElementById("btnAmx").style.background="white";
    } else if(i==2){ 
        document.getElementById("shanFu").style.display="block"; 
        document.getElementById("renXin").style.display="none"; 
        document.getElementById("amx").style.display="none"; 
        document.getElementById("btnShanFu").style.background="gray";
        document.getElementById("btnRenXin").style.background="white";
        document.getElementById("btnAmx").style.background="white";
    } else if(i==3){
    	document.getElementById("amx").style.display="block"; 
        document.getElementById("renXin").style.display="none"; 
        document.getElementById("shanFu").style.display="none"; 
        document.getElementById("btnAmx").style.background="gray";
        document.getElementById("btnRenXin").style.background="white";
        document.getElementById("btnShanFu").style.background="white";
    }else{
    	document.getElementById("amx").style.display="none"; 
        document.getElementById("renXin").style.display="none"; 
        document.getElementById("shanFu").style.display="none"; 
    }
}

function saveRenXin(){
	document.forms[0].action = "baseDataAction.do?method=savePaySet";
	document.forms[0].submit();
}

function saveShanFu(){
	document.forms[1].action = "baseDataAction.do?method=savePaySet";
	document.forms[1].submit();
}

function saveAmx(){
	document.forms[2].action = "baseDataAction.do?method=savePaySet";
	document.forms[2].submit();
}
</script>
</head>
<body>
<div class="bread">支付设置 &raquo; 
</div>

<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">默认支付</th>
		<td>
		    <label><input name="defPay" type="radio" value="1" />仁信 </label> 
		    <label><input name="defPay" type="radio" value="2" />闪付 </label> 
		    <label><input name="defPay" type="radio" value="3" />艾米森</label>
      	    <input type="submit" class="gbutton" value="保存" onclick="saveDefPay()" />
      	</td>
    </tr>
    <tr>
		<th align="right">设置</th>
		<td>
		    <input type="button" onclick="DisplayMyTable(1)" id="btnRenXin" value="仁信"/> 
		    <input type="button" onclick="DisplayMyTable(2)" id="btnShanFu" value="闪付"/> 
		    <input type="button" onclick="DisplayMyTable(3)" id="btnAmx" value="艾米森"/> 
      	</td>
    </tr>
</table>

<html:form method="post" action="/baseDataAction.do?method=savePaySet" >
<html:hidden name="baseDataForm" property="check"/>
<html:hidden property="renxin.type" name="baseDataForm"/>
<table class="tblistadd" id="renXin" style="display: none" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">商户partner</th>
		<td>
			<html:text property="renxin.par" name="baseDataForm"/>
		</td>
	</tr>
    <tr class="tr-color-body">
		<th align="center">密钥</th>
		<td>
		    <html:text property="renxin.payKey" name="baseDataForm"/>
		</td>
	</tr>
	<tr class="tr-color-body">
		<th align="center">操作</th>
		<td>
		    <input type="submit" class="gbutton" value="保存" onclick="saveRenXin()"/>
		</td>
	</tr>
</table>
</html:form>

<html:form method="post" action="/baseDataAction.do?method=savePaySet" >
<html:hidden property="shanfu.type" name="baseDataForm"/>
<table class="tblistadd" id="shanFu" style="display: none" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">商户partner</th>
		<td>
			<html:text property="shanfu.par" name="baseDataForm"/>
		</td>
	</tr>
	<tr class="tr-color-body">
		<th align="center">终端id</th>
		<td>
		   <html:text property="shanfu.code" name="baseDataForm"/>
		</td>
	</tr>
    <tr class="tr-color-body">
		<th align="center">密钥</th>
		<td>
		    <html:text property="shanfu.payKey" name="baseDataForm"/>
		</td>
	</tr>
	<tr class="tr-color-body">
		<th align="center">操作</th>
		<td>
		    <input type="submit" class="gbutton" value="保存" onclick="saveShanFu()" />
		</td>
	</tr>
</table>
</html:form>

<html:form method="post" action="/baseDataAction.do?method=savePaySet" >
<html:hidden property="amx.type" name="baseDataForm"/>
<table class="tblistadd" id="amx" style="display: none" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">商户号</th>
		<td>
			<html:text property="amx.par" name="baseDataForm"/>
		</td>
	</tr>
	    <tr class="tr-color-body">
		<th align="center">商户标识</th>
		<td>
		   <html:text property="amx.code" name="baseDataForm"/>
		</td>
	</tr>
    <tr class="tr-color-body">
		<th align="center">密钥</th>
		<td>
		    <html:text property="amx.payKey" name="baseDataForm"/>
		</td>
	</tr>
    <tr class="tr-color-body">
		<th align="center">操作</th>
		<td>
		    <input type="submit" class="gbutton" value="保存" onclick="saveAmx()"/>
		</td>
	</tr>
</table>
</html:form>
<script language="javascript">
$(document).ready(function(){
	var tf=document.baseDataForm[0];
	var value;
	if(tf["check"].value!=null&&tf["check"].value!=""){
      value = tf["check"].value;
	}
    $("input[name='defPay'][value='"+value+"']").attr("checked",true);
});
</script>
</body>
</html:html>