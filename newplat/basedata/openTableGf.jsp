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
function loadContent(){
	$(".layui-tab-title .layui-this a")[0].click()
}
window.onload = function(){
	initLiMenu();
	loadContent();
}

</script>
</head>
<body>
<div class="layui-tab-brief ">
<ul class="layui-tab-title layui-tab-more" style="padding-right:0;">
    <li class="layui-this"><a href="../gfCqSsc/cqSscAction.do?method=init" target="openContent">重庆时时彩</a></li>
    <li><a href="../gfBjSsc/bjSscAction.do?method=init" target="openContent">北京时时彩</a></li>
    <li><a href="../gfXjSsc/xjSscAction.do?method=init" target="openContent">新疆时时彩</a></li>
    <li><a href="../gfTjSsc/tjSscAction.do?method=init" target="openContent">天津时时彩</a></li>
	<li><a href="../gfThree/threeAction.do?method=init" target="openContent">三分彩</a></li>
	<li><a href="../gfFive/fiveAction.do?method=init" target="openContent">五分彩</a></li>
	<li><a href="../gfGdPick11/gdPick11Action.do?method=init" target="openContent">广东11选5</a></li>
	<li><a href="../gfJxPick11/jxPick11Action.do?method=init" target="openContent">江西11选5</a></li>
	<li><a href="../gfSdPick11/sdPick11Action.do?method=init" target="openContent">山东11选5</a></li>
	<li><a href="../gfShPick11/shPick11Action.do?method=init" target="openContent">上海11选5</a></li>
	<li><a href="../gfAhPick11/ahPick11Action.do?method=init" target="openContent">安徽11选5</a></li>
	<li><a href="../gfJsK3/jsK3Action.do?method=init" target="openContent">江苏快3</a></li>
	<li><a href="../gfAhK3/ahK3Action.do?method=init" target="openContent">安徽快3</a></li>
	<li><a href="../gfBjK3/bjK3Action.do?method=init" target="openContent">北京快3</a></li>
	<li><a href="../gfGxK3/gxK3Action.do?method=init" target="openContent">广西快3</a></li>
	<li><a href="../gfHubK3/hubK3Action.do?method=init" target="openContent">湖北快3</a></li>
	<li><a href="../gfJxK3/jxK3Action.do?method=init" target="openContent">江西快3</a></li>
	<li><a href="../gfJlK3/jlK3Action.do?method=init" target=openContent>吉林快3</a></li>
	<li><a href="../gfShK3/shK3Action.do?method=init" target="openContent">上海快3</a></li>
	<li><a href="../gfBjPk10/bjPk10Action.do?method=init" target="openContent">北京赛车</a></li>
	<!-- <li><a href="../basedata/baseDataAction.do?method=open" target="openContent">手动开奖</a></li> -->
	<!-- <li><a href="../basedata/baseDataAction.do?method=openGaBetSponsor" target="openContent">期号开奖</a></li> -->
</ul>
</div>
</body>
</html:html>