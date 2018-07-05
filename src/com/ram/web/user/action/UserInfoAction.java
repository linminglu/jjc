package com.ram.web.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.permission.IRoleService;
import com.ram.service.user.IUserService;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
/**
 * @author hulei 
 */
public class UserInfoAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final IRoleService roleService = (IRoleService)getService("roleService");
	
	/**
	 * 显示用户创建页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user=(User)request.getSession().getAttribute("loginUser");
		if(user!=null){
			
			log.info("user.username=" + user.getLoginName());
			log.info("user.getusertype=" + user.getUserType());
			request.setAttribute("userId", user.getUserId());
			if(user.getUserType().equals(RamConstants.UserTypeIsLearner3)){
				return mapping.findForward("learnerInfo");
			}else if(user.getUserType().equals(RamConstants.UserTypeIsTutor1)){
				return mapping.findForward("tutorInfo");
			}else{
				return mapping.findForward("managerInfo");
			}
		}else{
			return mapping.findForward("errorUserInfo");
		}
	}
	



}
