package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Role;
import com.ram.model.User;
import com.ram.service.permission.IPermissionService;
import com.ram.service.permission.IRolePermissionRlService;
import com.ram.service.permission.IRoleService;
import com.ram.web.permission.form.RolePermissionRlForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class RolePermissionRlCreateAction extends BaseDispatchAction {
	
	private IRoleService roleService = (IRoleService) getService("roleService");
	private IPermissionService permissionService = (IPermissionService) getService("permissionService");
	private IRolePermissionRlService rolePermissionRlService=(IRolePermissionRlService)getService("rolePermissionRlService");
	
	/**
	 * 显示还没有分配给该角色的权限的列表
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
		
		//Role role=(Role)request.getAttribute("role");
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		Role role=roleService.getRole(new Integer(roleId));
		List permissionList=permissionService.findPermissionsNotBelongToRole(roleId);
		request.setAttribute("role",role);
		request.setAttribute("permissionList",permissionList);
		return mapping.findForward("init");
	}

	/**
	 * 为角色分配操作权限
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int permissionId=ParamUtils.getIntParameter(request,"permissionId",0);
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		if(permissionId>0 && roleId>0){
			RolePermissionRlForm rpForm=(RolePermissionRlForm)form;
			//为角色分配操作权限
			log.info("为角色roleid=" +roleId+"分配操作权限permissionId=" + permissionId);
			rolePermissionRlService.createPermissionForRole(roleId,permissionId,user);
		}

		return mapping.findForward("save");
	}

	
	/**
	 * 把角色已经拥有的权限移除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int roleId = ParamUtils.getIntParameter(request, "roleId",0);
		int permissionId = ParamUtils.getIntParameter(request, "permissionId",0);
		if(roleId>0 && permissionId>0){
			rolePermissionRlService.removePermissionFromRole(roleId,permissionId,user);
		
		}
		Role role=(Role)roleService.getRole(new Integer(roleId));
		request.setAttribute("role",role);
		return mapping.findForward("list");
	}

}
