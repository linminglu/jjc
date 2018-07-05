<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.ram.model.User"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户信息 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>
</head>
<body>
<div class="hdover">
	<div class="topline"></div>
	<c:import url="../common/inc_naver.jsp">
			<c:param name="mpath" value=""></c:param>
	</c:import>
</div>
<div class="content">
	<div class="signup">
		<div class="" style="width: 800px; margin: 0 auto;">
			<div class="title_1">
				<a class="selected">基本资料</a>
			</div>
			<div class="m1L" style="position:relative;">
				<table class="table1" width="100%" cellspacing="0" cellpadding="0" style="">
					<tbody>
						<tr>
							<th width="70" align="right">头像：</th>
							<td width="250">
								<c:if test="${otherUser.logoMini eq null}">
									<img src="../images/noimg.png" class="logo"/>
								</c:if>
								<c:if test="${otherUser.logoMini ne null}">
									<img src="<c:out value='${otherUser.logoMini}'/>" class="logo"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<th align="right">昵称：</th>
							<td >
								<c:out value="${otherUser.userName}"/>
							</td>
						</tr>
						<tr>
							<th align="right">个人简介：</th>
							<td >
								<c:if test="${otherUser.motto eq ''}">无</c:if>
								<c:if test="${otherUser.motto eq null}">无</c:if>
								<c:if test="${otherUser.motto ne null}"><c:out value="${otherUser.motto}"/></c:if>
								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
