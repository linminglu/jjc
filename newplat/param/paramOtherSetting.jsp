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
				if(type=='49'){
					$('#param'+type).val(value);
					$('#param'+type).attr('data',paramId);
					$('#param'+type).attr('t',type);
// 					if(value=='1'){
// 						$('#param'+type).attr('checked',true);
// 					}
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
<div class="bread">系统管理&raquo;其他设置</div>
<div class="layui-tab layui-tab-brief ">
  <ul class="layui-tab-title">
    <li class="layui-this">关闭彩种提示语</li>
  </ul>
  <div class="layui-tab-content">
	    <div class="layui-tab-item layui-show">
	    	<label class="layui-form-label">关闭彩种提示语</label>
		    <div class="layui-input-block">
				<input id="param49" type="tel" name="title" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		    	<button class="layui-btn layui-btn-primary option">彩种维护</button>
		    	<button class="layui-btn layui-btn-primary option">彩种休市，节后开放</button>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param49">保存</button>
		    </div>
	    </div>
	</div>
</div>
<script type="text/javascript">

$(".option").bind("click",function(){
	$("#param49").val($(this).text());
});

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
	
	form.on('submit(param49)', function(data){
		var val=$('#param49').val();
		var id=$('#param49').attr('data');
		var type=$('#param49').attr('t');
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