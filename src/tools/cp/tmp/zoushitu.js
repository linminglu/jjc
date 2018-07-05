var zoushituObj={
	defPageSize:15,
	gameType:'',
	init:function(){
	},
	ssq:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.ssq_trend,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var reditems=obj.reditems;
						var blueitems=obj.blueitems;
						var redhtml='';
						for(var j=0;j<reditems.length;j++) {
							var ball=reditems[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							redhtml=redhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						var bluehtml='';
						for(var j=0;j<blueitems.length;j++) {
							var ball=blueitems[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='blueHighlight';
							}
							bluehtml=bluehtml+'<td class="blueball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						html=html+redhtml;
						html=html+'<td class="line"></td>';
						html=html+bluehtml;
					}
					$('#datalist').html(html);
					zoushituObj.switchssq();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchssq:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.ssq(type);
		});
	},
	cqssc:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.cqSsc_trendWeb,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.resultItems;
						var geArr=obj.ge;
						var shiArr=obj.shi;
						var baiArr=obj.bai;
						var qianArr=obj.qian;
						var wanArr=obj.wan;
						var zuArr=obj.zu;
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						html=html+'<td class="bd"></td>';
						
						var result='';
						for(var j=0;j<resultItems.length;j++) {
							var ball=resultItems[j];
							result=result+ball;
						}
						html=html+'<td>'+result+'</td>';
						html=html+'<td class="bd"></td>';
						
						var wanhtml='';
						for(var j=0;j<wanArr.length;j++) {
							var ball=wanArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							wanhtml=wanhtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+wanhtml;
						html=html+'<td class="bd"></td>';
						var qianhtml='';
						for(var j=0;j<qianArr.length;j++) {
							var ball=qianArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							qianhtml=qianhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+qianhtml;
						html=html+'<td class="bd"></td>';
						var baihtml='';
						for(var j=0;j<baiArr.length;j++) {
							var ball=baiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							baihtml=baihtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+baihtml;
						html=html+'<td class="bd"></td>';
						var shihtml='';
						for(var j=0;j<shiArr.length;j++) {
							var ball=shiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							shihtml=shihtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+shihtml;
						html=html+'<td class="bd"></td>';
						var gehtml='';
						for(var j=0;j<geArr.length;j++) {
							var ball=geArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							gehtml=gehtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+gehtml;
						html=html+'<td class="bd"></td>';
						
						var zuhtml='';
						for(var j=0;j<zuArr.length;j++) {
							var ball=zuArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
//								ball=ball.replace('&','');
								ball=ball.substring(0,1);
								clazz='redHighlight';
							}
							zuhtml=zuhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+zuhtml;
					}
					$('#datalist').html(html);
					zoushituObj.switchcqssc();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchcqssc:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.cqssc(type);
		});
	},
	sanfencai:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.three_trendWeb,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.resultItems;
						var geArr=obj.ge;
						var shiArr=obj.shi;
						var baiArr=obj.bai;
						var qianArr=obj.qian;
						var wanArr=obj.wan;
						var zuArr=obj.zu;
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						html=html+'<td class="bd"></td>';
						
						var result='';
						for(var j=0;j<resultItems.length;j++) {
							var ball=resultItems[j];
							result=result+ball;
						}
						html=html+'<td>'+result+'</td>';
						html=html+'<td class="bd"></td>';
						
						var wanhtml='';
						for(var j=0;j<wanArr.length;j++) {
							var ball=wanArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							wanhtml=wanhtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+wanhtml;
						html=html+'<td class="bd"></td>';
						var qianhtml='';
						for(var j=0;j<qianArr.length;j++) {
							var ball=qianArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							qianhtml=qianhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+qianhtml;
						html=html+'<td class="bd"></td>';
						var baihtml='';
						for(var j=0;j<baiArr.length;j++) {
							var ball=baiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							baihtml=baihtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+baihtml;
						html=html+'<td class="bd"></td>';
						var shihtml='';
						for(var j=0;j<shiArr.length;j++) {
							var ball=shiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							shihtml=shihtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+shihtml;
						html=html+'<td class="bd"></td>';
						var gehtml='';
						for(var j=0;j<geArr.length;j++) {
							var ball=geArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							gehtml=gehtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+gehtml;
						html=html+'<td class="bd"></td>';
						
						var zuhtml='';
						for(var j=0;j<zuArr.length;j++) {
							var ball=zuArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
//								ball=ball.replace('&','');
								ball=ball.substring(0,1);
								clazz='redHighlight';
							}
							zuhtml=zuhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+zuhtml;
					}
					$('#datalist').html(html);
					zoushituObj.switchsanfencai();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchsanfencai:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.sanfencai(type);
		});
	},
	wufencai:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.five_trendWeb,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.resultItems;
						var geArr=obj.ge;
						var shiArr=obj.shi;
						var baiArr=obj.bai;
						var qianArr=obj.qian;
						var wanArr=obj.wan;
						var zuArr=obj.zu;
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						html=html+'<td class="bd"></td>';
						
						var result='';
						for(var j=0;j<resultItems.length;j++) {
							var ball=resultItems[j];
							result=result+ball;
						}
						html=html+'<td>'+result+'</td>';
						html=html+'<td class="bd"></td>';
						
						var wanhtml='';
						for(var j=0;j<wanArr.length;j++) {
							var ball=wanArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							wanhtml=wanhtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+wanhtml;
						html=html+'<td class="bd"></td>';
						var qianhtml='';
						for(var j=0;j<qianArr.length;j++) {
							var ball=qianArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							qianhtml=qianhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+qianhtml;
						html=html+'<td class="bd"></td>';
						var baihtml='';
						for(var j=0;j<baiArr.length;j++) {
							var ball=baiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							baihtml=baihtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+baihtml;
						html=html+'<td class="bd"></td>';
						var shihtml='';
						for(var j=0;j<shiArr.length;j++) {
							var ball=shiArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='redHighlight';
							}
							shihtml=shihtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+shihtml;
						html=html+'<td class="bd"></td>';
						var gehtml='';
						for(var j=0;j<geArr.length;j++) {
							var ball=geArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='orangeHighlight';
							}
							gehtml=gehtml+'<td class="orangeball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+gehtml;
						html=html+'<td class="bd"></td>';
						
						var zuhtml='';
						for(var j=0;j<zuArr.length;j++) {
							var ball=zuArr[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
//								ball=ball.replace('&','');
								ball=ball.substring(0,1);
								clazz='redHighlight';
							}
							zuhtml=zuhtml+'<td class="redball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+zuhtml;
					}
					$('#datalist').html(html);
					zoushituObj.switchwufencai();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchwufencai:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.wufencai(type);
		});
	},
	gdpick11:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.gdPick11_trend,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.openitems;
						
						
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						var result='';
						
						for(var j=0;j<resultItems.length;j++) {
							var ball=resultItems[j];
							result=result+'<td class="chart-bg-kjhm">'+ball+'</td>';
						}
						html=html+result;
						
						var result2='';
						var subitems=obj.subitems;
						for(var j=0;j<subitems.length;j++) {
							var ball=subitems[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='blueHighlight';
							}
							result2=result2+'<td class="blueball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+result2;
						
						var he=obj.he;
						var kuadu=obj.kuadu;
						var daxiao=obj.daxiao;
						var jiou=obj.jiou;
						var zhihe=obj.zhihe;
						
						html=html+'<td>'+he+'</td>';
						html=html+'<td>'+kuadu+'</td>';
						html=html+'<td>'+daxiao+'</td>';
						html=html+'<td>'+jiou+'</td>';
						html=html+'<td>'+zhihe+'</td>';
						
					}
					$('#datalist').html(html);
					zoushituObj.switchgdpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgdpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gdpick11(type);
		});
	},
	jsk3:function(type){
		if(!type){
			type='';
		}
		var map = {};
		map['type'] =type;
		var mw=baseObj.mw(map);
		var mask=move.createLoading();
		mask.show();
		$.ajax({
			type: "POST",
			url:move.jsK3_trend,
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
					var items=data.items;
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var sessionNo=obj.sessionNo;
						var resultItems=obj.openitems;
						
						
						html=html+'<tr>';
						html=html+'<td>'+sessionNo+'</td>';
						var result='';
						
						for(var j=0;j<resultItems.length;j++) {
							var ball=resultItems[j];
							result=result+'<td class="chart-bg-kjhm">'+ball+'</td>';
						}
						html=html+result;
						
						var result2='';
						var subitems=obj.subitems;
						for(var j=0;j<subitems.length;j++) {
							var ball=subitems[j];
							var clazz='ordinary';
							if(ball.indexOf('&') >= 0){
								ball=ball.replace('&','');
								clazz='blueHighlight';
							}
							result2=result2+'<td class="blueball '+clazz+'"><span class="ball">'+ball+'</span></td>';
						}
						html=html+result2;
						
						var he=obj.he;
						var kuadu=obj.kuadu;
						var daxiao=obj.daxiao;
						var jiou=obj.jiou;
						var zhihe=obj.zhihe;
						
						html=html+'<td>'+he+'</td>';
						html=html+'<td>'+kuadu+'</td>';
						html=html+'<td>'+daxiao+'</td>';
						html=html+'<td>'+jiou+'</td>';
						html=html+'<td>'+zhihe+'</td>';
						
					}
					$('#datalist').html(html);
					zoushituObj.switchjsk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjsk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jsk3(type);
		});
	},
	showSession:function(){
		$(".session-name").mouseover(function(){
			$(this).children().show();
			$(this).addClass("session-name-selected");
		});
		$(".session-name").mouseout(function(){
			$(this).children().hide();
			$(this).removeClass("session-name-selected");
		});
	},
}

var activityObj={
	bindswitch:function(){
		$(".promotions .item .cover").bind("click", function(){
//			$('.promotions .item .detail').addClass('hidden');
			$(this).next('img').toggleClass('hidden');
		});
	},
	promotions:function(){
		$.ajax({
			type: "POST",
			url:move.baseData_activitie,
			dataType:'json',
			data:{
			},
			success: function(ret){
				var code=ret.code;
				var data=ret.data;
				var msg=ret.msg;
				var html='';
				if(code=='200'){
					var items=data.items;
					var html='';
					for(var i=0;i<items.length;i++) {
						var obj=items[i];
						var showImg=obj.showImg;
						var hindImg=obj.hindImg;
						html=html+'<div class="item"><a class="cover"><img src="'+showImg+'"></a>';
						html=html+'<img class="detail hidden" src="'+hindImg+'"></div>';
					}
					$('.promotions').html(html);
					activityObj.bindswitch();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
		
	}
}

$(document).ready(function(e) {
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