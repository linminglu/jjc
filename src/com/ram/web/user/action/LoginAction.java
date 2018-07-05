package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
public class LoginAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");

	/**
	 * 显示用户登陆页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String action = ParamUtils.getParameter(request, "action","");
		request.setAttribute("action", action);
		return mapping.findForward("init");
	}

	public ActionForward loginBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int userId=ParamUtils.getIntParameter(request,"uid",0);
		//int userGroupId=ParamUtils.getIntParameter(request,"gid",0);
		List loginUserGroupList =userService.getUserGroupById(userId);
		//UserGroup ug=(UserGroup)userService.getUserGroupByID(userGroupId);
		request.setAttribute("loginUserGroupList",loginUserGroupList);
		return mapping.findForward("loginUserGroupInit");
	}
	/**
	 * 进行用户登陆操作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		//LoginForm loginForm = (LoginForm) form;
		String loginName = ParamUtils.getParameter(request, "userName");
		String password = ParamUtils.getParameter(request, "password");
		boolean validateOk=false;//验证码验证是否通过
		validateOk=true;
		log.fatal("#################login:"+loginName+","+password);
		if(validateOk){
			//如果用户的验证码和随机验证码相同		
			log.info("开始进行用户密码校验....");
			User user = userService.getUser(loginName, password);
			if (user == null) {
				request.setAttribute("error", "1");
				this.updateUserLog(request, null, "[Login Fail]["+loginName+","+password+"]",loginName);
				return mapping.findForward("login");
			} else {
				Integer loginTimes = user.getLoginTimes();
				if(!ParamUtils.chkInteger(loginTimes)) loginTimes = 0;
				user.setLoginTimes((loginTimes+1));
				user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
				user.setLastLoginIp(request.getRemoteAddr());
				userService.saveUser(user,user);
				this.setSessionUser(request, user);
				this.updateUserLog(request, user, "[Login]",loginName);
				return mapping.findForward("loginSuccess");
			}
		}else{
			//如果验证码不同			request.setAttribute("error", "2");
			return mapping.findForward("login");
		}
	}
	
	public ActionForward loginm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		//LoginForm loginForm = (LoginForm) form;
		String loginName = ParamUtils.getParameter(request, "userName");
		String password = ParamUtils.getParameter(request, "password");
		boolean validateOk=false;//验证码验证是否通过
		validateOk=true;
		log.fatal("#################loginm:"+loginName+","+password);
		if(validateOk){
			//如果用户的验证码和随机验证码相同		
			log.info("开始进行用户密码校验....");
			User user = userService.getUser(loginName, password);
			if (user == null) {
				request.setAttribute("error", "1");
				this.updateUserLog(request, null, "[Loginm Fail]["+loginName+","+password+"]",loginName);
				return mapping.findForward("login");
			} else {
				Integer loginTimes = user.getLoginTimes();
				if(!ParamUtils.chkInteger(loginTimes)) loginTimes = 0;
				user.setLoginTimes((loginTimes+1));
				user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
				user.setLastLoginIp(request.getRemoteAddr());
				userService.saveUser(user,user);
				this.setSessionUser(request, user);
				this.updateUserLog(request, user, "[Loginm]",loginName);
				return mapping.findForward("loginSuccessm");
			}
		}else{
			//如果验证码不同
			request.setAttribute("error", "2");
			return mapping.findForward("login");
		}
	}
	
	public ActionForward logout(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,HttpServletResponse response ){
	HttpSession session = (HttpSession) request.getSession(false);		
	try {
		session.removeAttribute("loginUser");
		session.removeAttribute("loginUserGroup");
		session.invalidate();
//		removeCookies4FS(request, response);
	} catch (Exception ex) {
		log.warn("用户登出时出现异常.", ex);
	}
	return mapping.findForward("logout");
}
}
