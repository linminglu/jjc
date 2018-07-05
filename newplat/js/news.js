var lotteryObj={
	defPageSize:20,
	init:function(){
		$(".pager .first").bind("click", function(){
			var type=$(this).attr('type');
			move.pageIndex=0;
			if(type=='ssq'){
				lotteryObj.list(1);
			}else if(type=='sanfencai'){
				lotteryObj.list(2);
			}else if(type=='wufencai'){
				lotteryObj.list(3);
			}else if(type=='cqssc'){
				lotteryObj.list(4);
			}else if(type=='gd115'){
				lotteryObj.list(5);
			}
		});
		$(".pager .prev").bind("click", function(){
			var type=$(this).attr('type');
			if(move.pageIndex>0){
				move.pageIndex=move.pageIndex-1;
				if(type=='ssq'){
					lotteryObj.list(1);
				}else if(type=='sanfencai'){
					lotteryObj.list(2);
				}else if(type=='wufencai'){
					lotteryObj.list(3);
				}else if(type=='cqssc'){
					lotteryObj.list(4);
				}else if(type=='gd115'){
					lotteryObj.list(5);
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
					lotteryObj.list(1);
				}else if(type=='sanfencai'){
					lotteryObj.list(2);
				}else if(type=='wufencai'){
					lotteryObj.list(3);
				}else if(type=='cqssc'){
					lotteryObj.list(4);
				}else if(type=='gd115'){
					lotteryObj.list(5);
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
					lotteryObj.list(1);
				}else if(type=='sanfencai'){
					lotteryObj.list(2);
				}else if(type=='wufencai'){
					lotteryObj.list(3);
				}else if(type=='cqssc'){
					lotteryObj.list(4);
				}else if(type=='gd115'){
					lotteryObj.list(5);
				}
			}
		});
	},
	list:function(tid){
		if(!tid){
			tid='';
		}
		var map = {};
		map['pageIndex'] =move.pageIndex;
		map['pageSize'] =this.defPageSize;
		map['tid'] =tid;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.newslist,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					var data=ret.data;
					var items=data.items;
					var html='';
					for (var i = 0; i < items.length; i++) {
						var obj=items[i];
						var newsId=obj.newsId;
						var tag=obj.tag;
						var title=obj.title;
						var time=obj.time;
						var tid=obj.tid;
						var path='';
						if(tid==1){//双色球
							path='/news/ssq/'+newsId;
						}else if(tid==2){
							path='/news/sanfencai/'+newsId;
						}else if(tid==3){
							path='/news/wufencai/'+newsId;
						}else if(tid==4){
							path='/news/cqssc/'+newsId;
						}else if(tid==5){
							path='/news/gd115/'+newsId;
						}else{
							path='/news/notice/'+newsId;
						}
						if(!tag){
							tag=''
						}
						
						html=html+'<li><span class="tag">'+tag+'</span>';
						html=html+'<a href="'+path+'">'+title+'</a>';
						html=html+'<span class="date">'+time+'</span></li>';
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
					if(move.pageIndex==0){
						$('.pager').show();
					}
				}else{
					if(move.pageIndex==0){
						$('.pager').hide();
					}
				}
				$('#newslist').html(html);
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	},
	view:function(){
		var url=window.location;
		if(url){
			url=url+"";
			var id=url.substring(url.lastIndexOf('/')+1,url.length);
			var map = {};
			map['id'] =id;
			var mw=baseObj.mw(map);
			$.ajax({
				type: "POST",
				url:move.newsView,
				dataType:'json',
				data:{
					mw:mw
				},
				success: function(ret){
					var code=ret.code;
					if(code=='200'){
						var data=ret.data;
						var obj=data.obj;
						var title=obj.title;
						var author=obj.author;
						var dateTime=obj.dateTime;
						var content=obj.content;
						$('#title').text(title);
						$('#editor').html(author+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+dateTime);
						$('#content').html(content);
					}else{
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				}
			});
		}
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