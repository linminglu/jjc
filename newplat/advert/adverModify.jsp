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
	var frm = document.advertForm;
	if(frm["advert.title"].value.trim().length==0){
		alert("请输入标题");
		frm["advert.title"].focus();
		return false;
	}
	if(frm["advert.type"].checked==false){
 		alert("请选择内容来源");
 		return false;
 	}
	var html = editor.$txt.html();
	$('#content').val(html);
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
window.onload=function(){
	show(<bean:write name="advertForm" property="advert.type"/>);
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
<div class="bread">广告管理  &raquo; 
 <logic:equal name="advertForm" property="adType" value="1">
 轮播广告
 </logic:equal>
 <logic:equal name="advertForm" property="adType" value="2">
通知
 </logic:equal>
 &raquo; 
<logic:empty name="advertForm" property="advert.id">新增</logic:empty>
<logic:notEmpty name="advertForm" property="advert.id">修改</logic:notEmpty>
</div>
<html:form action="/advertAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="advertForm" property="advert.id"/>
<html:hidden name="advertForm" property="advert.adType"/>
<html:hidden name="advertForm" property="adType" styleId="adType"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<html:text property="advert.title" name="advertForm" maxlength="20" style="width: 540px;"/>
      	</td>
    </tr>
   	<tr class="tr-color-body">
      	<th align="right">内容来源</th>
      	<td>
      		<html:radio property="advert.type" value="1" onclick="show('1');" styleId="type1"><label for="type1">外部网址</label></html:radio>
      		<html:radio property="advert.type" value="2" onclick="show('2');" styleId="type2"><label for="type2">发布内容</label></html:radio>
      	</td>
   	</tr>
   	<tr class="tr-color-body" id="linktext">
      	<th align="right">网址</th>
      	<td>
      		<html:text property="advert.link" name="advertForm" style="width: 540px;"></html:text>
      	</td>
   	</tr>
	<tr class="tr-color-body" id="contentbox">
      	<th align="right">发布内容</th>
      	<td>
        	<html:hidden property="advert.content" name="advertForm" styleId="content"></html:hidden>
      		<div class="container" style="width: 540px;">
		        <div id="editor-trigger">
		        </div>
		    </div>
      	</td>
    </tr>
     <logic:equal name="advertForm" property="adType" value="1">
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">图片</th>
      	<td>
      		<html:file property="file" name="advertForm" size="50"/><br/>
      		<html:hidden property="advert.img" name="advertForm" />
      		<c:if test="${advertForm.advert.img ne null}">
      			<img src="..<c:out value="${advertForm.advert.img}"/>" alt="<c:out value="${advertForm.advert.title}"/>" title="<c:out value="${advertForm.advert.title}"/>" width="540"/>
      		</c:if>
      		<p class="red">建议尺寸
      		 <logic:equal name="advertForm" property="adType" value="1">
			 	[宽x高]1000x320
			 </logic:equal>
      		</p>
      	</td>
    </tr>
    </logic:equal>
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