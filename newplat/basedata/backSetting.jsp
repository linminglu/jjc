<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>打码返水设置</title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<script language="javascript">
function xmlHttpRequest(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp");
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest();
	}
}
</script>
</head>
<body>
<logic:present name="loginName_exist" scope="request">
	<script language="javascript">
		alert("该用户名已经被使用，请该用其它用户名");
	</script>
</logic:present>
<html:form action="/lotterySettingAction.do?method=saveBack" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden property="lotterySetting.lsId" name="lotterySettingForm"/>
<html:hidden property="rl1.rid" name="lotterySettingForm" />
<html:hidden property="rl2.rid" name="lotterySettingForm" />
<html:hidden property="rl1.lsId" name="lotterySettingForm" />
<html:hidden property="rl2.lsId" name="lotterySettingForm" />
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">返水时间(整数点)</th>
		<td>
        	<span><html:text property="startDate" name="lotterySettingForm" styleClass="Wdate" styleId="startTime"   onclick="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})" readonly="readonly"/></span>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">打码金额</th>
      	<td>
			<html:text property="rl2.rechargeMinMoney" name="lotterySettingForm" />-<html:text property="rl2.rechargeMaxMoney" name="lotterySettingForm" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">打码返水数值</th>
      	<td>
       		<html:text property="rl2.fixedMoney" name="lotterySettingForm" /> &nbsp;%
      	</td>
    </tr>
    </tr>
    	<tr class="tr-color-body">
      	<th align="right">打码返水开关</th>
      	<td>
      		<html:radio property="lotterySetting.status" name="lotterySettingForm" value="1">
      			<label for="gender1">开</label> 
      		</html:radio>
      		<html:radio property="lotterySetting.status" name="lotterySettingForm" value="0">
      			<label for="gender2">关</label> 
      		</html:radio>
      	</td>
    </tr>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>

<logic:present name="existEmail">
  	<script language="javascript">
		alert("登录提示：<bean:write name="existEmail"/>");
	</script>
</logic:present>
</html:form>
</body>
</html:html>