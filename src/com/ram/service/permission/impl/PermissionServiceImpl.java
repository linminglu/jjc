package com.ram.service.permission.impl;

import java.util.ArrayList;
import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.model.Permission;
import com.ram.model.User;
import com.ram.service.permission.IPermissionService;

public class PermissionServiceImpl extends BaseService implements IPermissionService {
	
	/**
	 * IOC方式注入权限DAO
	 * @param PermissionDAO
	 */
	private IPermissionDAO permissionDAO;
	
	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}

	/**
	 * 根据获得所有权限纪录
	 * @param elcourse
	 */
    public List findPermissions(){
		List perm=new ArrayList();
		perm=(List)permissionDAO.findPermissions();
		return perm;
		/**
		 * 用recover开头，表示设置status字段为0
		 * @param elcourse
		 */
	}
	 public void removePermission(Integer  id,User user){
		 permissionDAO.removePermission(id,user);
	 }
	 /**
		 * 根据主键真正删除权限纪录
		 * @param elcourse
		 */
	 public void deletePermission(Integer id,User user){
		 this.checking();
		 permissionDAO.deletePermission(id,user);
	 }
	 /**
		 * 用recover开头，表示设置status字段为1
		 * @param elcourse
		 */
	public void recoverPermission(Integer id,User user){
		permissionDAO.recoverPermission(id,user);
	}
	/**
	 * 根据对象保存权限纪录
	 * @param elcourse
	 */
	public void savePermission(Permission permission,User user){
		permissionDAO.savePermission(permission,user);
	}
	
	/**
	 * 根据主件获得权限对象
	 * @param elcourse
	 */
	public Permission getPermission(Integer id){
		Permission perm=new Permission();
		perm=(Permission)permissionDAO.getPermission(id);
		return perm;
	}

	public List findPermissionsNotBelongToRole(int roleId) {
		return permissionDAO.findPermissionsNotBelongToRole(roleId);
	}

	public Permission getPermissionByOperateValue(String operateValue) {
		return permissionDAO.getPermission(operateValue);
	}
	
	
}

