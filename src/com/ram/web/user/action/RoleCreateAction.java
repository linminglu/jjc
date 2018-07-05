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
public class RoleCreateAction extends BaseDispatchAction {
	private final IRoleService roleService = (IRoleService)getService("roleService");
	
	/** 
	 * 管理者可选职位列表页面初始化
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
		List roleList = roleService.findAvailableRoleById(userId);
		request.setAttribute("roleList", roleList);
		if("manager".equals(request.getParameter("type"))){
			return mapping.findForward("managerInit");
		}
		else{
			return mapping.findForward("tutorInit");
		}
	}
	
	/** 
	 * 添加管理者的职位
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
			String[] roleIndexes = request.getParameterValues("selIndex");
			int[] roleId = new int[roleIndexes.length];
			for(int i = 0;i < roleId.length;i++){
				roleId[i] = Integer.parseInt(roleIndexes[i]);
			}
			roleService.saveUserAndRoleRl(userId, roleId, user);
		}
		return mapping.findForward("create");
	}
}

