var move = {};
//分页的页码
move.pageIndex=0;
//缓存存储时间，毫秒
move.storageTime=2592000000;

move.local = location.toString().indexOf("/")>-1?0:1;
var pro='';
if(window.location.protocol){
	pro=window.location.protocol+"//";
}
move.server=pro+window.location.host+(move.local?"":"");
move.webpath=move.local?"":"";

move.api={
	advert:move.server+'/api/baseData_advert',
	notice:move.server+'/api/baseData_notice',
	column:move.server+'/api/jc_type',
	recommend:move.server+'/api/jc_recommend',
	match:move.server+'/api/jc_match',
	gameList:move.server+'/api/jc_matchType',
	changePwd:move.server+'/api/user_changePwd',
	reg:move.server+'/api/user_reg',
	login:move.server+'/api/user_login',
	logout:move.server+'/api/user_logout',
	checkCode:move.server+'/api/baseData_checkCode',
	tradeList:move.server+'/api/cash_tradeList',
	field:move.server+'/api/jc_field', // 比赛详情
	bet:move.server+'/api/jc_bet',
	jcRecord:move.server+'/api/user_jcRecord',
	money:move.server+'/api/baseData_money',
	promoList:move.server+'/api/lottery_promotionList',
	promoView:move.server+'/api/lottery_promotionDetail'
}

move.refresh=false;
/** 刷新页面 分页的时候用到 **/
move.refreshPage=function(){
	if(move.refresh){
		location.reload();
	}
	if(!move.refresh){
		move.refresh=true;
	}
}

/**平面宽度*/
move.width=$(window).width();
var desKey="P29lMhJ8";
/**
 * 获得get请求的URL参数
 * @param name 参数名
 * @returns
 */
move.getParameter=function(name) {
//	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
//	var r = window.location.search.substr(1).match(reg); 
//	if (r != null) return decodeURIComponent(r[2]); return null; 
	
	var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
	var url = decodeURIComponent(decodeURIComponent(window.location.href));
	if(reg.test(url)){
		return unescape(RegExp.$2.replace(/\+/g, " "));
	}
	return "";
}

function getvl(name) {
	var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
	if(reg.test(location.href))
	return unescape(RegExp.$2.replace(/\+/g, " "));
	return "";
}


move.createLoading=function() {
	var mask = {};
	mask.show=function(){
		var loading=$(".mask").html();
		if(loading){
			$(".mask").show();
			$(".masktrans").css("display","none");
		}else{
			var width=$(window).width();//宽度
			var widthLoading=width*0.4;//loading宽度
			var height=$(window).height();//高度
			$('body').append('<div class="masktrans"></div>'+'<div class="mask"><div class="loading"><div class=\"loading-spinner\"><div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div></div></div></div></div>');
			$(".masktrans").css("display","none");
			if(widthLoading>85){
				widthLoading=85;
			}
			var top=(height/2)-(widthLoading);
			var left=(width/2)-(widthLoading/2);
			$('.loading').css({width:widthLoading,height:widthLoading,marginTop:top,marginLeft:left});
		}
	}
	mask.close=function(){
		var loading=$(".mask").html();
		if(loading){
			$(".mask").fadeOut();
			$(".masktrans").fadeOut();
		}
	}
	return mask;
}
move.closeLoading = function(){
	var loading=$(".mask").html();
	if(loading){
		$(".mask").fadeOut();
		$(".masktrans").fadeOut();
	}
}
/**
 * 获得设备
 * @return 'ios'\'android'\'other'
 */
move.getDevice=function(){
	if(/(bxsios|bxsandroid)/i.test(navigator.userAgent)){
		return 'app';
	}else
	if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
	  return 'ios';
	} else if (/(Android)/i.test(navigator.userAgent)) {
	   return 'android';
	}else{
		return 'other'
	}
}

move.isWechat=function(){  
    var ua = navigator.userAgent.toLowerCase();  
    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
        return true;  
    } else {  
        return false;  
    }  
} 

move.browser=function(){
    var userAgent = navigator.userAgent;
    var isOpera = userAgent.indexOf("Opera") > -1;
    if (isOpera) {
        return "Opera"
    }
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    }
    if (userAgent.indexOf("Chrome") > -1){
    	return "Chrome";
    }
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    }
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
        return "IE";
    }
}


var baseObj={
	openIndex:function(){
		mui.openWindow({
			url: location.hash="#pageIndex", 
			show:{
				autoShow:false
			}
		});
	},
	openMe:function(){
		mui.openWindow({
			url: location.hash="#pageMe", 
			show:{
				autoShow:false
			}
		});
	},
	openLogin:function(){
		mui.openWindow({
			url: location.hash="#login",
			show:{
				autoShow:false
			}
		});
	},
	openView:function(url){
		mui.openWindow({
			url: url, 
			show:{
				autoShow:false
			}
		});
	},
	mw:function(map){
		//遍历map
		var mw="";
		for(var prop in map){
		    if(map.hasOwnProperty(prop)){
		    	var key=prop;
		    	var val=encodeURI(map[prop]);
		    	if(map[prop]==null||map[prop]=='null'){
		    		val="";
		    	}
		    	mw=mw+key+"="+val+"&";
		    }
		}
		if(mw.length>1){
			mw=mw.substring(0,mw.length-1);
		}
		if(mw!=''){
			mw=encryptByDES(mw);
			//第二次整体编码
			mw=encodeURI(mw);
			
		}
		return mw;
	}
}

function encryptByDES(message) {
    var keyHex = CryptoJS.enc.Utf8.parse(desKey);
    var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}

/**获得登录用户信息*/
move.user={
	uid:null,
	u:null,
	logo:null,
	longAlt:null,
	phone:null,
	loginName:null,
	username:null,
	exp:null,//过期时间
	money:0,//余额
	time:null,//存入时间
	points:0//积分
}
var user = {
	init:function(uid,u,longAlt,logo,phone,loginName,username,money){
		move.user.uid=(uid!=null)?uid:null;
		move.user.u=(u!=null)?u:null;
		move.user.longAlt=(longAlt!=null)?longAlt:null;
		move.user.logo=(logo!=null)?logo:null;
		move.user.phone=(phone!=null)?phone:null;
		move.user.loginName=(loginName!=null)?loginName:null;
		move.user.username=(username!=null)?username:null;
		move.user.money=(money!=null)?money:null;
		move.user.exp=move.storageTime;
		move.user.time=new Date().getTime();
	},
	get:function(){
		if(typeof store =='object'){
			var obj=store.get('user');
			if(obj!=null){
				if (new Date().getTime() - obj.time > obj.exp) {//过期
					store.remove('user'); 
					return null;
				}else{
					move.user=obj;
					return move.user;
				}
			}else{//用户未登录
				return null;
			}
		}else{
			console.log('没有引用store.min.js');
			return null;
		}
	},
	set:function(obj){
		store.set('user', obj);
	},
	clear:function(){
		store.remove('user');
	}
};
if(move.user.u==null){
	var obj=user.get();
	if(obj!=null){
		move.user=user.get();
	}
}
move.hideHeader=function(){
	$('.main-content').css("min-height",$(window).height());
	var header=$('header.mui-bar');
//	if(header){
//		if(!move.isWechat()){//微信
			$(header).before('<div class="margin-top-header"></div>');
			$('.bxs-pages').css('margin-top','44px');
//		}else{
//			$(header).hide();
//			$('div.margin-top-header').hide();
//		}
//		var plat=move.getParameter('plat');
//		if(plat=='1'||plat=='2'){//1.ios 2.安卓
//			$(header).hide();
//			$('div.margin-top-header').hide();
//		}
//	}
}

