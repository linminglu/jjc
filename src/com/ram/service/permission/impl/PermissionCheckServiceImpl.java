package com.ram.service.permission.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IGroupFunctionRlDAO;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRoleFunctionPermissionRlDAO;
import com.ram.dao.user.IUserDAO;
import com.ram.model.Function;
import com.ram.model.Permission;
import com.ram.model.RoleFunctionPermissionRl;
import com.ram.service.permission.IPermissionCheckService;

public class PermissionCheckServiceImpl extends BaseService implements
		IPermissionCheckService {

	private IFunctionDAO functionDAO;

	private IUserDAO userDAO;

	private IGroupFunctionRlDAO groupFunctionRlDAO;

	private IRoleFunctionPermissionRlDAO roleFunctionPermissionDAO;

	private IRoleDAO roleDAO;

	private IPermissionDAO permissionDAO;

	public Function getFunctionByServletPath(String functionActionPath) {
		return functionDAO.getFunction(functionActionPath);
	}

	/**
	 * 判断某个用户组对某个功能的ActionServletPath是否具有访问权限
	 */
	public boolean hasPermissionOnFunction(int userGroupId,
			String functionActionPath) {

		Function function = functionDAO.getFunction(functionActionPath);
		return this.hasPermissionOnFunction(userGroupId, function
				.getFunctionId().intValue());
	}

	/**
	 * 判断某个用户组对某个功能是否具有访问权限
	 */
	public boolean hasPermissionOnFunction(int userGroupId, int functionId) {
		return groupFunctionRlDAO.hasRelationInDB(userGroupId, functionId);
	}

	/**
	 * 判断某个人对某个功能中的某个操作标签是否具有权限 1，得到指定人的角色(一个人可能有多个角色) 2
	 */
	public boolean hasPermissionOnOperation(int userId, int functionId,
			String operationValue) {
		//User user = userDAO.getUser(userId);
		
		List roleIds = roleDAO.getRolesOfUser(userId);
		int roleId = 0;
		for (int i = 0; i < roleIds.size(); i++) {
			roleId = ((Integer) roleIds.get(i)).intValue();
//			log.info("roleId=" + roleId);
//			log.info("functionId=" + functionId);
//			log.info("operationValue=" + operationValue);
			if (this.hasPermissionOnOperationByRole(roleId, functionId,
					operationValue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断角色对某个功能的某个标签是否具有权限
	 */
	private boolean hasPermissionOnOperationByRole(int roleId, int functionId,
			String operationValue) {
		Permission p = (Permission) permissionDAO.getPermission(operationValue);
		if(p!=null){
			RoleFunctionPermissionRl rfp = roleFunctionPermissionDAO
				.getRoleFunctionPermissionRl(roleId, functionId, p
						.getPermissionId().intValue());
			if (rfp == null) {
				return false;
			} else {
				return true;
			}
		}else{
			return false;
		}
	}

	public void setFunctionDAO(IFunctionDAO functionDAO) {
		this.functionDAO = functionDAO;
	}

	public void setGroupFunctionRlDAO(IGroupFunctionRlDAO groupFunctionRlDAO) {
		this.groupFunctionRlDAO = groupFunctionRlDAO;
	}

	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setRoleFunctionPermissionDAO(
			IRoleFunctionPermissionRlDAO roleFunctionPermissionDAO) {
		this.roleFunctionPermissionDAO = roleFunctionPermissionDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
