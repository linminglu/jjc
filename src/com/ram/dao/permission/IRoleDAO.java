package com.ram.dao.permission;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.IDAO;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.model.UserRoleRl;

/**
 * @author Administrator
 * 
 */
public interface IRoleDAO extends IDAO {
	/**
	 * 得到全部角色信息
	 * 
	 * @param order
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List findAllRole(int firstResult, int maxResults);

	/**
	 * 得到全部有效角色信息
	 * 
	 * @return
	 */
	public List findAllValidRole();
	/**
	 * 得到全部角色信息
	 * 
	 * @param order
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List findAllValidRole(int firstResult, int maxResults);
	/**
	 * 查找全部有效角色的数据量,为分页提供依据
	 * 
	 * @return
	 */
	public int findAllRoleNum();

	/**
	 * 保存角色信息
	 * 
	 * @param tutor
	 * @return
	 */
	public void saveRole(Role role,User user);

	/**
	 * 修改角色信息
	 * 
	 * @param role
	 * @return
	 */
	public void updateRole(Role role,User user);

	/**
	 * 通过角色信息主键进行查找
	 * 
	 * @param id
	 */
	public Role getRole(Integer id);

	/**
	 * 通过持久化对象找到全部列表
	 * 
	 * @return
	 */
	public List findRoles();

	/**
	 * 把status设为无效状态
	 * 
	 * @param role
	 */
	public void removeRole(Integer roleId,User user);

	/**
	 * 根据角色主键把status设为无效状态

	/**
	 * 根据角色主键删除角色信息
	 * 
	 * @param id
	 */
	public void deleteRole(Integer id,User user);
	/**
	 * 根据角色主键把status设为有效状态
	 * @param id
	 */
	public void recoverRole(Integer id,User user);

	/**
	 * 根据页面进行role的查询
	 * 
	 * @param id
	 */
	public List findRoleByCriteria(DetachedCriteria detachedCriteria,
			int pageSize, int startIndex);
	
	
	
	/**
	 * 建立用户和角色之间的关系
	 */
	public void saveUserRoleRl(UserRoleRl userRoleRl,User user);
	
	
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
	public void saveUserAndRoleRl(int userId, int[] roleId,User user) throws DataAccessException;
	
	/**
	 * 删除用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndRoleRl(int userId, int roleId,User user) throws DataAccessException;

	public List getRolesOfUser(int userId);
}