move.hash={
	getHash:function(isQuery){
		var hashStr = location.hash;
		if(hashStr){
			if(hashStr.indexOf("&")>-1){//has param
				var hashArr = hashStr.split("&");
				var paramStr = "";
				for(var i=1;i<hashArr.length;i++){
					if(isQuery && hashArr[i].indexOf("page=")>-1){//filter page
						
					}else{
						paramStr += "&"+hashArr[i];
					}
				}
				return {hash:hashArr[0],param:paramStr};
			}else{
				return {hash:hashStr};
			}
		}
		return "";
	},
	getHashMethod:function(){
		var hashObj = this.getHash();
		if(hashObj){
			return hashObj.hash.replace("#","");
		}
		return "";
	},
	hashReq:function(){
		//监听hash变化
		var hashObj =this.getHash();//当前地址中的hash
		if(hashObj){
//			调用相对应的方法
			eval("m."+hashObj.hash.replace("#","")+"('"+(hashObj.param?hashObj.param:"")+"')");
//			m.pageInit(hashObj.param?hashObj.param:"");
		}else{
			location = "#pageIndex";
		}
	}
}

var req={
	post:function(url,map,callback,loading){//url:接口地址,map:参数,不用加密,callback:回调函数,loading:是否显示loading
		if(loading){
			var mark=move.createLoading();
			mark.show();
		}
		$.ajax({
		    type: "POST",
		    url:url,
		    dataType:'json',
		    data:{
		    	mw:map?baseObj.mw(map):""
		    },
		    success: function(res){
		    	move.closeLoading();
		    	if(res){
		    		if(!callback){
		    			alert('无效回调函数');
		    			return;
		    		}
		    		callback.call(!1,res);
		    	}
		    },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	mui.toast('网络错误',{ duration:'long', type:'div' }); 
	        }
		});
	}	
	
}


/**主要布局对象**/
var m={
	html:'',
	pageInit:function(){
		m.html='<div class="main-content"></div>';
		$("body").html(m.html);
	},
	header:function(isback,title,backUrl){
		m.html='<header class="mui-bar mui-bar-nav ">';
		if(backUrl&&backUrl!=''){
			m.html=m.html+'<a class="mui-icon mui-icon-left-nav mui-pull-left" href="'+backUrl+'"></a>';
		}else if(isback){
			m.html=m.html+'<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>';
		}
		m.html=m.html+'<h1 class="mui-title">'+title+'</h1>';
		m.html=m.html+'</header>';
		$('.main-content').prepend(m.html);
		move.hideHeader();
	},
	back:function(){
		m.html='<a class="mui-icon mui-icon-left-nav mui-pull-left backdu" id="backdu" href="javascript:history.back(-1);"></a>';
		$('.main-content').prepend(m.html);
	},
	tabbar:function(index){
		m.html='<div class="nav-box "><nav class="mui-bar mui-bar-tab" id="mui-bar">';
		var index1Class='';
		if(index=='1'){
			index1Class='mui-active';
		}
		m.html=m.html+'<a class="mui-bar-item '+index1Class+'" href="#pageIndex">';
		m.html=m.html+'<span class="mui-icon mui-img-home"></span><span class="mui-tab-label">首页</span></a>';
		var index2Class='';
		if(index=='2'){
			index2Class='mui-active';
		}
		m.html=m.html+'<a class="mui-bar-item '+index2Class+'" href="#pageZixun">';
		m.html=m.html+'<span class="mui-icon mui-img-zixun"></span><span class="mui-tab-label">资讯</span></a>';
		
		var index3Class='';
		if(index=='3'){
			index3Class='mui-active';
		}
		m.html=m.html+'<a class="mui-bar-item '+index3Class+'" href="#pageMe">';
		m.html=m.html+'<span class="mui-icon mui-img-me"></span><span class="mui-tab-label">我的</span></a>';
		
		$('.main-content').append(m.html);
	},
	height50:function(){
		$('.main-content').append('<div class="height50"></div>');
	},
	pageIndex:function(){
		//首页布局
		this.pageInit();
		this.header(false, "首页", null);
		this.tabbar(1);
		indexObj.pageIndexSlider();
		indexObj.column();
		indexObj.notice();
		indexObj.tuiJian();
		this.height50();
	},
	gameView:function(){
		this.pageInit();
		this.back();
		ga.gamePage();
	},
	gamelist:function(items,box){
		//生成游戏布局
		m.html='<ul>';
		for ( var i = 0; i < items.length; i++) {
			var obj=items[i];
			var id=obj.mId;
			var title=obj.title;
			var red=obj.red;// 红队
			var matchTime=obj.matchTime;
			var team1Name=obj.team1Name;
			var team1Img=obj.team1Img;
			var team2Name=obj.team2Name;
			var team2Img=obj.team2Img;
			var boNum=obj.boNum;
			
			var redName="";
			var redImg="";
			var blueName="";
			var blueImg="";
			if(team1Name==red){
				redName=team1Name;
				redImg=team1Img;
				blueName=team2Name;
				blueImg=team2Img;
			}else{
				redName=team2Name;
				redImg=team2Img;
				blueName=team1Name;
				blueImg=team1Img;
			}
			
			m.html=m.html+'<li class="gone" data="'+id+'">';
			m.html=m.html+'<div class="title">'+title+'</div>';
			m.html=m.html+'<div class="team team1"><div class="imgbox"><img src="'+redImg+'"/></div><span class="tiamname">'+redName+'<span class="field red">(主)</span></span></div>';
			m.html=m.html+'<div class="tiamvs"><span class="linele"></span>'+boNum+'<span class="liner"></span></div><div class="match_time">'+matchTime+'</div>';
			m.html=m.html+'<div class="team team2"><div class="imgbox"><img src="'+blueImg+'"/></div><span class="tiamname">'+blueName+'<span class="field yellow">(客)</span></span></div>';
			
			m.html=m.html+'</li>';
		}
		m.html=m.html+'</ul>';
		$(box).append(m.html);
		
		//绑定点击事件
		$("li.gone").bind("click", function(){
			var id=$(this).attr('data');
			location.hash='#gameView&id='+id;
		});
	},
	gameColumn:function(){
		//游戏栏目
		this.pageInit();
		var title=move.getParameter("title");
		this.header(true, title, null);
		ga.columnPage();
	},
	pageMe:function(){
		//我的页面布局
		this.pageInit();
		this.header(false, "我的", null)
		this.tabbar(3);
		me.home();
		this.height50();
	},
	pageZixun:function(){
		//资讯布局
		this.pageInit();
		this.header(false, "资讯", null)
		this.tabbar(2);
		promotion.promoList();
		this.height50();
	},
	jcRecord:function(){
		//竞猜记录
		this.pageInit();
		this.header(true, "竞猜记录", null)
		me.jcRecord();
	},
	tradeRecord:function(){
		//交易记录
		this.pageInit();
		this.header(true, "交易记录", null)
		me.tradeRecord();
	},
	levelRecord:function(){
		//下级报表
		this.pageInit();
		this.header(true, "下级报表", null)
		me.levelRecord();
	},
	tuiGuangLink:function(){
		//推广链接
		this.pageInit();
		this.header(true, "推广链接", null)
		me.tuiGuangLink();
	},
	setting:function(){
		//设置
		this.pageInit();
		this.header(true, "设置", null)
		me.setting();
	},
	changePwd:function(){
		//修改登录密码
		this.pageInit();
		this.header(true, "修改登录密码", null)
		me.changePwd();
	},
	login:function(){
		//登录
		this.pageInit();
		this.header(true, "登录", null)
		me.login();
	},
	register:function(){
		//注册
		this.pageInit();
		this.header(true, "注册", null)
		me.register();
	},
	notData:function(){
		m.html='<div class="not-data"><img src="images/empty.png"><p class="not-data-lable">没有找到数据</p></div>';
		$('.main-content').append(m.html);
	},
	promoView:function(ret){
		this.pageInit();
		this.header(true, "资讯详情", null)
		promotion.promoPage();
	}
}

