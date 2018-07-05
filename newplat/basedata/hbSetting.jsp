<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>红包设置</title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<script language="javascript">
function formSubmit(){
	var frm = document.lotterySettingForm;
	if(frm["lotterySetting.status"].checked==false){
 		alert("请选择开关");
 		return false;
 	}
	if(frm["startDate"].value.trim().length==0){
 		alert("请选择开始时间");
 		return false;
 	}
	if(frm["endDate"].value.trim().length==0){
 		alert("请选择结束时间");
 		return false;
 	}
	if(frm["lotterySetting.money"].value.trim().length==0){
 		alert("请输入最小金币数");
 		return false;
 	}
	var vl1Type=frm["rl1.type"].value;
	if(vl1Type=='0'){
		if(frm["rl1.fixedMoney"].value.trim().length==0){
	 		alert("请输入红包金额");
	 		frm["rl1.fixedMoney"].focus();
	 		return false;
	 	}
	}else if(vl1Type=='1'){
		if(frm["rl1.minMoney"].value.trim().length==0){
	 		alert("请输入最小金额");
	 		frm["rl1.minMoney"].focus();
	 		return false;
	 	}
		if(frm["rl1.maxMoney"].value.trim().length==0){
	 		alert("请输入最大金额");
	 		frm["rl1.maxMoney"].focus();
	 		return false;
	 	}
	}
	
	if(frm["rl1.num"].value.trim().length==0){
 		alert("请输入红包金额");
 		frm["rl1.num"].focus();
 		return false;
 	}
	
	if(frm["rl1.rechargeMinMoney"].value.trim().length==0){
 		alert("请输入最小充值金额");
 		frm["rl1.rechargeMinMoney"].focus();
 		return false;
 	}
	if(frm["rl1.rechargeMaxMoney"].value.trim().length==0){
 		alert("请输入最大充值金额");
 		frm["rl1.rechargeMaxMoney"].focus();
 		return false;
 	}
	
	var vl2Type=frm["rl2.type"].value;
	if(vl2Type=='0'){
		if(frm["rl2.fixedMoney"].value.trim().length==0){
	 		alert("请输入红包金额");
	 		frm["rl2.fixedMoney"].focus();
	 		return false;
	 	}
	}else if(vl2Type=='1'){
		if(frm["rl2.minMoney"].value.trim().length==0){
	 		alert("请输入最小金额");
	 		frm["rl2.minMoney"].focus();
	 		return false;
	 	}
		if(frm["rl2.maxMoney"].value.trim().length==0){
	 		alert("请输入最大金额");
	 		frm["rl2.maxMoney"].focus();
	 		return false;
	 	}
	}
	
	if(frm["rl2.num"].value.trim().length==0){
 		alert("请输入红包金额");
 		frm["rl2.num"].focus();
 		return false;
 	}
	
	if(frm["rl2.rechargeMinMoney"].value.trim().length==0){
 		alert("请输入最小充值金额");
 		frm["rl2.rechargeMinMoney"].focus();
 		return false;
 	}
	if(frm["rl2.rechargeMaxMoney"].value.trim().length==0){
 		alert("请输入最大充值金额");
 		frm["rl2.rechargeMaxMoney"].focus();
 		return false;
 	}
	
	var vl3Type=frm["rl3.type"].value;
	if(vl3Type=='0'){
		if(frm["rl3.fixedMoney"].value.trim().length==0){
	 		alert("请输入红包金额");
	 		frm["rl3.fixedMoney"].focus();
	 		return false;
	 	}
	}else if(vl3Type=='1'){
		if(frm["rl3.minMoney"].value.trim().length==0){
	 		alert("请输入最小金额");
	 		frm["rl3.minMoney"].focus();
	 		return false;
	 	}
		if(frm["rl3.maxMoney"].value.trim().length==0){
	 		alert("请输入最大金额");
	 		frm["rl3.maxMoney"].focus();
	 		return false;
	 	}
	}
	
	if(frm["rl3.num"].value.trim().length==0){
 		alert("请输入红包金额");
 		frm["rl3.num"].focus();
 		return false;
 	}
	
	if(frm["rl3.rechargeMinMoney"].value.trim().length==0){
 		alert("请输入最小充值金额");
 		frm["rl3.rechargeMinMoney"].focus();
 		return false;
 	}
	if(frm["rl3.rechargeMaxMoney"].value.trim().length==0){
 		alert("请输入最大充值金额");
 		frm["rl3.rechargeMaxMoney"].focus();
 		return false;
 	}
	
	return true;
}

function typeshow(val,t){
	if(val=='0'){//固定红包
		$('#redmoney'+t).show();
		$('#minMoney'+t).hide();
		$('#maxMoney'+t).hide();
	}else if(val=='1'){//随机红包
		$('#redmoney'+t).hide();
		$('#minMoney'+t).show();
		$('#maxMoney'+t).show();
	}
}


