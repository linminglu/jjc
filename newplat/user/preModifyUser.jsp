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
 	if(frm["user.userName"].value.length==0){
 		alert("请输入昵称");
 		frm["user.userName"].focus();
 		return false;
 	}	
 	if(frm["user.cellPhone"].value.length==0){
 		alert("请输入手机号");
 		frm["user.cellPhone"].focus();
 		return false;
 	}
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

function changeUserType(){
	var s= $("#selectUserType").val().trim();
	var frm = document.managerForm;
	frm["uType"].value= s;
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
<c:if test = "${managerForm.user.userType==5||managerForm.user.userType==6||managerForm.user.userType==7}">商家用户 </c:if>
<c:if test = "${managerForm.user.userType==2}">员工管理 </c:if>
<c:if test = "${managerForm.user.userType==9}">代理管理</c:if>
<c:if test = "${managerForm.user.userType==10}">物业管理员</c:if> &raquo; 修改</div>
<logic:present name="loginName_exist" scope="request">
	<script language="javascript">
		alert("该用户名已经被使用，请该用其它用户名");
	</script>
</logic:present>
<html:form action="/managerAction.do?method=modify" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="managerForm" property="user.userId"/>
<html:hidden property="user.userType" name="managerForm"/>
<html:hidden name="managerForm" property="user.password" />
<html:hidden name="managerForm" property="userType" />
<html:hidden name="managerForm" property="uType" />
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
      		<html:password name="managerForm" property="password" />
      		<input type="button" onclick="initialize()"  class="button"  value="初始密码"/>
      		<span class="span-tip">初始密码：888888</span>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">昵称</th>
      	<td>
       		<html:text property="user.userName" name="managerForm" maxlength="50"/>
      	</td>
    </tr>
	<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
	<!-- 用户 -->
    <tr class="tr-color-body">
      	<th align="right">手机号</th>
      	<td>
        	<html:text property="user.cellPhone" name="managerForm" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"/>
      	</td>
    </tr>
    </c:if>

    <tr class="tr-color-body">
      	<th align="right">用户类型</th>
      	<td>
       		<logic:equal  name="managerForm" property="user.userType" value="1">
					<font color="black"><strong>普通用户</strong></font>
			</logic:equal>
       		<logic:equal  name="managerForm" property="user.userType" value="2">
					<font color="black"><strong>数据管理员</strong></font>
			</logic:equal>
	       	<logic:equal  name="managerForm" property="user.userType" value="13">
					<font color="green"><strong>初级代理</strong></font>
			</logic:equal>
	       	<logic:equal  name="managerForm" property="user.userType" value="14">
					<font color="green"><strong>中级代理</strong></font>
			</logic:equal>
	       	<logic:equal name="managerForm" property="user.userType" value="15">
					<font color="green"><strong>高级代理</strong></font>
			</logic:equal>
			<logic:equal name="managerForm" property="user.userType" value="20">
					<font color="green"><strong>客服人员</strong></font>
			</logic:equal>
			<logic:equal name="managerForm" property="user.userType" value="21">
					<font color="green"><strong>代理查看</strong></font>
			</logic:equal>
      	</td>
    </tr>
       <c:if test = "${loginUser.userType==2||loginUser.userType==4}">
	       <tr class="tr-color-body">
		<th align="right">
			修改用户类型
		</th>
		<td>
	        <select id="selectUserType" onchange="changeUserType();">
				<option value="">请选择</option>
				<option value="13">初级代理</option>
				<option value="14">中级代理</option>
				<option value="15">高级代理</option>
				<option value="1">普通用户</option>
				<option value="20">客服人员</option>
				<option value="21">代理查看</option>
			</select>
      	</td>
	   </tr>
	   <tr class="tr-color-body">
	      <th align="right">上级代理Id</th>
	      <td>
	       		<html:text property="user.agentId" name="managerForm" maxlength="100" />
	      </td>
	   </tr>
	   
    </c:if>
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