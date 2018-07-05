var lotteryObj={
	optionArray:null,
	curSession:null,//当前session数据，接口获取更新
	curPlay:null,//当前玩法
	betPrice:1,//每注单价
	gameType:205,//彩种类型
	betItems:[],//下注清单
	betStatus:true,//下注状态
	playType:{//玩法对象t:玩法类型  m:赔率   n:最少选几个号(没有则填0)
		wxt:{t:8,m:20440,n:5},
		wxz:{t:7,m:100000,n:5},
		sxz:{t:6,m:1000,n:3},
		sxzs:{t:5,m:320,n:2},
		sxzl:{t:4,m:160,n:3},
		exz:{t:3,m:100,n:2},
		exzu:{t:2,m:50,n:2},
		yx:{t:1,m:10,n:1},
		dxds:{t:0,m:4,n:2},
		getMoney:function(type){
			for(key in lotteryObj.playType){
				if(lotteryObj.playType[key].t==type) return lotteryObj.playType[key].m;
			}
		},
		getN:function(type){
			for(key in lotteryObj.playType){
				if(lotteryObj.playType[key].t==type) return lotteryObj.playType[key].n;
			}
		}
	},
	init:function(){//入口初始化
		//var mask=move.createLoading();
		//mask.show();

		lotteryObj.panel.init();
		lotteryObj.playSelector.init();

		//初始化绑定
		lotteryObj.bindNumbers();
		//启动数据请求计时器
		lotteryObj.dataTimer.init();

		//玩法
		lotteryObj.gameOption();
	},
	gameOption:function(){// 玩法
		$.ajax({
			type: "POST",
			url:move.gameOption,
			dataType:"json",
			data:{
				mw:baseObj.mw({gameType:lotteryObj.gameType})//加密参数
			},
			success: function(ret){
				if(ret && ret.code=="200"){
					var items=ret.data.items;
					if(items){
						$(".ga-play-item .subtitle").each(function(i){
							$(this).html(items[i].betRate);
						});
					}
				}
			},
			error:function (jqXHR, textStatus, errorThrown) {
				console.log("dataTimer.exec::reqSession::[ERR]"+errorThrown);
			}
		});
	},
	reqSession:function(){//请求数据
		$.ajax({
			type: "POST",
			url:move.bjSsc_currentTime,
			dataType:"json",
			data:{mw:baseObj.mw({u:move.user.u})},//加密参数
			success: function(ret){
				if(ret && ret.code=="200"){
					lotteryObj.curSession = ret.data;//当前数据
					lotteryObj.update();//更新分发
				}
				//console.log("dataTimer.exec::reqSession::"+ret.code);
			},
			error:function (jqXHR, textStatus, errorThrown) {
				//mask.close();
				console.log("dataTimer.exec::reqSession::[ERR]"+errorThrown);
			}
		});
	},
	update:function(){
		lotteryObj.panel.open();
		lotteryObj.updateOpenInfo(lotteryObj.curSession);
	},
	updateOpenInfo(obj){//更新界面信息
		if(!obj) return;
		$(".open-ps").html(obj.preSessionNo+"期");
		$(".open-pcur").html("距"+obj.sessionNo+"期截止");
		if(obj.resultItems && obj.resultItems.length==5){//上期开奖号
			$(".open-pn").html("<span class=\"open-num\">"+move.widget.fmtPick11OpenNo(obj.resultItems)+"</span>");
		}else{
			$(".open-pn").html("<span class=\"open-wait\">等待开奖</span>");
		}
		if(obj.betTime) lotteryObj.betTimer.reset(obj.betTime);//重置启动下注计时器
		if(obj.openTime) lotteryObj.openTimer.reset(obj.openTime);//重置开奖计时器
		if(obj.resultItems && obj.resultItems.length>0) lotteryObj.dataTimer.clear();//清除数据计时器

	},
	updateBetTiming:function(time){//更新投注倒计时信息
		var hms = move.widget.fmtTimeHHMMSSObj(time);
		$(".open-hh").html(hms.h);
		$(".open-mm").html(hms.m);
		$(".open-ss").html(hms.s);
	},
	openlist:function(){
		var mask=move.createLoading();
		mask.show();
		var map = {};
		map['pageIndex'] =move.pageIndex;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.bjSsc_openList,
			dataType:"json",
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var data=ret.data;
				var code=ret.code;
				var msg=ret.msg;
				if(code=="200"){
					var items=data.items;
					var html='';
					if(items.length>0){
						for(var i=0;i<items.length;i++){
							var data=items[i];
							var resultItems=data.resultItems;
							var sessionNo=data.sessionNo;
							var openTime=data.openTime;
							var openResult=data.openResult;
							var openhtml="";
							for (var j = 0; j < resultItems.length; j++) {
								var num=resultItems[j];
								openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
							}
							html=html+'<li class="mui-table-view-cell"><div class="title-box"><div class="title">北京时时彩</div>';
							html=html+'<div class="time">第'+sessionNo+'期 '+openTime+'</div></div>';
							html=html+'<div class="open-number">'+openhtml+'</div></a></li>';
						}
						$("#dataList").append(html);
						move.pageIndex++;
	    				mui('#content').pullRefresh().endPullupToRefresh();
					}else{
						if(move.pageIndex==0){
							$('.not-data').show();
							$('#content').hide();
							mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}else{
	    					mui('#content').pullRefresh().endPullupToRefresh(true);
	    				}
					}
				}else{
					if(move.pageIndex==0){
						$('.not-data').show();
						$('#content').hide();
						mui('#content').pullRefresh().endPullupToRefresh(true);
    				}else{
    					mui('#content').pullRefresh().endPullupToRefresh(true);
    				}
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				if(move.pageIndex==0){
					$('.not-data').show();
					$('#content').hide();
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}else{
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}
				mask.close();
			}
		});
	},
}

