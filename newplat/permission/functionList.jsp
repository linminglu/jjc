<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/inc_include.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.ram.model.Function"%>
<%@ page import="com.framework.dao.hibernate.PaginationSupport"%>
<%
PaginationSupport ps=(PaginationSupport)request.getAttribute("ps");
List functionList=ps.getItems();
Function parentFunction=(Function)request.getAttribute("parentFunction");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file = "../common/inc_css.jsp"%>
</head>
<body  >
<p class=bar>学员管理 >> 用户组管理 >> 系统维护</p>
<table border="0" cellpadding="0" cellspacing="1" class="table-color" width="100%">
	<tr class="tr-color-head">
		<th nowrap="nowrap">序号</th>
		<th nowrap="nowrap">功能名称</th>
		<th nowrap="nowrap">排序</th>
		<th nowrap="nowrap">修改</th>
		<th nowrap="nowrap">删除</th>	
		<th nowrap="nowrap">功能URL</th>	
  	</tr>
  	<logic:iterate id="item" name="functionList" indexId="fid">
  	<bean:define id="currentFunctionId" name="item" property="functionId"/>
  	<tr class="tr-color-body"  onmouseover="this.style.background='#EEE'" onmouseout="this.style.background='#FFF'">
	    <td nowrap>
	    	<%=fid.intValue()+1%>
	    </td>
	    <td nowrap>
	    	<a href="functionAction.do?method=init&parentFunctionId=<%=currentFunctionId.toString()%>" title="点击进入下级功能">
	    		<bean:write name="item" property="functionTitle"/>
	    	</a>
	    </td>
	    <td nowrap="nowrap">
	    	<a href="functionAction.do?method=orderUp&functionId=<%=currentFunctionId.toString()%>" title="上调">UP</a>
	    	<bean:write name="item" property="orderSn"/>
	    	<a href="functionAction.do?method=orderDown&functionId=<%=currentFunctionId.toString()%>" title="下调">DOWN</a>    	
	    </td>
	    <td align="center" nowrap="nowrap">	
	    	<a class="button" href="functionAction.do?method=preModify&functionId=<%=currentFunctionId.toString()%>" title="修改">编辑</a>
		</td>
	    <td align="center" nowrap="nowrap">
			<c:if test="${item.subFunctionNumber==0}">
		    	<a class="button-delete" href="functionAction.do?method=delete&functionId=<%=currentFunctionId.toString()%>" title="删除">删除</a>
		    </c:if>
	    	<c:if test="${item.subFunctionNumber>0}">有下级功能</c:if>
	    </td>
	    <td nowrap><bean:write name="item" property="functionLinkAddress"/></td>	
  	</tr>
  	</logic:iterate>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="table-color" width="100%">
	<tr class="tr-color-body" >
		<td>
			<hr size=1>
			<a class="button" href="functionAction.do?method=preAdd&functionId=<%=parentFunction.getFunctionId()%>">创建功能</a>
			<a class="button" href="./functionAction.do?method=init&parentFunctionId=<%=parentFunction.getParentId()%>">返回功能</a>
			<!--a class="button" href="../user/userGroupListAction.do?method=init">用户组功能分配</a-->
	    </td>
  	</tr>
</table>

</body>
</html>