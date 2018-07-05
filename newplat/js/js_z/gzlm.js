/**
 * 在回复的时候，如果没有登录，若用此ajax登录
 */
function login() {
	var loginDiv = $("#loginDiv").html();
	var loginName = $("#loginName");
	var pwd = $("#pwd");
	$.dialog({
		lock : true,
		fixed : true,
		title : '登录',
		content : loginDiv,
		button : [ {
			value : '登录',
			callback : function() {
				sublogin();
			},
			focus : true
		}, {
			value : '关闭'
		} ]
	});
}
function sublogin() {
	var loginName = $("#loginName");
	var pwd = $("#pwd");
	if ($.trim(loginName.val()).length <= 0) {
		loginName.focus();
		return false;
	}
	if ($.trim(pwd.val()).length <= 0) {
		pwd.focus();
		return false;
	}
	if ($.trim(loginName.val()).length > 0 && $.trim(pwd.val()).length > 0) {
		// this.content('<div class="d-loading"><span>loading..</span></div>');
		$.post("../permission/loginAction.do?method=ajaxLogin&userName="
				+ loginName.val() + "&password=" + pwd.val(), function(data) {
			data = $.trim(data);
			if (data == "true") {
				document.location.reload();// 刷新当前页面
			} else {
				$.dialog({
					title : '提示',
					content : data,
					cancelValue : '关闭',
					cancel : function() {
					}
				});
			}
		});
	}
}
/**
 * 提交评论-succour
 * @param url 要跳回的url
 * @param succId  文章id
 * @param succTit 文章标题
 * @returns {Boolean}
 */
function comment(url,succId,succTit) {
	var commText = $("#commText");
	if ($.trim(commText.val()).length <= 0) {
		$.dialog({
			lock : true,
			fixed : true,
			title : '提示',
			content : '评论的内容不能为空!!',
			cancelValue : '关闭',
			cancel : function() {
			}
		});
	} else {
		$.post("../succour/succourCommAction.do?method=comment", $(
				"#commentForm").serialize(),// 发送表单数据
		function(data) {
//			data = $.trim(data);
			if (data.id!=undefined) {
//				window.location.href=url+succId+"_0.html";
				var id=data.id;
				var userId=data.user.userId;
				var logoMini=data.user.logoMini+"";
				var userName=data.user.userName+"";
				var commContent=data.commContent+"";
				var commDateTime=data.commDateTime+"";
				
				var txt=$("div.cmreplyw div").first().text();
				if(txt=='暂无评论！'){
					$("div.cmreplyw div").first().remove();//删除 暂无评论
				}
				$('div.cmreplyw ul.cmrtp').after('<div class="cmrbox">\
						<div class="cmrlogo"><img src="'+logoMini+'"  class="logo"/></div>\
						<div class="cmrcc">\
							<h5><a href="../otherUserInfo_'+userId+'.html">'+userName+'</a></h5>\
							<div class="cmrccw">\
								'+commContent+'\
							</div>\
							<div class="cmrtime">发表于：'+commDateTime+'</div>\
							<div class="cmrbar">\
								<a href="javascript:void(0);" onclick="delComm(this,'+id+');">删除</a>\
								<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"  data="{\'text\':\'【'+succTit+'】'+commContent+'\'}" style="float: right; margin: 0px 0px 0 10px;">\
									<a class="bds_tsina"></a>\
									<a class="bds_tqq"></a>\
								</div>\
							</div>\
						</div>\
						<div class="c"></div>\
					</div>');
			} else {
				$.dialog({
					title : '提示',
					content : data,
					cancelValue : '关闭',
					cancel : function() {
					}
				});
			}
		}, "json");
	}
	return false;
}
/**
 * 赞一下-succour
 * 
 * @param obj
 * @param commId
 * @param num
 */
function zan(obj, commId, num) {
	$.post(
			"../succour/succourBrowseAction.do?method=clickZan&commId="
					+ commId, function(data) {
				data = $.trim(data);
				if (data == "true") {
					var count = $("#zancount_" + num).text();
					$("#zancount_" + num).text(Number(count) + Number(1));
					obj.disabled = true;
				} else {
					$.dialog({
						title : '提示',
						content : data,
						cancelValue : '关闭',
						cancel : function() {
						}
					});
				}
			});
}

/**
 * 提交评论-news
 * @param url要跳回的url 
 * @param newsId 文章id
 * @param newsTit 文章标题
 * @returns {Boolean}
 */
