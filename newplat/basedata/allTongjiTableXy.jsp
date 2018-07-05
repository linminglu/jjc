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
    <li class="layui-this"><a href="../../xyBjPk10/bjPk10Action.do?method=betList" target="allTongjiContent">北京赛车</a></li>
   	<li class=""><a href="../../xyXyft/xyftAction.do?method=betList" target="allTongjiContent">幸运飞艇</a></li>
    <li class=""><a href="../../xyJsft/jsftAction.do?method=betList" target="allTongjiContent">急速飞艇</a></li>
    <li class=""><a href="../../xySfPk10/sfPk10Action.do?method=betList" target="allTongjiContent">急速赛车</a></li>
    <li class=""><a href="../../xySfPk102/sfPk102Action.do?method=betList" target="allTongjiContent">三分PK10</a></li>
    <li class=""><a href="../../xyCqSsc/cqSscAction.do?method=betList" target="allTongjiContent">重庆时时彩</a></li>
	<li class=""><a href="../../xyTjSsc/tjSscAction.do?method=betList" target="allTongjiContent">天津时时彩</a></li>
	<li class=""><a href="../../xyXjSsc/xjSscAction.do?method=betList" target="allTongjiContent">新疆时时彩</a></li>
	<li class=""><a href="../../xyBjSsc/bjSscAction.do?method=betList" target="allTongjiContent">北京时时彩</a></li>
    <li class=""><a href="../../xyBj3/bj3Action.do?method=betList" target="allTongjiContent">北京三分彩</a></li>
    <li class=""><a href="../../xyFive/fiveAction.do?method=betList" target="allTongjiContent">五分彩</a></li>
    <li class=""><a href="../../xyMarkSix/markSixAction.do?method=betList" target="allTongjiContent">六合彩</a></li>
    <li class=""><a href="../../xySflhc/sflhcAction.do?method=betList" target="allTongjiContent">急速六合彩</a></li>
    <li class=""><a href="../../xyBjLu28/bjLu28Action.do?method=betList" target="allTongjiContent">PC蛋蛋</a></li>
    <li class=""><a href="../../xyXjpLu28/xjpLu28Action.do?method=betList" target=allTongjiContent>新加坡幸运28</a></li>
    <li class=""><a href="../../xyJsK3/jsK3Action.do?method=betList" target="allTongjiContent">江苏快3</a></li>
    <li class=""><a href="../../xyBjK3/bjK3Action.do?method=betList" target="allTongjiContent">北京快3</a></li>
    <li class=""><a href="../../xyPoker/pokerAction.do?method=betList" target="allTongjiContent">快乐扑克3</a></li>
    <li class=""><a href="../../xyGdPick11/gdPick11Action.do?method=betList" target="allTongjiContent">广东11选5</a></li>
    <li class=""><a href="../../xyJxPick11/jxPick11Action.do?method=betList" target="allTongjiContent">江西11选5</a></li>
    <li class=""><a href="../../xySdPick11/sdPick11Action.do?method=betList" target="allTongjiContent">山东11选5</a></li>
    <li class=""><a href="../../xyGdK10/gdK10Action.do?method=betList" target="allTongjiContent">广东快乐十分</a></li>
	<li class=""><a href="../../xyGxK10/gxK10Action.do?method=betList" target="allTongjiContent">广西快乐十分</a></li>
	<li class=""><a href="../../xyCqK10/cqK10Action.do?method=betList" target="allTongjiContent">重庆快乐十分</a></li>
</ul>
</div>
</body>
</html:html>