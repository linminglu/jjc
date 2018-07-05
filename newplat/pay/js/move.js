var move = {};
//分页的页码
move.pageIndex=0;
//缓存存储时间，毫秒
move.storageTime=2592000000;

move.local = location.toString().indexOf("/")>-1?0:1;
move.server="http://"+window.location.host+(move.local?"":"/");
move.webpath=move.local?"":"/";

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
	mask.show=function(){
		var loading=$(".mask").html();
		if(loading){
			$(".mask").show();
		}else{
			var width=$(window).width();//宽度
			var widthLoading=width*0.4;//loading宽度
			var height=$(window).height();//高度
			$('body').append('<div class="masktrans"></div> <div class="mask"><div class="loading"><div class=" loader-inner line-spin-fade-loader"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div></div>');
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
			$(".mask").hide();
			$(".masktrans").hide();
		}
	}
	return mask;
}

/**
 * 获得设备
 * @return 'ios'\'android'\'other'
 */
move.getDevice=function(){
	if(/MicroMessenger/i.test(navigator.userAgent)) { 
		return "wechat";
	}else if(/(bxsios|bxsandroid)/i.test(navigator.userAgent)){
		return 'app';
	}else if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
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

function starupClose(){}
function starupSucc(){}

var starup={
	close:function(){
		try {
			starupClose();
		} catch (e) {
			
		}
		return false;
	},
	succ:function(){
		try {
			starupSucc();
		} catch (e) {
			
		}
		return false;
	}
}

var user = {
	init:function(uid,u,longAlt,logo,phone){
		move.user.uid=(uid!=null)?uid:null;
		move.user.u=(u!=null)?u:null;
		move.user.longAlt=(longAlt!=null)?longAlt:null;
		move.user.logo=(logo!=null)?logo:null;
		move.user.phone=(phone!=null)?phone:null;
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

var timerxiazhu=null;
var timerObj={
	interval:1000,
	intervalAPI:3000,
	rest:null,//下注倒计时
	openrest:null,//开奖倒计时剩余秒数
	init:function(second,source){//初始化，封盘倒计时
		second--;
		var day1=Math.floor(second/(60*60*24)); 
		var hour=Math.floor((second-day1*24*60*60)/3600); 
		var minute=Math.floor((second-day1*24*60*60-hour*3600)/60); 
		var second=Math.floor(second-day1*24*60*60-hour*3600-minute*60); 
		hour = this.checkTime(hour); 
		minute = this.checkTime(minute); 
		second = this.checkTime(second); 
		if(timerObj.rest>=0){
			if(timerObj.rest>0){
				timerObj.rest=timerObj.rest-1;
			}
		}
		console.log("timerObj.rest::"+timerObj.rest+"____s::"+source);
		if(timerObj.rest==0){
			timerObj.rest=null;
			window.clearInterval(timerxiazhu);
			timerxiazhu=null;
			if(source=='timer-succ'){
				console.log("timerObj.rest::"+timerObj.rest+"____s::"+source);
				var device=move.getDevice();
				if(device=='ios'||device=='android'){
					starup.succ();
				}
			}else if(source=='timer-fail'){
				console.log("timerObj.rest::"+timerObj.rest+"____s::"+source);
				var device=move.getDevice();
				if(device=='ios'||device=='android'){
					starup.close();
				}
			}
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
