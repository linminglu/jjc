package com.ram.dao.user.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.ram.dao.user.IUserGroupDAO;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.model.UserGroupRl;

/**
 * @author lixiaodong 
 * 
 */
public class UserGroupDAOHibernate extends AbstractBaseDAOHibernate implements
		IUserGroupDAO {

	private final String GET_ALL_USERGROUP = "from UserGroup ug order by ug.userGroupId";

	private final String GET_USERGROUP_BY_ID = "from UserGroup where userGroupId = ?";

	private final String GET_USERGROUP_BY_USERID = "from UserGroup as userGroup where userGroup.userGroupId in (select userGroupRl.userGroup.userGroupId from UserGroupRl as userGroupRl where userGroupRl.user.userId = ?) order by userGroup.userGroupId";

	private final String GET_ADD_USERGROUP_BY_USERID = "from UserGroup as userGroup where  userGroup.userGroupId not in (select userGroupRl.userGroup.userGroupId from UserGroupRl as userGroupRl where userGroupRl.user.userId = ?)";

	private final String GET_USER_BY_ID = "from User user where user.userId = ?";

	private final String DEL_USERGROUP_RL = "delete UserGroupRl where userGroupRlId in (select userGroupRl.userGroupRlId from UserGroupRl as userGroupRl where userGroupRl.user.userId = ? and userGroupRl.userGroup.userGroupId = ?)";

	private final String FIND_USERGORUPRL_BY_USERGROUPID="select distinct(user) from User user,UserGroupRl userGroupRl,UserGroup userGroup ,Manager m where userGroupRl.user.userId=user.userId and userGroupRl.userGroup.userGroupId=userGroup.userGroupId and m.user.userId=user.userId and m.tcId=? and userGroup.userGroupId in(:userGroupList) ";
	
	private final String FIND_USERGORUPRL_BY_USERID="select count(*) from UserGroupRl userGroupRl,UserGroup userGroup where  userGroupRl.userGroup.userGroupId=userGroup.userGroupId and  userGroup.userGroupId =27 and  userGroupRl.user.userId=?";
	
	/**                                                          
	 * 判断用户是否属于助学中心的用户组
	 * @param userId
	 * @return boolean
	 */
	public boolean getUserGroupByUserId(Integer userId) throws DataAccessException{
		boolean flag = false;
		int arNum = 0;
		Query query = getSession().createQuery(FIND_USERGORUPRL_BY_USERID);
	    query.setInteger(0,userId.intValue());
		List list = query.list();
		if(list != null && !list.isEmpty()){
			arNum = ((Integer)list.get(0)).intValue();
		}
		if(arNum != 0){
			flag = true;
		}
		
		return flag;
	}
	/**                                                          
	 * 创建或者修改一个用户组
	 * 
	 * @see com.ram.dao.user.IUserGroupDAO#createUserGroup(com.ram.model.UserGroup)
	 */
	public int saveUserGroup(UserGroup userGroup, User user)
			throws DataAccessException {
		saveObject(userGroup, user);
		return userGroup.getUserGroupId().intValue();
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
		if (itr.hasNext()) {
			user = (User) itr.next();
		}
		return user;
	}

	/**
	 * 根据用户组ID删除一个用户组
	 * 
	 * @see com.ram.dao.user.IUserGroupDAO#delUserGroup(int)
	 */
	public boolean deleteUserGroup(int id, User user)
			throws DataAccessException {
		this.deleteObject(this.getObject(UserGroup.class, new Integer(id)),
				user);
		return true;
	}

	/**
	 * 获得所有用户组信息
	 * 
	 * @see com.ram.dao.user.IUserGroupDAO#loadAllUserGroup()
	 */
	public List getAllUserGroup() throws DataAccessException {
		Query query = getSession().createQuery(GET_ALL_USERGROUP);
		List result = query.list();
		return result;
	}

	/**
	 * 根据用户组ID获得用户组的信息
	 * 
	 * @see com.ram.dao.user.IUserGroupDAO#loadUserGroup(int)
	 */
	public UserGroup getUserGroup(int userGroupId) throws DataAccessException {
		Query query = getSession().createQuery(GET_USERGROUP_BY_ID);
		query.setInteger(0, userGroupId);
		List result = query.list();
		Iterator item = result.iterator();
		return (UserGroup) item.next();
	}

	/**
	 * 修改用户组信息
	 * 
	 * @see com.ram.dao.user.IUserGroupDAO#modifyUserGroup(com.ram.model.UserGroup)
	 */
	public boolean modifyUserGroup(UserGroup userGroup, User user)
			throws DataAccessException {
		saveObject(userGroup, user);
		return true;
	}

	/**
	 * 建立用户和用户组之间的关系
	 */
	public boolean saveUserGroupRl(UserGroupRl userGroupRl, User user)
			throws DataAccessException {
		saveObject(userGroupRl, user);
		return true;
	}

	/**
	 * 获得一个用户所属的用户组
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getUserGroupById(int userId) throws DataAccessException {
		try{
			log.info("在UserGroupDAOHibernate.java中执行SQL");
			log.info("从 getHibernateTemplate的获取session");
			Session session=this.getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createQuery(GET_USERGROUP_BY_USERID);
			query.setInteger(0, userId);
			List userGroupList = query.list();
			session.close();
			log.info("关闭Hibernate 的session");
			return userGroupList;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得一个用户可以进行添加的用户组
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getAvailableUserGroupById(int userId)
			throws DataAccessException {
		Query query = getSession().createQuery(GET_ADD_USERGROUP_BY_USERID);
		query.setInteger(0, userId);
		List userGroupList = query.list();
		return userGroupList;
	}

	/**
	 * 建立用户和用户组之间的关系
	 * 
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void addUserAndGroupRl(int userId, int[] userGroupId, User operaUser)
			throws DataAccessException {
		User user = getUser(userId);
		for (int i = 0; i < userGroupId.length; i++) {
			UserGroupRl userGroupRl = new UserGroupRl();
			userGroupRl.setUser(user);
			UserGroup userGroup = (UserGroup) getUserGroup(userGroupId[i]);
			userGroupRl.setUserGroup(userGroup);
			getHibernateTemplate().save(userGroupRl);
		}
	}

	/**
	 * 删除用户和用户组之间的关系
	 * 
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndGroupRl(int userId, int userGroupId, User user)
			throws DataAccessException {
		Query query = getSession().createQuery(DEL_USERGROUP_RL);
		query.setInteger(0, userId);
		query.setInteger(1, userGroupId);
		query.executeUpdate();
	}
	/**
	 * 查询用户组中的用户
     * @param userGroupId
	 * @throws DataAccessException
	 */
	public List findUserOfUserGroup(Integer tcId){
		Query query=getSession().createQuery(FIND_USERGORUPRL_BY_USERGROUPID);
		query.setInteger(0,tcId.intValue());
		query.setParameterList("userGroupList",new Integer[]{new Integer(32),new Integer(35),new Integer(37)});
		return query.list();
		
	}
}
