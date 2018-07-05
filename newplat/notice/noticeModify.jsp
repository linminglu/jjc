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
function changeType(){
	var s=$("#selectType").val().trim();
	var frm=document.noticeForm;
	frm["notice.type"].value=s;
}
function formSubmit(){
	var frm = document.noticeForm;
	var type = frm["notice.type"].value;
	if(frm["notice.title"].value.trim().length==0){
		alert("请输入标题");
		frm["notice.title"].focus();
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
	var type=$("#type").val();
	$("#selectType").val(type);
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
<div class="bread">广告/通知管理&raquo;
<c:if test = "${noticeForm.notice.type==3}">首页公告 </c:if>
<c:if test = "${noticeForm.notice.type==2}">代理制度 </c:if>
<c:if test = "${noticeForm.notice.type==1}">优惠活动 </c:if>
<c:if test = "${noticeForm.notice.type==4}">充值公告 </c:if>
 &raquo;
<logic:empty name="noticeForm" property="notice.id">新增</logic:empty>
<logic:notEmpty name="noticeForm" property="notice.id">修改</logic:notEmpty>
</div>
<html:form action="/noticeAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="noticeForm" property="notice.id"/>
<html:hidden name="noticeForm" property="notice.status"/>
<html:hidden property="notice.type" name="noticeForm" styleId="type"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<html:text property="notice.title" name="noticeForm" maxlength="20"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
		<th align="right">
			通知类型
		</th>
		<td>
	        <select id="selectType" onchange="changeType();">
				<option value="">请选择</option>
				<option value="3">首页公告</option>
				<option value="4">充值公告</option>
			</select>
      	</td>
	</tr>
	<tr class="tr-color-body" id="contentbox">
      	<th align="right">内容</th>
      	<td>
      	    <%-- <c:if test = "${noticeForm.notice.type eq '3' || noticeForm.notice.type eq '4'}">
   	 			<html:textarea name="noticeForm" property="notice.content" cols="60" rows="8" />
   			</c:if> --%>
      	    <%-- <c:if test = "${noticeForm.notice.type ne '3' || noticeForm.notice.type ne '4'}"> --%>
        	<html:hidden property="notice.content" name="noticeForm" styleId="content"></html:hidden>
      		<div class="container" style="width: 540px;">
		        <div id="editor-trigger">
		        </div>
		    </div>
			<%-- </c:if> --%>
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