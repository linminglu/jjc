package com.ram.service.permission.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ram.dao.permission.IRoleDAO;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.model.UserRoleRl;
import com.ram.service.permission.IRoleService;

public class RoleServiceImpl implements IRoleService {

	private IRoleDAO roleDAO;

	public void setRoleDAO(IRoleDAO roleDAO) {
		// TODO Auto-generated method stub
		this.roleDAO = roleDAO;
	}

	public void createRole(Role role,User user) {
		// TODO Auto-generated method stub
		roleDAO.saveRole(role,user);
	}

	public void deleteRole(Integer id,User user) {
		// TODO Auto-generated method stub
		roleDAO.deleteRole(id,user);
	}

	public void removeRole(Integer id,User user) {
		// TODO Auto-generated method stub
		roleDAO.removeRole(id,user);
	}

	public void recoverRole(Integer id,User user) {
		// TODO Auto-generated method stub
		roleDAO.recoverRole(id,user);
	}

	public void modifyRole(Role role,User user) {
		// TODO Auto-generated method stub
		roleDAO.updateRole(role,user);
	}

	public Role getRole(Integer id) {
		// TODO Auto-generated method stub
		return roleDAO.getRole(id);
	}

	public List findAllRoles() {
		// TODO Auto-generated method stub
		return roleDAO.findObjects(Role.class);
	}

	public List findAllValidRoles() {
		return roleDAO.findAllValidRole();
	}
	
	/**
	 * 建立角色和用户之间的关系
	 */
	public void saveUserRoleRl(UserRoleRl userRoleRl, User user){
		roleDAO.saveUserRoleRl(userRoleRl, user);
	}
	
	/**
	 * 根据用户ID获得用户所属的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findRoleById(int userId) throws DataAccessException{
		return roleDAO.findRoleById(userId);
	}
	
	/**
	 * 根据用户ID获得用户可以添加的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAvailableRoleById(int userId) throws DataAccessException{
		return roleDAO.findAvailableRoleById(userId);
	}
	
	/**
	 * 建立用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void saveUserAndRoleRl(int userId, int[] roleId, User user) throws DataAccessException{
		roleDAO.saveUserAndRoleRl(userId, roleId, user);
	}
	
	/**
	 * 删除用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndRoleRl(int userId, int roleId, User user) throws DataAccessException{
		roleDAO.deleteUserAndRoleRl(userId, roleId, user);
	}

	public void createUserRoleRl(UserRoleRl userRoleRl) {
		
		// TODO Auto-generated method stub
		
	}
	
}
