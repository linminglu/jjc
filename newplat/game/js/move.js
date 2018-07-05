//if(navigator.userAgent.indexOf("Mobile")==-1) location.replace("/");
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
move.importantNotice=move.server+'/api/baseData_importantNotice';
move.walletNotice=move.server+'/api/baseData_walletNotice';
move.helpList=move.server+'/api/baseData_helpList';
move.latestList=move.server+'/api/baseData_latestList';
move.winList=move.server+'/api/baseData_winList';
move.changePwd=move.server+'/api/user_changePwd';
move.cashPassword=move.server+'/api/user_cashPassword';
move.gameColumn=move.server+'/api/baseData_gameColumn';
move.bankList=move.server+'/api/cash_bankList';
move.cityList=move.server+'/api/baseData_wechatList3';
move.cashSubmit=move.server+'/api/cash_submit';
move.recharge=move.server+'/api/recharge_submit';
move.rechargelist=move.server+'/api/recharge_list';
move.userRechargeWay=move.server+'/api/recharge_userRechargeWay';
move.banklist=move.server+'/api/cash_bankList';
move.rechargeOfflineSubmit=move.server+'/api/recharge_offlineSubmit';
move.rechargeSwitch=move.server+'/api/baseData_rechargeSwitch';
move.rechargelist=move.server+'/api/recharge_list';
move.heroicList=move.server+'/api/user_heroicList';// 英雄榜
move.tradeList=move.server+'/api/cash_tradeList';
move.codeRechargeSubmit=move.server+'/api/recharge_codeRechargeSubmit';
move.messageList=move.server+'/api/baseData_messageList';
move.messageView=move.server+'/api/baseData_messageDetail';


move.login=move.server+'/api/user_login';
move.logout=move.server+'/api/user_logout';
move.sendCode=move.server+'/api/sms_send';
move.reg=move.server+'/api/user_reg';
move.regcc=move.server+'/api/user_regCheck'
move.guestReg=move.server+'/api/user_guestReg';
move.retrievePwd=move.server+'/api/user_retrievePwd';
move.money=move.server+'/api/baseData_money';
move.rechargeDetail=move.server+'/api/recharge_detail';
move.cashList=move.server+'/api/cash_list';
move.betList=move.server+'/api/baseData_betlist';
move.xyBetList=move.server+'/api/baseData_xyBetlist';
move.myMember=move.server+'/api/baseData_apprenticelist';
move.user_recRevenue=move.server+'/api/user_recRevenue';
move.gameOption=move.server+'/api/baseData_gameOption';// 官方采种玩法

move.cqSsc_currentTime=move.server+'/api/gfCqSsc_currentTime';
move.cqSsc_betPanel=move.server+'/api/gfCqSsc_betPanel';
move.cqSSc_bet=move.server+'/api/gfCqSsc_bet';
move.cqSSc_trend=move.server+'/api/gfCqSsc_trend';
move.cqSSc_hotRanking=move.server+'/api/gfCqSsc_hotRanking';
move.cqSSc_openList=move.server+'/api/gfCqSsc_openList';

move.gdK10_currentTime=move.server+'/api/gfGdK10_currentTime';
move.gdK10_betPanel=move.server+'/api/gfGdK10_betPanel';
move.gdK10_bet=move.server+'/api/gfGdK10_bet';
move.gdK10_trend=move.server+'/api/gfGdK10_trend';
move.gdK10_hotRanking=move.server+'/api/gfGdK10_hotRanking';
move.gdK10_openList=move.server+'/api/gfGdK10_openList';

move.lottery_turnTableInfo=move.server+'/api/lottery_turnTableInfo';
move.lottery_turnTable=move.server+'/api/lottery_turnTable';
move.lottery_drawRedPackets=move.server+'/api/lottery_drawRedPackets'
move.promotionList=move.server+'/api/lottery_promotionList';