/**
 *选号
 *各玩法选号判断排列组合计算等
 */
lotteryObj.selector = {
	rateData:{},//保存玩法的倍数选择
	confirm:function(){//选号触发
 		lotteryObj.curPlay = lotteryObj.getCurrentPlay();//当前玩法类型
 		if(lotteryObj.selector.check(lotteryObj.curPlay,true)){//验证
 			mui.alert("ok,next betting...");
 		}
	},
	check:function(curPlay,isTips){//验证当前选号(当前玩法对象，是否验证提示)
		if(!curPlay) curPlay = lotteryObj.getCurrentPlay();
		var selArr = [];//已选号码
		$("#playItem"+curPlay.t).find(".ga-ball li>a.active").each(function(){selArr.push($(this).text())});
		if(selArr.length==0){
			if(isTips) mui.alert("至少选择1注号码才能投注！");return false;
		}
		switch(parseInt(curPlay.t)){
			case lotteryObj.playType.wxt.t://五星通选
				var arrW=[],arrQ=[];arrB=[];arrS=[];arrG=[];//万位千位百位十位个位
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){arrW.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){arrQ.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){arrB.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[3]).find(".ga-ball li>a.active").each(function(){arrS.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[4]).find(".ga-ball li>a.active").each(function(){arrG.push($(this).text())});
				if(arrW.length==0 || arrQ.length==0 || arrB.length==0 || arrS.length==0 || arrG.length==0){
					if(isTips) mui.alert("万位、千位、百位、十位、个位至少选择1个号码！");
					return false;
				}
				var count = arrW.length*arrQ.length*arrB.length*arrS.length*arrG.length;
				//重置直选模式
				selArr = {t:curPlay.t,c:count,arrW:arrW,arrQ:arrQ,arrB:arrB,arrS:arrS,arrG:arrG};
				break;
			case lotteryObj.playType.wxz.t://五星直选
				var arrW=[],arrQ=[];arrB=[];arrS=[];arrG=[];//万位千位百位十位个位
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){arrW.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){arrQ.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){arrB.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[3]).find(".ga-ball li>a.active").each(function(){arrS.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[4]).find(".ga-ball li>a.active").each(function(){arrG.push($(this).text())});
				if(arrW.length==0 || arrQ.length==0 || arrB.length==0 || arrS.length==0 || arrG.length==0){
					if(isTips) mui.alert("万位、千位、百位、十位、个位至少选择1个号码！");
					return false;
				}
				var count = arrW.length*arrQ.length*arrB.length*arrS.length*arrG.length;
				//重置直选模式
				selArr = {t:curPlay.t,c:count,arrW:arrW,arrQ:arrQ,arrB:arrB,arrS:arrS,arrG:arrG};
				break;
			case lotteryObj.playType.sxz.t://三星直选
				var arrB=[];arrS=[];arrG=[];//百位十位个位
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){arrB.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){arrS.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){arrG.push($(this).text())});
				if(arrB.length==0 || arrS.length==0 || arrG.length==0){
					if(isTips) mui.alert("百位、十位、个位至少选择1个号码！");
					return false;
				}
				var count = arrB.length*arrS.length*arrG.length;
				//重置直选模式
				selArr = {t:curPlay.t,c:count,arrB:arrB,arrS:arrS,arrG:arrG};
				break;
			case lotteryObj.playType.sxzs.t://三星组三
				if(selArr.length<lotteryObj.playType.sxzs.n){
					if(isTips) mui.alert("至少选择3个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.sxzl.t://三星组六
				if(selArr.length<lotteryObj.playType.sxzl.n){
					if(isTips) mui.alert("至少选择3个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.exz.t://二星直选
				var arrS=[];arrG=[];//十位个位
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){arrS.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){arrG.push($(this).text())});
				if(arrS.length==0 || arrG.length==0){
					if(isTips) mui.alert("十位、个位至少选择1个号码！");
					return false;
				}
				var count = arrS.length*arrG.length;
				//重置直选模式
				selArr = {t:curPlay.t,c:count,arrS:arrS,arrG:arrG};
				break;
			case lotteryObj.playType.exzu.t://二星组选
				if(selArr.length<lotteryObj.playType.exzu.n){
					if(isTips) mui.alert("至少选择2个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.yx.t://一星
				if(selArr.length<lotteryObj.playType.yx.n){
					if(isTips) mui.alert("至少选择1个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dxds.t://大小单双
				var arrS=[];arrG=[];//十位个位
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){arrS.push($(this).text())});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){arrG.push($(this).text())});
				if(arrS.length==0 || arrG.length==0){
					if(isTips) mui.alert("十位、个位至少选择1个号码！");
					return false;
				}
				var count = arrS.length*arrG.length;
				//重置直选模式
				selArr = {t:curPlay.t,c:count,arrS:arrS,arrG:arrG};
				break;
		}

		console.log("bet stat["+typeof selArr+"]:"+JSON.stringify(selArr));

		//仅验证提示则直接返回
		if(isTips) return true;
		return selArr;
	},
	preview:function(){//更新投注预计
		var curPlay = lotteryObj.getCurrentPlay();
		var selArr = lotteryObj.selector.check(curPlay);//选号
		var rate = lotteryObj.selector.getRate(curPlay);//默认为1整体设置倍数
		lotteryObj.selector.setRateVal(curPlay);
		if(selArr){
			var betObj = lotteryObj.selector.calcBet(curPlay,selArr,rate);
			lotteryObj.selector.showInfo(true,false,betObj);
		}else{
			lotteryObj.selector.showInfo(true,true,betObj);
		}
	},
	showInfo:function(add,reset,obj){//是否加号,是否重置,参数对象
		if(add){
			$(".ga-bottom-bar .lbar .totalnum").html(reset?"共0注，0元":"共"+obj.num+"注，"+obj.money+"元<span class=\"confirm-btn\">确定</span>");
			$(".ga-bottom-bar .lbar .betnum").html(reset?"":move.widget.arr2StrSpace(obj.selArr));
			$(".ga-bottom-bar .lbar .add").css("display",reset?"none":"block");
			$(".ga-bottom-bar .lbar").css("backgroundColor",reset?"#252625":"#DC3B40");
			$(".ga-bottom-bar .rbar .arrow").css("borderLeftColor",reset?"#252625":"#DC3B40");
			//$(".ga-bottom-rate").css("display",reset?"none":"block");
			$(".ga-bottom-rate").css("display","none");
			$(".ga-bottom-bar .list-act").css("display","block");
			$(".ga-bottom-bar .list-bet").css("display","none");
			$(".ga-bottom-bar .rbar").css("backgroundColor","#575858");
		}else{
			$(".ga-bottom-bar .lbar .totalnum").html("方案共"+obj.num+"注"+(obj.rate>1?"x"+obj.rate+"倍":"")+"，"+obj.money+"元");
			$(".ga-bottom-bar .lbar .betnum").html("");
			$(".ga-bottom-bar .lbar .add").css("display","none");
			$(".ga-bottom-bar .lbar").css("backgroundColor","#252625");
			$(".ga-bottom-bar .rbar .arrow").css("borderLeftColor","#252625");
			$(".ga-bottom-rate").css("display",lotteryObj.betItems.length>0?"block":"none");
			$(".ga-bottom-bar .list-act").css("display","none");
			$(".ga-bottom-bar .list-bet").css("display","block");
			$(".ga-bottom-bar .rbar").css("backgroundColor",lotteryObj.betStatus?"#DC3B40":"#575858");
		}
		lotteryObj.selector.listNum(true);
	},
	calcBet:function(curPlay,selArr,rate){
		if(!rate) rate = 1;
		if(selArr.c){//zhixuan
			var selArrNew = [];//直选的重新定义数组
			var numStr = "";
			if(selArr.arrW){
				for(var i=0;i<selArr.arrW.length;i++){
					numStr += selArr.arrW[i];
				}
			}
			if(selArr.arrQ){
				if(numStr) {//如果不是开始的话加逗号
					numStr += ","
				}
				for(var i=0;i<selArr.arrQ.length;i++){
					numStr += selArr.arrQ[i];
				}
			}
			if(selArr.arrB){
				if(numStr) {//如果不是开始的话加逗号
					numStr += ","
				}
				for(var i=0;i<selArr.arrB.length;i++){
					numStr += selArr.arrB[i];
				}
			}
			if(selArr.arrS){
				if(numStr) {//如果不是开始的话加逗号
					numStr += ","
				}
				for(var i=0;i<selArr.arrS.length;i++){
					numStr += selArr.arrS[i];
				}
			}
			if(selArr.arrG){
				if(numStr) {//如果不是开始的话加逗号
					numStr += ","
				}
				for(var i=0;i<selArr.arrG.length;i++){
					numStr += selArr.arrG[i];
				}
			}
			selArrNew.push(numStr);
			return {num:selArr.c,rate:rate,money:selArr.c*lotteryObj.betPrice*rate,selArr:selArrNew}
		}else{
			var m = selArr.length;//主选号数量
			var n = lotteryObj.playType.getN(curPlay.t);
			var betNum = 0;
			if(curPlay.t==5){//如果是三星组三
				betNum = lotteryObj.getTotalNumZusan(m,2);
			}else{
				betNum = lotteryObj.getBetNum(m,n);//预览下注数
			}
			var betMoney = betNum*lotteryObj.betPrice*rate;//下注金额
			console.log(curPlay.name+"[m,n]["+m+","+n+"]betNum="+betNum+",rate="+rate+",betMoney="+betMoney+",selAll="+JSON.stringify(selArr));
			return {num:betNum,rate:rate,money:betMoney,selArr:selArr}
		}
	},
	bet:function(){
		if(lotteryObj.betItems.length==0) return;
		//检查是否是有效投注期
		if(!lotteryObj.betStatus){
			mui.alert("封盘中...请稍后");
			return;
		}

		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/game/login.html?redirect='+directUrl)
			return;
		}

		var flag=0;// 防止重复提交
		mui.confirm("确认投注？","",function(obj){
			if(!obj || !obj.index) return;
			if(flag!=0) return;
			lotteryObj.selector.betSubmit();
			flag=++flag;
		});
	},
	betSubmit:function(){
		var betBallArray = [];
		for(var i=0;i<lotteryObj.betItems.length;i++){
			var obj = lotteryObj.betItems[i];
			var betObj = lotteryObj.selector.calcBet(obj.curPlay,obj.selArr,obj.rate);
			if(obj.curPlay.t==1){//如果是一星,选号之间不加逗号
				betBallArray.push({b:move.widget.arrToString(betObj.selArr),t:obj.curPlay.t});
			}else{
				betBallArray.push({b:move.widget.arr2String(betObj.selArr),t:obj.curPlay.t});
			}
		}
		var sessionArray = [{s:lotteryObj.curSession.sessionNo,m:lotteryObj.selector.getRateVal()}];
		//构造参数
		var map = {};
		map["u"] = move.user.u;
		map["betBallArray"] = JSON.stringify(betBallArray);
		map["sessionArray"] = JSON.stringify(sessionArray);
		map["betType"] = "0";//代购
		map["isAddNo"] = "0";
		map["isWinStop"] = "";
		map["num"] = "";
		map["buyNum"] = "";
		map["isGuaranteed"] = "";
		map["isPrivacy"] = "";
		map["guaranteedNum"] = "";
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.bjSsc_bet,
			dataType:"json",
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var data=ret.data;
				var code=ret.code;
				var msg=ret.msg;
				console.log("bet res::"+JSON.stringify(ret));
				if(code=="200"){
					lotteryObj.betItems =[];//清空
					lotteryObj.selector.initRate();lotteryObj.panel.toggle();
					lotteryObj.selector.listAction();//刷新
				}else{
					
				}
				mui.alert(msg,document.title, function() {});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	select:function(){//选号
		if(lotteryObj.panel.main){//当投注面板时有效
			var curPlay = lotteryObj.getCurrentPlay();
			var selArr = lotteryObj.selector.check(curPlay);//选号
			var rate = lotteryObj.selector.getRate(curPlay);
			if(!selArr) return;
			lotteryObj.betItems.push({curPlay:curPlay,rate:rate,selArr:selArr});//放入
			lotteryObj.selector.listNum();
			lotteryObj.selector.clear(curPlay);
		}
	},
	clear:function(curPlay){//放入清单清除当前的选号
		$("#playItem"+curPlay.t).find(".ga-ball li>a.active").removeClass("active");
		lotteryObj.selector.preview();
	},
	listNum:function(isClear){//更新清单数量提示
		$(".listnum span i").css("display",(lotteryObj.betItems.length>0?"inline-block":"none"));
		$(".listnum span i").html(lotteryObj.betItems.length);
		if(!isClear) lotteryObj.selector.listFly();
	},
	listFly:function(){
		if($("#animateFlyPoint").length==0)
			$(document.body).append("<div id=\"animateFlyPoint\">1</div>");
		$("#animateFlyPoint").animate(
			{left:$(".listnum span i").offset().left,bottom:10},
			"fast",
			function(){
				$("#animateFlyPoint").remove();
			});
	},
	listAction:function(){
		lotteryObj.selector.list();
		lotteryObj.selector.listPreview();
	},
	list:function(){
		var curPlay = lotteryObj.getCurrentPlay();
		lotteryObj.shtml = "<ul>";
		for(var i=0;i<lotteryObj.betItems.length;i++){
			var obj = lotteryObj.betItems[i];
			var betObj = lotteryObj.selector.calcBet(obj.curPlay,obj.selArr,obj.rate);
			lotteryObj.shtml += "<li>";
			lotteryObj.shtml += "<span class=\"num\">"+move.widget.arr2String(betObj.selArr)+"</span>";
			//lotteryObj.shtml += "<span class=\"info\">"+obj.curPlay.name+"复式 "+betObj.num+"注x1元x"+betObj.rate+"倍="+betObj.money+"元"+"</span>";
			lotteryObj.shtml += "<span class=\"info\">"+obj.curPlay.name+"复式 "+betObj.num+"注x1元 = "+betObj.money+"元"+"</span>";
			lotteryObj.shtml += "<span class=\"del\" data=\""+i+"\"><i></i></span>";
			lotteryObj.shtml += "</li>";
		}
		lotteryObj.shtml += "</ul>";
		if(lotteryObj.betItems.length>0) lotteryObj.shtml += "<div class=\"bottombar\"><button onclick=\"lotteryObj.selector.listRemove()\">清空</button></div>";
		$(".ga-list .mcontent").html(lotteryObj.shtml);
		$(".ga-list-box span.del").bind("tap",function(){
			lotteryObj.selector.listRemove($(this).attr("data"));
		});
	},
	listPreview:function(){
		var totalNum = 0,totalMoney = 0;
		for(var i=0;i<lotteryObj.betItems.length;i++){
			var obj = lotteryObj.betItems[i];
			var betObj = lotteryObj.selector.calcBet(obj.curPlay,obj.selArr,obj.rate);
			totalNum += betObj.num;
			totalMoney += betObj.money;
		}
		var mainRate = lotteryObj.selector.getRateVal();
		lotteryObj.selector.showInfo(false,false,{num:totalNum,money:totalMoney*mainRate,rate:mainRate});
	},
	listRemove:function(index){
		if(index){
			move.widget.arrRemove(lotteryObj.betItems,index);//更新数组
			lotteryObj.selector.listAction();
		}else{
			lotteryObj.betItems = [];
			lotteryObj.selector.listAction();
			//lotteryObj.selector.listNum(true);
		}
	},
	initRate:function(){
		$("#betRate").val(1);
	},
	setRate:function(curPlay){
		//curPlay = curPlay?curPlay:lotteryObj.getCurrentPlay();
		//lotteryObj.selector.rateData["rate"+curPlay.t] = lotteryObj.selector.getRateVal();
	},
	setRateVal:function(curPlay){
		curPlay = curPlay?curPlay:lotteryObj.getCurrentPlay();
		var rate = lotteryObj.selector.rateData["rate"+curPlay.t];
		$("#betRate").val(rate?rate:1)
	},
	getRate:function(curPlay){//存储的值--当前为指定的值
		// curPlay = curPlay?curPlay:lotteryObj.getCurrentPlay();
		// var rate = lotteryObj.selector.rateData["rate"+curPlay.t];
		// return rate?rate:1;
		return 1;
	},
	getRateVal:function(){//dom元素值
		var rate = parseInt($("#betRate").val());
		if(isNaN(rate)){
			$("#betRate").val(1);
			return 1;
		}
		return rate;
	},
	
}
/**
 *面板
 */
