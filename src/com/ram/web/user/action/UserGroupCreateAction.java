package com.ram.web.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.permission.ITreeForUserGroupService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.UserGroupForm;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class UserGroupCreateAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private ITreeForUserGroupService userGroupTreeService=(ITreeForUserGroupService)getService("treeForUserGroupService");
	
	/** 
	 * 显示新建用户组页面
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
		return mapping.findForward("init");
	}
	
	/** 
	 * 创建用户组,返回用户组列表页面
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
  		
		UserGroupForm userGroupForm = (UserGroupForm) form;
		UserGroup userGroup = userGroupForm.getUserGroup();
		int userGroupId = userService.saveUserGroup(userGroup, user);
		userGroupTreeService.createUserGroupTreeFolderWhenCreateUserGroup(userGroupId);
		return mapping.findForward("create");
	}
}
