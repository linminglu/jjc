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
<%@ include file = "../common/inc_ueditor.jsp"%> 
<script language="javascript" src="../js/platjs.js"></script>
<script type="text/javascript" src="<%=webContext %>/js/js_z/md5.js"></script>
<script language="javascript">
function xmlHttpRequest(){
	if( window.ActiveXObject ){
		return new ActiveXObject("Microsoft.xmlhttp");
	}else if( window.XMLHttpRequest ){
		return new XMLHttpRequest();
	}
}
function formSubmit(){
	var frm = document.promotionForm;
	if(frm["promotion.title"].value.trim().length==0){
		alert("请输入标题");
		frm["promotion.title"].focus();
		return false;
	}
	if(frm["startDate"].value.trim().length==0){
		alert("请选择开始时间");
		frm["startDate"].focus();
		return false;
	}
	var html = editor.$txt.html();
	if(!html){
		alert("请填写内容");
		return false;
	}
	$('#promotion.content').val(html);
	if(frm["file"].value.trim().length==0 && frm["promotion.img"].value.trim().length==0){
		alert("请选择图片");
		frm["file"].focus();
		return false;
	}
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">活动设置&raquo;优惠活动 &raquo;
<logic:empty name="promotionForm" property="promotion.id">新增</logic:empty>
<logic:notEmpty name="promotionForm" property="promotion.id">修改</logic:notEmpty>
</div>
<html:form action="/promotionAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="promotionForm" property="promotion.id"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<html:text property="promotion.title" name="promotionForm" maxlength="20" style="width: 540px;"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
    	<th align="right">活动开始时间</th>
      	<td>
    		<html:text property="startDate" name="promotionForm" styleClass="Wdate" onclick="WdatePicker({lang:'zh-cn'})" readonly="readonly"/>
    	</td>
    </tr>
	<%-- <tr class="tr-color-body" id="contentbox">
      	<th align="right">发布内容</th>
      	<td>
        	<html:hidden property="promotion.content" name="promotionForm" styleId="content"></html:hidden>
      		<div class="container" style="width: 540px;">
		        <div id="editor-trigger">
		        </div>
		    </div>
      	</td>
    </tr> --%>
    <tr class="tr-color-body">
      	<th align="right">内容&nbsp;</th>
	      	<td>
				<textarea id="promotion.content" name="promotion.content" style="width:650px;" class="editortextareas"><bean:write name="promotionForm" property="promotion.content"/></textarea>
				<script type="text/javascript">UECreator.def("promotion.content",0,350,false);</script>
	      	</td>
    </tr>
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">图片</th>
      	<td>
      		<html:file property="file" name="promotionForm" size="50"/><br/>
      		<html:hidden property="promotion.img" name="promotionForm" />
      		<c:if test="${promotionForm.promotion.img ne null}">
      			<img src="..<c:out value="${promotionForm.promotion.img}"/>" alt="<c:out value="${promotionForm.promotion.title}"/>" title="<c:out value="${promotionForm.promotion.title}"/>" height="160px" width="700px"/>
      		</c:if>
      		<p class="red">建议尺寸[宽x高]700x160
      		</p>
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