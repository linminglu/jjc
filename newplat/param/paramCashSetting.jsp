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
$(document).ready(function(){
	
	/* var reg = new RegExp("^\\d+(,\\d+)?$");
	if (!reg.test("20，00")) {
		alert("请输入正确的值")
		return false;
	}else{
		alert("正确")
	} */
	
});
function loadParam(form){
	$.post("/param/paramAction.do?method=loadParam",{},
		function(result){
			var code=result.code;
			var data=result.data;
			var items=data.items;
			for(var i=0;i<items.length;i++){
				var obj=items[i];
				var paramId=obj.paramId;
				var type=obj.type;
				var value=obj.value;
				if(type=='11'||type=='12'||type=='14'
						||type=='15'||type=='16'||type=='17'||type=='28'
						||type=='37'||type=='44'||type=='45'||type=='46'){
					$('#param'+type).val(value);
					$('#param'+type).attr('data',paramId);
					$('#param'+type).attr('t',type);
					if(value=='1'){
						$('#param'+type).attr('checked',true);
					}
				}else if(type=='13'){
					var val=value.split("&")[1];
					var time=value.split("&")[0];
					$('input[name="param13Time"]').val(time);
					$('#param13').val(val);
					$('#param13').attr('data',paramId);
					$('#param13').attr('t',type);
				}else if(type=="48"){
					var shtml = '';
					if(value=='0'||value==0){
						shtml += '<input id="param'+type+'" type="checkbox" data="'+paramId+'" t="'+type+'" name="title" lay-skin="switch" lay-filter="switch" lay-text="开启|关闭"/>';
					}else if(value=='1'||value==1){
						shtml += '<input id="param'+type+'" checked="" type="checkbox" t="'+type+'" data="'+paramId+'" name="title" lay-skin="switch" lay-filter="switch" lay-text="开启|关闭"/>';
					}
					$(".switch"+type).html(shtml);
					$(".switch"+type+" input").bind("click",function(){
						var checked = $(this).attr("checked");
						var val = 1;
						if(checked&&checked=="checked"){
							
						}else{
							val = 0;
						}
						var id=$(this).attr('data');
						var type=$(this).attr('t');
						saveParam(id,type,val);
					});
				}
			}
		 form.render();
		}, "json");
}

function backInit(form){// 打码返水
	$.post("/basedata/lotterySettingAction.do?method=backInit",{},
		function(result){
			var code=result.code;
			var data=result.data;
			if(code=='200'){
				var startTime=data.startTime;
				var minMoney=data.minMoney;
				var maxMoney=data.maxMoney;
				var fixedMoney=data.fixedMoney;
				var status=data.status;
				$('input[name="startTime"]').val(startTime);
				$('input[name="minMoney"]').val(minMoney);
				$('input[name="maxMoney"]').val(maxMoney);
				$('input[name="fixedMoney"]').val(fixedMoney);
				if(status=='1'){
					$('input[name="status"]').attr('checked',true);
					$('input[name="status"]').val("1");
				}
			}			
			form.render();
		}, "json");
}
</script>
<style type="text/css">
.settingtextareabox{}
.settingtextareabox .gbutton{margin: 10px 0;}
.layui-input-block .gbutton{margin: 10px 125px;}
.layui-tab-item{width: 580px;}
.layui-form-label {width: 200px;text-align: center;}
.layui-input-block {margin-left: 230px;margin-bottom: 10px;}
.layui-input-short {display: -webkit-inline-box;width: 44%;}
.prompt {position: fixed;color: red;}
@media screen and (max-width: 1400px) {
	.layui-tab-title{height: 80px;white-space: inherit;}
}
</style>
</head>
<body>
<div class="bread">系统管理&raquo;提现设置</div>
<div class="layui-tab layui-tab-brief ">

  <ul class="layui-tab-title">
    <li class="layui-this">每日提款次数</li>
    <li>提款手续费</li>
    <li>提款限制打码量</li>
    <li>开启/关闭提款功能</li>
  </ul>
  <div class="layui-tab-content">
  		
		    <div class="layui-tab-item layui-show">
		    	<label class="layui-form-label">每日免费提款次数</label>
			    <div class="layui-input-block">
					<input id="param44" type="number" name="title" required  lay-verify="required|number" placeholder="请输入每日提款次数，0为不限制" autocomplete="off" class="layui-input" data="" t=""/>
			    </div>
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="cishu">保存</button>
			    </div>
		    </div>
		    <div class="layui-tab-item">
		    	<label class="layui-form-label">提款手续费(单位:%)</label>
			    <div class="layui-input-block">
					<input id="param45" type="number" name="title" required  lay-verify="required|number" placeholder="请输入提款手续费" autocomplete="off" class="layui-input" data="" t=""/>
			    </div>
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="shoufee">保存</button>
			    </div>
		    </div>
		    <div class="layui-tab-item">
		    	<label class="layui-form-label">提款限制打码量(单位:%)</label>
			    <div class="layui-input-block">
					<input id="param46" type="number" name="title" required  lay-verify="required|number" placeholder="100为1倍打码，200为2倍打码" autocomplete="off" class="layui-input" data="" t=""/>
			    </div>
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="mabei">保存</button>
			    </div>
		    </div>
		    <div class="layui-tab-item">
		    	<form class="layui-form" action="">
			    	<label class="layui-form-label">开启/关闭提款功能</label>
				    <div class="layui-input-block switch48">
