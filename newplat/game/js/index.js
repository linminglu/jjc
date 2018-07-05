var indexObj={
	init:function(){
		this.slider();
		this.notice();
		this.winList();
		this.gameColumn();
		this.bindGfXy();
		//this.helpList();
		this.money();
		this.success();

		var tipH = move.storage.getS("tipH");
		if(!tipH || tipH==="0"){
			this.announcement();
		}
	},
	success:function(){
		//$(".ga-view-home").fadeIn();
	},
	money:function(){
		if(move.user.u!=null){
			var map = {};
			map['u'] = move.user.u;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			$.ajax({
				type: "POST",
				url:move.money,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					var code=ret.code;
					var msg=ret.msg;
					$("#userMoney").html(ret.data.money?ret.data.money:"0.00");
					if($('input#userMoney')&&$('input#userMoney').length>0){
						$('#userMoney').val('可提款金额：￥'+(ret.data.money?ret.data.money:"0.00"));
					}
					store.set('money',ret.data.money)
					move.user.money=ret.data.money;
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
		}
	},
	slider:function(){
		var map = {};
		map['adType'] =1;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.advert,
			dataType:'json',
			data:{
				mw:mw
			},
			success:function(ret){
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				if(code==200){
					var html='';
					var items=data.items;
					var totalLength=items.length;
					if(totalLength>0){
						var imgbox='<div class="mui-slider-group mui-slider-loop">';
						var roundbox='<div class="mui-slider-indicator">';
						var img=items[0].img;
						var type=items[0].type;
						if(img){
							var url;
							if(type==2){
								url=items[0].link;
							}else{
								url="#";
							}
							//imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate"><a href="'+url+'"> <img src="'+img+'"></a></div>';
							imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate"><img src="'+img+'"></div>';
						}
						var length=totalLength;
						if(totalLength>3){
							length=3;
							var staticadvertBox="";
							for (var i = length; i < totalLength; i++) {
								var obj=items[i];
								img=obj.img;//图片
								type=obj.type;
								if(img){
									var url;
									if(type==2){
										url=obj.link;
									}else{
										url="#";
									}
									var htmlcode='<a href="'+url+'"><img src="'+img+'"></a>';
									staticadvertBox=staticadvertBox+htmlcode;
								}
							}
							$("#static-advert").html(staticadvertBox);
						}
						for (var i = 0; i < length; i++) {
							if(i==0){
								roundbox=roundbox+'<div class="mui-indicator mui-active"></div>';
							}else{
								roundbox=roundbox+'<div class="mui-indicator"></div>';
							}
							var obj=items[i];
							img=obj.img;//图片
							type=obj.type;
							if(img){
								var url;
								if(type==2){
									url=obj.link;
								}else{
									url="#";
								}
								//var htmlcode='<div class="mui-slider-item"><a href="'+url+'"><img src="'+img+'"></a></div>';
								var htmlcode='<div class="mui-slider-item"><img src="'+img+'"></div>';
							}
							imgbox=imgbox+htmlcode;
						}
						img=items[length-1].img;
						type=items[length-1].type;
						if(img){
							var url;
							if(type==2){
								url=items[length-1].link;
							}else{
								url="#";
							}
							//imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate"><a href="'+url+'"><img src="'+img+'"></a></div>';
							imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate"><img src="'+img+'"></div>';
						}
						imgbox=imgbox+'</div>';
						roundbox=roundbox+'</div>';
						html=imgbox+roundbox;
					}
					jQuery("#slider").html(html);
					var slider = mui("#slider");
					slider.slider({
						interval: 2000
					});
				}else{
					var html="<div class=''><img src='img/def.jpg'/></div>";
					jQuery("#slider").html(html);
				}
			},
			error:function(xhr,type,errorThrown){
				//异常处理
				console.log(type);
			}
		});
	},
	notice:function(){
		$.ajax({
			type: "POST",
			url:move.notice,
			dataType:'json',
			data:{},  
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					var items=data.items;
					var imNotice = data.imNotice;
					if(items){
						var length=items.length;
						var html='';
						if(length==1){
							var obj=items[0];
							html=html+'<li><a href="'+obj.link+'">'+obj.title+'</a></li>';
							html=html+'<li><a href="'+obj.link+'">'+obj.title+'</a></li>';
						}else{
							for(var i=0;i<length;i++){
								var obj=items[i];
								var title=obj.title;
								var url=obj.link;
								var id=obj.id;
								if(url){
									html=html+'<li><a href="'+url+'">'+title+'</a></li>';
								}
							}
						}
						$("#topline ul").html(html);
						$(".smarticktemp").smarticker({
							title:'<img alt="" src="images/icon_announcement.png">'
						});
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	announcement:function(){
		var tipH = move.storage.getS("tipH");
		if(!tipH || tipH==="0"){
			var map = {};
			map['type'] =3;
			var mw=baseObj.mw(map);
			$.ajax({
				type: "POST",
				url:move.importantNotice,
				dataType:'json',
				data:{
					mw:mw
				},  
				success: function(ret){
					var code=ret.code;
					var data=ret.data;
					if(code=='200'){
						if(data){
							mui.alert(data.content,data.title);
							move.storage.setS("tipH","1");
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}
		
	},
	xiazhu:function(){
		$.ajax({
			type: "POST",
			url:move.latestList,
			dataType:'json',
			data:{},  
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					var items=data.items;
					if(items){
						var length=items.length;
						var html='';
						for(var i=0;i<length;i++){
							var obj=items[i];
							var title=obj.gameName;
							var type=obj.type;
							var img=obj.img;
							var openSessionNo=obj.openSessionNo;
							var gameCode=obj.gameCode;
							html=html+'<li class="mui-table-view-cell"><a href="'+gameCode+'" class="mui-navigate-right">';
							html=html+'<div class="logo-box"><img class="lazy" data-original="'+img+'"></div><div class="title-box"><div class="title">'+title+'</div>';
							html=html+'<div class="sbtitle">第'+openSessionNo+'期</div></div></a></li>';
						}
						$("#xiazhubox").html(html);
						
						//$("img.lazy").lazyload({effect: "fadeIn"});
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	xiazhuUpdate:function(){//更新当前期号信息
		// $.ajax({
		// 	type: "POST",
		// 	url:move.latestList,
		// 	dataType:'json',
		// 	data:{},  
		// 	success: function(ret){
		// 		var code=ret.code;
		// 		var data=ret.data;
		// 		if(code=='200'){
		// 			var items=data.items;
		// 			if(items){
		// 				var length=items.length;
		// 				var html='';
		// 				for(var i=0;i<length;i++){
		// 					var obj=items[i];
		// 					if(obj.openSessionNo){
		// 						$("#sbtitle_"+obj.type).html("第"+obj.openSessionNo+"期");
		// 						$("#openNum_"+obj.type).html(move.openUtil.fmtNumStyle(obj.openResult,obj.type));
		// 						$(".open-num-span").fadeIn(600);
		// 					}else{
		// 						$("#sbtitle_"+obj.type).html("...");
		// 					}
		// 				}
		// 			}
		// 		}
		// 	},
		// 	error: function (jqXHR, textStatus, errorThrown) {
		// 	}
		// });

		//return ;
		//从各彩种获取最新开奖数据
		$("#xy-bet-list li").each(function(){
			var dataArr = $(this).attr("data").split(",");
			var code = dataArr[0];
			var type = dataArr[1];
			indexObj.xiazhuPageAjax(code,type);
		});
		$("#gf-bet-list li").each(function(){
			var dataArr = $(this).attr("data").split(",");
			var code = dataArr[0];
			var type = dataArr[1];
			indexObj.xiazhuPageAjaxGf(code,type);
		});
	},
	xiazhuPageAjax:function(code,type){
		$.ajax({
			type: "POST",
			url:"/api/"+move.openUtil.fmtCode(code)+"_currentTime",
			dataType:'json',
			data:{},  
			success: function(ret){
				var data=ret.data;
				if(ret.code=='200'){
					$("#sbtitle_"+type).html("第"+data.obj.lastSessionNo+"期");
					if(data.obj.lastSessionNo && data.obj.openResult.length>0){
						$("#openNum_"+type).html(move.openUtil.fmtNumStyle(data.obj.openResult,type));
					}else{
						$("#openNum_"+type).html(move.openUtil.fmtNumStyle(null,type));
					}
					$(".open-num-span").fadeIn(200);

					if(data.obj.betTime && data.obj.betTime>0){
						indexObj["hBetIng"+type] = parseInt(data.obj.betTime);//倒计时量
						indexObj["hBetTimer"+type] = window.setInterval(function(){
							var hms = move.widget.fmtTimeHHMMSSObj(indexObj["hBetIng"+type]);
							$("#timerShow_"+type).html(hms.h+":"+hms.m+":"+hms.s);
							indexObj["hBetIng"+type]--;
							if(indexObj["hBetIng"+type]<0){//重要调用接接口
								window.clearInterval(indexObj["hBetTimer"+type]);
								window.setTimeout(function(){
									indexObj.xiazhuPageAjax(code,type);
								},5000)
							}
						},1000);
					}else{
						window.setTimeout(function(){
							indexObj.xiazhuPageAjax(code,type);
						},5000)
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	xiazhuPageAjaxGf:function(code,type){
		$.ajax({
				type: "POST",
				url:"/api/"+move.openUtil.fmtCode(code,true)+"_sessionInfo",
				dataType:'json',
				data:{},  
				success: function(ret){
					var data=ret.data;
					if(ret.code=='200'){
						$("#sbtitle_"+type).html("第"+data.preSessionNo+"期");
						if(data.preSessionNo && data.resultItems.length>0){
							$("#openNum_"+type).html(move.openUtil.fmtNumStyle(data.resultItems,type));
						}else{
							$("#openNum_"+type).html(move.openUtil.fmtNumStyle(null,type));
						}
						$(".open-num-span").fadeIn(200);

						if(data.betTime && data.betTime>0){
							indexObj["hBetIng"+type] = parseInt(data.betTime);//倒计时量
							indexObj["hBetTimer"+type] = window.setInterval(function(){
								var hms = move.widget.fmtTimeHHMMSSObj(indexObj["hBetIng"+type]);
								$("#timerShow_"+type).html(hms.h+":"+hms.m+":"+hms.s);
								indexObj["hBetIng"+type]--;
								if(indexObj["hBetIng"+type]<0){//重要调用接接口
									window.clearInterval(indexObj["hBetTimer"+type]);
									window.setTimeout(function(){
										indexObj.xiazhuPageAjaxGf(code,type);
									},5000)
								}
							},1000);
						}else{
							window.setTimeout(function(){
								indexObj.xiazhuPageAjaxGf(code,type);
							},5000)
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
	},
	open:function(){
		$.ajax({
			type: "POST",
			url:move.latestList,
			dataType:'json',
			data:{},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					var items=data.items;
					if(items){
						var size=items.length;
						var html='';
						for(var i=0;i<size;i++){
							var obj=items[i];
							var title=obj.gameName;
							var type=obj.type;
							var img=obj.img;
							var time=obj.time;
							var openSessionNo=obj.openSessionNo;
							var openResult=obj.openResult;
							var gameCode=obj.code;
							var playCate=obj.cate;
							var openhtml="";
							var resultArr=new Array();
							if(type=='13'){//双色球
								for ( var j = 0; j < openResult.length; j++) {
									var num=openResult[j];
									if(j==6){
										openhtml=openhtml+'<span class="num bg-blue">'+num+'</span>';
									}else{
										openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
									}
								}
							}else if(parseInt(type)>100 && parseInt(type)<200){//赛车类
								for ( var j = 0; j < openResult.length; j++) {
									var num=openResult[j];
									openhtml=openhtml+'<span class="num pkn-'+parseInt(num)+'">'+num+'</span>';
								}
							}else if(type==359||type==360||type==309||type==310){//六合彩类
								for ( var j = 0; j < openResult.length; j++) {
									var num=openResult[j];
									openhtml=openhtml+'<span class="num bg-red lhc'+openResult[j]+'">'+num+'</span>';
								}
							}else{//非双色球
								for ( var j = 0; j < openResult.length; j++) {
									var num=openResult[j];
									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
								}
							}
//							else if(type=='2'){//新加坡幸运28
//								//0=无波色 1=绿波 2=蓝波 3=红波
//								var length=openResult.length;
//								if(length==5){
//									for (var j = 0; j <3; j++) {
//										openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//										if(j==2){
//											openhtml=openhtml+'<span class="symbol">=</span>';
//										}else{
//											openhtml=openhtml+'<span class="symbol">+</span>';
//										}
//									}
//									var colorSize=openResult[4];
//									var colorClass='bg-white';
//									if(colorSize=='0'){
//										colorClass='bg-white';
//									}else if(colorSize=='1'){
//										colorClass='bg-green';
//									}else if(colorSize=='2'){
//										colorClass='bg-blue';
//									}else if(colorSize=='3'){
//										colorClass='bg-red';
//									}
//									openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
//								}
//							}else if(type=='3'){//重庆时时彩
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}else if(type=='4'){//PC蛋蛋
//								//0=无波色 1=绿波 2=蓝波 3=红波
//								var length=openResult.length;
//								if(length==5){
//									for (var j = 0; j <3; j++) {
//										openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//										if(j==2){
//											openhtml=openhtml+'<span class="symbol">=</span>';
//										}else{
//											openhtml=openhtml+'<span class="symbol">+</span>';
//										}
//									}
//									var colorSize=openResult[4];
//									var colorClass='bg-white';
//									if(colorSize=='0'){
//										colorClass='bg-white';
//									}else if(colorSize=='1'){
//										colorClass='bg-green';
//									}else if(colorSize=='2'){
//										colorClass='bg-blue';
//									}else if(colorSize=='3'){
//										colorClass='bg-red';
//									}
//									openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
//								}
//							}else if(type=='5'){//广东快乐十分
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}else if(type=='6'){//天津时时彩
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}else if(type=='7'){//新疆时时彩
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}else if(type=='8'){//快乐扑克3
//								var length=openResult.length;
//								if(length==5){
//									for (var j = 0; j <3; j++) {
//										openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//										if(j==2){
//											openhtml=openhtml+'<span class="symbol">=</span>';
//										}else{
//											openhtml=openhtml+'<span class="symbol">+</span>';
//										}
//									}
//									var colorSize=openResult[4];
//									var colorClass='bg-white';
//									if(colorSize=='0'){
//										colorClass='bg-gray';
//									}else if(colorSize=='1'){
//										colorClass='bg-coffee';
//									}else if(colorSize=='2'){
//										colorClass='bg-blue2';
//									}else if(colorSize=='3'){
//										colorClass='bg-violet';
//									}else if(colorSize=='4'){
//										colorClass='bg-green2';
//									}else if(colorSize=='5'){
//										colorClass='bg-red2';
//									}
//									openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
//								}
//							}else if(type=='9'){//广东11选5
//								for (var j = 0; j <openResult.length; j++) {
//									openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//									if(j==4){
//										openhtml=openhtml+'<span class="symbol">=</span>';
//									}else if((openResult.length-1)==j){
//									}else{
//										openhtml=openhtml+'<span class="symbol">+</span>';
//									}
//								}
//							}else if(type=='10'){//江苏快三
//								//0=无波色 1=绿波 2=蓝波 3=红波
//								var length=openResult.length;
//								if(length==5){
//									for (var j = 0; j <3; j++) {
//										openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//										if(j==2){
//											openhtml=openhtml+'<span class="symbol">=</span>';
//										}else{
//											openhtml=openhtml+'<span class="symbol">+</span>';
//										}
//									}
//									var colorSize=openResult[4];
//									var colorClass='bg-white';
//									if(colorSize=='0'){
//										colorClass='bg-white';
//									}else if(colorSize=='1'){
//										colorClass='bg-green';
//									}else if(colorSize=='2'){
//										colorClass='bg-blue';
//									}else if(colorSize=='3'){
//										colorClass='bg-red';
//									}
//									openhtml=openhtml+'<span class="num '+colorClass+'">'+openResult[3]+'</span>';
//								}
//							}else if(type=='11'){//广西快乐十分
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}else if(type=='12'){//
//								for (var j = 0; j <openResult.length; j++) {
//									openhtml=openhtml+'<span class="num bg-red">'+openResult[j]+'</span>';
//									if(j==5){
//										openhtml=openhtml+'<span class="symbol">=</span>';
//									}
//								}
//							}else if(type=='1'){//北京赛车
//								for ( var j = 0; j < openResult.length; j++) {
//									var num=openResult[j];
//									openhtml=openhtml+'<span class="num bg-red">'+num+'</span>';
//								}
//							}
							html=html+'<li class="mui-table-view-cell"><a href="/game/'+(playCate=="1"?"gf/":"xy/")+gameCode+'/openList.html"><div class="title-box"><div class="title">'+title+'</div>';
							html=html+'<div class="time">第'+openSessionNo+'期 '+time+'</div></div>';
							html=html+'<div class="open-number">'+openhtml+'</div></a></li>';
						}
						$("#openbox").html(html);
					}
				}else{
					
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	winList:function(){
		$.ajax({
			type: "POST",
			url:move.winList,
			dataType:'json',
			data:{},  
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					var items=data.winlist;
					if(items){
						var length=items.length;
						var html='';
						for(var i=0;i<length;i++){
							html=html+'<li class="winlist-data-cell">'+items[i]+'</li>';
						}
						$("#winlistdatabox").html(html);
						$("#winlistdatabox").smarticker({});
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	gameColumn:function(){
		$.ajax({
			type: "POST",
			url:move.gameColumn,
			dataType:'json',
			data:{},  
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				if(code=='200'){
					var gfItems=data.gfGamelist;
					var xyItems=data.xyGamelist;
					var redPacketsSwitch=data.redPacketsSwitch;
					var turnTableSwitch=data.turnTableSwitch;
					if(gfItems&&1>2){// 没有调用接口的数据，在页面上写死了
						var length=gfItems.length;
						var gfHtml='';
						for(var i=0;i<length;i++){
							var obj=gfItems[i];
							var status=obj.status;
							var img=obj.img;
							var title=obj.title;
							var subtitle=obj.subtitle;
							var type=obj.type;
							var url=obj.path;
							var exp=obj.exp;
							if (status=='1') {// 有效才显示
								if(type=='101'){// 北京赛车
									url="gf/bjpk10";
								}else if(type=='102'){// 极速赛车
									url="gf/sfpk10";
								}else if(type=='103'){// 三分PK拾
									url="gf/";
								}else if(type=='104'){//幸运飞艇
									url="gf/";
								}else if(type=='105'){//极速飞艇
									url="gf/";
								}else if(type=='201'){//重庆时时彩
									url="gf/cqssc";
								}else if(type=='202'){//新疆时时彩
									url="gf/";
								}else if(type=='203'){//天津时时彩
									url="gf/tjssc";
								}else if(type=='204'){//江西时时彩
									url="gf/";
								}else if(type=='205'){//北京时时彩
									url="gf/";
								}else if(type=='206'){//北京三分彩
									url="gf/bj3";
								}else if(type=='207'){//三分彩
									url="gf/sfc";
								}else if(type=='208'){//五分彩
									url="gf/wfc";
								}else if(type=='202'){//新疆时时彩
									url="gf/";
								}else if(type=='301'){//北京快乐8
									url="gf/";
								}else if(type=='302'){//新加坡幸运28
									url="gf/xjp28";
								}else if(type=='303'){//PC蛋蛋(北京幸运28)
									url="gf/pcdd";
								}else if(type=='305'){//广东快乐10分
									url="gf/gdk10";
								}else if(type=='306'){//广西快乐十分
									url="gf/gxk10";
								}else if(type=='307'){//重庆快乐十分(幸运农场)
									url="gf/";
								}else if(type=='308'){//快乐扑克3
									url="gf/";
								}else if(type=='309'){//香港六合彩
									url="gf/";
								}else if(type=='310'){//极速六合彩
									url="gf/";
								}else if(type=='311'){//双色球
									url="gf/ssq";
								}else if(type=='501'){// 广东11选5
									url="gf/gdpick11";
								}else if(type=='502'){//江西11选5
									url="gf/jxpick11";
								}else if(type=='503'){//山东11选5
									url="gf/sdpick11";
								}else if(type=='504'){//山西11选5
									url="gf/sxpick11";
								}else if(type=='505'){//北京11选5
									url="gf/bjpick11";
								}else if(type=='506'){//天津11选5
									url="gf/tjpick11";
								}else if(type=='507'){//河北11选5
									url="gf/hebpick11";
								}else if(type=='508'){//内蒙古11选5
									url="gf/nmgpick11";
								}else if(type=='509'){//辽宁11选5
									url="gf/lnpick11";
								}else if(type=='510'){//吉林11选5
									url="gf/jlpick11";
								}else if(type=='511'){//黑龙江11选5
									url="gf/hljpick11";
								}else if(type=='512'){//上海11选5
									url="gf/shpick11";
								}else if(type=='513'){//江苏11选5
									url="gf/jspick11";
								}else if(type=='514'){//浙江11选5
									url="gf/zjpick11";
								}else if(type=='515'){//安徽11选5
									url="gf/ahpick11";
								}else if(type=='516'){//福建11选5
									url="gf/fjpick11";
								}else if(type=='517'){//河南11选5
									url="gf/hebpick11";
								}else if(type=='518'){//湖北11选5
									url="gf/hubpick11";
								}else if(type=='519'){//广西11选5
									url="gf/gxpick11";
								}else if(type=='520'){//贵州11选5
									url="gf/gzpick11";
								}else if(type=='521'){//陕西11选5
									url="gf/shxpick11";
								}else if(type=='522'){//甘肃11选5
									url="gf/gspick11";
								}else if(type=='523'){//新疆11选5
									url="gf/xjpick11";
								}else if(type=='524'){//云南11选5
									url="gf/ynpick11";
								}else if(type=='601'){//江苏快3
									url="gf/jsk3";
								}else if(type=='602'){//安徽快3
									url="gf/ahk3";
								}else if(type=='603'){//北京快3
									url="gf/bjk3";
								}else if(type=='604'){//福建快3
									url="gf/fjk3";
								}else if(type=='605'){//贵州快3
									url="gf/gzk3";
								}else if(type=='606'){//广西快3
									url="gf/gxk3";
								}else if(type=='607'){//甘肃快3
									url="gf/gsk3";
								}else if(type=='608'){//湖北快3
									url="gf/hubk3";
								}else if(type=='609'){//河北快3
									url="gf/hebk3";
								}else if(type=='610'){//河南快3
									url="gf/hnk3";
								}else if(type=='611'){//江西快3
									url="gf/jxk3";
								}else if(type=='612'){//吉林快3
									url="gf/jlk3";
								}else if(type=='613'){//内蒙古快3
									url="gf/nmgk3";
								}else if(type=='614'){//上海快3
									url="gf/shk3";
								}
								gfHtml=gfHtml+'<li class="caizhong-cell"><a href="'+url+'"><div class="ico"><img src="'+img+'"></div>';
								gfHtml=gfHtml+'<div class="name-box"><div class="exp">'+exp+'</div><div class="title">'+title+'</div><div class="subtitle">'+subtitle+'</div></div></a></li>';
							}
						}
						$("#gf-bet-list").html(gfHtml);
					}
					if(xyItems&&1>2){// 没有调用接口的数据，在页面上写死了
						var length=xyItems.length;
						var xyHtml='';
						for(var i=0;i<length;i++){
							var obj=xyItems[i];
							var status=obj.status;
							var img=obj.img;
							var title=obj.title;
							var subtitle=obj.subtitle;
							var type=obj.type;
							var url=obj.path;
							if (status=='1') {// 有效才显示
								 if(type=='151'){
								 	url="xy/bjpk10";
								 }else if(type=='152'){
								 	url="xy/sfpk10";
								 }else if(type=='153'){
								 	url="xy/sfpk102";
								 }else if(type=='154'){
								 	url="xy/xyft";
								 }else if(type=='155'){
								 	url="xy/jsft";
								 }else if(type=='251'){
								 	url="xy/cqssc";
								 }else if(type=='252'){
								 	url="xy/xjssc";
								 }else if(type=='253'){
								 	url="xy/tjssc";
								 }else if(type=='254'){
								 	url="xy/";
								 }else if(type=='255'){
								 	url="xy/bjssc";
								 }else if(type=='256'){
								 	url="xy/bj3";
								 }else if(type=='257'){
								 	url="xy/";
								 }else if(type=='258'){
								 	url="xy/five";
								 }else if(type=='252'){
								 	url="xy/xjssc";
								 }else if(type=='351'){
								 	url="xy/";
								 }else if(type=='352'){
								 	url="xy/xjplu28";
								 }else if(type=='353'){
								 	url="xy/bjlu28";
								 }else if(type=='355'){
								 	url="xy/gdk10";
								 }else if(type=='356'){
								 	url="xy/gxk10";
								 }else if(type=='357'){
								 	url="xy/cqk10";
								 }else if(type=='358'){
								 	url="xy/poker";
								 }else if(type=='359'){
								 	url="xy/marksix";
								 }else if(type=='360'){
								 	url="xy/sflhc";
								 }else if(type=='361'){
								 	url="xy/";
								 }else if(type=='551'){
								 	url="xy/gdpick11";
								 }else if(type=='552'){
								 	url="xy/jxpick11";
								 }else if(type=='553'){
								 	url="xy/";
								 }else if(type=='554'){
								 	url="xy/";
								 }else if(type=='555'){
								 	url="xy/";
								 }else if(type=='556'){
								 	url="xy/";
								 }else if(type=='557'){
								 	url="xy/";
								 }else if(type=='558'){
								 	url="xy/";
								 }else if(type=='559'){
								 	url="xy/";
								 }else if(type=='560'){
								 	url="xy/";
								 }else if(type=='561'){
								 	url="xy/";
								 }else if(type=='562'){
								 	url="xy/";
								 }else if(type=='563'){
								 	url="xy/";
								 }else if(type=='564'){
								 	url="xy/";
								 }else if(type=='565'){
								 	url="xy/";
								 }else if(type=='566'){
								 	url="xy/";
								 }else if(type=='567'){
								 	url="xy/";
								 }else if(type=='568'){
								 	url="xy/";
								 }else if(type=='569'){
								 	url="xy/";
								 }else if(type=='570'){
								 	url="xy/";
								 }else if(type=='571'){
								 	url="xy/";
								 }else if(type=='572'){
								 	url="xy/";
								 }else if(type=='573'){
								 	url="xy/";
								 }else if(type=='574'){
								 	url="xy/";
								 }else if(type=='651'){
								 	url="xy/jsk3";
								 }else if(type=='652'){
								 	url="xy/";
								 }else if(type=='653'){
								 	url="xy/";
								 }else if(type=='654'){
								 	url="xy/";
								 }else if(type=='655'){
								 	url="xy/";
								 }else if(type=='656'){
								 	url="xy/";
								 }else if(type=='657'){
								 	url="xy/";
								 }else if(type=='658'){
								 	url="xy/";
								 }else if(type=='659'){
								 	url="xy/";
								 }else if(type=='660'){
								 	url="xy/";
								 }else if(type=='661'){
								 	url="xy/";
								 }else if(type=='662'){
								 	url="xy/";
								 }else if(type=='663'){
								 	url="xy/";
								 }else if(type=='664'){
								 	url="xy/";
								 }
								 xyHtml=xyHtml+'<li class="caizhong-cell"><a href="'+url+'"><div class="ico"><img src="'+img+'"></div>';
								 xyHtml=xyHtml+'<div class="name-box"><div class="exp">'+exp+'</div><div class="title">'+title+'</div><div class="subtitle">'+subtitle+'</div></div></a></li>';
							}
						}
						$("#xy-bet-list").html(xyHtml);
					}
					if(redPacketsSwitch=='1'){
						$("#redpack").show();
						$("#redpack").bind("click", function(){
							baseObj.openView('redpack');
						});
					}
					if(turnTableSwitch=='1'){
						$("#turntable").show();
						$("#turntable").bind("click", function(){
							baseObj.openView('turntable');
						});
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	zoushi:function(type,t){
		if(typeof type=='undefined'){
			type='0';
		}
		var url="";
		if(type=='0'){//三分彩
			url="/api/bj3_trend";
		}else if(type=='1'){
			url="/api/bjPk10_trend";
		}else if(type=='2'){
			url="/api/xjpLu28_trend";
		}else if(type=='3'){
			url="/api/cqSsc_trend";
		}else if(type=='4'){
			url="/api/bjLu28_trend";
		}else if(type=='5'){
			url="/api/gdK10_trend";
		}else if(type=='6'){
			url="/api/tjSsc_trend";
		}else if(type=='7'){
			url="/api/xjSsc_trend";
		}else if(type=='8'){
			url="/api/poker_trend";
		}else if(type=='9'){
			url="/api/gdPick11_trend";
		}else if(type=='10'){
			url="/api/jsK3_trend";
		}else if(type=='11'){
			url="/api/gxK10_trend";
		}else if(type=='12'){
			url="/api/markSix_trend";
		}
		
		var mask=move.createLoading();
		mask.show();
		if(typeof t=='undefined'){
			t='0';
		}
		var map = {};
		map['type'] =t;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.server+url,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var length=items.length;
					var cssid='tab'+t;
					var html='<ul class="zoushi-table" id="'+cssid+'">';
					for (var i = 0; i < length; i++) {
						var liclass='single';
						if(i%2==1){
							liclass='double';
						}
						html=html+'<li class="'+liclass+'">';
						var obj=items[i];
						var size=obj.length;
						for (var j = 0; j < size; j++) {
							var va=obj[j];
							var spanclass='';
							if(va=='大'||va=='双'||va=='龙'||va=='家禽'||va=='红波'){
								spanclass='colour-red';
							}else if(va=='小'||va=='单'||va=='虎'||va=='野兽'){
								spanclass='colour-green';
							}else if(va=='蓝波'){
								spanclass='colour-blue';
							}else if(va=='对子'){
								spanclass='colour-coffee';
							}else if(va=='同花'){
								spanclass='colour-blue2';
							}else if(va=='顺子'){
								spanclass='colour-violet';
							}else if(va=='同花顺'){
								spanclass='colour-green2';
							}
							html=html+'<span class="lattice '+spanclass+'">'+va+'</span>';
						}
						html=html+'</li>';
					}
					html=html+'</ul>';
					$('#zou'+type+' .zoushi-table-box').html(html);
				}else{
					mui.toast(msg,{
					    duration:'long',
					    type:'div' 
					});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	bindGfXy:function(){
		$(".mui-btn").unbind("click");
		$(".mui-btn").bind("click", function(){
			$(".gf-xy-btn").removeClass("active-btn");
			$(this).addClass("active-btn");
			var data=$(this).attr('data');
			if(data=='1'){
				$("#gfGameColumnbox").show();
				$("#xyGameColumnbox").hide();
			}else if(data=='2'){
				$("#xyGameColumnbox").show();
				$("#gfGameColumnbox").hide();
			}
		});
	},
	turntableInit:function(){
		var map = {};
		if(move.user.u){
			map['u'] = move.user.u;
		}
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.lottery_turnTableInfo,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var result=ret.data;
					var items=result.items;
					
					indexObj.turntableArr=items;
					
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var title=obj.title;
						var bonus=obj.bonus;
						$('lable'+(i+1)).text(title);
					}
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	turn:function(){
		if(move.user.u!=null){
			var map = {};
			map['u'] = move.user.u;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			mask.show();
			$.ajax({
				type: "POST",
				url:move.lottery_turnTable,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var msg=ret.msg;
					if(code=='200'){
						var result=ret.data;
						var bonus=result.bonus;
						
						var items=indexObj.turntableArr;
						
						for (var i = 0; i < items.length; i++) {
							var obj=items[i];
							var bonustemp=obj.bonus;
							if(bonus==bonustemp){
								newdraw.goto((i+1));
								indexObj.turnResultMsg = msg;//鎶藉缁撴灉
								break;
							}
						}
					}else{
						mui.alert(msg,document.title, function() {});
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
			baseObj.openLogin();
		}
	},
	turnShow:function(){
		if(indexObj.turnResultMsg){
			mui.alert(indexObj.turnResultMsg,document.title, function() {});
		}
	},
	receiveRed:function(){
		if(move.user.u!=null){
			var map = {};
			map['u'] = move.user.u;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			mask.show();
			$.ajax({
				type: "POST",
				url:move.lottery_drawRedPackets,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var msg=ret.msg;
					if(code=='200'){
						var result=ret.data;
						var fixedMoney=result.fixedMoney;
						
						$('#money').text(fixedMoney);
						$('#succ').show();
						$('.mask2').show();
					}else{
						$('#fail .explain').text(msg);
						$('#fail').show();
						$('.mask2').show();
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
			baseObj.openLogin();
		}
	},
	chkSign:function(){
		if(move.user.u!=null){
			var map = {};
			map['u'] = move.user.u;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			mask.show();
			$.ajax({
				type: "POST",
				url:move.user_signCheck,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var msg=ret.msg;
					if(code=='200'){
						var result=ret.data;
						var status=result.signStatus;
						if(status=='1'){
							$('.succsign').show();
						}else{
							$('.notsign').show();
							$(".notsign").bind("click", function(){
								indexObj.sign();
							});
						}
					}else{
						$('.notsign').show();
						$(".notsign").bind("click", function(){
							indexObj.sign();
						});
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
//			baseObj.openLogin();
		}
	},
	sign:function(){
		if(move.user.u!=null){
			var map = {};
			map['u'] = move.user.u;
			var mw=baseObj.mw(map);
			var mask=move.createLoading();
			mask.show();
			$.ajax({
				type: "POST",
				url:move.baseData_sign,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					mask.close();
					var code=ret.code;
					var msg=ret.msg;
					mui.alert(msg,document.title, function() {});
					if(code=='200'){
						var result=ret.data;
						var msg=result.msg;
						$('.notsign').hide();
						$('.succsign').show();
					}else{
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}else{
			baseObj.openLogin();
		}
	},
	helpList:function(){
		var map = {};
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show(true);
		$.ajax({
			type: "POST",
			url:move.helpList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				mask.close();
				var code=ret.code;
				var msg=ret.msg;
				if(code=='200'){
					var result=ret.data;
					var items = result.items;
					//客服
					if(result.onlineServerUrl) $(".g-kefu-abtn").attr("href",result.onlineServerUrl);
					//app下载
					if(result.appDownUrl) $(".g-appdown-abtn").attr("href",result.appDownUrl);
					//底部帮助
					for(var i = 0;i<items.length;i++){
						var tmp = items[i];
						var as = $(".short-bottom a");
						if(tmp.type==5){
							var a1 = as[0];
							$(a1).attr('href','javascript:baseObj.openView(\''+tmp.link+'\');');
						}else if(tmp.type==6){
							var a1 = as[1];
							$(a1).attr('href','javascript:baseObj.openView(\''+tmp.link+'\');');
						}else if(tmp.type==7){
							var a1 = as[2];
							$(a1).attr('href','javascript:baseObj.openView(\''+tmp.link+'\');');
						}else if(tmp.type==8){
							var a1 = as[3];
							$(a1).attr('href','javascript:baseObj.openView(\''+tmp.link+'\');');
						}
					}
				}else{
//						$('#fail .explain').text(msg);
//						$('#fail').show();
					$('.mask2').show();
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
		
	}
}
indexObj.regcc = function(){
	if(move.user&&move.user.u&&move.user.userType==1){
		mui.alert("您已登录！");
		return;
	}
	baseObj.openView('register.html');
	//var mask=move.createLoading();
	//mask.show();
	// $.ajax({
	// 	type: "POST",
	// 	url:move.regcc,
	// 	dataType:'json',
	// 	data:{mw:baseObj.mw({})},  
	// 	success: function(ret){
	// 		mask.close();
	// 		var code=ret.code;
	// 		if(code=='200'){
	// 			var data=ret.data;
	// 			var type=data.regType;
	// 			if(type=='1'){
	// 				baseObj.openView('register.html');
	// 			}else if(type=='2'){
	// 				//baseObj.openView('register2.html');
	// 				baseObj.openView('register.html');
	// 			}
	// 		}else{
	// 		}
	// 	},
	// 	error: function (jqXHR, textStatus, errorThrown) {
	// 	}
	// });
}
indexObj.login = function(){
	if(move.user&&move.user.u&&move.user.userType==1){
		mui.alert("您已登录！");
	}else{
		baseObj.openView("login.html");
	}
}
indexObj.recharge = function(){
	if(move.user&&move.user.u){
		if(move.user.userType==1){
			baseObj.openView("wallet.html");
		}else{
			mui.alert("体验客户无权限！");
		}
	}else{
		baseObj.openView("login.html");
	}
}
indexObj.signame = function(){
	if(move.user&&move.user.u){
		if(move.user.userType==1){
			baseObj.openView("sign.html");
		}else{
			mui.alert("体验客户无权限！");
		}
	}else{
		baseObj.openView("login.html");
	}
}