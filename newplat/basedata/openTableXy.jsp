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
	<li class="layui-this"><a href="../../xyBjPk10/bjPk10Action.do?method=init" target="openContent">北京赛车</a></li>
	<li><a href="../../xyCqSsc/cqSscAction.do?method=init" target="openContent">重庆时时彩</a></li>
	<li><a href="../../xyTjSsc/tjSscAction.do?method=init" target="openContent">天津时时彩</a></li>
	<li><a href="../../xyXjSsc/xjSscAction.do?method=init" target="openContent">新疆时时彩</a></li>
	<li><a href="../../xyBjSsc/bjSscAction.do?method=init" target="openContent">北京时时彩</a></li>
	<li><a href="../../xyFive/fiveAction.do?method=init" target="openContent">五分彩</a></li>
	<li><a href="../../xyGdK10/gdK10Action.do?method=init" target="openContent">广东快乐10分</a></li>
	<li><a href="../../xyBjLu28/bjLu28Action.do?method=init" target="openContent">PC蛋蛋</a></li>
	<li><a href="../../xyXjpLu28/xjpLu28Action.do?method=init" target="openContent">新加坡28</a></li>
	<li><a href="../../xyBj3/bj3Action.do?method=init" target="openContent">北京三分彩</a></li>
	<li><a href="../../xyPoker/pokerAction.do?method=init" target="openContent">快乐扑克3</a></li>
	<li><a href="../../xyJsK3/jsK3Action.do?method=init" target="openContent">江苏快3</a></li>
	<li><a href="../../xyGdPick11/gdPick11Action.do?method=init" target="openContent">广东11选5</a></li>
	<li><a href="../../xyJxPick11/jxPick11Action.do?method=init" target="openContent">江西11选5</a></li>
	<li><a href="../../xyGxK10/gxK10Action.do?method=init" target="openContent">广西快乐10分</a></li>
	<li><a href="../../xyCqK10/cqK10Action.do?method=init" target="openContent">重庆快乐10分</a></li>
	<li><a href="../../xySfPk10/sfPk10Action.do?method=init" target="openContent">急速赛车</a></li>
	<li><a href="../../xySfPk102/sfPk102Action.do?method=init" target="openContent">三分PK10</a></li>
	<li><a href="../../xyJsft/jsftAction.do?method=init" target="openContent">急速飞艇</a></li>
	<li><a href="../../xyXyft/xyftAction.do?method=init" target="openContent">幸运飞艇</a></li>
	<li><a href="../../xyMarkSix/markSixAction.do?method=init" target="openContent">六合彩</a></li>
	<li><a href="../../xySflhc/sflhcAction.do?method=init" target="openContent">急速六合彩</a></li>
</ul>
</div>
</body>
</html:html>