lotteryObj.panel = {
	main:true,
	init:function(){
		$(".ga-link-back").bind("tap",function(){//初始化返回按钮
			if(lotteryObj.panel.main){
				location = "/game";//gf
			}else{
				lotteryObj.panel.toggle();
			}
		});
		$(".ga-bottom-bar .listnum").bind("tap",function(){//初始化清单按钮
			if(lotteryObj.panel.main){
				lotteryObj.panel.toggle();
			}else{
				lotteryObj.selector.bet();//下注
			}
		});
		$(".ga-bottom-bar .lbar").bind("tap",function(){//初始化添加选号
			lotteryObj.selector.select();
		});
		$(".backmainbtn").bind("tap",function(){
			lotteryObj.panel.toggle();
		});
		$(".randbtn").bind("tap", function(){// han随机选号
			var num=$(this).attr('data');
			lotteryObj.panel.randomPlay(num);
		});
		lotteryObj.panel.resize();
	},
	toggle:function(){//控制主界面和清单界面
		$(".ga-list").toggle();
		$(".ga-main").toggle();
		if(!lotteryObj.panel.main){
			lotteryObj.panel.resize();
		}
		$(".ga-item-menu").toggle();
		lotteryObj.panel.main=lotteryObj.panel.main?false:true;

		if(!lotteryObj.panel.main){
			lotteryObj.selector.listAction();
		}else{
			lotteryObj.selector.preview();
		}
	},
	resize:function(){
		$(".ga-list-box .mcontent").css("max-height",$(window).height()-136-5);
		$(".ga-bottom-bar .lbar .txt").css("max-width",$(window).width()-$(".ga-bottom-bar .rbar").width()-$(".ga-bottom-bar .lbar .add").width());
	},
	close:function(){//封盘
		//投注选项变为封盘两个字
		$(".ga-ball li").append('<a class="close_bet">封盘</a>');lotteryObj.unBindNumbers();
		//不能投注
		lotteryObj.betStatus = false;
		if($(".ga-bottom-bar .rbar .list-bet").css("display")=="block") $(".ga-bottom-bar .rbar").css("backgroundColor","#575858");
	},
	open:function(){//开盘
		//把封盘两删除
		$(".ga-ball .close_bet").remove();lotteryObj.bindNumbers();
		//可以投注
		lotteryObj.betStatus = true;
		if($(".ga-bottom-bar .rbar .list-bet").css("display")=="block") $(".ga-bottom-bar .rbar").css("backgroundColor","#DC3B40");
	},
	randomPlay:function(count){// han随机选号
		var curPlay = lotteryObj.getCurrentPlay();//当前玩法
		var balls = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
		var text = ['大', '小', '单', '双'];
		for(var c = 0; c < count; ++c) {
			var selArr = [];
			if(curPlay.t == 8) {
				selArr=[balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)]]
			}else if(curPlay.t == 7) {
				selArr=[balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)]]
			}else if(curPlay.t == 6) {
				selArr=[balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)]]
			}else if(curPlay.t == 5) {
				alert('对不起该玩法不支持机选！');
				return;
			}else if(curPlay.t == 4) {
				var selected = {};
				var len = 0;
				while(len < 3) {
					var ball = balls[Math.floor(Math.random() * balls.length)];
					if(selected[ball] === undefined) {
						selected[ball] = 1;
						++len;
						selArr.push(ball);
					}
				}
				selArr=[Object.keys(selected).join(',')]
			}else if(curPlay.t == 3) {
				selArr=[balls[Math.floor(Math.random() * balls.length)],
						balls[Math.floor(Math.random() * balls.length)]]
			}else if(curPlay.t == 2) {
				var selected = {};
				var len = 0;
				while(len < 2) {
					var ball = balls[Math.floor(Math.random() * balls.length)];
					if(selected[ball] === undefined) {
						selected[ball] = 1;
						++len;
						selArr.push(ball);
					}
				}
				selArr=[Object.keys(selected).join(',')]
			}else if(curPlay.t == 1) {
				selArr=[balls[Math.floor(Math.random() * balls.length)]]
			}else if(curPlay.t == 0) {
				selArr=[text[Math.floor(Math.random() * text.length)],
						text[Math.floor(Math.random() * text.length)]]
			} else {
				return;
			}
			var rate = lotteryObj.selector.getRate(curPlay);
			if(!selArr) return;
			lotteryObj.betItems.push({curPlay:curPlay,rate:rate,selArr:selArr});//放入
			lotteryObj.panel.resize();
			lotteryObj.selector.listAction();
		}
	},
	randomBall: function(n, sp) {
		var balls = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
			}
		}
		return Object.keys(p).sort().join(sp || ',');
	},
}

