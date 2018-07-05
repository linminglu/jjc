var tradeObj={
	defPageSize:15,
	gameType:'',
	init:function(){
		this.sessionInfoList();
		this.showSession();
	},
	bindPage:function(){
		$(".pager .first").bind("click", function(){
			move.pageIndex=0;
			tradeObj.hemai();
		});
		$(".pager .prev").bind("click", function(){
			if(move.pageIndex>0){
				move.pageIndex=move.pageIndex-1;
				tradeObj.hemai();
			}
		});
		$(".pager .next").bind("click", function(){
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=move.pageIndex+1;
				tradeObj.hemai();
			}
		});
		$(".pager .last").bind("click", function(){
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=(toatlpage-1);
				tradeObj.hemai();
			}
		});
		$(".kinds .switch").bind("click", function(){
			var data=$(this).attr('data');
			tradeObj.gameType=data;
			tradeObj.hemai();
		});
		$(".conditions .date").change(function(){
			tradeObj.hemai();
		});
		$(".conditions .status").change(function(){
			tradeObj.hemai();
		});
		$(".conditions .money").change(function(){
			tradeObj.hemai();
		});
		$(".conditions .progress").change(function(){
			tradeObj.hemai();
		});
		$(".conditions .guarantees").change(function(){
			tradeObj.hemai();
		});
	},
	bindBuy:function(tt){
		$("#databox .button").bind("click", function(){
			var input=$(this).prevAll('input');
			var id=$(input).attr('name');
			var max=$(input).attr('max');
			var num=$(input).val();
			if(parseInt(num)>parseInt(max)){
				mui.alert('超出剩余份数',document.title, function() {});
				return;
			}
			tradeObj.buyHemai(id, num,tt);
		});
	},
	buyHemai:function(id,num,tt){
		if(move.user.u==null){
			var directUrl=location.href;
			directUrl=directUrl.split('#')[0];
			baseObj.openView('/login.html?redirect='+directUrl)
			return;
		}
		var map = {};
		map['u'] =move.user.u;
		map['jointId'] =id;
		map['buyNum'] =num;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.baseData_jointBet,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				mask.close();
				if(code=='200'){
					var money=data.money;
					var restNum=data.restNum;
					var schedule=data.schedule;
					var jindu=data.jindu;
					move.user.money=money;
					$('#schedule'+id+' .schedule').text(schedule);
					$('#schedule'+id+' .progress div.bar').css("width",jindu);
					$('#'+id).text(restNum);
					$("input[name='"+id+"']").attr("max", restNum);
					$('.logined .money').text(money);
					store.set('user', move.user);
					
					if(tt=='view'){
						$('#surNum').text(restNum+"份");
						$('.progress div.bar').css("width",jindu);
						$('.progress div.bar').text(jindu);
					}
					
					if(restNum==0){
						if(tt=='view'){//合买详情里面的购买按钮
							$('#databox .buttons').html('<a class="lottery-button status-3"></a>');
						}
					}
					mui.alert(msg,document.title, function() {});
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	hemai:function(){
		if(this.gameType==''){
			var gameType=move.getParameter('gameType');
			if(gameType){
				this.gameType=gameType;
			}
		}
		var date=$('.conditions .date').val();//时间
		var status=$('.conditions .status').val();//状态
		var money=$('.conditions .money').val();//方案金额
		var progress=$('.conditions .progress').val();//进度
		var guarantees=$('.conditions .guarantees').val();//保底
		var map = {};
		map['betTime'] =date;
		map['winResult'] =status;
		map['money'] =money;
		map['schedule'] =progress;
		map['isGuarantee'] =guarantees;
		map['gameType'] =this.gameType;
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;

		move.createLoading().show();
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.baseData_joinHall,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var html='';
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var restNum=obj.restNum;
						var jindu=obj.jindu;
						var schedule=obj.schedule;
						var gameType=obj.gameType;
						var gameName=obj.gameName;
						var baodi=obj.baodi;
						var jointId=obj.jointId;
						var money=obj.money;
						var userName=obj.userName;
						var winResult=obj.winResult;
						var aggBetMoney=obj.aggBetMoney;
						var money2=obj.money2;
						var jointId=obj.jointId;
						var isGuarantee=obj.isGuarantee;
						var vippath='';
						var viplevel=0;
						var viplevelpath='-';
						
						var qianWan=aggBetMoney/10000000;
						var qianWanYu=aggBetMoney%10000000;
						qianWan=parseInt(qianWan);
						
						var baiWan=qianWanYu/1000000;
						var baiWanYu=qianWanYu%1000000;
						baiWan=parseInt(baiWan);
						
						var shiWan=qianWanYu/1000000;
						var shiWanYu=qianWanYu%1000000;
						shiWan=parseInt(shiWan);
						
						var wan=shiWanYu/1000000;
						var wanYu=shiWanYu%1000000;
						wan=parseInt(wan);
						
						var levelhtml='';
						if(qianWan>0){
							levelhtml=levelhtml+'<span><img src="/images/static/star/crown.gif"> <img src="/images/static/star/'+qianWan+'.png"></span>';
						}
						if(baiWan>0){
							levelhtml=levelhtml+'<span><img src="/images/static/star/gold.gif"> <img src="/images/static/star/'+baiWan+'.png"></span>';
						}
						if(shiWan>0){
							levelhtml=levelhtml+'<span><img src="/images/static/star/diamond.gif"> <img src="/images/static/star/'+shiWan+'.png"></span>';
						}
						if(wan>0){
							levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"> <img src="/images/static/star/'+wan+'.png"></span>';
						}else{
							levelhtml=levelhtml+'<span><img src="/images/static/star/star.gif"></span>';
						}
						
						var baodihtml='';
						if(isGuarantee=='1'){
							baodihtml='<span class="guard">保</span>';
						}
						
						var statushtml='';
						if(winResult=='0'){// 0=未满员 1=无效 2=未开始 3=开奖中  4=中奖 5=不中奖
							statushtml='<span><input type="text" name="'+jointId+'" max="'+restNum+'" value="1" style="color: rgb(153, 153, 153);" onkeyup="if(isNaN(value))execCommand(\'undo\')" onafterpaste="if(isNaN(value))execCommand(\'undo\')"> <a class="button">购买</a></span>';
						}else if(winResult=='2'){
							statushtml='<span class="status status-2">等待开奖</span>'; 
						}else if(winResult=='4'){
							statushtml='<span class="status status-4">已中奖</span>'; 
						}else if(winResult=='5'){
							statushtml='<span class="status status-5">未中奖</span>'; 
						}else if(winResult=='3'){
							statushtml='<span class="status status-3">开奖中</span>'; 
						}
						
						html=html+'<tr><td>'+(i+1)+'</td>';
						html=html+'<td>'+gameName+'</td>';
						html=html+'<td>'+userName+'</td>';
						html=html+'<td>'+levelhtml+'</td>';
						html=html+'<td>'+money+'元</td>';
						html=html+'<td>|</td>';
						html=html+'<td><span id="'+jointId+'">'+restNum+'</span>份</td>';
						html=html+'<td id="schedule'+jointId+'"><span class="schedule">'+schedule+'</span>'+baodihtml+'<div class="progress"><div class="bar" style="width: '+jindu+';"></div></div></td>';
						html=html+'<td>'+statushtml+'</td>';
						html=html+'<td><a href="/trade/hemai/'+jointId+'" class="detail">详情</a></td>';
					}
					var pageIndex=data.pageIndex;
					var pageSize=data.pageSize;
					var total=data.total;
					var totalPage=total/pageSize;
					if(parseInt(total/pageSize)<totalPage){//不整除
						totalPage=totalPage+1;
					}
					totalPage=parseInt(totalPage);
					var nowpage=(pageIndex+1);
					$('.pager').show();
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
					$('.pager').hide();
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#databox').html(html)
				move.closeLoading();
				tradeObj.bindBuy();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	viewBind:function(){
		$(".tabs .tab").bind("click", function(){
			$(".tabs .tab").removeClass('current');
			$(this).addClass('current');
			var idzz=$(this).attr('data');
			$('.contents table').hide();
			$('#'+idzz).show();
		});		
	},
	hemaiView:function(){
		var url=window.location;
		if(url){
			url=url+"";
			var id=url.substring(url.lastIndexOf('/')+1,url.length);
			var mask=move.createLoading();
			mask.show();
			var map = {};
			map['jointId'] =id;
			if(move.user.u){
				map['u'] =move.user.u;
			}
			move.createLoading().show();
			var mw=baseObj.mw(map);
			$.ajax({
				type: "POST",
				url:move.baseData_jointBetDetail,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var data=ret.data;
					var msg=ret.msg;
					var html='';
					if(code=='200'){
						var img=data.img;
						var gameName=data.gameName;
						var betTime=data.betTime;
						var orderNum=data.orderNum;
						var sessionNo=data.sessionNo;
						var sponsor=data.sponsor;
						var restNum=data.restNum;
						var jindu=data.jindu;
						var baodi=data.baodi;
						var totalWinCash=data.totalWinCash;
						var winResult=data.winResult;//方案状态0=未满员 1=无效 2=未开始 3=开奖中  4=中奖 5=不中奖
						var totalPrice=data.money;
						var diPrice=data.guaMoney;
						var totalNum=data.num;
						var price=data.preMoney;
						var betNumIt=data.betNumIt;
						var multiple=data.multiple;
						
						var statushtml='';
						var statustext='';
						if(winResult=='0'){
							statustext='进行中';
							statushtml='<div class="buttons"><input type="text" name="'+id+'" max="'+restNum+'" value="1"> <span>份</span> <a class="lottery-button buy button">立即购买</a></div>';
						}else if(winResult=='1'){
							statustext='已过期';
							statushtml='<a class="lottery-button status-3"></a>';
						}else if(winResult=='2'){
							statustext='等待开奖';
							statushtml='<a class="lottery-button status-3"></a>';
						}else if(winResult=='3'){
							statustext='开奖中';
							statushtml='<a class="lottery-button status-3"></a>';
						}else if(winResult=='4'){
							statustext='已中奖';
							statushtml='<a class="lottery-button status-3"></a>';
						}else if(winResult=='5'){
							statustext='未中奖';
							statushtml='<a class="lottery-button status-3"></a>';
						}
						
						var bethtml='';
						for(var i=0;i<betNumIt.length;i++) {
							var obj=betNumIt[i];
							var lable=obj.title;
							var betBall=obj.betBall;
							if(lable==''){
								bethtml=bethtml+'<div>'+betBall+'</div>';
							}else{
								bethtml=bethtml+'<div>'+lable+': '+betBall+'</div>';
							}
						}
						$('.play .combian').html(bethtml);
						$('.progress-bar .progress').html('<div class="bar" style="width: '+jindu+';">'+jindu+'</div>');
						$('.progress-bar .icon-guard').text('保底'+baodi+'');
						$('.buttons').html(statushtml);
						$('#totalPrice').text(totalPrice+"元");
						$('#diPrice').text(diPrice+"元");
						$('#totalNum').text(totalNum+"份");
						$('#surNum').text(restNum+"份");
						$('#price').text(price+"元");
						if(totalWinCash!=''){
							$('#win').text(totalWinCash);
						}
						$('#statustext').text(statustext);
						$('#username').text(sponsor);
						$('#sessionNo').text(sessionNo);
						$('#faqitime').text(betTime);
						$('#orderNum').text(orderNum);
						
						var gameName=data.gameName;
						$('.project-logo img').attr('src',img);
						$('.project-detail .kind').text(gameName);
						var openTime=data.openTime;
						var openResult=data.openResult;
						
						if(!openTime){
							openTime='';
						}
						if(!openResult){
							openResult='';
						}
						
						$('#infoview .sessionNo').text(sessionNo);
						$('#infoview .totalPrice').text(totalPrice);
						$('#infoview .multiple').text(multiple);
						$('#infoview .numno').text(openResult);
						$('#infoview .date').text(openTime);
						$('#infoview .totalWinCash').text(totalWinCash);
						$('#infoview .status').text(statustext);
						
						var items=data.userIt;
						var html='';
						for(var i=0;i<items.length;i++) {
							var obj=items[i];
							var userName=obj.userName;
							var betMoney=obj.betMoney;
							var winCash=obj.winCash;
							var buyTime=obj.buyTime;
							if(betMoney){
								betMoney='￥'+betMoney;
							}
							if(winCash){
								winCash='￥'+winCash;
							}
							html=html+'<tr><td>'+(i+1)+'</td><td>'+userName+'</td><td>'+betMoney+'</td><td>'+winCash+'</td><td>'+buyTime+'</td></tr>';
						}
						$('#buyinfo tbody').html(html);
						tradeObj.bindBuy('view');
					}else{
						mui.alert(msg,document.title, function() {});
					}
					move.closeLoading();
				},
				error: function (jqXHR, textStatus, errorThrown) {
					mask.close();
				}
			});
		}
	},
	myView:function(){
		var url=window.location;
		if(url){
			url=url+"";
			var id=url.substring(url.lastIndexOf('/')+1,url.length);
			var mask=move.createLoading();
			mask.show();
			var map = {};
			map['jointId'] =id;
			var mw=baseObj.mw(map);
			$.ajax({
				type: "POST",
				url:move.baseData_gaBetDetail,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var data=ret.data;
					var msg=ret.msg;
					var html='';
					if(code=='200'){
						var img=data.img;
						var gameName=data.gameName;
						var betTime=data.buyTime;
						var orderNum=data.orderNum;
						var sessionNo=data.sessionNo;
						var name=data.name;
						var restNum=data.restNum;
						var jindu=data.jindu;
						var baodi=data.baodi;
						var totalWinCash=data.winCash;
						var winResult=data.winResult;//方案状态0=未满员 1=无效 2=未开始 3=开奖中  4=中奖 5=不中奖
						var totalPrice=data.betMoney;
						var betNumIt=data.betNumIt;
						var multiple=data.multiple;
						var gameType=data.gameType;//3=重庆时时彩 9=广东11选5 13=双色球 15=三分彩 14=五分彩
						var statushtml='';
						if(gameType=='3'){
							statushtml='<a class="buy2" href="/trade/cqssc/">继续投注</a>';
						}else if(gameType=='9'){
							statushtml='<a class="buy2" href="/trade/gd115/">继续投注</a>';
						}else if(gameType=='13'){
							statushtml='<a class="buy2" href="/trade/ssq/">继续投注</a>';
						}else if(gameType=='14'){
							statushtml='<a class="buy2" href="/trade/sanfencai/">继续投注</a>';
						}else if(gameType=='15'){
							statushtml='<a class="buy2" href="/trade/wufencai/">继续投注</a>';
						}
						
						var statustext='';
						if(winResult=='0'){
							statustext='进行中';
						}else if(winResult=='1'){
							statustext='已过期';
						}else if(winResult=='2'){
							statustext='等待开奖';
						}else if(winResult=='3'){
							statustext='开奖中';
						}else if(winResult=='4'){
							statustext='已中奖';
						}else if(winResult=='5'){
							statustext='未中奖';
						}
						
						var bethtml='';
						for(var i=0;i<betNumIt.length;i++) {
							var obj=betNumIt[i];
							var lable=obj.title;
							var betBall=obj.betBall;
							if(lable==''){
								bethtml=bethtml+'<div>'+betBall+'</div>';
							}else{
								bethtml=bethtml+'<div>'+lable+': '+betBall+'</div>';
							}
						}
						$('.play .combian').html(bethtml);
						$('.buttons').html(statushtml);
						$('#totalPrice').text(totalPrice+"元");
						$('#multiple').text(multiple+"倍");
						if(totalWinCash!=''){
							$('#win').text(totalWinCash);
						}
						$('#statustext').text(statustext);
						$('#username').text(name);
						$('#sessionNo').text(sessionNo);
						$('#faqitime').text(betTime);
						$('#orderNum').text(orderNum);
						
						var gameName=data.gameName;
						$('.project-logo img').attr('src',img);
						$('.project-detail .kind').text(gameName);
						var openTime=data.openTime;
						var openResult=data.openResult;
						
						if(!openTime){
							openTime='';
						}
						if(!openResult){
							openResult='';
						}
						$('.detail .openResult').text(openResult);
					}else{
						mui.alert(msg,document.title, function() {});
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					mask.close();
				}
			});
		}
	},
	sessionInfoList:function(){
		move.createLoading().show();
		var map = {};
		var mw=baseObj.mw(map);
		move.popNavData = {};//用于popNav避免重复调用接口
		$.ajax({
			type: "POST",
			url:move.lotteryDraw,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;

					move.popNavData = ret.data;
					move.popNavBuild();//用于popNav避免重复调用接口

					var items=result.items;
					var xyItems=result.xyItems;
					var html='';
					var xyHtml='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var explain=obj.explain;
						var openResult=obj.openResult;
						var openSessionNo=obj.openSessionNo;
						var gameName=obj.gameName;
						var type=obj.gameType;
						var openTime=obj.openTime;
						var gameCode=obj.gameCode;
						var touzhuUrl='';
						var kaijiangUrl='';
						var zoushiUrl='';
						if(type=='3'){
							touzhuUrl='/trade/cqssc/';
							zoushiUrl='/zoushitu/cqssc/';
							kaijiangUrl='/kaijiang/cqssc.html';
						}else if(type=='9'){
							touzhuUrl='/trade/gd115/';
							zoushiUrl='/zoushitu/gd115/';
							kaijiangUrl='/kaijiang/gd115.html';
						}else if(type=='13'){
							touzhuUrl='/trade/ssq/';
							zoushiUrl='/zoushitu/ssq/';
							kaijiangUrl='/kaijiang/ssq.html';
						}else if(type=='14'){
							touzhuUrl='/trade/sanfencai/';
							zoushiUrl='/zoushitu/sanfencai/';
							kaijiangUrl='/kaijiang/sanfencai.html';
						}else if(type=='15'){
							touzhuUrl='/trade/wufencai/';
							zoushiUrl='/zoushitu/wufencai/';
							kaijiangUrl='/kaijiang/wufencai.html';
						}
						html=html+'<div class="kinds-cell"><a href="/trade/'+gameCode+'/" class="icon '+gameCode+'"><img src="'+img+'"></a>';
						html=html+'<div class="kind-data-box"><a href="/trade/'+gameCode+'/" class="icon '+gameCode+'">';
//备份						html=html+'<h3><span class="kind">'+gameName+'</span> <span class="period">第'+openSessionNo+'期</span></h3>';
						html=html+'<h3><span class="kind">'+gameName+'</span></h3>';
						html=html+'<p>'+explain+'</p><div class="open-number">';
						var numhtml='';
//						for ( var j = 0; j < openResult.length; j++) {
//							var num=openResult[j];
//							var className='bg-red';
//							if(type=='13'){
//								if(j==6){//蓝球
//									className='bg-blue';
//								}
//							}
//							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
//						}
						html=html+numhtml;
//备份						html=html+'</div></a><div class="buttons"><a href="/kaijiang/'+gameCode+'.html">开奖公告</a>';
//备份						html=html+'<a href="/trade/'+gameCode+'/">我要投注</a></div></div></div>';
						html=html+'</div></a><div class="buttons">';
						html=html+'</div></div></div>';
						
						
//						html=html+'<tr><td class="kinds-logo"><img src="'+img+'"></td> <td><a href="'+kaijiangUrl+'">'+gameName+'</a></td> ';
//						html=html+'<td>'+openSessionNo+'期</td><td>'+openTime+'</td><td class="balls"><div class="open-number"> ';
//						var numhtml='';
//						for ( var j = 0; j < openResult.length; j++) {
//							var num=openResult[j];
//							var className='bg-red';
//							if(type=='13'){
//								if(j==6){//蓝球
//									className='bg-blue';
//								}
//							}
//							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
//						}
//						html=html+numhtml;
//						html=html+'</div></td><td>'+explain+'</td> ';
//						html=html+'<td><a href="'+zoushiUrl+'"><img src="/images/static/graph.gif"></a></td>  ';
//						html=html+'<td><a href="'+touzhuUrl+'">代购</a> <a href="'+touzhuUrl+'">合买</a></td></tr>';
					}
					$('#gf-kinds-box').html(html);
					
					for (var i = 0; i < xyItems.length; i++) {
						var obj=xyItems[i];
						var img=obj.img;
						var explain=obj.explain;
						var openResult=obj.openResult;
						var openSessionNo=obj.openSessionNo;
						var gameName=obj.gameName;
						var type=obj.gameType;
						var openTime=obj.openTime;
						var gameCode=obj.gameCode;
						xyHtml=xyHtml+'<div class="kinds-cell"><a href="/trade/xy/#'+gameCode+'" class="icon '+gameCode+'"><img src="'+img+'"></a>';
						xyHtml=xyHtml+'<div class="kind-data-box"><a href="/trade/xy/#'+gameCode+'" class="icon '+gameCode+'">';
						xyHtml=xyHtml+'<h3><span class="kind">'+gameName+'</span></h3>';
						xyHtml=xyHtml+'<p>'+explain+'</p><div class="open-number">';
						xyHtml=xyHtml+'</div></a><div class="buttons">';
						xyHtml=xyHtml+'</div></div></div>';
					}
					$('#xy-kinds-box').html(xyHtml);
				}else{
				}
				move.closeLoading();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	showSession:function(){
		$(".selebox .kinds-cell").mouseover(function(){
			if ($(this).find('.other-box').length > 1) {
				$(this).find('.other-box').show();
				var len=$(this).find('.other-box').length;
				var width=226.01+len*95;
				$(this).css({"width":""+width+"320px","background-color":"#fff","border":"1px solid #e60000","border-left":"0"});
			}
		});
		$(".selebox .kinds-cell").mouseout(function(){
			$(this).find('.other-box').hide();
			$(this).css({"width":"226.01px","background-color":"#fff","border":"0px solid #e60000","border-bottom": "1px dashed #eee"});
		});
	}
}


$(function(e) {
	$('#kinds').mousemove(function(e){
		$('#kinds').show();
	});
	$('.selebox').mousemove(function(e){
		$('#kinds').show();
	});
	$('#kinds').mouseout(function(e){
		$('#kinds').hide();
	});
	$('.selebox').mouseout(function(e){
		$('#kinds').hide();
	});
});