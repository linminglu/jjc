<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="../common/inc_css.jsp"%>
</head>
<body>
<table width="60%" border="0" cellspacing="0" cellpadding="0" align="center" >
      	<tr>
        	<td align="right"><img src="/main-table-corner-leftup.gif" width="3" height="3"/></td>
        	<td width="100%" class="graytable-bg-up"></td>
        	<td><img src="/main-table-corner-rightup.gif" width="3" height="3"/></td>
      	</tr>
      	
      	<tr>
        	<td class="graytable-bg-left"></td>
        	<td class="graytable-space5">
				<h3 class="font-alert">
					系统提示：<br>
					<p>
					<center>错误！您所访问的页面出现异常！</center>
					<br>&nbsp;
					异常信息：<bean:write name="exception"/>
					<br>
					<center>
					<input type="button" class="button-4" value="返回" onclick="javascript: history.back();">
					</center>
					</p>
				</h3>

			</td>
			<td class="graytable-bg-right"></td>
      	</tr>
      <tr>
        <td align="right"><img src="/main-table-corner-leftdown.gif" width="3" height="3" /></td>
        <td class="graytable-bg-down"></td>
        <td><img src="/main-table-corner-rightdown.gif" width="3" height="3" /></td>
      </tr>
</table>

</body>
