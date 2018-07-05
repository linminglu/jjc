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
.content .layui-elem-field .layui-field-box .option-cell{float: left;margin: 10px 40px;}
.content .del{font-size: 25px;color: #C10001;position: absolute;cursor: pointer;}
.content fieldset{position: relative;}
.content fieldset img{position: absolute;width: 5%;top: 10px;right: 0;}
.content .layui-colla-title {width: 0px;float: left;background-color: #4E5465;}
.content .layui-colla-title .layui-colla-icon{color: #fff;}
.content .field-title{height: 36px;background-color: #4E5465;padding-top: 6px;color: #fff;}
.content input{width: 200px;height: 30px;line-height: 30px;display: inline;}
.bread .add-match{font-size: 25px;position: absolute;cursor: pointer;color: #5FB878;font-weight: 600;margin-left: 5px;}
.bread span{margin-left: 30px;}
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
							html+='<h2 class="layui-colla-title">';
								html+='';
							html+='</h2>';
							html+='<div class="field-title">';
								html+='局名称：<input type="text" name="title" required value="'+fieldTitle+'" lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
								html+='<i class="layui-icon del">&#xe640;</i>';	
							html+='</div>';
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
									html+='<legend>';
										html+='玩法名称：<input type="text" name="title" required value="'+playTitle+'" lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
										html+='<i class="layui-icon del">&#xe640;</i>';	
									html+='</legend>';
									html+='<div class="layui-field-box">';
										/* html+='<table class="layui-table" lay-skin="nob">';
											html+='</tbody>';	
												html+='<tr>'; */
													/* html+='<td>'; */
														/* html+='<form class="layui-form">'; */
														var optionArr=playObj.optionArr;
														for(var k=0;k<optionArr.length;k++){
															var optionObj=optionArr[k];
															var optionId=optionObj.optionId;// 投注项id
															var optionTitle=optionObj.optionTitle;// 投注项名称
															var rate=optionObj.rate;// 赔率
															html+='<div class="option-cell">';
																html+='投注项名称：<input type="text" name="title" required value="'+optionTitle+'" lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
																html+='赔率：<input type="text" name="title" required value="'+rate+'" lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
																html+='<i class="layui-icon del">&#xe640;</i>';	
															html+='</div>';
															/* if(openStatus==1){//已开奖  
																if(optionTitle==openResult){
																	html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'" checked>';
																}else{
																	html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'" disabled>';
																}
															}else{
																html+='<input type="radio" lay-filter="optionRadio" name="play'+pTId+'" value="'+optionId+'" title="'+optionTitle+' @'+rate+'">';
															} */
														}
														/* html+='</form>'; */
													/* html+='</td>'; */
												/* tml+='</tr>';
											html+='</tbody>';
										html+='</table>'; */
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
	<div class="bread">竞猜管理  &raquo;比赛局
		<span>新增比赛局<i class="layui-icon add-match">&#xe608;</i></span>
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