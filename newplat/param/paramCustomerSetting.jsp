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
				if(type=='14'||type=='37'||type=='47'||type=='91'){
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
<div class="bread">系统管理&raquo;客服设置</div>
<div class="layui-tab layui-tab-brief ">
  <ul class="layui-tab-title">
    <li class="layui-this">聊天室</li>
    <li>QQ客服地址</li>
    <li>在线客服地址</li>
    <li>App下载地址</li>
  </ul>
  <div class="layui-tab-content">
	    <div class="layui-tab-item layui-show">
	    	<label class="layui-form-label">聊天室</label>
		    <div class="layui-input-block">
				<input id="param14" type="tel" name="title" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param14">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">QQ客服地址设置</label>
		    <div class="layui-input-block">
				<input id="param37" type="tel" name="title" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param37">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">在线客服地址设置</label>
		    <div class="layui-input-block">
				<input id="param47" type="tel" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param47">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">App下载地址设置</label>
		    <div class="layui-input-block">
				<input id="param91" type="tel" name="title" required  lay-verify="required" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param91">保存</button>
		    </div>
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
// 	backInit(form);
	form.on('switch(switch)', function(data){
		var obj=data.elem;
		var val=$(obj).val();
		if(val=='1'){
			$(obj).val("0");
		}else{
			$(obj).val("1");
		}
	});  
	
	form.on('submit(param14)', function(data){
		var val=$('#param14').val();
		var id=$('#param14').attr('data');
		var type=$('#param14').attr('t');
		saveParam(id,type,val);
	    return false;
	});
	form.on('submit(param37)', function(data){
		var val=$('#param37').val();
		var id=$('#param37').attr('data');
		var type=$('#param37').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	form.on('submit(param47)', function(data){
		var val=$('#param47').val();
		var id=$('#param47').attr('data');
		var type=$('#param47').attr('t');
		saveParam(id,type,val);
		return false;
	}); 
	form.on('submit(param91)', function(data){
		var val=$('#param91').val();
		var id=$('#param91').attr('data');
		var type=$('#param91').attr('t');
		saveParam(id,type,val);
		return false;
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