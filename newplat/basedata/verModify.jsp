<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function formSubmit(){
	var frm = document.baseDataForm;
	if(frm["version.type"].value.trim().length==0){
		alert("请选择设备类型");
		frm["version.type"].focus();
		return false;
	}
	if(frm["version.ver"].value.trim().length==0){
		alert("请输入版本号");
		frm["version.ver"].focus();
		return false;
	}
	if(frm["version.isDef"].value.trim().length==0){
		alert("请选择是否是当前版本");
		frm["version.isDef"].focus();
		return false;
	}
	if(frm["version.link"].value.trim().length==0){
		alert("请输入下载地址");
		frm["version.link"].focus();
		return false;
	}
	if(frm["version.content"].value.trim().length==0){
		alert("请输入升级内容");
		frm["version.content"].focus();
		return false;
	}
	return true;
}
</script>
</head>
<body>
<div class="bread">版本管理  &raquo; 
<logic:empty name="baseDataForm" property="version.id">新增</logic:empty>
<logic:notEmpty name="baseDataForm" property="version.id">修改</logic:notEmpty>
</div>
<html:form action="/baseDataAction.do?method=saveVer" onsubmit="return formSubmit();" >
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>"/>
<html:hidden name="baseDataForm" property="version.id"/>
<html:hidden name="baseDataForm" property="version.isDef" value="1"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">设备类型</th>
		<td>
        	<html:select property="version.type" name="baseDataForm">
				<html:option value="">请选择</html:option>
				<html:option value="1">IOS</html:option>
				<html:option value="2">Android</html:option>
			</html:select>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">版本号</th>
		<td>
        	<html:text name="baseDataForm" property="version.ver" onkeyup="value=value.replace(/[^\d\.]/g,'')"/>
      	</td>
    </tr>
    <%-- 
	<tr class="tr-color-body">
		<th align="right">当前版本</th>
		<td>
			<html:radio name="baseDataForm" property="version.isDef" value="1" styleId="isdef1"><label for="isdef1" class="cursor">是</label></html:radio>
			<html:radio name="baseDataForm" property="version.isDef" value="0" styleId="isdef0"><label for="isdef0" class="cursor">否</label></html:radio>
      	</td>
    </tr>
     --%>
    <tr class="tr-color-body">
		<th align="right">升级方式</th>
		<td>
        	<html:select property="version.flag" name="baseDataForm">
				<html:option value="1">强制升级</html:option>
				<html:option value="0">可选升级</html:option>
			</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">下载地址</th>
		<td>
        	<html:text name="baseDataForm" property="version.link" styleId="w400"/>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">升级内容</th>
		<td>
        	<html:textarea name="baseDataForm" property="version.content" cols="60" rows="8" />
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