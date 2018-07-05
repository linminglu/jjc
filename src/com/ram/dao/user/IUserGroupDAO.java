package com.ram.dao.user;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.framework.dao.IDAO;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.model.UserGroupRl;

/**
 * @author lixiaodong 
 */
public interface IUserGroupDAO extends IDAO{
	public int saveUserGroup(UserGroup userGroup, User user) throws DataAccessException;
	public boolean deleteUserGroup(int id, User user) throws DataAccessException;
	public List getAllUserGroup() throws DataAccessException;
	public UserGroup getUserGroup(int userGroupId) throws DataAccessException;
	public boolean modifyUserGroup(UserGroup userGroup, User user) throws DataAccessException;
	public boolean saveUserGroupRl(UserGroupRl userGroupRl, User user) throws DataAccessException;
	public List getUserGroupById(int userId) throws DataAccessException;
	public List getAvailableUserGroupById(int userId) throws DataAccessException;
	public void addUserAndGroupRl(int userId, int[] userGroupId, User user) throws DataAccessException;
	public void deleteUserAndGroupRl(int userId, int userGroupId, User user) throws DataAccessException;
	/**                                                          
	 * 判断用户是否属于助学中心的用户组
	 * @param userId
	 * @return boolean
	 */
	public boolean getUserGroupByUserId(Integer userId) throws DataAccessException;
	/**
	 * 查询用户组中的用户
     * @param userGroupId
	 * @throws DataAccessException
	 */
	public List findUserOfUserGroup(Integer userGroupId);
}
