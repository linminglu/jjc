<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<%@ include file = "../common/inc_ueditor.jsp"%>
<script language="javascript">
function loadPage(){
	var err = $("#errors");
	if(err && err.val()!=""){
		var errorCode= err.val();
		if(errorCode=="NOT_SUBMIT_FORM"){
			alert("不能重复提交表单！");
		}
	}
}
window.onload = function(){
	loadPage();
};
function formSubmit(){
	var frm = document.cardForm;
		if(frm["title"].value == ""){
			alert("请输入名称！");
		  frm["title"].focus();
			return false;
		}
		if(frm["parValue"].value == ""){
			alert("请输入面额！");
		  frm["parValue"].focus();
			return false;
		}
		if(frm["price"].value == ""){
			alert("请输入售价！");
		  frm["price"].focus();
			return false;
		}

		if(frm["imgUrl"].value==""){
			if(frm["cover"].value == ""){
				alert("请选择图片！");
			 	 frm["cover"].focus();
				return false;
			}
		}


		
	var tijiao=document.getElementById("tijiao");
    var back = document.getElementById("back");
    var infoObj = document.getElementById("info");
    infoObj.style.display = 'block'; 
    back.disabled = true;
	tijiao.disabled = true;
}

</script>
</head>
<body>

<div class="bread">商品管理  &raquo; 充值套餐  &raquo;
<c:if test="${action eq 'add'}">新增</c:if>
<c:if test="${action eq 'modif'}">修改</c:if>
</div>
<html:form action="/cardAction.do?method=saveCardItem" onsubmit="return formSubmit();"  enctype="multipart/form-data">
<html:hidden name="cardForm" property="itemId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">套餐名称</th>
		<td><html:text property="title" name="cardForm"/></td>
	</tr>
	<tr class="tr-color-body">
	    <th align="right">面额</th>
	    <td><html:text  name="cardForm" property="parValue" /></td>
	 </tr>
	 <tr class="tr-color-body">
	    <th align="right">售价</th>
	    <td><html:text  name="cardForm" property="price" /></td>
	 </tr>
	 <tr class="tr-color-body">
	    <th align="right">备注</th>
	    <td><html:textarea  name="cardForm" property="remarks"  cols="60" rows="8" /></td>
	 </tr>
	 <tr class="tr-color-body">
        <th align="right">图片</th>
      	<td>
      		<html:file property="file" name="cardForm" size="50"/><br/>
      		<html:hidden property="imgUrl" name="cardForm" />
      		<c:if test="${cardForm.imgUrl ne null}">
      			<img src="..<c:out value="${cardForm.imgUrl}"/>" alt="<c:out value="${typeForm.title}"/>" title="<c:out value="${typeForm.title}"/>"/>
      		</c:if>
      		<p class="red">建议尺寸：[宽x高] 322x204</p>
      	</td>
    </tr>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定" id ='tijiao'/>
    <input type="button" class="gbutton" id ='back' onclick="javascript:window.history.go(-1)" value="返回"/>
    <span id="info" style="display:none;color:red;font-size:14px;font-weight:bold;margin-top:10px;">正在上传，请稍等...</span>
    
</div>
</html:form>
</body>
</html:html>
