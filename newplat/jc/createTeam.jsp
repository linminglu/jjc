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
		alert("请输入战队名称");
		return false;
	}
	
	var tid1 = $("#tid1").val();
	if(!tid1){
		alert("请选择一级");
		return false;
	}
	
	var tid2 = $("#tid2").val();
	if(!tid2){
		alert("请选择二级");
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
<div class="bread">队伍管理  &raquo; 新增/修改 &raquo; 
</div>
<html:form action="/jcTeamAction.do?method=save" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="jcTeamForm" property="jcTeam.id"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">队伍名称</th>
      	<td>
        	<html:text property="jcTeam.title" name="jcTeamForm" maxlength="20" style="width: 540px;" styleId="title"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">一级</th>
      	<td>
        	<html:select property="jcTeam.tId1" styleClass="selectbox" name="jcTeamForm" styleId="tid1">
                <html:option value="">请选择</html:option>
			       <c:if test="${not empty type1List}">
			          <html:options collection="type1List" property="tid" labelProperty="title" />
			       </c:if>
			</html:select>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">二级</th>
      	<td>
        	<html:select property="jcTeam.tId2" styleClass="selectbox" name="jcTeamForm" styleId="tid2">
                <html:option value="">请选择</html:option>
			       <c:if test="${not empty type2List}">
			          <html:options collection="type2List" property="tid" labelProperty="title" />
			       </c:if>
			</html:select>
      	</td>
    </tr>
   	<tr class="tr-color-body">
      	<th align="right">其他说明</th>
      	<td>
      		<html:textarea property="jcTeam.remarks" name="jcTeamForm" styleId="remarks" cols="60" rows="8"/>
      	</td>
   	</tr>
	
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">LOGO</th>
      	<td>
      		<html:file property="file" name="jcTeamForm" size="50"/><br/>
      		<html:hidden property="jcTeam.img" name="jcTeamForm" />
      		<c:if test="${jcTeamForm.jcTeam.img ne null}">
      			<img src="..<c:out value="${jcTeamForm.jcTeam.img}"/>" alt="<c:out value="${jcTeamForm.jcTeam.img}"/>" title="<c:out value="${jcTeamForm.jcTeam.img}"/>" width="50" height="50"/>
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