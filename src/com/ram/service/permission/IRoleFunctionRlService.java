package com.ram.service.permission;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRoleFunctionRlDAO;
import com.ram.model.User;

public interface IRoleFunctionRlService extends IService {
	public void setRoleFunctionRlDAO(IRoleFunctionRlDAO rfDao);
	public void setFunctionDAO(IFunctionDAO fDao);
	public void setRoleDAO(IRoleDAO rDao);
	
	/**
	 * 拥有某个功能的角色列表
	 * @param functionId
	 * @return
	 */
	public List findRolesHavingFunction(int functionId);
	
	
	/**
	 * 可以分配给Function的角色列表
	 * @param functionId
	 * @return
	 */
	public List findRolesForFunction(int functionId);
	
	/**
	 * 存储role和function的关系
	 * 即：为功能制定/分派角色
	 * @param roleId
	 * @param functionId
	 */
	public void saveRoleFunctionRl(int roleId,int functionId,User user);
	
	/**
	 * 将role和function的关系删除
	 * 即：取消功能制定/分派角色
	 * @param roleId
	 * @param functionId
	 */
	public void deleteRoleFunctionRl(int roleId,int functionId,User user);
}
