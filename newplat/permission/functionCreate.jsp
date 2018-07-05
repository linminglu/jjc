<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.framework.util.ParamUtils"%>
<%@ page import="com.ram.model.Function"%>
<%
Function parentFunction=(Function)request.getAttribute("parentFunction");
%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_css.jsp"%>
<script language="javascript">
function validator(){
	var frm = document.functionForm;
	if( frm["function.functionTitle"].value == '' ){
		alert( "请输入功能名称" );
		frm["function.functionTitle"].select();
		return false;
	}
	return true;
}
</script>
</head>
<body>
<p class=bar>学员管理 >> 用户组管理 >> 系统维护 >> 创建功能</p>
<html:form action="/functionAction.do?method=add" focus="function.functionTitle" onsubmit="return validator();">
<input type="hidden" name="parentFunctionId" value="<%=parentFunction.getFunctionId()%>">
<table width="300" class="table-color" cellpadding="0" cellspacing="1" border="0">
	<tr class="tr-color-body">
		<th align="right">上级功能&nbsp;</th>
      	<td>&nbsp;
      		<%=parentFunction.getFunctionTitle()%>
      	</td>	
    </tr>
    <tr class="tr-color-body">
      	<th align="right">功能名称&nbsp;</th>
      	<td>&nbsp;
        	<html:text property="function.functionTitle"/><font class="font-must">[*必填]</font>
     	</td>
    </tr>    
    <tr class="tr-color-body">
      	<th align="right">功能链接&nbsp;</th>
      	<td>&nbsp;
        	<html:text property="function.functionLinkAddress"   size="100" value="../../HaveNotDeveloped.jsp"/><font class="font-must">[*必填]</font>
      	</td>
    </tr>
    <tr class="tr-color-body">
    	<td colspan="2">
    		<html:submit styleClass="button" value="创建功能"/>
			<a href="#" onclick="history.back(-1);" class="button">返回</a>
    	</td>
    </tr>
</table>
</html:form>
</body>
</html:html>