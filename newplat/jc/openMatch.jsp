<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_pageSetting.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<style type="text/css">
.content .layui-elem-field legend{font-size: 14;}
.content .layui-elem-field .layui-field-box {padding: 0px 15px;}
.content fieldset{position: relative;}
.content fieldset img{position: absolute;width: 5%;top: 10px;right: 0;}
.layui-form-radio {width: 200px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	var mId=getParameter("mId");
	loadParam(mId);
});
function loadParam(mId){
	$.post("/api/jc_jcOption?mId="+mId,{},
		function(result){
			var code=result.code;
			var data=result.data;
			var items=data.items;
			var html='';
			for(var i=0;i<items.length;i++){
				var fieldObj=items[i];
				var fieldTitle=fieldObj.fieldTitle;// 局名称
				html+='<li>';
					html+='<div class="layui-collapse">';
						html+='<div class="layui-colla-item">';
							html+='<h2 class="layui-colla-title">'+fieldTitle+'</h2>';
							html+='<div class="layui-colla-content layui-show">';
							var playArr=fieldObj.playArr;// 局名称
							for(var j=0;j<playArr.length;j++){
								var playObj=playArr[j];
								var pTId=playObj.pTId;// 玩法id
								var playTitle=playObj.playTitle;// 玩法名称
								var openResult=playObj.openResult;// 开奖结果
								var openStatus=playObj.openStatus;// 开奖状态0未开奖1已开奖 
								html+='<fieldset class="layui-elem-field">';
									if(openStatus==1){//已开奖
										html+='<img class="" src="/images/open_prize.png">';
									}
									html+='<legend>'+playTitle+'</legend>';
									html+='<div class="layui-field-box">';
										html+='<table class="layui-table" lay-skin="nob">';
											html+='</tbody>';	
												html+='<tr>';
													html+='<td>';
														html+='<form class="layui-form">';
														var optionArr=playObj.optionArr;
														for(var k=0;k<optionArr.length;k++){
															var optionObj=optionArr[k];
															var optionId=optionObj.optionId;// 投注项id
															var optionTitle=optionObj.optionTitle;// 投注项名称
															var rate=optionObj.rate;// 赔率
															if(openStatus==1){//已开奖  
																if(optionTitle==openResult){
																	html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'" checked>';
																}else{
																	html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'" disabled>';
																}
															}else{
																html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'">';
															}
														}
														html+='</form>';
													html+='</td>';
												html+='</tr>';
												if(openStatus==0){//未开奖  
												html+='<tr>';
													html+='<td>';
													html+='<button class="layui-btn layui-btn-big openPrize" optionId="">开奖</button>';
													html+='</td>';
												html+='<tr>';
												}
											html+='</tbody>';
										html+='</table>';
									html+='</div>';
								html+='</fieldset>';
							}
							html+='</div>';
						html+='</div>';
					html+='</div>';
				html+='</li>';
			}
			$("#dataList").html(html);
			//注意：折叠面板 依赖 element 模块，否则无法进行功能性操作
			layui.use('element');
			layui.use('form', function(){
				var form = layui.form;
				form.on('radio(optionRadio)', function(data){
					var obj=data.elem;
					var optionId=$(obj).val();
					$(obj).parents('.layui-table').find(".openPrize").attr("optionId", optionId);
				});
			});
			//绑定开奖点击事件
			$(".openPrize").bind("click", function(){
				var optionId=$(this).attr("optionId");
				if(optionId){
					open(optionId);
				}else{
					layer.msg('请选择开奖的投注项');
				}
			});
		}, "json");
}
function open(optionId){
	layer.confirm('确认开奖?', function(index){
		$.post("fieldAction.do?method=saveOpen", 
				{
					'oId':optionId
				},
				function(data){
					var code=data.code;
					if(code=='200'){
						layer.open({
							  content: data.msg,
							  yes: function(index, layero){
								  location.reload();
							  }
							});
					}else{
						layer.msg('开奖失败');
					}
			}, "json");
		layer.close(index);
	});    
}
function getParameter(name) {
	var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
	if(reg.test(window.location.href)){
		return unescape(RegExp.$2.replace(/\+/g, " "));
	}
	return "";
}

</script>
</head>
<body>
	<div class="bread">竞猜管理  &raquo;开奖
	</div>
	<div class="content">
		<ul id="dataList">
			<!-- <li>
				<div class="layui-collapse">
					<div class="layui-colla-item">
						<h2 class="layui-colla-title">第一场</h2>
						<div class="layui-colla-content layui-show">
							<fieldset class="layui-elem-field">
								<legend>单局获胜方</legend>
								<div class="layui-field-box">
									<table class="layui-table" lay-skin="nob">
										<tbody>
											<tr>
												<td>
													<form class="layui-form">
														<input type="radio" name="sex" value="RNG" title="RNG">
														<input type="radio" name="sex" value="SKT" title="SKT">
													</form>
												</td>
											</tr>
											<tr>
												<td>
													<button class="layui-btn layui-btn-big">开奖</button>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</li>-->
		</ul>
	</div>
</body>
<script>

</script>
</html:html>