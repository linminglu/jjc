package com.ram.service.permission;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.permission.IPermissionDAO;
import com.ram.model.Permission;
import com.ram.model.User;

public interface IPermissionService extends IService {
	/**
	 * IOC方式注入权限DAO
	 * @param PermissionDAO
	 */
	public void setPermissionDAO(IPermissionDAO permissionDAO);
	/**
	 * 用recover开头，表示设置status字段为0
	 * @param elcourse
	 */
	public void removePermission(Integer  id,User user);
	/**
	 * 根据主键真正删除权限纪录
	 * @param elcourse
	 */
	public void deletePermission(Integer  id,User user);
	/**
	 * 根据主件获得权限对象
	 * @param elcourse
	 */
	public Permission getPermission(Integer  id);
	
	public Permission getPermissionByOperateValue(String operateValue);
	/**
	 * 根据对象保存权限纪录
	 * @param elcourse
	 */
	public void savePermission(Permission permission,User user);
	/**
	 * 根据获得所有权限纪录
	 * @param elcourse
	 */
	public List findPermissions();
	/**
	 * 用recover开头，表示设置status字段为1
	 * @param elcourse
	 */
	public void recoverPermission(Integer  id,User user);


	/**
	 * add by hulei
	 * 查找不属于某个角色的所有权限
	 */
	public List findPermissionsNotBelongToRole(int roleId);
}
