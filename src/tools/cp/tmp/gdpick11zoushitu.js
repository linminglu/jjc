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