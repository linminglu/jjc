<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ram.BXSConstants"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>远程网络教学平台 新用户注册</title>
<%@ include file="../common/inc_css.jsp"%>
<script language="javascript" src="../js/validate.js"></script>
<script language="javascript" src="../js/show.js"></script>
<script language="javascript">
function xmlhttpReq(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp");
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest();
	}
}
function $(a){
	return document.getElementById(a);
}
function checkUser(a){
 	var username=document.registerForm["user.loginName"].value.trim();
 	if( username == "" && username == '' ){
 		alert( "用户名不能为空" );
 		return false;
 	}
 	if(checkLoginName(username)){
 		alert( "用户名只能为数字和字母" );
 		return false;
 	}
 	var doc = xmlhttpReq();
	var url = "../user/userRegistInitAction.do?method=checkUse&username="+username+"&rm="+(new Date()).getTime();

 	doc.open("GET", url, true);
	doc.onreadystatechange=function(){callback(a,doc)};
 	doc.send(null);
}
function callback(a,doc){
	if(doc.readyState == 4){
		if(doc.status == 200){
			if(doc.responseText=="yes"){
    			alert( "此用户名已存在" );
    		}else{
    			alert( "此用户可用" );
    		}
   		}else{
   			alert('保存失败,请稍后在试:'+doc.status);
   		}
  	}
}
</script>
<script language="javascript">
function validate(){
	var frm=document.registerForm;
	
	/*
	var cardType = frm.elements["learner.cardType"].value;
	var education = frm.elements["learner.education"].value;
	var nationality = frm["learner.nationality"].value;
	*/

	if(frm["user.loginName"].value=="" && frm["user.loginName"].value==''){
		alert("请输入用户名");
		frm["user.loginName"].select();
		return false;
	}
	
	if( checkLoginName( frm["user.loginName"].value )){
		alert( "用户名只能是数字和字母");
		frm["user.loginName"].select();
		return false;
	}
	
	if(frm["user.password"].value == "" && frm["user.password"].value == '' ){
		alert("请输入密码");
		frm["user.password"].select();
		return false;
	}
	
	if( frm["user.password"].value.length < 6  && frm["user.password"].value.length > 10){
		alert("请输入6-10位密码");
		frm["user.password"].select();
		return false;
	}else if( frm["user.password"].value != frm["confirmpassword"].value ){
		alert( "输入的密码不一致,请重新输入" );
		frm["confirmpassword"].select();
		return false;
		
	}
	if(frm["user.userEmail"].value.trim().length<4){
		alert("请输入有效的Email");
		frm["user.userEmail"].select();
		return false;
	}else{
		frm["user.userEmail"].value=frm["user.userEmail"].value.trim()
	}
	
	if(frm["user.userEmail"].value.trim().indexOf( "@" ) <= 0 || 
			frm["user.userEmail"].value.trim().indexOf( "@" ) >= ( frm["user.userEmail"].value.trim().length-1 )){
		alert("请输入有效的Email");
		frm["user.userEmail"].select();
		return false;
	}
	
	if(frm["user.userEmail"].value.trim().indexOf( "." ) <= 0 ){
		alert("请输入有效的Email");
		frm["user.userEmail"].select();
		return false;
	}
	
	/*
	if(frm["user.userName"].value.trim().length<1){
		alert("请输入姓名");
		frm["user.userName"].select();
		return false;
	}else{
		frm["user.userName"].value=frm["user.userName"].value.trim();
	}
	if(cardType == 0){
		alert("请选择证件类型");
		frm.elements["learner.cardType"].focus();
		return false;
	}
	if(frm["learner.idCode"].value.trim().length < 1){
		alert("请输入证件类型所对应的证件号");
		frm["learner.idCode"].select();
		return false;
	}
	
	if( cardType == 1 ){
		if( frm["learner.idCode"].value.length != 15 && frm["learner.idCode"].value.length != 18 ){
			alert("您输入的身份证号码位数不对，请重新输入证件号码");
			frm["learner.idCode"].select();
			return false;
		}
		if( frm["learner.idCode"].value.length == 15 ){
			var str = frm["learner.idCode"].value.trim().substring( 14,15 );
			if( checkSex( frm["learner.learnSex"],str ) == false ){
				return false;
			}
		}else{
			var str = frm["learner.idCode"].value.trim().substring( 16,17 );
			if( checkSex( frm["learner.learnSex"],str ) == false ){
				return false;
			}
		}
	}
	
	if( nationality == 0 ){
		alert("请选择民族");
		frm["learner.nationality"].focus();
		return false;
	}
	
	if( frm["birthday"].value == '' && frm["birthday"].value == "" ){
		alert( "请选择生日" );
		frm["birthday"].select();
		return false;
	}else if( cardType == 1 ){
		if( !checkUserBirth( frm["learner.idCode"].value.trim(),frm["birthday"].value.trim() )){
			alert("您填入的出生年月与身份证号不符");
			frm["birthday"].select();
			return false;
		}
	}else{
		var tDate = frm["birthday"].value;
		var strDate = tDate.split("-");
		var month = parseInt(strDate[1])-1;
		var birthday = new Date(strDate[0],month,strDate[2]);
		var nDate = new Date();
		if( birthday > nDate ){
			alert( "生日不能大于当前日期" );
			frm["birthday"].select();
			return false;
		}
	}
	
	
	if( checkNumber( frm["learner.mobile"].value )){
		alert("手机号必须为数字");
		frm["learner.mobile"].select();
		return false;
	}
	
	if(frm["learner.mobile"].value.trim() != '' && frm["learner.mobile"].value.trim().length!=11){
		alert("手机号位数为11位");
		frm["learner.mobile"].select();
		return false;
	}
	
	if( checkNumber( frm["sectionHome"].value )){
		alert("区号必须为数字");
		frm["sectionHome"].select();
		return false;
	}
	
	if( checkNumber( frm["learner.homePhone"].value )){
		alert("家庭电话必须为数字");
		frm["learner.homePhone"].select();
		return false;
	}
	
	if( frm["organizationId"].value == 0 ){
		alert("请选择单位名称");
		frm["organizationId"].focus();
		return false;
	}
	
	if(frm["learner.workplace"].value == "" && frm["learner.workplace"].value == ''){
		alert("请输入单位名称");
		frm["learner.workplace"].select();
		return false;
	}
	
	if(frm["learner.workplacePhone"].value == "" && frm["learner.workplacePhone"].value == '' && 
		frm["learner.mobile"].value.trim() == "" && frm["learner.mobile"].value.trim() == ''){
		alert("手机号码和单位固定电话应至少填写一项！");
		frm["learner.workplacePhone"].select();
		return false;
	}
	
	if( checkNumber( frm["sectionCode"].value )){
		alert("区号必须为数字");
		frm["sectionCode"].select();
		return false;
	}
	
	if( checkNumber( frm["learner.workplacePhone"].value )){
		alert("单位固定电话必须为数字");
		frm["learner.workplacePhone"].select();
		return false;
	}
	
	if(frm["learner.address"].value == "" && frm["learner.address"].value == ''){
		alert("请输入通讯地址");
		frm["learner.address"].select();
		return false;
	}
	
	if(frm["learner.postCode"].value.length != 6 ){
		alert("请正确输入邮政编码");
		frm["learner.postCode"].select();
		return false;
	}
	
	if( checkNumber( frm["learner.postCode"].value )){
		alert("邮政编码必须为数字");
		frm["learner.postCode"].select();
		return false;
	}
	
	if(education == 0){
		alert("请选择最高学历");
		frm.elements["learner.education"].focus();
		return false;
	}
	var provice = frm["learner.provice"].value;
	if( provice == 0 ){
		alert("请选择你所在的省份");
		frm["learner.provice"].focus();
		return false;
	}
	
	if( frm["learner.city"].value == "" && frm["learner.city"].value == '' ){
		alert( "请填写你所在的城市" );
		frm["learner.city"].select();
		return false;
	}*/
	return true;
}

