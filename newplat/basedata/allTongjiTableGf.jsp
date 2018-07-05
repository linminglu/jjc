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
	<li class="layui-this"><a href="../../gfBjPk10/bjPk10Action.do?method=betList" target="allTongjiContent">北京赛车PK10</a></li>
   	<li class=""><a href="../../gfXyFt/xyFtAction.do?method=betList" target="allTongjiContent">幸运飞艇</a></li>
    <li class=""><a href="../../gfJsFt/jsFtAction.do?method=betList" target="allTongjiContent">急速飞艇</a></li>
    <li class=""><a href="../../gfSfPk10/sfPk10Action.do?method=betList" target="allTongjiContent">急速赛车</a></li>
    <li class=""><a href="../../gfSfPk102/sfPk102Action.do?method=betList" target="allTongjiContent">三分PK10</a></li>
    <li class=""><a href="../../gfCqSsc/cqSscAction.do?method=betList" target="allTongjiContent">重庆时时彩</a></li>
	<li class=""><a href="../../gfTjSsc/tjSscAction.do?method=betList" target="allTongjiContent">天津时时彩</a></li>
	<li class=""><a href="../../gfXjSsc/xjSscAction.do?method=betList" target="allTongjiContent">新疆时时彩</a></li>
	<li class=""><a href="../../gfBjSsc/bjSscAction.do?method=betList" target="allTongjiContent">北京时时彩</a></li>
    <li class=""><a href="../../gfThree/threeAction.do?method=betList" target="allTongjiContent">三分彩</a></li>
    <li class=""><a href="../../gfFive/fiveAction.do?method=betList" target="allTongjiContent">五分彩</a></li>
    <li class=""><a href="../../gfGdPick11/gdPick11Action.do?method=betList" target="allTongjiContent">广东11选5</a></li>
    <li class=""><a href="../../gfJxPick11/jxPick11Action.do?method=betList" target="allTongjiContent">江西11选5</a></li>
    <li class=""><a href="../../gfSdPick11/sdPick11Action.do?method=betList" target="allTongjiContent">山东11选5</a></li>
    <li class=""><a href="../../gfShPick11/shPick11Action.do?method=betList" target="allTongjiContent">上海11选5</a></li>
    <li class=""><a href="../../gfAhPick11/ahPick11Action.do?method=betList" target="allTongjiContent">安徽11选5</a></li>
    <li class=""><a href="../../gfJsK3/jsK3Action.do?method=betList" target="allTongjiContent">江苏快3</a></li>
    <li class=""><a href="../../gfAhK3/ahK3Action.do?method=betList" target="allTongjiContent">安徽快3</a></li>
    <li class=""><a href="../../gfBjK3/bjK3Action.do?method=betList" target="allTongjiContent">北京快3</a></li>
    <li class=""><a href="../../gfGxK3/gxK3Action.do?method=betList" target="allTongjiContent">广西快3</a></li>
    <li class=""><a href="../../gfHubK3/hubK3Action.do?method=betList" target="allTongjiContent">湖北快3</a></li>
    <li class=""><a href="../../gfJxK3/jxK3Action.do?method=betList" target="allTongjiContent">江西快3</a></li>
    <li class=""><a href="../../gfJlK3/jlK3Action.do?method=betList" target="allTongjiContent">吉林快3</a></li>
    <li class=""><a href="../../gfShK3/shK3Action.do?method=betList" target="allTongjiContent">上海快3</a></li>
</ul>
</div>
</body>
</html:html>