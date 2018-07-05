<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<script type="text/javascript" src="/jc/js/mui/m.js?v=hp2018052805"></script>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_weditor.jsp"%>
<script language="javascript">
function baseObj_mw(map){
	//遍历map
	var mw="";
	for(var prop in map){
	    if(map.hasOwnProperty(prop)){
	    	var key=prop;
	    	var val=encodeURI(map[prop]);
	    	if(map[prop]==null||map[prop]=='null'){
	    		val="";
	    	}
	    	mw=mw+key+"="+val+"&";
	    }
	}
	if(mw.length>1){
		mw=mw.substring(0,mw.length-1);
	}
	if(mw!=''){
		mw=encryptByDES(mw);
		//第二次整体编码
		mw=encodeURI(mw);
	}
	return mw;
}
function encryptByDES(message) {
    var keyHex = CryptoJS.enc.Utf8.parse(desKey);
    var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}
var desKey="P29lMhJ8";
$(document).ready(function(){
	
});
layui.use('form', function(){
	var form=layui.form;
	var mId=getParameter("mId");

	loadFirstType(form);
 	loadSecondType(null,form);
 	loadTeam(null,form);
	form.on('select(first-type)', function(data){
		var firstId=data.value;
// 		loadSecondType(firstId);
	});
	form.on('select(second-type)', function(data){
		var secondId=data.value;
// 		loadTeam(secondId);
	});
	
	loadParam(form, mId);
	form.on('submit(formDemo)', function(data){
		submit();
		return false;
  	});
});

