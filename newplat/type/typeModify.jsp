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
<script language="javascript">
function formSubmit(){
	var frm = document.typeForm;
	
	if(frm["type.type"] && frm["type.type"].value.trim().length==0){
		alert("请选择栏目类型");
		return false;
	}
	
	if(frm["type.position"] && frm["type.position"].value.trim().length==0){
		alert("请选择栏目位置");
		return false;
	}
	
	if(frm["infoType"].value=='1'||frm["infoType"].value=='2'){
		if(frm["tid"].value.trim().length==0){
			alert("请选择类型");
			frm["tid"].focus();
			return false;
		}
	}
	
	if(frm["type.title"].value.trim().length==0){
		alert("请输入标题");
		frm["type.title"].focus();
		return false;
	}
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
</script>
</head>
<body>
<div class="bread">类型管理  &raquo;  
<logic:empty name="typeForm" property="type.tid">新增</logic:empty>
<logic:notEmpty name="typeForm" property="type.tid">修改</logic:notEmpty>
</div>

<html:form action="/typeAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="typeForm" property="type.tid"/>
<html:hidden name="typeForm" property="column"/>
<input type="hidden" name="infoType" value="<c:out value="${infoType}"/>"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<c:if test="${empty infoType}">
	<tr class="tr-color-body">
      	<th align="right">栏目类型</th>
      	<td>
        	<html:select property="type.type" name="typeForm">
        		<html:option value="">请选择</html:option>
        		<html:options collection="typeCateList" property="type" labelProperty="title"/>
        	</html:select>
      	</td>
    </tr>
	</c:if>
	
	<c:if test="${infoType==1 || infoType==2}">
	 <tr class="tr-color-body">
      	<th align="right">父栏目</th>
      	<td>
        	<html:select property="tid" name="typeForm">
        		<html:option value="">请选择</html:option>
	      	<logic:notEmpty name="list">
      			<html:options  collection="list"  property="tid" labelProperty="title"/>
	      	</logic:notEmpty>
      		</html:select>
      	</td>
    </tr>
    </c:if>
    <tr class="tr-color-body">
      	<th align="right">名称</th>
      	<td>
        	<html:text property="type.title" name="typeForm" maxlength="20"/>
      	</td>
    </tr>
    
    <c:if test="${empty infoType}">
    <tr class="tr-color-body">
      	<th align="right">显示位置</th>
      	<td>
        	<html:select property="type.position" name="typeForm">
        		<html:option value="">请选择</html:option>
        		<html:option value="1">主栏目</html:option>
        		<html:option value="2">次栏目</html:option>
        	</html:select>
      	</td>
    </tr>
    <%if(Constants.chkFuncConfig("system.func.type.template")){%>
    <tr class="tr-color-body">
    	<th align="right">二级展示</th>
      	<td>
        	<html:radio property="type.isShow" value="0">否</html:radio>
        	<html:radio property="type.isShow" value="1">是</html:radio>
        	<span class="span-tip">仅适用于电商类</span>
      	</td>
    </tr>
    <tr class="tr-color-body">
    	<th align="right">商品模板</th>
      	<td>
        	<html:radio property="type.template" value="1">快捷模式</html:radio>
        	<html:radio property="type.template" value="2">标准模式</html:radio>
      	</td>
    </tr>
    <%} %>
    </c:if>
    
   <%--  <c:if test="${infoType!=1 }"> --%>
	   	<tr class="tr-color-body">
	      	<th align="right">图片</th>
	      	<td>
	      		<html:file property="file" name="typeForm" size="50"/><br/>
	      		<html:hidden property="type.img" name="typeForm" />
	      		<c:if test="${typeForm.type.img ne null}">
	      			<img width="120" height="120" src="..<c:out value="${typeForm.type.img}"/>" alt="<c:out value="${typeForm.type.title}"/>" title="<c:out value="${typeForm.type.title}"/>"/>
	      		</c:if>
	      		<p class="red">建议尺寸
				 [宽x高]120x120
	      		</p>
	      	</td>
	    </tr>
   <%--  </c:if> --%>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>

</html:form>
</body>
</html:html>