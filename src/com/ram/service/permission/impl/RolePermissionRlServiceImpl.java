package com.ram.service.permission.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRolePermissionRlDAO;
import com.ram.model.Permission;
import com.ram.model.Role;
import com.ram.model.RolePermissionRl;
import com.ram.model.User;
import com.ram.service.permission.IRolePermissionRlService;

public class RolePermissionRlServiceImpl extends BaseService implements
		IRolePermissionRlService {
	private IRolePermissionRlDAO rolePermissionRlDAO;
	private IRoleDAO roleDAO;
	private IPermissionDAO permissionDAO;
	
	public void setRolePermissionRlDAO(IRolePermissionRlDAO rolePermissionRlDAO) {
		this.rolePermissionRlDAO=rolePermissionRlDAO;
	}
	public void setPermissionDAO(IPermissionDAO permissionDao) {
		this.permissionDAO=permissionDao;
	}

	public void setRoleDAO(IRoleDAO roleDao) {
		this.roleDAO=roleDao;
	}
	////////////////////////
	
	public void createPermissionForRole(int roleId, int permissionId,User user) {
		Role role=roleDAO.getRole(new Integer(roleId));
		Permission permission=permissionDAO.getPermission(new Integer(permissionId));
		RolePermissionRl rp=new RolePermissionRl();
		rp.setPermission(permission);
		rp.setRole(role);
		this.checking();
		rolePermissionRlDAO.saveObject(rp,user);
		
	}

	public void removePermissionFromRole(int roleId, int permissionId,User user) {
		RolePermissionRl rp=rolePermissionRlDAO.getRolePermissionRl(roleId,permissionId);
		rolePermissionRlDAO.deleteObject(RolePermissionRl.class,rp.getRolePermissionRlId(),user);
	}
	public List findPermissionBelongToRole(int roleId) {
		return rolePermissionRlDAO.findPermissionBelongToRole(roleId);
	}
	public List findPermissionNotBelongToRole(int roleId) {
		return rolePermissionRlDAO.findPermissionNotBelongToRole(roleId);
	}
	public RolePermissionRl getRolePermissionRl(int roleId, int permissionId) {
		return rolePermissionRlDAO.getRolePermissionRl(roleId,permissionId);
	
	}



}
