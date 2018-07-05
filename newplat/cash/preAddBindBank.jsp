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
	var frm = document.cashForm;
		if(frm["userBankBind.bindName"].value == ""){
			alert("请输入姓名！");
		  frm["userBankBind.bindName"].focus();
			return false;
		}
		if(frm["userBankBind.bankName"].value == ""){
			alert("请输入开户行名称！");
		  frm["userBankBind.bankName"].focus();
			return false;
		}
		if(frm["userBankBind.bindAccount"].value == ""){
			alert("请输入银行账号！");
		  frm["userBankBind.bindAccount"].focus();
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

<div class="bread">账户管理  &raquo; 添加账户  &raquo;
<c:if test="${action eq 'add'}">新增</c:if>
<c:if test="${action eq 'modif'}">修改</c:if>
</div>
<html:form action="/cashAction.do?method=saveBindBank" onsubmit="return formSubmit();"  enctype="multipart/form-data">
<html:hidden name="cashForm" property="userBankBind.bankBindId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
	    <th align="right">开户行名称</th>
	    <%-- <td>
	    	<html:text  name="cashForm" property="userBankBind.bankName" />
	    	<font color="red">说明：输入详细开户行名称，如北京银行阜城支行。</font>
	    </td> --%>
	    <td>
		    <html:select property="bankId" name="cashForm">
				<html:option value="">请选择</html:option>
			      <logic:notEmpty name="bankList">
		      		<html:options  collection="bankList" property="bankId" labelProperty="bankName"/>
			      </logic:notEmpty>
	      	</html:select>
      	</td>
	 </tr>
	 <tr class="tr-color-body">
		<th align="right">姓名</th>
		<td>
			<html:text property="userBankBind.bindName" name="cashForm"/>
		 	<font color="red">说明：输入和银行卡对应的姓名。</font>
		</td>
	</tr>
	 <tr class="tr-color-body">
	    <th align="right">银行账号</th>
	    <td><html:text  name="cashForm" property="userBankBind.bindAccount" /></td>
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