<!-- 				    	<input id="param48" type="checkbox" checked="" name="title" lay-skin="switch" lay-text="开启|关闭"/> -->
		<!-- 				<input id="param48" type="number" name="title" required  lay-verify="required|number" placeholder="100为1倍打码，200为2倍打码" autocomplete="off" class="layui-input" data="" t=""/> -->
				    </div>
			    </form>
		    </div>
	    
	</div>
</div>
<script type="text/javascript">
layui.use('element', function(){
// 	var element = layui.element;
});
layui.use('form', function(){
	var form = layui.form;
	loadParam(form);
	backInit(form);
	form.on('switch(switch)', function(data){
		var obj=data.elem;
		var val=$(obj).val();
		if(val=='1'){
			$(obj).val("0");
		}else{
			$(obj).val("1");
		}
	});  
	
	form.on('submit(cishu)', function(data){
		var val=$('#param44').val();
		var id=$('#param44').attr('data');
		var type=$('#param44').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	form.on('submit(shoufee)', function(data){
		var val=$('#param45').val();
		var id=$('#param45').attr('data');
		var type=$('#param45').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	form.on('submit(mabei)', function(data){
		var val=$('#param46').val();
		var id=$('#param46').attr('data');
		var type=$('#param46').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	//监听指定开关
	form.on('switch(switch)', function(data){
		var checked = $(this).attr("checked");
		var val = 1;
		if(checked&&checked=="checked"){
			
		}else{
			val = 0;
		}
		var id=$(this).attr('data');
		var type=$(this).attr('t');
		if(val==0){
			layer.tips('关闭后用户将不能提款', data.othis);
		}else if(val==1){
			layer.tips('开启后用户可以提款', data.othis);
		}
		
		saveParam(id,type,val);
	});
	
});
var layedit =null;
var editorDaili=null;
layui.use('layedit', function(){
	  /* loadSettingNotice();
	  layedit = layui.layedit;
	  editorDaili=layedit.build('editor-daili',{
		  tool: ['strong','italic','underline','del','|','left','center','right','link','unlink']
	  }); 
// 	  layedit.sync(editorDaili);
	  editorYouhui=layedit.build('editor-youhui',{
		  tool: ['strong','italic','underline','del','|','left','center','right','link','unlink']
	  });  */
});

function saveParam(id,type,val){
	var loadObj = layer.load();
	$.post("/param/paramAction.do?method=saveParam", 
			{'paramId':id,
			'type':type,
			'value':val
			},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.msg('保存成功',{time: 800});
				}else{
					layer.msg('保存失败',{time: 800});
				}
		}, "json");
}
function saveBack(startTime,minMoney,maxMoney,fixedMoney,status){// 保存个人返水
	var loadObj = layer.load();
	$.post("/basedata/lotterySettingAction.do?method=saveBack", 
			{
			'startTime':startTime,
			'minMoney':minMoney,
			'maxMoney':maxMoney,
			'fixedMoney':fixedMoney,
			'status':status
			},
			function(data){
				layer.close(loadObj);
				var code=data.code;
				if(code=='200'){
					layer.msg('保存成功',{time: 800});
				}else{
					layer.msg('保存失败',{time: 800});
				}
		}, "json");
}


</script>
</body>
</html:html>