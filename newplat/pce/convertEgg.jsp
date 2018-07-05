<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ include file="../common/inc_include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="../css/a.css" type="text/css" media="all">
<link rel="stylesheet" href="../css/aover.css" type="text/css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/moocore.js"></script>
<script type="text/javascript" src="../js/dialog.js"></script>
<script type="text/javascript">
function saveCategory(){
	var userPoints = $("userPoints");
	var userAllPoints = $("userAllPoints");
	if(!userPoints) return;
	if(userPoints.value==""){
		alert("请输入要兑换的数量！");
		userPoints.focus();
		return false;
	}
	if(isNaN(userPoints.value)){
		alert("兑换数量只能是数字，请检查！");
		userPoints.focus();
		return false;
	}
	if(parseInt(userPoints.value)>parseInt(userAllPoints.value)){
		alert("兑换数量超过积分总数！");
		userPoints.focus();
		return false;
	}
	if(confirm("确认兑换？")){
		new Request({
			method: "post", 
			url: "betSessionAction.do?method=convertEggSave",
			data:"userPoints="+$("userPoints").value,
			onRequest:function(){
			},
			onSuccess:function(res){
				if(res=="0"){
					parent.window.location.reload(true)
				}else{
					alert("兑换失败，请检查网络是否正常!");
				}
			}
		}).send(); 
	}
}
</script>
<title>我的资料 - 帮学试</title>
</head>
<body>
<div style="padding-top:10px;">
	<div class="p10">
		<c:if test="${user.userpoints!=null && user.userpoints>0}">
		<div class="f14" style="padding-left:80px;">
			<input type="hidden" id="userAllPoints" value="<c:out value="${user.userpoints}"/>"/>
			<c:if test="${userBet==null || userBet.betNumber==null || userBet.betNumber==0}">
			<div class="p5 red"><strong>玩好运来需要使用积分，你当前积分为0，请用积分兑换！</strong></div>
			</c:if>
			<div class="p5">你当前共有积分：<span class="points-span"><strong><c:out value="${user.userpoints}"/></strong></span></div>
			<div class="p5">兑换的积分数量：<input class="it" id="userPoints" name="userPoints" style="width:3em;"/></div>
			<div class="p5"><span class="red">提示：1积分=10积分</span><div>
			<div class="p5 mt20">
				<input type="button" class="rb11" value="确定" onclick="saveCategory()"/>
				<input type="button" class="gb11" value="取消" onclick="new parent.dialog().reset();"/>
			</div>
		</div>
		</c:if>
		<c:if test="${user.userpoints==null || user.userpoints==0}">
		<div class="f14 tac">
			<div class="p5"><span class="red">你当前积分为0，暂时不能兑换积分，你可以通过<a target="_top" class="sl" href="../dict/wordAction.do?method=init">背单词</a>赚取积分！</span></div>
			<div class="p5 mt20 tac">
				<input type="button" class="rb11" value="确定" onclick="new parent.dialog().reset();"/>
			</div>
		</div>
		</c:if>
			
	</div>
</div>
</body>
</html>
