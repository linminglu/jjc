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
 	/* if(frm["user.gender"].value.length==0){
 		alert("请选择性别");
 		return false;
 	} */
 	if(frm["user.cellPhone"].value.length==0){
 		alert("请输入电话");
 		frm["user.cellPhone"].focus();
 		return false;
 	}
 	if(frm["user.cellPhone"].value.length==0){
 		alert("请输入电话");
 		frm["user.cellPhone"].focus();
 		return false;
 	}
 	var userType=frm["user.userType"].value;
 	if(userType.length==0){
 		alert("请选择用户类型");
 		frm["user.userType"].focus();
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

</script>
</head>
<body>
<div class="bread">用户管理&raquo; 增加</div>
<html:form action="managerAction.do?method=add" focus="user.loginName" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden property="user.organizationId" name="managerForm" value="0" />
<input type="hidden" value="<c:out value='${userType}'/>" name="userType"/>
<html:hidden property="type" name="managerForm"/>

<table width="100%" class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">登录名</th>
      	<td>
        	<html:text property="user.loginName" name="managerForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">登录密码</th>
      	<td>
        	<html:password property="user.password" name="managerForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">昵称</th>
      	<td>
       		<html:text property="user.userName" name="managerForm" maxlength="50"/>
      	</td>
    </tr>
    <%-- <tr class="tr-color-body">
      	<th align="right">性别</th>
      	<td>
      		<html:radio property="user.gender" value="1" styleId="gender1" >
      			<label for="gender1">男</label> 
      		</html:radio>
      		<html:radio property="user.gender" value="2" styleId="gender2">
      			<label for="gender2">女</label> 
      		</html:radio>
      	</td>
    </tr> --%>
    <tr class="tr-color-body">
      	<th align="right">电话</th>
      	<td>
       		<html:text property="user.cellPhone" name="managerForm" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">
			用户类型
		</th>
		<td>
	        <html:select property="user.userType" name="managerForm"  onchange="changeUserType();">
				<option value="">请选择</option>
				<option value="1">普通用户</option>
				<option value="13">初级代理</option>
				<option value="14">中级代理</option>
				<option value="15">高级代理</option>
				<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
					<option value="20">客服人员</option>
					<option value="21">代理查看</option>
					<option value="2">数据管理员</option>
				</c:if>
			</html:select>
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