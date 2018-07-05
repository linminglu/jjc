package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.permission.ITreeForUserGroupService;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class UserGroupListAction extends BaseDispatchAction {
private final IUserService userService = (IUserService)getService("userService");
private ITreeForUserGroupService userGroupTreeService=(ITreeForUserGroupService)getService("treeForUserGroupService");	
	/** 
	 * 显示用户组列表页面
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
		getManagerInfo(request);
		return mapping.findForward("init");
	}
	
	
	/** 
	 * 删除用户组(userGroup.status=-1)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response){
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		if(request.getParameter("userGroupId") != null){
			int userGroupId = Integer.parseInt(request.getParameter("userGroupId"));
			userService.deleteUserGroup(userGroupId, user);
		}
		getManagerInfo(request);
		return mapping.findForward("delete");
	}
	
	/**
	 * 根据查询条件获得Manager信息列表
	 * @param request
	 * @param startIndex
	 * @param pageSize
	 * @param query
	 * @return
	 */
	 private boolean getManagerInfo(HttpServletRequest request){
		List userGroupList = userService.getAllUserGroup();
		request.setAttribute("userGroupList", userGroupList);
		return true;
     }
	 
	public ActionForward genTree(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response){
		
				int userGroupId=ParamUtils.getIntParameter(request,"userGroupId",0);
				if(userGroupId>0){
					//userGroupTreeService.createUserGroupTreeFolderWhenCreateUserGroup(userGroupId);
					userGroupTreeService.execute(userGroupId);
				}
				return mapping.findForward("genTree");
	}	 
}
