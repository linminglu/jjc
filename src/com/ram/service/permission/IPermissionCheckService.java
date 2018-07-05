package com.ram.service.permission;

import com.framework.service.IService;
import com.ram.model.Function;

public interface IPermissionCheckService
extends IService{

	public boolean hasPermissionOnFunction(int userGroupId,int functionId);
	public boolean hasPermissionOnFunction(int userGroupId,String functionActionPath);
	public boolean hasPermissionOnOperation(int userId,int functionId,String operationValue);
	public Function getFunctionByServletPath(String functionActionPath);
	
}
