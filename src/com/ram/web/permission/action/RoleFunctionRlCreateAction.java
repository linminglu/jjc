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
import com.ram.service.permission.IRoleFunctionRlService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * 把功能分配给角色
 * @author hulei
 *
 */
public class RoleFunctionRlCreateAction extends BaseDispatchAction {
	public IFunctionService functionService = (IFunctionService) getService("functionService");
	//private IRoleService roleService = (IRoleService) getService("roleService");
	//private IPermissionService permissionService = (IPermissionService) getService("permissionService");
	//private IRolePermissionRlService rolePermissionRlService=(IRolePermissionRlService)getService("rolePermissionRlService");
	private IRoleFunctionRlService roleFunctionRlService=(IRoleFunctionRlService)this.getService("roleFunctionRlService");

	/**
	 * 为功能分派角色/制定角色
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward init(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		int functionId=ParamUtils.getIntParameter(request,"functionId",0);
		if(functionId==0){
			functionId=ParamUtils.getIntAttribute(request,"functionId",0);
		}
		List rolesListForTheFunction=null;
		Function function=null;
		if(functionId>0){
			//列出该功能下的所有角色列表
			function=functionService.getFunction(new Integer(functionId));
			rolesListForTheFunction=roleFunctionRlService.findRolesForFunction(functionId); 
		}
		request.setAttribute("function",function);
		request.setAttribute("rolesListForTheFunction",rolesListForTheFunction);		
		return mapping.findForward("init");
	}
	
	public ActionForward save(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		int functionId=ParamUtils.getIntParameter(request,"functionId",0);
		if(roleId>0 && functionId>0){
			roleFunctionRlService.saveRoleFunctionRl(roleId,functionId,user);
		}
		request.setAttribute("functionId",String.valueOf(functionId));
		return mapping.findForward("save");
	}
}
