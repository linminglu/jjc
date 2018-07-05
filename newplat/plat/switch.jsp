<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  	<title>Untitled Document</title>
    <%@ include file="../common/inc_css.jsp"%>
  <script language="javascript">
	function menuHide()
	{
		document.all("icon_hide").style.display = "none";
		document.all("icon_show").style.display = "block";
		parent.document.all("middleFrame").cols="0,*";
	}
	function menuShow()
	{
		document.all("icon_show").style.display = "none";
		document.all("icon_hide").style.display = "block";
		parent.document.all("middleFrame").cols="201,*";
	}
</script>
</head>
<body leftmargin="0" rightmargin="0" bottommargin="0" topmargin="0" >

</body>
</html>