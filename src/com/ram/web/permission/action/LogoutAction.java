package com.ram.web.permission.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.framework.web.action.BaseDispatchAction;


public class LogoutAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");

	public ActionForward logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session=(HttpSession)request.getSession(false);
		User user=(User)session.getAttribute("loginUser");
		if(user!=null){
			userLogService.saveLog(user,"用户"+user.getLoginName()+"正常退出系统");
		}
		try{
			session.removeAttribute("loginUser");
			session.removeAttribute("loginUserGroup");
			session.invalidate();
		}catch(Exception ex){
			
		}
		return mapping.findForward("logout");
	}

}
