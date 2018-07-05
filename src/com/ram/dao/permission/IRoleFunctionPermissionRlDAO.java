package com.ram.dao.permission;

import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.Permission;
import com.ram.model.RoleFunctionPermissionRl;
import com.ram.model.RoleFunctionRl;
import com.ram.model.User;

public interface IRoleFunctionPermissionRlDAO extends IDAO {

	/**
	 * 列出某角色对某功能拥有的权限列表
	 * @param functionId
	 * @return
	 */
	public List findPermissionsOfRoleAndFunction(int roleId,int functionId);
	
	/**
	 * 查找可以分配某角色的某功能的权限列表
	 * @param functionId
	 * @return
	 */
	public List findPermissionsForRoleAndFunction(int roleId,int functionId);
	
	/**
	 * 设置某角色某功能下的权限值（保存）
	 * @param roleId
	 * @param functionId
	 * @param permissionId
	 * @param user
	 */
	public void saveRoleFunctionPermissionRl(RoleFunctionRl rf,Permission permission,User user);
	
	/**
	 * 删除某角色某功能下的权限值
	 * @param roleId
	 * @param functionId
	 * @param permissionId
	 * @param user
	 */
	public void deleteRoleFunctionPermissionRl(RoleFunctionRl rf,int permissionId,User user);
	
	
	/**
	 * 根据角色id，
	 * 功能id
	 * 权限id
	 * 获得RoleFunctionPermissionRl对象
	 * @param roleId
	 * @param functionId
	 * @param permission
	 * @return
	 */
	public RoleFunctionPermissionRl getRoleFunctionPermissionRl(int roleId,int functionId,int permissionId);
	
}
