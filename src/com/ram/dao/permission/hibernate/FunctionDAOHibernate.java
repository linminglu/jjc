package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.ram.dao.permission.IFunctionDAO;
import com.ram.model.Function;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * 
 * <p>
 * Title: 北外网院网络教学平台
 * </p>
 * <p>
 * Description: 平台功能Function的DAO类
 * 
 * </p>
 * <p>
 * Copyright：世纪盈华 2005
 * </p>
 * <p>
 * Company: 世纪盈华
 * </p>
 * 
 * @author 胡磊
 * @version 1.0
 */

public class FunctionDAOHibernate extends AbstractBaseDAOHibernate implements
		IFunctionDAO {

	private final static String FIND_ALL_FUNCTION = "from Function function  order function.orderSn desc, by function.functionId ";

	private final static String FIND_ALL_FUNCTION_SUM = "select count(*) from Function ";

	private final static String FIND_FUNCTION_BY_TITLE = "from Function function where function.functionTitle like %?% order by function.orderSn desc,function.functionId asc";

	private final static String FIND_FUNCTION_BY_TITLE_SUM = "select count(*) from Function function where   function.functionTitle like %?%  ";

	private final static String FIND_SUB_FUNCTION_BY_CUR_ID = "from Function function where function.parentId=?  order by function.orderSn desc, function.functionId asc";

	private final static String FIND_SUB_FUNCTION_BY_CUR_ID_SUM = "select count(*) from Function function where function.parentId=?  ";

	private final static String FIND_ALL_FUNCTION_PARENT_ID_IS_ZERO = "select distinct(function.functionId) from Function function where  function.parentId=0  order by function.orderSn desc,function.functionId asc";

	private final static String FIND_ALL_FUNCTION_PARENT_ID_IS_ZERO_By_UserGroup = "select distinct(function.functionId) from Function function where  function.parentId=0 and  function.functionId in (select gf.functionId from GroupFunctionRl gf where gf.groupId=?) order by function.orderSn desc,function.functionId asc";

	public FunctionDAOHibernate() {
		super();
	}

	/**
	 * 获得一个function
	 * 
	 * @param functionID
	 * @return
	 */
	public Function getFunction(Integer functionId) {
		return (Function) this.getObject(Function.class, functionId);
	}

	/**
	 * 保存function
	 * 
	 * @param function
	 */
	public void saveFunction(Function function, User user) {
		this.saveObject(function, user);
	}

	public void updateFunction(Function function, User user) {
		this.updateObject(function, user);
	}

	public void removeFunction(Integer functionId, User user) {
		this.deleteObject(this.getFunction(functionId), user);
	}

	public void deleteFunction(Integer functionId, User user) {
		this.deleteObject(this.getFunction(functionId), user);
	}

	// ----------------------------------------------------------
	/**
	 * 对title的like查询
	 * 
	 * @param functionTitle
	 * @return
	 */
	public List findFunctionByLikeTitle(String functionTitle, int firstResult) {
		Query query = getSession().createQuery(FIND_FUNCTION_BY_TITLE);
		query.setString(0, functionTitle);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(PaginationSupport.PAGESIZE);
		return query.list();

	}

	/**
	 * 对title的like查询的记录统计
	 * 
	 * 
	 * @param functionTitle
	 * @return
	 */
	public int findFunctionByLikeTitle_Num(String functionTitle) {
		Query query = getSession().createQuery(FIND_FUNCTION_BY_TITLE_SUM);
		query.setString(0, functionTitle);
		return ((Integer) query.list().get(0)).intValue();
	}

	// ----------------------------------------------------------
	/**
	 * 根据当前id，得到一组直接子功能
	 * 
	 * @param curFunctionID
	 * @return
	 */
	public List findSubFunctionByCurrentFunction(int curFunctionID,
			int firstResult) {

		Query query = getSession().createQuery(FIND_SUB_FUNCTION_BY_CUR_ID);
		query.setInteger(0, curFunctionID);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(PaginationSupport.PAGESIZE);
		return query.list();
	}

	/**
	 * 根据当前id，得到一组直接子功能的数据统计
	 * 
	 * 
	 * @param curFunctionID
	 * @return
	 */
	public int findSubFunctionByCurrentFunction_Num(int curFunctionID) {
		Query query = getSession().createQuery(FIND_SUB_FUNCTION_BY_CUR_ID_SUM);
		query.setInteger(0, curFunctionID);
		return ((Integer) query.list().get(0)).intValue();
	}

	// ----------------------------------------------------------
	/**
	 * 获取所有Function对象的数组
	 * 
	 * 
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List findAllFunctions(int firstResult) {
		Query query = getSession().createQuery(FIND_ALL_FUNCTION);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(PaginationSupport.PAGESIZE);
		return query.list();
	}

	/**
	 * 获取所有Function对象的数组的统计
	 * 
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public int findAllFunctions_Num() {
		Query query = getSession().createQuery(FIND_ALL_FUNCTION_SUM);
		return ((Integer) query.list().get(0)).intValue();
	}

	public List findAllFunctions() {
		Query query = getSession().createQuery(FIND_ALL_FUNCTION);
		return query.list();
	}

	public List findSubFunctions(int parentFunctionId) {
		Query query = getSession().createQuery(FIND_SUB_FUNCTION_BY_CUR_ID);
		query.setInteger(0, parentFunctionId);
		return query.list();
	}

	public List findAllParentIdIsZero() {
		Query query = getSession().createQuery(
				FIND_ALL_FUNCTION_PARENT_ID_IS_ZERO);
		List list = query.list();

		Integer IntegerFunctionId = null;
		int functionId = 0;
		for (int i = 0; i < list.size(); i++) {
			IntegerFunctionId = (Integer) list.get(i);
			Function function = this.getFunction(IntegerFunctionId);
		}
		return query.list();
	}

	public List findAllParentIdIsZeroByUserGroup(int userGroupId) {
		Query query = getSession().createQuery(
				FIND_ALL_FUNCTION_PARENT_ID_IS_ZERO_By_UserGroup);
		query.setInteger(0, userGroupId);
		List list = query.list();

		Integer IntegerFunctionId = null;
		int functionId = 0;
		for (int i = 0; i < list.size(); i++) {
			IntegerFunctionId = (Integer) list.get(i);
			Function function = this.getFunction(IntegerFunctionId);
		}
		return query.list();
	}

	/**
	 * 查找某个父功能下尚未分配给某个组的所有子功能
	 */
	private static String hqlFindAllSubFunctionsNotBelongGroup = "from Function f where f.parentId=? and f.functionId not in(select gf.functionId from GroupFunctionRl gf where gf.groupId=?) order by f.orderSn desc,f.functionId asc";

	public List findAllSubFunctionsNotBelongGroup(int parentFunctionId,
			int groupId) {

		Query query = getSession().createQuery(
				hqlFindAllSubFunctionsNotBelongGroup);
		query.setInteger(0, parentFunctionId);
		query.setInteger(1, groupId);
		List list = query.list();

		return list;

	}

	private static String hqlFindAllSubFunctionsBelongGroup = "from Function f where f.parentId=? and f.functionId in(select gf.functionId from GroupFunctionRl gf where gf.groupId=?) order by f.orderSn desc,f.functionId asc";

	public List findAllSubFunctionsBelongGroup(int parentFunctionId, int groupId) {

		Query query = getSession().createQuery(
				hqlFindAllSubFunctionsBelongGroup);
		query.setInteger(0, parentFunctionId);
		query.setInteger(1, groupId);
		List list = query.list();

		return list;

	}

	private static String hqlGetFunction = "from Function f where f.functionActionPath=? order by f.functionId";
	public Function getFunction(String functionActionPath) {
		Query q = getSession().createQuery(hqlGetFunction);
		q.setString(0, functionActionPath);
		Function f = null;
		if (q.list().size() > 0) {
			f = (Function) q.list().get(0);
		}
		return f;
	}

}
