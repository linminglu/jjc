<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include  file="../common/inc_datepicker.jsp" %>
<html:html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
<title>管理平台</title>
<script type="text/javascript">
$(function(){
	$("#param1_2").empty();
	var html = '';
	html +="<option>请选择</option>";
	$.post("fieldAction.do?method=getMatch",{},function(result){
		if(result!=null && result.length>0){
			var matchList = JSON.parse(result);
			 $.each(matchList,function(i){
				html +="<option value="+matchList[i].mId+">"+matchList[i].title+"</option>";
			});
			$(html).appendTo("#param1_2");
		}
	})
});

function getTeam(mId){
	$.post('fieldAction.do?method=getTeam',{'mId':mId},function(result){
		if(result!=null && result.length>0){
			var res = JSON.parse(result);
			var data = res.data;
			var code  = data.code;
			if(code=='200'){
				$('#red').val(data.obj.red);
				$('#blue').val(data.obj.blue);
			}
		}
	})
}

function loadParam(form, fId){
	$.post("fieldAction.do?method=loadParam",{'fId':fId},
		function(result){
			var code=result.code;
			var data=result.data;
			if(code=='200'){
				var fieldTitle = data.obj.fieldTitle;
				var mId = data.obj.mId;
				var fieldStatus = data.obj.fieldStatus;
				var startDate = data.obj.startDate;
				var endDate = data.obj.endDate;
				var red = data.obj.red;
				var blue = data.obj.blue;
				var pitems=data.obj.pitems;
				$('#playNum').val(pitems.length);
				var optionNum = 0;
				var html = '';
				html +="<option>请选择</option>";
				var k = 0;
				for(var i=0;i<pitems.length;i++){
					k++;
					var obj=pitems[i];
					var pTId = obj.pTId;
					var fId = obj.fId;
					var status = obj.status;
					var playTitle = obj.playTitle;
					var itemo = obj.itemo;
					if(i==0){
						$('#param1_7_1_1').val(pTId);
						$('#param1_7_1').val(playTitle);
						//$('#param1_8_1').val(status);
					}else{
						var playNum = i+1;
						var arr = new Array();
						arr.push("<hr><div class=\"layui-tab-item layui-show\" id=\"play"+playNum+"\">");
						arr.push("<input type=\"hidden\"  id=\"param1_7_1_"+playNum+"\" name=\"param1_7\" value=\""+pTId+"\"/>");
						arr.push("<label class=\"layui-form-label\">玩法"+playNum+"</label>");
						arr.push("<div class=\"layui-input-block\">");
						arr.push("<input id=\"param1_7_"+playNum+"\" type=\"text\" value=\""+playTitle+"\" onblur=\"getPlayName(this.value)\" name=\"playTitle\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
						arr.push("<input type=\"button\" class=\"button playAdd\" id=\"delPlay"+playNum+"\" onclick=\"delPlay("+playNum+")\" value=\"删除玩法\"/>");
						arr.push("</div>");
						arr.push("<label class=\"layui-form-label\">状态</label>");
						arr.push("<div class=\"layui-input-block\">");
						arr.push("<select id=\"param1_8_"+playNum+"\" name=\"playStatus\" lay-ignore value=\""+status+"\" required lay-verify=\"required\" placeholder=\"\" autocomplete=\"off\" class=\"layui-select\">");
						arr.push("<option value=\"1\">有效</option>");
						arr.push("<option value=\"0\">无效</option>");
						arr.push("</select>");
						arr.push("</div>");
						arr.push("</div>");
						$(arr.join("")).appendTo("#play");
					}
					
					html +="<option value="+pTId+">"+playTitle+"</option>";
					
					for(var j=0;j<itemo.length;j++){
						optionNum++;
						var opt = itemo[j];
						var oId = opt.oId;
						var ptId = opt.ptId;
						var optionTitle = opt.optionTitle;
						var rate = opt.rate;
						if(j==0 && k==1){
							$(html).appendTo("#param1_10_1");
							$('#param1_9_1_1').val(oId);
							$('#param1_9_1').val(optionTitle);
							$('#param1_10_1').val(ptId);
							$('#param1_11_1').val(rate);
						}else{
// 							var optionNum = j+1;
							var arr = new Array();
							arr.push("<hr><div class=\"layui-tab-item layui-show\" id=\"option"+optionNum+"\">");
							arr.push("<input type=\"hidden\"  id=\"param1_9_1_"+optionNum+"\" name=\"param1_9\" value=\""+oId+"\"/>");
							arr.push("<label class=\"layui-form-label\">投注名称"+optionNum+"</label>");
							arr.push("<div class=\"layui-input-block\">");
							arr.push("<input id=\"param1_9_"+optionNum+"\" type=\"text\" value=\""+optionTitle+"\" name=\"optionTitle\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
							arr.push("<input type=\"button\" class=\"button playAdd\" id=\"delPlay"+optionNum+"\" onclick=\"delOption("+optionNum+")\" value=\"删除投注\"/>");
							arr.push("</div>");
							arr.push("<label class=\"layui-form-label\">请选择该投注玩法</label>");
							arr.push("<div class=\"layui-input-block\">");
							arr.push("<select id=\"param1_10_"+optionNum+"\" name=\"optionPlay\" lay-ignore required lay-verify=\"required\" placeholder=\"\" autocomplete=\"off\" class=\"layui-select\">");
							arr.push("</select>");
							arr.push("</div>");
							arr.push("<label class=\"layui-form-label\">赔率"+optionNum+"</label>");
							arr.push("<div class=\"layui-input-block\">");
							arr.push("<input id=\"param1_11_"+optionNum+"\" value=\""+rate+"\" type=\"text\" name=\"optionRate\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
							arr.push("</div>");
							arr.push("</div>");
							$(arr.join("")).appendTo("#option");
							$(html).appendTo("#param1_10_"+optionNum+"");
							$("#param1_10_"+optionNum+"").val(ptId);
						}
					}
				}
				if(optionNum!=0){
					$('#optionNum').val(optionNum);
				}
				
				$("#fId").val(fId);
				$('#param1_1').val(fieldTitle);
				$('#param1_2').val(mId);
				//$('#param1_4').val(fieldStatus);
				$('#param1_5').val(startDate);
				$('#param1_6').val(endDate);
				$('#red').val(red);
				$('#blue').val(blue);
			}
		 form.render();
		}, "json");
}

function goField(){
	window.location = "fieldAction.do?method=init";
}
</script>
<style type="text/css">
.settingtextareabox{}
.settingtextareabox .gbutton{margin: 10px 0;}
.layui-input-block .gbutton{margin: 10px 125px;}
.layui-tab-item{width: 580px;}
.layui-form-label {width: 130px;}
.layui-input-block {margin-left: 160px;margin-bottom: 10px;}
.layui-input-short {display: -webkit-inline-box;width: 44%;}
.prompt {position: fixed;color: red;}
@media screen and (max-width: 1400px) {
	.layui-tab-title{height: 80px;white-space: inherit;}
}
.playAdd{width: 70px;margin-top: -30px;margin-left: 450px;}
.layui-select{width: 420px;}
</style>
</head>
<body>
<div class="bread">竞猜管理&raquo;比赛局添加</div>
<input type="hidden" value="<c:out value="${fId}"/>" id="fId"/>
<input type="hidden" id="playNum" value="1"/>
<input type="hidden" id="optionNum" value="1"/>
<div class="layui-tab layui-tab-brief ">
	<form class="layui-form" action="">
	    <div class="layui-tab-content">
		    <div class="layui-tab-item layui-show">
		    	<label class="layui-form-label" id="param1" data="" t="">比赛局名称：</label>
			    <div class="layui-input-block">
					<input id="param1_1" type="text" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input"/>
			    </div>
			    <label class="layui-form-label">赛事：</label>
			    <div class="layui-input-block">
			      <select id="param1_2" name="city" required lay-verify="required" lay-filter="city" placeholder="" autocomplete="off" lay-search="" class="layui-select" onchange="getTeam(this.value)">
			      </select>
			    </div>
			    <label class="layui-form-label">红方：</label>
			    <div class="layui-input-block">
			      <input id="red" name="red" required lay-verify="required" placeholder="" autocomplete="off" class="layui-input" readonly="readonly">
			      </input>
			    </div>
			    <label class="layui-form-label">蓝方：</label>
			    <div class="layui-input-block">
			      <input id="blue" name="blue" required lay-verify="required" placeholder="" autocomplete="off" class="layui-input" readonly="readonly">
			      </input>
			    </div>
			    <!-- <label class="layui-form-label">状态：</label>
			    <div class="layui-input-block">
			    	<select id="param1_4" name="fieldStatus" required lay-verify="required" placeholder="" autocomplete="off" class="layui-select">
			    		<option value="1">有效</option>
			    		<option value="0">无效</option>
			        </select>
			    </div> -->
			    <label class="layui-form-label">开始投注时间：</label>
			    <div class="layui-input-block">
					<input id="param1_5" type="text" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    </div>
			    <label class="layui-form-label">结束时间：</label>
			    <div class="layui-input-block">
					<input id="param1_6" type="text" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input" onclick="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			    </div>
		    </div>
		    <div class="layui-tab-item layui-show" id="play">
		    	<input type="hidden"  id="param1_7_1_1" name="param1_7"/>
		    	<label class="layui-form-label">玩法1：</label>
			    <div class="layui-input-block">
					<input id="param1_7_1" type="text" name="playTitle" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input" onblur="getPlayName(this.value)"/>
					<input type="button" class="button playAdd" id="addPlay" value="添加玩法"/>
			    </div>
			    <!-- <label class="layui-form-label">状态：</label>
			    <div class="layui-input-block">
			    	<select id="param1_8_1" name="playStatus" required lay-verify="required" lay-ignore placeholder="" autocomplete="off" class="layui-select">
			    		<option value="1">有效</option>
			    		<option value="0">无效</option>
			        </select>
			    </div> -->
		     </div>
	
		    <div class="layui-tab-item layui-show" id="option">
		    	<hr>
		    	<input type="hidden"  id="param1_9_1_1" name="param1_9"/>
		    	<label class="layui-form-label">投注项名称1：</label>
			    <div class="layui-input-block">
					<input id="param1_9_1" type="text" name="optionTitle" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input"/>
					<input type="button" class="button playAdd" id="addOption" value="添加投注"/>
			    </div>
			    <label class="layui-form-label">请选择该投注玩法：</label>
			    <div class="layui-input-block">
			    	<select id="param1_10_1" name="optionPlay" required lay-verify="required" placeholder="" autocomplete="off" lay-ignore class="layui-select">
			      	</select>
			    </div>
			    <label class="layui-form-label">赔率：</label>
			    <div class="layui-input-block">
					<input id="param1_11_1" type="text" name="optionRate" required  lay-verify="required|text" placeholder="" autocomplete="off" class="layui-input"/>
			    </div>
		    </div>
		    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="param1">确定</button>
			      <input type="button" class="gbutton" style="margin-left: -385px;" onclick="javascript:window.history.go(-1)" value="返回"/>
		    </div>
		</div>
		</form>
</div>
<script type="text/javascript">
layui.use('element', function(){
// 	var element = layui.element;
});

var playNum = 0;
var optionNum = 0;
$("#addPlay").click(function(){
	playNum = $('#playNum').val();
	optionNum = $('#optionNum').val();
	if(!playNum){
		playNum = 1;
	}
	
	playNum++;
	var arr = new Array();
	arr.push("<div class=\"layui-tab-item layui-show\" id=\"play"+playNum+"\">");
	arr.push("<hr><input type=\"hidden\"  id=\"param1_7_1_"+playNum+"\" name=\"param1_7\"/>");
	arr.push("<label class=\"layui-form-label\">玩法"+playNum+"</label>");
	arr.push("<div class=\"layui-input-block\">");
	arr.push("<input id=\"param1_7_"+playNum+"\" type=\"text\" onblur=\"getPlayName(this.value)\" name=\"playTitle\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
	arr.push("<input type=\"button\" class=\"button playAdd\" id=\"delPlay"+playNum+"\" onclick=\"delPlay("+playNum+")\" value=\"删除玩法\"/>");
	arr.push("</div>");
	arr.push("<label class=\"layui-form-label\">状态</label>");
	arr.push("<div class=\"layui-input-block\">");
	arr.push("<select id=\"param1_8_"+playNum+"\" name=\"playStatus\" required lay-verify=\"required\" lay-ignore placeholder=\"\" autocomplete=\"off\" class=\"layui-select\">");
	arr.push("<option value=\"1\">有效</option>");
	arr.push("<option value=\"0\">无效</option>");
	arr.push("</select>");
	arr.push("</div>");
	arr.push("</div>");
	$(arr.join("")).appendTo("#play");
	$('#playNum').val(playNum)
})


$("#addOption").click(function(){
	optionNum = $('#optionNum').val();
	if(!optionNum){
		optionNum = 1;
	}
	
	optionNum++;
	var arr = new Array();
	arr.push("<hr>");
	arr.push("<div class=\"layui-tab-item layui-show\" id=\"option"+optionNum+"\">");
	arr.push("<input type=\"hidden\"  id=\"param1_9_1_"+playNum+"\" name=\"param1_9\"/>");
	arr.push("<label class=\"layui-form-label\">投注名称"+optionNum+"</label>");
	arr.push("<div class=\"layui-input-block\">");
	arr.push("<input id=\"param1_9_"+optionNum+"\" type=\"text\" name=\"optionTitle\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
	arr.push("<input type=\"button\" class=\"button playAdd\" id=\"delPlay"+optionNum+"\" onclick=\"delOption("+optionNum+")\" value=\"删除投注\"/>");
	arr.push("</div>");
	arr.push("<label class=\"layui-form-label\">请选择该投注玩法</label>");
	arr.push("<div class=\"layui-input-block\">");
	arr.push("<select id=\"param1_10_"+optionNum+"\" name=\"optionPlay\" required lay-verify=\"required\" placeholder=\"\" lay-ignore autocomplete=\"off\" class=\"layui-select\">");
	arr.push("</select>");
	arr.push("</div>");
	arr.push("<label class=\"layui-form-label\">赔率"+optionNum+"</label>");
	arr.push("<div class=\"layui-input-block\">");
	arr.push("<input id=\"param1_11_"+optionNum+"\" type=\"text\" name=\"optionRate\" required  lay-verify=\"required|text\" placeholder=\"\" autocomplete=\"off\" class=\"layui-input\"/>");
	arr.push("</div>");
	arr.push("</div>");
	$(arr.join("")).appendTo("#option");
	$('#optionNum').val(optionNum);
	getPlayName(optionNum);
})

function delPlay(num){
	var playId = $("#param1_7_1_"+num+"").val();
	if(playId){
		$.post('fieldAction.do?method=del',{'playId':playId},function(result){
			var res = JSON.parse(result);
			var code = res.data.code;
			if(code=='200'){
				layer.alert('删除成功', {icon: 6, offset: '100px'},function(index){
					$("#play"+num+"").remove();
					playNum--;
			        location.reload();
		        });
			}
		})
	}else{
		$("#play"+num+"").remove();
		playNum--;
	}
}

function delOption(num){
	var optId = $("#param1_9_1_"+num+"").val();
	if(optId){
		$.post('fieldAction.do?method=del',{'optId':optId},function(result){
			var res = JSON.parse(result);
			var code = res.data.code;
			if(code=='200'){
				layer.alert('删除成功', {icon: 6, offset: '100px'},function(index){
					$("#option"+num+"").remove();
					optionNum--;
					location.reload();
		        });
			}
		})
	}else{
		$("#option"+num+"").remove();
		optionNum--;
	}
}

function getPlayName(title){
	var html = "<option value="+title+">"+title+"</option>";
	
	if(optionNum==0){
		optionNum = $('#optionNum').val();
	}
	
	var patrn = /^(-)?\d+(\.\d+)?$/;
    if (patrn.exec(title) == null || title == "") {
    	for(var j=1; j<parseInt(optionNum)+1; j++){
   			var bool = true;
   			$("#param1_10_"+j+" option").each(function(){  //遍历所有option  
   		          var txt = $(this).val();   //获取option值   
   		          if(txt==title){  
   		        	  bool = false;
   		          }  
   		     })
   		     if(bool){
   		    	 $(html).appendTo("#param1_10_"+j+"");
   		     }
    	}
    } else {
    	var titles = "";
    	$("input[name='playTitle']").each(function(){
    		titles += $(this).val()+",";
    	});
    	var arrTitle = titles.split(",");
    	if(arrTitle!="" && arrTitle.length>0){
    		var html = "<option>请选择</option>";
    		for(var i=0; i<arrTitle.length; i++){
    			var play = arrTitle[i];
    			if(play!=""){
    				html += "<option value="+play+">"+play+"</option>";
    			}
    			
    		}
    		
    		$(html).appendTo("#param1_10_"+title+"");
    	}
    }
}

layui.use('form', function(){
	var form = layui.form;
	var fId = $("#fId").val();
 	loadParam(form, fId);
// 	form.on('switch(switch)', function(data){
// 		var obj=data.elem;
// 		var val=$(obj).val();
// 		if(val=='1'){
// 			$(obj).val("0");
// 		}else{
// 			$(obj).val("1");
// 		}
// 	});
form.on('select(city)', function(data){
    var mId = data.value;
    $.post('fieldAction.do?method=getTeam',{'mId':mId},function(result){
		if(result!=null && result.length>0){
			var res = JSON.parse(result);
			var data = res.data;
			var code  = data.code;
			if(code=='200'){
				$('#red').val(data.obj.red);
				$('#blue').val(data.obj.blue);
			}
		}
	})
})

	form.on('submit(param1)', function(data){
		
		var list = document.getElementsByTagName("input");
		for (var i = 0; i < list.length; i++) {
			// 判断是否为文本框
			if (list[i].type == "text") {
				// 判断文本框是否为空
				if (list[i].value == "") {
					layer.msg('请将所有输入框填写完整!',{time: 800});
					return false;
				}
			}
		}
		
		var fId = $('#fId').val();
		var param1_1=$('#param1_1').val();
		var param1_2=$('#param1_2').val();
		if(!param1_2){
			layer.msg('请选择赛事!',{time: 800});
			return false;
		}
// 		var param1_3=$('#param1_3').val();
		//var param1_4=$('#param1_4').val();
		var param1_5=$('#param1_5').val();
		var param1_6=$('#param1_6').val();
		
		var playIds = "";
		$("input[name='param1_7']").each(function(){
			playIds += $(this).val()+",";
		});
		
		var playTitles = "";
		$("input[name='playTitle']").each(function(){
			playTitles += $(this).val()+",";
		});
		
		var playStatus = "";
		$("[name='playStatus'] option:selected").each(function(){
			playStatus += $(this).val()+",";
		});
		
		var optionIds = "";
		$("input[name='param1_9']").each(function(){
			optionIds += $(this).val()+",";
		});
		
		var optionTitles = "";
		$("input[name='optionTitle']").each(function(){
			optionTitles += $(this).val()+",";
		});
		
		var optionPlays = "";
		var isplay = false;
		$("[name='optionPlay'] option:selected").each(function(){
			optionPlays += $(this).val()+",";
			if($(this).val()=="" || $(this).val()=="请选择"){
				isplay = true;
			}
		});
		if(optionPlays=="请选择," || optionPlays=="," || optionPlays=="" || isplay){
			layer.msg('请选择投注项的玩法!',{time: 800});
			return false;
		}
		
		var optionRates = "";
		$("input[name='optionRate']").each(function(){
			optionRates += $(this).val()+",";
		});
		
		saveParam(fId, param1_1, param1_1, param1_2, param1_5, param1_6, playIds, playTitles, playStatus, optionIds, optionTitles, optionPlays, optionRates);
		return false;
	});  
});
var layedit =null;
var editorDaili=null;
layui.use('layedit', function(){
});

function saveParam(fId, param1_1, param1_1, param1_2, param1_5, param1_6, playIds, playTitles, playStatus, optionIds, optionTitles, optionPlays, optionRates){
	var loadObj = layer.load();
	$.post("fieldAction.do?method=saveParam", 
			{'param1_1':param1_1,
			'param1_2':param1_2,
			'param1_3':fId,
			//'param1_4':param1_4,
			'param1_5':param1_5,
			'param1_6':param1_6,
			'playIds':playIds,
			'playTitles':playTitles,
			'playStatus':playStatus,
			'optionIds':optionIds,
			'optionTitles':optionTitles,
			'optionPlays':optionPlays,
			'optionRates':optionRates
			},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
				layer.open({
					  content: '保存成功',
					  yes: function(index, layero){
						  location = "fieldAction.do?method=init";
					  }
					});
				}else{
					layer.msg('保存失败',{time: 800});
				}
		}, "json");
}
</script>
</body>
</html:html>