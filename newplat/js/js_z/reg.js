//$.ajaxSetup({
//	async : false
//// 设置get、post同步
//});
var randCodeFlag = false;// 验证码全局
var loginNameFlag = false;// 登录名全局
var emaliFlag = false;// 邮箱全局
var qqFlag = false;// QQ全局

function submitForm_reg() {
	var ret = true;

	if (!lNameisNull()) {
		ret = false;
	}
	if (!uPwdisNull()) {
		ret = false;
	}
	if (!uPwd2isNull()) {
		ret = false;
	}
	if (!emailisNull()) {
		ret = false;
	}
	if (!phoneisNull()) {
		ret = false;
	}
	// if (!qqisNull()) {
	// ret = false;
	// }
	// if (!operatPwdisNull()) {
	// ret = false;
	// }
	// if (!chkRandCode()) {
	// ret = false;
	// }
	return ret;
}
function addClass_r(id) {
	$("#" + id).removeClass("color_b");// 清除已应用的类样式
	$("#" + id).addClass("color_r");// 添加类样式
}
function addClass_b(id) {
	$("#" + id).removeClass("color_r");// 清除已应用的类样式
	$("#" + id).addClass("color_b");// 添加类样式
}

function lNameTiShi(obj) {
	var uName = $.trim($("#loginName").val());
	addClass_b("lNameTips");
	$(obj).addClass("bor_o");
	$("#lNameTips").text("请输入2-10位字符,包括字母，数字，下划线");
	// $("#lNameTips").slideDown("slow");
}
function lNameisNull(obj) {
	
	var loginNameReg = /^.{2,10}$/;
	var loginName = $.trim($("#loginName").val());
	var uNameTips = $("#lNameTips");
	$(obj).removeClass("bor_o");
	if (loginNameReg.test($.trim(loginName)) == false) {
		addClass_r("lNameTips");
		uNameTips.html("登录名不符合格式");
		// uNameTips.slideDown("fast");
		return false;
	} else {
		var ret = loginNameVerification();
		return ret;
	}
}

function pwdTiShi(obj) {
	var uPwd = $.trim($("#password").val());
	addClass_b("uPwdTips");
	$(obj).addClass("bor_o");
	$("#uPwdTips").text("请输入6-15位的密码,区分大小写");
}
function uPwdisNull(obj) {
	var regPwd = /^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,15}$/;
	var uPwd = $.trim($("#password").val());
	$(obj).removeClass("bor_o");
	if (uPwd == "") {
		addClass_r("uPwdTips");
		$("#uPwdTips").text("密码不能为空");
		return false;
	} else {
		if (regPwd.test(uPwd) == false) {
			addClass_r("uPwdTips");
			$("#uPwdTips").text("密码格式不正确");
			return false;
		} else {
			$("#uPwdTips").html(
					"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			return true;
		}
	}
}

