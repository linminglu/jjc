<%@page import="com.apps.Constants"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ram.model.User"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<style type="text/css">
#spandoing{background:red;border-radius:10px;text-align:center;color:#fff;padding:3px;display:inline-block;width:25px;}
#spandoing2{background:red;border-radius:10px;text-align:center;color:#fff;padding:3px;display:inline-block;width:25px;}
#spandoing3{background:red;border-radius:10px;text-align:center;color:#fff;padding:3px;display:inline-block;width:25px;}
#spandoing4{background:red;border-radius:10px;text-align:center;color:#fff;padding:3px;display:inline-block;width:25px;}
#spandoing5{background:red;border-radius:10px;text-align:center;color:#fff;padding:3px;display:inline-block;width:25px;}

</style>
<link href="../css/a.css" rel="stylesheet" type="text/css" media="all"/>
<link href="../css/aover.css" rel="stylesheet" type="text/css" media="all"/>
<script src="../js/js_z/jquery-1.7.2.min.js" type="text/javascript"></script>
</head>
<body>
<%
	String logoTitle = "<img src='../images/ui/logo_learning.gif' border='0'/>";
	int topHeight = 60;
	User user=(User)session.getAttribute("loginUser");
%>
<div class="iframetop" style="background-color:#393D49;">
<table width="100%" height="<%=topHeight%>" border="0" cellpadding="0" cellspacing="0" class="tophead">
	<tr>
		<td width="210" align="center">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td height="<%=topHeight%>" align="center"><h1 style="color:#fff;font-size:22px;">MANAGEMENT</h1></td>
			</tr>
		</table>
		</td>
		<td class="topbody">
		<table width="100%" height="<%=topHeight-10%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="topwel" valign="bottom" align="right">
					<c:if test="${loginUser.userType eq '2' || loginUser.userType eq '4'|| loginUser.userType eq '0'}">
						<span class="icon"></span>在线人数：<span id="onlineNumber">0</span>
		 				<span class="cd">┊</span>
		 				<!-- <span class="icon"></span><a class="sl-hl"  target="fraContent" href="../card/cardAction.do?method=initofflineOrder&status=0">您有<span id="spandoing5">0</span>条新线下订单</a>
		 				<span class="cd">┊</span>
		 				<span class="icon"></span><a class="sl-hl"  target="fraContent" href="../cash/cashAction.do?method=init&status=0">您有<span id="spandoing4">0</span>条新提现</a>
						<span class="cd">┊</span> -->
					</c:if>
					<%if(user!=null){%>
						<!-- <span class="icon iconname"></span> -->
						<a class="sl-hl"  target="fraContent" href="../user/managerAction.do?method=loginUserModify"><%=user.getLoginName() %></a>
						<span class="cd">┊</span>
					<%} %>
					<a class="sl-hl" target="_top" href="../logout.html"><!-- <span class="icon iconquit"></span> -->退出</a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<script type="text/javascript">
function loadOnlineNumber(){
	jQuery.post("../basedata/baseDataAction.do?method=onlineNumber",
		function(ret){
			var code=ret.code;
			var num=ret.data.number;
			if(!num) num = 0;
			jQuery('#onlineNumber').text(num);
		}, "json");
}
function loadNewApplyCash(){
	jQuery.post("../cash/cashAction.do?method=untreated",
		function(data){
			data = jQuery.trim(data);
			var num=Number(data);
			if(typeof num=='number'){
				if(!num) num = 0;
				jQuery('#spandoing4').html(num);
				if(num > 0){
					if(jQuery.browser.msie){//IE
						jQuery('#spandoing4').append('<bgsound src="../res/tip-facebook.wav" autostart="true" loop="1">');					
					}else{
						jQuery('#spandoing4').append('<embed src="../res/tip-facebook.wav" autostart="true" loop="false" hidden="true" >');							
					}
				}
			}
		});
} 
/**
 * 新的线下订单
 */
function loadNewOfflineOrder(){
	jQuery.post("../card/cardAction.do?method=loadNewOfflineOrder",
		function(data){
		    var jsonData = JSON.parse(data);
            var code = jsonData.code;
            var num = 0;
            var unLockNum = 0;
            if(code == "200"){
            	num = Number(jsonData.num);
            	unLockNum = Number(jsonData.unLockNum);
            }
			if(typeof num=='number'){
				if(!num) num = 0;
				jQuery('#spandoing5').html(num);
			}
            
			if(typeof unLockNum == 'number'){
				if(unLockNum && unLockNum > 0){
					if(jQuery.browser.msie){//IE
						jQuery('#spandoing5').append('<bgsound src="../res/tip-facebook.wav" autostart="true" loop="1">');					
					}else{
						jQuery('#spandoing5').append('<embed src="../res/tip-facebook.wav" autostart="true" loop="false" hidden="true" >');							
					}
				}
			}
		});
} 

//先执行一次
loadNewApplyCash();
loadNewOfflineOrder();
loadOnlineNumber();
//定时执行
setInterval(function(){
	loadNewApplyCash();
	loadNewOfflineOrder();
},18000);
setInterval(function(){
	loadOnlineNumber();
},60000);

</script>
</body>
</html>
