<%@page import="com.apps.Constants"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"> 
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../skins/tree.css" />
<script type="text/javascript">
function shmenu(obj){
	if(!obj) return;
	var parent = obj.parentNode;
	var uls = parent.getElementsByTagName("ul");
	if(uls && uls.length>0){
		var ul = uls[0];
		ul.style.display = (ul.style.display=="none"?"":"none");
	}
}
//-->
//添加以下代码
function initLiMenu(){
	var items = document.getElementsByTagName("a");
	if(!items || items.length<=0) return;
	for(var i=0;i<items.length;i++){
		items[i].onclick = function(){
			var lis = document.getElementsByTagName("a");
			for(var k=0;k<lis.length;k++){
				lis[k].className=(this==lis[k]?"liselected":"")
			}
		}
	}
}
window.onload = function(){
	initLiMenu();
}
</script>
</head>
<body>
<!-- 管理员菜单 -->
<div class="treediv">
	<div class="treebox">
		<h2 onclick="shmenu(this)">用户管理</h2>
		<ul>
			<li><a href="../../user/managerAction.do?method=init&userType=1&category=2" target="fraContent">会员用户</a></li>
<%--			<li><a href="../../agent/agentAction.do?method=tradeDetailList" target="fraContent">资金明细</a></li>--%>
			<li><a href="../../agent/agentAction.do?method=betDetailList" target="fraContent">投注明细</a></li>
			<li><a href="../../agent/agentAction.do?method=agentReport" target="fraContent">代理报表</a></li>
			<li><a href="../../user/managerAction.do?method=modifyPassword" target="fraContent">修改密码</a></li>
		</ul>
	</div>
	
</div>
</body>
</html>
