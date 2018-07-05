package com.ram.web.permission.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;

import com.ram.model.Function;
import com.ram.model.Manager;
import com.ram.model.Permission;
import com.ram.model.User;
import com.ram.service.permission.IPermissionService;
import com.ram.web.permission.form.PermissionForm;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import javax.servlet.http.HttpSession;

public class PermissionModifyAction extends BaseDispatchAction {

	IPermissionService permissionService = (IPermissionService) getService("permissionService");

	/**
	 * 跳转到修改权限页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		int permissionId=ParamUtils.getIntParameter(request,"permissionId",0);
		Permission permission = permissionService.getPermission(new Integer(permissionId));
		
		PermissionForm permissionForm = (PermissionForm) form;
		permissionForm.setPermissionTitle(permission.getPermissionTitle());
		permissionForm.setPermissionValue(permission.getPermissionValue());

		request.setAttribute("permissionId", String.valueOf(permissionId));

		return mapping.findForward("init");
	}

	/**
	 * 修改权限页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		
		int permissionId=ParamUtils.getIntParameter(request,"permissionId",0);
		Permission permission = permissionService.getPermission(new Integer(permissionId));
		
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		PermissionForm permissionForm = (PermissionForm) form;
		permission.setPermissionTitle(permissionForm.getPermissionTitle());
		permission.setPermissionValue(permissionForm.getPermissionValue());
		permissionService.savePermission(permission,user);
		return mapping.findForward("save");
	}
}
