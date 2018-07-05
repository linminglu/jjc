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
	window.location.href='../permission/loginAction.do?method=                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               init';
</script>
</center>
</logic:empty>
<logic:notEmpty name="loginUser">
<frameset name="mainFramey" rows="41,*" frameborder="no" border="0" framespacing="0">
	<frame name="allCashDetailTable" src="allCashDetailTable.jsp?userId=<%=request.getParameter("userId")%>" scrolling="no" noresize="noresize" />
    <frame name="allCashDetailContent" src="allCashDetailContent.jsp?userId=<%=request.getParameter("userId")%>" frameborder="0" scrolling="auto"/>
</frameset>	
</logic:notEmpty>
<body>
<!-- <div class="bread">各彩种投注明细</div> -->
</body>
</html:html>