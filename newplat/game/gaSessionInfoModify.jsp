<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include  file="../common/inc_datepicker.jsp" %>  
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript" src="../js/platjs.js"></script>
<script type="text/javascript" src="<%=webContext %>/js/js_z/md5.js"></script>
<script language="javascript">

function formSubmit(){
	if(confirm("确认保存？")){
		return true;
	}
	return false;
}

</script>
</head>
<body>
<div class="bread">彩种管理  &raquo;&raquo; 修改</div>
<html:form action="/gameAction.do?method=modify" onsubmit="return formSubmit();" enctype="multipart/form-data">
<html:hidden name="gameForm" property="gaSessionInfo.infoId"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<th align="right">彩票名称</th>
		<td>
        	<html:text name="gameForm" property="gaSessionInfo.gameTitle" disabled="true"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">副标题</th>
      	<td>
       		<html:text property="gaSessionInfo.subtitle" name="gameForm" maxlength="50"/>
      	</td>
    </tr>
    <tr class="tr-color-body">
      	<th align="right">在线开奖网址</th>
      	<td>
       		<html:text property="gaSessionInfo.liveUrl" name="gameForm" maxlength="200"/>
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