<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>转盘设置</title>
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
	if(frm["setting1.lable1"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable1"].focus();
 		return false;
 	}
	if(frm["setting1.val1"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val1"].focus();
 		return false;
 	}
	if(frm["setting1.lable2"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable2"].focus();
 		return false;
 	}
	if(frm["setting1.val2"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val2"].focus();
 		return false;
 	}
	if(frm["setting1.lable3"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable3"].focus();
 		return false;
 	}
	if(frm["setting1.val3"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val3"].focus();
 		return false;
 	}
	if(frm["setting1.lable4"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable4"].focus();
 		return false;
 	}
	if(frm["setting1.val4"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val4"].focus();
 		return false;
 	}
	if(frm["setting1.lable5"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable5"].focus();
 		return false;
 	}
	if(frm["setting1.val5"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val5"].focus();
 		return false;
 	}
	if(frm["setting1.lable6"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable6"].focus();
 		return false;
 	}
	if(frm["setting1.val6"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val6"].focus();
 		return false;
 	}
	if(frm["setting1.lable7"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable7"].focus();
 		return false;
 	}
	if(frm["setting1.val7"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val7"].focus();
 		return false;
 	}
	if(frm["setting1.lable8"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable8"].focus();
 		return false;
 	}
	if(frm["setting1.val8"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val8"].focus();
 		return false;
 	}
	if(frm["setting1.lable9"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting1.lable9"].focus();
 		return false;
 	}
	if(frm["setting1.val9"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting1.val9"].focus();
 		return false;
 	}
	if(frm["setting1.minMoney"].value.trim().length==0){
 		alert("请输入最小充值额");
 		frm["setting1.minMoney"].focus();
 		return false;
 	}
	if(frm["setting1.maxMoney"].value.trim().length==0){
 		alert("请输入最大充值额");
 		frm["setting1.maxMoney"].focus();
 		return false;
 	}
	
	
	if(frm["setting2.lable1"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable1"].focus();
 		return false;
 	}
	if(frm["setting2.val1"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val1"].focus();
 		return false;
 	}
	if(frm["setting2.lable2"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable2"].focus();
 		return false;
 	}
	if(frm["setting2.val2"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val2"].focus();
 		return false;
 	}
	if(frm["setting2.lable3"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable3"].focus();
 		return false;
 	}
	if(frm["setting2.val3"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val3"].focus();
 		return false;
 	}
	if(frm["setting2.lable4"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable4"].focus();
 		return false;
 	}
	if(frm["setting2.val4"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val4"].focus();
 		return false;
 	}
	if(frm["setting2.lable5"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable5"].focus();
 		return false;
 	}
	if(frm["setting2.val5"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val5"].focus();
 		return false;
 	}
	if(frm["setting2.lable6"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable6"].focus();
 		return false;
 	}
	if(frm["setting2.val6"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val6"].focus();
 		return false;
 	}
	if(frm["setting2.lable7"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable7"].focus();
 		return false;
 	}
	if(frm["setting2.val7"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val7"].focus();
 		return false;
 	}
	if(frm["setting2.lable8"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable8"].focus();
 		return false;
 	}
	if(frm["setting2.val8"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val8"].focus();
 		return false;
 	}
	if(frm["setting2.lable9"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting2.lable9"].focus();
 		return false;
 	}
	if(frm["setting2.val9"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting2.val9"].focus();
 		return false;
 	}
	if(frm["setting2.minMoney"].value.trim().length==0){
 		alert("请输入最小充值额");
 		frm["setting2.minMoney"].focus();
 		return false;
 	}
	if(frm["setting2.maxMoney"].value.trim().length==0){
 		alert("请输入最大充值额");
 		frm["setting2.maxMoney"].focus();
 		return false;
 	}
	if(frm["setting3.lable1"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable1"].focus();
 		return false;
 	}
	if(frm["setting3.val1"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val1"].focus();
 		return false;
 	}
	if(frm["setting3.lable2"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable2"].focus();
 		return false;
 	}
	if(frm["setting3.val2"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val2"].focus();
 		return false;
 	}
	if(frm["setting3.lable3"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable3"].focus();
 		return false;
 	}
	if(frm["setting3.val3"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val3"].focus();
 		return false;
 	}
	if(frm["setting3.lable4"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable4"].focus();
 		return false;
 	}
	if(frm["setting3.val4"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val4"].focus();
 		return false;
 	}
	if(frm["setting3.lable5"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable5"].focus();
 		return false;
 	}
	if(frm["setting3.val5"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val5"].focus();
 		return false;
 	}
	if(frm["setting3.lable6"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable6"].focus();
 		return false;
 	}
	if(frm["setting3.val6"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val6"].focus();
 		return false;
 	}
	if(frm["setting3.lable7"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable7"].focus();
 		return false;
 	}
	if(frm["setting3.val7"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val7"].focus();
 		return false;
 	}
	if(frm["setting3.lable8"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable8"].focus();
 		return false;
 	}
	if(frm["setting3.val8"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val8"].focus();
 		return false;
 	}
	if(frm["setting3.lable9"].value.trim().length==0){
 		alert("请输入转盘文字");
 		frm["setting3.lable9"].focus();
 		return false;
 	}
	if(frm["setting3.val9"].value.trim().length==0){
 		alert("请输入转盘奖金");
 		frm["setting3.val9"].focus();
 		return false;
 	}
	if(frm["setting3.minMoney"].value.trim().length==0){
 		alert("请输入最小充值额");
 		frm["setting3.minMoney"].focus();
 		return false;
 	}
	if(frm["setting3.maxMoney"].value.trim().length==0){
 		alert("请输入最大充值额");
 		frm["setting3.maxMoney"].focus();
 		return false;
 	}
	
	return true;
}
function loadPage(){
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
.settingcard{width: 260px;box-shadow: 0px 0px 15px #eee;margin: 10px;border-radius:10px;overflow: hidden;padding: 5px;float: left;}
.cardtable{}
.cardtable {width: 100%;border: 1px solid #E9EEF0;border-right: 0;border-bottom: 0;color1: #4C748C;border-collapse: collapse;}
.cardtable .title td{padding: 8px 0;}
.cardtable td {border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;text-align: center;}
.cardtable td input{width: 90%;text-align: center;margin: 5px auto;}
</style>
</head>
<body>
<div class="bread">活动管理&raquo;转盘设置</div>
<input type="hidden" name="errors" id="errors" value="<c:out value="${msg}"/>" />
<html:form action="/lotterySettingAction.do?method=savezp" onsubmit="return formSubmit();">
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
				<td colspan="2">转盘1
					<html:hidden property="setting1.rlId" name="lotterySettingForm" />
				</td>
			</tr>
			<tr class="title">
				<td>转盘文字</td>
				<td>奖金</td>
			</tr>
			<tr>
				<td><html:text styleId="sl11" property="setting1.lable1" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv11" property="setting1.val1" name="lotterySettingForm"onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl12" property="setting1.lable2" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv12" property="setting1.val2" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl13" property="setting1.lable3" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv13" property="setting1.val3" name="lotterySettingForm"  onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl14" property="setting1.lable4" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv14" property="setting1.val4" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl15" property="setting1.lable5" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv15" property="setting1.val5" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')"  maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl16" property="setting1.lable6" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv16" property="setting1.val6" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl17" property="setting1.lable7" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv17" property="setting1.val7" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl18" property="setting1.lable8" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv18" property="setting1.val8" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl19" property="setting1.lable9" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv19" property="setting1.val9" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text styleId="smin1" property="setting1.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text styleId="smax2" property="setting1.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
		</table>
	</div>
	<div class="settingcard">
		<table class="cardtable">
			<tr class="title">
				<td colspan="2">转盘2
				<html:hidden property="setting2.rlId" name="lotterySettingForm" /></td>
			</tr>
			<tr class="title">
				<td>转盘文字</td>
				<td>奖金</td>
			</tr>
			<tr>
				<td><html:text styleId="sl21" property="setting2.lable1" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv21" property="setting2.val1" name="lotterySettingForm"onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl22" property="setting2.lable2" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv22" property="setting2.val2" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl23" property="setting2.lable3" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv23" property="setting2.val3" name="lotterySettingForm"  onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl24" property="setting2.lable4" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv24" property="setting2.val4" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl25" property="setting2.lable5" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv25" property="setting2.val5" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')"  maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl26" property="setting2.lable6" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv26" property="setting2.val6" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl27" property="setting1.lable7" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv27" property="setting1.val7" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl28" property="setting1.lable8" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv28" property="setting1.val8" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl29" property="setting1.lable9" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv29" property="setting1.val9" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text styleId="smin2" property="setting2.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text styleId="smax2" property="setting2.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
		</table>
	</div>
	<div class="settingcard">
		<table class="cardtable">
			<tr class="title">
				<td colspan="2">转盘3
					<html:hidden property="setting3.rlId" name="lotterySettingForm" />
				</td>
			</tr>
			<tr class="title">
				<td>转盘文字</td>
				<td>奖金</td>
			</tr>
			<tr>
				<td><html:text styleId="sl31" property="setting3.lable1" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv31" property="setting3.val1" name="lotterySettingForm"onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl32" property="setting3.lable2" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv32" property="setting3.val2" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl33" property="setting3.lable3" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv33" property="setting3.val3" name="lotterySettingForm"  onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl34" property="setting3.lable4" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv34" property="setting3.val4" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl35" property="setting3.lable5" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv35" property="setting3.val5" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')"  maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl36" property="setting3.lable6" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv36" property="setting3.val6" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl37" property="setting1.lable7" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv37" property="setting1.val7" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl38" property="setting1.lable8" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv38" property="setting1.val8" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td><html:text styleId="sl39" property="setting1.lable9" name="lotterySettingForm"/></td>
				<td><html:text styleId="sv39" property="setting1.val9" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最小充值额</td>
				<td><html:text styleId="smin3" property="setting3.minMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
			</tr>
			<tr>
				<td>最大充值额</td>
				<td><html:text styleId="smax3" property="setting3.maxMoney" name="lotterySettingForm" onkeyup="if(isNaN(value))execCommand('undo')" maxlength="8"/></td>
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