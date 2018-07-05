package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.permission.IRoleService;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class RoleListAction extends BaseDispatchAction {
	private final IRoleService roleService = (IRoleService)getService("roleService");
	
	/** 
	 * 管理者的职位列表页面初始化
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
		int userId = Integer.parseInt(request.getParameter("userId"));
		List roleList = roleService.findRoleById(userId);
		request.setAttribute("roleList", roleList);
		request.setAttribute("userId", request.getParameter("userId"));
		if("manager".equals(request.getParameter("type"))){
			return mapping.findForward("managerInit");
		}
		else{
			return mapping.findForward("tutorInit");
		}
	}
	
	/** 
	 * 删除指定用户的角色
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
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		roleService.deleteUserAndRoleRl(userId, roleId, user);
		return mapping.findForward("delete");
	}
}

