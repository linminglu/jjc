<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户</title>
<c:import url="../common/inc_style.jsp"/>
<script src="../js/js_z/jquery.mailAutoComplete-3.1.js" type="text/javascript"></script>
<%@ include file = "../common/inc_file.jsp"%>

<script type="text/javascript">
function dd(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22331";
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&id=" + rid+ "&bth=" + bth+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://121.199.32.183/myt/smsApp?action=sendCode'+data;
	
}
function zc(){
	var loginName="13778945612";
	var pwd = "1";
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var sendCode  = "605119";
	var sdcode ="605119";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&sendCode=" + sendCode+ "&sdCode=" + sdcode;
	window.location.href='http://192.168.1.65:8090/myt/loginApp?action=reg'+data;
	
}
function rg(){
	var loginName="测试账号 ";
	var pwd = "1";
	var rid = "22358"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&unZh=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://121.199.32.183/myt/loginApp?action=regSe'+data;
	
}
function kt(){
	var loginName="text999900";
	//var pwd = "1";
	var rid = "22228";
	var infoId = "7";
	var data = "&uid=" + rid+ "&id=" + infoId ;
	window.location.href='http://192.168.1.65:8888/getData?action=ktSe'+data;
}
function st(){
	var loginName="text999900";
	//var pwd = "1";
	var rid = "22228";
	var infoId = "12";
	var data = "&uid=" + rid+ "&id=" + infoId ;
	window.location.href='http://192.168.1.65:8888/getData?action=ktPre'+data;
}
function fx(){
	var loginName="text999900";
	//var pwd = "1";
	var rid = "22228";
	var infoId = "12";
	var data = "&uid=" + rid+ "&id=" + infoId ;
	window.location.href='http://192.168.1.65:8888/getData?action=fxList'+data;
}
function kb(){
	var loginName="text999900";
	//var pwd = "1";
	var rid = "22228";
	var data = "&uid=" + rid;
	window.location.href='http://192.168.1.65:8888/hhdl/setData?action=hykSe'+data;
}
function sz(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22351"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=grsz'+data;
	
}
function szSave(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22351"
	var bth = "20130206";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8090/myt/setData?action=grszSe'+data;
	
}
function szse(){
	var loginName="t11";
	var pwd = "1";
	var rid = "22370"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://121.199.32.183/myt/setData?action=grszSe'+data;
	
}
function lb(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22351"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=lbList'+data;
	
}
function yuje(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=urbal'+data;
	
}
function dtlist(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=ktList'+data;
	
}
function next(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var id = "24";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+"&id="+id+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=jfView'+data;
	
}
function fx(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=lxwm'+data;
	
}
function fxse(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var id = "24";
	var city = "葫芦岛市";
	var lnk = "http://www.baidu.com"
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&lnk="+lnk+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/setData?action=scSe'+data;
	
}
function save(){
	var loginName="http://www.baidu.com";
	var pwd = "1";
	var rid = "22370";
	var bth = "2013-02-05";
	var proe = "辽宁省11111";
	var city = "葫芦岛市11111";
	var con = "dddddddddddddddd";
	var job = "IT工程师111111";
	var unZh = "丽丽2323";
	var tx = "200";
	var id = "17";
	var itCode  = "0d6bfc";
	var data = "&lnk=" + loginName+ "&id=" + id + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city="+city + "&unZh="+unZh+ "&job=" + job+ "&itCode=" + itCode+"&tx="+tx;
	window.location.href='http://121.199.32.183/myt/setData?action=scSe'+data;
	
}
function dele(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228";
	var id = "24";
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var con = "dddddddddddddddd"
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" +"&id="+id+ "&con="+con+city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/setData?action=ktSe'+data;
	
}

