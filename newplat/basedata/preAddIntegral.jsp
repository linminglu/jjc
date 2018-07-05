<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_ueditor.jsp"%>
<script language="javascript">
function loadPage(){
	var err = $("#errors");
	if(err && err.val()!=""){
		var errorCode= err.val();
		if(errorCode=="NOT_SUBMIT_FORM"){
			alert("不能重复提交表单！");
		}
	}
}
window.onload = function(){
	loadPage();
};
function formSubmit(){
	var frm = document.baseDataForm;
	if(frm["sysOption.optionTitle"].value == "26"){
		if(frm["sysOption.points"].value == ""){
			alert("请选择游戏难度！");
			return false;
		}
	}
	if(frm["sysOption.optionTitle"].value == "3"){
		if(frm["sysOption.points"].value.length==0){
			alert("请输入值")
			frm["sysOption.points"].focus();
		    return false;
		}
	}
		
	var tijiao=document.getElementById("tijiao");
    var back = document.getElementById("back");
    var infoObj = document.getElementById("info");
    infoObj.style.display = 'block'; 
    back.disabled = true;
	tijiao.disabled = true;
}

</script>
</head>
<body>

<div class="bread"><c:if test="${optionType == 3 }">积分管理  &raquo; 积分设置  &raquo;</c:if>
<c:if test="${optionType == 26 }">系统管理&raquo; 游戏难度设置</c:if>
<c:if test="${optionType == 27 }">系统管理&raquo; 我的积分说明</c:if>
<c:if test="${action eq 'add'}">新增</c:if>
<c:if test="${action eq 'modif'}">修改</c:if>
</div>
<html:form action="/baseDataAction.do?method=saveIntegral" onsubmit="return formSubmit();"  enctype="multipart/form-data">
<html:hidden name="baseDataForm" property="sysOption.optionId"/>
<html:hidden name="baseDataForm" property="sysOption.optionType"/>
<html:hidden name="baseDataForm" property="unitType"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<logic:equal name="baseDataForm" property="sysOption.optionType" value="26">
		<tr class="tr-color-body">
			<th align="right">标题</th>
			<td><html:text property="sysOption.optionTitle" name="baseDataForm" disabled="true"/></td>
		</tr>
		<tr class="tr-color-body">
      	<th align="right">游戏难度</th>
      	<td>
      		<html:radio property="sysOption.points" value="1.00" name="baseDataForm" styleId="invoice0"><label for="invoice1" class="cursor">低</label></html:radio>
      		<html:radio property="sysOption.points" value="2.00" name="baseDataForm" styleId="invoice1"><label for="invoice0" class="cursor">中</label></html:radio>
      		<html:radio property="sysOption.points" value="3.00" name="baseDataForm" styleId="invoice2"><label for="invoice0" class="cursor">高</label></html:radio>
      	</td>
    </tr>
	</logic:equal>
	<logic:equal name="baseDataForm" property="sysOption.optionType" value="3">
	    <tr class="tr-color-body">
			<th align="right">标题</th>
			<td><html:text property="sysOption.optionTitle" name="baseDataForm" disabled="true"/></td>
		</tr>
		<tr class="tr-color-body">
		    <th align="right">积分</th>
		    <td><html:text  name="baseDataForm" property="sysOption.points" /></td>
		</tr>
	</logic:equal>
	<logic:equal name="baseDataForm" property="sysOption.optionType" value="27">
	    <tr class="tr-color-body">
			<th align="right">标题</th>
			<td><html:text property="sysOption.optionTitle" name="baseDataForm" disabled="true"/></td>
		</tr>
		<tr class="tr-color-body">
		    <th align="right">我的积分说明</th>
		    <td><html:textarea  name="baseDataForm" property="sysOption.content" cols="50" rows="5"/></td>
		</tr>
	</logic:equal>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定" id ='tijiao'/>
    <input type="button" class="gbutton" id ='back' onclick="javascript:window.history.go(-1)" value="返回"/>
    <span id="info" style="display:none;color:red;font-size:14px;font-weight:bold;margin-top:10px;">正在上传，请稍等...</span>
    
</div>
</html:form>
</body>
</html:html>
