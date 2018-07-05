package com.ram.web.permission.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.model.SellerUserRl;
import com.apps.service.ISellerService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.model.User;
import com.ram.model.UserLog;
import com.ram.service.user.IUserService;
import com.ram.util.IPUtil;
import com.ram.web.permission.form.LoginForm;

public class LoginAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");

	// private final ISellerService sellerService = (ISellerService)
	// getService("sellerService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("login" + this.checkLoginUI(request));
	}

	/** 
	 * 用户登录时的处理 
	 * 处理一个账号同时只有一个地方登录的关键 
	 * @param request 
	 */  
	public static void userLoginHandle(HttpServletRequest request, ActionForm form){  
	    //当前登录的用户  
		LoginForm loginForm = (LoginForm) form;
		String account = (loginForm.getUserName() + "").trim();

		if (!ParamUtils.chkString(account))
			account = ParamUtils.getParameter(request, "userName", "");
	    //当前sessionId  
	    String sessionId=request.getSession().getId();  
	    //删除当前sessionId绑定的用户，用户--HttpSession  
//	    Constants.USER_SESSION.remove(Constants.SESSIONID_USER.get(sessionId));  
	      
	    //删除当前登录用户绑定的HttpSession  
	    HttpSession session=Constants.USER_SESSION.get(account);  
	    if(session!=null){
	    	Constants.SESSIONID_USER.remove(session.getId());  
//	    	session.removeAttribute("loginUser");
//			session.removeAttribute("loginUserGroup");
//			session.invalidate();
	    	//您的账号已经在另一处登录了,你被迫下线!
	        try {
				session.setAttribute("loginCode", "402");
			} catch (Exception e) {
			}  
	    }  
	} 
	
	// 登录
	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		LoginForm loginForm = (LoginForm) form;
		String account = (loginForm.getUserName() + "").trim();
		String password = loginForm.getPassword() + "".trim();

		if (!ParamUtils.chkString(account))
			account = ParamUtils.getParameter(request, "userName", "");
		if (!ParamUtils.chkString(password))
			password = ParamUtils.getParameter(request, "password", "");

		if (!ParamUtils.chkString(account) || !ParamUtils.chkString(password)) {
			request.setAttribute("error", "1");
		}
		User user = userService.getUserByloginComm(account, password);
		if (user == null) {
			User duser = userService.getUserByLoginName(account);
			if (duser != null) {
				this.updateUserLog(request, duser, "尝试登录失败[密码不正确]");
			}
			request.setAttribute("error", "1");
			loginForm.reset(mapping, request);
			return mapping.findForward("login");
		}else {
			String userType = user.getUserType();
			if(userType.equals(Constants.USER_TYPE_ADMIN)||userType.equals(Constants.USER_TYPE_SUPERADMIN)
					||userType.equals(Constants.USER_TYPE_FINANCE)
					||userType.equals(Constants.USER_TYPE_CUS_SERVICE)||userType.equals(Constants.USER_TYPE_AGENT)||userType.equals(Constants.USER_TYPE_AGENT_ONE)
					||userType.equals(Constants.USER_TYPE_AGENT_TWO)||userType.equals(Constants.USER_TYPE_AGENT_THREE)){
				String ipAddr = IPUtil.getIpAddr(request);
				List<String> whiteList=new ArrayList<String>();
				whiteList.add("43.249.255.212");
				whiteList.add("182.16.88.82");
				whiteList.add("103.49.215.108");
				whiteList.add("103.229.126.149");
				whiteList.add("144.48.242.25");
				boolean isAllow=false;
				for (String whiteIp : whiteList) {
					if(ipAddr.contains(whiteIp)){
						isAllow=true;
						break;
					}
				}
//				if(!isAllow){
//					request.setAttribute("error", "帐号密码错误");
//					loginForm.reset(mapping, request);
//					return mapping.findForward("login");
//				}
			}
			else{
				//无权限
				request.setAttribute("error", "用户名或密码错误");
				loginForm.reset(mapping, request);
				return mapping.findForward("login");
			}
			
			//-----------此处代码只允许一个帐号在一处登录
			HttpSession session=request.getSession();  
			
			//处理用户登录(保持同一时间同一账号只能在一处登录)  
            userLoginHandle(request,form);  
            //添加用户与HttpSession的绑定  
            Constants.USER_SESSION.put(account.trim(), session);  
            //添加sessionId和用户的绑定  
            Constants.SESSIONID_USER.put(session.getId(), account); 
          //-----------
			
			Date now = DateTimeUtil.getJavaUtilDateNow();
			// 用户登录信息更新
			Integer loginTimes = ParamUtils.chkInteger(user.getLoginTimes()) ? user
					.getLoginTimes() : 0;
			user.setLoginTimes(loginTimes + 1);

			user.setLastLoginDate(now);
			user.setLastLoginIp(getIpAddr(request));

			userService.saveUser(user, user);
			this.updateUserLog(request, user, "登录成功");
			request.getSession(true).setAttribute("loginUser", user);
			if (user.getUserType().equals(Constants.USER_TYPE_ADMIN)
					||user.getUserType().equals(Constants.USER_TYPE_SUPERADMIN)
					|| user.getUserType().equals(Constants.USER_TYPE_AGENT_ONE)
					|| user.getUserType().equals(Constants.USER_TYPE_AGENT_TWO)
					|| user.getUserType().equals(Constants.USER_TYPE_AGENT_THREE)
					|| user.getUserType().equals(Constants.USER_TYPE_FINANCE)
					|| user.getUserType().equals(Constants.USER_TYPE_CUS_SERVICE)
			) {// 管理员
				// 后台页面
				
				return mapping.findForward("loginUserGroupSuccess");
			} else {
				// 登录之前的url
				String gotoUrl = (String) request.getSession().getAttribute(
						"GOTO_URL");
				if (gotoUrl != null && gotoUrl != "") {
					try {
						response.sendRedirect(gotoUrl);
						return null;
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}
			return mapping.findForward("loginOrdiSuccess");// 登录成功
		}
	}

	public ActionForward initSj(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("initSj");
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public ActionForward loginSj(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		LoginForm loginForm = (LoginForm) form;
		String account = (loginForm.getUserName() + "").trim();
		String password = loginForm.getPassword() + "".trim();

		if (!ParamUtils.chkString(account))
			account = ParamUtils.getParameter(request, "userName", "");
		if (!ParamUtils.chkString(password))
			account = ParamUtils.getParameter(request, "password", "");

		if (!ParamUtils.chkString(account) || !ParamUtils.chkString(password)) {
			request.setAttribute("error", "1");
		}

		User user = userService.getUserByloginCommSj(account, password);

		if (user == null) {
			User duser = userService.getUserByLoginName(account);
			if (duser != null) {
				this.updateUserLog(request, duser, "尝试登录失败[密码不正确]");
			}
			request.setAttribute("error", "1");
			loginForm.reset(mapping, request);
			return mapping.findForward("loginsz");
		} else {
			Date now = DateTimeUtil.getJavaUtilDateNow();

			// 用户登录信息更新
			Integer loginTimes = ParamUtils.chkInteger(user.getLoginTimes()) ? user
					.getLoginTimes() : 0;
			user.setLoginTimes(loginTimes + 1);

			user.setLastLoginDate(now);
			user.setLastLoginIp(request.getRemoteAddr());
			request.getSession(true).setAttribute("loginUser", user);
			userService.saveUser(user, user);
			this.updateUserLog(request, user, "登录成功");
			return mapping.findForward("initXfm");// 登录成功
		}
	}

	// public ActionForward initXfm(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) {
	// return mapping.findForward("initXfm");
	// }

	// 登出
	public ActionForward logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = (HttpSession) request.getSession(false);
		try {
			session.removeAttribute("loginUser");
			session.removeAttribute("loginUserGroup");
			session.invalidate();
			// removeCookies4FS(request, response);
		} catch (Exception ex) {
			log.warn("用户登出时出现异常.", ex);
		}
		return mapping.findForward("logout");
	}

	// 保存日志
	protected void updateUserLog(HttpServletRequest request, User user,
			String loginAction) {
		// 记录访问者
		String ip = this.getIpAddr(request);
		if (user != null) {
			if (user.getUserId() > 1) {
				UserLog log = new UserLog();
				log.setUserId(user.getUserId());
				log.setLoginName(user.getLoginName());
				log.setIpAddress(ip);
				log.setDateTime(DateTimeUtil.getJavaUtilDateNow());
				log.setActionText(loginAction);
				userService.saveUserLog(log);
			}
		} else {
			UserLog log = new UserLog();
			log.setUserId(0);
			log.setLoginName(request.getSession().getId());
			log.setIpAddress(ip);
			log.setDateTime(DateTimeUtil.getJavaUtilDateNow());
			log.setActionText(loginAction);
			userService.saveUserLog(log);
		}
	}

	public String checkLoginUI(HttpServletRequest request) {
		return "";
	}

	/**
	 * ajax输出流
	 * 
	 * @param response
	 * @param info
	 */
	private void AjaxWriter(HttpServletResponse response, String info) {
		PrintWriter out;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.println(info);
			out.flush();// 清空缓冲区
			out.close();// 关闭输入流
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	// 登录
	public ActionForward loginsj(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String account = ParamUtils
				.getParameter(request, "userName", "default");
		String password = ParamUtils.getParameter(request, "password",
				"default");
		String info = "";

		User user = userService.getUserByloginComm(account, password);
		if (user == null) {
			User duser = userService.getUserByLoginName(account);
			if (duser != null) {
				this.updateUserLog(request, duser, "尝试登录失败[密码不正确]");
			}
			info = "用户名或密码不正确!";
			AjaxWriter(response, info);
			return null;

		} else {
			Date now = DateTimeUtil.getJavaUtilDateNow();

			// 用户登录信息更新
			Integer loginTimes = ParamUtils.chkInteger(user.getLoginTimes()) ? user
					.getLoginTimes() : 0;
			user.setLoginTimes(loginTimes + 1);

			user.setLastLoginDate(now);
			user.setLastLoginIp(request.getRemoteAddr());

			userService.saveUser(user, user);

			request.getSession(true).setAttribute("loginUser", user);
			info = "true";
			AjaxWriter(response, info);
			return null;
		}
	}

	/**
	 * 用户登录时的处理 处理一个账号同时只有一个地方登录的关键
	 * 
	 * @param request
	 */
	public static String userLoginHandle(HttpServletRequest request) {
		// 当前登录的用户
		String userName = request.getParameter("userName");
		// 当前sessionId
		String sessionId = request.getSession().getId();
		Map<String, HttpSession> USER_SESSION = Constants.getUserSession();
		Map<String, String> SESSIONID_USER = Constants.getSessionIdUser();

		if (USER_SESSION.get(userName) != null) {
			return "当前账号已经在登录中";
		}

		// //删除当前sessionId绑定的用户，用户--HttpSession
		// USER_SESSION.remove(SESSIONID_USER.remove(sessionId));
		//
		// //删除当前登录用户绑定的HttpSession
		// HttpSession session=USER_SESSION.remove(userName);
		// if(session!=null){
		// SESSIONID_USER.remove(session.getId());
		// session.removeAttribute("loginName");
		// session.setAttribute("userMsg", "您的账号已经在另一处登录了,你被迫下线!");
		// return "";
		// }
		return "";
	}

}