/**index.js**/
var indexObj={
	pageIndexSlider:function(){
		m.html='<div id="indexslider" class="mui-slider" >';
		m.html=m.html+'<img src="/jc/images/def_img_bg21.png"/>';
		
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		
		var map = {};
		map['adType'] =1;
		req.post(move.api.advert, map, indexObj.pageIndexSliderBuild, false)
	},
	pageIndexSliderBuild:function(ret){
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
					imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate">'+
					'<a href="'+url+'"> <img src="'+img+'"></a></div>';
				}
				var length=totalLength;
//					if(totalLength>3){
//						length=3;
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
//					}
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
						var htmlcode='<div class="mui-slider-item">'+
						'<a href="'+url+'"><img src="'+img+'"></a></div>';
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
					imgbox=imgbox+'<div class="mui-slider-item mui-slider-item-duplicate">'+
					'<a href="'+url+'"><img src="'+img+'"></a></div>';
				}
				imgbox=imgbox+'</div>';
				roundbox=roundbox+'</div>';
				html=imgbox+roundbox;
			}
			jQuery("#indexslider").html(html);
			if(move.width>540){
				move.width=540;
			}
			var imgHight=move.width/2;
			$("#indexslider img").css("height",""+imgHight+"px");
			var slider = mui("#indexslider");
			slider.slider({
				interval: 2000
			});
		}else{
			var html="<div class=''><img src='img/def.jpg'/></div>";
			jQuery("#indexslider").html(html);
		}
	},
	column:function(){
		m.html='<div class=" menu-box" id="column">';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		var map = {};
		map['type'] =1;
		req.post(move.api.column, map, indexObj.columnBuild, false)
	},
	columnBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code==200){
			var html='';
			var items=data.items;
			m.html='';
			m.html=m.html+'<ul class="mui-table-view mui-grid-view mui-grid-9">';
			for ( var i = 0; i < items.length; i++) {
				var obj=items[i];
				var id=obj.tid;
				var title=obj.title;
				var img=obj.img;
				m.html=m.html+'<li class="mui-table-view-cell mui-media gamecolumn" data="'+id+'" title="'+title+'"><span class="mui-icon"><img src="'+img+'"></span> <div class="mui-media-body">'+title+'</div> </li>';
				
			}
			m.html=m.html+'</ul>';
			$('#column').append(m.html);
			//绑定点击事件
			$("li.gamecolumn").bind("click", function(){
				var id=$(this).attr('data');
				var title=$(this).attr('title');
				location.hash='#gameColumn&id='+id+'&title='+title;
			});
		}
	},
	notice:function(){
		m.html='<div class="topline-box " id="topline">';
		m.html=m.html+'<div class="smarticker1 smarticktemp smarticker no-category no-subcategory theme1 box size1 cundefined shadow">';
		m.html=m.html+'<div class="sec1-2 tickertitle"></div>';
		m.html=m.html+'<div class="smarticker-news newsholder hidden sec10" style="display: block; height: 100%;">';
		m.html=m.html+'<ul id="newsholder" class="newsholder hidden" style="display: block; height: 100%;">';
		
		m.html=m.html+'</ul>';
		m.html=m.html+'</div>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		//写入通知信息
		req.post(move.api.notice, null, indexObj.noticeBuild, false);
	},
	noticeBuild:function(ret){
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
	tuiJian:function(){
		//首页推荐布局
		m.html='<div class="tuijianbox">热门推荐</div>';
		$('.main-content').append(m.html);
		//
		m.html='<div class="gamelist mui-active" id="gamelist"></div>';
		$('.main-content').append(m.html);
		req.post(move.api.recommend, null, indexObj.tuiJianBuild, false);
	},
	tuiJianBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			m.gamelist(items, $('#gamelist'));
		}
	}
}
/**index.js-end**/

