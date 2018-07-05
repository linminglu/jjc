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
	var frm = document.rechargeWayForm;
	
	if(frm["rechargeWay.title"] && frm["rechargeWay.title"].value.trim().length==0){
		alert("请填写名称");
		return false;
	}
	
	if(frm["rechargeWay.bankName"] && frm["rechargeWay.bankName"].value.trim().length==0){
		alert("请填写银行名");
		return false;
	}
	if(frm["rechargeWay.bankAccount"] && frm["rechargeWay.bankAccount"].value.trim().length==0){
		alert("请填写银行卡号");
		return false;
	}
	if(frm["rechargeWay.userName"] && frm["rechargeWay.userName"].value.trim().length==0){
		alert("请填写持卡人姓名");
		return false;
	}
	if(frm["rechargeWay.des"] && frm["rechargeWay.des"].value.trim().length==0){
		alert("请填写简单描述或者国籍");
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
<div class="bread">充值方式管理  &raquo;  
<logic:empty name="rechargeWayForm" property="rechargeWay.id">新增</logic:empty>
<logic:notEmpty name="rechargeWayForm" property="rechargeWay.id">修改</logic:notEmpty>
</div>

<html:form action="/rechargeWayAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="rechargeWayForm" property="rechargeWay.id"/>
<html:hidden name="rechargeWayForm" property="passageWayId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
      	<th align="right">渠道类型</th>
      	<td>
        	<html:select property="rechargeWay.parentId" name="rechargeWayForm">
        		<html:option value="">请选择</html:option>
        		<html:options collection="rwList" property="id" labelProperty="title"/>
        	</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">名称</th>
      	<td>
        	<html:text property="rechargeWay.title" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">银行名称</th>
      	<td>
        	<html:text property="rechargeWay.bankName" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">银行卡号</th>
      	<td>
        	<html:text property="rechargeWay.bankAccount" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">持卡人姓名</th>
      	<td>
        	<html:text property="rechargeWay.userName" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">描述</th>
      	<td>
        	<html:text property="rechargeWay.des" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">快速充值的地址</th>
      	<td>
        	<html:text property="rechargeWay.url" name="rechargeWayForm" maxlength="50"/>
      	</td>
    </tr>
   	<tr class="tr-color-body">
      	<th align="right">图片</th>
      	<td>
      		<html:file property="file" name="rechargeWayForm" size="50"/><br/>
      		<html:hidden property="rechargeWay.img" name="rechargeWayForm" />
      		<c:if test="${rechargeWayForm.rechargeWay.img ne null}">
      			<img width="120" height="120" src="..<c:out value="${rechargeWayForm.rechargeWay.img}"/>" alt="<c:out value="${rechargeWayForm.rechargeWay.title}"/>" title="<c:out value="${rechargeWayForm.rechargeWay.title}"/>"/>
      		</c:if>
      		<p class="red">建议尺寸
			 [宽x高]120x120
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