/**
 * 号码绑定初始化
 */
lotteryObj.bindNumbers = function(){//绑定号码点击事件
	if(!lotteryObj.bindNumbersHandler){
		mui(".ga-ball").on("tap","li>a:nth-child(1)",function(){//全部玩法号码点击事件
			$(this).toggleClass("active");//全部玩法号码
			var curPlay = lotteryObj.getCurrentPlay();//当前玩法
			var text=$(this).text();
			lotteryObj.selector.preview();
		});
		mui(".ga-short").on("tap","a",function(){//全部快捷按钮 [全/大/小/单/双/清]
			var thisShort = $(this);
			var gaBallObj = thisShort.parent().next().children();//号码父元素
			if(!gaBallObj) return;
			gaBallObj.find("li>a").each(function(){//处理号码选择
				$(this).attr("class",move.widget.arrHas(lotteryObj.getShortArr(thisShort.text()),$(this).text())?"active":"");
			});
			lotteryObj.selector.preview();
		});
		// mui(".ga-plays").on("tap",".ga-play-item",function(){
		// 	window.setTimeout(function(){
		// 		lotteryObj.selector.preview();
		// 	},300);
		// });
		$(".ga-plays").on("scroll",function(){
			lotteryObj.selector.preview();
		});
		mui(".ga-bottom-bar").on("tap",".ga-btn-clear",function(){//清空当前玩法选号
			var curPlay = lotteryObj.getCurrentPlay();
			$("#playItem"+curPlay.t+" .ga-ball").find("a").each(function(){
				$(this).attr("class","");
			});
			lotteryObj.selector.preview();
		});
		mui(".ga-bottom-rate").on("tap",".numbox span",function(){//倍数事件
			var obj = $(this);
			var num = parseInt($("#betRate").val());
			if(isNaN(num)){//不是数字默认为1
				num = 1;
			}
			if(obj.attr("class")=="plus"){
				$("#betRate").val(num+1);
			}else{
				$("#betRate").val(num-1<1?1:num-1);
			}
			lotteryObj.selector.setRate();
			//lotteryObj.selector.preview();
			lotteryObj.selector.listPreview();
		});
		$("#betRate").on("change",function(){
			var num = parseInt($("#betRate").val());
			if(isNaN(num) || num<1){//不是数字默认为1
				$("#betRate").val(1);
			}
	
			lotteryObj.selector.setRate();
			//lotteryObj.selector.preview();
			lotteryObj.selector.listPreview();
		});
		lotteryObj.bindNumbersHandler = true;
	}
}
/**
 * 取消号码绑定初始化
 */