/**game.js**/
var ga={
	optionId:null,//投注id
	rate:null,//赔率
	betMoney:0,//投注金额
	gtitle:'',//比赛title
	team1:'',//团队1
	team2:'',//团队2
	optionTitle:'',//投注项名称
	playTitle:'',//玩法名称
	betMoneyArr:[10,500,2000,5000],//快捷投注值
	minBetMoney:10,//最小投注金额
	maxBetMoney:100000,//最大投注金额
	loopGamePage:function(){
		var id=move.getParameter('id');
		var map = {};
		map['mId'] =id;
		map['u'] =move.user.u;
		req.post(move.api.field, map, ga.loopGamePageBuild, false);
	},
	loopGamePageBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code=='200'){
			var obj=data.obj;
			if(obj){
				var items=obj.items;
				var money=obj.money;
				if(move.user.u!=null){
					move.user.money=money;
				}
				if(items.length>0){
					for(var i=0;i<items.length;i++){
						var item=items[i];
						var fId=item.fId;
						var status=item.status;// 局状态0.不可以投注1.可以投注
						var pitems=item.pitems;
						if(pitems.length>0){
							for(var j=0;j<pitems.length;j++){
								var pitem=pitems[j];
								var pTId=pitem.pTId;// 玩法id
								var openStatus=pitem.openStatus;// 开奖状态0=未开将 1=已开奖
								var isBet="1";
								if(status==0||openStatus==1){
									isBet="0";
								}
								var rateStatus=$("#option"+pTId+" .rate").attr("status");
								if(isBet!=rateStatus){
									if(isBet==0){
										$("#option"+pTId).prepend("<img src=\"images/over.png\">");
										$("#option"+pTId+" .betbox").addClass("colour-gray");
										$("#option"+pTId+" .rate").unbind("click");
										$("#option"+pTId+" .rate").attr("status", isBet);
									}
								}
							}
						}
					}
				}
			}
		}
	},
	gamePage:function(){
		var id=move.getParameter('id');
		var map = {};
		map['mId'] =id;
		map['u'] =move.user.u;
		req.post(move.api.field, map, ga.gamePageBuild, true);
	},
	gamePageBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code=='200'){
			setInterval(function(){
				ga.loopGamePage();
			},5000);
			var obj=data.obj;
			if(obj){
				var money=obj.money;// 用户余额
				var title=obj.title;// 赛事标题
				var subTitle=obj.subTitle;// 副标题
				var matchTime=obj.matchTime;// 比赛时间
				var boNum=obj.boNum;// 场次
				var team1Name=obj.team1Name;// 队伍1名称
				var team1Img=obj.team1Img;// 队伍1图片
				var team2Name=obj.team2Name;// 队伍2名称
				var team2Img=obj.team2Img;// 队伍2图片
				
				if(move.user.u!=null){
					move.user.money=money;
				}
				
				m.html='<div class="gone miange">';
				m.html+='<div class="gobox"><div class="date">'+matchTime+'</div>';
				m.html+='<div class="title" id="gtitle">'+title+'·'+subTitle+'</div>';
				m.html+='<div class="team team1"><div class="imgbox"><img src="'+team1Img+'" onerror="this.src=\'/jc/images/match_default.png\'"/></div><span class="tiamname">'+team1Name+'</span></div>';
				m.html+='<div class="tiamvs2"></div>';
				m.html+='<div class="tiamvsbo">'+boNum+'</div>';
				m.html+='<div class="team team2"><div class="imgbox"><img src="'+team2Img+'" onerror="this.src=\'/jc/images/match_default.png\'"/></div><span class="tiamname">'+team2Name+'</span></div>';
				m.html+='</div></div>';
				$('.main-content').append(m.html);
				
				var items=obj.items;
				if(items.length>0){
					//投注面板
					m.html='<div class="play-tab "><div class="tabbox">';
					var optionHtml='<div class="optionall-box">';
					for(var i=0;i<items.length;i++){
						var item=items[i];
						var fId=item.fId;
						var startDate=item.startDate;// 开始投注时间
						var fieldTitle=item.fieldTitle;// 比赛局标题
						var status=item.status;// 状态0.不可以投注1.可以投注
						if(i==0){
							m.html+='<a class="tab-item mui-active" data="'+fId+'">'+fieldTitle+'</a>';
						}else{
							m.html+='<a class="tab-item" data="'+fId+'">'+fieldTitle+'</a>';
						}
						var pitems=item.pitems;
						if(pitems.length>0){
							if(i==0){
								optionHtml+='<ul class="option-box mui-active" id="optionbox'+fId+'">';
							}else{
								optionHtml+='<ul class="option-box" id="optionbox'+fId+'">';
							}
							for(var j=0;j<pitems.length;j++){
								var pitem=pitems[j];
								var pTId=pitem.pTId;// 玩法id
								var playTitle=pitem.playTitle;// 玩法名称
								var openStatus=pitem.openStatus;// 开奖状态0=未开将 1=已开奖
								optionHtml+='<li class="option-cell" id="option'+pTId+'">';
								var isBet="1";// 是否可以投注
								if(status==0 || openStatus==1){
									optionHtml+='<img src="images/over.png">';
									isBet="0";
								}
								optionHtml+='<div class="title"><span class="ico"></span>'+playTitle+'</div>';
								if(status==0 || openStatus==1){
									optionHtml+='<ul class="betbox colour-gray">';
								}else{
									optionHtml+='<ul class="betbox">';
								}
								var itemo=pitem.itemo;
								if(itemo.length>0){
									var widthClass="";
									var floatClass="";
									if(itemo.length>3){
										widthClass="w45";
										floatClass="bet-float";
									}else{
										widthClass="w60";
									}
									for(var k=0;k<itemo.length;k++){
										var itemoo=itemo[k];
										var rate=itemoo.rate;// 投注项赔率
										var optionTitle=itemoo.optionTitle;// 投注项名称
										var oId=itemoo.oId;// 投注项id
										optionHtml+='<li class="bet-cell '+widthClass+' '+floatClass+'">'
											+'<span class="name">'+optionTitle+'</span>'
											+'<span class="rate" data="'+oId+'" status="'+isBet+'" rate="'+rate+'" group="g1" optionTitle="'+optionTitle+'" playTitle="'+playTitle+'">'+rate
											+'</span></li>';
									}
								}
								optionHtml+='</ul>';
								optionHtml+='</li>';
							}
							optionHtml+='</ul>';
						}
					}
					$('.main-content').append(m.html);
					$('.main-content').append(optionHtml);
					
					//绑定tab
					$(".tab-item").bind("click", function(){
						$('.option-box').hide();
						var id=$(this).attr('data');
						$('#optionbox'+id).fadeIn();
						$('.tab-item').removeClass('mui-active');
						$(this).addClass('mui-active');
					});
					
					$(".rate").bind("click", function(){
						var status=$(this).attr('status');
						if(status==1){
							var id=$(this).attr('data');
							var rate=$(this).attr('rate');
							var optionTitle=$(this).attr('optionTitle');
							var playTitle=$(this).attr('playTitle');
							ga.optionId=id;
							ga.rate=rate;
							ga.optionTitle=optionTitle;
							ga.playTitle=playTitle;
							ga.betPanel();
						}
					});
				}
			}else{
				m.notData();//无数据布局
				$('.not-data').show();
			}
		}else{
			m.notData();//无数据布局
			$('.not-data').show();
		}
	},
	betPanel:function(){
		var html=$('#betPanel').html();
		if(html==null||html==''){
			ga.initBetPanel();
		}else{
			$("#money").html(move.user.money);
		}
		$('#playTitle').text(ga.playTitle);
		$('#optionTitle').text(ga.optionTitle);
		$('#optionRate').text(ga.rate);
		ga.betPanelShow();
	},
	betPanelShow:function(){
		$('.all-b-mark').show();
//		$('#betPanel').css('top',);
		$('#betPanel').show();
	},
	betPanelHide:function(){
		$('.all-b-mark').hide();
		$('#betPanel').hide();
		$('#betMoney').val("");
	},
	initBetPanel:function(){
		//遮罩
		m.html='<div class="all-b-mark"></div>';
		$('.main-content').append(m.html);
		m.html='<div id="betPanel">';
		m.html=m.html+'<button class="cancel"></button>';
		m.html=m.html+'<div class="title">'+ga.gtitle+'</div>';
		m.html=m.html+'<div class="subtitle">'+ga.team1+"&nbsp;&nbsp;&nbsp;&nbsp;VS&nbsp;&nbsp;&nbsp;&nbsp;"+ga.team2+'</div>';
		m.html=m.html+'<div class="betrebox">';
		m.html=m.html+'<p class="betretitle" id="playTitle"></p>';
		m.html=m.html+'<p class="betretitle"><span id="optionTitle"></span>@<span id="optionRate"></span></p>';
		m.html=m.html+'</div>';
		
		m.html=m.html+'<div  class="bet-user"><span class="profit">可赢：&nbsp;&nbsp;<em id="profit">0.00</em></span><span class="balance">余额：&nbsp;&nbsp;<em id="money">'+(move.user.money==null?0.00:move.user.money)+'</em></span></div>';
		
		//投注输入金额框
		m.html=m.html+'<div class="betting"><div tabindex="1" class="input"><input id="betMoney" type="tel" val="" placeholder="限额'+ga.minBetMoney+'-'+ga.maxBetMoney+'"/> </div> <div class="bet-max betmoney" data="'+ga.maxBetMoney+'">Max</div></div>';
		
		//快捷金额
		m.html=m.html+'<ul class="betmoneybox">';
		var betMoneyArr=ga.betMoneyArr;
		for ( var i = 0; i < betMoneyArr.length; i++) {
			m.html=m.html+'<li class="betmoney" data="'+betMoneyArr[i]+'">'+betMoneyArr[i]+'</li>';
		}
		m.html=m.html+'</ul>';
		
		//投注按钮
		m.html=m.html+'<div class="confirm-bet">确认投注</div>';
		
		$('.main-content').append(m.html);
		
		$("#betPanel .cancel").bind("click", function(){
			$('.all-b-mark').hide();
			$('#betPanel').hide();
		});
		//绑定快捷投注金额
		$(".betmoney").bind("click", function(){
			var money=$(this).attr('data');
			$('#betMoney').val(money);
			ga.calculInput();
		});
		
		$('#betMoney').bind('input propertychange', function() {
			ga.calculInput();
		}); 
		
		//绑定投注接口
		$('.confirm-bet').bind('click', function() {
			ga.bet($('#betMoney').val());
		}); 
	},
	calculInput:function(){
		var betMoney=$('#betMoney').val();
		var rate=ga.rate;
		var profit= math.mul(betMoney,rate);
		$('#profit').text(profit);
	},
	bet:function(betMoney){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		var map = {};
		map['u']=move.user.u;
		map['oId']=ga.optionId;
		map['totalprice']=betMoney;
		req.post(move.api.bet, map, ga.betBulid, true);
	},
	betBulid:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code=='200'){
			var money=data.money;
			move.user.money=money;
			mui.toast(msg,{duration:'long', type:'div'});
			ga.betPanelHide();
		}else{
			mui.toast(msg,{duration:'long', type:'div'});
		}
	},
	columnPage:function(){
		var id=move.getParameter("id");
		var map = {};
		map['tId'] =id;
		req.post(move.api.match, map, ga.columnPageBuild, true);
	},
	columnPageBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			if(items.length>0){
				//tab
				m.html='<div class="play-tab "><div class="tabbox">';
				for(var i=0;i<items.length;i++){
					var obj=items[i];
					var title=obj.title;
					var tid=obj.tid;
					if(i==0){
						m.html+='<a class="tab-item mui-active" data="'+tid+'">'+title+'</a>';
					}else{
						m.html+='<a class="tab-item" data="'+tid+'">'+title+'</a>';
					}
				}
				m.html+='</div></div>';
				$('.main-content').append(m.html);
				//绑定tab
				$(".tab-item").bind("click", function(){
					$('.tab-item').removeClass('mui-active');
					$(this).addClass('mui-active');
					// 重置上拉加载
					mui('#content').pullRefresh().refresh(true);
					$("#content").show();
					$('.not-data').hide();
					move.pageIndex=0;
					$("#dataList").html("");
					ga.gameListData();
				});
				ga.gameList();
			}else{
				m.notData();//无数据布局
			}
		}else{
			m.notData();//无数据布局
		}
	},
	gameList:function(){
		move.refreshPage();// 刷新页面
		//写分页布局容器
		m.html='<div id="content" class="list-box mui-scroll-wrapper bxs-pages" style="margin-top: 84px;">';
		m.html=m.html+'<div class="mui-scroll"><ul class="recharge-list-box" id="dataList"></ul>';
		m.html=m.html+'</div>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		m.notData();//无数据布局
		//分页js
		m.html='<script type="text/javascript">';
		m.html=m.html+'me.bindTab();';
		m.html=m.html+'mui.init({pullRefresh:{container:"#content",up:{height:100,auto:true,callback:function(){ga.refreshPage("gameListData");}}}});</script>';
		$('.main-content').append(m.html);
	},
	gameListData:function(){
//		if(isReset){
//			move.pageIndex=0;
//		}
		var tId=$(".tabbox .mui-active").attr("data");
		var map={};
		map['tId']=tId;
		map['pageIndex']=move.pageIndex;
		req.post(move.api.gameList, map, ga.gameListDataBuild, true);
	},
	gameListDataBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			var length=items.length;
			if(length>0){
				m.html='';
				for(var i=0;i<length;i++){
					var obj=items[i];
					var mId=obj.mId;
					var matchTime=obj.matchTime;
					var title=obj.title;
					var red=obj.red;// 红队
					var subTitle=obj.subTitle;
					var boNum=obj.boNum;
					var matchTime=obj.matchTime;
					var team1Name=obj.team1Name;
					var team1Img=obj.team1Img;
					var team2Name=obj.team2Name;
					var team2Img=obj.team2Img;
					m.html+='<li class="gone" data="'+mId+'">';
					m.html+='<div class="title">'+title+"·"+subTitle+'</div>';
					var redName="";
					var redImg="";
					var blueName="";
					var blueImg="";
					if(team1Name==red){
						redName=team1Name;
						redImg=team1Img;
						blueName=team2Name;
						blueImg=team2Img;
					}else{
						redName=team2Name;
						redImg=team2Img;
						blueName=team1Name;
						blueImg=team1Img;
					}
					m.html+='<div class="team team1"><div class="imgbox"><img src="'+redImg+'" onerror="this.src=\'/jc/images/match_default.png\'"/></div><span class="tiamname">'+redName+'<span class="field red">(主)</span></span></div>';
					m.html+='<div class="tiamvs"><span class="linele"></span>'+boNum+'<span class="liner"></span></div><div class="match_time">'+matchTime+'</div>';
					m.html+='<div class="team team2"><div class="imgbox"><img src="'+blueImg+'" onerror="this.src=\'/jc/images/match_default.png\'"/></div><span class="tiamname">'+blueName+'<span class="field yellow">(客)</span></span></div>';
					m.html+='</li>';
				}
				$("#dataList").append(m.html);
				//绑定点击事件
				$("li.gone").bind("tap", function(){
					var id=$(this).attr('data');
					location.hash='#gameView&id='+id;
				});
				move.pageIndex++;
				mui('#content').pullRefresh().endPullupToRefresh();
			}else{
				if(move.pageIndex==0){
					$('.not-data').show();
					$('#content').hide();
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}else{
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}
			}
		}else{
			if(move.pageIndex==0){
				$('.not-data').show();
				$('#content').hide();
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}else{
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}
		}
	},
	refreshPage:function(func){
		if(func){
			ga[func].call();
		}else if($(".page-tab-item.mui-active").length>0){
			ga[$(".page-tab-item.mui-active").attr("data")].call();
		} 
	}
}
/**game.js**/

