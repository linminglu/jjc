<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理平台</title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
function formSubmit(){
	var frm = document.paramForm;
	if(frm["param.type"].value.trim()== '6' || frm["param.type"].value.trim()=='7' || frm["param.type"].value.trim()=='8' ){
		if(frm["param.value"].value.trim().length==0){
			alert("请输入取值");
			frm["param.value"].focus();
			return false;
		}
		if(frm["param.minRecharge"].value.trim().length==0){
			alert("请输入最小充值金额");
			frm["param.minRecharge"].focus();
			return false;
		}
		if(frm["param.maxRecharge"].value.trim().length==0){
			alert("请输入最大充值金额");
			frm["param.maxRecharge"].focus();
			return false;
		}
		if(frm["param.maxPresent"].value.trim().length==0){
			alert("请输入最高赠送金额");
			frm["param.maxPresent"].focus();
			return false;
		}
		if(frm["param.type"].value=="int"){
		    if(!isInt(frm["param.value"].value)){
			alert("请输入整数!");
			frm["param.value"].focus();
			return false;
			}
		}
	}else if(frm["param.type"].value.trim()== '5'){
		if(frm["param.value"].value.trim().length==0){
			alert("请输入取值");
			frm["param.value"].focus();
			return false;
		}
	}
	
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}
function  isInt(str) {
     var re = /^[0-9]*[1-9][0-9]*$/;
    return re.test(str);
}

window.onload=function(){
}
</script>
</head>
<body>
<div class="bread">系统管理  &raquo;  
<logic:empty name="paramForm" property="param.paramId">新增</logic:empty>
<logic:notEmpty name="paramForm" property="param.paramId">修改</logic:notEmpty>
</div>
<html:form action="/paramAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<input type="hidden" name="pager.offset"  value="<c:out value="${startIndex}"/>" />
<input type="hidden" name="maxPageItems"  value="<c:out value="${maxPageItems}"/>" />
<html:hidden name="paramForm" property="param.type"/>
<html:hidden name="paramForm" property="param.paramId"/>
<html:hidden name="paramForm" property="title"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
      	    <bean:write property="param.title" name="paramForm"/>
      	</td>
    </tr>
    <c:if test="${paramForm.param.type == '1'}">
    <tr class="tr-color-body" id="imgbox">
      	<th align="right">支付公司</th>
      	<td>
      		<html:radio property="param.value" name="paramForm" value="4">大面包</html:radio>
      		<html:radio property="param.value" name="paramForm" value="5">轻易付</html:radio>
      	</td>
    </tr>
    </c:if> 
    <c:if test="${paramForm.param.type == '12'||paramForm.param.type == '13'||paramForm.param.type == '14'||paramForm.param.type == '15'}">
    <tr class="tr-color-body" id="imgbox">
      	<th align="right">开关</th>
      	<td>
      		<html:radio property="param.value" name="paramForm" value="1">打开</html:radio>
      		<html:radio property="param.value" name="paramForm" value="0">关闭</html:radio>
      	</td>
    </tr>
    </c:if> 
    <c:if test="${paramForm.param.type == '5'}">
           	<tr class="tr-color-body" id="imgbox">
      	<th align="right">提款比例</th>
      	<td>
      	<html:text property="param.value" name="paramForm" maxlength="20"/>
      	</td>
        </tr>
        </c:if> 
        <c:if test="${paramForm.param.type == '6' || paramForm.param.type == '7'|| paramForm.param.type == '8'}">
            <tr class="tr-color-body">
      	<th align="right">最小充值</th>
      	
      	<td>
        	<html:text property="param.minRecharge" name="paramForm" maxlength="20"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">最大充值</th>
      	<td>
        	<html:text property="param.maxRecharge" name="paramForm" maxlength="20"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">赠送比例</th>
      	<td>
        	<html:text property="param.value" name="paramForm" maxlength="20"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">最高赠送</th>
      	<td>
        	<html:text property="param.maxPresent" name="paramForm" maxlength="20"/>
      	</td>
    </tr>
        </c:if> 

        <c:if test="${paramForm.param.type == '9' || paramForm.param.type == '10'}">
           	<tr class="tr-color-body" id="imgbox">
      	<th align="right">二维码</th>
      	<td>
      		<html:file property="file" name="paramForm" size="50"/><br/>
      		<html:hidden property="param.value" name="paramForm" />
      		<c:if test="${paramForm.param.value ne null}">
      			<img src="..<c:out value="${paramForm.param.value}"/>" alt="<c:out value="${paramForm.param.title}"/>" title="<c:out value="${paramForm.param.title}"/>" width="540"/>
      		</c:if>
      	</td>
    </tr>
        
        </c:if> 
        <c:if test="${paramForm.param.type == '11'}">
           	<tr class="tr-color-body" id="imgbox">
      	<th align="right">描述内容</th>
      	<td>        	
      	<html:textarea name="paramForm" property="param.value" cols="60" rows="8" />
      	</td>
        </tr>
        </c:if> 
        
    
</table>
<div class="div-bottom-bar">
	<input type="submit" class="gbutton" value="确定"/>
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>

</html:form>
</body>
</html:html>