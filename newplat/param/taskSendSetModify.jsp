<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function formSubmit(){
	var frm = document.paramForm;
	if(frm["param.value"].value.trim().length==0){
		alert("请输入取值");
		frm["param.value"].focus();
		return false;
	}
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

window.onload=function(){
}
</script>
</head>
<body>
<div class="bread">
<c:if test = "${paramForm.type==17}">任务赚钱设置 </c:if>
<logic:notEmpty name="paramForm" property="param.paramId">修改</logic:notEmpty>
</div>
<html:form action="/paramAction.do?method=saveTaskSendSet" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="paramForm" property="param.type"/>
<html:hidden name="paramForm" property="param.status"/>
<html:hidden name="paramForm" property="param.paramId"/>
<html:hidden name="paramForm" property="param.title"/>
<html:hidden name="paramForm" property="type"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
      	    <bean:write property="param.title" name="paramForm"/>
      	</td>
    </tr>
 	<tr class="tr-color-body" id="imgbox">
      	<th align="right">链接地址</th>
      	<td>
      		<html:text property="param.value" name="paramForm"  /> &nbsp;
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