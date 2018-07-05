var indexObj={
	init:function(){
		this.advert();
		this.winList();
		this.lotteryNotice();
		this.winRank("allph");
		this.joinHall();
		this.notice('gg');
		this.notice('help');
		this.newsInfo();
		this.showSession();
		this.bindVersion();
		$(".groupbuy .tabs a").bind("click", function(){
			$(".groupbuy .tabs a").removeClass('current');
			$(this).addClass('current');
			var type=$(this).attr('data');
//			if(type){
				indexObj.joinHall(type);
//			}
		});
		$("#checkCode").bind("click", function(){
			var timestamp = Date.parse(new Date());
			$(this).attr('src','/api/baseData_checkCode?t='+timestamp);
		});
		$("#loginbtn").bind("click", function(){// 在move.js里
			move.login();
		});
		$(".float-all-kinds-line .close").bind("click", function(){// 首页两侧关闭的点击事件
			$(".float-all-kinds-line").hide();
		});
	},
	notice:function(idzz){
		var type='0';
		if(idzz=='gg'){
			type='0';
		}else if(idzz=='help'){
			type='1';
		}
		
		var map = {};
		map['type'] ='0';
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.baseData_noticeOrHelp,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var newsId=obj.newsId;
						var title=obj.title;
						html=html+'<li><a href="/news/notice/'+newsId+'" target="_blank">'+title+'</a></li>';
					}
					$('#'+idzz).html(html);
				}else{
//					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	newsInfo:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.baseData_newsInfo,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var ctitle=obj.ctitle;
						var subItems=obj.subItems;
						html=html+'<div class="category"><span class="tag">'+ctitle+'<em>◆</em></span><ul>';
						
						for(var j=0;j<subItems.length;j++) {
							var tempobj=subItems[j];
							var newsId=tempobj.newsId;
							var title=tempobj.title;
							html=html+'<li><a href="/news/notice/'+newsId+'" target="_blank">'+title+'</a></li>';
						}
						html=html+'</ul></div>';
					}
					$('#newsbox').html(html);
				}else{
					//mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	advert:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.advert,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='<ul>';
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						html += "<li><a href=\""+obj.link+"\"><img src=\""+obj.img+"\"></a></li>";
					}
					html += "</ul>";
					$('#slider').html(html);

					var unslider = $('#slider').unslider({
					    dots: true,
					    speed:200,
					    delay:5000
					});
					//unslider.data('unslider');
				}else{
					//mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	winList:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.winRank,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					if(items.length >=5){
						for(var i=0;i<items.length;i++) {
							var obj=items[i];
//							var gameName=obj.gameName;
							var userName=obj.userName;
							var money=obj.winMoney;
							html=html+'<li class="flexbox"><span class="kind">'+userName+'</span><span class="winner">'+userName+'</span>'; 
							html=html+'<span class="award">'+(money?money:0)+'元</span></li>';
						}
					}else{
						for(var i=0;i<items.length;i++) {
							var obj=items[i];
//						var gameName=obj.gameName;
							var userName=obj.userName;
							var money=obj.winMoney;
							html=html+'<li class="flexbox"><span class="kind">'+userName+'</span><span class="winner">'+userName+'</span>'; 
							html=html+'<span class="award">'+(money?money:0)+'元</span></li>';
						}
					}
					$('#newwinbox').html(html);
				}else{
					//mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	lotteryNotice:function(){
		var map = {};
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.lotteryNotice,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var title=obj.gameName;
						var sessionNo=obj.openSessionNo;
						var openResult=obj.openResult;
						var time=obj.openTime;
						var type=obj.gameType;
						var numhtml='';
						var touzhuUrl='';
						var zoushiUrl='';
						if(type=='251'){
							touzhuUrl='/trade/xy/#cqssc';
							zoushiUrl='/zoushitu/cqssc/';
						}else if(type=='151'){
							touzhuUrl='/trade/xy/#bjpk10';
							zoushiUrl='/zoushitu/bjpk10/';
						}
						if(openResult.length==10){
							for ( var j = 0; j < openResult.length; j++) {
								var num=openResult[j];
								numhtml = numhtml+'<span class="num pkn-'+num+'">'+num+'</span>';
							}
						}else{
							for ( var j = 0; j < openResult.length; j++) {
								var num=openResult[j];
								numhtml = numhtml+'<span class="num bg-red">'+num+'</span>';
							}
						}
						html=html+'<li class="kj-cell"><div class="title"><span class="main">'+title+'</span> <span class="subtitle">第'+sessionNo+'期</span></div>';
						html=html+'<div class="open-number">'+numhtml+'</div>';
						html=html+'<div class="open-time"><span class="time">'+time+'</span>';
						html=html+'<a class="wb" href="'+touzhuUrl+'">投注</a>';
						html=html+'<a class="wb" href="'+zoushiUrl+'">走势</a>';
						html=html+'</div></li>';
					}
					$('.caizhongkj').html(html);
				}else{
					mui.alert(msg,document.title, function() {});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	winRank:function(idss){
		var type;
		if(!idss){
			type='2';
		}
		if(idss=='allph'){
			type='2';
		}else if(idss=='zhouph'){
			type='1';
		}else if(idss=='riph'){
			type='0';
		}
		var oldhtml=$.trim($('#'+idss).html());
		if(oldhtml.length==0){
			var map = {};
			map['rankType'] =type;
			var mw=baseObj.mw(map);
			$.ajax({
				type: "POST",
				url:move.winRank,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					var code=ret.code;
					var msg=ret.msg;
					if(code=='200'){
						var data=ret.data;
						var items=data.items;
						var html='<li class="title"><span class="paiming">排名</span><span class="yonghuming">用户名</span><span class="jine">中奖金额</span></li>';
						for(var i=0;i<items.length;i++) {
							var obj=items[i];
							var userName=obj.userName;
							var winMoney=obj.winMoney;
							html=html+'<li class="item"><span class="paiming">'+(i+1)+'</span><span class="yonghuming">'+userName+'</span>';
							html=html+'<span class="jine">'+(winMoney?winMoney:0)+'</span></li>';
						}
						$('#'+idss).html(html);
					}else{
						//mui.alert(msg,document.title, function() {});
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
			$('.paihangbangdata').hide();
			$('#'+idss).show();
		}
	},
	joinHall:function(type){
		if(!type){
			type=''
		}
		var map = {};
		map['gameType'] =type;
		map['pageSize'] =15;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.joinHall,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
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
							statushtml='<span class="status status-2 colour-green">进行中</span>'; 
						}else if(winResult=='2'){
							statushtml='<span class="status status-2">等待开奖</span>'; 
						}else if(winResult=='4'){
							statushtml='<span class="status status-4 colour-red">已中奖</span>'; 
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
				}else{
					html='<tr><td colspan="10" class="notfound">抱歉！没有找到符合条件的结果！</td></tr>';
				}
				$('#hemaidatabox').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	showSession:function(){
		$(".left-box .selebox .kinds-cell").mouseover(function(){
			if ($(this).find('.other-box').length > 1) {
				$(this).find('.other-box').show();
				var len=$(this).find('.other-box').length;
				var width=226.01+len*95;
				$(this).css({"width":""+width+"320px","background-color":"#fff","border":"1px solid #e60000","border-right":"0"});
			}
		});
		$(".right-box .selebox .kinds-cell").mouseover(function(){
			if ($(this).find('.other-box').length > 1) {
				$(this).find('.other-box').show();
				var len=$(this).find('.other-box').length;
				var width=226.01+len*95;
				$(this).css({"width":""+width+"320px","background-color":"#fff","border":"1px solid #e60000","border-right":"0","margin-left":"-191px"});
			}
		});
		$(".left-box .selebox .kinds-cell").mouseout(function(){
			$(this).find('.other-box').hide();
			$(this).css({"width":"226.01px","background-color":"#fff","border":"0px solid #e60000","border-bottom": "1px dashed #eee"});
		});
		$(".right-box .selebox .kinds-cell").mouseout(function(){
			$(this).find('.other-box').hide();
			$(this).css({"width":"226.01px","background-color":"#fff","border":"0px solid #e60000","border-bottom": "1px dashed #eee","margin-left":"0"});
		});
	},
	bindVersion:function(){
		$(".title-version").mouseover(function(){
			$(".title-version").removeClass("mui-active");
			$(this).addClass("mui-active");
			var data=$(this).attr("data");
			if(data==1){
				$("#a-code").show();
				$("#i-code").hide();
			}else{
				$("#i-code").show();
				$("#a-code").hide();
			}
		});
	}
}
$(document).ready(function(e) {
	
	$('.tabname').mousemove(function(e){
		var id=$(this).attr('data');
		$('.tabname').removeClass('active');
		$(this).addClass('active');
		$('.news-help').hide();
		$('#'+id).show();
	});
	$('.tabs .tab').mousemove(function(e){
		var id=$(this).attr('data');
		$(this).parent().children('.tab').removeClass('active');
		$(this).addClass('active');
		$(this).parent().nextAll('.contents').hide();
		$('#'+id).show();
	});
	$('.paihangtab').click(function(e){
		$(".hbox .paihangtab").removeClass("selected");
		$(this).addClass("selected");
		var id=$(this).attr('data');
		$('.paihangbangdata').hide();
		indexObj.winRank(id);
		$('#'+id).show();
		
	});
});