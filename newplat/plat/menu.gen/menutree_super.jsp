<%@page import="com.apps.Constants"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"> 
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../skins/tree.css" />
<script type="text/javascript">
function shmenu(obj){
	if(!obj) return;
	var parent = obj.parentNode;
	var uls = parent.getElementsByTagName("ul");
	if(uls && uls.length>0){
		var ul = uls[0];
		ul.style.display = (ul.style.display=="none"?"":"none");
	}
}
//-->
//添加以下代码
function initLiMenu(){
	var items = document.getElementsByTagName("a");
	if(!items || items.length<=0) return;
	for(var i=0;i<items.length;i++){
		items[i].onclick = function(){
			var lis = document.getElementsByTagName("a");
			for(var k=0;k<lis.length;k++){
				lis[k].className=(this==lis[k]?"liselected":"")
			}
		}
	}
}
window.onload = function(){
	initLiMenu();
}
</script>
</head>
<body>
<!-- 管理员菜单 -->
<div class="treediv">
	<div class="treebox">
		<h2 onclick="shmenu(this)">用户管理</h2>
		<ul>
			<li><a href="../../user/managerAction.do?method=init&userType=2"  target="fraContent">管理员</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=1"  target="fraContent">注册用户</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=11"  target="fraContent">试玩用户</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=12&category=1" target="fraContent">一级代理</a></li>
		    <li><a href="../../user/managerAction.do?method=init&userType=12&category=2" target="fraContent">二级代理</a></li>
<%--			<li><a href="../../user/managerAction.do?method=todayRegist" target="fraContent">今日注册用户</a></li>--%>		
<%--			<li><a href="../../basedata/baseDataAction.do?method=initSessionNo" target="fraContent">初始化期号</a></li>--%>

			<li><a href="../../agent/agentAction.do?method=tradeDetailList" target="fraContent">资金明细</a></li>
			<!-- <li><a href="../../dcb/dcbAction.do?method=dateManager" target="fraContent">双色球时间管理</a></li> -->			
		</ul>
	</div>
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">开奖管理</h2>
		<ul>
			<li><a href="../../cqSsc/cqSscAction.do?method=init" target="fraContent">重庆时时彩</a></li>
			<li><a href="../../gdPick11/gdPick11Action.do?method=init" target="fraContent">广东11选5</a></li>
			<li><a href="../../dcb/dcbAction.do?method=init" target="fraContent">双色球</a></li>
			<li><a href="../../three/threeAction.do?method=init" target="fraContent">三分彩</a></li>
			<li><a href="../../five/fiveAction.do?method=init" target="fraContent">五分彩</a></li>
		</ul>
	</div>
	
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">充值管理</h2>
		<ul>
<%--			<!-- <li><a href="../../card/cardAction.do?method=initCardRechargeItem" target="fraContent">充值套餐</a></li> -->--%>
<%--			<li><a href="../../card/cardAction.do?method=initROrder" target="fraContent">在线订单</a></li>--%>
			<li><a href="../../card/cardAction.do?method=initofflineOrder" target="fraContent">线下订单</a></li>
		</ul>
	</div>
	
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">广告管理</h2>
		<ul>
			<li><a href="../../advert/advertAction.do?method=init&adType=1" target="fraContent">首页轮播</a></li>
<%--			<li><a href="../../advert/advertAction.do?method=initActivity" target="fraContent">活动</a></li>--%>
			<li><a href="../../advert/advertAction.do?method=init&adType=2" target="fraContent">通知</a></li> 
		
		</ul>
	</div>
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">资讯管理</h2>
		<ul>
			<li><a href="../../news/newsAction.do?method=initNews" target="fraContent"> 资讯列表</a></li>
		</ul>
	</div>

	<div class="treebox">
		<h2 onclick="shmenu(this)">统计管理</h2>
		<ul>
					<li><a href="../../agent/agentAction.do?method=agentReport" target="fraContent">代理报表</a></li>
<%--			<li><a href="../../cqSsc/cqSscAction.do?method=betList" target="fraContent">重庆时时彩</a></li>--%>
<%--			<li><a href="../../gdPick11/gdPick11Action.do?method=betList" target="fraContent">广东11选5</a></li>--%>
					<li><a href="../../basedata/baseDataAction.do?method=totalBet" target="fraContent">今日投注统计</a></li>
					<li><a href="../../basedata/baseDataAction.do?method=dayBetCount" target="fraContent">每日投注统计</a></li>
		</ul>
	</div>
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">投注管理</h2>
		<ul>
<%--			<li><a href="../../cqSsc/cqSscAction.do?method=betManager" target="fraContent">重庆时时彩投注</a></li>--%>
<%--			<li><a href="../../gdPick11/gdPick11Action.do?method=betManager" target="fraContent">广东11选5</a></li>--%>
			<li><a href="../../agent/agentAction.do?method=betDetailList" target="fraContent">投注明细</a></li>

		</ul>
	</div>
<div class="treebox">
		<h2 onclick="shmenu(this)">提现管理</h2>
		<ul>
			<li><a href="../../cash/cashAction.do?method=init" target="fraContent">提现管理</a></li>
<%--			<li><a href="../../cash/cashAction.do?method=initBank" target="fraContent">银行信息</a></li>--%>
		</ul>
	</div> 

	
	<div class="treebox">
		<h2 onclick="shmenu(this)">系统管理</h2>
		<ul>
			<!--
			<li><a href="../../basedata/baseDataAction.do?method=feedbackInit" target="fraContent">留言管理</a></li> -->
			<li><a href="../../message/messageAction.do?method=init" target="fraContent">消息管理</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=verInit" target="fraContent">版本管理</a></li>
<%--			<li><a href="../../basedata/baseDataAction.do?method=paySet" target="fraContent">支付设置</a></li>--%>
			<li><a href="../../param/paramAction.do?method=init" target="fraContent">系统设置</a></li>
<!-- 			<li><a href="../../basedata/baseDataAction.do?method=pushInit" target="fraContent">推送管理</a></li> -->
	<!-- 		<li><a href="../../sms/smsAction.do?method=statistics" target="fraContent">短信统计</a></li> -->
		</ul>
	</div>
	
</div>
</body>
</html>