/**me.js**/
var me={
	home:function(){
		m.html='<div class="head-box">';
		m.html=m.html+'<div class="head"><img src="'+move.user.logo+'" onerror="this.src=\'/jc/images/head_default.png\'"/></div>';
		if(move.user.u){
			m.html=m.html+'<div class="name">'+move.user.loginName+'</div>';
		}else{
			m.html=m.html+'<div class="name"><a class="colour-white" href="#login">点击登录</a></div>';
		}
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		
		//我的页面功能条布局
		m.html='<div class="me-cell-box">';
		m.html=m.html+'<ul class="mui-table-view">';
		//竞猜记录
		m.html=m.html+'<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#jcRecord">';
		m.html=m.html+'<span class="me-ico"><img src="/jc/images/icon_competition.png"></span><span class="me-ico-words fl">竞猜记录</span> ';
		m.html=m.html+'</a> </li>';
		
		m.html=m.html+'<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#tradeRecord">';
		m.html=m.html+'<span class="me-ico"><img src="/jc/images/icon_transaction.png"></span><span class="me-ico-words fl">交易记录</span> ';
		m.html=m.html+'</a> </li>';
		
		m.html=m.html+'<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#levelRecord">';
		m.html=m.html+'<span class="me-ico"><img src="/jc/images/icon_report forms.png"></span><span class="me-ico-words fl">下级报表</span> ';
		m.html=m.html+'</a> </li>';
		
		m.html=m.html+'<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#tuiGuangLink">';
		m.html=m.html+'<span class="me-ico"><img src="/jc/images/icon_link.png"></span><span class="me-ico-words fl">推广链接</span> ';
		m.html=m.html+'</a> </li>';
		
		m.html=m.html+'<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#setting">';
		m.html=m.html+'<span class="me-ico"><img src="/jc/images/icon_install.png"></span><span class="me-ico-words fl">设&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;置</span> ';
		m.html=m.html+'</a> </li>';
		
		$('.main-content').append(m.html);
	},
	jcRecord:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		move.refreshPage();// 刷新页面
		//tab
		m.html='<div class="page-tab">';
		m.html=m.html+'<a class="page-tab-item mui-active w3" data="1">待开奖</a>';
		m.html=m.html+'<a class="page-tab-item w3" data="2">中奖</a>';
		m.html=m.html+'<a class="page-tab-item w3" data="3">未中奖</a>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		//绑定tab
//		me.bindTab();
		$(".page-tab-item").bind("click", function(){
			$('.page-tab-item').removeClass('mui-active');
			$(this).addClass('mui-active');
			// 重置上拉加载
			mui('#content').pullRefresh().refresh(true);
			move.pageIndex=0;
			$("#content").show();
			$('.not-data').hide();
			$("#dataList").html("");
			me.jcRecordData();
		});
		
		//写分页布局容器
		m.html='<div id="content" class="list-box mui-scroll-wrapper bxs-pages" style="margin-top: 90px;">';
		m.html=m.html+'<div class="mui-scroll"><ul class="recharge-list-box" id="dataList"></ul>';
		m.html=m.html+'</div>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		//无数据布局
		m.notData();
		//分页js
		m.html='<script type="text/javascript">';
		m.html=m.html+'mui.init({pullRefresh:{container:"#content",up:{height:100,auto:true,callback:function(){me.refreshPage("jcRecordData");}}}});</script>';
		$('.main-content').append(m.html);
	},
	jcRecordData:function(isReset){
		var status=$(".page-tab .mui-active").attr("data")-1;
		var map = {};
		map['u']=move.user.u;
		map['status']=status;
		map['pageIndex']=move.pageIndex;
		req.post(move.api.jcRecord, map, me.jcRecordDataBuild, true);
	},
	jcRecordDataBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			if(items.length>0){
				m.html='';
				for(var i=0;i<items.length;i++){
					var macthObj=items[i];
					var betTime=macthObj.betTime;// 时间
					var playName=macthObj.playName;// 比赛名称
					var winResult=macthObj.winResult;// 状态0.未开奖 1.中奖 2.未中奖
					m.html+='<li class="jc-record-line">';
						m.html+='<div class="jc-record-info">';
							m.html+='<p class="match-title colour-white">';
								m.html+=playName;
							m.html+='</p>';
							m.html+='<p class="match-date colour-white">';
								m.html+=betTime;
							m.html+='</p>';
							var color="";
							var status="";
							if(winResult==0){
								color="colour-yellow";
								status="待开奖";
							}else if(winResult==1){
								color="colour-red";
								status="中奖";
							}else if(winResult==2){
								color="colour-green";
								status="未中奖";
							}
							m.html+='<span class="jc-record-status '+color+'">';
								m.html+=status;
							m.html+='</span>';
						m.html+='</div>';
					var roomArr=macthObj.roomArr;// 局数组
					if(roomArr.length>0){
						for(var j=0;j<roomArr.length;j++){
							var roomObj=roomArr[j];
							var room=roomObj.room;// 局名称
							m.html+='<div class="jc-record-detail">';
								m.html+='<p class="play-title colour-white">';
									m.html+=room;
								m.html+='</p>';
								m.html+='<ul>';
							var optionArr=roomObj.optionArr;
							if(optionArr.length>0){
								for(var k=0;k<optionArr.length;k++){
									var optionObj=optionArr[k];
									var optionTitle=optionObj.optionTitle;// 投注项名称
									var betMoney=optionObj.betMoney;// 投注金额
									var winCash=optionObj.winCash;// 收益
									var betRate=optionObj.betRate;// 赔率
									m.html+='<li class="jc-record-detail-line">';
										m.html+='<p class="colour-yellow">';
											m.html+=optionTitle;
										m.html+='</p>';
										m.html+='<div class="bet-info colour-white">';
											m.html+='<div class="w33">';
												m.html+='投注金额：';
												m.html+='<span class="colour-red">'+betMoney;
												m.html+='</span>';
											m.html+='</div>';
											m.html+='<div class="w33">';
												m.html+='<span class="bet-rate">赔率：'+betRate;
												m.html+='</span>';
											m.html+='</div>';
											if(winResult==1){
											m.html+='<div class="w33">';
												m.html+='收益：<span class="colour-red">'+winCash;
												m.html+='</span>';
											m.html+='</div>';
											}
										m.html+='</div>';
									m.html+='</li>';
								}
								m.html+='</ul>';
								m.html+='</div>';
							}
						}
					}
					m.html+='</li>';
					$("#dataList").html(m.html);
					$(".jc-record-info").bind("tap",function(){
						var node=$(this).nextAll();
						if(node.is(':hidden')){
							$(".jc-record-detail").hide();
							node.show();
						}else{
							node.hide();　
						}
					});
					move.pageIndex++;
					mui('#content').pullRefresh().endPullupToRefresh();
				}
			}else{
				if(move.pageIndex==0){
					$('.not-data').show();
					$('#content').hide();
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}else{
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}
			}
		}else{
			if(move.pageIndex==0){
				$('.not-data').show();
				$('#content').hide();
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}else{
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}
		}
	},
	tradeRecord:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		move.refreshPage();// 刷新页面
		//写分页布局容器
		m.html='<div id="content" class="list-box mui-scroll-wrapper bxs-pages" style="margin-top: 44px;">';
		m.html=m.html+'<div class="mui-scroll"><ul class="recharge-list-box" id="dataList"></ul>';
		m.html=m.html+'</div>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		
		//无数据布局
		m.notData();
		//分页js
		m.html='<script type="text/javascript">';
		m.html=m.html+'mui.init({pullRefresh:{container:"#content",up:{height:100,auto:true,callback:function(){me.refreshPage("tradeRecordData");}}}});';
		m.html=m.html+'</script>';
		$('.main-content').append(m.html);
	},
	tradeRecordData:function(){
//		if(isReset){
//			move.pageIndex=0;
//		}
		var map = {};
		map['u'] =move.user.u;
		map['pageIndex'] =move.pageIndex;
		req.post(move.api.tradeList, map, me.tradeRecordDataBuild, true);
	},
	tradeRecordDataBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			var length=items.length;
			if(length>0){
				m.html='';
				for(var i=0;i<length;i++){
					var obj=items[i];
					var tradeContent=obj.tradeContent;
					var tradeMoney=obj.tradeMoney;
					var tradeTime=obj.tradeTime;
					m.html+='<li class="trade-record-line">';
						m.html+='<div class="trade-record-info">';
							m.html+='<p class="colour-white">';
								m.html+=tradeContent;
							m.html+='</p>';
							m.html+='<p class="colour-white">';
								m.html+=tradeTime;
							m.html+='</p>';
							m.html+='<span class="trade-money colour-white">';
								if(parseInt(tradeMoney)>0){
									m.html+='<span class="colour-red">'+tradeMoney+'</span>';
								}else{
									m.html+='<span class="colour-green">'+tradeMoney+'</span>';
								}
							m.html+='</span>';
						m.html+='</div>';
					m.html+='</li>';
				}
				$("#dataList").append(m.html);
				move.pageIndex++;
				mui('#content').pullRefresh().endPullupToRefresh();
			}else{
				if(move.pageIndex==0){
					$('.not-data').show();
					$('#content').hide();
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}else{
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}
			}
		}else{
			if(move.pageIndex==0){
				$('.not-data').show();
				$('#content').hide();
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}else{
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}
		}
	},
	setting:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		m.html='<div class="me-cell-box">';
			m.html+='<ul class="mui-table-view">';
				m.html+='<li class="mui-table-view-cell"> <a class="mui-navigate-right" href="#changePwd">';
				m.html+='<span class="me-ico"><span class="mui-icon mui-icon-locked"></span></span><span class="me-ico-words fl">修改登录密码</span> ';
				m.html+='</a></li>';
			m.html+='</ul>';
		m.html+='</div>';
		m.html+='<div class="bottom-box">';
			m.html+='<div class="btn colour-white" id="logout">退出当前帐号</div>';
		m.html+='</div>';
		$('.main-content').append(m.html);
		$("#logout").bind("tap",function(){
			var map = {};
			map['u']=move.user.u;
			req.post(move.api.logout, map, me.logoutBuild, true);
		});
	},
	logoutBuild:function(ret){
		var code=ret.code;
		var msg=ret.msg;
		if(code=='200'){
			user.clear();// 清除本地数据
			location.reload();// 刷新页面
			baseObj.openIndex();
		}else{
			mui.toast(msg,{duration:'long', type:'div'});
		}
	},
	changePwd:function(){
		if(move.user.u==null){
			baseObj.openLogin();
			return;
		}
		m.html='<div class="password-box">';
			m.html+='<div class="mui-input-row mui-password">';
				m.html+='<input type="password" id="oldpwd" class="mui-input-password" maxlength="12" placeholder="请输入旧密码">';
			m.html+='</div>';
			m.html+='<div class="mui-input-row mui-password">';
				m.html+='<input type="password" id="newpwd" class="mui-input-password" maxlength="12" placeholder="请输入新密码">';
			m.html+='</div>';
			m.html+='<div class="mui-input-row mui-password">';
				m.html+='<input type="password" id="newpwd2" class="mui-input-password" maxlength="12" placeholder="请确认新密码">';
			m.html+='</div>';
			m.html+='<a href="retrievePwd.html" style="color: black;float: right;">忘记旧密码?</a>';
			m.html+='<div class="btn-box colour-white">';
				m.html+='<input onclick="" id="savebtn" class="btn" value="保存"/>';
			m.html+='</div>';
		m.html+='</div>';
		
		$('.main-content').append(m.html);
		$("#savebtn").bind("tap",function(){
			var oldpwd=$("#oldpwd").val();
			var newpwd=$("#newpwd").val();
			var newpwd2=$("#newpwd2").val();
			if(!oldpwd){
				mui.toast('请输入旧密码',{duration:'long', type:'div'});
				return;
			}
			if(!newpwd){
				mui.toast('请输入新密码',{duration:'long', type:'div'});
				return;
			}
			if(!newpwd2){
				mui.toast('请确认新密码',{duration:'long', type:'div'});
				return;
			}
			var map = {};
			map['u']=move.user.u;
			map['oldpassword']=hex_md5(oldpwd).toUpperCase();
			map['newpassword']=hex_md5(newpwd).toUpperCase();
			map['repassword']=hex_md5(newpwd2).toUpperCase();
			req.post(move.api.changePwd, map, me.changePwdBuild, true);
		});
	},
	changePwdBuild:function(ret){
		var code=ret.code;
		var msg=ret.msg;
		if(code=='200'){
			mui.alert('修改成功','修改登录密码','确定',function(){  
				baseObj.openMe();
            },'div');
		}else{
			mui.toast(msg,{duration:'long', type:'div'});
		}
	},
	login:function(){
		m.html='<div class="login-head-box">';
			m.html+='<img alt="" src="/jc/images/logo.png">';
		m.html+='</div>';
		m.html+='<div class="login-form">';
			m.html+='<input type="tel" placeholder="请输入帐号" id="phone" class="login-input" maxlength="11"/>';
			m.html+='<p class="line-grey"></p>';
			m.html+='<input type="password" placeholder="请输入密码" id="pwd" class="login-input" maxlength="12"/>';
			m.html+='<p class="line-grey"></p>';
			m.html+='<div class="btn-box">';
				m.html+='<input type="button" value="确定" id="loginbtn" class="btn login-btn">';
				m.html+='<a href="#register"><input type="button" value="注册" class="btn reg-btn"></a>';
				m.html+='<span class="btn-forgetPass">';
					m.html+='<a class="colour-white" href="retrievePwd.html">忘记密码?</a>';
				m.html+='</span>';
			m.html+='</div>';
		m.html+='</div>';
		$('.main-content').append(m.html);
		$("#loginbtn").bind("tap",function(){
			var phone=$("#phone").val();
			var pwd=$("#pwd").val();
			if(!phone){
				mui.toast('请输入帐号',{duration:'long', type:'div'});
				return;
			}
			if(!pwd){
				mui.toast('请输入密码',{duration:'long', type:'div'});
				return;
			}
			var map = {};
			map['loginName']=phone;
			map['password']=hex_md5(pwd).toUpperCase();
			req.post(move.api.login, map, me.loginBuild, true);
		});
	},
	loginBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code=='200'){
			var obj=data.obj;
			var uid=obj.uid;
			var u=obj.u;
			var logo=obj.logo;
			var cellPhone=obj.cellPhone;
			var loginName=obj.loginName;
			var userName=obj.userName;
			var money=obj.money;
			user.init(uid,u,"",logo,cellPhone,loginName,userName,money);
			user.set(move.user);
			baseObj.openIndex();
		}else{
			mui.toast(msg,{duration:'long', type:'div'});
			return;
		}
	},
	register:function(){
		m.html='<div class="login-head-box">';
			m.html+='<img alt="" src="/jc/images/logo.png">';
		m.html+='</div>';
		m.html+='<div class="reg-form">';
			m.html+='<div class="mui-input-row">';
				m.html+='<input type="tel" placeholder="请输入手机号" id="phone" class="reg-input" maxlength="11"/>';
			m.html+='</div>';
			m.html+='<div class="mui-input-row">';
				m.html+='<input type="password" placeholder="请输入密码" id="pwd" class="mui-input-password reg-input" maxlength="12"/>';
			m.html+='</div>';
			m.html+='<div class="mui-input-row">';
				m.html+='<input type="password" placeholder="请确认密码" id="pwd2" class="mui-input-password reg-input" maxlength="12"/>';
			m.html+='</div>';
			m.html+='<div class="verification-code" >';
				m.html+='<img id="sendCode" src="/api/baseData_checkCode" style="border: none;background: none;">';
			m.html+='</div>';
			m.html+='<div class="mui-input-row" style="padding-right: 110px;">';
				m.html+='<input type="text" placeholder="验证码" id="code" maxlength="6" style="float: left;"/>';
			m.html+='</div>';
			m.html+='<div class="btn-box">';
				m.html+='<input type="button" value="注册" id="regbtn" class="btn reg-btn">';
				m.html+='<a href="#login"><input type="button" value="已有帐号，去登录" id="logbtn" class="btn login-btn"></a>';
			m.html+='</div>';
		m.html+='</div>';
		$('.main-content').append(m.html);
		$("#sendCode").bind("tap",function(){
			$("#sendCode").get(0).src=move.api.checkCode+'?t='+Math.random(); 
		});
		$("#regbtn").bind("tap",function(){
			var phone=$("#phone").val();
			var pwd=$("#pwd").val();
			var pwd2=$("#pwd2").val();
			var code=$("#code").val();
			if(!phone){
				mui.toast('请输入手机号',{duration:'long', type:'div'});
				return;
			}
			if(!pwd){
				mui.toast('请输入密码',{duration:'long', type:'div'});
				return;
			}
			if(!pwd2){
				mui.toast('请输入确认密码',{duration:'long', type:'div'});
				return;
			}
			if(!code){
				mui.toast('请输入验证码',{duration:'long', type:'div'});
				return;
			}
			var map = {};
			map['loginName']=phone;
			map['password']=hex_md5(pwd).toUpperCase();
			map['repassword']=hex_md5(pwd2).toUpperCase();
			map['inpcode']=code;
			req.post(move.api.reg, map, me.registerBuild, true);
		});
	},
	registerBuild:function(ret){
		var code=ret.code;
		var msg=ret.msg;
		if(code=='200'){
			mui.alert('注册成功','注册','确定',function(){  
				baseObj.openLogin();
            },'div');
		}else{
			mui.toast(msg,{duration:'long', type:'div'});
		}
	},
	money:function(){
		var map = {};
		map['u']=move.user.u;
		req.post(move.api.money, map, me.moneyBuild, false);
	},
	moneyBuild:function(ret){
		var code=ret.code;
		var msg=ret.msg;
		if(code=='200'){
			var data=ret.data;
			move.user.money=data.money;
			alert(move.user.money+"__");
		}
	},
	bindTab:function(){
		$(".page-tab-item").each(function(){
			$(this).bind("tap",function(){
				$(".page-tab-item").removeClass("mui-active");//bind css
				$(this).addClass("mui-active");
				var func = me[$(this).attr("data")];
				if(func){
					func.call(0,true);
				}
			});
		});
	},
	refreshPage:function(func){
		if(func){
			me[func].call();
		}else if($(".page-tab-item.mui-active").length>0){
			me[$(".page-tab-item.mui-active").attr("data")].call();
		} 
	}
}