function loadParam(form, mId){
	console.log("aa");
	var map={};
	map['mId']=mId;
	$.ajax({
	    type: "POST",
	    url:"/api/jc_getMatch",
	    dataType:'json',
	    data:{
	    	mw:baseObj_mw(map)
	    },
	    success: function(ret){
	    	console.log("bb");
	    	var code=ret.code;
			var data=ret.data;
			if(code=="200"){
				$("#match-title").val(data.obj.title);
				$("#match-sub-title").val(data.obj.subTitle);
				$("#first-type").val(data.obj.tid1);
				$("#second-type").val(data.obj.tid2);
				$("#first-team").val(data.obj.team1Name);
				$("#second-team").val(data.obj.team2Name);
				$("#red-team").val(data.obj.red);
				$("#blue-team").val(data.obj.blue);
				$("#bo-num").val(data.obj.boNum);
				$("#match-type").val(data.obj.type);
				$("#open-time").val(data.obj.openTime);
				$("#match-time").val(data.obj.matchTime);
				$("#rercentage").val(data.obj.rercentage);
				var items = data.obj.items;
				if(items!=null && items!=undefined){
					for (var i=0;i<items.length;i++){
						var field = items[i];
						var pitems = field.pitems;
						if(i==0){
							$("#fId").val(field.fId);
							$("#field-title").val(field.fieldTitle);
							$("#start-date").val(field.startDate);
							$("#end-date").val(field.endDate);
							for(var j=0;j<pitems.length;j++){
								var play = pitems[j];
								var options = play.itemo;
								if(j==0){
									$("#play-title").val(play.playTitle);
									$("#ptId").val(play.pTId);
									
									for(var k=0;k<options.length;k++){
										var option = options[k];
										if(k==0){
											$("#option-title").val(option.optionTitle);
											$("#rate").val(option.rate);
											$("#oId").val(option.oId);
										}else{
											var html='';
											html+='<div class="option-info">';
												html+='<div class="layui-input-block">';
													html+='<input type="hidden" name="oId" value="'+option.oId+'" class="oId">';
													html+='投注项名称11：<input id="" type="text" value="'+option.optionTitle+'" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>';
													html+='赔率：<input id="rate" value="'+option.rate+'" type="text" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>';
													html+='<i class="del option" onclick="del(this,3)">&#xe640;</i>';
												html+='</div>';
											html+='</div>';
											if(k==options.length-1){
												html+='<i class="addopt" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>'
												$(".aopt").remove();
											}
											$(".play-info .option-info:last").after(html);
										}
									}
									
								}else{
									var html='';
									html+='<div class="play-info">';
										html+='<div class="layui-input-block">';
										    html+='<input type="hidden" name="ptId" value="'+play.pTId+'" class="ptId">';
											html+='玩法名称：<input id="" type="text" value="'+play.playTitle+'" required lay-verify="required|text" placeholder="请输入玩法名称" class="layui-input play-title"/>';
											html+='<i class="layui-icon del" onclick="del(this,2)">&#xe640;</i>';
											if(j==pitems.length-1){
												html+='<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>';
											}
											$(".layui-field-box .play-info:first .layui-icon").remove();
										html+='</div>';
										
										var options = play.itemo;
										for(var a=0;a<options.length;a++){
											var option = options[a];
											html+='<div class="option-info">'+
											'<div class="layui-input-block">'+
											'<input type="hidden" name="oId" value="'+option.oId+'" class="oId">'+
											'投注项名称：<input id="" type="text" value="'+option.optionTitle+'" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>'+
											'赔率：<input id="" type="text" required value="'+option.rate+'" lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>'
											if(a!=0){
												html+='<i class="layui-icon del" onclick="del(this,3)">&#xe640;</i>';
											}
											html+='</div>'+
											'</div>'
											if(a==options.length-1){
												html+='<i class="layui-icon" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>'
											}
										}
										html+='</div>'
										$(".layui-field-box .play-info:last").after(html);
								}
							}
						}else{
							var html='<div class="match-field-line">'+
							'<fieldset class="layui-elem-field">'+
							'<legend style="font-size: 14px;">比赛场次<i class="layui-icon del" onclick="del(this,3)">&#xe640;</i></legend>'+
							'<div class="layui-field-box">'+
								'<div class="field-info">'+
									   '<div class="layui-input-block">'+
									   '<input type="hidden" name="fId" value="'+field.fId+'" class="fId">'+
										'场次名称：<input id="" type="text" value="'+field.fieldTitle+'" required lay-verify="required|text" placeholder="请输入场次名称" class="layui-input field-title"/>'+
									   '</div>'+
									   '<div class="layui-input-block">'+
										'开始投注时间：<input id="" type="text" value="'+field.startDate+'" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate start-date" onclick="WdatePicker({lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'+
									   '</div>'+
									   '<div class="layui-input-block">'+
									   	'截至投注时间：<input id="" type="text" value="'+field.endDate+'" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate end-date" onclick="WdatePicker({lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'+
									   '</div>'+
									'</div>'
									if(pitems!=null && pitems!=undefined){
										for(var m=0;m<pitems.length;m++){
											var play = pitems[m];
											html+='<div class="play-info">'+
											'<div class="layui-input-block">'+
												'<input type="hidden" name="ptId" value="'+play.pTId+'" class="ptId">'+
												'玩法名称：<input id="" type="text" value="'+play.playTitle+'" required lay-verify="required|text" placeholder="请输入玩法名称" class="layui-input play-title"/>'
												if(m!=0){
													html+='<i class="layui-icon del" onclick="del(this,2)">&#xe640;</i>';
												}
												if(m==pitems.length-1){
													html+='<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>'
										    	}
												html+='</div>'
											var options = play.itemo;
											for(var a=0;a<options.length;a++){
												var option = options[a];
												html+='<div class="option-info">';
													html+='<div class="layui-input-block">';
														html+='<input type="hidden" name="oId" value="'+option.oId+'" class="oId">';
														html+='投注项名称：<input id="" type="text" value="'+option.optionTitle+'" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>';
														html+='赔率：<input id="" type="text" value="'+option.rate+'" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>';
														if(a!=0){
															html+='<i class="layui-icon del" onclick="del(this,3)">&#xe640;</i>';
														}
													html+='</div>';
												html+='</div>';
												
												if(a==options.length-1){
													html+='<i class="layui-icon" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>'
												}
											}
											html+='</div>'
										}
									}
									html+='</div>'+
							'</fieldset>'+
							'</div>';
							$(".match-field-box .match-field-line:last").after(html);
						}
					}
				}
				 $(".option").addClass("layui-icon");
				 $(".addopt").addClass("layui-icon ");
				layui.form.render();// 重新渲染页面
			}
	    },
        error: function(jqXHR, textStatus, errorThrown) {
        	layer.msg('网络错误'); 
        }
	});
}

