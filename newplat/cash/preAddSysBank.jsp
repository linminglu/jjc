<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_ueditor.jsp"%>
<script language="javascript">
function loadPage(){
	var err = $("#errors");
	if(err && err.val()!=""){
		var errorCode= err.val();
		if(errorCode=="NOT_SUBMIT_FORM"){
			alert("不能重复提交表单！");
		}
	}
}
window.onload = function(){
	loadPage();
};
function formSubmit(){
	var frm = document.cashForm;
		if(frm["sysBank.userName"].value == ""){
			alert("请输入姓名！");
		  frm["sysBank.userName"].focus();
			return false;
		}
		if(frm["sysBank.bankName"].value == ""){
			alert("请输入开户行名称！");
		  frm["sysBank.bankName"].focus();
			return false;
		}
		if(frm["sysBank.bankAccount"].value == ""){
			alert("请输入银行账号！");
		  frm["sysBank.bankAccount"].focus();
			return false;
		}
		if(frm["sysBank.country"].value == ""){
			alert("请输入国家！");
		  frm["sysBank.country"].focus();
			return false;
		}
		
	var tijiao=document.getElementById("tijiao");
    var back = document.getElementById("back");
    var infoObj = document.getElementById("info");
    infoObj.style.display = 'block'; 
    back.disabled = true;
	tijiao.disabled = true;
}

</script>
</head>
<body>

<div class="bread">账户管理  &raquo; 添加账户  &raquo;
<c:if test="${action eq 'add'}">新增</c:if>
<c:if test="${action eq 'modif'}">修改</c:if>
</div>
<html:form action="/cashAction.do?method=saveBank" onsubmit="return formSubmit();"  enctype="multipart/form-data">
<html:hidden name="cashForm" property="sysBank.bankId"/>
<html:hidden name="cashForm" property="sysBank.status"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">银行</th>
		<td>
			<html:text property="sysBank.bankName" name="cashForm"/>
		</td>
	</tr>
    <tr class="tr-color-body">
		<th align="center">账号</th>
		<td>
		    <html:text property="sysBank.bankAccount" name="cashForm"></html:text>
		</td>
	</tr>
	<tr class="tr-color-body">
		<th align="center">姓名</th>
		<td>
		    <html:text property="sysBank.userName" name="cashForm"></html:text>
		</td>
	</tr>
	<tr class="tr-color-body">
		<th align="center">国家</th>
		<td>
		    <html:text property="sysBank.country" name="cashForm"></html:text>
		</td>
	</tr>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定" id ='tijiao'/>
    <input type="button" class="gbutton" id ='back' onclick="javascript:window.history.go(-1)" value="返回"/>
    <span id="info" style="display:none;color:red;font-size:14px;font-weight:bold;margin-top:10px;">正在上传，请稍等...</span>
    
</div>
</html:form>
</body>
</html:html>