var promotion={
	promoList:function(){
		move.refreshPage();// 刷新页面
		//写分页布局容器
		m.html='<div id="content" class="list-box mui-scroll-wrapper bxs-pages" style="margin-top: 50px;">';
		m.html=m.html+'<div class="mui-scroll"><ul class="recharge-list-box" id="dataList"></ul>';
		m.html=m.html+'</div>';
		m.html=m.html+'</div>';
		$('.main-content').append(m.html);
		m.notData();//无数据布局
		//分页js
		m.html='<script type="text/javascript">';
		m.html=m.html+'promotion.bindTab();';
		m.html=m.html+'mui.init({pullRefresh:{container:"#content",up:{height:100,auto:true,callback:function(){promotion.refreshPage("promoListData");}}}});</script>';
		$('.main-content').append(m.html);
	},
	promoListData:function(res){
		var pId=$(".tabbox .mui-active").attr("data");
		var map={};
		map['id']=pId;
		map['pageIndex']=move.pageIndex;
		req.post(move.api.promoList, map, promotion.promoListDataBuild, true);
	},
	promoListDataBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		if(code=='200'){
			var items=data.items;
			var length=items.length;
			if(length>0){
				m.html='';
				for(var i=0;i<length;i++){
					var obj=items[i];
					var pId=obj.id;
					var title=obj.title;
					var img=obj.img;
					var startDate=obj.startDate;
					
					m.html+='<li class="gone" data="'+pId+'">';
					m.html+='<div class="mui-card">';
					m.html+='<div class="mui-card-header">'+title+'</div>';
					
					m.html+='<div class="mui-card-content"><img src="'+img+'" style="width:100%"></div>';
					m.html+='<div class="mui-card-footer"><span>起始时间：'+startDate+'</span><span>详情》</span></div>';
					m.html+='</div>';
					m.html+='</li>';
				}
				$("#dataList").append(m.html);
				//绑定点击事件
				$("li.gone").bind("tap", function(){
					var id=$(this).attr('data');
					location.hash='#promoView&id='+id;
				});
				move.pageIndex++;
				mui('#content').pullRefresh().endPullupToRefresh();
			}else{
				if(move.pageIndex==0){
					$('.not-data').show();
					$('#content').hide();
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}else{
					mui('#content').pullRefresh().endPullupToRefresh(true);
				}
			}
		}else{
			if(move.pageIndex==0){
				$('.not-data').show();
				$('#content').hide();
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}else{
				mui('#content').pullRefresh().endPullupToRefresh(true);
			}
		}
	},
	bindTab:function(){
		$(".page-tab-item").each(function(){
			$(this).bind("tap",function(){
				$(".page-tab-item").removeClass("mui-active");//bind css
				$(this).addClass("mui-active");
				var func = promotion[$(this).attr("data")];
				if(func){
					func.call(0,true);
				}
			});
		});
	},
	refreshPage:function(func){
		if(func){
			promotion[func].call();
		}else if($(".page-tab-item.mui-active").length>0){
			promotion[$(".page-tab-item.mui-active").attr("data")].call();
		} 
	},
	promoPage:function(){
		var id=move.getParameter('id');
		var map = {};
		map['id'] =id;
		req.post(move.api.promoView, map, promotion.promoPageBuild, true);
	},
	promoPageBuild:function(ret){
		var code=ret.code;
		var data=ret.data;
		var msg=ret.msg;
		if(code=='200'){
			var obj=data.obj;
			if(obj){
				var title=obj.title;
				var content=obj.content;
				var startDate=obj.startDate;
				var img=obj.img;
				
				m.html='<div class="mui-content-padded" id="content">';
				m.html+='<p style="text-align: center;">'+title+'</p>';
				m.html+='<p>'+content+'</p>';
				m.html+='</div>';
				$('.main-content').append(m.html);
			}else{
				m.notData();//无数据布局
				$('.not-data').show();
			}
		}else{
			m.notData();//无数据布局
			$('.not-data').show();
		}
	}
}

