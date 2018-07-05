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
		if(frm["cardRechargeItem.title"].value == ""){
			alert("请输入套餐名称！");
		  frm["cardRechargeItem.title"].focus();
			return false;
		}
		if(frm["cardRechargeItem.parValue"].value == ""){
			alert("请输入积分！");
		  frm["cardRechargeItem.parValue"].focus();
			return false;
		}
		if(frm["cardRechargeItem.price"].value == ""){
			alert("请输入售价！");
		  frm["cardRechargeItem.price"].focus();
			return false;
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

<div class="bread">充值管理  &raquo; 充值套餐  &raquo;
<c:if test="${action eq 'add'}">新增</c:if>
<c:if test="${action eq 'modif'}">修改</c:if>
</div>
<html:form action="/cardAction.do?method=saveCardRechargeItem" onsubmit="return formSubmit();"  enctype="multipart/form-data">
<html:hidden name="cardForm" property="cardRechargeItem.itemId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
		<th align="right">套餐名称</th>
		<td><html:text property="cardRechargeItem.title" name="cardForm"/></td>
	</tr>
	<tr class="tr-color-body">
	    <th align="right">积分</th>
	    <td><html:text  name="cardForm" property="cardRechargeItem.parValue" onkeyup="value=value.replace(/[^\d\.]/g,'')"/></td>
	 </tr>
	 <tr class="tr-color-body">
	    <th align="right">售价</th>
	    <td><html:text  name="cardForm" property="cardRechargeItem.price" onkeyup="value=value.replace(/[^\d\.]/g,'')"/></td>
	 </tr>

      <%-- <tr class="tr-color-body" style="display:none">
      	<th align="right">所在城市</th>
      	<td>
        	<html:select property="rechargeSetMeal.cityId" name="cardForm">
        		<html:option value="">请选择</html:option>
        		<html:options collection="citys" property="cityId" labelProperty="city"/>
        	</html:select>
      	</td>
    </tr> --%>
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定" id ='tijiao'/>
    <input type="button" class="gbutton" id ='back' onclick="javascript:window.history.go(-1)" value="返回"/>
    <span id="info" style="display:none;color:red;font-size:14px;font-weight:bold;margin-top:10px;">正在上传，请稍等...</span>
    
</div>
</html:form>
</body>
</html:html>
