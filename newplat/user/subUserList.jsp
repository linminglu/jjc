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
function changeStatus(id,status){
	$.post("managerAction.do?method=changeStatus&userId="+ id , 
		function(data) {
				data = $.trim(data);
				if (data == "success") {
					if(status=='1'){
						$('#infoId'+id).html('<font color="red"><strong>无效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'0\');">开</a>');
					}else{
						$('#infoId'+id).html('<font color="green"><strong>有效</strong></font>&nbsp;&nbsp;<a class="button" href="javascript:void(0);" onclick="changeStatus('+id+',\'1\');">关</a>');
					}
				}
		});	
	}
</script>
</head>
<body>
<div class="bread">用户管理  &raquo; 
</div>
<html:form action="/managerAction.do?method=subUserList" method="post">
<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4' || loginUser.userType eq '20'}">
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			用户名/ID
			<html:text property="loginName" name="managerForm"/>
			<input type="submit" class="gbutton" value="查询" />
			<input type="button" class="gbutton" value="添加" onclick="location='managerAction.do?method=preAdd&userType=<c:out value="${userType}"/>';"/>
		</td>
	</tr>
</table>
</c:if>
</html:form>

<%
//分页
String totalCount =request.getAttribute("count")!=null?(String)request.getAttribute("count"):"0";
String userType =request.getAttribute("userType")!=null?(String)request.getAttribute("userType"):"";
String category =request.getAttribute("category")!=null?(String)request.getAttribute("category"):"";
String agentId =request.getAttribute("agentId")!=null?(String)request.getAttribute("agentId"):"0";
%>
<pg:pager 
	url="managerAction.do" 
	index="<%=index%>"
	items="<%=Integer.parseInt(totalCount)%>"
	maxPageItems="<%= maxPageItems %>"
	maxIndexPages="<%= maxIndexPages %>"
	isOffset="<%= isOffset %>"
	export="offset,currentPageNumber=pageNumber"
	scope="request"> 
	<pg:param name="method" value="subUserList"/>
	<pg:param name="agentId" value="<%=agentId%>"/>
	<bean:define id="totalLength" value="<%=totalCount%>"/>
	
<bean:size id="length" name="managerList"/>
<logic:notEmpty name="managerList">
<table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="sel">序号</th>
		<th>ID</th>
		<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
		<th>用户名</th>
		</c:if>
		<th>类型</th>
	    <th>余额</th>
	    <th>状态</th>	   	    

	    <th>IP</th>
	 	<!-- <th>登陆次数</th>  -->  
	    <c:if test = "${userType==1}">
	    	<th>上级代理</th>
	    	<th>银行名称</th>	
	    	<th>银行卡</th>
	    	<th>持卡人姓名</th>
	    </c:if>
	    <th>登录时间</th>
	    <th>注册日期</th>
	    <th>操作</th>

	</tr>
	<logic:iterate id="item" name="managerList" indexId="num">
	<pg:item>
	<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
		<td class="numbers"><%=num.intValue()+1 %></td>
	    <td align="center">
	        <bean:write name="item" property="userId" />
	    </td>
	    <c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
	    <td align="center">
	    	 <a href="managerAction.do?method=userView&userId=<c:out value='${item.userId}'/>&userType=<c:out value="${userType}"/>" /><c:out value="${item.loginName}"/></a>
	    </td>
	    </c:if>
	    <td align="center">
	       	<logic:equal name="item" property="userType" value="1">
					<font color="black"><strong>普通用户</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="2">
					<font color="black"><strong>数据管理员</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="13">
					<font color="green"><strong>初级代理</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="14">
					<font color="green"><strong>中级代理</strong></font>&nbsp;
			</logic:equal>
	       	<logic:equal name="item" property="userType" value="15">
					<font color="green"><strong>高级代理</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="userType" value="20">
					<font color="black"><strong>客服人员</strong></font>&nbsp;
			</logic:equal>
			<logic:equal name="item" property="userType" value="21">
					<font color="black"><strong>代理查看</strong></font>&nbsp;
			</logic:equal>
	    </td>
		    <td align="center">
			     	<bean:write name="item" property="money"/>
			</td>
		    <td align="center">
			  <logic:equal name="item" property="status" value="1">
				<font color="green"><strong>有效</strong></font>&nbsp;
				<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
				<a class="button" onclick="changeStatus(<bean:write name="item" property="userId"/>,'1');" href='javascript:void(0);'>关</a>&nbsp;
				</c:if>
			  </logic:equal>
			  <logic:equal name="item" property="status" value="0">
				<font color="red"><strong>无效</strong></font>&nbsp;
				<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
				<a class="button" onclick="changeStatus(<bean:write name="item" property="userId"/>,'0');" href='javascript:void(0);'>开</a>&nbsp;
				</c:if>
			  </logic:equal>
		      <logic:equal name="item" property="status" value="2">
				<font style="color: #666;"><strong>未激活</strong></font> 
			  </logic:equal>
		    </td>
	    <td align="center">
	        <bean:write name="item" property="lastLoginIp"/>
	    </td>
	  <!-- 
	    <td align="center">
	        <bean:write name="item" property="browseCount"/>
	    </td>-->
		<c:if test = "${userType==1}">
			<c:if test = "${item.agentName ne null}">
				<td align="center">
			        <bean:write name="item" property="agentName"/>
			    </td>
			</c:if>
			<c:if test = "${item.agentName eq null}">
					<td align="center">
				        无
				    </td>
			</c:if>
		 <td align="center">
		        <bean:write name="item" property="bankName"/>
		    </td>
		    <td align="center">
		        <bean:write name="item" property="accountNo"/>
		    </td>
		    <td align="center">
		        <bean:write name="item" property="bindName"/>
		    </td>
	    </c:if>
		<td align="center">
	      	<bean:write name="item" property="lastLoginDate" format="yyyy-MM-dd HH:mm:ss"/>
		</td>
		<td align="center">
	      	<bean:write name="item" property="registDateTime" format="yyyy-MM-dd"/>
		</td>

	   	<td align="center">
	   		<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'|| loginUser.userType eq '20'}">
			<input type="button" class="button" value="充值" 	onclick="location='managerAction.do?method=preModifyBalance&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			<input type="button" class="button" value="扣款" 	onclick="location='managerAction.do?method=preModifyMoney&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>&category=<c:out value="${category}"/>';"/>
			</c:if>
			<input type="button" class="button" value="报表" 	onclick="location='../agent/agentAction.do?method=userReport&userId=<bean:write name="item" property="userId"/>&usType=<c:out value="${item.userType}"/>';"/>
			<input type="button" class="button" value="投注明细" 	onclick="location='managerAction.do?method=betList&userId=<bean:write name="item" property="userId"/>';"/>
			<c:if test = "${loginUser.userType eq '2' || loginUser.userType eq '4'}">
	   			<a href='managerAction.do?method=preModifyUser&userId=<bean:write name="item" property="userId"/>&userType=<c:out value="${item.userType}"/>'><img class="xtbImg ico-edit" src="../images/modify.png"/></a>
	   		</c:if>
	   		<a href='managerAction.do?method=subUserList&agentId=<bean:write name="item" property="userId"/>'>下级会员</a>
	   	</td>
	</tr>
	</pg:item>
	</logic:iterate>
</table>
	</logic:notEmpty>
<logic:empty name="managerList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>

<logic:greaterThan name="totalLength" value="<%=maxPageItems+""%>">
	<jsp:include page="../template/pagination_template.jsp" flush="true"/>	    		
</logic:greaterThan>
</pg:pager>

</body>
</html>