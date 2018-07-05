<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<!-- <script language="javascript" src="../js/validate.js"></script> -->
<!-- <script language="javascript" src="../js/show.js"></script> -->
<script language="javascript" src="../js/platjs.js"></script>
<script type="text/javascript" src="<%=webContext %>/js/js_z/md5.js"></script>
<script language="javascript">
function xmlHttpRequest(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp");
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest();
	}
}
function initialize(){
	if(confirm("确认初化密码？")){
		var pwd = document.managerForm["user.password"];
		
		var doc = xmlHttpRequest();
		var url = "managerAction.do?method=initializePassword&&userId=<bean:write name='managerForm' property='user.userId'/>";
		
		doc.open("GET",url,true);
		doc.onreadystatechange=function(){callback(pwd,doc)};
	 	doc.send(null);
	}
}
function callback(pwd,doc){
	if( doc.readyState == 4 ){
		if( doc.status == 200 ){
			pwd.value = doc.responseText;
			alert( "初始化成功" );
		}else{
			alert( "初始化失败" + doc.status );
		}
	}
}
function formSubmit(){
	var frm = document.managerForm;
	var userType = frm["user.userType"].value;
	if(frm["user.loginName"].value.trim().length==0){
		alert("请输入登录名");
		frm["user.loginName"].focus();
		return false;
	}
	
	if(frm["user.password"].value.length==0){
		alert("请输入登录密码");
		frm["user.password"].focus();
		return false;
	} 
	
 	if(frm["user.userName"].value.length==0){
 		alert("请输入昵称");
 		frm["user.userName"].focus();
 		return false;
 	}
	
 	if(frm["user.gender"].value.length==0){
 		alert("请选择性别");
 		return false;
 	}
 	
 	if(frm["user.cellPhone"].value.length==0){
 		alert("请输入手机号");
 		frm["user.cellPhone"].focus();
 		return false;
 	}
 	
 	if(frm["user.address"].value.length==0){
 		alert("请输入地址");
 		frm["user.address"].focus();
 		return false;
 	}
 	
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

</script>
</head>
<body>
<div class="bread">用户信息  &raquo; 修改</div>
<logic:present name="loginName_exist" scope="request">
	<script language="javascript">
		alert("该用户名已经被使用，请该用其它用户名");
	</script>
</logic:present>
<html:form action="/managerAction.do?method=loginUserSave" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="managerForm" property="user.userId"/>
<html:hidden property="user.userType" name="managerForm"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">登录名</th>
		<td>
        	<html:text name="managerForm" property="user.loginName" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">密码</th>
      	<td>
      		<html:password name="managerForm" property="user.password" />
      		<!-- <input type="button" onclick="initialize()"  class="button"  value="初始密码"/>
      		<span class="span-tip">初始密码：888888</span> -->
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">昵称</th>
      	<td>
       		<html:text property="user.userName" name="managerForm" maxlength="50"/>
      	</td>
    </tr>

    	<tr class="tr-color-body">
      	<th align="right">性别</th>
      	<td>
      		<html:radio property="user.gender" value="1" styleId="gender1" >
      			<label for="gender1">男</label> 
      		</html:radio>
      		<html:radio property="user.gender" value="2" styleId="gender2">
      			<label for="gender2">女</label> 
      		</html:radio>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">手机号</th>
      	<td>
        	<html:text property="user.cellPhone" name="managerForm" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"/>
      	</td>
    </tr>
    
        <tr class="tr-color-body">
      	<th align="right">地址</th>
      	<td>
       		<html:text property="user.address" name="managerForm" maxlength="100" />
      	</td>
    </tr>
   
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>

<iframe width=174 height=189 name="gToday:normal:agenda.js" id="gToday:normal:agenda.js" src="<c:url value="/js/ipopeng.htm"/>" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;">
</iframe>
<logic:present name="existEmail">
  	<script language="javascript">
		alert("登录提示：<bean:write name="existEmail"/>");
	</script>
</logic:present>
</html:form>
</body>
</html:html>