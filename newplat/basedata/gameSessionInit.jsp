<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_file.jsp"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
</head>
<logic:empty name="loginUser">
<center>
<script type="text/javascript">
	window.location.href='../permission/loginAction.do?method=init';
</script>
</center>
</logic:empty>
<logic:notEmpty name="loginUser">
<frameset name="mainFramey" rows="42,*" frameborder="no" border="0" framespacing="0">
	<frame name="gameSessionInitTable" src="gameSessionInitTable.jsp" scrolling="no" noresize="noresize" />
    <frame name="gameSessionInitContent" src="gameSessionInitContent.jsp" frameborder="0" scrolling="auto"/>
</frameset>	
</logic:notEmpty>
<body>
</body>
</html:html>