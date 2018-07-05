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
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_weditor.jsp"%>
<script language="javascript">
function formSubmit(){

	var pTId = $("#pTId").val();
	if(!pTId){
		alert("请选择要开奖的玩法");
		return false;
	}
	
	var oId = $("#oId").val();
	if(!oId){
		alert("请选择开奖结果");
		return false;
	}
	
	if(confirm("确认开奖？")){
		return true;
	}
	return false;
}

$(function(){
	var msg = $("#msg").val();
	if(msg){
		alert(msg);
	}
})

function getRate(oId){
	$.post("fieldAction.do?method=getRateByOId&oId="+oId,
			function(data) {
				if(data!=null) {
					$("#rate").val(data);
				}
		});
}

function getOptions(ptId){
	$("#oId").empty();
	var html = '';
	html +="<option>请选择</option>";
	$.post("fieldAction.do?method=getOptions",{'ptId':ptId},function(result){
		if(result!=null && result.length>0){
			var optionList = JSON.parse(result);
			 $.each(optionList,function(i){
				html +="<option value="+optionList[i].oId+">"+optionList[i].title+"</option>";
			});
			$(html).appendTo("#oId");
		}
	})
}
</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">开奖 
</div>
<html:form action="/fieldAction.do?method=saveOpen" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="fieldForm" property="field.fId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">比赛局名称</th>
      	<td>
        	<html:text property="field.title" name="fieldForm" maxlength="20" style="width: 540px;" styleId="title" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">赛事</th>
      	<td>
        	<html:text property="field.mTitle" name="fieldForm" maxlength="20" style="width: 540px;" styleId="mTitle" disabled="true"/>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">红方</th>
      	<td>
        	<html:text property="field.red" name="fieldForm" maxlength="20" style="width: 540px;" styleId="red" disabled="true"/>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">蓝方</th>
      	<td>
        	<html:text property="field.blue" name="fieldForm" maxlength="20" style="width: 540px;" styleId="blue" disabled="true"/>
      	</td>
    </tr>
    
    <tr class="tr-color-body">
      	<th align="right">请选择要开奖的玩法</th>
      	<td>
        	<html:select property="playType.pTId" name="fieldForm" style="width: 540px;" styleId="pTId" onchange="getOptions(this.value)">
        		<html:option value="">请选择</html:option>
			       <c:if test="${not empty playList}">
			          <html:options collection="playList" property="pTId" labelProperty="title"/>
			       </c:if>
        	</html:select>
      	</td>
    </tr>
    
   	<tr class="tr-color-body">
      	<th align="right">请选择开奖结果</th>
      	<td>
        	<html:select property="option.oId" name="fieldForm" style="width: 540px;" styleId="oId" onchange="getRate(this.value)">
        		<html:option value="">请选择</html:option>
        	</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">赔率</th>
      	<td>
        	<html:text property="option.rate" name="fieldForm" maxlength="20" style="width: 540px;" styleId="rate" readonly="true"/>
      	</td>
    </tr>
    
	
</table>
<div class="div-bottom-bar">
	<input id="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
<script type="text/javascript">
$("#submit").click(function(){
	var playId = $("#pTId").val();
	var oId = $("#oId").val();
	if(!playId){
		layer.msg('请选择玩法',{time: 800});
		return false;
	}
	
	if(!oId){
		layer.msg('请选择开奖结果',{time: 800});
		return false;
	}
	
	$.post("fieldAction.do?method=saveOpen", 
			{'playId':playId,'oId':oId},
			function(data){
				var code=data.code;
				if(code=='200'){
					layer.open({
						  content: data.msg,
						  yes: function(index, layero){
							  location = "fieldAction.do?method=fieldPlay";
						  }
						});
				}else{
					layer.msg('开奖失败',{time: 800});
				}
		}, "json");
})
</script>
</html:html>