//jQuery.timers 1.2
jQuery.fn.extend({everyTime:function(c,a,d,b){return this.each(function(){jQuery.timer.add(this,c,a,d,b)})},oneTime:function(c,a,d){return this.each(function(){jQuery.timer.add(this,c,a,d,1)})},stopTime:function(c,a){return this.each(function(){jQuery.timer.remove(this,c,a)})}});jQuery.extend({timer:{global:[],guid:1,dataKey:"jQuery.timer",regex:/^([0-9]+(?:\.[0-9]*)?)\s*(.*s)?$/,powers:{ms:1,cs:10,ds:100,s:1E3,das:1E4,hs:1E5,ks:1E6},timeParse:function(c){if(void 0==c||null==c)return null;var a=this.regex.exec(jQuery.trim(c.toString()));return a[2]?parseFloat(a[1])*(this.powers[a[2]]||1):c},add:function(c,a,d,b,e){var g=0;jQuery.isFunction(d)&&(e||(e=b),b=d,d=a);a=jQuery.timer.timeParse(a);if(!("number"!=typeof a||isNaN(a)||0>a)){if("number"!=typeof e||isNaN(e)||0>e)e=0;e=e||0;var f=jQuery.data(c,this.dataKey)||jQuery.data(c,this.dataKey,{});f[d]||(f[d]={});b.timerID=b.timerID||this.guid++;var h=function(){(++g>e&&0!==e||!1===b.call(c,g))&&jQuery.timer.remove(c,d,b)};h.timerID=b.timerID;f[d][b.timerID]||(f[d][b.timerID]=window.setInterval(h,a));this.global.push(c)}},remove:function(c,a,d){var b=jQuery.data(c,this.dataKey),e;if(b){if(!a)for(a in b)this.remove(c,a,d);else if(b[a]){if(d)d.timerID&&(window.clearInterval(b[a][d.timerID]),delete b[a][d.timerID]);else for(d in b[a])window.clearInterval(b[a][d]),delete b[a][d];for(e in b[a])break;e||(e=null,delete b[a])}for(e in b)break;e||jQuery.removeData(c,this.dataKey)}}}});jQuery(window).bind("unload",function(){jQuery.each(jQuery.timer.global,function(c,a){jQuery.timer.remove(a)})});
document.write("<script type='text/javascript' src='/js/md5.js'></script>");  
document.write("<script type='text/javascript' src='/js/login.js'></script>");  
var move = {};
//分页的页码
move.pageIndex=0;
//缓存存储时间，毫秒
move.storageTime=2592000000;

// move.local = location.toString().indexOf("/longzhicai")>-1?0:1;
move.local = location.toString().indexOf("/cp")>-1?0:1;
// move.server="http://"+window.location.host+(move.local?"":"/longzhicai");
move.server="http://"+window.location.host+(move.local?"":"/cp");
// move.webpath=move.local?"":"/longzhicai";
move.webpath=move.local?"":"/cp";

move.advert=move.server+'/api/baseData_advert';
move.notice=move.server+'/api/baseData_notice';
move.latestList=move.server+'/api/baseData_latestList';
move.winList=move.server+'/api/baseData_winList';
move.changePwd=move.server+'/api/user_changePwd';
move.cashPassword=move.server+'/api/user_cashPassword';
move.gameColumn=move.server+'/api/baseData_gameColumn';
move.bankList=move.server+'/api/cash_bankList';
move.cityList=move.server+'/api/baseData_wechatList3';
move.cashSubmit=move.server+'/api/cash_submit';
move.recharge=move.server+'/api/recharge_submit';
move.banklist=move.server+'/api/baseData_bankMsg';
move.rechargeOfflineSubmit=move.server+'/api/recharge_offlineSubmit';
move.betRecordList=move.server+'/api/baseData_betRecordList';
move.balanceRecordList=move.server+'/api/baseData_balanceRecordList';
move.balanceRecordDetail=move.server+'/api/baseData_balanceRecordDetail';
move.cashBindList=move.server+'/api/cash_bindList';
move.cashBindSubmit=move.server+'/api/cash_bindSubmit';
move.helpList=move.server+'/api/baseData_helpList';//


move.login=move.server+'/api/user_login';
move.logout=move.server+'/api/user_logout';
move.sendCode=move.server+'/api/sms_send';
move.reg=move.server+'/api/user_reg';
move.guestReg=move.server+'/api/user_guestReg';
move.retrievePwd=move.server+'/api/user_retrievePwd';
move.money=move.server+'/api/baseData_money';
move.rechargeDetail=move.server+'/api/recharge_detail';
move.cashList=move.server+'/api/cash_list';
move.betList=move.server+'/api/baseData_betlist';
move.betListByType=move.server+'/api/baseData_betlistByType';
move.reportWeb=move.server+'/api/baseData_reportWeb';


move.bjPk10_currentTime=move.server+'/api/bjPk10_currentTime';
move.bjPk10_betPanel=move.server+'/api/bjPk10_betPanel';
move.bjPk10_bet=move.server+'/api/bjPk10_bet';
move.bjPk10_trend=move.server+'/api/bjPk10_trend';
move.bjPk10_hotRanking=move.server+'/api/bjPk10_hotRanking';
move.bjPk10_openList=move.server+'/api/bjPk10_openList';
move.bjPk10_latestLottery=move.server+'/api/bjPk10_latestLottery';

move.xyft_currentTime=move.server+'/api/xyft_currentTime';
move.xyft_betPanel=move.server+'/api/xyft_betPanel';
move.xyft_bet=move.server+'/api/xyft_bet';
move.xyft_trend=move.server+'/api/xyft_trend';
move.xyft_hotRanking=move.server+'/api/xyft_hotRanking';
move.xyft_openList=move.server+'/api/xyft_openList';
move.xyft_latestLottery=move.server+'/api/xyft_latestLottery';

move.sfPk10_currentTime=move.server+'/api/sfPk10_currentTime';
move.sfPk10_betPanel=move.server+'/api/sfPk10_betPanel';
move.sfPk10_bet=move.server+'/api/sfPk10_bet';
move.sfPk10_trend=move.server+'/api/sfPk10_trend';
move.sfPk10_hotRanking=move.server+'/api/sfPk10_hotRanking';
move.sfPk10_openList=move.server+'/api/sfPk10_openList';
move.sfPk10_latestLottery=move.server+'/api/sfPk10_latestLottery';

