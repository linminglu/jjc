<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript"> 
//if(self.parent.frames.length != 0) self.parent.location=document.location.href; 
</script> 
<%
	String title = "";
	try{
		title = "管理平台";
	}catch(Exception e){
		out.print("<script type=\"text/javascript\">location=\"../permission/loginAction.do?method=init\";</script>");		
	}
%>
<title><%=title%></title>
</head>
<logic:empty name="loginUser">
	<center>
<!-- 	<a href="../permission/loginAction.do?method=init">点击登录</a> -->
<script type="text/javascript">
// 	window.location.href='../login.html';
	window.location.href='../permission/loginAction.do?method=init';
</script>
	</center>
</logic:empty>

<%--本系统中管理员的类型值为2 --%>
<logic:notEmpty name="loginUser">
<c:if test="${loginUser.userType eq '1'}">
<!--如果不是管理员跳转到主页，如果没有登录，跳转到登录页-->
<script type="text/javascript">
// 	window.location.href='../permission/homeAction.do?method=init';//home
	window.location.href='../';//home
</script>
</c:if>
<script type="text/javascript">
</script>
<frameset rows="62,*" frameborder="no" border="0" framespacing="0">
	<frame src="top.jsp" name="topFrame" scrolling="no"  noresize="noresize" />
	<frameset cols="242,*" name="middleFrame"  frameborder="no" border="0" framespacing="0">
		<frame src="left.jsp" name="fraLeft" scrolling="no" noresize="noresize" />
		<frameset name="mainFramey" cols="5,*" frameborder="no" border="0" framespacing="0">
			<frame src="switch.jsp" name="fraSwitch" scrolling="no" noresize="noresize" />
		      <frame name="fraContent" src="content.jsp" frameborder="0" scrolling="auto"/>
		</frameset>		    
	</frameset>
</frameset>
<noframes><body>
</body></noframes>
</logic:notEmpty>
</html>
