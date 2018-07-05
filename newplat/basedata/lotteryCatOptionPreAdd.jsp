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
	var frm = document.baseDataForm;
	if(frm["gasInfo.gameTitle"].value.trim().length==0){
		alert("请输入游戏名");
		frm["gasInfo.gameTitle"].focus();
		return false;
	}
	if(frm["gasInfo.gameType"].value.length==0){
		alert("请输入游戏类型");
		frm["gasInfo.gameType"].focus();
		return false;
	}
 	if(frm["gasInfo.gameCode"].value.length==0){
 		alert("请输入游戏代码");
 		frm["gasInfo.gameCode"].focus();
 		return false;
 	}
 	if(frm["gasInfo.gameSet"].value.length==0){
 		alert("请输入游戏设置");
 		frm["gasInfo.gameSet"].focus();
 		return false;
 	}
 	if(frm["gasInfo.gameSet"].value.length==0){
 		alert("请输入游戏设置");
 		frm["gasInfo.gameSet"].focus();
 		return false;
 	}
 	if(frm["gasInfo.status"].value.length==0){
 		alert("请输入游戏设置");
 		frm["gasInfo.status"].focus();
 		return false;
 	}
 	if(frm["gasInfo.des"].value.length==0){
 		alert("请输入描述");
 		frm["gasInfo.des"].focus();
 		return false;
 	}
 	if(frm["gasInfo.exp"].value.length==0){
 		alert("请输入开奖期数说明");
 		frm["gasInfo.des"].focus();
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
<div class="bread"><c:out value='${gasTitle}'/> &raquo;玩法&raquo;添加玩法</div>
<html:form action="baseDataAction.do?method=lotteryCatOptionAdd" focus="user.loginName" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden property="gasInfo.infoId" name="baseDataForm"/>
<html:hidden property="gabOpt.betOptionId" name="baseDataForm"/>
<table width="100%" class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">名称</th>
      	<td>
        	<html:text property="gabOpt.optionTitle" name="baseDataForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">下注类型</th>
      	<td>
        	<html:text property="gabOpt.optionType" name="baseDataForm"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">赔率</th>
      	<td>
       		<html:text property="gabOpt.betRate" name="baseDataForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">playType</th>
      	<td>
       		<html:text property="gabOpt.playType" name="baseDataForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">积分倍数</th>
      	<td>
       		<html:text property="gabOpt.pointMultiple" name="baseDataForm" maxlength="50"/>
      	</td>
    </tr>
<!--     <tr class="tr-color-body" id="imgbox"> -->
<!--       	<th align="right">图片</th> -->
<!--       	<td> -->
<%--       		<html:file property="file" name="baseDataForm" size="50"/><br/> --%>
<%--       		<html:hidden property="gasInfo.img" name="baseDataForm" /> --%>
<%--       		<c:if test="${baseDataForm.gasInfo.img ne null}"> --%>
<%--       			<img src="..<c:out value="${baseDataForm.gasInfo.img}"/>" alt="<c:out value="${baseDataForm.gasInfo.gameTitle}"/>" title="<c:out value="${baseDataForm.gasInfo.gameTitle}"/>" width="120"/> --%>
<%--       		</c:if> --%>
<!--       		<p class="red">建议尺寸 -->
<!--       			[宽x高]120x120 -->
<!--       		</p> -->
<!--       	</td> -->
<!--     </tr> -->
<!--     <tr class="tr-color-body"> -->
<!--       	<th align="right">状态</th> -->
<!--       	<td> -->
<%--       		<html:radio property="gasInfo.status" value="1" styleId="status1"> --%>
<!--       			<label for="status1">有效</label>  -->
<%--       		</html:radio> --%>
<%--       		<html:radio property="gasInfo.status" value="0" styleId="status2"> --%>
<!--       			<label for="status2">无效</label>  -->
<%--       		</html:radio> --%>
<!--       	</td> -->
<!--     </tr> -->

</table>

<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>

</html:form>
</body>
</html:html>