move.user_signCheck=move.server+'/api/user_signCheck';
move.baseData_sign=move.server+'/api/baseData_sign';
move.blacklist=move.server+'/api/baseData_blacklist';


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
	points:0//积分
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
	mask.show=function(hideMask){
		var loading=$(".mask").html();
		if(loading){
			$(".mask").show();
		}else{
			var width=$(window).width();//宽度
			var widthLoading=width*0.4;//loading宽度
			var height=$(window).height();//高度
			//$('body').append('<div class="masktrans"></div><div class="mask"><div class="loading"><div class=" loader-inner line-spin-fade-loader"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div></div>');
			$('body').append('<div class="masktrans"></div><div class="mask"><div class="loading"><div class=\"loading-spinner\"><div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div></div></div></div></div>');
			if(widthLoading>85){
				widthLoading=85;
			}
			var top=(height/2)-(widthLoading);
			var left=(width/2)-(widthLoading/2);
			$('.loading').css({width:widthLoading,height:widthLoading,marginTop:top,marginLeft:left});
			if(hideMask){
				$(".masktrans").hide();
			}else{
				$(".masktrans").show();
			}
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

move.openViewGuest = function(url){
	if(move.user&&move.user.u){
		if(move.user&&move.user.userType&&move.user.userType=="1"){
			baseObj.openView(url);
		}else{
			mui.alert("体验客户无权限！");
		}
	}else{
		//mui.alert("请先登录!");
		location = "/game/login.html";
	}
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
	openIframeView:function(fUrl,url){
		mui.openWindow({
			url: fUrl+"?"+url, 
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
		    	//var val=encodeURI(map[prop]);
		    	var val=map[prop];
		    	if(map[prop]==null||map[prop]=='null'){
		    		val="";
		    	}
		    	mw=mw+key+"="+val+"&";
		    }
		}
		if(mw.length>1){
			mw=mw.substring(0,mw.length-1);
		}
		//console.log("mw::"+mw);
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
		move.user.userName=(userName!=null)?userName:null;
		move.user.exp=move.storageTime;
		move.user.time=new Date().getTime();
		move.user.money=money;
		move.user.points=points;
		move.user.userType=userType;
		move.user.loginTime = new Date().getTime(); 
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
//		console.log("timerObj.rest::"+timerObj.rest+"____s::"+source);
		if(timerObj.rest==0){
			timerObj.rest=null;
			window.clearInterval(timerxiazhu);
			timerxiazhu=null;
			if(source=='timer-bj'){
				lotteryObj.seal();
			}else if(source=='timer-bj3'){
				lotteryObj.seal();
			}else if(source=='timer-bjpcdd'){
				lotteryObj.seal();
			}else if(source=='timer-xjp28'){
				lotteryObj.seal();
			}else if(source=='timer-cqssc'){
				lotteryObj.seal();
			}else if(source=='timer-gdk10'){
				lotteryObj.seal();
			}else if(source=='timer-tjssc'){
				lotteryObj.seal();
			}else if(source=='timer-xjssc'){
				lotteryObj.seal();
			}else if(source=='timer-kuailepuke3'){
				lotteryObj.seal();
			}else if(source=='timer-jiangsuk3'){
				lotteryObj.seal();
			}else if(source=='timer-gxk10'){
				lotteryObj.seal();
			}else if(source=='timer-gdPick11'){
				lotteryObj.seal();
			}else if(source=='timer-liuhecai'){
				lotteryObj.seal();
			}else if(source=='timer-cqK10'){
				lotteryObj.seal();
			}else if(source=='timer-xyft'){
				lotteryObj.seal();
			}else if(source=='timer-sfpk102'){
				lotteryObj.seal();
			}else if(source=='timer-sflhc'){
				lotteryObj.seal();
			}else if(source=='timer-jxPick11'){
				lotteryObj.seal();
			}else if(source=='timer-sdPick11'){
				lotteryObj.seal();
			}else if(source=='timer-jsft'){
				lotteryObj.seal();
			}else if(source=='timer-five'){
				lotteryObj.seal();
			}else if(source=='timer-bjssc'){
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
		if(timerObj.openrest>0){
			timerObj.openrest=timerObj.openrest-1;
		}
		//console.log("timerObj.openrest::"+timerObj.openrest+"____s::"+source);
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
			}else if(source=='timer-gdk10open'){
				console.warn('此处调用广东快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-tjsscopen'){
				console.warn('此处调用天津时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-xjsscopen'){
				console.warn('此处调用新疆时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-kuailepuke3open'){
				console.warn('此处调用快乐扑克3开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jiangsuk3open'){
				console.warn('此处调用江苏快3开奖方法');
				lotteryObj.open();
			}else if(source=='timer-gxk10open'){
				console.warn('此处调用广西快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-gdPick11open'){
				console.warn('此处调用广东11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-liuhecaiopen'){
				console.warn('此处调用六合彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-cqK10open'){
				console.warn('此处调用重庆快乐十分开奖方法');
				lotteryObj.open();
			}else if(source=='timer-xyftopen'){
				console.warn('此处调用幸运飞艇开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sfpk102open'){
				console.warn('此处调用三分PK10开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jxPick11open'){
				console.warn('此处调用江西11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sdPick11open'){
				console.warn('此处调用山东11选5开奖方法');
				lotteryObj.open();
			}else if(source=='timer-jsftopen'){
				console.warn('此处调用急速飞艇开奖方法');
				lotteryObj.open();
			}else if(source=='timer-fiveopen'){
				console.warn('此处调用五分彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-bjsscopen'){
				console.warn('此处调用北京时时彩开奖方法');
				lotteryObj.open();
			}else if(source=='timer-sflhcopen'){
				console.warn('此处调用急速六合彩开奖方法');
				lotteryObj.open();
			}
		}
	},
	result:function(source){
		//console.warn('循环请求开奖接口');
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
		}else if(source=='timer-gdk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-tjsscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-xjsscopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-kuailepuke3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-jiangsuk3open'){
			lotteryObj.open(-1);
		}else if(source=='timer-gxk10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-gdPick11open'){
			lotteryObj.open(-1);
		}else if(source=='timer-liuhecaiopen'){
			lotteryObj.open(-1)
		}else if(source=='timer-cqK10open'){
			lotteryObj.open(-1);
		}else if(source=='timer-xyftopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-sfpk102open'){
			lotteryObj.open(-1);
		}else if(source=='timer-sflhcopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-jxPick11open'){
			lotteryObj.open(-1);
		}else if(source=='timer-sdPick11open'){
			lotteryObj.open(-1);
		}else if(source=='timer-jsftopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-fiveopen'){
			lotteryObj.open(-1);
		}else if(source=='timer-bjsscopen'){
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
	return;
	$('.main-content').css("min-height",$(window).height()-48);
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

//开奖工具类
move.openUtil = {
	fmtNumStyle:function(numArr,type){//格式化开奖数字样式
		var shtml = "";
		shtml += "<div class=\"open-num-span\">";
		if(!numArr){
			numArr = ["开","奖","中"];
			type = "0";
		}
		if(parseInt(type)>100 && parseInt(type)<200){//pk10
			for(var i=0;i<numArr.length;i++){
				shtml += "<span class=\"pkn-"+parseInt(numArr[i])+"\">"+numArr[i]+"</span>";
			}
			
		}else if(parseInt(type)==309||parseInt(type)==359||parseInt(type)==310||parseInt(type)==360){//lhc
			for(var i=0;i<numArr.length;i++){
				shtml += "<span class=\"lhc"+parseInt(numArr[i])+"\">"+numArr[i]+"</span>";
			}
			//lhc20 
		}else{
			for(var i=0;i<numArr.length;i++){
				shtml += "<span>"+numArr[i]+"</span>";
			}
		}
		shtml += "</div>";
		return shtml;
	},
	fmtCode:function(code,ifGF){

		if(!code) return code;
		if(ifGF) code = "gf"+code.charAt(0).toUpperCase()+code.substr(1,code.length);
		if(code.indexOf("pick11")>=1){
			return code.replace("pick11","").substr(0,code.length)+"Pick11";
		}else if(code.indexOf("k3")>=1){
			return code.replace("k3","").substr(0,code.length)+"K3";
		}else if(code.indexOf("ssc")>=1){
			return code.replace("ssc","").substr(0,code.length)+"Ssc";
		}else if(code.indexOf("pk102")>=1){
			return code.replace("pk102","").substr(0,code.length)+"Pk102";
		}else if(code.indexOf("pk10")>=1){
			return code.replace("pk10","").substr(0,code.length)+"Pk10";
		}else if(code.indexOf("lu28")>=1){
			return code.replace("lu28","").substr(0,code.length)+"Lu28";
		}else if(code=="gfJsft"){
			return "gfJsFt";
		}else if(code=="gfXyft"){
			return "gfXyFt";
		}else if(code.indexOf("ft")>=1){
			return code;
		}else if(code.indexOf("k10")>=1){
			return code.replace("k10","").substr(0,code.length)+"K10";
		}else if(code=="wfc"){
			return "five";
		}else if(code=="gfWfc"){
			return "gfFive";
		}else if(code=="sfc"){
			return "sfc";
		}else if(code=="gfSfc"){
			return "gfThree";
		}else if(code=="marksix"){
			return "markSix";
		}else if(code=="sflhc"){
			return "sflhc"
		}
		return code;
	}
}
//本地存储local/session
move.storage = {
	get:function(key){return localStorage[key];},
	set:function(key,val){localStorage[key]=val;},
	getJSON:function(key){return JSON.parse(localStorage[key]);},
	setJSON:function(key,jsonVal){localStorage[key]=JSON.stringify(jsonVal)},
	getS:function(key){return sessionStorage[key]},
	setS:function(key,val){sessionStorage[key]=val},
	getSJSON:function(key){return JSON.parse(sessionStorage[key]);},
	setSJSON:function(key,jsonVal){sessionStorage[key]=JSON.stringify(jsonVal)}
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
	fmtBjPk10OpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=10) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtSfPk10OpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=10) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtSfPk102OpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=10) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtXyFtOpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=10) return "";
		move.shtml = "";
		for(var i=0;i<arr.length;i++) move.shtml += "<a>"+arr[i]+"</a>";
		return move.shtml;
	},
	fmtJsFtOpenNo:function(arr){//Array String 格式化开奖号html 
		if(!arr || arr.length!=10) return "";
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
	},
	getQueryString:function(key){
		var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
	    var r = window.location.search.substr(1).match(reg);
	    if(r) return  unescape(r[2]); return null;
	},
	getGameCode(url){
		if(url.indexOf("/xy/")>-1 || url.indexOf("/gf/")>-1){
			var arr = url.split("/game/")[1].split("/");
			return arr[0]+arr[1];
		}
	}

}
move.loader = {
	init:function(){
		if(move.loader.currentPage()){
			move.storage.set("lastPage",move.loader.currentPage());
			move.storage.set("currentPage",location.href);
		}else{
			move.storage.set("currentPage",location.href);
		}
	},
	currentPage:function(){
		var page = move.storage.get("currentPage");
		return page?page:"";
	},
	lastPage:function(){
		var page = move.storage.get("lastPage");
		return page?page:"";
	},
	get:function(url){//test
		$.ajax({
			type: "POST",
			url:url,
			dataType:'html',
			data:{},
			success: function(ret){
				$("html").html(ret.toString());
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	}
}
move.updateHelper = function(){
	//var mask=move.createLoading();
	//mask.show(true);
    $.ajax({
		type:"POST",
		url:move.helpList,
		dataType:'json',
		data:{
			mw:baseObj.mw({})
		},
		success: function(ret){
			var code=ret.code;
			var msg=ret.msg;
			if(code=='200'){
				//客服
				if(ret.data.onlineServerUrl){
					$(".g-kefu-abtn").bind("tap",function(){
						baseObj.openView(ret.data.onlineServerUrl);
					});
				}
				if(ret.data.qqServerUrl){
					$(".g-qqkefu-abtn").bind("tap",function(){
						baseObj.openView(ret.data.qqServerUrl);
					});
				}
				//app下载
				if(ret.data.appDownUrl){
					$(".g-appdown-abtn").bind("tap",function(){
						baseObj.openView(ret.data.appDownUrl);
					});
				}
				//底部帮助
				if($(".short-bottom a").length>0){
					var items = ret.data.items;
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
				}
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//mask.close();
		}
	});
}

function chkBlack(){
	if(move.user.u){
		var map = {};
		map['u'] =move.user.u;
		var mw=baseObj.mw(map);
		$.ajax({
			type: "POST",
			url:move.blacklist,
			dataType:'json',
			data:{
				mw:mw
			},
			success: function(ret){
				var code=ret.code;
				var msg=ret.msg;
				if(code=='402'){
					user.clear();
					window.clearTimeout(timer10s);
					mui.alert(msg,document.title, function() {baseObj.openIndex();});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			}
		});
	}
}
var timer10s;
$(document).ready(function(){
	timer10s=window.setInterval(function(){chkBlack();},10000);
	
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

     //init method
     move.loader.init();
});
