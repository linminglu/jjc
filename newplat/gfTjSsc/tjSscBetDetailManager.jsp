<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>龙之彩</title>
<%@ include file = "../common/inc_file.jsp"%>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../js/js_z/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/js_z/dialog.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="bread">投注  &raquo; 投注详情</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<td colspan="2" class="bg-blue">
			具体信息
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">期号</th>
		<td>
			<bean:write name="item" property="gaBetDetail.sessionNo" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">ID</th>
		<td>
			<bean:write name="item" property="gaBetDetail.userId" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">用户名</th>
		<td>
			<bean:write name="item" property="user.userName" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">玩法</th>
		<td>
			<bean:write name="item" property="gaBetDetail.playName" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">投注选项</th>
		<td>
			<bean:write name="item" property="gaBetDetail.betName" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">投注名称</th>
		<td>
			<bean:write name="item" property="gaBetDetail.optionTitle" />
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">投注时间</th>
		<td>
			<bean:write name="item" property="gaBetDetail.betTime" format="yyyy-MM-dd HH:mm:ss"/>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">投注金额</th>
		<td>
			<bean:write name="item" property="gaBetDetail.betMoney" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">是否中奖</th>
		<td>
			<c:if test="${item.gaBetDetail.winResult =='0'}">
		    	<strong><font color ="blue">未开奖</font></strong> 
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='1'}">
		    	<strong><font color ="red">中奖</font></strong> 
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='2'}">
		    	<strong><font color ="green">未中奖</font></strong>
		    </c:if>
	        <c:if test="${item.gaBetDetail.winResult =='3'}">
		    	<strong><font color ="purple">打和</font></strong> 
		    </c:if>
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">中奖金额</th>
		<td>
			<bean:write name="item" property="gaBetDetail.winCash" />
      	</td>
    </tr>
     <tr class="tr-color-body">
		<th align="right">赔率</th>
		<td>
			<bean:write name="item" property="gaBetDetail.betRate" />
      	</td>
    </tr>

</table>

<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>