function checkLoginName(loginName){
	var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	for( var i=0;i<loginName.length;i++ ){
		var name = loginName.charAt(i);
		if( str.indexOf(name) == -1 ){
			return true;
		}
	}
	return false;
}
/*function checkUserBirth(idCode,birthDay){
	var code;
	var birth = birthDay.split("-");
	if( idCode.length != 18 ){
		birth[0] = birth[0].substring( 2,4 );
	}
	var birthStr = birth[0] + birth[1] + birth[2];
	if( idCode.length == 18 ){
		if( birthStr == idCode.substring( 6,14 )){
			return true;
		}
	}else{
		if( birthStr == idCode.substring( 6,12 )){
			return true;
		}
	}
	return false;
}
function changeWorkplace(){
	var frm=document.registerForm;
	var place = frm["organizationId"];
	var work = frm["learner.workplace"];

	if( place.value == '999' ){
		work.style.display = '';
		work.value = '';
	}else{
		work.style.display = 'none';
		work.value = place.options[place.selectedIndex].text;		
	}
}
function checkSex(obj,str){
	if( str%2 > 0 ){
		if( 'm' != obj.value ){
			alert( "您填入的性别与身份证号不符" );
			obj.focus();
			return false;
		}
	}
	if( str%2 == 0 ){
		if( 'f' != obj.value ){
			alert( "您填入的性别与身份证号不符" );
			obj.focus();
			return false;
		}
	}
}*/
</script>
</head>
<body class="jrbody"><br/><br/>
<table width="780" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
    	<td>
    		<img src="<%=imgbase%>/register-01.gif"/>
    	</td>
	</tr>
  	<tr>
    	<td height="1" bgcolor="#3C62D9"></td>
  	</tr>
  	<tr>
    	<td bgcolor="#1777B8">
		    <table width="778" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		    	<tr>
		        	<td valign="top" class="jrlogobg-register"><p>&nbsp;</p>
						<table width="640" align="center" border="0" cellpadding="0" cellspacing="0">
							<tr class="tr-color-body">
								<td height="30" align="center" class="jr0f60a4">
									<bean:write name="ActiveCodeInfo"/>
								</td>
							</tr>
						    <tr class="tr-color-body">
						    	<td colspan="6" align="center" class="jr0f60a4">
						    		<a href="loginAction.do?method=init" class="button">登录</a>
						      	</td>
						    </tr>
						</table>
				        <br />
					</td>
		    	</tr>
		    </table>
    	</td>
 	</tr>
	<tr>
    	<td height="1" bgcolor="#3C62D9"></td>
  	</tr>
</table>
</body>
</html:html>