move.sfPk102_currentTime=move.server+'/api/sfPk102_currentTime';
move.sfPk102_betPanel=move.server+'/api/sfPk102_betPanel';
move.sfPk102_bet=move.server+'/api/sfPk102_bet';
move.sfPk102_trend=move.server+'/api/sfPk102_trend';
move.sfPk102_hotRanking=move.server+'/api/sfPk102_hotRanking';
move.sfPk102_openList=move.server+'/api/sfPk102_openList';
move.sfPk102_latestLottery=move.server+'/api/sfPk102_latestLottery';

move.jsft_currentTime=move.server+'/api/jsft_currentTime';
move.jsft_betPanel=move.server+'/api/jsft_betPanel';
move.jsft_bet=move.server+'/api/jsft_bet';
move.jsft_trend=move.server+'/api/jsft_trend';
move.jsft_hotRanking=move.server+'/api/jsft_hotRanking';
move.jsft_openList=move.server+'/api/jsft_openList';
move.jsft_latestLottery=move.server+'/api/jsft_latestLottery';

move.bj3_currentTime=move.server+'/api/bj3_currentTime';
move.bj3_betPanel=move.server+'/api/bj3_betPanel';
move.bj3_bet=move.server+'/api/bj3_bet';
move.bj3_trend=move.server+'/api/bj3_trend';
move.bj3_hotRanking=move.server+'/api/bj3_hotRanking';
move.bj3_openList=move.server+'/api/bj3_openList';
move.bj3_latestLottery=move.server+'/api/bj3_latestLottery';

move.bjLu28_currentTime=move.server+'/api/bjLu28_currentTime';
move.bjLu28_betPanel=move.server+'/api/bjLu28_betPanel';
move.bjLu28_bet=move.server+'/api/bjLu28_bet';
move.bjLu28_trend=move.server+'/api/bjLu28_trend';
move.bjLu28_hotRanking=move.server+'/api/bjLu28_hotRanking';
move.bjLu28_openList=move.server+'/api/bjLu28_openList';
move.bjLu28_latestLottery=move.server+'/api/bjLu28_latestLottery';

move.xjpLu28_currentTime=move.server+'/api/xjpLu28_currentTime';
move.xjpLu28_betPanel=move.server+'/api/xjpLu28_betPanel';
move.xjpLu28_bet=move.server+'/api/xjpLu28_bet';
move.xjpLu28_trend=move.server+'/api/xjpLu28_trend';
move.xjpLu28_hotRanking=move.server+'/api/xjpLu28_hotRanking';
move.xjpLu28_openList=move.server+'/api/xjpLu28_openList';
move.xjpLu28_latestLottery=move.server+'/api/xjpLu28_latestLottery';

move.cqSsc_currentTime=move.server+'/api/cqSsc_currentTime';
move.cqSsc_betPanel=move.server+'/api/cqSsc_betPanel';
move.cqSSc_bet=move.server+'/api/cqSsc_bet';
move.cqSSc_trend=move.server+'/api/cqSsc_trend';
move.cqSSc_hotRanking=move.server+'/api/cqSsc_hotRanking';
move.cqSSc_openList=move.server+'/api/cqSsc_openList';
move.cqSSc_winRanking=move.server+'/api/cqSsc_winRanking';
move.cqSsc_latestLottery=move.server+'/api/cqSsc_latestLottery';

move.xjSsc_currentTime=move.server+'/api/xjSsc_currentTime';
move.xjSsc_betPanel=move.server+'/api/xjSsc_betPanel';
move.xjSsc_bet=move.server+'/api/xjSsc_bet';
move.xjSsc_trend=move.server+'/api/xjSsc_trend';
move.xjSsc_hotRanking=move.server+'/api/xjSsc_hotRanking';
move.xjSsc_openList=move.server+'/api/xjSsc_openList';
move.xjSsc_latestLottery=move.server+'/api/xjSsc_latestLottery';

move.tjSsc_currentTime=move.server+'/api/tjSsc_currentTime';
move.tjSsc_betPanel=move.server+'/api/tjSsc_betPanel';
move.tjSsc_bet=move.server+'/api/tjSsc_bet';
move.tjSsc_trend=move.server+'/api/tjSsc_trend';
move.tjSsc_hotRanking=move.server+'/api/tjSsc_hotRanking';
move.tjSsc_openList=move.server+'/api/tjSsc_openList';
move.tjSsc_latestLottery=move.server+'/api/tjSsc_latestLottery';

move.five_currentTime=move.server+'/api/five_currentTime';
move.five_betPanel=move.server+'/api/five_betPanel';
move.five_bet=move.server+'/api/five_bet';
move.five_trend=move.server+'/api/five_trend';
move.five_hotRanking=move.server+'/api/five_hotRanking';
move.five_openList=move.server+'/api/five_openList';
move.five_winRanking=move.server+'/api/five_winRanking';
move.five_latestLottery=move.server+'/api/five_latestLottery';

move.bjssc_currentTime=move.server+'/api/bjSsc_currentTime';
move.bjssc_betPanel=move.server+'/api/bjSsc_betPanel';
move.bjssc_bet=move.server+'/api/bjSsc_bet';
move.bjssc_trend=move.server+'/api/bjSsc_trend';
move.bjssc_hotRanking=move.server+'/api/bjSsc_hotRanking';
move.bjssc_openList=move.server+'/api/bjSsc_openList';
move.bjssc_winRanking=move.server+'/api/bjSsc_winRanking';
move.bjssc_latestLottery=move.server+'/api/bjSsc_latestLottery';


move.gdK10_currentTime=move.server+'/api/gdK10_currentTime';
move.gdK10_betPanel=move.server+'/api/gdK10_betPanel';
move.gdK10_bet=move.server+'/api/gdK10_bet';
move.gdK10_trend=move.server+'/api/gdK10_trend';
move.gdK10_hotRanking=move.server+'/api/gdK10_hotRanking';
move.gdK10_openList=move.server+'/api/gdK10_openList';
move.gdK10_latestLottery=move.server+'/api/gdK10_latestLottery';

move.kuailepuke3_currentTime=move.server+'/api/poker_currentTime';
move.kuailepuke3_betPanel=move.server+'/api/poker_betPanel';
move.kuailepuke3_bet=move.server+'/api/poker_bet';
move.kuailepuke3_trend=move.server+'/api/poker_trend';
move.kuailepuke3_hotRanking=move.server+'/api/poker_hotRanking';
move.kuailepuke3_openList=move.server+'/api/poker_openList';
move.kuailepuke3_latestLottery=move.server+'/api/poker_latestLottery';

