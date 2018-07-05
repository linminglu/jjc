package com.ram.web.permission.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import com.ram.model.Permission;
import com.ram.model.User;
import com.ram.service.permission.IPermissionService;
import com.ram.web.permission.form.PermissionForm;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import javax.servlet.http.HttpSession;

public class PermissionListAction extends BaseDispatchAction {

	IPermissionService permissionService = (IPermissionService) getService("permissionService");

	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		PermissionForm permissionForm = (PermissionForm) form;
		List list = permissionService.findPermissions();
		request.setAttribute("permissionList", list);
		return mapping.findForward("init");
	}

	/**
	 * 删除权限
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public final ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		int permissionId = ParamUtils.getIntParameter(request, "permissionId",
				0);
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		permissionService.deletePermission(new Integer(permissionId),user);
		return mapping.findForward("delete");
	}
}
