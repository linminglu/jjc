package com.ram.dao.permission;

import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.RolePermissionRl;

/**
 * @author hulei
 *
 */
public interface IRolePermissionRlDAO extends IDAO {

	/**
	 * 查找指定角色下的权限列表
	 * @param roleId
	 * @return
	 */
	public List findPermissionBelongToRole(int roleId);

	public List findPermissionNotBelongToRole(int roleId);
	/**
	 * 获得一个角色下的权限数量
	 * @param roleId
	 * @return
	 */
	public int getPermissionNumberOfRole(int roleId);
	
	public RolePermissionRl getRolePermissionRl(int roleId,int permissionId);
}
