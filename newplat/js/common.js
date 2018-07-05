/**
 * TanLu JS 常用工具集合
 */

var ua = navigator.userAgent.toLowerCase();
var isStrict = document.compatMode == "CSS1Compat";
var isOpera = ua.indexOf("opera") > -1;
var isSafari = (/webkit|khtml/).test(ua);
var isIE = !isOpera && ua.indexOf("msie") > -1;
var isIE7 = !isOpera && ua.indexOf("msie 7") > -1;
var isGecko = !isSafari && ua.indexOf("gecko") > -1;
var isBorderBox = isIE && !isStrict;
var isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1);
var isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1);
var isLinux = (ua.indexOf("linux") != -1);
var isSecure = window.location.href.toLowerCase().indexOf("https") === 0;

String.prototype.trim = function(){//去掉前台空格
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.bytes = function(){
	var arr = this.match(/[^\x00-\xff]/g);
	return this.length+(arr?arr.length:0);
}

var BXS = {
	webContext:"",
	ESTUDY_PREIVEW_LEN:200,
	DIALOG_CLEAR_TIME:2000,
	point_user_register:50,
	point_user_login:5,
	point_user_bug:50,
	point_user_invite:50,
	point_dict_word_remeber:2,
	point_dict_word_write:1,
	point_dict_word_test:1,
	point_dict_word_test_s100:10,
	point_dict_word_test_s90:8,
	point_dict_word_test_s80 :6,
	point_dict_word_test_s60:4,
	point_dict_word_test_sl60:2,
	point_dict_word_review:2,
	getBXSProductCategoryName:function(cateId){
		switch (cateId) {
			case 1:return "甲级";
			case 2:return "六级";
			case 3:return "考研英语";
			case 4:return "BEC";
			case 5:return "雅思";
			case 6:return "托福";
			case 7:return "GRE";
			case 8:return "SAT";
			case 9:return "";
			case 10:return "";
			default:return"";
		}
	},
	getCategoryNameZh:function(categoryId){
		switch (categoryId) {
			case 0:return "其他";
			case 1:return "四级";
			case 2:return "六级";
			case 3:return "考研英语";
			case 4:return "BEC";
			case 5:return "雅思";
			case 6:return "托福";
			case 7:return "GRE";
			case 8:return "SAT";
			case 9:return "";
			case 10:return "";
			case 101:return "新编大学英语(1)";
			case 102:return "新编大学英语(2)";
			case 103:return "新编大学英语(3)";
			case 104:return "新编大学英语(4)";
			case 105:return "21世纪大学英语读写教程一";
			case 106:return "21世纪大学英语读写教程二";
			case 107:return "21世纪大学英语读写教程三";
			case 108:return "21世纪大学英语读写教程四";
			case 109:return "大学英语自学教程上册";
			case 110:return "大学英语自学教程下册";
			case 111:return "大学英语精读(预一)";
			case 112:return "大学英语精读(预二)";
			case 113:return "大学英语精读(第一册)";
			case 114:return "大学英语精读(第二册)";
			case 115:return "大学英语精读(第三册)";
			case 116:return "大学英语精读(第四册)";
			case 117:return "大学英语精读(第五册)";
			case 118:return "大学英语精读(第六册)";
			case 119:return "GMAT";
			case 120:return "专科起点升本科考试大纲";
			case 121:return "成人学位英语三级";
			default:return"";
		}
	},
	getUserPictureOfUserType:function(userType,picUrl,originId){
		if(userType=='7'){
			return "<img width='50' height='50' src='"+picUrl+"'/>";
		}else if(userType=='8'){
			if(originId=='101'){
				return "<img width='50' height='50' src='http://"+(location.href.indexOf("localhost")>0 || location.href.indexOf("192.168.0")>0?"192.168.0.121:8090":"221.122.110.169:8090")+"/pxjd"+picUrl+"'/>";
			}else if(originId=='102'){
				return "<img width='50' height='50' src='http://"+(location.href.indexOf("localhost")>0 || location.href.indexOf("192.168.0")>0?"192.168.0.205:8888":"221.122.110.164:8888")+"/bxs"+picUrl+"'/>";
			}
		}else{
			if(picUrl.indexOf("/bxs")>-1){
				return "<img width='50' height='50' src='"+picUrl+"'/>";
			}else{
				return "<img width='50' height='50' src='/bxs"+picUrl+"'/>";
			}
		}
	}
}

//通用
//function $(s){if(document.getElementById){return eval('document.getElementById("' + s + '")');}else{return eval('document.all.' + s);}}
function $(id) {
	return document.getElementById(id);
//  var elements = new Array();
//
//  for (var i = 0; i < arguments.length; i++) {
//    var element = arguments[i];
//    if (typeof element == 'string')
//      element = document.getElementById(element);
//
//    if (arguments.length == 1)
//      return element;
//
//    elements.push(element);
//  }
//
//  return elements;
}
function s(s){if (!$BXS(s)) return; $BXS(s).style.display = "block";}
function h(s){if (!$BXS(s)) return; $BXS(s).style.display = "none";}
function sh(s){if (!$BXS(s)) return; $BXS(s).style.display = $BXS(s).style.display == "none"?"block":"none";}
function hs(s){if (!$BXS(s)) return; $BXS(s).style.display = $BXS(s).style.display == "block"?"none":"block";}
function vv(s){if (!$BXS(s)) return; $BXS(s).style.visibility = "visible";}
function vh(s){if (!$BXS(s)) return; $BXS(s).style.visibility = "hidden";}

var $$2_ = function(id){//返回指定Id的DOM对象
	if(!id) return null;
	if(!!id) return document.getElementById(id);
}
var $N = function(name){//返回指定name的DOM对象集合
	if(!name) return null;
	if(!!name) return document.getElementsByName(name);
}
var $n = function(name){//返回指定name的DOM对象集合
	if(!name) return null;
	if(!!name){
		var objs = document.getElementsByName(name);
		if(objs && objs.length>0) return objs[0];
	}
	return null;
}
var $T = function(tag,el){//返回指定标签的DOM对象集合
	if(!tag) return null;
	if(!!tag){
		if(el && typeof el == "object"){
			return el.getElementsByTagName(tag);
		}else{
			return document.getElementsByTagName(tag);
		}
	}
}

function ybFormat(pron){
//	0 &#952;
//	1 &#593;
//	2 &#652;
//	3 &#601;
//	4 &#603;
//	5 &#230;
//	6 &#596;
//	7 &#643;
//	8 &#240;
//	9 &#331;
//	
//	; &nbsp;
//	= &#658;
	var pronToHtml=[952,593,652,601,603,230,596,643,240,331];
	var pStr = "";
	for(var i=0;i<pron.length;i++){
		c = pron.charAt(i);
		if(c=='0'||parseInt(c)>0){
			pStr += '&#'+pronToHtml[parseInt(c)]+';';
		}else if(c==';'){
			//pStr += '&#716;'
			pStr += '&nbsp;'
		}else if(c=='='){
			pStr += '&#658;';
		}else{
			pStr += c;
		}
	}
	//alert(pron+" "+pStr);
	return pStr;
}
function openNewWindow(url,names,ww,wh){
	var screenWidth = screen.availWidth;  
 	var screenHeight = screen.availHeight; 
	var args = "width="+ww+",height="+wh+",toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=no";
	var win = window.open(url,(names?names:""),args);
	if(win){
		try {
			win.moveTo(screenWidth/2-ww/2,screenHeight/2-wh/2);
            win.focus();
		} catch (e) {}
    }
}
function openMaxWindow(url,names){
		var screenWidth = screen.availWidth;  
 		var screenHeight = screen.availHeight; 
 		var fulls = "fullscreen=1,scrollbars=1";
 		var args = "toolbar=no, menubar=no, scrollbars=yes,resizable=yes,location=no, status=no"; //width="+1024+",height="+710+",
		//var win = window.open(url,"","top=0,left=0,width="+width+",height="+height+",scrollbars=yes,resizable=yes");
		var win = window.open(url,(names?names:""),args);
		//var win = window.open(url,"",fulls);
		//alert(screenWidth+","+screenHeight);
		if(win){
			try {
				win.moveTo(0,0);
	            win.outerWidth = screenWidth;
	            win.outerHeight = screenHeight;
	            win.resizeTo(screenWidth,screenHeight);
	            win.focus();
			} catch (e) {}
        }
}
function openMaxWindow2(url,names){
	 window.open(url,'fullscreen','fullscreen,scrollbars')
}
function refreshParent(){
	try{window.opener.location.reload(true);}catch(e){}
}
function reSubmitParent(){
	try{window.opener.document.forms[0].submit();}catch(e){}
}
function reloadParent(){
	parent.location.reload(true);
}
function openDownload(url){
	window.open("../attach/attach.do?method=download&url="+url);
}

function openShareItem(id,cate,title,width,height){
	openWindow('../usershare/userShareAction.do?method=iShare&id='+id+'&cate='+cate,width?width:400,height?height:240,title);
}
function openDialog(title,width,height,shtml){
	_openDialog(title,width,height,shtml);
}
function openConfirm(title,width,height,shtml,sfunc){
	_openConfirm(title,width,height,shtml,sfunc);
}
function openPrompt(title,width,height,shtml){
	_openPrompt(title,width,height,shtml);
}
function inviteFriend(type,title,url,width,height){
	openWindow('../invited/invitedAction.do?method=init&type='+type+'&title='+title+"&url="+url,width?width:460,height?height:330,"邀请好友");	
}
function openIframeWindow(url,title,width,height){
	openWindow(url,width?width:460,height?height:330,title);
}
function genHtmlCommentLen(str,maxLen){//general comment length 1000byte
	if(!str) return true;
	if(!maxLen) maxLen = 1000;
	var len = str.bytes();
	if(len>maxLen){
		alert("评论内容超长，请修改！\n\n长度限制：小于1000个字符，当前长度(包括html标签)："+len);
		return false;
	}else{
		return true;
	}
}
function genTextCommentLen(str,maxLen){//general text comment length 1000byte
	if(!str) return true;
	if(!maxLen) maxLen = 1000;
	var len = str.bytes();
	if(len>1000){
		alert("评论内容超长，请修改！\n\n长度限制：小于1000个字符，当前长度："+len);
		return false;
	}else{
		return true;
	}
}
function genHtmlMessageLen(str,maxLen){//general comment length 1000byte
	if(!str) return true;
	if(!maxLen) maxLen = 1000;
	var len = str.bytes();
	if(len>maxLen){
		alert("消息内容超长，请修改！\n\n长度限制：小于1000个字符，当前长度(包括html标签)："+len);
		return false;
	}else{
		return true;
	}
}
function getMessageContentLen(str,maxLen){
	if(!str) return "";
	if(!maxLen) maxLen = 20;
	var len = str.length;
	if(len>maxLen){
		return str.substr(0,20);
	}else{
		return str;
	}
}
function genContentLen(str,maxLen){
	if(!str) return true;
	if(!maxLen) maxLen = 1000;
	var len = str.bytes();
	return (len>maxLen?false:true);
}
function setUserDefPic(img,gender,type){
	var size = 50;
	if(type==0)	size = 120;
	if(type==1) size = 50;
	if(type==2) size = 20;
	alert(img.src);
	img.src = "/images/ui/p"+size+"_"+gender+".gif";
}
function formatTime(time){//格式化时间hh:mm:ss
	var hour = parseInt(time/3600);
	var minute = parseInt((time%3600)/60);
	var second = parseInt(time%60);
	if(minute<10) minute = "0"+minute;
	if(second<10) second = "0"+second;
	if(time<3600){
		return minute+":"+second;
	}
	return hour+":"+minute+":"+second;
}
function chkSubmitBtn(tf){
	var btns = document.getElementsByTagName("INPUT");
	if(btns){
		for(var i=0;i<btns.length;i++){
			if(btns[i].type.toString().toUpperCase()=="SUBMIT") btns[i].disabled=(tf?false:true);
		}
	}
}
function getRadioValue(names){
	if(!names) return "";
	var items = $N(names);
	for(var i=0;i<items.length;i++){
		var a = items[i];
		if(a.checked==true) return a.value;
	}
	return "";
}
var TLT = {
	maxLineStringNum:80,
	createDom:function(tagName,className,id){
		var o = document.createElement(tagName);
		if(id) o.id = id;
		if(className) o.className = className;
		return o;
	},
	elWidth:function(el){
		return el.offsetWidth;
	},
	elHeight:function(el){
		return el.offsetHeight;
	},
	elLeft:function(el){//返回el在文档中的left
		var ol = el.offsetLeft;
		while(el.offsetParent){
			el = el.offsetParent;
			if(el.offsetLeft && el.offsetLeft > 0)
				ol += el.offsetLeft;
		}
		return ol;
	},
	elTop:function(el){//返回el在文档中的left
		var ot = el.offsetTop;
		while(el.offsetParent){
			el = el.offsetParent;
			if(el.offsetTop && el.offsetTop > 0)
				ot += el.offsetTop;
		}
		return ot;
	},
	winSize:function(isScroll){//返回当前window宽和高
		var myWidth = 0, myHeight = 0;
		if( typeof( window.innerWidth ) == 'number' ) {
			//Non-IE
			myWidth = window.innerWidth;
			myHeight = window.innerHeight;
		} else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
			//IE 6+ in 'standards compliant mode'
			myWidth = document.documentElement.clientWidth;
			myHeight = document.documentElement.clientHeight;
		} else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
			//IE 4 compatible
			myWidth = document.body.clientWidth;
			myHeight = document.body.clientHeight;
		}
		//alert(myWidth+","+myHeight);
		if(isScroll){
			if(document.body.scrollHeight && document.body.scrollHeight != "")
				myHeight = document.body.scrollHeight+20;
		}
		return([myWidth,myHeight]);
	},
	scrollTop:function(obj){
		return obj.scrollTop;
	},
	getTime:function(){
		var d = new Date();
		return d.getTime();
	},
	jsHtml:function(val){
		var s = "";
        if(!val || val.length == 0) return "";   
//        s = val.replace(/&/g,"&gt;");   
//        s = s.replace(/</g,"&lt;");   
//        s = s.replace(/>/g,"&gt;");   
//        s = s.replace(/ /g,"&nbsp;");   
//        s = s.replace(/\'/g,"&#39;");   
//        s = s.replace(/\"/g,"&quot;");   
//        s = s.replace(/\n/g,"<br/>");   
        s = val.replace(/\n/g,"<br/>"); 
        return s;  
	},
	isImage:function(str){
		if(!str) return false;
		if(str.match(/\.(jpg|jpeg|gif|bmp|png)(\?|$)/i) == null){
			return false;
		}
		return true;
	},
	formatFloat:function(src,pos){
		if(src==0) return 0;
		return Math.round(src*Math.pow(10,pos))/Math.pow(10,pos);
	},
	randNum:function(min,max){
		return parseInt(Math.random()*(max-min+1)+min);
	},
	randArray:function(arr,index,num){
		var temp_array = new Array();
	    for(var k=0;k<arr.length;k++){
	    	if(k!==index)
	        	temp_array.push(arr[k]);
	    }
	    var return_array = new Array();
	    for (var i = 0; i<num; i++) {
	        if (temp_array.length>0) {
	            var arrIndex = Math.floor(Math.random()*temp_array.length);
	            return_array[i] = temp_array[arrIndex];
	            temp_array.splice(arrIndex, 1);
	        } else {
	            break;
	        }
	    }
   		return return_array;
	},
	randOrderArray:function(srcArr){
		var len = srcArr.length;
		var arr1 = [];
		for(var i=0;i<len;i++){
			arr1[i] = i;
		}
		var arr2 = [];
		for(var i=0;i<len;i++){
			arr2[i] = arr1.splice(Math.floor(Math.random()*arr1.length),1);
		}
		var returnArr = [];
		for(var i=0;i<len;i++){
			returnArr[i] = srcArr[arr2[i]];
		}
		return returnArr;
	},
	categoryName:function(cateId){
		switch(cateId){
			case 1: return "英语四级";
			case 2: return "英语六级";
			case 3: return "考研英语";
			case 4: return "考研政治";
			default: return "unknown";
		}
	},
	dictCategoryName:function(cateId){
		switch(cateId){
			case 1: return "英语四级词汇";
			case 2: return "英语六级词汇";
			case 3: return "考研词汇";
			case 15: return "标记词汇";
			default: return "unknown";
		}
	},
	getCate:function(cateId){
		if(!cateId) cateId = 0;
		cateId = parseInt(cateId);
		switch (cateId) {
			case 1: return "cet4";
			case 2: return "cet6";
			case 3: return "ky";
			case 4: return "bec";
			case 5: return "ielts";
			case 6: return "toelf";
			case 7: return "gre";
			default: return "unknown";
		}
	},
	getDian:function(n){
		if(!n) n = 6;
		var s = "";
		for(var i=0;i<n;i++) s+=".";
		return s;
	}
}

