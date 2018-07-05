<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@ include file = "../common/inc_file.jsp"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<script type="text/javascript">

function initLiMenu(){
	$(".layui-tab-title li").bind("click", function(){
		$(".layui-tab-title li").removeClass('layui-this');
		$(this).addClass("layui-this");
	});
}
window.onload = function(){
	initLiMenu();
}

</script>
</head>
<body>
<!-- <div class="bread">彩票管理&raquo;各彩种开奖管理</div> -->
<div class="layui-tab-brief ">
<label class="tab-prefix">彩票管理&raquo;各彩种开奖管理</label>
<ul class="layui-tab-title layui-tab-more" style="padding-right:0;">
    <li class="layui-this"><a href="../../basedata/openTableXy.jsp?type=1" target="openTable">信用</a></li>
   	<li class=""><a href="../../basedata/openTableGf.jsp?type=2" target="openTable">官方</a></li>
</ul>
</div>
</body>
</html:html>