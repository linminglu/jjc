package com.ram.service.permission.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRoleFunctionRlDAO;
import com.ram.model.User;
import com.ram.service.permission.IRoleFunctionRlService;

public class RoleFunctionRlServiceImpl  extends BaseService implements
IRoleFunctionRlService {
	private IFunctionDAO functionDAO;
	private IRoleDAO roleDAO;
	private IRoleFunctionRlDAO roleFunctionRlDAO;
	
	public void setFunctionDAO(IFunctionDAO fDao) {
		this.functionDAO=fDao;
		
	}

	public void setRoleDAO(IRoleDAO rDao) {
		this.roleDAO=rDao;
	}

	public void setRoleFunctionRlDAO(IRoleFunctionRlDAO rfDao) {
		this.roleFunctionRlDAO=rfDao;
	}

	/**
	 * 查找拥有某个功能的角色
	 * @param functionId
	 * @return
	 */
	public List findRolesHavingFunction(int functionId){
		return roleFunctionRlDAO.findRolesByFunction(functionId);
	}

	public List findRolesForFunction(int functionId) {
		
		return roleFunctionRlDAO.findRolesForFunction(functionId);
	}

	public void deleteRoleFunctionRl(int roleId, int functionId,User user) {
		roleFunctionRlDAO.deleteRoleFunctionRl(roleId,functionId,user);
		
	}

	public void saveRoleFunctionRl(int roleId, int functionId,User user) {
		roleFunctionRlDAO.saveRoleFunctionRl(roleId,functionId,user);
	}
	
	
	
}
