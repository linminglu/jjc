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
<p class="bar">学员管理 >>  用户组管理 >> 用户组功能分配</p>
<table border="0" cellpadding="0" cellspacing="1" class="table-color" width="100%">
	<tr class="tr-color-body">
    	<td>当前用户组：<c:out value="${userGroup.userGroupName}"/></td>
  	</tr>
  	<tr class="tr-color-body">
    	<td>当前父功能：<c:out value="${parentFunction.functionTitle}"/></td>	
  	</tr>    
  	<tr class="tr-color-body">
    	<td class="font-bold">[<c:out value="${userGroup.userGroupName}"/>]在[<c:out value="${parentFunction.functionTitle}"/>]下已经具备的功能有</td>	
  	</tr>    
</table>
<table border="0" cellpadding="0" cellspacing="1" class="table-color" width="100%">
	<tr class="tr-color-head">
	    <th>序号</th>
		<th>功能名称</th>
		<th>功能URL</th>
		<th>operation</th>
  	</tr>
  	<logic:present name="functionList" >
	<logic:iterate id="function" name="functionList" indexId="fid">
	<tr class="tr-color-body" onmouseover="this.style.background='#EEE'" onmouseout="this.style.background='white'">
		<td>
			<%=fid.intValue()+1%>
		</td>
		<td>
			<logic:equal value="" name="function" property="functionLinkAddress">
				<a href='groupFunctionAction.do?method=init&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${function.functionId}"/>'>
					<bean:write name="function" property="functionTitle"/>
				</a>				
			</logic:equal>
			<logic:equal value="../../HaveNotDeveloped.jsp" name="function" property="functionLinkAddress">
				<a href='groupFunctionAction.do?method=init&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${function.functionId}"/>'>
					<bean:write name="function" property="functionTitle"/>
				</a>				
			</logic:equal>		
			<logic:notEqual value="../../HaveNotDeveloped.jsp" name="function" property="functionLinkAddress">						
				<logic:notEqual value="" name="function" property="functionLinkAddress">
			   		<bean:write name="function" property="functionTitle"/>
				</logic:notEqual>
			</logic:notEqual>
		</td>
		<td>
			<logic:equal value="../../HaveNotDeveloped.jsp" name="function" property="functionLinkAddress">
				有下级菜单			</logic:equal>
			<logic:equal value="" name="function" property="functionLinkAddress">
				有下级菜单
			</logic:equal>
			<logic:notEqual value="" name="function" property="functionLinkAddress">
				<logic:notEqual value="../../HaveNotDeveloped.jsp" name="function" property="functionLinkAddress">
					<c:out value="${function.functionLinkAddress}"/>
				</logic:notEqual>		
			</logic:notEqual>		
		</td>
		<td align="center">
			<a class="button-delete" href='groupFunctionAction.do?method=delete&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${parentFunctionId}"/>&functionId=<c:out value="${function.functionId}"/>'>
				移除当前功能和下属功能
			</a>
	  	</td>
	</tr>
	</logic:iterate>  
	</logic:present>
  	<logic:notPresent name="functionList">
  	<tr class="tr-color-body">
  		<td colspan="4">在本级下未分配功能</td>
  	</tr>
  	</logic:notPresent>
  	<tr class="tr-color-body">
	  	<td colspan="4">
			<a class="button" href='groupFunctionAction.do?method=add&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${parentFunctionId}"/>'>添加本级功能权限</a>
		  	<a class="button" href='groupFunctionAction.do?method=addAll&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${parentFunctionId}"/>'>添加本级所有功能权限</a>	
			<a class="button" href="javascript:history.back();">返回</a>
			<!--a class="button" href='groupFunctionAction.do?method=init&userGroupId=<c:out value="${userGroup.userGroupId}"/>&parentFunctionId=<c:out value="${parentFunction.parentId}"/>'>返回功能</a>
			<a class="button" href='../user/userGroupListAction.do?method=init'>返回用户组管理</a-->
	  	</td>
  	</tr>
</table>
</body>
</html:html>