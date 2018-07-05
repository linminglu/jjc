<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.ram.model.User"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户中心 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>
<script type="text/javascript">
function modifSub(){
	if(confirm('确定要修改密码吗？')){
		var pwd=$("#password");
		var newPwd=$("#newPwd");
		var newPwd2=$("#newPwd2");
		if($.trim(pwd.val()).length<6){
			$.dialog({
				title : '提示',
				content : "原密码必须大于6位",
				ok:function(){
				}
			});
			pwd.focus();
			return false;
		}
		if($.trim(newPwd.val()).length<6){
			$.dialog({
				title : '提示',
				content : "新密码必须大于6位",
				ok:function(){
				}
			});
			newPwd.focus();
			return false;
		}
		if($.trim(newPwd2.val()).length<6){
			$.dialog({
				title : '提示',
				content : "新密码必须大于6位",
				ok:function(){
				}
			});
			newPwd2.focus();
			return false;
		}
		return true;
	}
	return false;
}

$(document).ready(function(){
	var err = $("#errors");
	if(err && err.val()!=""){
		$.dialog({
			title : '提示',
			content : err.val(),
			ok:function(){
				window.location.href="../myhome/resetPassword.html";
			}
		});
	}
});

</script>
</head>
<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="hdover">
	<div class="topline"></div>
	<c:import url="../common/inc_naver.jsp">
			<c:param name="mpath" value=""></c:param>
	</c:import>
</div>
<div class="content">
	<div class="signup">
		<div class="n-left-menu">
			<h3></h3>
			<ul>
				<li >
					<a href="/myhome/index.html">个人信息</a>
				</li>
				<li >
					<a href="/myhome/resetLogo.html">修改头像</a>
				</li>
				<li class="selected">
					<a href="/myhome/resetPassword.html">修改密码</a>
				</li>
			</ul>
		</div>
		<div class="n-right">
			<div class="title_1">
				<a class="selected" href="#">修改密码</a>
			</div>
			<div class="m1L" style="position:relative;">
			<form action="/myhome/password.html" method="post" onsubmit="return modifSub();" >
			<html:hidden property="user.userId" name="userForm" />
				<table class="table1" width="100%" cellspacing="0" cellpadding="0" style="margin-left:100px;">
					<tbody>
						<tr>
							<th align="right"><strong>原密码：</strong></th>
							<td> <html:password styleId="password" property="password" name="userForm"/> </td>
						</tr>
						<tr>
							<th width="70" align="right">新密码：</th>
							<td width="280"><html:password styleId="newPwd" property="newPassword" name="userForm"/></td>
						</tr>
						<tr>
							<th width="70" align="right">再次输入新密码：</th>
							<td width="280">
								<html:password styleId="newPwd2" property="newPassword2" name="userForm"/>
							</td>
						</tr>
						<tr>
							<th width="70" align="right"></th>
							<td width="280">
								<input type="submit" class="btn_save" value="保存" />
							</td>
						</tr>
					</tbody>
				</table>
				</form>
			</div>
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
