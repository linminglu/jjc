package com.ram.service.permission;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRolePermissionRlDAO;
import com.ram.model.RolePermissionRl;
import com.ram.model.User;

public interface IRolePermissionRlService extends IService{

	public void setRolePermissionRlDAO(IRolePermissionRlDAO rpDao);
	public void setPermissionDAO(IPermissionDAO pDao);
	public void setRoleDAO(IRoleDAO rDao);
	
	public RolePermissionRl getRolePermissionRl(int roleId,int permissionId);
	
	/**
	 * 为角色分配权限
	 * @param roleId
	 * @param permissionId
	 */
	public void createPermissionForRole(int roleId,int permissionId,User user);
	
	
	/**
	 * 把角色的权限删除
	 */
	public void removePermissionFromRole(int roleId,int permissionId,User user);
	
	
	/**
	 * 查找role下具有的已经分配的权限
	 * @param roleId
	 */
	public List findPermissionBelongToRole(int roleId);
	
	
	/**
	 * 查找role下尚未分配的权限
	 */
	public List findPermissionNotBelongToRole(int roleId);
	
}
