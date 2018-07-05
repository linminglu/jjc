<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_file.jsp"%>
<%@ page import="com.framework.util.ParamUtils"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
</head>
<script type="text/javascript">
$(document).ready(function(){
	var userId=getParameter("userId");
	if(userId){
		$("frame[name='betDetailListContent']").attr("src","betDetailListContent.jsp?userId="+userId);
	}
});
function getParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return decodeURIComponent(r[2]); return null; 
}
</script>
<logic:empty name="loginUser">
<center>
<script type="text/javascript">
	window.location.href='../permission/loginAction.do?method=init';
</script>
</center>
</logic:empty>
<logic:notEmpty name="loginUser">
<frameset name="mainFramey" rows="42,*" frameborder="no" border="0" framespacing="0">
	<frame name="betDetailListTable" src="betDetailListTable.jsp?userId=<%=request.getParameter("userId")%>" scrolling="no" noresize="noresize" />
    <frame name="betDetailListContent" src="betDetailListContent.jsp?userId=<%=request.getParameter("userId")%>" frameborder="0" scrolling="auto"/>
</frameset>	
</logic:notEmpty>
<body>
</body>
</html:html>