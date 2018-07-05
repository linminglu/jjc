var lotteryObj={
	defPageSize:10,
	init:function(){
		$(".pager .first").bind("click", function(){
			var type=$(this).attr('type');
			move.pageIndex=0;
			if(type=='ssq'){
				lotteryObj.ssq();
			}else if(type=='sanfencai'){
				lotteryObj.sanfencai();
			}else if(type=='wufencai'){
				lotteryObj.wufencai();
			}else if(type=='cqssc'){
				lotteryObj.cqssc();
			}else if(type=='gd115'){
				lotteryObj.gd115();
			}else if(type=='jsk3'){
				lotteryObj.jsk3();
			}
		});
		$(".pager .prev").bind("click", function(){
			var type=$(this).attr('type');
			if(move.pageIndex>0){
				move.pageIndex=move.pageIndex-1;
				if(type=='ssq'){
					lotteryObj.ssq();
				}else if(type=='sanfencai'){
					lotteryObj.sanfencai();
				}else if(type=='wufencai'){
					lotteryObj.wufencai();
				}else if(type=='cqssc'){
					lotteryObj.cqssc();
				}else if(type=='gd115'){
					lotteryObj.gd115();
				}else if(type=='jsk3'){
					lotteryObj.jsk3();
				}
			}
		});
		$(".pager .next").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=move.pageIndex+1;
				if(type=='ssq'){
					lotteryObj.ssq();
				}else if(type=='sanfencai'){
					lotteryObj.sanfencai();
				}else if(type=='wufencai'){
					lotteryObj.wufencai();
				}else if(type=='cqssc'){
					lotteryObj.cqssc();
				}else if(type=='gd115'){
					lotteryObj.gd115();
				}else if(type=='jsk3'){
					lotteryObj.jsk3();
				}
			}
		});
		$(".pager .last").bind("click", function(){
			var type=$(this).attr('type');
			var toatlpage=$('#toatlpage').text();
			toatlpage=parseInt(toatlpage);
			if((move.pageIndex+1)<toatlpage){
				move.pageIndex=(toatlpage-1);
				if(type=='ssq'){
					lotteryObj.ssq();
				}else if(type=='sanfencai'){
					lotteryObj.sanfencai();
				}else if(type=='wufencai'){
					lotteryObj.wufencai();
				}else if(type=='cqssc'){
					lotteryObj.cqssc();
				}else if(type=='gd115'){
					lotteryObj.gd115();
				}else if(type=='jsk3'){
					lotteryObj.jsk3();
				}
			}
		});
	},
	kaijiangList:function(){
		var map = {};
		var mw=baseObj.mw(map);
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
					var items=result.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var explain=obj.explain;
						var openResult=obj.openResult;
						var openSessionNo=obj.openSessionNo;
						var gameName=obj.gameName;
						var type=obj.gameType;
						var openTime=obj.openTime;
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
						
						html=html+'<tr><td class="kinds-logo"><img src="'+img+'"></td> <td><a href="'+kaijiangUrl+'">'+gameName+'</a></td> ';
						html=html+'<td>'+openSessionNo+'期</td><td>'+openTime+'</td><td class="balls"><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							if(type=='13'){
								if(j==6){//蓝球
									className='bg-blue';
								}
							}
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml;
						html=html+'</div></td><td>'+explain+'</td> ';
						html=html+'<td><a href="'+zoushiUrl+'"><img src="/images/static/graph.gif"></a></td>  ';
						// html=html+'<td><a href="'+touzhuUrl+'">代购</a> <a href="'+touzhuUrl+'">合买</a></td></tr>';
					}
					$('#kindsopenlist').html(html);
				}else{
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	ssq:function(){
		console.log(move.pageIndex);
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.ssq_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							if(j==6){//蓝球
								className='bg-blue';
							}
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	sanfencai:function(){
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.three_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	wufencai:function(){
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.five_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	cqssc:function(){
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.cqSsc_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	gd115:function(){
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.gd115_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	jsk3:function(){
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.jsK3_openList,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var html='';
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var img=obj.img;
						var openResult=obj.resultItems;
						var sessionNo=obj.sessionNo;
						var openTime=obj.openTime;
						
						html=html+'<tr><td>第 '+sessionNo+' 期</td> ';
						html=html+'<td>'+openTime+'</td><td><div class="open-number"> ';
						var numhtml='';
						for ( var j = 0; j < openResult.length; j++) {
							var num=openResult[j];
							var className='bg-red';
							numhtml=numhtml+'<span class="num '+className+'">'+num+'</span>';
						}
						html=html+numhtml+'</div></td></tr>';
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
					$('#nowpage').text(nowpage);
					$('#toatlpage').text(totalPage);
					$('#totalnum').text(total);
					$('#pageSize').text(pageSize);
				}else{
				}
				$('#kindsopenlist').html(html)
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