//window onload object
var Load = {
	loadFunc:[],
	init:function(){
		var len = Load.loadFunc.length;
		for(var i=0;i<len;i++){
			if(Load.loadFunc[i] && typeof(Load.loadFunc[i])=="function")
				Load.loadFunc[i].call();
		}
	},
	add:function(func){
		var index = Load.loadFunc.length;
		Load.loadFunc[index] = func;
	}
}

var popMask = {
	element:undefined,
	mObj:undefined,
	show:function(flag){
		mObj = document.getElementById("maskDiv");
		if(!mObj){
			mObj = document.createElement("DIV");
			mObj.id="maskDiv";
			mObj.className = "mask";
			//mObj.ondblclick = function(){popMask.clean();};
			document.body.appendChild(mObj);
		}
		popMask.element = mObj;
		popMask.element.flag = flag;
		with(mObj.style){
			display = "block";
			width = "100%";
			height = "100%";
		}
		popMask.hideSelect(true);
	},
	clean:function(){
		//mObj.parentNode.removeChild(mObj);
		popMask.hideSelect(false);
		if(mObj){
			with(mObj.style){
				display = "none";
			}
		}
	},
	hideSelect:function(tf){
		var sels = document.getElementsByTagName("select");
		if(sels){
			for(var i=0;i<sels.length;i++){
				with(sels[i].style){
					visibility = (tf?"hidden":"visible");
				}
			}
		}
	}
};

