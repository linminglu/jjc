//大玩法选择
lotteryObj.playSelector = {
	//plays:{p0:"两面盘",p1:"1-10名",p2:"冠亚军和"},
	//gfxy:false,
	//code:"",
	set:function(param){
		lotteryObj.playSelector.paramSet = {};
		lotteryObj.playSelector.code = param.code;
		lotteryObj.playSelector.gfxy = param.gfxy?param.gfxy:false;
		lotteryObj.playSelector.plays = param.plays;
		lotteryObj.playSelector.paramSet.wanfa = param.wanfa;
		lotteryObj.playSelector.paramSet.wanfatab = param.wanfatab;
	},
	init:function(playType){
		lotteryObj.playSelector.backInit();
		lotteryObj.playSelector.menuInit();
		//if(location.href.indexOf("marksix")>-1 || location.href.indexOf("sflhc")>-1) return;
		
		lotteryObj.shtml = "";
		lotteryObj.shtml += "<label>玩法</label>";
		lotteryObj.shtml += "<span class=\"gp-cur\">";
		lotteryObj.shtml += "<strong class=\"gp-play-name\">"+lotteryObj.playSelector.getName(playType)+"</strong>";
		lotteryObj.shtml += "<i class=\"mui-icon mui-icon-arrowdown\"></i>";
		lotteryObj.shtml += "</span>";
		lotteryObj.shtml += "<span class=\"gp-gfxy\">"+(lotteryObj.playSelector.isGF()?"官":"信")+"</span>";
		$(".g-plays").html(lotteryObj.shtml);
		lotteryObj.playSelector.bind();

	},
	getName:function(playType){
		if(!playType) playType = lotteryObj.playSelector.def();
		return this.plays["p"+playType];
	},
	setName:function(playType){
		$(".gp-play-name").html(lotteryObj.playSelector.getName(playType));
	},
	gfxyTag:function(){
		return location.href.indexOf("/gf/")>0?"gf":"xy";
	},
	isGF:function(){
		return lotteryObj.playSelector.gfxyTag()=="gf"?true:false;
	},
	isLHC:function(){
		return  location.href.indexOf("/sflhc/")>0 || location.href.indexOf("/marksix/")>0?true:false;
	},
	def:function(){
		if(lotteryObj.playSelector.isGF()){
			return $($(".ga-play-item.mui-active").get(0)).attr("data");
		}else{
			if(lotteryObj.playSelector.isLHC()){
				return $($("#wanfatab .mui-table-view-cell").get(0)).attr("t");
			}else{
				if($(".ga-play-item.mui-active").length>0){
					return $($(".ga-play-item.mui-active").get(0)).attr("t");
				}else if($(".tab-switch-cell.mui-active").length>0){
					return $($(".tab-switch-cell.mui-active").get(0)).attr("t");
				}
			}
		}
	},
	getTabObj:function(playType){
		if(lotteryObj.playSelector.isGF()){
			return $(".ga-play-item[data="+playType+"]").get(0);
		}else if(lotteryObj.playSelector.isLHC()){
			return $(".mui-wanfa-cell[t="+playType+"]").get(0);
		}else{
			var target;
			if($(".ga-play-item[t="+playType+"]").length>0){
				target =  $(".ga-play-item[t="+playType+"]").get(0);
			}else if($(".tab-switch-cell[t="+playType+"]").length>0){
				target =  $(".tab-switch-cell[t="+playType+"]").get(0);
			}
			return target;
		}
	},
	bind:function(){
		$(".g-plays").on("tap",function(){
			lotteryObj.playSelector.toggle();
		});
		if(lotteryObj.playSelector.isGF()){
			$(".ga-play-item").bind("touchend",function(){
				window.setTimeout(function(){
					lotteryObj.playSelector.setName(lotteryObj.playSelector.def());
				},200);
				
			});
			$(".mui-slider-item").bind("touchend",function(){
				window.setTimeout(function(){
					lotteryObj.playSelector.setName(lotteryObj.playSelector.def());
				},1000);
			});
		}
		if(lotteryObj.playSelector.isGF()){
			lotteryObj.playSelector.bindGFTab();
		}
	},
	bindGFTab:function(){
		$(".ga-play-item").on("tap",function(){
			$(".ga-play-selnum").hide();
			$(".ga-play-item").removeClass("mui-active");
			$(this).addClass("mui-active");
			lotteryObj.selector.preview();
			$($("#playItem"+$(this).attr("data"))).fadeIn();
		});
		var selPlay = $(".ga-play-item.mui-active").attr("data");
		if(selPlay){
			$("#playItem"+selPlay).fadeIn();
		}
	},
	toggle:function(playType){
		if(!playType) playType = lotteryObj.playSelector.def();
		if($(".g-play-selector").length==0){
			lotteryObj.shtml = "<div class=\"g-play-mask\"></div>";
			lotteryObj.shtml += "<div class=\"g-play-selector\">";
			if(lotteryObj.playSelector.gfxy){
				lotteryObj.shtml += "<div class=gps-type>";
				lotteryObj.shtml += "<div class=\"left\"><span data=\"xy\" class=\"tag xy"+(!lotteryObj.playSelector.isGF()?" act":"")+"\">信用彩票</span></div>";
				lotteryObj.shtml += "<div class=\"right\"><span data=\"gf\"  class=\"tag gf"+(lotteryObj.playSelector.isGF()?" act":"")+"\">官方彩票</span></div>";
				lotteryObj.shtml += "</div>";
			}
			lotteryObj.shtml += "<div class=\"gps-items"+(lotteryObj.playSelector.isLHC()?" gps-items-small":"")+(lotteryObj.playSelector.isGF()?" gps-items-gf":"")+"\">";
			for(key in lotteryObj.playSelector.plays){
				var type = key.replace(/p/g,"");
				lotteryObj.shtml += "<span class=\"item item-"+(lotteryObj.playSelector.isLHC()?"small":"big")+(playType==type?" act":"")+"\" data=\""+type+"\">"+lotteryObj.playSelector.plays[key]+"</span>";
			}
			lotteryObj.shtml += "</div>";
			$(document.body).append(lotteryObj.shtml);
			lotteryObj.playSelector.show();

			$(".gps-items span").bind("tap",function(){
				$(".gps-items span").each(function(){
					$(this).removeClass("act");
				});
				$(this).addClass("act");
				lotteryObj.playSelector.update($(this).attr("data"));

			});

			$(".g-play-mask").bind("tap",function(ev){
				if(ev.target && ev.target.className=="g-play-mask" && lotteryObj.playSelector.display){
					if(lotteryObj.playSelector.display){
						lotteryObj.playSelector.hide();
					}
				}
			});

			if(lotteryObj.playSelector.gfxy){
				$(".gps-type span").bind("tap",function(){
					lotteryObj.playSelector.view($(this).attr("data"));
				});
			}
		}else{
			if(!lotteryObj.playSelector.display){
				lotteryObj.playSelector.show();
			}else{
				lotteryObj.playSelector.hide();
			}
		}
	},
	view:function(gfxy){
		var gcode = lotteryObj.playSelector.code;
		if(gcode=="bj3" || gcode=="sfc"){
			gcode = gfxy=="xy"?"bj3":"sfc";
		}else if(gcode=="five" || gcode=="wfc"){
			gcode = gfxy=="xy"?"five":"wfc";
		}
		location.href="/game/"+gfxy+"/"+gcode;
	},
	show:function(){
		$(".g-play-mask").fadeIn();
		$(".g-play-mask").css("height",$(document).height());
		$(".g-play-selector").show();
		//$(document.body).css("overflow","hidden");
		window.setTimeout(function(){
			lotteryObj.playSelector.display = true;
		},200);
	},
	hide:function(){
		$(".g-play-mask").fadeOut();
		$(".g-play-selector").hide();
		//$(document.body).css("overflow","auto");
		lotteryObj.playSelector.display = false;
	},
	update:function(playType){
		if(!playType) playType = lotteryObj.playSelector.def();
		lotteryObj.playSelector.setName(playType);
		if(lotteryObj.playSelector.isGF()){
			lotteryObj.playSelector.playTypeClickGF(lotteryObj.playSelector.getTabObj(playType));
		}else{
			if(lotteryObj.playSelector.isLHC()){
				lotteryObj.playSelector.playTypeClickLHC(lotteryObj.playSelector.getTabObj(playType));
			}else{
				lotteryObj.playSelector.playTypeClick(lotteryObj.playSelector.getTabObj(playType));
			}
		}
		lotteryObj.playSelector.hide();
	},
	playTypeClickGF:function(obj){
		if(!obj) return;
		mui.trigger(obj,"tap");
		//var type = $(obj).attr("data");
	},
	playTypeClickLHC:function(obj,isSelf){
		if(isSelf) mui('#wanfatab').popover('toggle');
		$("#wanfatab .mui-wanfa-cell").removeClass('mui-active');
		$(obj).addClass('mui-active');
		$('.playmethod').hide();
		var idName=$(obj).attr('data');
		var type=$(obj).attr('t');
		$('#'+idName).show();
		$('#wanfa').val(type);
		$('#menu').html($(obj).text()+'<span class="mui-icon mui-icon-arrowdown"></span>');
		var html=$.trim($('#zx'+type).html());
		if(html==''){
			lotteryObj.loadPanel(type);
		}
		lotteryObj.playSelector.setName(type);
	},
	playTypeClick:function(obj){
		var param = lotteryObj.playSelector.paramSet;
		$(param.wanfatab).removeClass('mui-active');
		$(obj).addClass('mui-active');
		$('.playmethod').hide();
		var idName=$(obj).attr('data');
		var type=$(obj).attr('t');
		$('#'+idName).show();
		$(param.wanfa).val(type);
		if(type=='1'){
			var html=$.trim($('#zx1').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='2'){
			var html=$.trim($('#zx2').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='3'){
			var html=$.trim($('#zx3').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='4'){
			var html=$.trim($('#zx4').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='5'){
			var html=$.trim($('#zx5').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='6'){
			var html=$.trim($('#zx6').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='7'){
			var html=$.trim($('#zx7').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='8'){
			var html=$.trim($('#zx8').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='9'){
			var html=$.trim($('#zx9').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='10'){
			var html=$.trim($('#zx10').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='11'){
			var html=$.trim($('#zx11').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='12'){
			var html=$.trim($('#zx12').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='13'){
			var html=$.trim($('#zx13').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='14'){
			var html=$.trim($('#zx14').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='15'){
			var html=$.trim($('#zx15').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='16'){
			var html=$.trim($('#zx16').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}else if(type=='17'){
			var html=$.trim($('#zx17').html());
			if(html==''){
				lotteryObj.loadPanel(type);
			}
		}

		lotteryObj.playSelector.setName(type);
	}
}
lotteryObj.playSelector.backInit=function(){
	$(".gaxy-link-back").bind("tap",function(){//初始化返回按钮xy
		location = "/game";//xy
		//history.back();
	});
}

lotteryObj.playSelector.menuInit = function(){
	$("#topPopover").remove();
	var shtml = "";
	shtml += "<div id=\"topPopover\" class=\"mui-popover ga-menu-popover\">";
	shtml += "	<div class=\"mui-popover-arrow\"></div>";
	shtml += "	<div class=\"mui-scroll-wrapper\">";
	shtml += "		<div class=\"mui-scroll\">";
	shtml += "			<ul class=\"mui-table-view\">";
	shtml += "				<li class=\"mui-table-view-cell ga-menu-item\"><a href=\"../../touzhuList.html\">投注记录</a></li>";
	shtml += "				<li class=\"mui-table-view-cell ga-menu-item\"><a href=\"openList.html\">最近开奖</a></li>";
	shtml += "				<li class=\"mui-table-view-cell ga-menu-item\"><a id=\"rechargeABtn\">存取款</a></li>";
	shtml += "				<li class=\"mui-table-view-cell ga-menu-item\"><a class=\"ga-rule-link\">玩法说明</a></li>";
	shtml += "				<li class=\"mui-table-view-cell ga-menu-item\"><a>账户余额<span class=\"ga-user-money\">0.00</span></a></li>";
	shtml += "				</li>";
	shtml += "			</ul>";
	shtml += "		</div>";
	shtml += "	</div>";
	shtml += "</div>";
	$(".ga-view").append(shtml);
	lotteryObj.playSelector.updateMoney();
	$("#rechargeABtn").bind("tap",function(){
		move.openViewGuest("../../wallet.html");
	});
	$(".ga-item-menu").bind("tap",function(){
		lotteryObj.playSelector.updateMoney();
	});
	$(".ga-rule-link").on("tap",function(){
		location = "../../rule.html?c="+move.widget.getGameCode(location.href);;
	});
	// $(".ga-menu-item a").on("tap",function(){
	// 	mui(".mui-popover").popover("hide");
	// });
}

//更新余额
lotteryObj.playSelector.updateMoney = function(){
	if(move.user && move.user.u){
		$.ajax({
			type: "POST",
			url:move.money,
			dataType:'json',
			data:{mw:baseObj.mw({u:move.user.u})},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				$(".ga-user-money").html(ret.data.money);
				move.user.money=ret.data.money;
			},
			error: function (jqXHR, textStatus, errorThrown){}
		});
	}
}

$(function(){
	$(".help-box .close-icon").bind("tap", function(){
		$('.masktrans2').hide();
		$('#zoushibox').hide();
		$('#paimingbox').hide();
	});

	document.addEventListener('touchstart',function (event) {  
         if(event.touches.length>1){  
             event.preventDefault();  
         }  
     })  
     var lastTouchEnd=0;  
     document.addEventListener('touchend',function (event) {  
         var now=(new Date()).getTime();  
         if(now-lastTouchEnd<=300){  
             event.preventDefault();  
         }  
         lastTouchEnd=now;  
     },false)
});