function comment_news(url,newsId,newsTit) {
	var commText = $("#commText");
	if ($.trim(commText.val()).length <= 0) {
		$.dialog({
			lock : true,
			fixed : true,
			title : '提示',
			content : '评论的内容不能为空!!',
			cancelValue : '关闭',
			cancel : function() {
			}
		});
	} else {
		$.post("../news/newsAction.do?method=comment", $("#commentForm")
				.serialize(),// 发送表单数据
		function(data) {
//			data = $.trim(data);
			if (data.id!=undefined) {
//				window.location.href=url+newsId+"_0.html";
				var id=data.id;
				var userId=data.user.userId;
				var logoMini=data.user.logoMini+"";
				var userName=data.user.userName+"";
				var commContent=data.commContent+"";
				var commDateTime=data.commDateTime+"";
				
				var txt=$("div.cmreplyw div").first().text();
				if(txt=='暂无评论！'){
					$("div.cmreplyw div").first().remove();//删除 暂无评论
				}
//after 表示在此元素后面追加
				$('div.cmreplyw ul.cmrtp').after('<div class="cmrbox">\
						<div class="cmrlogo"><img src="'+logoMini+'"  class="logo"/></div>\
						<div class="cmrcc">\
							<h5><a href="../otherUserInfo_'+userId+'.html">'+userName+'</a></h5>\
							<div class="cmrccw">\
								'+commContent+'\
							</div>\
							<div class="cmrtime">发表于：'+commDateTime+'</div>\
							<div class="cmrbar">\
								<a href="javascript:void(0);" onclick="delNewsComm(this,'+id+');">删除</a>\
								<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"  data="{\'text\':\'【'+newsTit+'】'+commContent+'\'}" style="float: right; margin: 0px 0px 0 10px;">\
									<a class="bds_tsina"></a>\
									<a class="bds_tqq"></a>\
								</div>\
							</div>\
						</div>\
						<div class="c"></div>\
					</div>');
			} else {
				$.dialog({
					title : '提示',
					content : data,
					cancelValue : '关闭',
					cancel : function() {
					}
				});
			}
		}, "json");
	}
	return false;
}
/**
 * 赞一下-news
 * 
 * @param obj
 * @param commId
 * @param num
 */
function zan_news(obj, commId, num) {
	$.post("../home/browseAction.do?method=clickZan&commId=" + commId,
			function(data) {
				data = $.trim(data);
				if (data == "true") {
					var count = $("#zancount_" + num).text();
					$("#zancount_" + num).text(Number(count) + Number(1));
					obj.disabled = true;
				} else {
					$.dialog({
						title : '提示',
						content : data,
						cancelValue : '关闭',
						cancel : function() {
						}
					});
				}
			});
}

/**
 * 视频评论-ajax分页
 */
function getDate(path) {
	$.ajax({
		// 请求的资源,用什么都一样html也可以
		url : path,
		cache : false,
		success : function(html) {
			$("#videoComm").html(html);
			$('.cmrccw').emotionsToHtml();//把评论中的表情转换为图片
		}
	});
}


function Opens(url){
	var tager=$('#target');
	alert(tager);
//	if(){
		
//	}
	$('body').append("<div id='target' style='display: none;'><a id='open' target='_blank' href='"+url+"' > </a></div>")
	document.getElementById("open").click();
	$('#target').html('');
}

/**
 * news类点击次数+1
 * 
 * @param newsId
 */
function newsClick(newsId, url) {
	window.open(url);  
	$.post("../home/browseAction.do?method=newsClick&newsId=" + newsId,
			function(data) {
				data = $.trim(data);
				// if (data == "true") {
//				window.location.href = url;
//				window.open(url);  
				// } else {
				// window.location.href=url;
				// }
			});
}
/**
 * 活动类点击次数+1
 * 
 * @param newsId
 */
function activityClick(actiId, url) {
	window.open(url);  
	$.post("../home/browseAction.do?method=activityClick&actiId=" + actiId,
			function(data) {
				data = $.trim(data);
				// if (data == "true") {
//				window.location.href = url;
//				window.open(url);  
//				Opens(url);
				// } else {
				// window.location.href=url;
				// }
			});
}
/**
 * 救助类点击次数+1
 * 
 * @param newsId
 */
function succourClick(succId, url) {
	window.open(url); 
	$.post("../succour/succourBrowseAction.do?method=succClick&succId=" + succId,
			function(data) {
		data = $.trim(data);
		// if (data == "true") {
//		window.location.href = url;
//		window.open(url);  
//		Opens(url);
		// } else {
		// window.location.href=url;
		// }
	});
}

/**
 * 发布帖子
 */
function postingSubmit() {
	var editor = new UE.ui.Editor();
	editor.render('succour.content');
	var title=$("#title").val();
	if($.trim(title)==""){
		$.dialog({
			title : '提示',
			content : "标题不能为空！",
		});
		$("#title").focus();
		return false;
	}
	
//    if(!editor.hasContents()){
//    	$.dialog({
//			title : '提示',
//			content : "内容不能为空！",
//		});
//		return false;
//    }
	return true;
}



