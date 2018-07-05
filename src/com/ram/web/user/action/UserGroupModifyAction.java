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
public class UserGroupModifyAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private ITreeForUserGroupService userGroupTreeService=(ITreeForUserGroupService)getService("treeForUserGroupService");

	/** 
	 * 显示修改功能名称页面
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
		UserGroupForm userGroupForm = new UserGroupForm();
		if(request.getParameter("userGroupId") != null){
			request.setAttribute("userGroupId", request.getParameter("userGroupId"));
			int userGroupId = Integer.parseInt(request.getParameter("userGroupId"));
			UserGroup userGroup = userService.getUserGroupByID(userGroupId);
			userGroupForm.setUserGroup(userGroup);
			request.setAttribute("userGroupModifyInitForm", userGroupForm);
		}
		return mapping.findForward("init");
	}
	
	/** 
	 * 修改Manager,并且返回Manager列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward modify(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		UserGroupForm userGroupForm = (UserGroupForm)form;
		if(request.getParameter("userGroupId") != null){
			int userGroupId = Integer.parseInt(request.getParameter("userGroupId"));
			UserGroup userGroup = userService.getUserGroupByID(userGroupId);
			userGroup.setUserGroupDesc(userGroupForm.getUserGroup().getUserGroupDesc());
			userGroup.setUserGroupName(userGroupForm.getUserGroup().getUserGroupName());
			userService.saveUserGroup(userGroup, user);
			userGroupTreeService.createUserGroupTreeFolderWhenCreateUserGroup(userGroupId);			
		}
		return mapping.findForward("modify");
	}
}
