<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
<script language="javascript">
window.onload=function(){
}
</script>
</head>
<body>
<div class="bread">系统管理  &raquo; 推送管理 
</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <logic:equal name="baseDataDTO" property="push.type" value="1">
       <tr class="tr-color-body">
   	     <th align="right">商家</th>
   	    <td>
     		<bean:write name="baseDataDTO" property="title"/>
   	    </td>
       </tr>
   </logic:equal>
    <logic:equal name="baseDataDTO" property="push.type" value="2">
       <tr class="tr-color-body">
   	     <th align="right">商家</th>
   	    <td>
     		<bean:write name="baseDataDTO" property="title"/>
   	    </td>
       </tr>
   </logic:equal>
    <logic:equal name="baseDataDTO" property="push.type" value="3">
       <tr class="tr-color-body">
   	     <th align="right">商家</th>
   	    <td>
     		<bean:write name="baseDataDTO" property="title"/>
   	    </td>
       </tr>
   </logic:equal>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>