function loadPage(){
	$('.typeClass').each(function(i){
		typeshow($(this).val(),i+1);
	 });
	
	var err = document.getElementById("errors");
	if(err && err.value!=""){
		alert(err.value);
	}
}
window.onload = function(){
	loadPage();
};
</script>
<style type="text/css">
.tblist td{padding:9px 6px ;border-right:1px solid #ddd;border-bottom:1px solid #ddd;text-align:left;}
input[type='text'], .tblist .moon td select {border: 1px solid #bbb;-moz-border-radius: 3px;-webkit-border-radius: 3px;border-radius: 3px;height: 25px;}
.tblist td input, .tblist th input, .tblist td .select { margin: 0 5px 0 0;}

.settingcard-box{overflow: hidden;}
.settingcard{width: 260px;box-shadow: 0px 0px 15px #eee;margin: 10px;border-radius:10px;overflow: hidden;padding: 5px;float: left;min-height: 297px;}
.cardtable{}
.cardtable {width: 100%;border: 1px solid #E9EEF0;border-right: 0;border-bottom: 0;color1: #4C748C;border-collapse: collapse;}
.cardtable .title td{padding: 8px 0;}
.cardtable td {border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;text-align: center;}
.cardtable td input{width: 90%;text-align: center;margin: 5px auto;}
.cardtable td select{width: 90%;text-align: center;margin: 5px auto;}
.minMoneytd,.maxMoneytd{display: none;}
</style>
</head>
<body>
<div class="bread">活动管理&raquo;红包设置</div>
<input type="hidden" name="errors" id="errors" value="<c:out value="${msg}"/>" />
<html:form action="/lotterySettingAction.do?method=savehb" onsubmit="return formSubmit();">
<div class="settingbox">
	<table class="tblist" cellpadding="0" cellspacing="0" border="0">
		<tr class="">
			<td>
				开关：<html:radio property="lotterySetting.status" name="lotterySettingForm" value="1"  styleId="status1"><label for="status1">开</label></html:radio>
      				<html:radio property="lotterySetting.status" name="lotterySettingForm" value="0"  styleId="status0"><label for="status0">关</label></html:radio>
			</td>
		</tr>
		<tr class="">
			<td>
				<span>开始时间：<html:text property="startDate" name="lotterySettingForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			</td>
		</tr>
		<tr class="">
			<td>
				<span>结束时间：<html:text property="endDate" name="lotterySettingForm" styleClass="Wdate" styleId="endDate"  onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/></span>
			</td>
		</tr>
		<tr class="">
			<td>
				<span>最小金币数：<html:text property="lotterySetting.money" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')"/></span>
			</td>
		</tr>
	</table>
</div>

<div class="settingcard-box">
	<div class="settingcard">
		<table class="cardtable">
			<tr class="title">
				<td colspan="2">红包1
					<html:hidden property="rl1.rid" name="lotterySettingForm" />
					<html:hidden property="rl1.lsId" name="lotterySettingForm" />
				</td>
			</tr>
			<tr>
				<td>红包类型</td>
				<td>
					<html:select property="rl1.type" name="lotterySettingForm" onchange="typeshow(this.value,1);" styleClass="typeClass">
						<html:option value="0">固定值</html:option>
						<html:option value="1">随机值</html:option>
					</html:select>
				</td>
			</tr>
			<tr id="redmoney1">
				<td>红包金额</td>
				<td><html:text property="rl1.fixedMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="minMoney1" class="minMoneytd">
				<td>最小金额</td>
				<td><html:text property="rl1.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="maxMoney1" class="maxMoneytd">
				<td>最大金额</td>
				<td><html:text property="rl1.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr>
				<td>红包数量</td>
				<td><html:text property="rl1.num" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text property="rl1.rechargeMinMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text property="rl1.rechargeMaxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
		</table>
	</div>
	<div class="settingcard">
		<table class="cardtable">
			<tr class="title">
				<td colspan="2">红包2
					<html:hidden property="rl2.rid" name="lotterySettingForm" />
					<html:hidden property="rl2.lsId" name="lotterySettingForm" />
				</td>
			</tr>
			<tr>
				<td>红包类型</td>
				<td>
					<html:select property="rl2.type" name="lotterySettingForm" onchange="typeshow(this.value,2);" styleClass="typeClass">
						<html:option value="0">固定值</html:option>
						<html:option value="1">随机值</html:option>
					</html:select>
				</td>
			</tr>
			<tr id="redmoney2">
				<td>红包金额</td>
				<td><html:text property="rl2.fixedMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="minMoney2" class="minMoneytd">
				<td>最小金额</td>
				<td><html:text property="rl2.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="maxMoney2" class="maxMoneytd">
				<td>最大金额</td>
				<td><html:text property="rl2.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr>
				<td>红包数量</td>
				<td><html:text property="rl2.num" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text property="rl2.rechargeMinMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text property="rl2.rechargeMaxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
		</table>
	</div>
	<div class="settingcard">
		<table class="cardtable">
			<tr class="title">
				<td colspan="2">红包3
					<html:hidden property="rl3.rid" name="lotterySettingForm" />
					<html:hidden property="rl3.lsId" name="lotterySettingForm" />
				</td>
			</tr>
			<tr>
				<td>红包类型</td>
				<td>
					<html:select property="rl3.type" name="lotterySettingForm" onchange="typeshow(this.value,3);" styleClass="typeClass">
						<html:option value="0">固定值</html:option>
						<html:option value="1">随机值</html:option>
					</html:select>
				</td>
			</tr>
			<tr id="redmoney3">
				<td>红包金额</td>
				<td><html:text property="rl3.fixedMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="minMoney3" class="minMoneytd">
				<td>最小金额</td>
				<td><html:text property="rl3.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr id="maxMoney3" class="maxMoneytd">
				<td>最大金额</td>
				<td><html:text property="rl3.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8" /></td>
			</tr>
			<tr>
				<td>红包数量</td>
				<td><html:text property="rl3.num" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text property="rl3.rechargeMinMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text property="rl3.rechargeMaxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
		</table>
	</div>
</div>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="保存"/>
</div>
</html:form>
</body>
</html:html>