function loadFirstType(form){
	var map={};
	map['type']=1;
	$.ajax({
	    type: "POST",
	    url:"/api/jc_type",
	    dataType:'json',
	    data:{
	    	mw:baseObj_mw(map)
	    },
	    success: function(ret){
	    	var code=ret.code;
			var data=ret.data;
			console.log(ret);
			var html="";
			if(code=="200"){
				$("#first-type").empty();
				html+="<option value=\"\">请选择一级分类</option>";
				var items=data.items;
				$.each(items,function(i){
					html+="<option value='"+items[i].tid+"'>"+items[i].title+"</option>";
				});
				$("#first-type").html(html);
				form.render();// 重新渲染页面
				console.log(html);
			}else{
				html+="<option disabled>没有一级分类</option>";
				$("#first-type").html(html);
				form.render();// 重新渲染页面
			}
	    },
        error: function(jqXHR, textStatus, errorThrown) {
        	layer.msg('网络错误'); 
        }
	});
}
function loadSecondType(firstId,form){
	if(!firstId){
		firstId = $("#first-type").val();
	}
	var reg = /^[0-9]+.?[0-9]*$/;
	  if (!reg.test(firstId)) {
		  firstId="";
	  }
// 	if(firstId){
		$("#second-type").empty();
		var map={};
		map['type']=2;
		map['firstId']=firstId;
		$.ajax({
		    type: "POST",
		    url:"/api/jc_type",
		    dataType:'json',
		    data:{
		    	mw:baseObj_mw(map)
		    },
		    success: function(ret){
		    	var code=ret.code;
				var data=ret.data;
				console.log(ret);
				var html="";
				if(code=="200"){
					html+="<option value=\"\">请选择二级分类</option>";
					var items=data.items;
					$.each(items,function(i){
						html+="<option value='"+items[i].tid+"'>"+items[i].title+"</option>";
					});
				}else{
					html+="<option disabled>此分类下没有二级分类</option>";
				}
				$("#second-type").html(html);
				form.render();// 重新渲染页面
				console.log(html);
		    },
	        error: function(jqXHR, textStatus, errorThrown) {
	        	layer.msg('网络错误'); 
	        }
		});
		
		form.render();// 重新渲染页面
// 	}
}
function loadTeam(secondId,form){
	if(!secondId){
		secondId = $("#second-type").val();
	}
	
	var reg = /^[0-9]+.?[0-9]*$/;
	  if (!reg.test(secondId)) {
		  secondId="";
	  }
	  
// 	if(secondId){
		$("#first-team").empty();
		$("#second-team").empty();
		var map={};
		map['secondId']=secondId;
		$.ajax({
		    type: "POST",
		    url:"/api/jc_teamList",
		    dataType:'json',
		    data:{
		    	mw:baseObj_mw(map)
		    },
		    success: function(ret){
		    	var code=ret.code;
				var data=ret.data;
				var html="";
				if(code=="200"){
					html+="<option value=\"\">请选择队伍</option>";
					var items=data.items;
					$.each(items,function(i){
						html+="<option value='"+items[i].tid+"'>"+items[i].title+"</option>";
					});
				}else{
					html+="<option disabled>此分类下没有队伍</option>";
				}
				$("#first-team").html(html);
				$("#second-team").html(html);
				$("#red-team").html(html);
				$("#blue-team").html(html);
				form.render();// 重新渲染页面
		    },
	        error: function(jqXHR, textStatus, errorThrown) {
	        	layer.msg('网络错误'); 
	        }
		});
		
		form.render();// 重新渲染页面
// 	}
}
function addField(){
	var html='<div class="match-field-line">'+
				'<fieldset class="layui-elem-field">'+
				'<legend style="font-size: 14px;">比赛场次<i class="layui-icon del" onclick="del(this,3)">&#xe640;</i></legend>'+
				'<div class="layui-field-box">'+
					'<div class="field-info">'+
					       '<input type="hidden" name="fId" class="fId">'+
						   '<div class="layui-input-block">'+
							'场次名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入场次名称" class="layui-input field-title"/>'+
						   '</div>'+
						   '<div class="layui-input-block">'+
							'开始投注时间：<input id="" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate start-date" onclick="WdatePicker({lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'+
						   '</div>'+
						   '<div class="layui-input-block">'+
						   	'截至投注时间：<input id="" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate end-date" onclick="WdatePicker({lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'+
						   '</div>'+
						'</div>'+
						'<div class="play-info">'+
							'<div class="layui-input-block">'+
								'<input type="hidden" name="ptId" class="ptId">'+
								'玩法名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入玩法名称" class="layui-input play-title"/>'+
						    	'<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>'+
							'</div>'+
						    '<div class="option-info">'+
						    	'<div class="layui-input-block">'+
						    		'<input type="hidden" name="oId" class="oId">'+
									'投注项名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>'+
									'赔率：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>'+
							    '</div>'+
						    '</div>'+
							'<i class="layui-icon" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>'+
						'</div>'+
					'</div>'+
				'</fieldset>'+
			'</div>';
	$(".match-field-box .match-field-line:last").after(html);
}
function addPlay(obj){
	var html='';
	html+='<div class="play-info">';
		html+='<div class="layui-input-block">';
			html+='<input type="hidden" name="ptId" class="ptId">';
			html+='玩法名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入玩法名称" class="layui-input play-title"/>';
			html+='<i class="layui-icon del" onclick="del(this,2)">&#xe640;</i>';
			html+='<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>';
		html+='</div>';
		html+='<div class="option-info">';
			html+='<div class="layui-input-block">';
				html+='<input type="hidden" name="oId" class="oId">';
				html+='投注项名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>';
				html+='赔率：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>';
			html+='</div>';
		html+='</div>';
		html+='<i class="layui-icon" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>';
	html+='</div>';
	$(obj).parent().parent().after(html);
	$(obj).remove();
}
function addOption(obj){
	var html='';
	html+='<div class="option-info">';
		html+='<div class="layui-input-block">';
			html+='<input type="hidden" name="oId" class="oId">';
			html+='投注项名称：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>';
			html+='赔率：<input id="" type="text" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>';
			html+='<i class="layui-icon del" onclick="del(this,3)">&#xe640;</i>';
		html+='</div>';
	html+='</div>';
	$(obj).prev().after(html);
}
function del(obj,type){
	//type:1.场次2.玩法3.投注项
	if(type==1){
		$(obj).parent().parent().parent().remove();
	}else if(type==2){
		var nextLen=$(obj).parent().parent().next().length;
		if(nextLen==0){
			$(obj).parent().parent().prev().children(".layui-input-block").append('<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>');
		}
		$(obj).parent().parent().remove();
	}else if(type==3){
		$(obj).parent().parent().remove();
	}
}
function submit(){
	var param='';
	var mId = $("#mId").val();
	var matchTitle=$("#match-title").val();
	var subTitle=$("#match-sub-title").val();
	var firstType=$("#first-type").val();
	var secondType=$("#second-type").val();
	var firstTeam=$("#first-team").val();
	var secondTeam=$("#second-team").val();
	var redTeam=$("#red-team").val();
	var blueTeam=$("#blue-team").val();
	var boNum=$("#bo-num").val();
	var matchType=$("#match-type").val();
	var openTime=$("#open-time").val();
	var matchTime=$("#match-time").val();
	var rercentage=$("#rercentage").val();
	if(firstTeam==secondTeam){
		layer.msg("参赛队伍不能是同一支队伍");
		return false;
	}
	
	if(redTeam!=firstTeam && redTeam!=secondTeam){
		layer.msg("红方必须是参赛队伍之一");
		return false;
	}
	
	if(blueTeam!=firstTeam && blueTeam!=secondTeam){
		layer.msg("蓝方必须是参赛队伍之一");
		return false;
	}
	
	if(redTeam==blueTeam){
		layer.msg("红蓝队伍不能是同一支队伍");
		return false;
	}
	
	param+='{';
	param+='"mId":"'+mId+'",';
	param+='"matchTitle":"'+matchTitle+'",';
	param+='"subTitle":"'+subTitle+'",';
	param+='"firstType":"'+firstType+'",';
	param+='"secondType":"'+secondType+'",';
	param+='"firstTeam":"'+firstTeam+'",';
	param+='"secondTeam":"'+secondTeam+'",';
	param+='"redTeam":"'+redTeam+'",';
	param+='"blueTeam":"'+blueTeam+'",';
	param+='"boNum":"'+boNum+'",';
	param+='"matchType":"'+matchType+'",';
	param+='"openTime":"'+openTime+'",';
	param+='"matchTime":"'+matchTime+'",';
	param+='"rercentage":"'+rercentage+'",';
	param+='"fieldArr":[';
	var matchLen=$(".match-field-line").length;
	$(".match-field-line").each(function(i){// 所有的比赛场次
		var fId=$(this).find(".fId").val();
		var fieldTitle=$(this).find(".field-title").val();
		var startDate=$(this).find(".start-date").val();
		var endDate=$(this).find(".end-date").val();
		param+='{';
		param+='"fId":"'+fId+'",'
		param+='"fieldTitle":"'+fieldTitle+'",';
		param+='"startDate":"'+startDate+'",';
		param+='"endDate":"'+endDate+'",';
		param+='"playArr":[';
		var playLen=$(this).find(".play-info").length;
		$(this).find(".play-info").each(function(j){
			var ptId=$(this).find(".ptId").val();
			var playTitle=$(this).find(".play-title").val();
			param+='{';
			param+='"ptId":"'+ptId+'",';
			param+='"playTitle":"'+playTitle+'",';
			param+='"optionArr":[';
			var optionLen=$(this).find(".option-info").length;
			$(this).find(".option-info").each(function(k){
				var oId=$(this).find(".oId").val();
				var optionTitle=$(this).find(".option-title").val();
				var rate=$(this).find(".rate").val();
				param+='{';
				param+='"oId":"'+oId+'",';
				param+='"optionTitle":"'+optionTitle+'",';
				param+='"rate":"'+rate+'"';
				if(optionLen-1==k){
					param+='}';
				}else{
					param+='},';
				}
			});
			param+=']';
			if(playLen-1==j){
				param+='}';
			}else{
				param+='},';
			}
		});
		param+=']';
		if(matchLen-1==i){
			param+='}';
		}else{
			param+='},';
		}
	});
	param+=']';
	param+='}';
	
	$.ajax({
	    type: "POST",
	    url:"/api/jc_saveUpdateMatch",
	    dataType:'json',
	    data:{
	    	param:param
	    },
	    success: function(ret){
	    	var code=ret.code;
			var data=ret.data;
			var msg=ret.msg;
			if(code=="200"){
				layer.msg(msg);
				layer.open({
					content : '保存成功,继续添加？',
					btn : [ '添加', '返回'],
					yes : function(index, layero) {
						location = "createMatch.jsp";
					},
					btn2 : function(index, layero) {
						location = "matchAction.do?method=init";
					},
					cancel : function() {
						location = "matchAction.do?method=init";
					}
				});
			} else {
				layer.msg(msg);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			layer.msg('网络错误');
		}
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
<style type="text/css">
.layui-tab-content .layui-tab-item{width: 580px;margin-bottom: 10px;}
.layui-tab .layui-tab-content .layui-form .match-field-box .del{left: 0;color: red;cursor: pointer;font-size: 30px;}
.match-field-box .match-field-line .layui-elem-field legend{color: #009688;}
.match-field-box .match-field-line .layui-elem-field .layui-field-box{padding: 10px 0px 10px 90px;}
.match-field-box .match-field-line .layui-input-block{display: -webkit-inline-box;margin-left: 0px;line-height: 40px;}
.match-field-box .match-field-line .field-info .layui-input-block input{width: 230px;}
.match-field-box .match-field-line .play-info{margin-top: 10px;border-top: 1px solid #e6e6e6;padding-top: 10px;padding-bottom: 10px;margin-left: 50px;display: flow-root;}
.match-field-box .match-field-line .play-info .add-des{position: relative;bottom: 5px;}
.match-field-box .match-field-line .play-info .option-info{margin-top: 5px;width: 50%;float: left;}
.match-field-box .match-field-line .play-info .option-info .layui-input-block input{width: 150px;margin-right: 5px;}
.match-field-box .match-field-line .play-info .layui-input-block{width: 100%;}
.match-field-box .match-field-line .play-info .layui-input-block input{width: 250px;}
.match-field-box .match-field-line .play-info .layui-input-block i{left: 50px;top: 0px;}
.match-field-box .match-field-line .play-info i{position: relative;top: 10px;left: 0px;font-size: 30px;color: #009688;cursor: pointer;}
.layui-tab-content .btn-box {text-align: center;}
.layui-tab-content .btn-box i{font-size: 30px;color: #009688;cursor: pointer;top: 10px;position: relative;}
.layui-tab-content .btn-box .add-des{position: relative;bottom: 5px;}
</style>
</head>
<body>
<div class="bread">赛事管理 &raquo;修改&raquo;</div>
<input type="hidden" id="mId" value="<c:out value="${mId}"/>"/>
<div class="layui-tab layui-tab-brief ">
	<div class="layui-tab-content">
		<form class="layui-form">
			<div class="layui-tab-item layui-show">
				<label class="layui-form-label">赛事名称：</label>
			    <div class="layui-input-block">
					<input id="match-title" type="text" required lay-verify="required|text" placeholder="请输入赛事名称" class="layui-input"/>
			    </div>
			</div>
			<div class="layui-tab-item layui-show">
				<label class="layui-form-label">副标题：</label>
			    <div class="layui-input-block">
					<input id="match-sub-title" type="text" required lay-verify="required|text" placeholder="请输入副标题" class="layui-input"/>
			    </div>
			</div>
			<div class="layui-tab-item layui-show">
				<label class="layui-form-label">一级分类：</label>
			    <div class="layui-input-block">
				    <select id="first-type" lay-verify="required" lay-filter="first-type">
				    	<option>请选择一级分类</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">二级分类：</label>
			    <div class="layui-input-block">
				    <select id="second-type" lay-verify="required" lay-filter="second-type">
				    	<option value="">请选择二级分类</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">队伍1：</label>
			    <div class="layui-input-block">
				    <select id="first-team" lay-verify="required">
				   		<option value="">请选择队伍1</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">队伍2：</label>
			    <div class="layui-input-block">
				    <select id="second-team" lay-verify="required">
				    	<option value="">请选择队伍2</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">红方：</label>
			    <div class="layui-input-block">
				    <select id="red-team" lay-verify="required">
				   		<option value="">请选择红方</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">蓝方：</label>
			    <div class="layui-input-block">
				    <select id="blue-team" lay-verify="required">
				    	<option value="">请选择蓝方</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">总场次：</label>
			    <div class="layui-input-block">
				    <select id="bo-num" lay-verify="required">
				    	<option value="">请选择总场次</option>
				    	<option value="1">BO1</option>
				    	<option value="2">BO2</option>
				    	<option value="3">BO3</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label">类型：</label>
			    <div class="layui-input-block">
				    <select id="match-type" lay-verify="required">
				    	<option value="">请选择类型</option>
				    	<option value="1">对战</option>
				    	<option value="2">单</option>
				    </select>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show">
				<label class="layui-form-label" >开奖时间：</label>
			    <div class="layui-input-block">
					<input id="open-time" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    </div>
			</div>
			<div class="layui-tab-item layui-show">
				<label class="layui-form-label">比赛时间：</label>
			    <div class="layui-input-block">
					<input id="match-time" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    </div>
			</div>
			<div class="layui-tab-item layui-show">
				<label class="layui-form-label">抽水比例：</label>
			    <div class="layui-input-block">
					<input id="rercentage" type="text" required  lay-verify="required|text" placeholder="请输入抽水比例" class="layui-input"/>
			    </div>
			</div>
			<div class="match-field-box">
				<div class="match-field-line">
					<fieldset class="layui-elem-field">
						<legend style="font-size: 14px;">比赛场次</legend>
						<div class="layui-field-box">
							<div class="field-info">
								<input type="hidden" name="fId" id="fId" class="fId"/>
							    <div class="layui-input-block">
									场次名称：<input id="field-title" type="text" required lay-verify="required|text" placeholder="请输入场次名称" class="layui-input field-title"/>
							    </div>
							    <div class="layui-input-block">
									开始投注时间：<input id="start-date" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate start-date" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							    </div>
							    <div class="layui-input-block">
									截至投注时间：<input id="end-date" type="text" required lay-verify="required|text" placeholder="请选择时间" class="layui-input Wdate end-date" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							    </div>
							</div>
							<div class="play-info">
								<div class="layui-input-block">
									<input type="hidden" name="ptId" id="ptId" class="ptId"/>
									玩法名称：<input id="play-title" type="text" required lay-verify="required|text" placeholder="请输入玩法名称" class="layui-input play-title"/>
							    	<i class="layui-icon" onclick="addPlay(this)">&#xe61f;<span class="add-des">玩法</span></i>
							    </div>
							    <div class="option-info">
							    	<div class="layui-input-block">
							    		<input type="hidden" name="oId" id="oId" class="oId"/>
										投注项名称：<input id="option-title" type="text" required lay-verify="required|text" placeholder="请输入投注项名称" class="layui-input option-title"/>
										赔率：<input id="rate" type="text" required lay-verify="required|text" placeholder="请输入投注项赔率" class="layui-input rate"/>
								    </div>
							    </div>
							    <i class="layui-icon aopt" onclick="addOption(this)">&#xe61f;<span class="add-des">投注项</span></i>
							</div>
						</div>
					</fieldset>
				</div>
			</div>
			<div class="btn-box">
				<i onclick="addField()" class="layui-icon">&#xe61f;<span class="add-des">场次</span></i>
				<button class="layui-btn" lay-submit lay-filter="formDemo">提交</button>
      			<button type="reset" class="layui-btn layui-btn-primary" onclick="javascript:window.history.go(-1)">返回</button>
			</div>
		</form>
	</div>
</div>
</body>
<script>
</script>
</html:html>