//消息提示框
//mode->{type:0,call:[Func],val:[txt],timer:[10,0]}//alert
//mode->{type:1,call:[Func1,Func2],val:[txt1,txt2],timer:[10,1]}//confirm
//mode->{type:2,html:html}//customize
var popAlert = {
	element:undefined,
	msgObj:undefined,
	insTimer:undefined,
	btnVal:{ok:"确定",cansel:"取消"},
	init:function(info,mode,flag){
		var headBar,infoBar,commBar;
		if(typeof msgObj != "object"){
			msgObj = document.createElement("div");
			msgObj.className = "popalert";
			document.body.appendChild(msgObj);
			headBar = document.createElement("div");
			infoBar = document.createElement("div");
			commBar = document.createElement("div");
			headBar.className = "headbar";
			headBar.appendChild(document.createTextNode("提示"));
			headBar.appendChild(document.createElement("span"));
			infoBar.className = "infobar";
			commBar.className = "commbar";
			msgObj.appendChild(headBar);
			msgObj.appendChild(infoBar);
			msgObj.appendChild(commBar);
		}else{
			headBar = msgObj.children[0];
			infoBar = msgObj.children[1];
			commBar = msgObj.children[2];
		}
		popAlert.element = msgObj;
		popAlert.element.flag = flag;
		infoBar.innerHTML = "";
		commBar.innerHTML = "";
		infoBar.appendChild(document.createTextNode(info));
		if(mode.type==0){
			var btn = document.createElement("a");
			btn.appendChild(document.createTextNode((mode.val && mode.val[0]?mode.val[0]:popAlert.btnVal.ok)));
			btn.onclick = mode.call[0];
			commBar.appendChild(btn);
		}else if(mode.type==1){
			var okBtn = document.createElement("a");
			okBtn.onmouseover = function(){this.style.background="#666";this.style.color="#fff"};
			okBtn.onmouseout = function(){this.style.background="#eee";this.style.color="#000"};
			var noBtn = document.createElement("a");
			noBtn.onmouseover = function(){this.style.background="#666";this.style.color="#fff"};
			noBtn.onmouseout = function(){this.style.background="#eee";this.style.color="#000"};
			okBtn.appendChild(document.createTextNode((mode.val && mode.val[0]?mode.val[0]:popAlert.btnVal.ok)));
			noBtn.appendChild(document.createTextNode((mode.val && mode.val[1]?mode.val[1]:popAlert.btnVal.cansel)));
			okBtn.onclick = mode.call[0];
			noBtn.onclick = mode.call[1];
			commBar.appendChild(okBtn);
			commBar.appendChild(noBtn);
		}else if(mode.type==2){
			commBar.innerHTMl = mode.html;
		}else{
			return false;
		}
		
		if(mode.timer && typeof(mode.timer) == "object"){
			if(mode.timer[0] > 0 && mode.timer[1] >= 0){
				var actStr = "";
				actStr = (mode.val&&mode.val[mode.timer[1]]?mode.val[mode.timer[1]]:(mode.timer[1]==0?popAlert.btnVal.ok:popAlert.btnVal.cansel));
				popAlert.timer(headBar,mode.timer[0],mode.call[mode.timer[1]],actStr)
			}
		}else{
			headBar.getElementsByTagName("SPAN")[0].innerHTML = "";
		}	
		msgObj.style.display = "";
	},
	clean:function(){
		//msgObj.parentNode.removeChild(msgObj);
		if(msgObj){
			msgObj.style.display = "none";
			window.clearInterval(insTimer);
		}
	},
	timer:function(el,sec,func,actStr){
		var elSpan = el.getElementsByTagName("SPAN")[0];
		var nowSec = parseInt(sec);
		elSpan.innerHTML = "";
		insTimer = window.setInterval(insTiming,1000);
		function insTiming(){
			if(nowSec<0){
				window.clearInterval(insTimer);
				elSpan.innerHTML = "";
				func.call();
				return false;
			}
			elSpan.innerHTML = "&nbsp;[系统将在<b>"+nowSec+"</b>秒后自动<b>"+actStr+"</b>]";
			nowSec--;
		}
	}
};

