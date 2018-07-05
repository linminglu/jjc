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

	var teamTitle = $("#title").val();
	if(!teamTitle){
		alert("请输入分类名称");
		return false;
	}
	
	var type = $('#type').val();
	if(!type){
		alert("请选择级别");
		return false;
	}
	
	var parentId = $('#parentId').val();
	if(!parentId){
		alert("请选择父级");
		return false;
	}
	
	if(confirm("确认保存？")){
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

</script>
<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">分类管理  &raquo; 新增/修改 &raquo; 
</div>
<html:form action="/typeAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="typeForm" property="type.tid"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">分类名称</th>
      	<td>
        	<html:text property="type.title" name="typeForm" maxlength="20" style="width: 540px;" styleId="title"/>
      	</td>
    </tr>
   	<tr class="tr-color-body">
      	<th align="right">级别</th>
      	<td>
      		<html:select property="type.type" styleClass="seletbox" name="typeForm" styleId="type">
        	 	<html:option value="">请选择</html:option>
        	 	<html:option value="1">一级</html:option>
        		<html:option value="2">二级</html:option>
        	</html:select>
      	</td>
   	</tr>
   	<tr>
		<th>父级</th>
	    <td>
	     	<html:select property="type.parentId" name="typeForm" styleId="parentId">
				 <html:option value="0">顶级</html:option>
	   			 <c:if test="${not empty typeList}">
			          <html:options collection="typeList" property="tid" labelProperty="title" />
			       </c:if>
			</html:select>
	    </td>
	</tr>
	
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">图片</th>
      	<td>
      		<html:file property="file" name="typeForm" size="50"/><br/>
      		<html:hidden property="type.img" name="typeForm" />
      		<c:if test="${typeForm.type.img ne null}">
      			<img src="..<c:out value="${typeForm.type.img}"/>" alt="<c:out value="${typeForm.type.img}"/>" title="<c:out value="${typeForm.type.img}"/>" width="50" height="50"/>
      		</c:if>
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