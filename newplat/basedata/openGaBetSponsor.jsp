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

$(document).ready(function(){

    $('#btn_submit').click(function(){
    	
    	var frm = document.baseDataForm;
	 	if(frm["type"].value.length==0){
	 		alert("请选择游戏类型");
	 		frm["type"].focus();
	 		return false;
	 	}

		var orderNo = $('#orderNo').val();
		if (orderNo == null || orderNo.trim() == "") {
			alert("请输入期号！");
		}else{
			 if(confirm('确实开奖吗？期号：'+orderNo)){	
				 $("#load-layout").show();
					$.post("baseDataAction.do?method=openSessionNo&sessionNo="+orderNo.trim()+"&gameType="+frm["type"].value.trim(), 
							function(data) {
								data = $.trim(data);					
								if (data == "success") {
									alert("开奖成功！");	
									$("#load-layout").hide();
								}else{
									$("#load-layout").hide();
									alert("开奖失败！");
								}
					});
			}
		}
    });
});
</script>
</head>
<body>
<div class="bread">开奖管理  &raquo;手动开奖</div>
<html:form action="/baseDataAction.do?method=saveVer" onsubmit="return formSubmit();" >
<table class="tblist" cellpadding="0" cellspacing="0" border="0">
	<tr class="moon">
		<td>
			<span>游戏类型：
				<html:select property="type" name="baseDataForm" styleClass="sele">
					<html:option value="">请选择</html:option>
					<html:option value="15">三分彩</html:option>
					<html:option value="14">五分彩</html:option>
					<html:option value="13">双色球</html:option>
					<html:option value="3">重庆时时彩</html:option>
					<html:option value="9">广东11选5</html:option>
				</html:select>
			</span>
		
			<span>期号:&nbsp;&nbsp;<input id="orderNo" type="text" maxlength="20" style="width: 240px;"/></span>
    		&nbsp;<input id="btn_submit" type="button" value="开奖" class="gbutton"/>
		</td>
	</tr>
</table>
</html:form>
<div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;left:0px;opacity:0.4;background:#000;display:none;">
    <!--放置载入图片层，让载入图片放在大致中心就可以，需要绝对中心的话可以在js中做微调，这里我就忽略-->
    <div style="position:absolute;left:49%;top:200px;width:图片宽度px;height:图片高度px;">
        <img src="../images/loading.gif" />
    </div>
</div>
</body>
</html:html>