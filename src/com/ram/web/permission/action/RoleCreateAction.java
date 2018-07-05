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
import com.framework.web.action.BaseDispatchAction;

public class RoleCreateAction extends BaseDispatchAction {
	private IRoleService roleService = (IRoleService) getService("roleService");

	/**
	 * 添加角色的init入口
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
		// TODO process request and return an ActionForward instance, for
		// example:
		// return mapping.findForward("forward_name");
		saveToken(request);
		return mapping.findForward("init");
	}

	/**
	 * 创建角色
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO process request and return an ActionForward instance, for
		// example:
		// return mapping.findForward("forward_name");
		RoleForm roleForm = (RoleForm) form;
		// BeanUtils.copyProperties(rol)
		Role role = roleForm.getRole();
		// role.setRoleName(roleForm.getRole().getRoleName());
		//从session中取得User对象
		User user = null;
		roleService.createRole(role,user);
		saveToken(request);
		return mapping.findForward("list");
	}
}
