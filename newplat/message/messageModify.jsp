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

<style type="text/css">
#editor-trigger{height: 300px}
</style>
</head>
<body>
<div class="bread">消息详情&raquo; 
</div>
<html:hidden name="messageForm" property="message.id"/>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
    <tr class="tr-color-body">
      	<th align="right">标题</th>
      	<td>
        	<bean:write  property="message.title" name="messageForm" />
      	</td>
    </tr>
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">内容</th>
      	<td>
      	  <bean:write  name="messageForm" property="message.content" />
      	</td>
    </tr>
   	<tr class="tr-color-body" id="imgbox">
      	<th align="right">接收者</th>
      	<td>
      	  <bean:write  name="messageForm" property="message.loginName"/>
      	</td>
    </tr>
</table>
<div class="div-bottom-bar">
    <input type="button" class="gbutton" onclick="javascript:window.history.go(-1)" value="返回"/>
</div>
</body>
</html:html>