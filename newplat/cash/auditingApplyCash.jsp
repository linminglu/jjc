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
	var status = frm["userApplyCash.auditStatus"].value;
	if( status == ""){
		alert("请选择审核状态！");
		return false;
	}else if(status == "2"){
		var remark = frm["userApplyCash.remark"].value.trim();
		if(remark.length==0){
			alert("请填写原因");
			frm["userApplyCash.remark"].focus();
			return false;
		}
	}
	if(confirm("您确定要进行本次操作吗，一旦操作不能修改！")){
		return true;
	}else{
		return false;
	}
}

</script>
</head>
<body>

<div class="bread">提现管理  &raquo; 提现审核  
	     
</div>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<html:form action="/cashAction.do?method=saveAuditing" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="cashForm" property="userApplyCash.applyCashId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	  <tr class="tr-color-body">
		<th align="right">申请人</th>
		<td><bean:write name="cashForm" property="user.userName"/></td>		      
	  </tr>
	  <tr class="tr-color-body">
		<th align="right">申请提现金额</th>
		<td><bean:write name="cashForm" property="userApplyCash.cashMoney"/>元</td>		      
	  </tr>
	  <tr class="tr-color-body">
		<th align="right">申请时间</th>
		<td><bean:write name="cashForm" property="userApplyCash.createTime" format="yyyy-MM-dd HH:mm" /></td>		      
	  </tr>
	  <tr class="tr-color-body">
		<th align="right">审核状态</th>
		<td>
			<html:select property="userApplyCash.auditStatus" name="cashForm" styleClass="sele">
				<html:option value="">请选择</html:option>
				<html:option value="1">已通过</html:option>
				<html:option value="2">未通过</html:option>
			</html:select>
		</td>			      
	</tr>
	<tr class="tr-color-body">
      	<th align="right">拒绝原因</th>
      	<td>
      		<html:textarea property="userApplyCash.remark" name="cashForm" cols="60" rows="5"/>
      		<p class="red">拒绝的时候填写</p>
      	</td>
    </tr>
	     		
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定" id ='tijiao'/>
    <input type="button" class="gbutton" id ='back' onclick="javascript:window.history.go(-1)" value="返回"/>
    <span id="info" style="display:none;color:red;font-size:14px;font-weight:bold;margin-top:10px;">正在上传，请稍等...</span>
</div>
<%-- <table class="tblist" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <th>序号</th>
        <th>商品标题</th>
	    <th>购买数量</th>
	    <th>实付金额</th>
    </tr>
    <logic:iterate id="item" name="list" indexId="num">
		<tr onmousemove="this.style.background='#EEEEEE'" onmouseout="this.style.background='#ffffff'">
			<td class="numbers"><%=num.intValue()+1 %></td>
			<td><c:out value="${item.title}"/></td>
			<td><c:out value="${item.count}"/></td>
			<td><strong><c:out value="${item.money}"/></strong></td>
		</tr>
	</logic:iterate>
</table> --%>
</html:form>
</body>
</html:html>
