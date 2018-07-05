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
function checkUser(a){
	var b = a.parentNode;
	var username = b.getElementsByTagName("input")[0].value;
	if( username == "" && username == '' ){
		alert( "昵称不成为空" );
		return false;
	}
	if( checkLoginName( username )){
		alert( "昵称只能是数字和字母");
		return false;
	}
 	var doc = xmlhttpReq();
	var url = "../user/userRegistInitAction.do?method=checkUse&username="+username+"&rm="+(new Date()).getTime();
	//alert(url);
 	doc.open("GET", url, true);
 	doc.onreadystatechange=function(){callback(a,doc)};
 	doc.send(null);
}
function callback(a,doc){
  	if(doc.readyState == 4){
   		if(doc.status == 200){
    		if(doc.responseText=="yes"){
    			alert("此昵称已存在");
    		}else{
    			alert("此昵称可用");
    		}
   		}else{
   			alert('保存失败,请稍后在试:'+doc.status);
   		}
  	}
}
function formSubmit(){
	var frm = document.managerForm;
	var userType = frm["type"].value;
	if(frm["userLevel.levelName"].value.trim().length==0){
		alert("请输入等级名");
		frm["userLevel.levelName"].focus();
		return false;
	}
	if(frm["userLevel.point"].value.length==0){
		alert("请输入成长积分");
		frm["userLevel.point"].focus();
		return false;
	}
	if(isNaN(frm["userLevel.point"].value)){
		alert("请输入正确的成长积分");
		frm["userLevel.point"].focus();
		return false;
	}
 	if(frm["userLevel.award"].value.length==0){
 		alert("请输入跳级奖励");
 		frm["userLevel.award"].focus();
 		return false;
 	}
 	if(isNaN(frm["userLevel.award"].value)){
		alert("请输入正确的跳级奖励");
		frm["userLevel.award"].focus();
		return false;
	}
 	if(frm["userLevel.level"].value.length==0){
 		alert("请输入等级");
 		frm["userLevel.level"].focus();
 		return false;
 	}
 	if(isNaN(frm["userLevel.level"].value)){
		alert("请输入正确的等级");
		frm["userLevel.level"].focus();
		return false;
	}
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
function checkUserGroupSelect(){
	var items = document.getElementsByName("selectIndex");
	if(items && items.length>0){
		var count=0;
		for(var i=0;i<items.length;i++){
			if(items[i].checked==true) count++;
		}
		return count>0?true:false;
	}
	return true;
}
function selectAll(flag){
	var frm = document.managerForm;
	for(var i=0;i<frm.length;i++){
		if( frm.elements[i].name == "selectIndex" ){
			frm.elements[i].checked = flag;
		}
	}
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

function changeSchedule2(typeId){
	$.post("managerAction.do?method=findCityBusAreaList&cid="+typeId+"&t="+new Date().getTime(), 
       		function(data){
    			changeTypeList2(data);
       		});
    
}
function changeTypeList2(data){ 
	 var shijiSelect = document.all('busaid');
	 shijiSelect.innerHTML="";
	 var obj = jQuery.parseJSON(data);//eval(request.responseText);
	 var option0 = document.createElement("OPTION");
	 option0.innerHTML = "请选择";
	 option0.value = "";
	 shijiSelect.appendChild(option0);
	 if(obj.length > 0){
		for(var i=0;i<obj.length;i++){
			var option = document.createElement("OPTION");
	        	option.innerHTML = obj[i].title;
	       		option.value = obj[i].busaid;
	  			shijiSelect.appendChild(option);   
		}
	}
}

function changeSchedule3(typeId){
	$.post("managerAction.do?method=findCityCommunityList&cid="+typeId+"&t="+new Date().getTime(), 
       		function(data){
    			changeTypeList3(data);
       		});
    
}
function changeTypeList3(data){ 
	 var shijiSelect = document.all('ccid');
	 shijiSelect.innerHTML="";
	 var obj = jQuery.parseJSON(data);//eval(request.responseText);
	 var option0 = document.createElement("OPTION");
	 option0.innerHTML = "请选择";
	 option0.value = "";
	 shijiSelect.appendChild(option0);
	 if(obj.length > 0){
		for(var i=0;i<obj.length;i++){
			var option = document.createElement("OPTION");
	        	option.innerHTML = obj[i].title;
	       		option.value = obj[i].ccid;
	  			shijiSelect.appendChild(option);   
		}
	}
}

window.onload = function(){
	var msg = $("#msg").val();
	if(msg&&msg.length>0){
		alert(msg);
	}
}
</script>
</head>
<body>
<div class="bread">等级管理&raquo;<c:if test="${id>=0}">修改等级</c:if><c:if test="${id<0}">添加等级</c:if></div>
<html:form action="managerAction.do?method=addLevel" focus="userLevel.levelName" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" value="<c:out value='${id}'/>" name="id"/>
<input type="hidden" value="<c:out value='${msg}'/>" id="msg"/>
<html:hidden property="type" name="managerForm"/>

<table width="100%" class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    
    <tr class="tr-color-body">
		<th align="right">等级名称</th>
      	<td>
        	<html:text property="userLevel.levelName" name="managerForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">成长积分</th>
      	<td>
        	<html:text property="userLevel.point" name="managerForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">跳级奖励</th>
      	<td>
       		<html:text property="userLevel.award" name="managerForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">等级</th>
      	<td>
       		<html:text property="userLevel.level" name="managerForm" maxlength="50"/>
      	</td>
    </tr>
    
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.location='../../user/managerAction.do?method=userLevelList'" value="返回"/>
</div>
</html:form>
</body>
</html:html>