lotteryObj.unBindNumbers = function(){//绑定号码点击事件
	mui(".ga-ball").off("tap");
	mui(".ga-short").off("tap");
	mui(".ga-bottom-bar").off("tap");
	mui(".ga-bottom-rate").off("tap");
	mui(".#betRate").off("change");
	lotteryObj.bindNumbersHandler = false;
}

/**
 *获取快捷号码数组
 */
lotteryObj.getShortArr = function(short){//根据具体的彩种返回数据
	if(!short) return [];
	if(short==="全") return ["0","1","2","3","4","5","6","7","8","9"];
	else if(short==="大") return ["5","6","7","8","9"];
	else if(short==="小") return ["0","1","2","3","4"];
	else if(short==="单") return ["1","3","5","7","9"];
	else if(short==="双") return ["0","2","4","6","8"];
	return [];
}

/**
 *获取当前玩法类型 返回{t:type,n:name,m:money}
 *在程序过程中随时调用
 *通过实时解析dom元素状态获取
 */
lotteryObj.getCurrentPlay = function(){
	var playType = $(".ga-play-item.mui-active").attr("data");//玩法dom元素根据class.active来确定当前玩法
	if(playType) return {t:playType,name:$(".ga-play-item.mui-active span:first").text(),m:lotteryObj.playType.getMoney(playType)};
	return null;
}
/**
 *获取m个号选n的组合
 */
