package com.ram.web.permission.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.user.IUserService;
import com.ram.web.permission.form.LoginUserGroupForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 * 
 */
public class LoginUserGroupAction extends BaseDispatchAction {

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
		return mapping.findForward("loginUserGroupPage");
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
	public ActionForward loginUserGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
		User userName = null;
		userName = ( User )request.getSession( false ).getAttribute("loginUser");
			
		int userGroupId = ParamUtils.getIntParameter(request,
				"ug", 0);
		if (userGroupId == 0) {
			log.warn("登陆时候，传入的用户组ID=0！登陆失败！");
			userLogService.saveLog(userName,"用户登录用户组失败！登陆失败");
			request.getSession(false).removeAttribute("loginUser");
			return mapping.findForward("loginUserGroupFail");
		} else {
			UserGroup ug = (UserGroup) userService
					.getUserGroupByID(userGroupId);
			if (ug == null) {
				userLogService.saveLog(userName,"用户登录的用户组无效！登陆失败");
				log.info("登陆时候，无法根据传入的用户组ID获得正确的用户组对象！登陆失败！");
				request.getSession(false).removeAttribute("loginUserGroup");
				return mapping.findForward("loginUserGroupFail");
			} else {
				request.getSession(false).setAttribute("loginUserGroup", ug);
				User user=(User)request.getSession().getAttribute("loginUser");
				request.setAttribute("loginUser",user);
				//把用户登录到的用户组，也写入到信息中。
				request.getSession(false).setAttribute("loginUserGroup",ug);
				userLogService.saveLog(user,"用户登录平台的用户组["+ug.getUserGroupName()+"]界面成功！");				
				return mapping.findForward("loginUserGroupSuccess");
			}

		}

	}
}
