package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.ram.dao.permission.IGroupFunctionRlDAO;
import com.ram.model.Function;
import com.ram.model.GroupFunctionRl;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

public class GroupFunctionRlDAOHibernate extends AbstractBaseDAOHibernate
		implements IGroupFunctionRlDAO {


	/**
	 * 查找某个组的某个功能下的子功能
	 */
	private final static String hqlFindSubFunctionsByGroup = 
		"from Function function where function.parentId=? and function.functionId in "+
		"(select groupFunctionRl.functionId from GroupFunctionRl groupFunctionRl "+
		"where groupFunctionRl.groupId=? ) order by function.orderSn desc,function.functionId asc";
	
	public List findSubFunctionsByGroup(Integer userGroupId, Integer parentFunctionId) {
		Query query = getSession().createQuery(hqlFindSubFunctionsByGroup);
		query.setInteger(0, parentFunctionId.intValue());
		query.setInteger(1, userGroupId.intValue());
		List list = query.list();

		for (int i = 0; i < list.size(); i++) {
			Function f = (Function) list.get(i);
		}
		return query.list();
	}

	/**
	 * 将组和功能的关系存入数据库
	 * 
	 * @param groupId
	 * @param functionId
	 */
	public void saveFunctionWithGroup(Integer userGroupId, Integer functionId,User user) {
		GroupFunctionRl groupFunctionRl = new GroupFunctionRl();
		groupFunctionRl.setFunctionId(functionId);
		groupFunctionRl.setGroupId(userGroupId);
		log.info("将组和功能的关系存入数据库");
		this.saveObject(groupFunctionRl,user);

	}

	/**
	 * 找到指定条件的对象
	 */
	private static String hqlGetGroupFunctionRl="from GroupFunctionRl gf where gf.groupId=? and gf.functionId=?";
	public GroupFunctionRl getGroupFunctionRl(Integer userGroupId, Integer functionId) {
		
		Query query=getSession().createQuery(hqlGetGroupFunctionRl);
		query.setInteger(0,userGroupId.intValue());
		query.setInteger(1,functionId.intValue());
		if(query.list().size()==0){
			return null;
			
		}else{
			return (GroupFunctionRl)query.list().get(0);
			
		}
	}
	
	
	private static String hqlHasRelationInDB="from GroupFunctionRl gf where gf.groupId=? and gf.functionId=?";
	/**
	 * 判断是否具有功能的访问权限
	 * @param userGroupId
	 * @param functionId
	 * @return
	 */
	public boolean hasRelationInDB(int userGroupId,int functionId){
		Query query=getSession().createQuery(hqlHasRelationInDB);
		query.setInteger(0,userGroupId);
		query.setInteger(1,functionId);
		if(query.list().size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	
	public void deleteGroupFunctionRl(Integer groupFunctionRlId, User user) {
		this.deleteObject(GroupFunctionRl.class,groupFunctionRlId,user);
		
	}



	public void removeGroupFunctionRl(Integer groupFunctionRlId, User user) {
		this.removeObject(GroupFunctionRl.class,groupFunctionRlId,user);
	}

	public void saveGroupFunctionRl(GroupFunctionRl groupFunctionRl, User user) {
		this.saveGroupFunctionRl(groupFunctionRl,user);
		
	}

	public void updateGroupFunctionRl(GroupFunctionRl groupFunctionRl, User user) {
		this.updateObject(groupFunctionRl,user);
	}

	/**
	 * 找到指定功能下的下级子功能
	 */
	public List findSubFunctonsByFunction(int functionId) {
		String hqlFindSubFunctonsByParentFunction="from Function f where f.parentId=? order by f.orderSn desc,f.functionId asc";
		Query q=this.getSession().createQuery(hqlFindSubFunctonsByParentFunction);
		q.setInteger(0,functionId);
		return q.list();
	}



	
}
