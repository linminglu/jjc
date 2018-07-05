<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_weditor.jsp"%>
<script language="javascript">
function formSubmit(){
	var frm = document.messageForm;
	if(frm["message.title"].value.trim().length==0){
		alert("请输入标题");
		frm["message.title"].focus();
		return false;
	}
	if(frm["message.content"].value.trim().length==0){
		alert("请输入内容");
		frm["message.content"].focus();
		return false;
	}
	var loginName = frm["message.loginName"].value.trim();
	if(loginName.length==0){
		alert("请输入接收者");
		frm["message.loginName"].focus();
		return false;
	}
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
function findUser(){
	var frm = document.messageForm;
	var loginName = frm["message.loginName"].value.trim();
	if(loginName.length==0){
		alert("请输入用户的登录名");
		frm["message.loginName"].focus();
		return false;
	}
	$.post("messageAction.do?method=findUser&loginName="+ loginName , 
		function(data) {
			data = $.trim(data);
			if (data == "success") {
				alert("用户存在！");
			}else{
				alert("用户不存在！请重新输入！");
				$('#receiver').html('输入用户名：<html:text name="messageForm" property="message.loginName" maxlength="20" style="width: 200px;"/>');
				frm["message.loginName"].focus();
			}
	});	
}
</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">新增消息 &raquo; 
</div>
<html:form action="/messageAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<html:text property="message.title" name="messageForm" maxlength="20" style="width: 540px;"/>
      	</td>
    </tr>
   	<tr class="tr-color-body">
      	<th align="right">内容</th>
      	<td>
      	  <html:textarea name="messageForm" property="message.content" cols="73" rows="8" />
      	</td>
    </tr>
   	<tr class="tr-color-body" >
      	<th align="right">接收者</th>
      	<td>
          <span id="receiver">输入用户名：
      	      <html:text name="messageForm" property="message.loginName" maxlength="20" style="width: 200px;"/>
      	  </span>
		  <a class="button" onclick="findUser();" href="javascript:void(0);">确认用户</a>
          <p class="red">发给所有人输入：所有人  ；发给个人则输入：个人的登陆名，点击确认用户验证用户是否存在</p>
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