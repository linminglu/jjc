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
	if(frm["operateType"].value.trim().length==0){
		alert("请选择类型");
		frm["operateType"].focus();
		return false;
	}
	
	if(frm["userpoints"].value.trim().length==0){
		alert("请输入金额");
		frm["userpoints"].focus();
		return false;
	}
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
function changeArea(val){
	var testJson='<%=request.getAttribute("cityJson")%>';
	var json = eval("(" + testJson + ")");  
	
	var length=json.length;
	for(var i=0;i<length;i++){
		var cid=json[i].id;
		if(cid==val){
			var areaList=json[i].list;
			var areaLengrh=areaList.length;
			var obj=document.getElementById('seleAreaId');
			obj.options.length=0;
			obj.options.add(new Option("请选择","")); //这个兼容IE与firefox
			for(var j=0;j<areaLengrh;j++){
				obj.options.add(new Option(areaList[j].title,areaList[j].id)); //这个兼容IE与firefox
			}
		}
	}
}

</script>
</head>
<body>
<div class="bread">用户管理  &raquo; <c:if test = "${managerForm.user.userType==1}">注册用户 </c:if>
<c:if test = "${managerForm.user.userType==3}">商家用户 </c:if>
<c:if test = "${managerForm.user.userType==11}">测试用户 </c:if>
<c:if test = "${managerForm.user.userType==2}">员工管理 </c:if> &raquo; 修改</div>
<logic:present name="loginName_exist" scope="request">
	<script language="javascript">
		alert("该用户名已经被使用，请该用其它用户名");
	</script>
</logic:present>
<html:form action="/managerAction.do?method=modifyMoney" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" value="<c:out value='${category}'/>" name="category"/>
<html:hidden name="managerForm" property="user.userId"/>
<html:hidden name="managerForm" property="user.loginName"/>
<html:hidden property="user.userType" name="managerForm"/> 

<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">ID</th>
		<td>
        	<html:text name="managerForm" property="user.userId" disabled="true"/>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">用户名</th>
		<td>
        	<html:text name="managerForm" property="user.loginName" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">当前余额</th>
      	<td>
      		<html:text name="managerForm" property="user.money" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
        <th align="right">选择扣款方式</th>
        <td>
        	<html:select property="type" name="managerForm" styleClass="sele">
				<html:option value="">请选择</html:option>
				<html:option value="1">正常扣款</html:option>
				<html:option value="0">其它扣款</html:option>
			</html:select>
      	</td>
    </tr>
   <!-- <tr class="tr-color-body">
      	<th align="right">操作类型</th>
      	<td>
        	<html:select property="operateType" name="managerForm" styleClass="sele">
				<html:option value="">请选择</html:option>
				<html:option value="1">增加积分</html:option>
				<html:option value="0">削减积分</html:option>
			</html:select>
      	</td>
    </tr>-->
    <tr class="tr-color-body">
      	<th align="right">输入扣款金额</th>
      	<td>
      		<html:text name="managerForm" property="userpoints"  onkeyup="value=value.match( /^[1-9]\d*|0$/)" />
      		<!--  <font color="red">说明：回收积分应小于用户原有的积分。</font>-->
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