lotteryObj.getBetNum = function(m,n){//
	var fenzi=1;
	var fenmu=1;
	for(var k=m;k>=(m-n+1);k--){
		fenzi=fenzi*k;
		//console.log("fenzi="+fenzi+","+k);
	}
	for(var k=1;k<=n;k++){
		fenmu=fenmu*k;
		//console.log("fenmu="+fenmu+","+k);
	}
	return fenzi/fenmu;
}

/**
 * 计算注数，M选N个数
 */
lotteryObj.getTotalNumZusan = function(m,n) { 
	var fenzi=1;
	var fenmu=1;
	for(var k=m;k>=(m-n+1);k--){
		fenzi=fenzi*k;
	}
	for(var k=1;k<=n;k++){
		fenmu=fenmu*k;
	}
	return (fenzi/fenmu)*2;
}

/*
 *数据请求计时器
 *ajax获取数据更新显示和触发下注计时
 */
lotteryObj.dataTimer = {//数据请求计时器
	id:null,//引用
	pt:3000,//频率ms
	init:function(){//重置
		lotteryObj.dataTimer.clear();
		lotteryObj.dataTimer.exec();
		lotteryObj.dataTimer.id = window.setInterval(function(){
			lotteryObj.dataTimer.exec();
		},lotteryObj.dataTimer.pt);
		console.log("dataTimer.init::"+lotteryObj.dataTimer.id);
	},
	exec:function(){//更新
		lotteryObj.reqSession();
		//console.log("dataTimer.exec.reqSession()::"+lotteryObj.dataTimer.id);
	},
	clear:function(){//清除
		window.clearInterval(lotteryObj.dataTimer.id);
		console.log("dataTimer.clear::"+lotteryObj.dataTimer.id);
	}
}

