package com.ram.service.permission;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.dao.permission.IRoleDAO;
import com.ram.dao.permission.IRoleFunctionPermissionRlDAO;
import com.ram.model.RoleFunctionPermissionRl;
import com.ram.model.User;

public interface IRoleFunctionPermissionRlService extends IService {
	public void setRoleFunctionPermissionRlDAO(
			IRoleFunctionPermissionRlDAO rfpDao);

	public void setFunctionDAO(IFunctionDAO functionDao);

	public void setRoleDAO(IRoleDAO roleDao);

	public void setPermissionDAO(IPermissionDAO permissionDao);

	public RoleFunctionPermissionRl getRoleFunctionPermissionRl(int roleId,
			int functionId, int permissionId);

	public List findPermissionsOfRoleAndFunction(int roleId, int functionId);

	public List findPermissionsForRoleAndFunction(int roleId, int functionId);

	public void saveRoleFunctionPermissionRl(int roleId, int functionId,
			int permissionId, User user);

	public void deleteRoleFunctionPermissionRl(int roleId, int functionId,int permissionId, User user);
}
