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

	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

</script>
</head>
<body>
<div class="bread">修改密码</div>
<logic:present name="loginName_exist" scope="request">
	<script language="javascript">
		alert("该用户名已经被使用，请该用其它用户名");
	</script>
</logic:present>
<html:form action="/managerAction.do?method=savePassword" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="managerForm" property="user.userId"/>
<html:hidden name="managerForm" property="user.password" />
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">旧密码</th>
		<td>
        	<html:password name="managerForm" property="oldPassword" />
      	</td>
    </tr>
    
   <tr class="tr-color-body">
      	<th align="right">新密码</th>
      	<td>
      		<html:password name="managerForm" property="password" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">确认密码</th>
      	<td>
      		<html:password name="managerForm" property="repassword" />
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