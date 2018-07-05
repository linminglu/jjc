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
	var frm = document.gaForm;
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
window.onload=function(){

}
function show(type){
	if(type=='1'){
		$('#linktext').show();
		$('#contentbox').hide();
	}else if(type=='2'){
		$('#contentbox').show();
		$('#linktext').hide();
	}
}

</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">开奖API网址
 &raquo; 
<logic:empty name="gaForm" property="sessionInfo.infoId">新增</logic:empty>
<logic:notEmpty name="gaForm" property="sessionInfo.infoId">修改</logic:notEmpty>
</div>
<html:form action="/gaAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="gaForm" property="sessionInfo.infoId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">游戏名称</th>
      	<td>
        	<html:text property="sessionInfo.gameTitle" name="gaForm"  disabled="true"/>
      	</td>
    </tr>
	<tr class="tr-color-body" >
      	<th align="right">开彩网API</th>
      	<td>
        	<html:text property="sessionInfo.kaicaiUrl" name="gaForm"    style="width:1000px;"/>
      	</td>
    </tr>
   <tr class="tr-color-body" >
      	<th align="right">彩票控API</th>
      	<td>
        	<html:text property="sessionInfo.caipiaoUrl" name="gaForm"  style="width:1000px;"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">当前使用</th>
      	<td>
      		<html:radio property="sessionInfo.urlSwitch" value="1" styleId="gender1" >
      			<label for="gender1">开彩网</label> 
      		</html:radio>
      		<html:radio property="sessionInfo.urlSwitch" value="2" styleId="gender2">
      			<label for="gender2">彩票控</label> 
      		</html:radio>
      	</td>
    </tr>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
<script type="text/javascript">
// 创建编辑器
var editor = new wangEditor('editor-trigger');
editor.config.menus = ['bold','eraser','quote','unorderlist','orderlist','alignleft','aligncenter','alignright','|',
           			'link','unlink','|','undo','redo'];
editor.config.menuFixed = false;
editor.create();
//初始化编辑器的内容
editor.$txt.html($('#content').val());
</script>
</body>
</html:html>