//弹出box
var popBox = {
	init:function(){
		
	}
}

//bind event
function addEventSimple(obj,evt,fn) {
	if (obj.addEventListener)
		obj.addEventListener(evt,fn,false);
	else if (obj.attachEvent)
		obj.attachEvent('on'+evt,fn);
}

function removeEventSimple(obj,evt,fn) {
	if (obj.removeEventListener)
		obj.removeEventListener(evt,fn,false);
	else if (obj.detachEvent)
		obj.detachEvent('on'+evt,fn);
}

function getFckTextLen(name){//获取FCKEditor内容的长度
	var oEditor = FCKeditorAPI.GetInstance(name) ;
	var oDOM = oEditor.EditorDocument ;
	var iLength ;
	if ( document.all ){
		iLength = oDOM.body.innerText.toString().trim().length ;
	}else{
		var r = oDOM.createRange() ;
		r.selectNodeContents( oDOM.body ) ;
		iLength = r.toString().trim().length ;
	}
	if(!iLength) iLength = 0;
	return iLength
}
function getFckContent(name){
	try {
		var oEditor = FCKeditorAPI.GetInstance(name) ;
		var oDOM = oEditor.EditorDocument ;
		if ( document.all ){
			return oDOM.body.innerText;
		}else{
			var r = oDOM.createRange() ;
			r.selectNodeContents(oDOM.body) ;
			return r.toString();
		}
	} catch (e) {
		return null;
	}
	
}
//function getTextSubstr(str,len){
//	if(!len) len = 80;
//	if(!str) return "";
//	if(str.bytes()<=len) return str;
//	alert(str.bytes()+","+len+":"+str.substr(0,len));
//	return str.substr(0,len)+"...";
//}
function getTextSubstr(str,len){
	if(!len) len=50;
	if(!str || !len) { return ''; }
	//预期计数：中文2字节，英文1字节
	var a = 0;
	//循环计数
	var i = 0;
	//临时字串
	var temp = '';
	for (i=0;i<str.length;i++){
		if (str.charCodeAt(i)>255){
			//按照预期计数增加2
			a+=2;
		}else{
			a++;
		}
		//如果增加计数后长度大于限定长度，就直接返回临时字符串
		if(a > len) { return temp+"..."; }
		//将当前内容加到临时字符串
		temp += str.charAt(i);
	}
	//如果全部是单字节字符，就直接返回源字符串
	return str;
}
function getFckInnerHtml(name){
	var oEditor = FCKeditorAPI.GetInstance(name) ;
	return oEditor.EditorDocument.body.innerHTML;
}
function chkString(s){
	if(!s) return false;
	if(s.trim().length<=0) return false;
	return true;
}
function imageLimit(str){
	if(!str || str=="") return false;
	str = str.toLowerCase();
	if(str.match(/\.(jpg|jpeg|gif|bmp|png)(\?|$)/i)==null) return true;
	else return false;
}