/*
 *下注计时器
 *依靠数据请求计时器触发
 */
lotteryObj.betTimer = {
	id:null,//引用
	pt:1000,//频率ms
	ing:0,//计时量s
	inited:false,//是否初始化
	reset:function(time){//重置
		if(lotteryObj.betTimer.inited){//已初始化则直接更新ing计时量即可
			lotteryObj.betTimer.ing = time;//重置计时量
			console.log("betTimer.rest inited::"+lotteryObj.betTimer.id);
			return;
		}
		lotteryObj.betTimer.clear();
		lotteryObj.betTimer.ing = time;//重置计时量
		lotteryObj.betTimer.exec(lotteryObj.betTimer.ing);
		lotteryObj.betTimer.id = window.setInterval(function(){
			lotteryObj.betTimer.exec();
		},lotteryObj.betTimer.pt);
		lotteryObj.betTimer.inited = true;
		console.log("betTimer.rest::"+lotteryObj.betTimer.id+","+lotteryObj.betTimer.ing);
	},
	exec:function(){//更新
		lotteryObj.updateBetTiming(lotteryObj.betTimer.ing);
		if(lotteryObj.betTimer.ing<=0){
			lotteryObj.betTimer.inited = false;
			lotteryObj.betTimer.clear();
			lotteryObj.panel.close();//封盘
			return;
		}
		lotteryObj.betTimer.ing--;
		//console.log("betTimer.exec::"+lotteryObj.betTimer.ing);
	},
	clear:function(){//清除
		window.clearInterval(lotteryObj.betTimer.id);
		console.log("betTimer.clear::"+lotteryObj.betTimer.id);
	}
}
/*
 *开奖计时器
 *依靠数据请求计时器触发
 */