function pwd2TiShi(obj) {
	var uPwd2 = $.trim($("#confirmPassword").val());
	$(obj).addClass("bor_o");
	addClass_b("uPwd2Tips");
	$("#uPwd2Tips").text("重复输入密码");
}
function uPwd2isNull(obj) {
	var uPwd = $.trim($("#password").val());
	var uPwd2 = $.trim($("#confirmPassword").val());
	$(obj).removeClass("bor_o");
	if (uPwd2 == "") {
		addClass_r("uPwd2Tips");
		$("#uPwd2Tips").text("重复密码不能为空");
		return false;
	} else {
		if (uPwd != uPwd2) {
			addClass_r("uPwd2Tips");
			$("#uPwd2Tips").text("两次输入的密码不相同");
			return false;
		} else {
			$("#uPwd2Tips").html(
					"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			return true;
		}
	}
}
function emailTiShi(obj) {
	var uEmail = $.trim($("#userEmail").val());
	$(obj).addClass("bor_o");
	if (uEmail == "") {
		addClass_b("uEmailTips");
		$("#uEmailTips").text("请输入正确的邮箱格式");
	}
}
function emailisNull(obj) {
	var reguEmail = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/;
	var uEmail = $.trim($("#userEmail").val());
	$(obj).removeClass("bor_o");
	if (uEmail == "") {
		addClass_r("uEmailTips");
		$("#uEmailTips").text("邮箱不能为空");
		return false;
	} else {
		if (reguEmail.test(uEmail) == false) {
			addClass_r("uEmailTips");
			$("#uEmailTips").text("输入的邮箱格式不正确");
			return false;
		} else {
			var ret = emailVerification();
			return ret;
		}
	}
}
function qqTiShi(obj) {
	var userqq = $.trim($("#userqq").val());
	$(obj).addClass("bor_o");
	if (userqq == "") {
		addClass_b("qqTips");
		$("#qqTips").text("请输入正确QQ");
	}
}
function qqisNull(obj) {
	var regQQ = /^\d{5,12}$/;
	var userqq = $.trim($("#userqq").val());
	$(obj).removeClass("bor_o");
	if (userqq == "") {
		addClass_r("qqTips");
		$("#qqTips").text("QQ不能为空");
		qqFlag = false;
	} else {
		if (regQQ.test(userqq) == false) {
			addClass_r("qqTips");
			$("#qqTips").text("输入的QQ格式不正确");
			qqFlag = false;
		} else {
			$.post("qqExistAction.do", {
				"qq" : userqq
			}, function(data) {
				data = $.trim(data);
				if (data == "true") {// 不能注册
					addClass_r("qqTips");
					$("#qqTips").text("QQ已被注册");
					qqFlag = false;
				} else if (data == "false") {// 可以注册
					var userqq = $.trim($("#userqq").val());
					$("#QQemail").text(userqq + "@qq.com");
					$("#userEmail").val(userqq + "@qq.com");
					$("#qqTips").html(
							"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
					qqFlag = true;
				}
			});
		}
	}
	return qqFlag;
}
function operatPwdTiShi(obj) {
	var uPwd = $.trim($("#operatPwd").val());
	$(obj).addClass("bor_o");
	if (uPwd == "") {
		addClass_b("operatPwdTips");
		$("#operatPwdTips").text("6-15位操作码,区分大小写，不能和密码相同,操作码用于系统内对资金操作时的确认操作。");
	}
}
function operatPwdisNull(obj) {
	var regPwd = /^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,15}$/;
	var password = $("#password");
	var operatPwd = $("#operatPwd");
	var uPwd = $.trim($("#operatPwd").val());
	$(obj).removeClass("bor_o");
	if (uPwd == "") {
		addClass_r("operatPwdTips");
		$("#operatPwdTips").text("操作码不能为空");
		return false;
	} else {
		if (regPwd.test(uPwd) == false) {
			addClass_r("operatPwdTips");
			$("#operatPwdTips").text("操作码格式不正确");
			return false;
		} else if (password.val() == operatPwd.val()) {
			addClass_r("operatPwdTips");
			$("#operatPwdTips").text("操作码不能和密码相同");
			return false;
		} else {
			$("#operatPwdTips").html(
					"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			return true;
		}
	}
}
function loginNameVerification() {
	var uName = $.trim($("#loginName").val());
	var uNameTips = $("#lNameTips");
	uNameTips.text("");// 清除提示信息
	$.post("../user/userExistAction.do", {
		"loginName" : uName
	}, function(data) {
		data = $.trim(data);
		if (data == "true") {// 不能注册
			addClass_r("lNameTips");
			uNameTips.text("登录名已被注册");
			loginNameFlag = false;
		} else if (data == "false") {// 可以注册
			uNameTips.html("<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			loginNameFlag = true;
		}
	});
	return loginNameFlag;
}
function emailVerification() {
	var uEmail = $.trim($("#userEmail").val());
	$("#uEmailTips").text("");// 清除提示信息
	$.post("../user/emailExistAction.do", {
		"email" : uEmail
	}, function(data) {
		data = $.trim(data);
		if (data == "true") {// 不能注册
			addClass_r("uEmailTips");
			$("#uEmailTips").text("邮箱已被注册");
			emaliFlag = false;
		} else if (data == "false") {// 可以注册
			$("#uEmailTips").html(
					"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			emaliFlag = true;
		}
	});
	return emaliFlag;
}
function refresh(id) {
	$("#randomCode").attr("src", "../imageServlet?" + Math.random());
}
function chkRandCodeFocus(obj) {
	$(obj).addClass("bor_o");
	$("#proRandCodeimg").text("");
}

function chkRandCode(obj) {
	var val = $.trim($("#textRandCodeimg").val());
	var proRandCodeimg = $("#proRandCodeimg");
	$(obj).removeClass("bor_o");
	/**
	 * $.ajax({ // 请求的资源,用什么都一样html也可以 url : "chkRandCodeAction.do?&randCode="
	 * +val, cache : false, success : function(data) { data = $.trim(data); if
	 * (data == "true") {// 验证码正确 proRandCodeimg .html("<img alt='输入正确'
	 * src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记 randCodeFlag = true; }
	 * else{// 验证码错误 addClass_r("proRandCodeimg");
	 * proRandCodeimg.text("验证码不正确！"); randCodeFlag = false; } } });
	 */

	$
			.post(
					"chkRandCodeAction.do",
					{
						"randCode" : val
					},
					function(data) {
						data = $.trim(data);
						if (data == "true") {// 验证码正确
							proRandCodeimg
									.html("<img alt='输入正确' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
							randCodeFlag = true;
						} else {// 验证码错误
							addClass_r("proRandCodeimg");
							proRandCodeimg.text("验证码不正确！");
							randCodeFlag = false;
						}
					});
	return randCodeFlag;

}

function phoneTiShi(obj) {
	var phone = $.trim($("#phone").val());
	$(obj).addClass("bor_o");
	addClass_b("phoneTips");
	$("#phoneTips").text("请填写您的手机号");
}
function phoneisNull(obj) {
	var phone = $.trim($("#phone").val());
	$(obj).removeClass("bor_o");
	if (phone == "") {
		addClass_r("phoneTips");
		$("#phoneTips").text("手机号不能为空");
		return false;
	} else {
		if (!(/^1[3|4|5|8]\d{9}$/.test(phone))) {
			addClass_r("phoneTips");
			$("#phoneTips").text("请输入正确的手机号");
			return false;
		} else {
			$("#phoneTips").html(
					"<img alt='' src='../images/pic_login_ok.gif'/>");// 追加一些HTML标记
			return true;
		}
	}
}

/**
 * 检测是否是邮箱格式
 * 
 * @param strEmail
 * @returns true： 是，false：否
 */
function cheEmail(strEmail) {
	var myReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/;
	if (myReg.test(strEmail))
		return true;
	return false;
}
/**
 * 检测是否是4-20位的字符串
 * 
 * @param str
 * @returns true： 是，false：否
 */
function cheStr(str) {
	var myReg = /^(\w){4,20}$/;
	if (myReg.test(str))
		return true;
	return false;
}

/** 通过邮箱找回密码* */
function pwdForEmai5() {
	var ret = true;
	var name = $("#userName").val();
	var emali = $("#userEmail").val();

	if (!cheStr(name)) {
		alert("请填写正确的用户名！");
		ret = false;
	}
	if (!cheEmail(emali)) {
		alert("邮箱不符合格式！");
		ret = false;
	}
	if (!chkRandCode()) {
		alert("验证码不正确！");
		ret = false;
	}
	return ret;
}

function pwdReset() {
	var ret = true;

	if (!uPwdisNull()) {
		ret = false;
	}
	if (!uPwd2isNull()) {
		ret = false;
	}
	return ret;
}
