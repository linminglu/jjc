<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file = "../common/inc_css.jsp"%>
</head>
<body>
<p class=bar>系统管理 >> 用户组管理 >> 用户组功能分配 >> 功能权限分配</p>
<table border="0" cellpadding="0" cellspacing="1" class="table-color" width="100%">
	<tr class="tr-color-body">
    	<td>当前用户组：<c:out value="${userGroup.userGroupName}"/></td>
  	</tr>
  	<tr class="tr-color-body">
    	<td>当前父功能：<c:out value="${parentFunction.functionTitle}"/></td>	
  	</tr>  
  	<tr class="tr-color-body">
    	<td class="font-bold">[<c:out value="${userGroup.userGroupName}"/>]分配在[<c:out value="${parentFunction.functionTitle}"/>]功能下的子功能</td>	
  	</tr>   
</table>
<logic:present name="functionList" >
<table border="0" cellpadding="0" cellspacing="1" class="table-color" width="100%">
	<tr class="tr-color-head">
	  	<th>功能ID</th>
		<th>功能名称</th>
		<th>功能URL</th>
		<th>operation</th>
  	</tr>
  	<logic:notEmpty name="functionList">
  	<logic:iterate id="function" name="functionList" indexId="fid">
  	<tr class="tr-color-body" onmouseover="this.style.background='yellow'" onmouseout="this.style.background='white'">
  		<td>
  			<%=fid.intValue()+1%>
  		</td>
	    <td nowrap>
	   		<bean:write name="function" property="functionTitle"/>
	   	</td>
	   	<td nowrap>
	   		<bean:write name="function" property="functionLinkAddress"/>
	   	</td>
	   	<td align="center">
	   		<a class="button" href='groupFunctionAction.do?method=saveFunction&userGroupId=<c:out value="${userGroup.userGroupId}"/>&functionId=<c:out value="${function.functionId}"/>&parentFunctionId=<c:out value="${function.parentId}"/>'>分配给该组</a>
	   	</td>
	</tr>
  	</logic:iterate>  
  	</logic:notEmpty>
  	<logic:empty name="functionList">
  	<tr class="tr-color-body">
  		<td colspan="5" class="font-must">在本级下无功能可以分配</td>
  	</tr>
  	</logic:empty>
</table>
</logic:present>
<hr size=1>
<a class="button"  onclick="javascript:history.back();">返回</a>
</body>
</html:html>