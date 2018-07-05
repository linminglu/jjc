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
				}else if(type=='51'||type=='52'||type=='53'||type=='54'||type=='55'
						||type=='56'||type=='57'||type=='58'||type=='59'||type=='60'
						||type=='61'||type=='62'||type=='63'||type=='64'||type=='65'
						||type=='66'||type=='67'||type=='68'||type=='69'||type=='70'){
					var beizhu=obj.beizhu;
					var status=obj.status;
					if(value.indexOf("&")>0){
						var pars = value.split("&");
						var timeStr = pars[0];
						var rateStr = pars[1];
						var time = $(".partten[data='"+type+"']").find("input[name='startTime']");
						$(time).val(timeStr);
						var fixedMoney = $(".partten[data='"+type+"']").find("input[name='fixedMoney']");
						$(fixedMoney).val(rateStr);
					}else{
						var fixedMoney = $(".partten[data='"+type+"']").find("input[name='fixedMoney']");
						$(fixedMoney).val(value);
					}
					
					var pars2 = beizhu.split(",");
					var minMoneyStr = pars2[0];
					var maxMoneyStr = pars2[1];
					var minMoney = $(".partten[data='"+type+"']").find("input[name='minMoney']");
					$(minMoney).val(minMoneyStr);
					var maxMoney = $(".partten[data='"+type+"']").find("input[name='maxMoney']");
					$(maxMoney).val(maxMoneyStr);
					
					var shtml = "";
					if(status==0){
						shtml += "<input type=\"checkbox\" name=\"status\" lay-skin=\"switch\" lay-text=\"开|关\" lay-filter=\"switch\" value=\"0\"/>";
					}else if(status==1){
						shtml += "<input type=\"checkbox\" name=\"status\" checked=\"\" lay-skin=\"switch\" lay-text=\"开|关\" lay-filter=\"switch\" value=\"0\"/>";
					}
					var openswitch =  $(".partten[data='"+type+"'] div.switch");
					openswitch.html(shtml);
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
<div class="bread">系统管理&raquo;赠送设置</div>
<div class="layui-tab layui-tab-brief ">
  <ul class="layui-tab-title">
    <li class="layui-this">会员打码返水</li>
    <li>会员打码返水代理</li>
    <li>会员充值返水</li>
    <li>会员充值返水代理</li>
    <li>注册赠送金额</li>
    <li>签到赠送金额</li>
    <!-- <li>聊天室</li>
    <li>QQ客服地址</li>
    <li>每日提款次数</li>
    <li>提款手续费</li>
    <li>提款限制打码量</li> -->
  </ul>
  <div class="layui-tab-content">
	  <div class="layui-tab-item layui-show">
			<form class="layui-form" action="#" lay-filter="betFeedback">
				<div class="partten" data="51">
					<label class="layui-form-label">返水时间(整数点)</label>
				    <div class="layui-input-block">
				    	<input property="" name="startTime" class="layui-input"  onclick="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})" readonly="readonly"/>
				    </div>
			    	<label class="layui-form-label">打码金额</label>
				    <div class="layui-input-block">
				    	<input onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9" name="minMoney" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9" name="maxMoney" required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">打码返水数值(%)</label>
				    <div class="layui-input-block">
						<input type="" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" name="fixedMoney" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">打码返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				
				<div class="partten" data="52">
			    	<label class="layui-form-label">打码金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number" class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number" class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">打码返水数值(%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">打码返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
					<hr>
				</div>
				<div class="partten" data="53">
			    	<label class="layui-form-label">打码金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number" class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number" class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">打码返水数值(%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">打码返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
					<hr>
				</div>
				<div class="partten" data="54">
			    	<label class="layui-form-label">打码金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number" class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number" class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">打码返水数值(%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">打码返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
					<hr>
				</div>
				<div class="partten" data="55">
			    	<label class="layui-form-label">打码金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number" class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number" class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">打码返水数值(%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">打码返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				</div>
		    	
			    <div class="layui-input-block">
			    	<span class="" style="color: red;">如果会员打码量小于最低一档的最小值，没有返水；</span>
			    	<br/>
				  	<span class="" style="color: red;">如果会员打码量大于最高一档的最大值，将按照最高档来返水。</span>
			      <button class="gbutton" lay-submit="" >保存</button>
			    </div>
			 </form>
	    </div>
	    <div class="layui-tab-item">
		    <form class="layui-form" action="#" lay-filter="betFeedAgentback">
		    	<div class="partten" data="66">
		    		<label class="layui-form-label">返水时间(整数点)</label>
				    <div class="layui-input-block">
				    	<input property="" name="startTime" class="layui-input"  onclick="WdatePicker({lang:'zh-cn',dateFmt:'HH:mm:ss'})" readonly="readonly"/>
				    </div>
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"   required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">返水数值(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="67">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">返水数值(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="68">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">返水数值(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="69">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">返水数值(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="70">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">返水数值(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="">保存</button>
			    </div>
			</form>
	    </div>
	    
	    <div class="layui-tab-item">
	    	<form class="layui-form" action="#" lay-filter="rechargeFeedback">
		    	<div class="partten" data="56">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				
				<div class="partten" data="57">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				
				<div class="partten" data="58">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input  name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input  name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="59">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="60">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="">保存</button>
			    </div>
			</form>
	    </div>
	    
	    <div class="layui-tab-item">
		    <form class="layui-form" action="#" lay-filter="rechargeFeedAgentback">
		    	<div class="partten" data="61">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水代理设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="62">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水代理设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="63">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水代理设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="64">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true" required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水代理设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				<div class="partten" data="65">
			    	<label class="layui-form-label">充值金额</label>
				    <div class="layui-input-block">
				    	<input name="minMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  readonly="true"  required lay-verify="number"  class="layui-input layui-input-short"/>&nbsp;&nbsp;——
				    	<input name="maxMoney" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"  required lay-verify="number"  class="layui-input layui-input-short"/>
				    </div>
				    <label class="layui-form-label">会员充值返水代理设置(单位:%)</label>
				    <div class="layui-input-block">
						<input type="" name="fixedMoney" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
				    </div>
				    <label class="layui-form-label">充值返水开关</label>
				    <div class="layui-input-block">
				    	<div class="switch">
				    		
				    	</div>
	<!-- 						<input type="checkbox" name="status" lay-skin="switch" lay-text="开|关" lay-filter="switch" value="0"/> -->
				    </div>
				    <hr>
				</div>
				
			    <div class="layui-input-block">
			      <button class="gbutton" lay-submit="" lay-filter="">保存</button>
			    </div>
			</form>
	    </div>
	    
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">会员注册赠送金额设置(单位:元)</label>
		    <div class="layui-input-block">
				<input id="param15" onkeyup="value=value.replace(/[^\d.]/g,'')" name="title" required maxlength="3"   lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param15">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">会员签到赠送金额设置(单位:元)</label>
		    <div class="layui-input-block">
				<input id="param16" type="" name="title" required  lay-verify="required|number" placeholder="" autocomplete="off" class="layui-input"/>
		    	<span class="prompt" style="position: fixed;color: red;">(填0表示不送;)</span>
		    	<br/>
		    	<span class="prompt" style="position: fixed;color: red;">(如果输入两个数值，赠送金额将在两个数值之间取随机值，如0.1,0.5，赠送金额将会是0.1~0.5之前的随机值；)</span>
		    	<br/>
		    	<span class="prompt" style="position: fixed;color: red;">(如果输入三或者三个以上个数值，赠送金额将在三个数值中随机取一个，如0.1,0.5,1.0，赠送金额将会是0.1,0.5,1.0中的一个。)</span>	
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="param16">保存</button>
		    </div>
	    </div>
	    <!-- <div class="layui-tab-item">
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
	    	<label class="layui-form-label">每日免费提款次数</label>
		    <div class="layui-input-block">
				<input id="param44" type="number" name="title" required  lay-verify="required|number" placeholder="请输入每日提款次数，0为不限制" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="cishu">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">提款手续费(单位:%)</label>
		    <div class="layui-input-block">
				<input id="param45" type="number" name="title" required  lay-verify="required|number" placeholder="请输入提款手续费" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="shoufee">保存</button>
		    </div>
	    </div>
	    <div class="layui-tab-item">
	    	<label class="layui-form-label">提款限制打码量(单位:%)</label>
		    <div class="layui-input-block">
				<input id="param46" type="number" name="title" required  lay-verify="required|number" placeholder="100为1倍打码，200为2倍打码" autocomplete="off" class="layui-input"/>
		    </div>
		    <div class="layui-input-block">
		      <button class="gbutton" lay-submit="" lay-filter="mabei">保存</button>
		    </div>
	    </div> -->
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
	
	form.on('submit(betFeedback)', function(data){
		
		var map = {};
		var items = [];
		var parttens = $(".layui-show .partten");
		if(parttens&&parttens.length>0){
			for(var i = 0;i<parttens.length;i++){
				var currentPartten = parttens[i];
				if(i==0){
					var startTimeStr = $(currentPartten).find("input[name='startTime']").val();
					map['startTime'] = startTimeStr;
					
// 					var reg = new RegExp("^\d+:00:00$");
// 					if (!reg.test(startTimeStr)) {
// 						alert("请选择整点时间")
// 						return false;
// 					}
				}
				
				var minMoneyStr = $(currentPartten).find("input[name='minMoney']").val();
				var maxMoneyStr = $(currentPartten).find("input[name='maxMoney']").val();
				var openswitch = $(currentPartten).find("input[name='status']");
				var rate = $(currentPartten).find("input[name='fixedMoney']").val();
				var checked = $(openswitch).attr("checked");
				var statusStr = 1;
				if(checked&&checked=="checked"){
					
				}else{
					statusStr = 0;
				}
				// 验证
				if(isNaN(minMoneyStr)||isNaN(maxMoneyStr)||isNaN(rate)){
					alert("请填写正确的数值");
					return false;
				}
				if(parseFloat(minMoneyStr)>parseFloat(maxMoneyStr)){
					alert("最小值应小于最大值");
					return false;
				}
				if(parseFloat(rate)>20){
					alert("返水比例不应超过20%");
					return false;
				}
				
				var obj = {};
				obj['minMoney'] = minMoneyStr;
				obj['maxMoney'] = maxMoneyStr;
				obj['status'] = ""+statusStr;
				obj['rate'] = rate;
				obj['type'] = $(currentPartten).attr("data");
				items.push(obj);
			}
		}
		map['items'] = items;
		saveBack(map);
		return false;
	});  
	form.on('submit(betFeedAgentback)', function(data){
		
		var map = {};
		var items = [];
		var parttens = $(".layui-show .partten");
		if(parttens&&parttens.length>0){
			for(var i = 0;i<parttens.length;i++){
				var currentPartten = parttens[i];
				if(i==0){
					var startTimeStr = $(currentPartten).find("input[name='startTime']").val();
					map['startTime'] = startTimeStr;
				}
				
				var minMoneyStr = $(currentPartten).find("input[name='minMoney']").val();
				var maxMoneyStr = $(currentPartten).find("input[name='maxMoney']").val();
				var openswitch = $(currentPartten).find("input[name='status']");
				var rate = $(currentPartten).find("input[name='fixedMoney']").val();
				var checked = $(openswitch).attr("checked");
				var statusStr = 1;
				if(checked&&checked=="checked"){
					
				}else{
					statusStr = 0;
				}
				// 验证
				if(isNaN(minMoneyStr)||isNaN(maxMoneyStr)||isNaN(rate)){
					alert("请填写正确的数值");
					return false;
				}
				if(parseFloat(minMoneyStr)>parseFloat(maxMoneyStr)){
					alert("最小值应小于最大值");
					return false;
				}
				if(parseFloat(rate)>20){
					alert("返水比例不应超过20%");
					return false;
				}
				var obj = {};
				obj['minMoney'] = minMoneyStr;
				obj['maxMoney'] = maxMoneyStr;
				obj['status'] = ""+statusStr;
				obj['rate'] = rate;
				obj['type'] = $(currentPartten).attr("data");
				items.push(obj);
			}
		}
		map['items'] = items;
		saveBack(map);
		return false;
	});  
	form.on('submit(rechargeFeedback)', function(data){
		
		var map = {};
		var items = [];
		var parttens = $(".layui-show .partten");
		if(parttens&&parttens.length>0){
			for(var i = 0;i<parttens.length;i++){
				var currentPartten = parttens[i];
				
				if(i==0){
					var startTimeStr = $(currentPartten).find("input[name='startTime']").val();
					map['startTime'] = startTimeStr;
					
// 					var reg = new RegExp("^\d+:00:00$");
// 					if (!reg.test(startTimeStr)) {
// 						alert("请选择整点时间")
// 						return false;
// 					}
				}
				
				var minMoneyStr = $(currentPartten).find("input[name='minMoney']").val();
				var maxMoneyStr = $(currentPartten).find("input[name='maxMoney']").val();
				var openswitch = $(currentPartten).find("input[name='status']");
				var rate = $(currentPartten).find("input[name='fixedMoney']").val();
				var checked = $(openswitch).attr("checked");
				var statusStr = 1;
				if(checked&&checked=="checked"){
					
				}else{
					statusStr = 0;
				}
				// 验证
				if(isNaN(minMoneyStr)||isNaN(maxMoneyStr)||isNaN(rate)){
					alert("请填写正确的数值");
					return false;
				}
				if(parseFloat(minMoneyStr)>parseFloat(maxMoneyStr)){
					alert("最小值应小于最大值");
					return false;
				}
				if(parseFloat(rate)>20){
					alert("返水比例不应超过20%");
					return false;
				}
				var obj = {};
				obj['minMoney'] = minMoneyStr;
				obj['maxMoney'] = maxMoneyStr;
				obj['status'] = ""+statusStr;
				obj['rate'] = rate;
				obj['type'] = $(currentPartten).attr("data");
				items.push(obj);
			}
		}
		map['items'] = items;
		saveBack(map);
		return false;
	});  
	form.on('submit(rechargeFeedAgentback)', function(data){
		
		var map = {};
		var items = [];
		var parttens = $(".layui-show .partten");
		if(parttens&&parttens.length>0){
			for(var i = 0;i<parttens.length;i++){
				var currentPartten = parttens[i];
				if(i==0){
					var startTimeStr = $(currentPartten).find("input[name='startTime']").val();
					map['startTime'] = startTimeStr;
					
				}
				var minMoneyStr = $(currentPartten).find("input[name='minMoney']").val();
				var maxMoneyStr = $(currentPartten).find("input[name='maxMoney']").val();
				var openswitch = $(currentPartten).find("input[name='status']");
				var rate = $(currentPartten).find("input[name='fixedMoney']").val();
				var checked = $(openswitch).attr("checked");
				var statusStr = 1;
				if(checked&&checked=="checked"){
					
				}else{
					statusStr = 0;
				}
				// 验证
				if(isNaN(minMoneyStr)||isNaN(maxMoneyStr)||isNaN(rate)){
					alert("请填写正确的数值");
					return false;
				}
				if(parseFloat(minMoneyStr)>parseFloat(maxMoneyStr)){
					alert("最小值应小于最大值");
					return false;
				}
				if(parseFloat(rate)>20){
					alert("返水比例不应超过20%");
					return false;
				}
				var obj = {};
				obj['minMoney'] = minMoneyStr;
				obj['maxMoney'] = maxMoneyStr;
				obj['status'] = ""+statusStr;
				obj['rate'] = rate;
				obj['type'] = $(currentPartten).attr("data");
				items.push(obj);
			}
		}
		map['items'] = items;
		saveBack(map);
		return false;
	});  
	
	form.on('submit(backInit)', function(data){
		var startTime=$('input[name="startTime"]').val();
		var minMoney=$('input[name="minMoney"]').val();
		var maxMoney=$('input[name="maxMoney"]').val();
		var fixedMoney=$('input[name="fixedMoney"]').val();
		var status=$('input[name="status"]').val();
		saveBack(startTime,minMoney,maxMoney,fixedMoney,status);
		return false;
	});  
	form.on('submit(param11)', function(data){
		var val=$('#param11').val();
		var id=$('#param11').attr('data');
		var type=$('#param11').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	form.on('submit(param12)', function(data){
		var val=$('#param12').val();
		var id=$('#param12').attr('data');
		var type=$('#param12').attr('t');
		saveParam(id,type,val);
		return false;
	});  
	form.on('submit(param13)', function(data){
		var time=$('input[name="param13Time"]').val();
		var val=$('#param13').val();
		var id=$('#param13').attr('data');
		var type=$('#param13').attr('t');
		saveParam(id,type,time+"&"+val);
		return false;
	});  
	form.on('submit(param14)', function(data){
		var val=$('#param14').val();
		var id=$('#param14').attr('data');
		var type=$('#param14').attr('t');
		saveParam(id,type,val);
	    return false;
	});
	form.on('submit(param15)', function(data){
		var val=$('#param15').val();
		var id=$('#param15').attr('data');
		var type=$('#param15').attr('t');
		saveParam(id,type,val);
	    return false;
	});
	form.on('submit(param16)', function(data){
		var val=$('#param16').val();
		var id=$('#param16').attr('data');
		var type=$('#param16').attr('t');
		var reg = new RegExp("^(\\d+(.\\d+)?)+(,\\d+(.\\d+)?)*$");
		if(val=="0"){
			
		}else {
			if (!reg.test(val)) {
				alert("请输入正确的值")
				return false;
			}
			var arr=val.split(",");
			if(arr[1]){
				if(parseFloat(arr[0])>=parseFloat(arr[1])){
					alert("开始值要大于结束值")
					return false;
				}
			}
		}
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
	
	
	linkedMaxAndMin();
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
function saveBack(map){// 保存个人返水
	var loadObj = layer.load();
	
	$.post("/basedata/lotterySettingAction.do?method=saveBack", 
			{
			'map':JSON.stringify(map),
			'test':"test"
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

function linkedMaxAndMin(){
	
	var parttens = $(" .partten");
	if(parttens&&parttens.length>0){
		for(var i = 0;i<parttens.length;i++){
			if(i<parttens.length-1){
				var currentPartten = parttens[i];
				var maxMoney = $(currentPartten).find("input[name='maxMoney']");
				if(maxMoney&&maxMoney.length>0){
					maxMoney.unbind("keyup");
					maxMoney.bind("keyup",function(){
						var partten = $(this).parent().parent();
						var minMoney = $(partten).next().find("input[name='minMoney']");
						minMoney.val($(this).val());
					});
				}
			}
		}
	}
	
};


</script>
</body>
</html:html>