package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class UserGroupManageCreateAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	
	/** 
	 * 用户组列表页面初始化
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		
		int userId=ParamUtils.getIntParameter(request,"userId",0);
		log.info(this.getClass().getName() +  ",init(),userId=" + userId);
		List userGroupList = userService.getAvailableUserGroupById(userId);
		request.setAttribute("userGroupList", userGroupList);
		//把目前用户登录的用户组放到reqeust里面
		UserGroup loginUserGroup=(UserGroup)request.getSession(false).getAttribute("loginUserGroup");
		//log.info("当前用户登录到的用户组:" + loginUserGroup);
		if(loginUserGroup!=null){
			//log.info("当前用户登录到的用户组:" + loginUserGroup.getUserGroupName());
			request.setAttribute("loginUserGroup",loginUserGroup);
		}else{
			return mapping.findForward("error");
		}
		
		if("manager".equals(request.getParameter("type"))){
			log.info(this.getClass().getName() +  ",init(),goto managerInit");
			return mapping.findForward("managerInit");
		}
		else{
			log.info(this.getClass().getName() +  ",init(),goto tutorInit");
			return mapping.findForward("tutorInit");
		}
	}
	
	/** 
	 * 给用户添加用户组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward create(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		if(request.getParameterValues("selIndex") != null){
			int userId = Integer.parseInt(request.getParameter("userId"));
			String[] userGroupIndexes = request.getParameterValues("selIndex");
			int[] userGroupId = new int[userGroupIndexes.length];
			for(int i = 0;i < userGroupId.length;i++){
				userGroupId[i] = Integer.parseInt(userGroupIndexes[i]);
			}
			userService.addUserAndGroupRl(userId, userGroupId, user);
		}
		return mapping.findForward("create");
	}
}

