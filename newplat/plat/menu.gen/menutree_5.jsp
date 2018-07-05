<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"> 
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../skins/tree.css" />
<script type="text/javascript">
<!--
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
//add以下代码
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
		<h2 onclick="shmenu(this)">System Management</h2>
		<ul>
			<li><a href="../../myt/mytAction.do?method=initFb" target="fraContent">Publish Management</a></li>
		</ul>
	</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">Offer Management</h2>
		<ul>
			<li><a href="../../myt/mytUserManagerAction.do?method=init" target="fraContent">Order Management</a></li>
			<li><a href="../../myt/mytUserManagerAction.do?method=initXf" target="fraContent">消费管理</a></li>
			<li><a href="../../myt/mytUserManagerAction.do?method=initJs" target="fraContent">订单结算</a></li>
		</ul>
	</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">User Management</h2>
		<ul>
			<li><a href="../../myt/mytUserManagerAction.do?method=collectProduct" target="fraContent">Favorite Offer</a></li>
			<li><a href="../../myt/mytUserManagerAction.do?method=collectSeller" target="fraContent">Favorite Merchant</a></li>
		</ul>
	</div>	
	
</div>
</body>
</html>
