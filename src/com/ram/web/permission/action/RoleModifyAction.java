package com.ram.web.permission.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Role;
import com.ram.model.User;
import com.ram.service.permission.IRoleService;
import com.ram.web.permission.form.RoleForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class RoleModifyAction extends BaseDispatchAction {
	private IRoleService roleService = (IRoleService) getService("roleService");

	/**
	 * 初始化列表
	 * 
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
		RoleForm roleForm = (RoleForm) form;
		String id = ParamUtils.getParameter(request,"id");
		Role role = roleService.getRole(Integer.valueOf(id));
		roleForm.setRole(role);
		return mapping.findForward("init");
	}

	/**
	 * 修改操作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm roleForm = (RoleForm) form;
		// String id = request.getParameter("role.roleId");
		// Role role = roleService.getRole(Integer.valueOf(id));
		Role role = roleForm.getRole();
		//从session中取得User对象
		User user = null;
		roleService.modifyRole(role,user);
		return mapping.findForward("list");
	}

}