function del(succId){
	if(confirm("您确定要删除这条数据吗？")){
		$.post("../succour/succourCommAction.do?method=delComm&succId=" + succId,
				function(data) {
					data = $.trim(data);
					if (data == "true") {
						document.location.reload();
					} else {
					}
				});
	}
}
/**
 * 删除自己的评论
 * @param obj
 */
function delComm(obj,succId){
	
	if(confirm("您确定要删除这条评论吗？")){
		$.post("../succour/succourCommAction.do?method=delComm&succId=" + succId,
				function(data) {
					data = $.trim(data);
					if (data == "true") {
						$(obj).parent().parent().parent().hide("slow");
					} else {
						document.location.reload();
					}
				});
	}
	
}
/**
 * 删除自己的评论
 * @param obj
 */
function delNewsComm(obj,newsId){
	
	if(confirm("您确定要删除这条评论吗？")){
		$.post("../news/newsAction.do?method=delComm&newsId=" + newsId,
				function(data) {
			data = $.trim(data);
			if (data == "true") {
				$(obj).parent().parent().parent().hide("slow");
			} else {
				document.location.reload();
			}
		});
	}
	
}
/**
 * 删除自己的评论
 * @param obj
 */
function delActiComm(obj,aId){
	if(confirm("您确定要删除这条评论吗？")){
		$.post("../news/activityAction.do?method=delComm&aId=" + aId,
				function(data) {
			data = $.trim(data);
			if (data == "true") {
				$(obj).parent().parent().parent().hide("slow");
			} else {
				document.location.reload();
			}
		});
	}
	
}


/**
 * 提交评论-活动
 * @param url 要跳回的url
 * @param succId  文章id
 * @param succTit 文章标题
 * @returns {Boolean}
 */
function comment_acti(url,succId,succTit) {
	var commText = $("#commText");
	if ($.trim(commText.val()).length <= 0) {
		$.dialog({
			lock : true,
			fixed : true,
			title : '提示',
			content : '评论的内容不能为空!!',
			cancelValue : '关闭',
			cancel : function() {
			}
		});
	} else {
		$.post("../news/activityAction.do?method=comment", $(
				"#commentForm").serialize(),// 发送表单数据
		function(data) {
//			data = $.trim(data);
			if (data.id!=undefined) {
//				window.location.href=url+succId+"_0.html";
				var id=data.id;
				var userId=data.user.userId;
				var logoMini=data.user.logoMini+"";
				var userName=data.user.userName+"";
				var commContent=data.commContent+"";
				var commDateTime=data.commDateTime+"";
				
				var txt=$("div.cmreplyw div").first().text();
				if(txt=='暂无评论！'){
					$("div.cmreplyw div").first().remove();//删除 暂无评论
				}
				$('div.cmreplyw ul.cmrtp').after('<div class="cmrbox">\
						<div class="cmrlogo"><img src="'+logoMini+'"  class="logo"/></div>\
						<div class="cmrcc">\
							<h5><a href="../otherUserInfo_'+userId+'.html">'+userName+'</a></h5>\
							<div class="cmrccw">\
								'+commContent+'\
							</div>\
							<div class="cmrtime">发表于：'+commDateTime+'</div>\
							<div class="cmrbar">\
								<a href="javascript:void(0);" onclick="delComm(this,'+id+');">删除</a>\
								<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare"  data="{\'text\':\'【'+succTit+'】'+commContent+'\'}" style="float: right; margin: 0px 0px 0 10px;">\
									<a class="bds_tsina"></a>\
									<a class="bds_tqq"></a>\
								</div>\
							</div>\
						</div>\
						<div class="c"></div>\
					</div>');
			} else {
				$.dialog({
					title : '提示',
					content : data,
					cancelValue : '关闭',
					cancel : function() {
					}
				});
			}
		}, "json");
	}
	return false;
}
/**
 * 赞一下-活动
 * 
 * @param obj
 * @param commId
 * @param num
 */
function zan_acti(obj, commId, num) {
	$.post(
			"../home/browseAction.do?method=clickActiZan&commId="
					+ commId, function(data) {
				data = $.trim(data);
				if (data == "true") {
					var count = $("#zancount_" + num).text();
					$("#zancount_" + num).text(Number(count) + Number(1));
					obj.disabled = true;
				} else {
					$.dialog({
						title : '提示',
						content : data,
						cancelValue : '关闭',
						cancel : function() {
						}
					});
				}
			});
}