function fengye(){
	var loginName="text99990011";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName+ "&pwd=" + pwd + "&bth=" + bth+ "&uid=" + rid+ "&proe=" + proe+ "&city=" + city+ "&job=" + job+ "&itCode=" + itCode;
	window.location.href='http://192.168.1.65:8888/myt/getData?action=myList'+data;
	
}
function yazheng(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22228"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&un=" + loginName;
	window.location.href='http://192.168.1.65:8090/myt/smsApp?action=sendCode'+data;
	
}
function grsz(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22358"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var data = "&uid=" + rid;
	window.location.href='http://192.168.1.65:8090/myt/getData?action=grsz'+data;
	
}
function sclist(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22358"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var id = "33";
	var data = "&uid=" + rid+"&id="+id;
	window.location.href='http://192.168.1.65:8090/myt/getData?action=ktPre'+data;
	
}
function wdList(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22387"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "0d6bfc";
	var id = "33";
	var data = "&uid=" + rid;
	window.location.href='http://192.168.1.65:8090/myt/getData?action=scList'+data;
	
}
function banben(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22388"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "33";
	var data = "&uid=" + rid;
	window.location.href='http://192.168.1.65:8090/myt/setData?action=bbgx'+data;
	
}
function sch(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22333"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "33";
	var pgnm = "3";
	var data = "&uid=" + rid+"&pgnm="+pgnm;
	window.location.href='http://192.168.1.65:8090/myt/getData?action=scList'+data;
	
}
function dd(){
	var loginName="100010369842";
	var pwd = "123456";
	var rid = "22333"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "33";
	var pgnm = "3";
	var data = "&un=" + loginName+"&pwd="+pwd;
	window.location.href='http://192.168.1.65:8888/myt/loginApp?action=login'+data;
	
}
function zc(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22333"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var sendCode  = "58996";
	var id = "33";
	var pgnm = "3";
	var data = "&uid=" + loginName+"&pwd="+pwd+"&sendCode="+sendCode;
	window.location.href='http://192.168.1.65:8888/myt/loginApp?action=reg'+data;
	
}
function yz(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "22333"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "33";
	var pgnm = "3";
	var data = "&un=" + loginName+"&pgnm="+pgnm;
	window.location.href='http://192.168.1.65:8888/myt/smsApp?action=sendCode'+data;
	
}
function sswww(){
	var loginName="15010369842";
	var pwd = "58658";
	var rePwd = "58658"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "22285";
	var pgnm = "3";
	var data = "&pwd=" + pwd+"&rePwd="+rePwd+"&uid="+id;
	window.location.href='http://mayituan.boguyuan.com/myt/loginApp?action=pwd'+data;
	
}
function lexing(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "1"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "22286";
	var pgnm = "1";
	var type = "2";
	var con = "很好";
	var star = "2.5";
	var data = "&id=" + rid +"&uid="+id+"&type="+type+"&pgnm="+pgnm+"&acode=1"+"&star="+star+"&con="+con;
	window.location.href='http://mayituan.boguyuan.com/myt/reqData?action=viewPro'+data;
	
}
function ceshian(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "3"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "389";
	var uid = "22285";
	var pgnm = "1";
	var con = "想当好很好";
	var star = "2.5";
	var unm ="李长生";
	var zpri ="100.25";
	var type="4";
	var pwd="123456";
	var repwd="123456";
	var ord ="4";
	var moy ="100";
	var data = "&ord="+ord+"&acode=1&id="+id+"&uid="+uid+"&type=3&moy="+moy;
	window.location.href='http://mayituan.boguyuan.com/myt/reqData?action=listOrd'+data;
	
}
function ceshian1(){
	var loginName="15010369842";
	var pwd = "1";
	var rid = "3"
	var bth = "2013-02-06";
	var proe = "辽宁省";
	var city = "葫芦岛市";
	var job = "IT工程师";
	var itCode  = "2.0";
	var id = "389";
	var uid = "22285";
	var pgnm = "1";
	var con = "想当好很好";
	var star = "2.5";
	var unm ="李长生";
	var zpri ="100.25";
	var type="4";
	var pwd="123456";
	var repwd="123456";
	var ord ="4";
	var moy ="100";
	var data = "&ord="+ord+"&acode=1&id="+id+"&uid="+uid+"&type=1&moy="+moy;
	window.location.href='http://mayituan.boguyuan.com/myt/reqData?action=payOrd'+data;
	
}

</script>
</head>
<body>
<div align="center" style="margin-top: 200px;">
<table >
<tr>
<td>
	<ul>
	<li>
		<input type="button" class="greenBtn_" style="margin-top: 10px;" value="测试dd按钮" onclick="ceshian1();"/></li>
		<input type="button" class="greenBtn_" style="margin-top: 10px;" value="测试按钮" onclick="ceshian();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="类型ww" onclick="bendi();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="收藏列表" onclick="sch();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="轮播" onclick="lbss();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="版本号测试" onclick="banben();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="个人设置保存" onclick="szSave();"/></li>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="个人设置" onclick="grsz();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="登录" onclick="dd();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="注册" onclick="zc();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="发送验证码" onclick="yz();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="完善" onclick="rg();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="看图答题" onclick="kt();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="查看试题" onclick="st();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="信息分享" onclick="fx();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="会员卡包" onclick="kb();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="返现" onclick="sz();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="更新设wwww置" onclick="szse();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="分页" onclick="fengye();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="发送验证码" onclick="yazheng();"/></li>
	</ul>
	</td>
	<td>
	<ul>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="查看轮播" onclick="lb();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="账户金额" onclick="yuje();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" style="margin-top: 10px;" value="看图答题" onclick="dtlist();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="下一张" onclick="next();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="分享" onclick="fx();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="分享保存Link" onclick="fxse();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="删除" onclick="dele();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="保存我的收藏" onclick="save();"/></li>
		<li><input type="button" class="greenBtn_" value="我的收藏列表" onclick="wdList();"/></li>
		</br>
		<li><input type="button" class="greenBtn_" value="查询列表" onclick="sclist();"/></li>
	</ul>
	</td>
	</tr>
	</table>
</div>
</body>
</html>
