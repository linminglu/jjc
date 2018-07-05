package com.ram.web.permission.action;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.Permission;
import com.ram.model.User;
import com.ram.service.permission.IPermissionService;
import com.ram.web.permission.form.PermissionForm;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.web.action.BaseDispatchAction;

public class PermissionCreateAction extends BaseDispatchAction{
	
	IPermissionService permissionService = (IPermissionService) getService("permissionService");
	
	/** 
	 * 初始化登陆到添加页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
          
		return mapping.findForward("init");
	}

	/** 
	 * 添加权限页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public final ActionForward save(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
		
		    PermissionForm permissionForm=(PermissionForm)form;
		    Permission permission =new Permission();
		    permission.setPermissionTitle(permissionForm.getPermissionTitle());
		    permission.setPermissionValue(permissionForm.getPermissionValue());
		    
			//获取当前用户
			User user = null;
			user = ( User )request.getSession( false ).getAttribute( "loginUser" );
			
		    permissionService.savePermission(permission,user);
		    return mapping.findForward("save");
		}
}