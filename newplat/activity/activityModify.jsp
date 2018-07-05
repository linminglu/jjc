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
	if(frm["activity.title"].value.trim().length==0){
		alert("请输入标题");
		frm["activity.title"].focus();
		return false;
	}
	var html = editor.$txt.html();
	$('#content').val(html);
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
<div class="bread">优惠活动 &raquo; 
<logic:empty name="advertForm" property="activity.id">新增</logic:empty>
<logic:notEmpty name="advertForm" property="activity.id">修改</logic:notEmpty>
</div>
<html:form action="/activityAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">

<html:hidden name="advertForm" property="activity.id"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<html:text property="activity.title" name="advertForm" maxlength="20" style="width: 540px;"/>
      	</td>
    </tr>
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">显示图片</th>
      	<td>
      		<html:file property="file" name="advertForm" size="50"/><br/>
      		<html:hidden property="activity.showImg" name="advertForm" />
      		<c:if test="${advertForm.activity.showImg ne null}">
      			<img src="..<c:out value="${advertForm.activity.showImg}"/>" alt="<c:out value="${advertForm.activity.title}"/>" title="<c:out value="${advertForm.activity.title}"/>" width="540"/>
      		</c:if>
      		<p class="red">建议尺寸[宽x高]640x320
      		</p>
      	</td>
    </tr>
       	<tr class="tr-color-body" id="imgbox">
      	<th align="right">隐藏图片</th>
      	<td>
      		<html:file property="file2" name="advertForm" size="50"/><br/>
      		<html:hidden property="activity.hideImg" name="advertForm" />
      		<c:if test="${advertForm.activity.hideImg ne null}">
      			<img src="..<c:out value="${advertForm.activity.hideImg}"/>" alt="<c:out value="${advertForm.activity.title}"/>" title="<c:out value="${advertForm.activity.title}"/>" width="540"/>
      		</c:if>
      		<p class="red">建议尺寸[宽x高]640x320
      		</p>
      	</td>
    </tr>
    
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</html:form>
</body>
</html:html>