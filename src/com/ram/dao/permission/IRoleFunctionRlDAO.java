package com.ram.dao.permission;

import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.RoleFunctionRl;
import com.ram.model.User;

public interface IRoleFunctionRlDAO extends IDAO {

	/**
	 * 列出某功能下的，拥有该功能的角色列表
	 * @param functionId
	 * @return
	 */
	public List findRolesByFunction(int functionId);
	
	/**
	 * 查找可以分配某功能的角色列表
	 * @param functionId
	 * @return
	 */
	public List findRolesForFunction(int functionId);
	
	public void saveRoleFunctionRl(int roleId,int functionId,User user);
	
	public void deleteRoleFunctionRl(int roleId,int functionId,User user);
	
	public RoleFunctionRl getRoleFunctionRl(int roleId,int functionId);
	
}
