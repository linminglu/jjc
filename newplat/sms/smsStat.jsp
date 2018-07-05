<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.framework.util.ManageFile"%>
<%@ include file ="/common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@ include file = "../common/inc_file.jsp"%>
</head>
<body>
<div class="bread">其他信息  &raquo; 短信记录统计</div>
<table class="tblistadd" border="0" cellpadding="0" cellspacing="1">
	<tr class="tr-color-body">
		<td class="bg-blue" colspan="2">短信统计 </td>
	</tr>
	<tr class="tr-color-body">
		<th align="right">累计充值条数</th>
		<td>
        	<%=ManageFile.loadTextFile(Constants.getWebRootPath()+"/sms/smsChangeCount.txt")%>
      	</td>
    </tr>
	<tr class="tr-color-body">
		<th align="right">发送短信条数</th>
		<td>
        	<logic:equal name="smsForm" property="totalCount" value="0">
        		0
			</logic:equal>
        	<logic:notEqual name="smsForm" property="totalCount" value="0">
        		<a href="smsAction.do?method=init">
        			<c:out value="${smsForm.totalCount}"/>
        		</a>
        	</logic:notEqual>
      	</td>
    </tr>
</table>
</body>
</html:html>