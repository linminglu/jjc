<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ include file="../common/inc_pageSetting.jsp"%>
<%@ page import="com.card.model.CardGeneLog"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>礼品卡生成</title>
<%@ include file="../common/inc_file.jsp"%>

<script src="../styleset/_admin/js/common.js" type=text/javascript></script>
<SCRIPT src="../styleset/_admin/js/utils.js" type=text/javascript></SCRIPT>
<script language="javascript" src="../js/show.js"></script>
<script language="javascript" src="../js/common-new.js"></script>

<script type="text/javascript"
	src="../js/validation/validation-framework.js"></script>
<script type="text/javascript">
		function formSubmit(){
	   var form = document.forms[0];
	  if(form.elements["cardGeneLog.protocolCode"].value.trim() == ''){alert("请输入协议号！");return false;}
	  if(form.elements["cardGeneLog.batchCode"].value.trim() == ''){alert("请输入批次号！");return false;}
//	     var num=form.elements["cardGeneLog.cardAmount"].value.trim();
//	    if(isNaN(num)){  alert("充值积分必须输入数字"); if(num<=0){alert("充值积分数额必须大于0");return false;}
//	    return false;}
	  if(form.["itemId"].value== ''){alert("请选择礼品卡套餐！");return false;}
		if(confirm("确认生成一批礼品卡？")){
			UECreator.setFormatUrl();
			return true;
		}
		return false;
	}

</script>
<script language="javascript">
function xmlhttpReq(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp")
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest()
	}
}

function checkUser(a){
	
	var b = a.parentNode;
	var username = b.getElementsByTagName("input")[0].value;
	
	if( username == "" && username == '' ){
		alert( "批次不成为空" );
		return false;
	}
    if(isNaN(username.trim())){  alert("充值积分必须输入数字");return false;}
 	var doc = xmlhttpReq();
	var url = "../cardcard/cardAction.do?method=checkUser&batchCode="+username
    doc.open("GET", url, true);
 	doc.onreadystatechange=function(){callback(a,doc)};
 	doc.send(null);
}
function callback(a,doc){
  	if(doc.readyState == 4){
   		if(doc.status == 200){
    		if(doc.responseText=="yes"){
    			alert("此批次已存在");
    		}else{
    			alert("此批次可用");
    		}
   		}else{
   			alert('保存失败,请稍后在试:'+doc.status);
   		}
  	}
}
</script>
</head>


<div class="bread">管理中心 &raquo; 礼品卡生成</div>
<body onload="setListDivEvent();onloadMethod();">
	<html:form action="/cardAction.do?method=generateCardMain"
		onsubmit="return formSubmit();">

		<TABLE class="tblistadd" width="90%" align=center>
			<tr class="tr-color-body">
				<td align="right"><font color="red">*</font>协议号：</td>
				<td>&nbsp; <html:text property="cardGeneLog.protocolCode"
						maxlength="4" /></td>
			</tr>
			<tr class="tr-color-body">
				<td align="right"><font color="red">*</font>批次号：</td>
				<td>&nbsp; <html:text property="cardGeneLog.batchCode"
						maxlength="4" onblur="checkUser(this);return false" /></td>
			</tr>


			<tr id="cardAmount_id">
				<td align="right"><font color="red">*</font>礼品卡套餐：</td>
				<td>&nbsp;
					<html:select property="itemId" name="cardForm" styleClass="sele">
						<html:option value="">请选择</html:option>
						<html:options collection="cardItemList" property="itemId" labelProperty="title"/>
					</html:select>
				</td>
			</tr>
			<tr class="tr-color-body">
				<td align="right"><font color="red">*</font>生成数量：</td>
				<td>&nbsp; <html:text property="cardGeneLog.cardCnt"
						maxlength="4" /></td>
			</tr>
			<tr class="tr-color-body">
				<td align="right"><font color="red">*</font>起始有效期：</td>
				<td>&nbsp; <input type="text" name="startDate"
					readonly="readonly"
					onclick="if(self.gfPop)gfPop.fPopCalendar(document.cardForm['startDate']);return false;"
					value="<bean:write name="cardForm" property="startDate" format="yyyy-MM-dd"/>" />
				</td>
			</tr>
			<tr class="tr-color-body">
				<td align="right"><font color="red">*</font>截止有效期：</td>
				<td>&nbsp; <input type="text" name="endDate"
					readonly="readonly"
					onclick="if(self.gfPop)gfPop.fPopCalendar(document.cardForm['endDate']);return false;"
					value="<bean:write name="cardForm" property="endDate" format="yyyy-MM-dd"/>" />
				</td>
			</tr>

			<tr class="tr-color-body">
				<td align="right">备注：</td>
				<td>&nbsp; <html:textarea property="cardGeneLog.remark"
						rows="5" cols="60" /></td>
			</tr>

		</TABLE>
		<div class="div-bottom-bar">

			<input type="submit" class="gbutton" value="确定" /> <input
				type="button" class="gbutton"
				onclick="javascript:window.history.go(-1)" value="返回" />&nbsp;&nbsp;<font
				color="red">*&nbsp;&nbsp;为必填项</font>

		</div>
	</html:form>
	<iframe width="174" height="189" name="gToday:normal:agenda.js"
		id="gToday:normal:agenda.js" src="<c:url value="/js/ipopeng.htm"/>"
		scrolling="no" frameborder="0"
		style="visibility: visible; z-index: 999; position: absolute; left: -500px; top: 0px;"></iframe>
</body>
</html:html>