<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>远程网络教学平台   后台管理</title>
<%@ include file = "../common/inc_css.jsp"%>
</head>
<body>
<%
	int i=0;
%>
<script>
function openPlatform(ugid){
	var swidth=screen.width();
	var url="loginAction.do?method=loginUserGroup&userGroupId=" + userGroupId;
	var winproperty="width="+swidth+",height=97%,top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no";
	
	window.open(url,'bxsnet',winproperty);
}
</script>
<logic:empty name="loginUserGroupList">
	用户未被分配对应的用户组，请和管理员联系。
</logic:empty>

<logic:notEmpty name="loginUserGroupList">
<bean:size id="ugNum" name="loginUserGroupList"/>
<table border="0" cellpadding="4" cellpadding="4" align="center" width="300">
	<logic:iterate id="userGroup" name="loginUserGroupList">   
	<%
		if(i%4==0){
			out.println("<tr height='120'>");
		}
	%> 	
	<td align='center'>
		<a target="_top" href='../permission/loginAction.do?method=loginUserGroup&userGroupId=<bean:write name="userGroup" property="userGroupId"/>'>
	   		<img width="80" alt="<bean:write name="userGroup" property="userGroupName"/>" onerror="this.src='../images/loginicon/ug0.png'" src="../images/loginicon/ug<bean:write name='userGroup' property='userGroupId'/>.png" border="0" ><br><br>
			<bean:write name="userGroup" property="userGroupName"/>
		</a>
	<logic:equal value="1" name="ugNum">
		<script langague="javascript">
			location.href="../permission/loginAction.do?method=loginUserGroup&userGroupId=<bean:write name="userGroup" property="userGroupId"/>";
		</script>
	</logic:equal>
	</td>
	<%
		if(i % 4==3){
			out.println("</tr>");
		}
		i++;		
	%>
	</logic:iterate>				
</table>
</logic:notEmpty>
</body>
</html>