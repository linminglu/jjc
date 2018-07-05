<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ram.model.User"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<logic:messagesPresent>
	<textarea name="textareaError"  cols="60" rows="4" 
		class="textareaError" readonly ><html:errors/></textarea>
</logic:messagesPresent>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file = "../common/inc_file.jsp"%>
<!-- <script language="javascript" src="../js/validate.js"></script> -->
<!-- <script language="javascript" src="../js/show.js"></script> -->
<script type="text/javascript" src="../js/platjs.js"></script>
<script type="text/javascript">
function xmlhttpReq(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp")
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest()
	}
}
// function $(a){
// 	return document.getElementById(a)
// }

function formSubmit(){
	var frm = document.managerForm;
	var userType = frm["type"].value;
	if(frm["userlimit.loginName"].value.trim().length==0){
		alert("请输入登录名");
		frm["loginName"].focus();
		return false;
	}
	if(frm["userlimit.betMoney"].value.trim().length==0){
		alert("请输入打码量限制");
		frm["userlimit.betMoney"].focus();
		return false;
	}
	if(parseFloat(frm["userlimit.betMoney"].value.trim())<100){
		alert("打码量至少要1倍（100%）");
		frm["userlimit.betMoney"].focus();
		return false;
	}
	
	if(frm["userlimit.betMoney"].value.length==0
			&&frm["userlimit.cashTip"].value.length==0
			&&frm["userlimit.cashMessage"].value.length==0){
		alert("打码量限制、提示语、短信验证至少要选择一个");
		return false;
	}
 	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}


function findUser(){
	var frm = document.managerForm;
	var loginName = frm["userlimit.loginName"].value.trim();
	if(loginName.length==0){
		alert("请输入用户的登录名");
		frm["userlimit.loginName"].focus();
		return false;
	}
	$.post("/user/userAction.do?method=findUserByIdOrName&loginName="+ loginName , 
		function(data) {
			data = $.trim(data);
			var result = JSON.parse(data);
			if (result.flag == "success") {
				alert("用户存在！");
			}else{
				alert("用户不存在！请重新输入！");
				$(".tip").text(result.notExistName+"不存在");
// 				$('#receiver').html('输入用户名：<input type="text" name="message.loginName" maxlength="20" value="" style="width: 200px;">');
				frm["userlimit.loginName"].focus();
			}
	});	
}

</script>
</head>
<body>
<div class="bread">用户限制管理&raquo; 增加</div>
<html:form  action="managerAction.do?method=userLimitAdd" focus="loginName" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden property="userlimit.id" name="managerForm" />
<%-- <input type="hidden" value="<c:out value='${userType}'/>" name="userType"/> --%>
<html:hidden property="type" name="managerForm"/>

<table width="100%" class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body highly">
		<th align="right" class="width130">用户ID/手机号</th>
      	<td>
        	<html:text property="userlimit.loginName" name="managerForm"/>
        	<a class="button" onclick="findUser();" href="javascript:void(0);">确认用户</a>
        	</br>
        	<span class="promat" style="color: red;">（可以填多个用户ID/手机号，用,隔开，例如 23352,13311111111）</span>
      		<span class="tip" style="color: red;"></span>
      	</td>
    </tr>
	<tr class="tr-color-body highly">
		<th align="right">提款所需打码量(%)</th>
      	<td>
        	<html:text property="userlimit.betMoney" name="managerForm"/>
        	</br>
        	<span class="promat" style="color: red;">（100为1倍打码，200为2倍打码）</span>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">提款提示语</th>
      	<td>
        	<html:text property="userlimit.cashTip" name="managerForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">提款短信验证</th> 
      	<td>
      		<html:radio property="userlimit.cashMessage" name="managerForm" value="1" styleId="gender1" >
      			<label for="gender1">有</label> 
      		</html:radio>
      		<html:radio property="userlimit.cashMessage" name="managerForm" value="0" styleId="gender2">
      			<label for="gender2">无</label> 
      		</html:radio>
      	</td>
    </tr>
    
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
</html:html>