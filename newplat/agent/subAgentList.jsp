<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file ="../common/inc_include.jsp"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function del(){
	var frm = document.agentForm;
	var bool = false;
	var sel = frm["selectIndex"];
	for(var i=0;i<sel.length;i++ ){
		if(sel[i].checked){
			bool = true;
		}
	}
	if( !bool ){
		alert( "请选择删除对象" );
		return false;
	}
	if( confirm( "你确认要删除吗?" )){
		return true;
	}
	return false;
}

function delUser(url,loginName){
	if(loginName && (loginName=="admin" || loginName=="cuisy" || loginName=="toefladmin" || loginName=="genadmin" 
		 	|| loginName=="testadmin" || loginName=="examadmin"
			|| loginName=="satadmin" || loginName=="ieltsadmin" || loginName=="greadmin" || loginName=="gmatadmin")){
		alert("不能删除此管理账号！");
		return false;
	}
	if(confirm("确认删除用户?")){
		location.href = url;
	}
	
}
function selectAll(flag){
	var selAll = document.agentForm;
	for( var i=0;i<selAll.elements.length;i++ ){
		if( selAll.elements[i].name == 'selectIndex' ){
			selAll.elements[i].checked = flag;
		}
	}
}
function delItem(id,type){
	if(confirm('确实要删除吗？')){
		$.post("managerAction.do?method=delUser&uid="+id+"&userType="+type,
			function(data) {
				data = $.parseJSON(data);
				if(data && data.code=='200') {
					location.reload();
					alert("删除成功！");
				}else{
					alert("删除失败！\n\n提示：数据已应用则不能删除！");
				}
		});	
	}
}
</script>
</head>
<body>
<div class="bread">下线代理  &raquo;
</div>
<html:form action="/agentAction.do?method=subAgentList" method="post">
<input type="hidden" value="<c:out value='${userType}'/>" name="userType"/>
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			代理
			<html:text property="userName" name="agentForm"/>
			<input type="submit" class="gbutton" value="查询" />
		</td>
	</tr>
</table>
</html:form>	

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
%>
<pg:pager 
	url="agentAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="subAgentList"/>
	<pg:param name="userType" value="<%=userType%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<html:form action="/agentAction.do?method=delete" onsubmit="return del();">
<bean:size id="length" name="list"/>
<logic:notEmpty name="list">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th>会员账号</th>
		<th>用户类型</th>
	    <th>上一级代理</th>
	    <th>最近登录</th>
	    <th>当前余额</th>
	    <th>状态</th>
	    <th>操作</th>
	</tr>
	<logic:iterate id="item" name="list" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
	    <td align="center">
	        <bean:write name="item" property="loginName" />
	    </td>
	    <td align="center">
	        <c:if test = "${userType==12}">
		              代理用户
	        </c:if>	
	    </td>
	    <td align="center">
		    <c:out value="${agentName}"/>
		</td>
		<td align="center">
	      	<bean:write name="item" property="lastLoginDate" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
		<td align="center">
	        <bean:write name="item" property="money"/>
	    </td>
	    <td align="center">
		    <logic:equal name="item" property="status" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;<a class="button" href='managerAction.do?method=pauseUser&&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${userType}"/>'>关</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="status" value="2">
				<font style="color: #666;"><strong>未激活</strong></font> 
			</logic:equal>
			<logic:equal name="item" property="status" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;<a class="button" href="managerAction.do?method=releaseUser&&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${userType}"/>">开</a>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="status" value="1">
			<!-- <font color="gray">未激活</font> -->
			</logic:equal>
	    </td>
	   	<td align="center">
	   		<input type="button" class="button" value="充值" onclick="location='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>';"/>
	   		<%-- <a href='managerAction.do?method=preModify&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		<c:if test="${userPermission=='superAdmin'}">
	   		<a href='../tluser/profile.do?method=basic&&uid=<bean:write name="item" property="userId"/>'>修改2</a>
	   		</c:if>
	   		<c:if test = "${userType=='1'||userType=='9'||userType=='10'}">
	   			<a href="#" onclick="delUser('managerAction.do?method=delete&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>','<bean:write name="item" property="loginName"/>')" ><img class="xtbImg ico-del" src="../images/trash.png"/></a>
	   		</c:if>
	   		<%if(Constants.chkFuncConfig("system.func.type.manage")){ %>
	   		<c:if test="${adminUserType eq '2'}">
			<a href="javascript:delItem(<bean:write name="item" property="userId"/>,<c:out value="${userType}"/>);"><img class="xtbImg ico-del" src="../images/trash.png"/></a>
			</c:if>
			<%} %> --%>
	   	</td>

	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

</html:form>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>