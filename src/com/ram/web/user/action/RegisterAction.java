package com.ram.web.user.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.MD5;
import com.ram.web.user.form.RegisterForm;

public class RegisterAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("init");
	}

	/**
	 * 准备用户注册
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward signup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = (HttpSession) request.getSession(false);
		try {
			session.removeAttribute("loginUser");
			session.removeAttribute("loginUserGroup");
			session.invalidate();
		} catch (Exception ex) {
			log.warn(".", ex);
		}
		saveToken(request);
		return mapping.findForward("signup");
	}

	/**
	 * web端注册用户，新注册的用户都是未激活状态的
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveSignup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (isTokenValid(request, true)) {
			String emailReg = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
			RegisterForm regForm = (RegisterForm) form;
			User userForm = regForm.getUser();
			String rePassword = regForm.getConfirmPassword();
			String password = userForm.getPassword();

			String email = userForm.getUserEmail();
			// Integer qq = userForm.getQq();
			userForm.setRegistDateTime(DateTimeUtil.getNowSQLDate());// 用户注册时间
			String loginName = null;
			if (ParamUtils.chkString(userForm.getLoginName())) {
				loginName = userForm.getLoginName().toLowerCase().trim();
				userForm.setLoginName(loginName);
				userForm.setUserName(loginName);
			}

			if (!ParamUtils.chkString(loginName)) {
				log.info("## 用户注册：无效的登录名，请重新注册！");
				request.setAttribute("error", "无效的登录名，请重新注册！");
				return mapping.findForward("saveSignup");
			}

			// log.info(ParamUtils.chkString(password));
			if (!ParamUtils.chkString(password)) {
				request.setAttribute("error", "密码不能为空！");
				return mapping.findForward("saveSignup");
			} else if (!ParamUtils.chkString(rePassword)) {
				request.setAttribute("error", "重复密码不能为空！");
				return mapping.findForward("saveSignup");
			} else if (!password.equals(rePassword)) {
				request.setAttribute("error", "两次输入的密码不同！！");
				return mapping.findForward("saveSignup");
			}
			if (!email.matches(emailReg)) {
				request.setAttribute("error", "邮箱不符合格式!!");
				return mapping.findForward("saveSignup");
			}
			// if(!ParamUtils.chkInteger(qq)){//如果QQ为0或者为空
			// request.setAttribute("error", "QQ不能为空!!");
			// return mapping.findForward("saveSignup");
			// }

//			userForm.setPassword(MD5.exc(password));// 密码加密
			userForm.setPassword(password);// 
			// 添加用户
			 User userRegister = userService.saveUserRegister(userForm);
			 
			 request.getSession(true).setAttribute("loginUser", userRegister);
			 
//			request.setAttribute("email", userForm.getUserEmail());
			 request.setAttribute("error", "signupOK");
			 return mapping.findForward("signupOK");
		} else {
			request.setAttribute("error", "1");// 表单重复提交
		}
		// userService.saveUserRegister();
		// log.info("user.........." + user);
		return mapping.findForward("saveSignup");
	}

	/**
	 * 检测用户名是否被注册-Ajax
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ActionForward isUserExsit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// log.info(11111);
		// request.setCharacterEncoding("utf-8");
		// response.setCharacterEncoding("utf-8");
		// response.setContentType("text/html;charset=utf-8");
		String loginName = request.getParameter("loginName");
		log.info(loginName);
		boolean isExist = userService.checkUserExist(loginName);
		log.info(isExist);
		// System.out.println("..." + loginName + "...");
		PrintWriter out = response.getWriter();
		out.println(isExist);
		out.flush();// 清空缓冲区
		out.close();// 关闭输入流
		return null;
	}

	/**
	 * 加载密码找回页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward pwdResetInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		saveToken(request);
		return mapping.findForward("pwdResetInit");
	}

	/**
	 * 修改密码
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward modifyPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String loginName = ParamUtils.getParameter(request, "loginName");
		RegisterForm registerForm = (RegisterForm) form;
		User userF = registerForm.getUser();
		String password = userF.getPassword();
		String confirmPassword = registerForm.getConfirmPassword();
		if (ParamUtils.chkString(password)
				&& ParamUtils.chkString(confirmPassword)) {
			User user = userService.getUserByLoginName(loginName);// 根据登录名查找用户信息
			user.setPassword(MD5.exc(password));
			userService.saveUser(user);
			request.setAttribute("error", "重置密码成功！");
		} else {
			request.setAttribute("error", "两次输入的密码不一致");
		}
		return mapping.findForward("modifyPwdInit");
	}

}