move.jiangsuk3_currentTime=move.server+'/api/jsK3_currentTime';
move.jiangsuk3_betPanel=move.server+'/api/jsK3_betPanel';
move.jiangsuk3_bet=move.server+'/api/jsK3_bet';
move.jiangsuk3_trend=move.server+'/api/jsK3_trend';
move.jiangsuk3_hotRanking=move.server+'/api/jsK3_hotRanking';
move.jiangsuk3_openList=move.server+'/api/jsK3_openList';
move.jsK3_latestLottery=move.server+'/api/jsK3_latestLottery';

move.jxk3_currentTime=move.server+'/api/jxK3_currentTime';
move.jxk3_betPanel=move.server+'/api/jxK3_betPanel';
move.jxk3_bet=move.server+'/api/jxK3_bet';
move.jxk3_trend=move.server+'/api/jxK3_trend';
move.jxk3_hotRanking=move.server+'/api/jxK3_hotRanking';
move.jxk3_openList=move.server+'/api/jxK3_openList';
move.jxk3=move.server+'/api/jxK3_latestLottery';

move.bjk3_currentTime=move.server+'/api/bjK3_currentTime';
move.bjk3_betPanel=move.server+'/api/bjK3_betPanel';
move.bjk3_bet=move.server+'/api/bjK3_bet';
move.bjk3_trend=move.server+'/api/bjK3_trend';
move.bjk3_hotRanking=move.server+'/api/bjK3_hotRanking';
move.bjk3_openList=move.server+'/api/bjK3_openList';
move.bjk3_latest=move.server+'/api/bjK3_latestLottery';

move.gxk10_currentTime=move.server+'/api/gxK10_currentTime';
move.gxk10_betPanel=move.server+'/api/gxK10_betPanel';
move.gxk10_bet=move.server+'/api/gxK10_bet';
move.gxk10_trend=move.server+'/api/gxK10_trend';
move.gxk10_hotRanking=move.server+'/api/gxK10_hotRanking';
move.gxk10_openList=move.server+'/api/gxK10_openList';
move.gxk10_latestLottery=move.server+'/api/gxK10_latestLottery';

move.cqk10_currentTime=move.server+'/api/cqK10_currentTime';
move.cqk10_betPanel=move.server+'/api/cqK10_betPanel';
move.cqk10_bet=move.server+'/api/cqK10_bet';
move.cqk10_trend=move.server+'/api/cqK10_trend';
move.cqk10_hotRanking=move.server+'/api/cqK10_hotRanking';
move.cqk10_openList=move.server+'/api/cqK10_openList';
move.cqk10_latestLottery=move.server+'/api/cqK10_latestLottery';

move.gd115_currentTime=move.server+'/api/gdPick11_currentTime';
move.gd115_betPanel=move.server+'/api/gdPick11_betPanel';
move.gd115_bet=move.server+'/api/gdPick11_bet';
move.gd115_trend=move.server+'/api/gdPick11_trend';
move.gd115_hotRanking=move.server+'/api/gdPick11_hotRanking';
move.gd115_openList=move.server+'/api/gdPick11_openList';
move.gdPick11_latestLottery=move.server+'/api/gdPick11_latestLottery';

move.sdPick11_currentTime=move.server+'/api/sdPick11_currentTime';
move.sdPick11_betPanel=move.server+'/api/sdPick11_betPanel';
move.sdPick11_bet=move.server+'/api/sdPick11_bet';
move.sdPick11_trend=move.server+'/api/sdPick11_trend';
move.sdPick11_hotRanking=move.server+'/api/sdPick11_hotRanking';
move.sdPick11_openList=move.server+'/api/sdPick11_openList';
move.sdPick11_latestLottery=move.server+'/api/sdPick11_latestLottery';

move.jxPick11_currentTime=move.server+'/api/jxPick11_currentTime';
move.jxPick11_betPanel=move.server+'/api/jxPick11_betPanel';
move.jxPick11_bet=move.server+'/api/jxPick11_bet';
move.jxPick11_trend=move.server+'/api/jxPick11_trend';
move.jxPick11_hotRanking=move.server+'/api/jxPick11_hotRanking';
move.jxPick11_openList=move.server+'/api/jxPick11_openList';
move.jxPick11_latestLottery=move.server+'/api/jxPick11_latestLottery';

move.liuhecai_currentTime=move.server+'/api/markSix_currentTime';
move.liuhecai_betPanel=move.server+'/api/markSix_betPanel';
move.liuhecai_bet=move.server+'/api/markSix_bet';
move.liuhecai_trend=move.server+'/api/markSix_trend';
move.liuhecai_hotRanking=move.server+'/api/markSix_hotRanking';
move.liuhecai_combinaInfo=move.server+'/api/markSix_combinaInfo';
move.liuhecai_openList=move.server+'/api/markSix_openList';
move.liuhecai_latestLottery=move.server+'/api/markSix_latestLottery';

move.sflhc_currentTime=move.server+'/api/sflhc_currentTime';
move.sflhc_betPanel=move.server+'/api/sflhc_betPanel';
move.sflhc_bet=move.server+'/api/sflhc_bet';
move.sflhc_trend=move.server+'/api/sflhc_trend';
move.sflhc_hotRanking=move.server+'/api/sflhc_hotRanking';
move.sflhc_combinaInfo=move.server+'/api/sflhc_combinaInfo';
move.sflhc_openList=move.server+'/api/sflhc_openList';
move.sflhc_latestLottery=move.server+'/api/sflhc_latestLottery';

/**平面宽度*/
move.width=$(window).width();

/**获得登录用户信息*/
move.user={
	uid:null,
	u:null,
	logo:null,
	longAlt:null,
	exp:null,//过期时间
	time:null,//存入时间
	points:0,//积分
	isGuest:1,
	loginName:null
}

/**
 * 获得get请求的URL参数
 * @param name 参数名
 * @returns
 */
move.getParameter=function(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return decodeURIComponent(r[2]); return null; 
}

