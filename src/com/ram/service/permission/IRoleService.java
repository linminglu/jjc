package com.ram.service.permission;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ram.dao.permission.IRoleDAO;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.model.UserRoleRl;

public interface IRoleService {
	/**
	 * IOC方式注入权限DAO
	 * 
	 * @param RoleDAO
	 */
	public void setRoleDAO(IRoleDAO roleDAO);

	/**
	 * 创建角色
	 * 
	 * @param role
	 */
	public void createRole(Role role,User user);

	/**
	 * 根据主键删除角色
	 * 
	 * @param id
	 */
	public void deleteRole(Integer id,User user);


	/**
	 * 根据主键把角色设为无效
	 * 
	 * @param id
	 */
	public void removeRole(Integer id,User user);


	/**
	 * 根据主键把角色设为无效
	 * 
	 * @param id
	 */
	public void recoverRole(Integer id,User user);

	/**
	 * 修改角色
	 * 
	 * @param role
	 */
	public void modifyRole(Role role,User user);

	/**
	 * 根据主键获得角色
	 * 
	 * @param id
	 * @return
	 */
	public Role getRole(Integer id);

	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	public List findAllRoles();
	/**
	 * 查询所有有效的角色
	 * 
	 * @return
	 */
	public List findAllValidRoles();
	
	/**
	 * 建立角色和用户之间的关系
	 */
	public void saveUserRoleRl(UserRoleRl userRoleRl, User user);
	
	
	/**
	 * 根据用户ID获得用户所属的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findRoleById(int userId) throws DataAccessException;
	
	/**
	 * 根据用户ID获得用户可以添加的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAvailableRoleById(int userId) throws DataAccessException;
	
	/**
	 * 建立用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void saveUserAndRoleRl(int userId, int[] roleId, User user) throws DataAccessException;
	
	/**
	 * 删除用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndRoleRl(int userId, int roleId, User user) throws DataAccessException;


	public void createUserRoleRl(UserRoleRl userRoleRl);
}
