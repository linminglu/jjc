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
function initgfbjpk10(){
	var value= $("#gfbjpk10SessionNo").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确认初始化吗？期号：'+value)){	
			 $("#load-layout").show();
				$.post("../gfBjPk10/bjPk10Action.do?method=initSessionNo&sessionNo="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								$("#load-layout").hide();
								alert("初始化成功！");	
							}else if(data == "inited"){
								$("#load-layout").hide();
								alert("今天期号已生成！");	
							}else{
								$("#load-layout").hide();
								alert("初始化成功！");	
							}
				});
		}
	}else{
		alert("请输入昨天最后一期期号");
	}
}
function initgfbjk3(){
	 var value= $("#gfbjk3SessionNo").val();
	 if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确认初始化吗？期号：'+value)){	
			 $("#load-layout").show();
			$.post("../gfBjK3/bjK3Action.do?method=initSessionNo&sessionNo="+value, 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
							$("#load-layout").hide();
							alert("初始化成功！");	
						}else if(data == "inited"){
							$("#load-layout").hide();
							alert("今天期号已生成！");	
						}else{
							$("#load-layout").hide();
							alert("初始化失败！");	
						}
			});
		 }
	 }else{
			alert("请输入昨天最后一期期号");
		}
}
function initgfcqssc(){
	 if(confirm('确认初始化吗？')){		
			$("#load-layout").show();
		$.post("../gfCqSsc/cqSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfbjssc(){
	 if(confirm('确认初始化吗？')){		
			$("#load-layout").show();
		$.post("../gfBjSsc/bjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfxjssc(){
	 if(confirm('确认初始化吗？')){		
			$("#load-layout").show();
		$.post("../gfXjSsc/xjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgftjssc(){
	 if(confirm('确认初始化吗？')){		
			$("#load-layout").show();
		$.post("../gfTjSsc/tjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfgdpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfGdPick11/gdPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfjxpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfJxPick11/jxPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfsdpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfSdPick11/sdPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfshpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfShPick11/shPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfahpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfAhPick11/ahPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfjsk3(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfJsK3/jsK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfahk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../gfAhK3/ahK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfgxk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../gfGxK3/gxK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfhubk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../gfHubK3/hubK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfjxk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../gfJxK3/jxK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfjlk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../gfJlK3/jlK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfshk3(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../gfShK3/shK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfgxk10(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../gxK10/gxK10Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfsfc(){
	 if(confirm('确认初始化吗？')){
			$("#load-layout").show();
		$.post("../gfThree/threeAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今年期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}


function initgfwfc(){
	 if(confirm('确认初始化吗？')){
			$("#load-layout").show();
		$.post("../gfFive/fiveAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今年期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfbjkl8(){
	 if(confirm('确认初始化吗？')){
			$("#load-layout").show();
		$.post("../bjkl8/bjKl8Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今年期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initgfsfpk10(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
			$.post("../gfSfPk10/sfPk10Action.do?method=initSessionNo", 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
							$("#load-layout").hide();
							alert("初始化成功！");	
						}else if(data == "inited"){
							$("#load-layout").hide();
							alert("今天期号已生成！");	
						}else{
							$("#load-layout").hide();
							alert("初始化成功！");	
						}
			});
	}
}
function initgfsfpk102(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
			$.post("../gfSfPk102/sfPk102Action.do?method=initSessionNo", 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
							$("#load-layout").hide();
							alert("初始化成功！");	
						}else if(data == "inited"){
							$("#load-layout").hide();
							alert("今天期号已生成！");	
						}else{
							$("#load-layout").hide();
							alert("初始化成功！");	
						}
			});
	}
}
function initgfxyft(){
	 if(confirm('确认初始化吗？')){		
		 	$("#load-layout").show();
			$.post("../gfXyFt/xyFtAction.do?method=initSessionNo", 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
							$("#load-layout").hide();
							alert("初始化成功！");	
						}else if(data == "inited"){
							$("#load-layout").hide();
							alert("今天期号已生成！");	
						}else{
							$("#load-layout").hide();
							alert("初始化成功！");	
						}
			});
	}
}
function initgfjsft(){
	 if(confirm('确认初始化吗？')){		
	 	$("#load-layout").show();
		$.post("../gfJsFt/jsFtAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化成功！");	
					}
		});
	}
}

// 信用
function initxybjkl8(){
	 if(confirm('确认初始化吗？')){
			$("#load-layout").show();
		$.post("../xyBjLu28/bjKl8Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
						$("#load-layout").hide();
						alert("初始化成功！");
					}else if(data == "inited"){
						$("#load-layout").hide();
						alert("今年期号已生成！");	
					}else{
						$("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxybjpk10(){
	var value= $("#xybjpk10SessionNo").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确认初始化吗？期号：'+value)){	
				$("#load-layout").show();
				$.post("../xyBjPk10/bjPk10Action.do?method=initSessionNo&sessionNo="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								$("#load-layout").hide();
								alert("初始化成功！");	
							}else if(data == "inited"){
								$("#load-layout").hide();
								alert("今天期号已生成！");	
							}else{
								$("#load-layout").hide();
								alert("初始化成功！");	
							}
				});
		}
	}else{
		alert("请输入昨天最后一期期号");
	}
}
function initxybjk3(){
	var value= $("#xybjk3SessionNo").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		if(confirm('确认初始化吗？期号：'+value)){	
			$("#load-layout").show();
			$.post("../xyBjK3/bjK3Action.do?method=initSessionNo&sessionNo="+value, 
					function(data) {
						data = $.trim(data);
						if (data == "success") {
			                       $("#load-layout").hide();
							alert("初始化成功！");	
						}else if(data == "inited"){
			                     $("#load-layout").hide();
							alert("今天期号已生成！");	
						}else{
			                     $("#load-layout").hide();
							alert("初始化失败！");	
						}
			});
		}
	}else{
		alert("请输入昨天最后一期期号");
	}
}
function initxybj3(){
	 if(confirm('确认初始化吗？')){
				$("#load-layout").show();
				$.post("../xyBj3/bj3Action.do?method=initSessionNo", 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								$("#load-layout").hide();
								alert("初始化成功！");	
							}else if(data == "inited"){
								$("#load-layout").hide();
								alert("今天期号已生成！");	
							}else{
								$("#load-layout").hide();
								alert("初始化失败！");	
							}
				});
	 }
}
function initxyfive(){
	 if(confirm('确认初始化吗？')){
				$("#load-layout").show();
				$.post("../xyFive/fiveAction.do?method=initSessionNo", 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
								$("#load-layout").hide();
								alert("初始化成功！");	
							}else if(data == "inited"){
								$("#load-layout").hide();
								alert("今天期号已生成！");	
							}else{
								$("#load-layout").hide();
								alert("初始化失败！");	
							}
				});
	 }
}
function initxycqssc(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyCqSsc/cqSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxyxjssc(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyXjSsc/xjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxytjssc(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyTjSsc/tjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxybjssc(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyBjSsc/bjSscAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
                       $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                       $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                       $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxybjlu28(){
	var value= $("#xybjlu28SessionNo").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确认初始化吗？期号：'+value)){	
			 $("#load-layout").show();
				$.post("../xyBjLu28/bjLu28Action.do?method=initSessionNo&sessionNo="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
                              $("#load-layout").hide();

								alert("初始化成功！");	
							}else if(data == "inited"){
		                        $("#load-layout").hide();

								alert("今天期号已生成！");	
							}else{
		                        $("#load-layout").hide();

								alert("初始化成功！");	
							}
				});
		}
	}else{
		alert("请输入昨天最后一期期号");
	}
}
function initxyxjplu28(){
	var value= $("#xyxjplu28SessionNo").val();
	if(value!=null&&value!=""&&value.trim()!=""){
		 if(confirm('确认初始化吗？期号：'+value)){	
			 $("#load-layout").show();
				$.post("../xyXjpLu28/xjpLu28Action.do?method=initSessionNo&sessionNo="+value, 
						function(data) {
							data = $.trim(data);
							if (data == "success") {
	//							location.reload();
	                        $("#load-layout").hide();
								alert("初始化成功！");	
							}else if(data == "inited"){
		                        $("#load-layout").hide();
								alert("今天期号已生成！");	
							}else{
		                        $("#load-layout").hide();
								alert("初始化成功！");	
							}
				});
		}
	}else{
		alert("请输入昨天最后一期期号");
	}
}
function initxygdk10(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyGdK10/gdK10Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxypoker(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyPoker/pokerAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxyjsk3(){
	 if(confirm('确认初始化吗？')){	
		 $("#load-layout").show();
		$.post("../xyJsK3/jsK3Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                        $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxygdpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../xyGdPick11/gdPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxysdpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../xySdPick11/sdPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxyjxpick11(){
	 if(confirm('确认初始化吗？')){		
		 $("#load-layout").show();
		$.post("../xyJxPick11/jxPick11Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxygxk10(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xyGxK10/gxK10Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxycqk10(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xyCqK10/cqK10Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今天期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}

function initxymarksix(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xyMarkSix/markSixAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("今年期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxysflhc(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xySflhc/sflhcAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("期号已生成！");
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxysfpk10(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xySfPk10/sfPk10Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxysfpk102(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xySfPk102/sfPk102Action.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                   $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxyjsft(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xyJsft/jsftAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
function initxyxyft(){
	 if(confirm('确认初始化吗？')){
		 $("#load-layout").show();
		$.post("../xyXyft/xyftAction.do?method=initSessionNo", 
				function(data) {
					data = $.trim(data);
					if (data == "success") {
	                    $("#load-layout").hide();
						alert("初始化成功！");	
					}else if(data == "inited"){
                        $("#load-layout").hide();
						alert("期号已生成！");	
					}else{
                        $("#load-layout").hide();
						alert("初始化失败！");	
					}
		});
	 }
}
</script>
</head>
<body>
<logic:notEmpty name="lotteryCatList">
	<logic:iterate id="items" name="lotteryCatList" indexId="num">
		<c:choose>
			<!-- 需要输入期号的 -->
			<c:when test = "${items.gameType eq '101' || items.gameType eq '151' || items.gameType eq '352' 
				|| items.gameType eq '353' || items.gameType eq '603' || items.gameType eq '653'}">
				<div class="session-line">
					<img src="<bean:write name="items" property="img"/>">
					<span class="game-title"><bean:write name="items" property="gameTitle"/></span>
					<c:if test = "${items.playCate eq '1'}">
						<input class="layui-input" placeholder="昨天最后一期期号" id="gf<bean:write name="items" property="gameCode"/>SessionNo" name="resultNo"  value=""/>
						<input type="button" class="gbutton" onclick="javascript:initgf<bean:write name="items" property="gameCode"/>();" value="初始化"/>
					</c:if>
					<c:if test = "${items.playCate eq '2'}">
						<input class="layui-input" placeholder="昨天最后一期期号" id="xy<bean:write name="items" property="gameCode"/>SessionNo" name="resultNo"  value=""/>
						<input type="button" class="gbutton" onclick="javascript:initxy<bean:write name="items" property="gameCode"/>();" value="初始化"/>
					</c:if>
				</div>
			</c:when>
			<!-- 不需要输入期号的 -->
			<c:otherwise>
				<div class="session-line">
					<img src="<bean:write name="items" property="img"/>">
					<span class="game-title"><bean:write name="items" property="gameTitle"/></span>
					<c:if test = "${items.playCate eq '1'}">
						<input type="button" class="gbutton" onclick="javascript:initgf<bean:write name="items" property="gameCode"/>();" value="初始化"/>
					</c:if>
					<c:if test = "${items.playCate eq '2'}">
						<input type="button" class="gbutton" onclick="javascript:initxy<bean:write name="items" property="gameCode"/>();" value="初始化"/>
					</c:if>
				</div>
			</c:otherwise>
		</c:choose>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="lotteryCatList">
<div class="tbl-no-data">暂无数据！</div>
</logic:empty>
<div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;left:0px;opacity:0.4;background:#000;display:none;">
    <!--放置载入图片层，让载入图片放在大致中心就可以，需要绝对中心的话可以在js中做微调，这里我就忽略-->
    <div style="position:absolute;left:49%;top:200px;width:图片宽度px;height:图片高度px;">
        <img src="../images/loading.gif" />
    </div>
</div>
</body>
</html:html>