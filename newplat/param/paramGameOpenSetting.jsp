<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file = "../common/inc_file.jsp"%>
<html:html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
<title>管理平台</title>
<script type="text/javascript">
$(document).ready(function(){
	
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
				if(type=='30'||type=='31'||type=='38'||type=='39'||type=='40'||type=='41'||type=='42'){
					$("input[name='param"+type+"']").attr('data',paramId);
					$("input[name='param"+type+"']").attr('t',type);
					$("input[name='param"+type+"'][value='"+value+"']").attr('checked',true);
				}
			}
		 form.render();
		}, "json");
}
</script>
<style type="text/css">
.settingtextareabox{}
.settingtextareabox .gbutton{margin: 10px 0;}
.layui-input-block .gbutton{margin: 10px 0;}
.layui-tab-item{width: 580px;}
</style>
</head>
<body>
<div class="bread">开奖管理&raquo;开奖设置</div>
<div class="layui-tab layui-tab-brief ">
  <!-- <ul class="layui-tab-title">
    <li class="layui-this">开奖设置</li>
  </ul> -->
  <div class="layui-tab-content">
    <div class="layui-tab-item layui-show">
    	<form class="layui-form" action="#" lay-filter="reform">
   		<div class="layui-form-item">
   			<label class="layui-form-label">北京三分彩</label>
          	<div class="layui-input-block">
				<input type="radio" name="param42" value="0" title="随机" lay-filter="gameOpen"/>
      			<input type="radio" name="param42" value="3" title="3次" lay-filter="gameOpen"/>
      			<input type="radio" name="param42" value="5" title="5次" lay-filter="gameOpen"/>
          	</div>
        </div>
   		<div class="layui-form-item">
			<label class="layui-form-label">极速赛车</label>
			<div class="layui-input-block">
				<input type="radio" name="param30" value="0" title="随机" lay-filter="gameOpen"/>
	  			<input type="radio" name="param30" value="3" title="3次" lay-filter="gameOpen"/>
	  			<input type="radio" name="param30" value="5" title="5次" lay-filter="gameOpen"/>
      		</div>
		</div>
   		<div class="layui-form-item">
			<label class="layui-form-label">极速飞艇</label>
			<div class="layui-input-block">
				<input type="radio" name="param31" value="0" title="随机" lay-filter="gameOpen"/>
	  			<input type="radio" name="param31" value="3" title="3次" lay-filter="gameOpen"/>
	  			<input type="radio" name="param31" value="5" title="5次" lay-filter="gameOpen"/>
      		</div>
		</div>
		<div class="layui-form-item">
   			<label class="layui-form-label">三分PK10</label>
          	<div class="layui-input-block">
				<input type="radio" name="param38" value="0" title="随机" lay-filter="gameOpen"/>
      			<input type="radio" name="param38" value="3" title="3次" lay-filter="gameOpen"/>
      			<input type="radio" name="param38" value="5" title="5次" lay-filter="gameOpen"/>
          	</div>
        </div>
		<div class="layui-form-item">
   			<label class="layui-form-label">急速六合彩</label>
          	<div class="layui-input-block">
				<input type="radio" name="param39" value="0" title="随机" lay-filter="gameOpen"/>
      			<input type="radio" name="param39" value="3" title="3次" lay-filter="gameOpen"/>
      			<input type="radio" name="param39" value="5" title="5次" lay-filter="gameOpen"/>
          	</div>
        </div>
		<div class="layui-form-item">
   			<label class="layui-form-label">五分彩</label>
          	<div class="layui-input-block">
				<input type="radio" name="param40" value="0" title="随机" lay-filter="gameOpen"/>
      			<input type="radio" name="param40" value="3" title="3次" lay-filter="gameOpen"/>
      			<input type="radio" name="param40" value="5" title="5次" lay-filter="gameOpen"/>
          	</div>
        </div>
		<div class="layui-form-item">
   			<label class="layui-form-label">北京时时彩</label>
          	<div class="layui-input-block">
				<input type="radio" name="param41" value="0" title="随机" lay-filter="gameOpen"/>
      			<input type="radio" name="param41" value="3" title="3次" lay-filter="gameOpen"/>
      			<input type="radio" name="param41" value="5" title="5次" lay-filter="gameOpen"/>
          	</div>
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
	
	form.on('radio(gameOpen)', function(data){
		//在线充值开关
		var loadObj = layer.load();
		var obj=data.elem;
		var val=data.value;
		var id=$(obj).attr('data');
		var type=$(obj).attr('t');
		
		saveParam(id,type,val);
	});  
	
	
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
</script>
</body>
</html:html>