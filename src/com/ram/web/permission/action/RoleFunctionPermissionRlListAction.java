package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Function;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;
import com.ram.service.permission.IRoleFunctionPermissionRlService;
import com.ram.service.permission.IRoleService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
/**
 * 角色功能分配列表
 * 
 * 或者:
 * 功能下的角色列表
 * 
 * @author hulei
 *
 */
public class RoleFunctionPermissionRlListAction extends BaseDispatchAction {

	private IRoleFunctionPermissionRlService rfpService=(IRoleFunctionPermissionRlService)this.getService("roleFunctionPermissionRlService");
	private IFunctionService functionService=(IFunctionService)this.getService("functionService");
	private IRoleService roleService=(IRoleService)this.getService("roleService");
	/**
	 * 在功能管理界面
	 * 点击功能指派给角色按钮后
	 * 进入的为功能指派角色界面的init方法
	 * 该方法显示：所有该功能下的，拥有该功能的role
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
		
		int functionId=ParamUtils.getIntParameter(request,"functionId",0);
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		List permissionListOfRoleAndFunction=null;
		Function function=null;
		Role role=null;
		if(functionId>0){
			//列出该功能下的所有角色列表
			function=functionService.getFunction(new Integer(functionId));
			role=roleService.getRole(new Integer(roleId));
			permissionListOfRoleAndFunction=rfpService.findPermissionsOfRoleAndFunction(roleId,functionId); 
		}
		request.setAttribute("function",function);
		request.setAttribute("role",role);
		request.setAttribute("permissionListOfRoleAndFunction",permissionListOfRoleAndFunction);
		return mapping.findForward("init");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		int functionId=ParamUtils.getIntParameter(request,"functionId",0);
		int permissionId=ParamUtils.getIntParameter(request,"permissionId",0);
		request.setAttribute("functionId",String.valueOf(functionId));
		request.setAttribute("roleId",String.valueOf(roleId));
		rfpService.deleteRoleFunctionPermissionRl(roleId,functionId,permissionId,user);
		return mapping.findForward("delete");
	}
}
