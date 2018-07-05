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
	xjssc:function(type){
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
			url:move.xjSsc_trendWeb,
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
					zoushituObj.switchxjssc();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchxjssc:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.xjssc(type);
		});
	},
	tjssc:function(type){
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
			url:move.tjSsc_trendWeb,
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
					zoushituObj.switchtjssc();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchtjssc:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.tjssc(type);
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
	bjpick11:function(type){
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
			url:move.bjPick11_trend,
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
					zoushituObj.switchbjpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchbjpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.bjpick11(type);
		});
	},

tjpick11:function(type){
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
			url:move.tjPick11_trend,
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
					zoushituObj.switchtjpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchtjpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.tjpick11(type);
		});
	},

hebpick11:function(type){
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
			url:move.hebPick11_trend,
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
					zoushituObj.switchhebpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhebpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hebpick11(type);
		});
	},

nmgpick11:function(type){
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
			url:move.nmgPick11_trend,
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
					zoushituObj.switchnmgpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchnmgpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.nmgpick11(type);
		});
	},

lnpick11:function(type){
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
			url:move.lnPick11_trend,
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
					zoushituObj.switchlnpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchlnpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.lnpick11(type);
		});
	},

jlpick11:function(type){
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
			url:move.jlPick11_trend,
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
					zoushituObj.switchjlpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjlpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jlpick11(type);
		});
	},

hljpick11:function(type){
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
			url:move.hljPick11_trend,
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
					zoushituObj.switchhljpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhljpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hljpick11(type);
		});
	},

shpick11:function(type){
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
			url:move.shPick11_trend,
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
					zoushituObj.switchshpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchshpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.shpick11(type);
		});
	},

jspick11:function(type){
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
			url:move.jsPick11_trend,
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
					zoushituObj.switchjspick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjspick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jspick11(type);
		});
	},

zjpick11:function(type){
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
			url:move.zjPick11_trend,
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
					zoushituObj.switchzjpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchzjpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.zjpick11(type);
		});
	},

ahpick11:function(type){
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
			url:move.ahPick11_trend,
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
					zoushituObj.switchahpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchahpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.ahpick11(type);
		});
	},

fjpick11:function(type){
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
			url:move.fjPick11_trend,
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
					zoushituObj.switchfjpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchfjpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.fjpick11(type);
		});
	},

jxpick11:function(type){
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
			url:move.jxPick11_trend,
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
					zoushituObj.switchjxpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjxpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jxpick11(type);
		});
	},

hnpick11:function(type){
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
			url:move.hnPick11_trend,
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
					zoushituObj.switchhnpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhnpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hnpick11(type);
		});
	},

hubpick11:function(type){
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
			url:move.hubPick11_trend,
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
					zoushituObj.switchhubpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhubpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hubpick11(type);
		});
	},

gxpick11:function(type){
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
			url:move.gxPick11_trend,
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
					zoushituObj.switchgxpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgxpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gxpick11(type);
		});
	},

gzpick11:function(type){
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
			url:move.gzPick11_trend,
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
					zoushituObj.switchgzpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgzpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gzpick11(type);
		});
	},

shxpick11:function(type){
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
			url:move.shxPick11_trend,
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
					zoushituObj.switchshxpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchshxpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.shxpick11(type);
		});
	},

gspick11:function(type){
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
			url:move.gsPick11_trend,
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
					zoushituObj.switchgspick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgspick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gspick11(type);
		});
	},

xjpick11:function(type){
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
			url:move.xjPick11_trend,
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
					zoushituObj.switchxjpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchxjpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.xjpick11(type);
		});
	},

sdpick11:function(type){
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
			url:move.sdPick11_trend,
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
					zoushituObj.switchsdpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchsdpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.sdpick11(type);
		});
	},

sxpick11:function(type){
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
			url:move.sxPick11_trend,
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
					zoushituObj.switchsxpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchsxpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.sxpick11(type);
		});
	},

ynpick11:function(type){
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
			url:move.ynPick11_trend,
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
					zoushituObj.switchynpick11();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchynpick11:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.ynpick11(type);
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
	ahk3:function(type){
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
			url:move.ahK3_trend,
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
					zoushituObj.switchahk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchahk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.ahk3(type);
		});
	},
	bjk3:function(type){
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
			url:move.bjK3_trend,
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
					zoushituObj.switchbjk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchbjk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.bjk3(type);
		});
	},
	fjk3:function(type){
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
			url:move.fjK3_trend,
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
					zoushituObj.switchfjk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchfjk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.fjk3(type);
		});
	},
	gzk3:function(type){
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
			url:move.gzK3_trend,
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
					zoushituObj.switchgzk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgzk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gzk3(type);
		});
	},
	gxk3:function(type){
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
			url:move.gxK3_trend,
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
					zoushituObj.switchgxk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgxk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gxk3(type);
		});
	},
	gsk3:function(type){
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
			url:move.gsK3_trend,
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
					zoushituObj.switchgsk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchgsk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.gsk3(type);
		});
	},
	hubk3:function(type){
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
			url:move.hubK3_trend,
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
					zoushituObj.switchhubk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhubk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hubk3(type);
		});
	},
	hebk3:function(type){
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
			url:move.hebK3_trend,
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
					zoushituObj.switchhebk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhebk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hebk3(type);
		});
	},
	hnk3:function(type){
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
			url:move.hnK3_trend,
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
					zoushituObj.switchhnk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchhnk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.hnk3(type);
		});
	},
	jxk3:function(type){
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
			url:move.jxK3_trend,
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
					zoushituObj.switchjxk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjxk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jxk3(type);
		});
	},
	jlk3:function(type){
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
			url:move.jlK3_trend,
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
					zoushituObj.switchjlk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchjlk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.jlk3(type);
		});
	},
	nmgk3:function(type){
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
			url:move.nmgK3_trend,
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
					zoushituObj.switchnmgk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchnmgk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.nmgk3(type);
		});
	},
	shk3:function(type){
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
			url:move.shK3_trend,
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
					zoushituObj.switchshk3();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchshk3:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.shk3(type);
		});
	},
	bjssc:function(type){
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
			url:move.bjSsc_trendWeb,
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
					zoushituObj.switchbjssc();
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				mask.close();
			}
		});
	},
	switchbjssc:function(){
		$(".select .option").bind("click", function(){
			var type=$(this).attr('data');
			zoushituObj.bjssc(type);
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
			$(this).siblings().next('div').toggleClass('hidden');
			$(this).next('div').toggleClass('hidden');
		});
	},
	promotions:function(){
		$.ajax({
			type: "POST",
			url:move.promotionList,
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
						var title=obj.title;
						var startDate=obj.startDate;
						var img=obj.img;
						var content=obj.content;
						html=html+'<div class="item"><a class="cover">';
						html=html+'<img src="'+img+'"><p class="new_title">'+title+'</p>';
						html=html+'<p class="new_date">起始时间：'+startDate+'</p></a>';
						html=html+'<div class="detail hidden">'+content+'</div></div>';
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
