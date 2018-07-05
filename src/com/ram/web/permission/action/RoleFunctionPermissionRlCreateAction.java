package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Function;
import com.ram.model.Permission;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;
import com.ram.service.permission.IPermissionService;
import com.ram.service.permission.IRoleFunctionPermissionRlService;
import com.ram.service.permission.IRoleFunctionRlService;
import com.ram.service.permission.IRolePermissionRlService;
import com.ram.service.permission.IRoleService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * 把功能分配给角色
 * @author hulei
 *
 */
public class RoleFunctionPermissionRlCreateAction extends BaseDispatchAction {
	public IFunctionService functionService = (IFunctionService) getService("functionService");
	private IRoleService roleService = (IRoleService) getService("roleService");
	private IPermissionService permissionService = (IPermissionService) getService("permissionService");
	private IRolePermissionRlService rolePermissionRlService=(IRolePermissionRlService)getService("rolePermissionRlService");
	private IRoleFunctionRlService roleFunctionRlService=(IRoleFunctionRlService)this.getService("roleFunctionRlService");
	private IRoleFunctionPermissionRlService roleFunctionPermissionRlService=(IRoleFunctionPermissionRlService)this.getService("roleFunctionPermissionRlService");
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
		int roleId=ParamUtils.getIntParameter(request,"roleId",0);
		if(functionId==0){
			functionId=ParamUtils.getIntAttribute(request,"functionId",0);
		}
		if(roleId==0){
			roleId=ParamUtils.getIntAttribute(request,"roleId",0);
		}
		
		List permissionListFor_rf=null;
		Function function=null;
		Role role=null;
		if(functionId>0 && roleId>0){
			//列出该功能下的所有角色列表
			function=functionService.getFunction(new Integer(functionId));
			role=roleService.getRole(new Integer(roleId));
			permissionListFor_rf=roleFunctionPermissionRlService.findPermissionsForRoleAndFunction(roleId,functionId); 
			log.info("permissionListFor_rf.size=" +permissionListFor_rf.size());
		}
		request.setAttribute("function",function);
		request.setAttribute("role",role);
		request.setAttribute("permissionListFor_rf",permissionListFor_rf);		
		for(int i=0;i<permissionListFor_rf.size();i++){
			Permission p=(Permission)permissionListFor_rf.get(i);
			log.info("p=" + p.getPermissionTitle());
		}
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
		int permissionId=ParamUtils.getIntParameter(request,"permissionId",0);
		if(roleId>0 && functionId>0 && permissionId>0){
			roleFunctionPermissionRlService.saveRoleFunctionPermissionRl(roleId,functionId,permissionId,user);
		}
		request.setAttribute("functionId",String.valueOf(functionId));
		request.setAttribute("roleId",String.valueOf(roleId));
		return mapping.findForward("save");
	}
}