move.createLoading=function() {
	var mask = {};
	mask.show=function(){
		var loading=$(".mask").html();
		if(loading){
			$(".mask").show();
		}else{
			var width=$(window).width();//宽度
			var widthLoading=width*0.4;//loading宽度
			var height=$(window).height();//高度
			//$('body').append('<div class="masktrans"></div> <div class="mask"><div class="loading"><div class=" loader-inner line-spin-fade-loader"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div></div>');
			$('body').append('<div class="masktrans"></div><div class="mask"><div class="loading"><div class=\"loading-spinner\"><div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div></div></div></div></div>');
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

/**
 * 打开链接
 * @param vurl
 */
function openCustURL(vurl){
	mui.openWindow({url:vurl,show:{
		autoShow:true,
		aniShow:'slide-in-right',
		duration:400
	},waiting:{authShow:false,title:'正在加载...'}});
}


var baseObj={
	openIndex:function(){
		mui.openWindow({
			url:  move.webpath+'/game/index.html', 
			show:{
				autoShow:false
			}
		});
	},
	openMe:function(){
		mui.openWindow({
			url:  move.webpath+'/game/me.html', 
			show:{
				autoShow:false
			}
		});
	},
	xiazhu:function(){
		mui.openWindow({
			url:  move.webpath+'/game/xiazhu.html', 
			show:{
				autoShow:false
			}
		});
	},
	open:function(){
		mui.openWindow({
			url:  move.webpath+'/game/kaijiang.html', 
			show:{
				autoShow:false
			}
		});
	},
	openLogin:function(){
		mui.openWindow({
			url: move.webpath+'/game/login.html', 
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

var desKey="P29lMhJ8";
function encryptByDES(message) {
    var keyHex = CryptoJS.enc.Utf8.parse(desKey);
    var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}
var starup={
	login:function(){
		starupLogin();
		return false;
	},
	home:function(){
		starupHome();
		return false;
	},
	userHome:function(name){
		starupUserHome(name);
		return false;
	},
	commentList:function(articleId,id){
		starupCommentList(articleId,id);
		return false;
	},
	video:function(url){
		starupVideo(url);
	}
}

var user = {
	init:function(uid,u,longAlt,logo,phone,loginName,userName,money,points,userType){
		move.user.uid=(uid!=null)?uid:null;
		move.user.u=(u!=null)?u:null;
		move.user.longAlt=(longAlt!=null)?longAlt:null;
		move.user.logo=(logo!=null)?logo:null;
		move.user.phone=(phone!=null)?phone:null;
		move.user.loginName=(loginName!=null)?loginName:null;
		move.user.userName=(userName)?userName:null;
		move.user.exp=move.storageTime;
		move.user.time=new Date().getTime();
		move.user.money=money;
		move.user.points=points;
		move.user.userType=userType;
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
	},
//	logout:function(){//登出
//
//		mui.confirm('确认退出？', '提示', ["确认","取消"], function(e) {
//	        if (e.index == 0) {
//	            if(move.user!=null){
//					var map = {};
//					map['u'] =move.user.u;
//					var mw=baseObj.mw(map);
//					$.ajax({
//					    type: "POST",
//					    url:move.logout,
//					    dataType:'json',
//					    data:{
//					    	mw:mw
//					    },  
//					    success: function(ret){
//					    	var code=ret.error_no;
//					    	if(code=='200'){
//					    		user.clear();
//					    	}else{
//					    		user.clear();
//					    	}
//					    	window.location.href=move.webpath+"/login.html";
//					    	// baseObj.openIndex();
//					    },
//				        error: function (jqXHR, textStatus, errorThrown) {
//				        	user.clear();
//				        	// baseObj.openIndex();
//				        }
//					});
//				}else{
//					user.clear();
//					baseObj.openIndex();
//				}
//	        } else {
//	            
//	        }
//	    })
//		
//	}
};
if(move.user.u==null){
	var obj=user.get();
	if(obj!=null){
		move.user=user.get();
	}
}
var timerxiazhu=null;
var timeropen=null;
var timeropenresult=null;
var timerObj={
	interval:1000,
	intervalAPI:3000,
	rest:null,//下注倒计时
	openrest:null,//开奖倒计时剩余秒数
	init:function(second,source){//初始化，封盘倒计时
		second--;
		var day1=0; 
//		var day1=Math.floor(second/(60*60*24)); 
		var hour=Math.floor((second-day1*24*60*60)/3600); 
		var minute=Math.floor((second-day1*24*60*60-hour*3600)/60); 
		var second=Math.floor(second-day1*24*60*60-hour*3600-minute*60); 
		if(hour>0){
			$("#countDownDiv").removeClass("b-top-tips-time");
			$("#countDownDiv").addClass("b-top-tips-time-small");
			$("#hour").show();
			if($("#hour").next().text()=="时"){

			}else{
				$("#hour").after("<b>时</b>");
			}
			
		}else{
			$("#countDownDiv").addClass("b-top-tips-time");
			$("#countDownDiv").removeClass("b-top-tips-time-small");
			$("#hour").hide();
		}
		hour = this.checkTime(hour); 
		minute = this.checkTime(minute); 
		second = this.checkTime(second); 
		if(timerObj.rest>=0){
			if(timerObj.rest>0){
				$('#hour').html(hour);
				$('#minute').html(minute);
				$('#sec').html(second);
				timerObj.rest=timerObj.rest-1;
			}
		}
		console.log("timerObj.rest::"+timerObj.rest+"____s::"+source);
		if(timerObj.rest==0){
			timerObj.rest=null;
			window.clearInterval(timerxiazhu);
			timerxiazhu=null;
			if(source=='timer-bj'){
				console.warn('此处调用北京赛车封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-bj3'){
				console.warn('此处调用北京三分彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-bjpcdd'){
				console.warn('此处调用北京PC蛋蛋封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-xjp28'){
				console.warn('此处调用新加坡28封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-cqssc'){
				console.warn('此处调用重庆时时彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-xjssc'){
				console.warn('此处调用新疆时时彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-tjssc'){
				console.warn('此处调用天津时时彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-gdk10'){
				console.warn('此处调用广东快乐十分封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-kuailepuke3'){
				console.warn('此处调用快乐扑克3封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-jiangsuk3'){
				console.warn('此处调用江苏快3封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-bjk3'){
				console.warn('此处调用北京快3封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-gxk10'){
				console.warn('此处调用广西快乐十分封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-gd115'){
				console.warn('此处调用广东11选5封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-liuhecai'){
				console.warn('此处调用六合彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-sfpk10'){
				console.warn('此处调用急速赛车封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-sfpk102'){
				console.warn('此处调用三分pk10封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-jsft'){
				console.warn('此处调用极速飞艇封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-jx115'){
				console.warn('此处调用江西11选5封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-sd115'){
				console.warn('此处调用山东11选5封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-cqk10'){
				console.warn('此处调用重庆快乐十分封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-sflhc'){
				console.warn('此处调用三分六合彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-five'){
				console.warn('此处调用五分彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-bjSsc'){
				console.warn('此处调用北京时时彩封盘方法');
				lotteryObj.seal();
			}else if(source=='timer-xyft'){
				console.warn('此处调用幸运飞艇封盘方法');
				lotteryObj.seal();
			}
		}
	},
	open:function(second,source){//开奖倒计时
		var day1=Math.floor(second/(60*60*24)); 
		var hour=Math.floor((second-day1*24*60*60)/3600); 
		var minute=Math.floor((second-day1*24*60*60-hour*3600)/60); 
		var second=Math.floor(second-day1*24*60*60-hour*3600-minute*60); 
		hour = this.checkTime(hour); 
		minute = this.checkTime(minute); 
		second = this.checkTime(second); 
		if(timerObj.openrest>=0){
			if(timerObj.openrest>0){
				// $('#open-date #day').html(day1);
				// $('#hour').html(hour);
				$('#open-date #minute').html(minute);
				$('#open-date #sec').html(second);
			}
		}
		if(timerObj.openrest>0){
			timerObj.openrest=timerObj.openrest-1;
		}

		console.log("timerObj.openrest::"+timerObj.openrest+"____s::"+source);
		if(timerObj.openrest==0){
			timerObj.openrest=null;
			window.clearInterval(timeropen);
			timeropen=null;
			if(source=='timer-bjopen'){
				console.warn('此处调用北京赛车开奖方法');
				lotteryObj.open();
			}else if(source=='timer-bj3open'){
				console.warn('此处调用北京三分彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-bjpcddopen'){
				console.warn('此处调用北京PC蛋蛋开奖方法');
				lotteryObj.open();
			}else if(source=='timer-xjp28open'){
				console.warn('此处调用新加坡28开奖方法');
				lotteryObj.open();
			}else if(source=='timer-cqsscopen'){
				console.warn('此处调用重庆时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-xjsscopen'){
				console.warn('此处调用新疆时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-tjsscopen'){
				console.warn('此处调用天津时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-gdk10open'){
				console.warn('此处调用广东快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-kuailepuke3open'){
				console.warn('此处调用快乐扑克3开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jiangsuk3open'){
				console.warn('此处调用江苏快3开奖方法');
				lotteryObj.open();
			}else if(source=='timer-bjk3open'){
				console.warn('此处调用北京快3开奖方法');
				lotteryObj.open();
			}else if(source=='timer-gxk10open'){
				console.warn('此处调用广西快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-gd115open'){
				console.warn('此处调用广东11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-liuhecaiopen'){
				console.warn('此处调用六合彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sfpk10open'){
				console.warn('此处调用急速赛车开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sfpk102open'){
				console.warn('此处调用三分pk10开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jsftopen'){
				console.warn('此处调用极速飞艇开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jx115open'){
				console.warn('此处调用江西11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sd115open'){
				console.warn('此处调用山东11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-cqk10open'){
				console.warn('此处调用重庆快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sflhcopen'){
				console.warn('此处调用三分六合彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-fiveopen'){
				console.warn('此处调用五分彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-bjSscopen'){
				console.warn('此处调用北京时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-xyftopen'){
				console.warn('此处调用幸运飞艇开奖方法');
				lotteryObj.open();
			}
		}
	},
	result:function(source){
		console.warn('循环请求开奖接口');
		if(source=='timer-bjopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-bj3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-bjpcddopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-xjp28open'){
			lotteryObj.open(-1);
		}else if(source=='timer-cqsscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-xjsscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-tjsscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-gdk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-kuailepuke3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-jiangsuk3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-bjk3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-gxk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-gd115open'){
			lotteryObj.open(-1);
		}else if(source=='timer-liuhecaiopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-sfpk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-sfpk102open'){
			lotteryObj.open(-1);
		}else if(source=='timer-jsftopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-jx115open'){
			lotteryObj.open(-1);
		}else if(source=='timer-sd115open'){
			lotteryObj.open(-1);
		}else if(source=='timer-cqk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-sflhcopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-fiveopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-bjSscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-xyftopen'){
			lotteryObj.open(-1);
		}
	},
	checkTime:function(i){
		//将0-9的数字前面加上0，例1变为01 
		if(i<10){ 
			 i = "0" + i; 
		} 
		return i; 
	}
}

move.hideHeader=function(){
	$('.main-content').css("min-height",$(window).height());
	var header=$('header.mui-bar');
	if(header){
		if(!move.isWechat()){//微信
			$(header).before('<div class="margin-top-header"></div>');
			$('.bxs-pages').css('margin-top','44px');
		}else{
			$(header).hide();
			$('div.margin-top-header').hide();
		}
		var plat=move.getParameter('plat');
		if(plat=='1'||plat=='2'){//1.ios 2.安卓
			$(header).hide();
			$('div.margin-top-header').hide();
		}
	}
}
function bettingObj(optionId,betPoints){
	var obj=new Object(); 
	obj.id=optionId; 
	obj.p=betPoints; 
	return obj; 
}
move.doLogin=function(){
	var loginName=$("input[name='loginName']").val();
	var password=$("input[name='password']").val();
	var imgCode=$("input[name='imgCode']").val();
	
	if(!move.widget.isPhone(loginName)){
	    mui.alert('请输入有效的手机号码',document.title, function() {});
	    return;
	}
    if ($.trim(password)==''){
    	mui.alert('请输入密码',document.title, function() {});
    	return;
    }
    if($.trim(imgCode)==''){
    	mui.alert('请输入验证码',document.title, function() {});
    	return;
    }
    var map = {};
    map['loginName'] =loginName;
    map['password'] =hex_md5(password).toUpperCase();
    map['machineType'] ='3';
    map['imgCode'] =imgCode;
	var mw=baseObj.mw(map);
	var mask=move.createLoading();
	mask.show();
    $.ajax({
		type: "POST",
		url:move.login,
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
				var obj=result.obj;
				var logo=obj.logo;
				var uid=obj.uid;
				var u=obj.u;
				var loginName=obj.loginName;
				var cellPhone=obj.cellPhone;
				var userName=obj.userName;
				var money=obj.money;
				var point=obj.point;
				var userType=obj.userType;
				user.init(uid,u,'',logo,cellPhone,loginName,userName,money,point,userType);
				user.set(move.user);
				mui.toast('登陆成功',{ duration:'long', type:'div' }); 
				var redirect=move.getParameter('redirect');
				if(redirect){
					baseObj.openView(redirect);
				}else{
					baseObj.openView('index.html');
				}
			}else{
				mui.alert(msg,document.title, function() {});
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			mask.close();
		}
	});
}
move.doLogout=function(){
	$('.user-info-line #logout').bind("click", function(){
		store.remove('user');
//		$('.topline .logined').hide();
//		$('.topline .loginline').show();
		$('.user-info-line').hide();
		$(".flex-line").append('<div class="login-line fr">'
				+'<div class="login">'
				+'<div class="username fl">'
				+'<span class="mui-icon mui-icon-contact icon"></span>'
				+'<input type="text" placeholder="用户名" name="loginName">'
				+'</div>'
				+'<div class="password fl">'
				+'<span class="mui-icon mui-icon-locked icon"></span>'
				+'<input type="password" placeholder="密码" name="password">'
				+'</div>'
				+'<div class="imgCode fl">'
				+'<span class="mui-icon mui-icon-info icon"></span>'
				+'<input type="text" placeholder="验证码" name="imgCode">'
				+'<img src="/api/baseData_checkCode" id="checkCode">'
				+'</div>'
				+'<div class="login-btn-right fl" style="">'
				+'<button type="button" id="loginbtn" class="login-btn btnn fl">登录'
				+'</button>'
				+'<a class="reg-btn btnn btn fl" href="register.html" id="register">注册'
				+'</a>'
				+'</div>'
				+'</div>'
				+'</div>');
		$('.logined').html('<span class="welcome">苹果彩票【pg55.cc】</span>'
				+'<span class="fr"><a rel="nofollow" id="guestReg" href="#">试玩登录</a>|<a rel="nofollow" href="#">在线客服</a>|<a rel="nofollow" href="#" onclick="javascript:move.collection()">加入收藏</a></span>');
		$("#guestReg").bind("click", function(){// 绑定试玩登录点击事件
			loginObj.guestReg();
		});
		$("#loginbtn").bind("click", function(){
			move.doLogin();
		});
		$("#checkCode").bind("click", function(){
			var timestamp = Date.parse(new Date());
			$(this).attr('src','/api/baseData_checkCode?t='+timestamp);
		});
	});
}

move.lotteryDraw=move.server+'/api/baseData_lotteryDraw';
move.popNav = function(){//pop快捷弹出菜单
	if(move.popNaved) return;//已初始化
	if(move.popNavData){//已有数据
		move.popNavBuild();
	}else{//重复请求
		$.ajax({
			type: "POST",
			url:move.lotteryDraw,
			dataType:'json',
			data:{mw:baseObj.mw({})},
			success: function(ret){
				var code=ret.code;
				if(code=='200'){
					move.popNavData = ret.data;
					move.popNavBuild();
					lotteryObj.bindGameGuide();
				}
			},
			error: function (jqXHR, textStatus, errorThrown){}
		});
	}
}
move.popNavBuild = function(){
	if(!move.popNavData || !move.popNavData.items) return;
	var s = "<div class=\"ga-menu-pop\"><div class=\"pop-box\">";
	//xy
	s += "<div class=\"xywrap\"><h3>信用彩票<span class=\"ico-hl\">信</span></h3><ul>";
	for(var i=0;i<move.popNavData.xyItems.length;i++){
		var jo = move.popNavData.xyItems[i];
		s += "<li><a href=\"/trade/xy/#"+jo.gameCode+"\"><img src=\""+jo.img+"\"><span>"+jo.gameName+"</span></a></li>";
	}
	s += "</ul></div>"
	//gf
	s += "<div class=\"gfwrap\"><h3>官方彩票<span class=\"ico-hl\">官</span></h3><ul>";
	for(var i=0;i<move.popNavData.items.length;i++){
		var jo = move.popNavData.items[i];
		s += "<li><a href=\"/trade/"+jo.gameCode+"\"><img src=\""+jo.img+"\"><span>"+jo.gameName+"</span></a></li>";
	}
	s += "</ul></div>";
	s += "</div><div class=\"arrow\"></div></div>";
	move.popNaved = true;
	$($(".menu-box li")[1]).append(s);
	$($(".menu-box li")[1]).hover(function(){
        $(this).oneTime(10,function(){
        	$(".ga-menu-pop").css("left",($(window).width()/2-($(".ga-menu-pop").width()+60)/2)+"px"); 
			$(".ga-menu-pop").fadeIn();
        });
    },function(){
        $(this).oneTime(0,function(){ 
			$(".ga-menu-pop").fadeOut();
       });
    });
    $(".ga-menu-pop").css("left",($(window).width()/2-($(".ga-menu-pop").width()+60)/2)+"px");
}//~


move.doLogout=function(){
	$('.user-info-line #logout').bind("click", function(){
		store.remove('user');
		$('.user-info-line').hide();
		$(".flex-line").append('<div class="login-line fr">'
				+'<div class="login">'
				+'<div class="username fl">'
				+'<span class="mui-icon mui-icon-contact icon"></span>'
				+'<input type="text" placeholder="用户名" name="loginName">'
				+'</div>'
				+'<div class="password fl">'
				+'<span class="mui-icon mui-icon-locked icon"></span>'
				+'<input type="password" placeholder="密码" name="password">'
				+'</div>'
				+'<div class="imgCode fl">'
				+'<span class="mui-icon mui-icon-info icon"></span>'
				+'<input type="text" placeholder="验证码" name="imgCode">'
				+'<img src="/api/baseData_checkCode" id="checkCode">'
				+'</div>'
				+'<div class="login-btn-right fl" style="">'
				+'<button type="button" id="loginbtn" class="login-btn btnn fl">登录'
				+'</button>'
				+'<a class="reg-btn btnn btn fl" href="register.html" id="register">注册'
				+'</a>'
				+'</div>'
				+'</div>'
				+'</div>');
		$('.logined').html('<span class="welcome">苹果彩票【pg55.cc】</span>'
				+'<span class="fr"><a rel="nofollow" id="guestReg" href="#">免费试玩</a>|<a rel="nofollow" href="#">在线客服</a>|<a rel="nofollow" href="#" onclick="javascript:move.collection()">加入收藏</a></span>');
		$("#guestReg").bind("click", function(){// 绑定免费试玩点击事件
			loginObj.guestReg();
		});
		$("#loginbtn").bind("click", function(){
			move.doLogin();
		});
		$("#checkCode").bind("click", function(){
			var timestamp = Date.parse(new Date());
			$(this).attr('src','/api/baseData_checkCode?t='+timestamp);
		});
	});
}

function getmoney(){
	if(move.user.u==null){
		return;
	}
	var map = {};
	map['u'] =move.user.u;
	var mw=baseObj.mw(map);
	$.ajax({
		type: "POST",
		url:move.money,
		dataType:'json',
		data:{
			mw:mw
		},
		success: function(ret){
			var code=ret.code;
			var data=ret.data;
			var msg=ret.msg;
			var html='';
			if(code=='200'){
				var money=data.money;
				var aggBetMoney=data.aggregateBetMoney;
				var points=data.userpoints;
				var logo=data.logo;
				var dayBetMoney=data.dayBetMoney;
				var isAuthentication=data.isAuthentication;
				var isVerifyPhone=data.isVerifyPhone;
				move.user.points=points;
				move.user.money=money;
				
				$('.logined .point').text(points);
				$('.logined .money').text(money);
				store.set('user', move.user);
			}else{
				if(msg.length>0){
					mui.alert(msg,document.title, function() {});
				}
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
		}
	});
}

$(function(e) {
	//game pop nav init
	if(location.pathname!="/trade/") move.popNav();

	$('.nav-line .selebox').html('<span class="selhand">选择彩种<i class="mui-icon mui-icon-bars"></i></span>'
			+'<ul class="kinds" id="kinds">'
			+'<li class="kinds-cell">'
			+'<a href="/trade/xy/#cqssc">'
			+'<span class="czlogo"><img src="/images/column/cqssc.png"></span>'
			+'<span class="kind">重庆时时彩</span>'
			+'<span class="note">5-10分钟一期</span>'
			+'</a>'
			+'</li>'
			+'<li class="kinds-cell">'
			+'<a href="/trade/xy/#bj3">'
			+'<span class="czlogo"><img src="/images/column/bj3.png"></span>'
			+'<span class="kind">北京三分彩</span>'
			+'<span class="note">5-10分钟一期</span>'
			+'</a>'
			+'</li>'
			+'<li class="kinds-cell">'
			+'<a href="/trade/xy/#poker">'
			+'<span class="czlogo"><img src="/images/column/poker.png"></span>'
			+'<span class="kind">快乐扑克3</span>'
			+'<span class="note">5-10分钟一期</span>'
			+'</a>'
			+'</li>'
			+'<li class="kinds-cell">'
			+'<span class="session-type">'
			+'高频彩'
			+'</span>'
			+'<div class="session-box">'
			+'<a href="/trade/xy/#tjssc">'
			+'<p class="session-title">天津时时彩</p>'
			+'</a>'
			+'<a href="/trade/xy/#xjssc">'
			+'<p class="session-title">新疆时时彩</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box">'
			+'<a href="/trade/xy/#bjpk10">'
			+'<p class="session-title">北京赛车</p>'
			+'</a>'
			+'<a href="/trade/xy/#sfpk10">'
			+'<p class="session-title">急速赛车</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/xy/#bjlu28">'
			+'<p class="session-title">pc蛋蛋</p>'
			+'</a>'
			+'<a href="/trade/xy/#xjplu28">'
			+'<p class="session-title">幸运28</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/sfc/">'
			+'<p class="session-title">三分彩</p>'
			+'</a>'
			+'<a href="/trade/wfc/">'
			+'<p class="session-title">五分彩</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/xy/#gdk10">'
			+'<p class="session-title">广东快乐十分</p>'
			+'</a>'
			+'<a href="/trade/xy/#gxk10">'
			+'<p class="session-title">广西快乐十分</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/xy/#marksix">'
			+'<p class="session-title">香港六合彩</p>'
			+'</a>'
			+'</div>'
			+'</li>'
			+'<li class="kinds-cell">'
			+'<span class="session-type yi-xuan-wu">'
			+'11选5'
			+'</span>'
			+'<div class="session-box">'
			+'<a href="/trade/xy/#gd115">'
			+'<p class="session-title">广东11选5</p>'
			+'</a>'
			+'<a href="/trade/jxpick11/">'
			+'<p class="session-title">江西11选5</p>'
			+'</a>'
			+'<a href="/trade/ahpick11/">'
			+'<p class="session-title">安徽11选5</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box">'
			+'<a href="/trade/shpick11/">'
			+'<p class="session-title">上海11选5</p>'
			+'</a>'
			+'<a href="/trade/xy/#sdpick11/">'
			+'<p class="session-title">山东11选5</p>'
			+'</a>'
			+'</div>'
			+'</li>'
			+'<li class="kinds-cell">'
			+'<span class="session-type k-three">'
			+'快3'
			+'</span>'
			+'<div class="session-box">'
			+'<a href="/trade/xy/#jsk3">'
			+'<p class="session-title">江苏快3</p>'
			+'</a>'
			+'<a href="/trade/jxk3/">'
			+'<p class="session-title">江西快3</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box">'
			+'<a href="/trade/ahk3/">'
			+'<p class="session-title">安徽快3</p>'
			+'</a>'
			+'<a href="/trade/bjk3/">'
			+'<p class="session-title">北京快3</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/gxk3/">'
			+'<p class="session-title">广西快3</p>'
			+'</a>'
			+'<a href="/trade/shk3/">'
			+'<p class="session-title">上海快3</p>'
			+'</a>'
			+'</div>'
			+'<div class="session-box other-box">'
			+'<a href="/trade/jlk3/">'
			+'<p class="session-title">吉林快3</p>'
			+'</a>'
			+'<a href="/trade/hubk3/">'
			+'<p class="session-title">湖北快3</p>'
			+'</a>'
			+'</div>'
			+'</li>'
			+'</ul>');
	
	//已登录
	if(move.user.u!=null){
		$('.logined').html('<span class="welcome">苹果彩票【pg55.cc】</span>'
				+'<span class="fr"><a rel="nofollow" href="#">在线客服</a>|<a rel="nofollow" href="#" onclick="javascript:move.collection()">加入收藏</a></span>');
		getmoney();
		var points=move.user.points;
		var money=move.user.money;
		var userName=move.user.userName;
//		$('.logined .user').text(userName);
//		$('.logined .money').text(money);
//		$('.logined .point').text(points);
//		$('.topline .loginline').hide();
//		$('.topline .logined').show();
		
		$('.flex-line').append('<div class="user-info-line">'
				+'<span class="welcome">欢迎您，<a href="/user/" class="user">'+userName+'</a>&nbsp;余额：￥<span class="money">'+money+'</span>元&nbsp;</span>'
				+'<a href="/user/" class="myUser">我的账户</a>'
				+'<a href="/user/deposit" class="recharge">充值</a>'
				+'<a href="/user/withdraw" class="cash">提现</a>'
				+'<a href="/user/record" class="record">投注记录</a>'
				+'<a class="logout" id="logout" href="#">退出</a>'
				+'</div>');
		move.doLogout();
	} else {
		$('.logined').html('<span class="welcome">苹果彩票【pg55.cc】</span>'
				+'<span class="fr"><a rel="nofollow" id="guestReg" href="#">免费试玩</a>|<a rel="nofollow" href="#">在线客服</a>|<a rel="nofollow" href="#" onclick="javascript:move.collection()">加入收藏</a></span>');
		$("#guestReg").bind("click", function(){// 绑定免费试玩点击事件
			loginObj.guestReg();
		});
		$(".flex-line").append('<div class="login-line fr">'
				+'<div class="login">'
				+'<div class="username fl">'
				+'<span class="mui-icon mui-icon-contact icon"></span>'
				+'<input type="text" placeholder="用户名" name="loginName">'
				+'</div>'
				+'<div class="password fl">'
				+'<span class="mui-icon mui-icon-locked icon"></span>'
				+'<input type="password" placeholder="密码" name="password">'
				+'</div>'
				+'<div class="imgCode fl">'
				+'<span class="mui-icon mui-icon-info icon"></span>'
				+'<input type="text" placeholder="验证码" name="imgCode">'
				+'<img src="/api/baseData_checkCode" id="checkCode">'
				+'</div>'
				+'<div class="login-btn-right fl" style="">'
				+'<button type="button" id="loginbtn" class="login-btn btnn fl">登录'
				+'</button>'
				+'<a class="reg-btn btnn btn fl" href="/register.html" id="register">注册'
				+'</a>'
				+'</div>'
				+'</div>'
				+'</div>');
		$("#loginbtn").bind("click", function(){
			move.doLogin();
		});
		$("#checkCode").bind("click", function(){
			var timestamp = Date.parse(new Date());
			$(this).attr('src','/api/baseData_checkCode?t='+timestamp);
		});
	}
});

move.collection = function(){
	var url = window.location;
    var title = document.title;
    try{
		window.external.addFavorite(url,title);
	}catch(e){
		try{
			window.sidebar.addPanel(title,url,"");
		}catch(e){
			alert("抱歉，您所使用的浏览器无法完成此操作。\n\n请使用快捷键Ctrl+D进行添加！");
		}
	}
}

move.updateServer = function(){
	var map = {};
	var mw=baseObj.mw(map);
	var mask=move.createLoading();
	mask.show();
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
				var qqServerUrl=result.qqServerUrl;
				var qqServerAccount=result.qqServerAccount;
				
				var arr = $(".fr a");
				for(var i = 0;i<arr.length;i++){
					var tmp = arr[i];
					if($(tmp).text()=="在线客服"){
						$(tmp).attr("href",qqServerUrl);
						$(tmp).attr("target","_blank");
					}
				}
				
				$(".serverQQ").text("QQ客服："+qqServerAccount);
			}else{
				// mui.alert(msg,document.title, function() {});
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			mask.close();
		}
	});
}
//小工具处理
move.widget = {
	isPhone:function(phone){
		return !/^[1][3,4,5,7,8][0-9]{9}$/.test(phone)?false:true;
	},
	arrHas:function(arr,val){
		if(!arr || !val) return false;
		return $.inArray(val,arr)>-1?true:false;
	},
	fmtPick11OpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=5) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtK3OpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=3) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtTimeHHMMSSObj:function(s){//秒 返回obj.h/m/s
		return {h:(parseInt(s/3600)<10?"0"+parseInt(s/3600):parseInt(s/3600)),
			m:(parseInt((s%3600)/60)<10?"0"+parseInt((s%3600)/60):parseInt((s%3600)/60)),
			s:(parseInt(s%60)<10?"0"+parseInt(s%60):parseInt(s%60))};
	},
	fmtTimeDDHHMMObj:function(s){//秒 返回obj.d/h/m
		return {
			d:(parseInt((s/3600)/24)<10?"0"+parseInt((s/3600)/24):parseInt((s/3600)/24)),
			h:(parseInt(s/3600)>24?((parseInt(s/3600)-24)<10?"0"+(parseInt(s/3600)-24):(parseInt(s/3600)-24)):(parseInt(s/3600)<10?"0"+parseInt(s/3600):parseInt(s/3600))),
			m:(parseInt((s%3600)/60)<10?"0"+parseInt((s%3600)/60):parseInt((s%3600)/60))};
	},
	fmtNumOut:function(number,round){
		var numStr = number+"";
		if(numStr.indexOf(".")>0 && !isNaN(number)){
			if(number==0) return 0;
			if(parseInt(numStr.split(".")[1])==0) return parseInt(number);
			return parseFloat(number).toFixed(round?round:2);
		}
		return number;
	},
	arr2String:function(arr){
		var str = "";
		for(var i=0;i<arr.length;i++){
			if(i>0) str += ",";
			str += arr[i];
		}
		return str;
	},
	arr2StrSpace:function(arr){
		var str = "";
		for(var i=0;i<arr.length;i++){
			if(i>0) str += " ";
			str += arr[i];
		}
		return str;
	},
	arrToString:function(arr){
		var str = "";
		for(var i=0;i<arr.length;i++){
			if(i>0) str += "";
			str += arr[i];
		}
		return str;
	},
	arrRemove:function(arr,index){
		if(!arr || isNaN(index)) return arr;
		arr.splice(index,1);
	}

}
$(function(){
	move.popNav();
	move.updateServer();
});