$(document).ready(function(){
	$(window).bind('hashchange', function(e) {
		move.hash.hashReq();
	});
	move.hash.hashReq();
	
	$(document).bind('contextmenu', function(e) {
		e.preventDefault();
	});
	//禁止放大/缩小
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

var math={
	div:function(arg1, arg2){
		//除法
		var t1 = 0, t2 = 0, r1, r2;
	    try {
	        t1 = arg1.toString().split(".")[1].length;
	    }
	    catch (e) {
	    }
	    try {
	        t2 = arg2.toString().split(".")[1].length;
	    }
	    catch (e) {
	    }
	    with (Math) {
	        r1 = Number(arg1.toString().replace(".", ""));
	        r2 = Number(arg2.toString().replace(".", ""));
	        return (r1 / r2) * pow(10, t2 - t1);
	    }
	},
	mul:function(arg1, arg2) {
		//乘法
	    var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	    try {
	        m += s1.split(".")[1].length;
	    }
	    catch (e) {
	    }
	    try {
	        m += s2.split(".")[1].length;
	    }
	    catch (e) {
	    }
	    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
	},
	sub:function(arg1, arg2) {
		//减法
	    var r1, r2, m, n;
	    try {
	        r1 = arg1.toString().split(".")[1].length;
	    }
	    catch (e) {
	        r1 = 0;
	    }
	    try {
	        r2 = arg2.toString().split(".")[1].length;
	    }
	    catch (e) {
	        r2 = 0;
	    }
	    m = Math.pow(10, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
	    n = (r1 >= r2) ? r1 : r2;
	    return ((arg1 * m - arg2 * m) / m).toFixed(n);
	},
	add:function(arg1, arg2) {
		//加法
	    var r1, r2, m, c;
	    try {
	        r1 = arg1.toString().split(".")[1].length;
	    }
	    catch (e) {
	        r1 = 0;
	    }
	    try {
	        r2 = arg2.toString().split(".")[1].length;
	    }
	    catch (e) {
	        r2 = 0;
	    }
	    c = Math.abs(r1 - r2);
	    m = Math.pow(10, Math.max(r1, r2));
	    if (c > 0) {
	        var cm = Math.pow(10, c);
	        if (r1 > r2) {
	            arg1 = Number(arg1.toString().replace(".", ""));
	            arg2 = Number(arg2.toString().replace(".", "")) * cm;
	        } else {
	            arg1 = Number(arg1.toString().replace(".", "")) * cm;
	            arg2 = Number(arg2.toString().replace(".", ""));
	        }
	    } else {
	        arg1 = Number(arg1.toString().replace(".", ""));
	        arg2 = Number(arg2.toString().replace(".", ""));
	    }
	    return (arg1 + arg2) / m;
	}
}






