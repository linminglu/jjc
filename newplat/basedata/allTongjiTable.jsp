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
<div class="layui-tab-brief ">
<ul class="layui-tab-title layui-tab-more" style="padding-right:0;">
    <li class="layui-this"><a href="../../gaBjPk10/bjPk10Action.do?method=betList" target="allTongjiContent">北京赛车PK10</a></li>
   	<li class=""><a href="../../gaJisuft/jisuftAction.do?method=betList" target="allTongjiContent">幸运飞艇</a></li>
    <li class=""><a href="../../gaJsft/jsftAction.do?method=betList" target="allTongjiContent">急速飞艇</a></li>
    <li class=""><a href="../../gaSfPk10/sfPk10Action.do?method=betList" target="allTongjiContent">急速赛车</a></li>
    <li class=""><a href="../../gaSfPk102/sfPk102Action.do?method=betList" target="allTongjiContent">三分PK10</a></li>
    <li class=""><a href="../../cqSsc/cqSscAction.do?method=betList" target="allTongjiContent">重庆时时彩</a></li>
	<li class=""><a href="../../tjSsc/tjSscAction.do?method=betList" target="allTongjiContent">天津时时彩</a></li>
	<li class=""><a href="../../xjSsc/xjSscAction.do?method=betList" target="allTongjiContent">新疆时时彩</a></li>
	<li class=""><a href="../../bjSsc/bjSscAction.do?method=betList" target="allTongjiContent">北京时时彩</a></li>
    <li class=""><a href="../../bj3/bj3Action.do?method=betList" target="allTongjiContent">北京三分彩</a></li>
    <li class=""><a href="../../five/fiveAction.do?method=betList" target="allTongjiContent">五分彩</a></li>
    <li class=""><a href="../../markSix/markSixAction.do?method=betList" target="allTongjiContent">六合彩</a></li>
    <li class=""><a href="../../sflhc/sflhcAction.do?method=betList" target="allTongjiContent">急速六合彩</a></li>
    <li class=""><a href="../../bjLu28/bjLu28Action.do?method=betList" target="allTongjiContent">PC蛋蛋</a></li>
    <li class=""><a href="../../xjpLu28/xjpLu28Action.do?method=betList" target=allTongjiContent>新加坡幸运28</a></li>
    <li class=""><a href="../../jsK3/jsK3Action.do?method=betList" target="allTongjiContent">江苏快三</a></li>
    <li class=""><a href="../../poker/pokerAction.do?method=betList" target="allTongjiContent">快乐扑克3</a></li>
    <li class=""><a href="../../gdPick11/gdPick11Action.do?method=betList" target="allTongjiContent">广东11选5</a></li>
    <li class=""><a href="../../jxPick11/jxPick11Action.do?method=betList" target="allTongjiContent">江西11选5</a></li>
    <li class=""><a href="../../gdK10/gdK10Action.do?method=betList" target="allTongjiContent">广东快乐十分</a></li>
	<li class=""><a href="../../gxK10/gxK10Action.do?method=betList" target="allTongjiContent">广西快乐十分</a></li>
	<li class=""><a href="../../cqK10/cqK10Action.do?method=betList" target="allTongjiContent">重庆快乐十分</a></li>
</ul>
</div>
</body>
</html:html>