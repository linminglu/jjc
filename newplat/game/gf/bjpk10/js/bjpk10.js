var lotteryObj={
	optionArray:null,
	curSession:null,//当前session数据，接口获取更新
	curPlay:null,//当前玩法
	betPrice:1,//每注单价
	gameType:101,//彩种类型
	betItems:[],//下注清单
	betStatus:true,//下注状态
	playType:{//玩法对象   t:玩法类型  m:赔率   n:最少选几个号(没有则填0)
		qy:{t:10,m:9.800,n:1},// 前一
		qe:{t:9,m:88.200,n:2},// 前二
		qs:{t:8,m:705.600,n:3},// 前三
		dwd1_5:{t:7,m:9.800,n:1},// 定位胆(1-5名)
		dwd6_10:{t:6,m:9.800,n:1},// 定位胆(6-10名)
		dxg:{t:5,m:1.960,n:1},// 大小(冠军)
		dxy:{t:4,m:1.960,n:1},// 大小(亚军)
		dxj:{t:3,m:1.960,n:1},// 大小(季军)
		dsg:{t:2,m:1.960,n:1},// 单双(冠军)
		dsy:{t:1,m:1.960,n:1},// 单双(亚军)
		dsj:{t:0,m:1.960,n:1},// 单双(季军)
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
			url:move.bjpk10_currentTime,
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
		if(obj.resultItems && obj.resultItems.length==10){//上期开奖号
			$(".open-pn").html("<span class=\"open-num\">"+move.widget.fmtBjPk10OpenNo(obj.resultItems)+"</span>");
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
			url:move.bjpk10_openList,
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
							html=html+'<li class="mui-table-view-cell"><div class="title-box"><div class="title">北京PK10</div>';
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
		$("#playItem"+curPlay.t).find(".ga-ball li>a.active").each(function(){
			selArr.push($(this).text())
		});
		if(selArr.length==0){
			if(isTips) mui.alert("至少选择1注号码才能投注！");return false;
		}
		switch(parseInt(curPlay.t)){
			case lotteryObj.playType.qy.t:// 前一
				if(selArr.length<lotteryObj.playType.qy.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.qy.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.qe.t:// 前二
				var diyiArr=[],dierArr=[];// 冠军，亚军
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){
					diyiArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){
					dierArr.push($(this).text())
				});
				var count = 0;
				for(var i=0;i<diyiArr.length;i++){//冠军，亚军必须能组成至少组成一组有效投注号(两个不同的号码)
					var diyi=diyiArr[i];
					for(var j=0;j<dierArr.length;j++){
						var dier=dierArr[j];
						if(diyi!=dier){
							count ++;
						}
					}
				}
				if(count==0){
					if(isTips) mui.alert("冠军和亚军至少选择1个号码！");
					return false;
				}
				selArr = {c:count,diyiArr:diyiArr,dierArr:dierArr};
				break;
			case lotteryObj.playType.qs.t:// 前三
				var diyiArr=[],dierArr=[],disanArr=[];// 冠军，亚军，季军
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){
					diyiArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){
					dierArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){
					disanArr.push($(this).text())
				});
				var count = 0;
				for(var i=0;i<diyiArr.length;i++){//冠军，亚军，季军必须能组成至少组成一组有效投注号(三个不同的号码)
					var diyi=diyiArr[i];
					for(var j=0;j<dierArr.length;j++){
						var dier=dierArr[j];
						if(diyi!=dier){
							for(var k=0;k<disanArr.length;k++){
								var disan=disanArr[k];
								if(diyi!=disan&&dier!=disan){
									count += 1;
								}
							}
						}
					}
				}
				if(count==0){
					if(isTips) mui.alert("冠军，亚军，季军至少选择1个号码！");
					return false;
				}
				selArr = {c:count,diyiArr:diyiArr,dierArr:dierArr,disanArr:disanArr};
				break;
			case lotteryObj.playType.dwd1_5.t:// 定位胆(1-5名)
				var diyiArr=[],dierArr=[],disanArr=[],disiArr=[],diwuArr=[];// 冠军，亚军，季军，第四名，第五名
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){
					diyiArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){
					dierArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){
					disanArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[3]).find(".ga-ball li>a.active").each(function(){
					disiArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[4]).find(".ga-ball li>a.active").each(function(){
					diwuArr.push($(this).text())
				});
				if(selArr.length<lotteryObj.playType.dwd1_5.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dwd1_5.n+"个号码！");
					return false;
				}
				selArr = {diyiArr:diyiArr,dierArr:dierArr,disanArr:disanArr,disiArr:disiArr,diwuArr:diwuArr};
				break;
			case lotteryObj.playType.dwd6_10.t:// 定位胆(6-10名)
				var diliuArr=[],diqiArr=[],dibaArr=[],dijiuArr=[],dishiArr=[];// 第六名，第七名，第八名，第九名，第十名
				$($("#playItem"+curPlay.t+" .ga-play-box")[0]).find(".ga-ball li>a.active").each(function(){
					diliuArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[1]).find(".ga-ball li>a.active").each(function(){
					diqiArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[2]).find(".ga-ball li>a.active").each(function(){
					dibaArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[3]).find(".ga-ball li>a.active").each(function(){
					dijiuArr.push($(this).text())
				});
				$($("#playItem"+curPlay.t+" .ga-play-box")[4]).find(".ga-ball li>a.active").each(function(){
					dishiArr.push($(this).text())
				});
				if(selArr.length<lotteryObj.playType.dwd6_10.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dwd6_10.n+"个号码！");
					return false;
				}
				selArr = {diliuArr:diliuArr,diqiArr:diqiArr,dibaArr:dibaArr,dijiuArr:dijiuArr,dishiArr:dishiArr};
				break;
			case lotteryObj.playType.dxg.t:// 大小(冠军)
				if(selArr.length<lotteryObj.playType.dxg.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dxg.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dxy.t:// 大小(亚军)
				if(selArr.length<lotteryObj.playType.dxy.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dxy.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dxj.t:// 大小(亚军)
				if(selArr.length<lotteryObj.playType.dxj.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dxj.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dsg.t:// 单双(冠军)
				if(selArr.length<lotteryObj.playType.dsg.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dsg.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dsy.t:// 单双(亚军)
				if(selArr.length<lotteryObj.playType.dsy.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dsy.n+"个号码！");
					return false;
				}
				break;
			case lotteryObj.playType.dsj.t:// 单双(季军)
				if(selArr.length<lotteryObj.playType.dsj.n){
					if(isTips) mui.alert("至少选择"+lotteryObj.playType.dsj.n+"个号码！");
					return false;
				}
				break;
		}
		console.log("bet stat["+typeof selArr+"]:"+JSON.stringify(selArr));

		//仅验证提示则直接返回
		if(isTips) return true;
		return selArr;
	},
	preview:function(){//更新投注预计
		var curPlay = lotteryObj.getCurrentPlay();
		var selArr = lotteryObj.selector.check(curPlay);//选号(所有选中的号)
		var rate = lotteryObj.selector.getRate(curPlay);//默认为1整体设置倍数
		lotteryObj.selector.setRateVal(curPlay);
		if(selArr){
			var betObj = lotteryObj.selector.calcBet(curPlay,selArr,rate);//计算投注
			lotteryObj.selector.showInfo(true,false,betObj);
		}else{
			lotteryObj.selector.showInfo(true,true,betObj);
		}
	},
	showInfo:function(add,reset,obj){//是否加号,是否重置,参数对象
		if(add){
			var curPlay = lotteryObj.getCurrentPlay();
			$(".ga-bottom-bar .lbar .totalnum").html(reset?"共0注，0元":"共"+obj.num+"注，"+obj.money+"元<span class=\"confirm-btn\">确定</span>");
			$(".ga-bottom-bar .lbar .betnum").html(reset?"":move.widget.arr2StrSpace(obj.selArr));
			$(".ga-bottom-bar .lbar .add").css("display",reset?"none":"block");
			$(".ga-bottom-bar .lbar").css("backgroundColor",reset?"#252625":"#DC3B40");
			$(".ga-bottom-bar .rbar .arrow").css("borderLeftColor",reset?"#252625":"#DC3B40");
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
	calcBet:function(curPlay,selArr,rate){//玩法，选中的号，倍率
		if(!rate) rate = 1;
		if(curPlay.t==9){// 前二
			var selArrNew = [];// 重新定义数组
			var numStr = "";
			if(selArr.diyiArr) numStr += move.widget.arr2String(selArr.diyiArr);
			if(selArr.dierArr) numStr += "|"+move.widget.arr2String(selArr.dierArr);
			selArrNew.push(numStr);
			return {num:selArr.c,rate:rate,money:selArr.c*lotteryObj.betPrice*rate,selArr:selArrNew}
		}else if(curPlay.t==8){// 前三
			var selArrNew = [];// 重新定义数组
			var numStr = "";
			if(selArr.diyiArr) numStr += move.widget.arr2String(selArr.diyiArr);
			if(selArr.dierArr) numStr += "|"+move.widget.arr2String(selArr.dierArr);
			if(selArr.disanArr) numStr += "|"+move.widget.arr2String(selArr.disanArr);
			selArrNew.push(numStr);
			return {num:selArr.c,rate:rate,money:selArr.c*lotteryObj.betPrice*rate,selArr:selArrNew}
		}else if(curPlay.t==7){// 定位胆1-5名
			var selArrNew = [];// 重新定义数组
			var numStr = "";
			var betNum=0;// 下注数
			if(selArr.diyiArr){
				numStr += move.widget.arr2String(selArr.diyiArr);
				betNum=betNum+selArr.diyiArr.length;
			}
			if(selArr.dierArr){
				numStr += "|"+move.widget.arr2String(selArr.dierArr);
				betNum=betNum+selArr.dierArr.length;
			}
			if(selArr.disanArr){
				numStr += "|"+move.widget.arr2String(selArr.disanArr);
				betNum=betNum+selArr.disanArr.length;
			}
			if(selArr.disiArr){
				numStr += "|"+move.widget.arr2String(selArr.disiArr);
				betNum=betNum+selArr.disiArr.length;
			}
			if(selArr.diwuArr){
				numStr += "|"+move.widget.arr2String(selArr.diwuArr);
				betNum=betNum+selArr.diwuArr.length;
			}
			selArrNew.push(numStr);
			return {num:betNum,rate:rate,money:betNum*lotteryObj.betPrice*rate,selArr:selArrNew}
		}else if(curPlay.t==6){// 定位胆6-10名
			var selArrNew = [];// 重新定义数组
			var numStr = "";
			var betNum=0;// 下注数
			if(selArr.diliuArr){
				numStr += move.widget.arr2String(selArr.diliuArr);
				betNum=betNum+selArr.diliuArr.length;
			}
			if(selArr.diqiArr){
				numStr += "|"+move.widget.arr2String(selArr.diqiArr);
				betNum=betNum+selArr.diqiArr.length;
			}
			if(selArr.dibaArr){
				numStr += "|"+move.widget.arr2String(selArr.dibaArr);
				betNum=betNum+selArr.dibaArr.length;
			}
			if(selArr.dijiuArr){
				numStr += "|"+move.widget.arr2String(selArr.dijiuArr);
				betNum=betNum+selArr.dijiuArr.length;
			}
			if(selArr.dishiArr){
				numStr += "|"+move.widget.arr2String(selArr.dishiArr);
				betNum=betNum+selArr.dishiArr.length;
			}
			selArrNew.push(numStr);
			return {num:betNum,rate:rate,money:betNum*lotteryObj.betPrice*rate,selArr:selArrNew}
		}else{
			var m = selArr.length;//主选号数量
			var n = lotteryObj.playType.getN(curPlay.t);// 至少选几个号
			/*han*/
			var betNum = lotteryObj.getBetNum(m,n);//预览下注数
			/*han*/
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
//			if(obj.curPlay.t==5){
//				betBallArray.push({b:betObj.selArr,t:obj.curPlay.t});
//			}else{
//				betBallArray.push({b:move.widget.arr2String(betObj.selArr),t:obj.curPlay.t});
//			}
			betBallArray.push({b:move.widget.arr2String(betObj.selArr),t:obj.curPlay.t});
		}
		var sessionArray = [{s:lotteryObj.curSession.sessionNo,m:lotteryObj.selector.getRateVal()}];
		//构造参数
		var map = {};
		map["u"] = move.user.u;
		map["betBallArray"] = JSON.stringify(betBallArray);
		map["sessionArray"] = JSON.stringify(sessionArray);
		map["betType"] = "0";//代购
		map["isAddNo"] = "";
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
			url:move.bjpk10_bet,
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
		$(".randbtn").bind("tap", function(){// 随机选号
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
	randomPlay:function(count){// 随机选号
		var curPlay = lotteryObj.getCurrentPlay();// 当前玩法
		for(var c = 0; c < count; ++c) {
			var selArr = null;
			if(curPlay.t==10){
				selArr=lotteryObj.panel.randomBall(1);
			} else if(curPlay.t==9){
				selArr=lotteryObj.panel.randomBallQianEr();
			}else if(curPlay.t==8){
				selArr=lotteryObj.panel.randomBallQianEr();
			}else if(curPlay.t==7){
				selArr=lotteryObj.panel.randomBallDingWeiDan15();
			}else if(curPlay.t==6){
				selArr=lotteryObj.panel.randomBallDingWeiDan610();
			}else if(curPlay.t==5||curPlay.t==4||curPlay.t==3){
				selArr=lotteryObj.panel.randomBallDaXiao();
			}else if(curPlay.t==2||curPlay.t==1||curPlay.t==0){
				selArr=lotteryObj.panel.randomBallDanShuang();
			}
			var rate = lotteryObj.selector.getRate(curPlay);
			if(!selArr) return;
			lotteryObj.betItems.push({curPlay:curPlay,selArr:selArr,rate:rate});//放入
			lotteryObj.panel.resize();
			lotteryObj.selector.listAction();
		}
	},
	randomBall: function(n, sp) {//随机选号
		var selArr = [];
		var balls = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
				selArr.push(ball);
			}
		}
		return selArr;
	},
	randomBallQianEr: function() {// 前二随机
		var selArr=this.randomBall(2);
		var diyiArr=[selArr[0]];
		var dierArr=[selArr[1]];
		selArr = {c:1,diyiArr:diyiArr,dierArr:dierArr};
		return selArr;
	},
	randomBallQianEr: function() {// 前三随机
		var selArr=this.randomBall(3);
		var diyiArr=[selArr[0]];
		var dierArr=[selArr[1]];
		var disanArr=[selArr[2]];
		selArr = {c:1,diyiArr:diyiArr,dierArr:dierArr,disanArr:disanArr};
		return selArr;
	},
	randomBallDingWeiDan15: function() {// 定位胆1-5名
		var selArr=this.randomBall(1);
		var diyiArr=[];
		var dierArr=[];
		var disanArr=[];
		var disiArr=[];
		var diwuArr=[];
		var ran=Math.floor(Math.random() * 5);
		if(ran==0){
			diyiArr=[selArr];
		}else if(ran==1){
			dierArr=[selArr];
		}else if(ran==2){
			disanArr=[selArr];
		}else if(ran==3){
			disiArr=[selArr];
		}else if(ran==4){
			diwuArr=[selArr];
		}
		selArr = {c:1,diyiArr:diyiArr,dierArr:dierArr,disanArr:disanArr,disiArr:disiArr,diwuArr:diwuArr};
		return selArr;
	},
	randomBallDingWeiDan610: function() {// 定位胆6-10名
		var selArr=this.randomBall(1);
		var diliuArr=[];
		var diqiArr=[];
		var dibaArr=[];
		var dijiuArr=[];
		var dishiArr=[];
		var ran=Math.floor(Math.random() * 5);
		if(ran==0){
			diliuArr=[selArr];
		}else if(ran==1){
			diqiArr=[selArr];
		}else if(ran==2){
			dibaArr=[selArr];
		}else if(ran==3){
			dijiuArr=[selArr];
		}else if(ran==4){
			dishiArr=[selArr];
		}
		selArr = {c:1,diliuArr:diliuArr,diqiArr:diqiArr,dibaArr:dibaArr,dijiuArr:dijiuArr,dishiArr:dishiArr};
		return selArr;
	},
	randomBall: function(n, sp) {//随机选号
		var selArr = [];
		var balls = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10'];
		var p = {};
		while(Object.keys(p).length < n) {
			var ball = balls[Math.floor(Math.random() * balls.length)];
			if(!p[ball]) {
				p[ball] = ball;
				selArr.push(ball);
			}
		}
		return selArr;
	},
	randomBallDaXiao(){
		var selArr = [];
		var balls = ['大','小'];
		var p = {};
		var ball = balls[Math.floor(Math.random() * balls.length)];
		if(!p[ball]) {
			p[ball] = ball;
			selArr.push(ball);
		}
		return selArr;
	},
	randomBallDanShuang(){
		var selArr = [];
		var balls = ['单','双'];
		var p = {};
		var ball = balls[Math.floor(Math.random() * balls.length)];
		if(!p[ball]) {
			p[ball] = ball;
			selArr.push(ball);
		}
		return selArr;
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
	if(short==="全") return ["01","02","03","04","05","06","07","08","09","10"];
	else if(short==="大") return ["06","07","08","09","10"];
	else if(short==="小") return ["01","02","03","04","05"];
	else if(short==="单") return ["01","03","05","07","09"];
	else if(short==="双") return ["02","04","06","08","10"];
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
 * 传一个数字，返回所有种类的三不同号
 */
lotteryObj.getSanBuTong = function(number){
	var arr = new Array();
	for (var i=1;i<=number;i++) {
		for (var j=1;j<=6;j++) {
			if (j > i) {
				for (var k=1;k<=6;k++) {
					if (k > j) {
						if (i == number || j == number || k == number) {
							arr.push(i+","+j+","+k);
						}
					}
				}
			}
		}
	}
	return arr;
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
move.bjpk10_currentTime=move.server+"/api/gfBjPk10_sessionInfo";
move.bjpk10_betPanel=move.server+"/api/gfBjPk10_betPanel";
move.bjpk10_bet=move.server+"/api/gfBjPk10_bet";
move.bjpk10_trend=move.server+"/api/gfBjPk10_trend";
move.bjpk10_hotRanking=move.server+"/api/gfBjPk10_hotRanking";
move.bjpk10_openList=move.server+"/api/gfBjPk10_openList";
