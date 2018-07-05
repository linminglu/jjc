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
</script>
</head>

<body>
<div class="treehd"><span>管理菜单</span></div>
<div class="treediv">
	#treeData#
	
	<!-- 
	<div class="treehead">Platform</div>
	<div class="treebox">
		<h2 onclick="shmenu(this)">Menu</h2>
		<ul>
			<li><a href="#">sub menu</a></li>		
		</ul>
	</div>
	 -->
</div>
</body>
</html>
