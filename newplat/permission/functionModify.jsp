<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
 <%@ include file = "../common/inc_css.jsp"%>
</head>
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
<body>
<p class=bar>学员管理 >> 用户组管理 >> 系统维护 >> 修改功能</p>
<html:form action="/functionAction.do?method=modify" focus="function.functionTitle" onsubmit="return validator();">
<input type="hidden" name="functionId" value="<%=request.getAttribute("functionId")%>">
<table width="300" class="table-color" cellpadding="0" cellspacing="1" border="0">
	<tr class="tr-color-body">
		<td align="right">功能名称&nbsp;</th>
      	<td>&nbsp;
        	<html:text name= "functionForm" property="function.functionTitle" size="100" maxlength="100"/>
      	</td>
    </tr>    
    <tr class="tr-color-body">
      	<td align="right">功能链接&nbsp;</th>
      	<td>&nbsp;
        	<html:text name= "functionForm" property="function.functionLinkAddress" size="100" maxlength="250" />
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<td align="right">父功能&nbsp;</th>
      	<td>&nbsp;
			<html:select property="function.parentId">
				<html:options collection="pfLabelValueBeanList" labelProperty="label" property="value" />
			</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<td align="right">排序号&nbsp;</th>
      	<td>&nbsp;
			<html:text name= "functionForm" property="function.orderSn" size="4" styleClass="only-number"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
    	<td colspan="2">
    		<html:submit styleClass="button" value="修改功能"/>
			<a class="button" href="#" onclick="history.back(-1)">返回</a>
    	</td>
    </tr>
</table>
</html:form>
</body>
</html:html>