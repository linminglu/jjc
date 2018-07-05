<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file ="../common/inc_include.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8" />
<title>用户注册 - <c:out value="${slogan}"/></title>
<c:import url="../common/inc_style.jsp"/>
<script src="../js/js_z/reg.js" type="text/javascript"></script>
<script src="../js/js_z/jquery.mailAutoComplete-3.1.js" type="text/javascript"></script>

<script type="text/javascript">
function regSub(){
	if(confirm('确定要注册吗？')){
		$('#regForm').submit();
	}
}
$(document).ready(function(){
	var err = $("#errors");
	if(err && err.val()!=""){
		if(err.val()=='signupOK'){
			$.dialog({
				title : '注册成功',
				content : '注册成功，点击按钮跳转到网站主页',
				cancelValue : '去主页',
				cancel: function () {
					window.location.href='../';
			    }
			});
		}
		if(err.val()=='1'){
			$.dialog({
				title : '提示',
				content : '不能重复提交表单',
				ok: function () {
					window.location.href='../regSignup.html';
			    }
			});
		}
	}
});
</script>
</head>
<body>
<input type="hidden" name="errors" id="errors" value="<c:out value="${error}"/>" />
<div class="hdover">
	<div class="topline"></div>
	<div class="hdline">
		<div class="header">
			<div class="hdlogo">
				<h1>
					<a href="/">
						<img src="../images/gz_03.png"/>
					</a>
				</h1>
			</div>
			<div class="hdregbar">
				<p><input type="button" value="" onclick="window.open('/','_blank');" class="btnReturn"/></p>
			</div>
			<div class="c"></div>
		</div>
	</div>
</div>
<div class="content">
	<div class="signup">
		<div class="reg-left">
			<html:form action="/registerAction.do?method=saveSignup"
				method="post" onsubmit="return submitForm_reg()" styleId="regForm">
				<p class="reg-title">你准备好了吗？加入果子联盟~</p>
				<p>
					<label class="reg-user" for="user_login"></label>
					<html:text property="user.loginName"
								name="registerForm" styleId="loginName" styleClass="reg-input"
								onfocus="lNameTiShi(this);" onblur="lNameisNull(this);"></html:text> 
					
					<span id="lNameTips" class="tips">填写登录名</span>
				</p>
				
				<p>
					<label class="reg-pwd" for="user_pwd1"></label>
					<html:password property="user.password"
								name="registerForm" styleId="password" styleClass="reg-input"
								onfocus="pwdTiShi(this)" onblur="uPwdisNull(this)" ></html:password>
					<span id="uPwdTips" class="tips">6位以上密码</span>
				</p>
				<p>
					<label class="reg-pwd" for="user_pwd2"></label>
					<html:password property="confirmPassword"
								name="registerForm" styleId="confirmPassword"
								styleClass="reg-input" onfocus="pwd2TiShi(this)"
								onblur="uPwd2isNull(this)"></html:password>
					<span id="uPwd2Tips" class="tips">重复上边的密码</span>
				</p>
				<p>
					<label class="reg-email" for="user_email"></label>
					<html:text property="user.userEmail" name="registerForm"
								styleId="userEmail" styleClass="reg-input" onfocus="emailTiShi(this)"
								onblur="emailisNull(this)"></html:text>
					<span id="uEmailTips" class="tips">填写您的用电子邮件地址</span>
				</p>
				<p>
					<label class="reg-phone" ></label>
					<html:text property="user.cellPhone"
								name="registerForm" styleId="phone"
								styleClass="reg-input" onfocus="phoneTiShi(this)"
								onblur="phoneisNull(this)"></html:text>
					<span id="phoneTips" class="tips">填写您的手机号</span>
				</p>
				<p class="reg-submit">
					<input id="wp-submit" class="pfBtn" type="submit" value="注册" name="wp-submit"/>
				</p>
				
			</html:form>
		</div>
		<div class="reg-right">
			<p>如果您已有果子联盟帐号，请直接点击登录</p>
			<div class="loginBtn">
				<a class="showdiv" title="登录" href="../login.html">登录</a>
			</div>
		</div>
	</div>
</div>
<c:import url="../common/inc_footer.jsp"/>
</body>
</html>
