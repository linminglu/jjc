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
		<h2 onclick="shmenu(this)">快捷管理</h2>
		<ul>
			<li><a href="../../user/managerAction.do?method=userList"  target="fraContent">用户管理</a></li>
			<li><a href="../../user/managerAction.do?method=balance" target="fraContent">资金明细</a></li>
			<li><a href="../../agent/betDetailList.jsp" target="fraContent">投注明细</a></li>
			<!-- <li><a href="../../user/managerAction.do?method=userListByBalance"  target="fraContent">余额排序</a></li> -->
		</ul>
	</div>
	
	<div class="treebox">
		<h2 onclick="shmenu(this)">竞猜管理</h2>
		<ul>
			<li><a href="../../jc/typeAction.do?method=init"  target="fraContent">分类管理</a></li>
			<li><a href="../../jc/jcTeamAction.do?method=init"  target="fraContent">队伍管理</a></li>
			<li><a href="../../jc/matchAction.do?method=init" target="fraContent">赛事管理</a></li>
			<li><a href="../../jc/fieldAction.do?method=init" target="fraContent">比赛局管理</a></li>
			<!-- <li><a href="../../jc/matchAction.do?method=initMatch" target="fraContent">开奖管理</a></li> -->
		</ul>
	</div>
	<!-- <div class="treebox">
		<h2 onclick="shmenu(this)">统计管理</h2>
		<ul>
			<li><a href="../../user/managerAction.do?method=dailyStatisticsList" target="fraContent">用户统计</a></li>
			<li><a href="../../user/managerAction.do?method=agentStatisticsList" target="fraContent">代理统计</a></li>
			<li><a href="../../agent/agentAction.do?method=report" target="fraContent">网站总报表</a></li>
			<li><a href="../../user/managerAction.do?method=userMoneyReport" target="fraContent">用户盈亏统计</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=totalBet" target="fraContent">今日投注统计</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=dayBetCount" target="fraContent">每日投注统计</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=userDayPayoff" target="fraContent">用户今日盈利榜</a></li>
			<li><a href="../../basedata/allTongjiList.jsp" target="fraContent">各彩种统计管理</a></li>
			<li><a href="../../basedata/allTongjiDetailList.jsp" target="fraContent">各彩种投注明细</a></li>
			<li><a href="../../agent/agentAction.do?method=agentReport" target="fraContent">代理报表</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=totalBet" target="fraContent">今日投注统计</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=dayBetCount" target="fraContent">每日投注统计</a></li>
		</ul>
	</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">用户管理</h2>
		<ul>
			<li><a href="../../user/managerAction.do?method=agentList" target="fraContent">代理列表</a></li>
			<li><a href="../../user/managerAction.do?method=todayRegist" target="fraContent">今日注册用户</a></li>		
			<li><a href="../../user/managerAction.do?method=userLevelList" target="fraContent">用户等级管理</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=11"  target="fraContent">试玩用户</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=12&category=1" target="fraContent">一级代理</a></li>
			<li><a href="../../user/managerAction.do?method=init&userType=12&category=2" target="fraContent">二级代理</a></li>
			<li><a href="../../dcb/dcbAction.do?method=dateManager" target="fraContent">双色球时间管理</a></li>	
			<li><a href="../../markSix/markSixAction.do?method=dateManager" target="fraContent">六合彩时间管理</a></li>
		</ul>
	</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">充值/提现管理</h2>
		<ul>
            <li><a href="../../cash/cashAction.do?method=init" target="fraContent">提现管理</a></li>
            <li><a href="../../card/cardAction.do?method=initofflineOrder" target="fraContent">线下订单</a></li>
            <li><a href="../../card/cardAction.do?method=initROrder" target="fraContent">在线订单</a></li>
			<li><a href="../../card/cardAction.do?method=initCardRechargeItem" target="fraContent">充值套餐</a></li>
			<li><a href="../../card/rechargeWayAction.do?method=initRechargeWay&passageWayId=1" target="fraContent">充值通道一</a></li>
			<li><a href="../../card/rechargeWayAction.do?method=initRechargeWay&passageWayId=2" target="fraContent">充值通道二</a></li>
			<li><a href="../../card/rechargeWayAction.do?method=initRechargeWay&passageWayId=3" target="fraContent">充值通道三</a></li>
			<li><a href="../../card/rechargeWayAction.do?method=initRechargeWay&passageWayId=4" target="fraContent">充值通道四</a></li>
			<li><a href="../../card/rechargeWayAction.do?method=initRechargeWay&passageWayId=5" target="fraContent">充值通道五</a></li>
			<li><a href="../../cash/cashAction.do?method=initBank" target="fraContent">银行信息</a></li>
		</ul>
	</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">彩票管理</h2>
		<ul>
			<li><a href="../../param/paramAction.do?method=gameOpenSetting" target="fraContent">开奖设置</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=lotteryCatList" target="fraContent">彩种管理</a></li>
			<li><a href="../../basedata/baseDataAction.do?method=initSessionNo" target="fraContent">初始化期号</a></li>
			<li><a href="../../game/gaAction.do?method=initGameUrl" target="fraContent">开奖网址切换</a></li>
			<li><a href="../../basedata/openList.jsp" target="fraContent">各彩票开奖管理</a></li>
			<li><a href="../../xyMarkSix/markSixAction.do?method=dateManager" target="fraContent">六合彩时间管理</a></li>
		</ul>
	</div>-->
	<div class="treebox">
		<h2 onclick="shmenu(this)">广告/通知管理</h2>
		<ul>
			<li><a href="../../advert/advertAction.do?method=init&adType=2" target="fraContent">首页通知</a></li> 
			<li><a href="../../advert/advertAction.do?method=init&adType=1" target="fraContent">首页轮播</a></li>
<!-- 			<li><a href="../../notice/noticeAction.do?method=init" target="fraContent">公告管理</a></li> -->
<!-- 			<li><a href="../../news/newsAction.do?method=initNews" target="fraContent">资讯列表</a></li> -->
			<li><a href="../../advert/promotionAction.do?method=init" target="fraContent">优惠活动</a></li>
<!-- 			<li><a href="../../notice/noticeAction.do?method=init" target="fraContent">通知管理</a></li> -->
<!-- 			<li><a href="../../message/messageAction.do?method=init" target="fraContent">消息管理</a></li>  -->
		</ul> 
	</div>
	<!--<div class="treebox">
		<h2 onclick="shmenu(this)">活动管理</h2>
		<ul>
			<li><a href="../../advert/promotionAction.do?method=init" target="fraContent">优惠活动</a></li>
			<li><a href="../../basedata/lotterySettingAction.do?method=zpInit" target="fraContent">转盘设置</a></li>
			<li><a href="../../basedata/lotterySettingAction.do?method=hbInit" target="fraContent">红包设置</a></li> 
		</ul>
	</div> -->
</div>
</body>
</html>
