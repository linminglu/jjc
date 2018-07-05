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

function modify(id){
	document.forms[0].action = "paramAction.do?method=preQQSendSetModify&action=modif&paramId="+id;
	document.forms[0].submit();
}

window.onload=function(){
}
</script>
</head>
<body>
<div class="bread">系统设置  &raquo; QQ客服地址设置</div>
<html:form action="/paramAction.do?method=qqSendSet">
</html:form>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>名称</th>
		<th>链接</th>
		<th>操作</th>
	</tr>
	<tr class="tr-color-body" onmousemove="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td align="center">
	     	<bean:write property="title" name="param"/>
	    </td>

	   	<td align="center">
	     	<bean:write property="value" name="param"/>
	    </td>
	    <td align="center">
	     	<a class="edit" href="javascript:modify(<bean:write name="param" property="paramId"/>);"><img class="xtbImg" src="../images/view.png"/></a>
	    </td>
	</tr>
</table>

</body>
</html:html>