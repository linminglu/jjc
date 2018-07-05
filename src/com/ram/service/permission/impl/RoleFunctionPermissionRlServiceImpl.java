package com.ram.service.permission.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRoleFunctionPermissionRlDAO;
import com.ram.dao.permission.IRoleFunctionRlDAO;
import com.ram.model.Permission;
import com.ram.model.Role;
import com.ram.model.RoleFunctionPermissionRl;
import com.ram.model.RoleFunctionRl;
import com.ram.model.RolePermissionRl;
import com.ram.model.User;
import com.ram.service.permission.IRoleFunctionPermissionRlService;

public class RoleFunctionPermissionRlServiceImpl extends BaseService implements
		IRoleFunctionPermissionRlService {
	private IFunctionDAO functionDAO;

	private IRoleDAO roleDAO;

	private IPermissionDAO permissionDAO;

	private IRoleFunctionRlDAO roleFunctionRlDAO;

	private IRoleFunctionPermissionRlDAO roleFunctionPermissionRlDAO;

	public void setFunctionDAO(IFunctionDAO functionDAO) {
		this.functionDAO = functionDAO;
	}

	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setRoleFunctionPermissionRlDAO(
			IRoleFunctionPermissionRlDAO roleFunctionPermissionRlDAO) {
		this.roleFunctionPermissionRlDAO = roleFunctionPermissionRlDAO;
	}

	public void deleteRoleFunctionPermissionRl(int roleId, int functionId,
			int permissionId, User user) {

		RoleFunctionRl rf = (RoleFunctionRl) roleFunctionRlDAO
				.getRoleFunctionRl(roleId, functionId);
		this.roleFunctionPermissionRlDAO.deleteRoleFunctionPermissionRl(rf,
				permissionId, user);
	}

	/*
	 * 查找角色（角色从角色本身具备的权限中取得最大权限范围）对某个功能可以分配的权限有哪些
	 */
	public List findPermissionsForRoleAndFunction(int roleId, int functionId) {
		// 1，先取该角色对该功能已经具有的权限		// 2，再取该角色具有的权限		// 两个相减
		
		//该角色已经具备的权限有：permissionListOfRoleFunction
		List permissionListOfRoleFunction = roleFunctionPermissionRlDAO
				.findPermissionsOfRoleAndFunction(roleId, functionId);
		
		log.info("permissionListOfRoleFunction.size=" + permissionListOfRoleFunction.size());
		
		Role role = roleDAO.getRole(new Integer(roleId));
		Set permissionSetOfRole = role.getRolePermissionRls();
		
		log.info("permissionSetOfRole.size=" + permissionSetOfRole.size());
		List permissionListOfRole = new ArrayList();
		//该角色拥有的权限有：permissionSetOfRole
		this.checking();
		Iterator itRoleOwn = permissionSetOfRole.iterator();
		while (itRoleOwn.hasNext()) {
			RolePermissionRl rp = (RolePermissionRl) itRoleOwn.next();
			permissionListOfRole.add(rp.getPermission());
		}
		//相减		
		permissionListOfRole.removeAll(permissionListOfRoleFunction);
		return permissionListOfRole;
	}

	public List findPermissionsOfRoleAndFunction(int roleId, int functionId) {
		return this.roleFunctionPermissionRlDAO
				.findPermissionsOfRoleAndFunction(roleId, functionId);
	}

	public RoleFunctionPermissionRl getRoleFunctionPermissionRl(int roleId,
			int functionId, int permissionId) {
		return this.roleFunctionPermissionRlDAO.getRoleFunctionPermissionRl(
				roleId, functionId, permissionId);

	}

	public void saveRoleFunctionPermissionRl(int roleId, int functionId,
			int permissionId, User user) {
		RoleFunctionRl rf = roleFunctionRlDAO.getRoleFunctionRl(roleId,
				functionId);
		Permission permission = permissionDAO.getPermission(new Integer(
				permissionId));
		this.roleFunctionPermissionRlDAO.saveRoleFunctionPermissionRl(rf,
				permission, user);

	}

	public void setRoleFunctionRlDAO(IRoleFunctionRlDAO roleFunctionRlDAO) {
		this.roleFunctionRlDAO = roleFunctionRlDAO;
	}

}