lotteryObj.openTimer = {
	id:null,//引用
	pt:1000,//频率ms
	ing:0,//计时量s
	inited:false,//是否初始化
	reset:function(time){//重置
		if(lotteryObj.openTimer.inited){//已初始化则直接更新ing计时量即可
			lotteryObj.openTimer.ing = time;//重置计时量
			console.log("openTimer.rest inited::"+lotteryObj.openTimer.id);
			return;
		}
		lotteryObj.openTimer.clear();
		lotteryObj.openTimer.ing = time;//重置计时量
		lotteryObj.openTimer.exec(lotteryObj.openTimer.ing);
		lotteryObj.openTimer.id = window.setInterval(function(){
			lotteryObj.openTimer.exec();
		},lotteryObj.openTimer.pt);
		lotteryObj.openTimer.inited = true;
		console.log("openTimer.rest::"+lotteryObj.openTimer.id+","+lotteryObj.openTimer.ing);
	},
	exec:function(){//更新
		if(lotteryObj.openTimer.ing<=0){
			lotteryObj.openTimer.inited = false;
			lotteryObj.openTimer.clear();
			//封盘//重置请求服务器
			lotteryObj.panel.open();
			lotteryObj.dataTimer.init();
			return;
		}
		lotteryObj.openTimer.ing--;
		//console.log("openTimer.exec::"+lotteryObj.openTimer.ing);
	},
	clear:function(){//清除
		window.clearInterval(lotteryObj.openTimer.id);
		console.log("openTimer.clear::"+lotteryObj.openTimer.id);
	}
}

move.bjSsc_currentTime=move.server+'/api/gfBjSsc_sessionInfo';
move.bjSsc_betPanel=move.server+'/api/gfBjSsc_betPanel';
move.bjSsc_bet=move.server+'/api/gfBjSsc_bet';
move.bjSsc_trend=move.server+"/api/gfBjSsc_trend";
//move.bjSsc_winRanking=move.server+'/api/bjSsc_winRanking';
move.bjSsc_openList=move.server+'/api/gfBjSsc_openList';
move.bjSsc_gamePlayType=move.server+'/api/gfBjSsc_gamePlayType';
move.bjSsc_latestLottery=move.server+'/api/gfBjSsc_latestLottery';
move.bjSsc_trendWeb=move.server+'/api/gfBjSsc_trendWeb';
