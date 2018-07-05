<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>    
    <title></title>
    <LINK href="<%//=request.getContextPath() %>/css/main.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="../style/base.css" media="all">
		<link type="text/css" rel="stylesheet" href="../style/navigation.css" media="all">
<script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript">
    function getEggs(obj){
    	var link="<%=request.getContextPath() %>/pce/betSessionAction.do?method=getEggs&bsId="+obj+"&userId="+$("userId").value+"&t="+TLT.getTime();
    	//alert(link);
    	location.href=link;
    }
   
 
    
    
</script>
<script type="text/javascript">
function pageInit(){
	updateLink();
}
function updateLink(){
	var as = document.getElementsByTagName("a");
	if(as){
		var tt = TLT.getTime();
		for(var i=0;i<as.length;i++){
			if(as[i].href && as[i].href.length>5){
				var href = as[i].href;
				as[i].href = href + "&t="+tt;
			}
		}
	}
}
window.onload = pageInit;
</script>
</head>

<body>
<div id="sub-nav">
	<ul id="userFeedList">
		<li><a href="../xiaonei/p.do?method=init&userId=<bean:write name="userId"/>">词霸</a>
		<li><a href="../xiaonei/p.do?method=more&userId=<bean:write name="userId"/>">已练�?</a>
		<li><a href="../pce/betSessionAction.do?method=getBetSessionList&userId=<bean:write name="userId"/>">幸运游戏</a>
		<li class="selected"><a href="#">获取积分</a>
		<li><a target="_blank" href="../pce/help/test.htm?" class="help">词霸说明</a></li>
		<li><a target="_blank" href="../pce/help/game.htm?" class="help">游戏说明</a></li>
	</ul>
</div>

<div id="single-column">

<form>
<input type="hidden" id="userId" value="<bean:write name="userId"/>"/>
<table align="center" border="0" cellpadding="0" cellspacing="0" class="table-list">
    <tr class="tr-head">
    	<th><!--  请完成下面的试题，做对将获得500个金�?-->
    	<input type="button" value="立即获取500个金�?" onclick="getEggs('${bsId }')">
    	</th>
    </tr>
    <tr clas="tr-body">
    	<td><div id="queBox"></div></td>
    </tr>
</table>
<center>

</input>
</form>

</div>
</body>

</html>
