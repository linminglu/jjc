package com.ram.dao.permission.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.ram.dao.permission.IRoleDAO;
import com.ram.model.Role;
import com.ram.model.User;
import com.ram.model.UserRoleRl;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

public class RoleDAOHibernate extends AbstractBaseDAOHibernate implements
		IRoleDAO {

	private final String FIND_ALLROLES = "from Role order by roleId";
	private final String FIND_ALLVALIDROLES = "from Role role order by role.roleId";
	private final String FIND_ALLROLESNUM = "select count(role.roleId) from Role role  order by role.roleId";
	private final String GET_USER_ROLE_BY_USERID = "from Role as role where  role.roleId in (select userRoleRl.role.roleId from UserRoleRl as userRoleRl where userRoleRl.user.userId = ?) order by role.roleId";
	private final String GET_ADD_USER_ROLE_BY_USERID = "from Role as role where  role.roleId not in (select userRoleRl.role.roleId from UserRoleRl as userRoleRl where userRoleRl.user.userId = ?) order by role.roleId";
	private final String GET_USER_BY_ID = "from User user where user.userId = ? order by user.userId";
	private final String DEL_USER_ROLE_RL = "delete from UserRoleRl where userRoleRlId = (select userRoleRl.userRoleRlId from UserRoleRl as userRoleRl where userRoleRl.user.userId = ? and userRoleRl.role.roleId = ?)";

	public List findAllRole(int firstResult, int maxResults) {
		Query q = getSession().createQuery(FIND_ALLROLES);
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		List result = q.list();
		return result;
	}

	public int findAllRoleNum() {
		List l = getHibernateTemplate().find(FIND_ALLROLESNUM);
		if (l != null && !l.isEmpty()) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public List findAllValidRole() {
		Query q = getSession().createQuery(FIND_ALLVALIDROLES);
		List result = q.list();
		return result;
	}

	public List findAllValidRole(int firstResult, int maxResults) {
		Query q = getSession().createQuery(FIND_ALLROLES);
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		List result = q.list();
		return result;
	}

	public void saveRole(Role role,User user) {
		// TODO Auto-generated method stub
		//super.saveObjectStatus(role);
		saveObject(role,user);
	}

	public void updateRole(Role role,User user) {
		// TODO Auto-generated method stub
		saveObject(role,user);
	}

	public Role getRole(Integer id) {
		// TODO Auto-generated method stub
		
		Role role = (Role) getObject(Role.class, id);
		if (role == null) {
			log.warn("uh oh, Role '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Role.class, id);
		}

		return role;
	}

	public List findRoles() {
		// TODO Auto-generated method stub
		return findObjects(Role.class);
	}

	public void deleteRole(Role role,User user) {
		// TODO Auto-generated method stub
		this.deleteObject(Role.class, role,user);
	}

	public void deleteRole(Integer id,User user) {
		// TODO Auto-generated method stub
		this.deleteObject(Role.class, id,user);
	}

	public List findRoleByCriteria(DetachedCriteria detachedCriteria,
			int pageSize, int startIndex) {
		// TODO Auto-generated method stub
		this.findPageByCriteria(detachedCriteria, pageSize, startIndex);
		return null;
	}

	public void removeRole(Role role,User user) {
		// TODO Auto-generated method stub
		//super.removeObjectStatus(role);
		this.removeObject(role,user);

	}

	public void removeRole(Integer id,User user) {
		// TODO Auto-generated method stub
		Role role = this.getRole(id);
		this.removeRole(role,user);
	}

	public void recoverRole(Integer id,User user) {
		// TODO Auto-generated method stub
		Role role = this.getRole(id);
		this.recoverRole(role,user);
	}

	public void recoverRole(Role role,User user) {
		// TODO Auto-generated method stub
		//super.saveObjectStatus(role);
		recoverObject(role,user);
	}
	
	/**
	 * 建立用户和角色之间的关系
	 */
	public void saveUserRoleRl(UserRoleRl userRoleRl, User user){
		saveObject(userRoleRl,user);
	}
	
	/**
	 * 获得一个用户
	 */
	public User getUser(int id) throws DataAccessException {
		// TODO Auto-generated method stub
		Query query = getSession().createQuery(GET_USER_BY_ID);
		query.setInteger(0, id);
		List userList = query.list();
		Iterator itr = userList.iterator();
		User user = null;
		if(itr.hasNext()){
			user = (User)itr.next();
		}
		return user;
	}
	
	/**
	 * 根据用户ID获得用户所属的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findRoleById(int userId) throws DataAccessException{
		Query query = getSession().createQuery(GET_USER_ROLE_BY_USERID);
		query.setInteger(0, userId);
		List userRoleList = query.list();
		return userRoleList;
	}
	
	/**
	 * 根据用户ID获得用户可以添加的角色
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAvailableRoleById(int userId) throws DataAccessException{
		Query query = getSession().createQuery(GET_ADD_USER_ROLE_BY_USERID);
		query.setInteger(0, userId);
		List userRoleList = query.list();
		return userRoleList;
	}
	
	/**
	 * 建立用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void saveUserAndRoleRl(int userId, int[] roleId, User operaUser) throws DataAccessException{
		User user = getUser(userId);
		for(int i = 0;i < roleId.length;i++){
			UserRoleRl userRoleRl = new UserRoleRl();
			userRoleRl.setUser(user);
			Role role = (Role)getRole(new Integer(roleId[i]));
			userRoleRl.setRole(role);
			getHibernateTemplate().save(userRoleRl);
		}
	}
	
	/**
	 * 删除用户和角色之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndRoleRl(int userId, int roleId, User user) throws DataAccessException{
		Query query = getSession().createQuery(DEL_USER_ROLE_RL);
		query.setInteger(0, userId);
		query.setInteger(1, roleId);
		query.executeUpdate();
	}

	/**
	 * 返回一个用户所属的角色列表
	 */
	public List getRolesOfUser(int userId) {
		String hqlGetRolesOfUser="select ur.role.roleId from UserRoleRl ur where ur.user.userId=?";
		Query query=getSession().createQuery(hqlGetRolesOfUser);
		query.setInteger(0,userId);
		return query.list();
	}
	
}