function mp3Limit(str){
	if(!str || str=="") return false;
	str = str.toLowerCase();
	if(str.match(/\.(mp3|mp3)(\?|$)/i)==null) return true;
	else return false;
}

var browser = function(){
    var agent = navigator.userAgent.toLowerCase(),
        opera = window.opera,
        browser = {
        /**
         * 检测浏览器是否为IE
         * @name baidu.editor.browser.ie
         * @property    检测浏览器是否为IE
         * @grammar     baidu.editor.browser.ie
         * @return     {Boolean}    返回是否为ie浏览器
         */
        ie		: !!window.ActiveXObject,

        /**
         * 检测浏览器是否为Opera
         * @name baidu.editor.browser.opera
         * @property    检测浏览器是否为Opera
         * @grammar     baidu.editor.browser.opera
         * @return     {Boolean}    返回是否为opera浏览器
         */
        opera	: ( !!opera && opera.version ),

        /**
         * 检测浏览器是否为WebKit内核
         * @name baidu.editor.browser.webkit
         * @property    检测浏览器是否为WebKit内核
         * @grammar     baidu.editor.browser.webkit
         * @return     {Boolean}    返回是否为WebKit内核
         */
        webkit	: ( agent.indexOf( ' applewebkit/' ) > -1 ),

        /**
         * 检查是否为Macintosh系统
         * @name baidu.editor.browser.mac
         * @property    检查是否为Macintosh系统
         * @grammar     baidu.editor.browser.mac
         * @return     {Boolean}    返回是否为Macintosh系统
         */
        mac	: ( agent.indexOf( 'macintosh' ) > -1 ),

        /**
         * 检查浏览器是否为quirks模式
         * @name baidu.editor.browser.quirks
         * @property    检查浏览器是否为quirks模式
         * @grammar     baidu.editor.browser.quirks
         * @return     {Boolean}    返回是否为quirks模式
         */
        quirks : ( document.compatMode == 'BackCompat' )
    };

    /**
     * 检测浏览器是否为Gecko内核，如Firefox
     * @name baidu.editor.browser.gecko
     * @property    检测浏览器是否为Gecko内核
     * @grammar     baidu.editor.browser.gecko
     * @return     {Boolean}    返回是否为Gecko内核
     */
    browser.gecko = ( navigator.product == 'Gecko' && !browser.webkit && !browser.opera );

    var version = 0;

    // Internet Explorer 6.0+
    if ( browser.ie )
    {
        version = parseFloat( agent.match( /msie (\d+)/ )[1] );
        /**
         * 检测浏览器是否为 IE9 模式
         */
        browser.ie9Compat = document.documentMode == 9;
        /**
         * 检测浏览器是否为 IE8 浏览器
         * @name baidu.editor.browser.IE8
         * @property    检测浏览器是否为 IE8 浏览器
         * @grammar     baidu.editor.browser.IE8
         * @return     {Boolean}    返回是否为 IE8 浏览器
         */
        browser.ie8 = !!document.documentMode;

        /**
         * 检测浏览器是否为 IE8 模式
         * @name baidu.editor.browser.ie8Compat
         * @property    检测浏览器是否为 IE8 模式
         * @grammar     baidu.editor.browser.ie8Compat
         * @return     {Boolean}    返回是否为 IE8 模式
         */
        browser.ie8Compat = document.documentMode == 8;

        /**
         * 检测浏览器是否运行在 兼容IE7模式
         * @name baidu.editor.browser.ie7Compat
         * @property    检测浏览器是否为兼容IE7模式
         * @grammar     baidu.editor.browser.ie7Compat
         * @return     {Boolean}    返回是否为兼容IE7模式
         */
        browser.ie7Compat = ( ( version == 7 && !document.documentMode )
                || document.documentMode == 7 );

        /**
         * 检测浏览器是否IE6模式或怪异模式
         * @name baidu.editor.browser.ie6Compat
         * @property    检测浏览器是否IE6 模式或怪异模式
         * @grammar     baidu.editor.browser.ie6Compat
         * @return     {Boolean}    返回是否为IE6 模式或怪异模式
         */
        browser.ie6Compat = ( version < 7 || browser.quirks );

    }

    // Gecko.
    if ( browser.gecko )
    {
        var geckoRelease = agent.match( /rv:([\d\.]+)/ );
        if ( geckoRelease )
        {
            geckoRelease = geckoRelease[1].split( '.' );
            version = geckoRelease[0] * 10000 + ( geckoRelease[1] || 0 ) * 100 + ( geckoRelease[2] || 0 ) * 1;
        }
    }
    /**
     * 检测浏览器是否为chrome
     * @name baidu.editor.browser.chrome
     * @property    检测浏览器是否为chrome
     * @grammar     baidu.editor.browser.chrome
     * @return     {Boolean}    返回是否为chrome浏览器
     */
    if (/chrome\/(\d+\.\d)/i.test(agent)) {
        browser.chrome = + RegExp['\x241'];
    }
    /**
     * 检测浏览器是否为safari
     * @name baidu.editor.browser.safari
     * @property    检测浏览器是否为safari
     * @grammar     baidu.editor.browser.safari
     * @return     {Boolean}    返回是否为safari浏览器
     */
    if(/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(agent) && !/chrome/i.test(agent)){
    	browser.safari = + (RegExp['\x241'] || RegExp['\x242']);
    }


    // Opera 9.50+
    if ( browser.opera )
        version = parseFloat( opera.version() );

    // WebKit 522+ (Safari 3+)
    if ( browser.webkit )
        version = parseFloat( agent.match( / applewebkit\/(\d+)/ )[1] );

    /**
     * 浏览器版本
     *
     * gecko内核浏览器的版本会转换成这样(如 1.9.0.2 -> 10900).
     *
     * webkit内核浏览器版本号使用其build号 (如 522).
     * @name baidu.editor.browser.version
     * @grammar     baidu.editor.browser.version
     * @return     {Boolean}    返回浏览器版本号
     * @example
     * if ( baidu.editor.browser.ie && <b>baidu.editor.browser.version</b> <= 6 )
     *     alert( "Ouch!" );
     */
    browser.version = version;

    /**
     * 是否是兼容模式的浏览器
     * @name baidu.editor.browser.isCompatible
     * @grammar     baidu.editor.browser.isCompatible
     * @return     {Boolean}    返回是否是兼容模式的浏览器
     * @example
     * if ( baidu.editor.browser.isCompatible )
     *     alert( "Your browser is pretty cool!" );
     */
    browser.isCompatible =
        !browser.mobile && (
        ( browser.ie && version >= 6 ) ||
        ( browser.gecko && version >= 10801 ) ||
        ( browser.opera && version >= 9.5 ) ||
        ( browser.air && version >= 1 ) ||
        ( browser.webkit && version >= 522 ) ||
        false );
    return browser;
}();
