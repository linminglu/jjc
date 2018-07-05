/*
 * Created on 2005-7-25
 * 包括了对分页的支持
 * 如果采用hibernate2.0式的分页可以使用query对象进行，sqlserver数据库需要对SQLServerdialog方言进行改写
 * 采用hibernate3.0新的DetachedCriteria进行分页请继承本类
 * 特别是一些直接需要在web层次构建的查询推荐使用本类
 */
package com.framework.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ram.model.User;
import com.framework.dao.IDAO;
import com.framework.util.HQUtils;
import com.framework.util.HQuery;
import com.framework.util.Paras;

/**
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractBaseDAOHibernate extends HibernateDaoSupport
		implements IDAO {
	// 设置保存标志
	protected static String DELETED_STATUS = "0";//0表示删除状态
	
	protected static String VALID_STATUS = "1";//1表示正常状态

	protected static String IN_VALID_STATUS="2";//2表示无效状态
	
	protected final Log log = LogFactory.getLog(getClass());

	public void setCacheQueries(boolean cacheQueries) {
	}

	public void setQueryCacheRegion(String queryCacheRegion) {
	}


	private void persist(final Object entity) {
		getHibernateTemplate().save(entity);
	}
	private final void save(final Object entity) {
		 getHibernateTemplate().saveOrUpdate(entity);
	    //getHibernateTemplate().merge(entity);
	}
	
	//////////////////////////////////////////////////////////////////
	public Object getObject(final Class entity, final Serializable id) {
	    Object o = getHibernateTemplate().get(entity, id);
        if (o == null) {
        	log.error("调用AbstractBaseDAOHibernate的getObject对象的时候出错");
        	log.error("对象"+entity.getName() +",ID=" + id+"的实体在数据库中未找到");
        	return null;
        }else{
        	return o;
        }
	}
	
	public Object loadObject(final Class entity, final Serializable id) {
		Object o=getHibernateTemplate().load(entity, id);
		if(o==null){
			log.error("调用AbstractBaseDAOHibernate的loadObject对象的时候出错");
			log.error("对象"+entity.getName() +",ID=" + id+"的实体在数据库中未找到");
			return null;
		}else{
			return o;
		}
	}
	



	/**
	 * 保存对象的时候，调用saveObject方法
	 * 把status设成1
	 */
	public void saveObject(Object entity, User user) {
		//initObjectLastupdateAndupdateuser(entity, user);
		//initObjectStatus(entity);
		save(entity);
	}
	
	public void saveObject(Object entity) {
		save(entity);
	}
	/**
	 * 保存并返回数据库中的数据
	 * 
	 * @param object
	 * @return
	 */
	public Object saveObjectDB(Object object) {
		Serializable serializable = getHibernateTemplate().save(object);
		return getHibernateTemplate().get(object.getClass(), serializable);
	}
	
	public Object saveObjectAndReturn(Object entity,User user){
		getHibernateTemplate().save(entity);
		return entity;
	}
	/**
	 * 更新修改对象的时候，调用updateObject方法
	 */
	public void updateObject(Object entity, User user) {
		//initObjectLastupdateAndupdateuser(entity, user);
		save(entity);
	}	

	public void update(Object entity) {
		getHibernateTemplate().update(entity);
	}	

	/**
	 * removeObject：假删除		
	 * removeObject:将对象设置为删除状态0
	 * 对象状态分三种：
	 * 删除状态：status=0
	 * 有效（正常）状态：status=1
	 * 无效状态：status=2
	 * @param object
	 * @param user
	 */	
	public void removeObject(final Class clazz, final Serializable id,
			User user) {
		if(user !=null){
		log.info("[假删除操作，设置对象为删除状态]用户：" + user.getLoginName() + "设置了一个数据为删除状态：class=" + clazz.getName() + ",id=" + id);
		}
		Object object = this.getObject(clazz, id);
		//initObjectLastupdateAndupdateuser(object, user);
		removeObjectStatus(object);
		save(object);
		
	}
	/**
	 * removeObject：假删除		
	 * removeObject:将对象设置为删除状态0
	 * 对象状态分三种：
	 * 删除状态：status=0
	 * 有效（正常）状态：status=1
	 * 无效状态：status=2
	 * @param object
	 * @param user
	 */	
	public void removeObject(Object object,User user) {
		if(user !=null){
		log.info("[假删除操作，设置对象为删除状态]用户：" + user.getLoginName() + "设置了一个数据为删除状态：class=" + object.getClass().getName() + ",object=" + object.toString());
		}
		//initObjectLastupdateAndupdateuser(object, user);
		removeObjectStatus(object);
		save(object);
	}		
	
	/**
	 * recoverObject:将对象恢复为有效状态1
	 * 对象状态分三种：
	 * 删除状态：status=0
	 * 有效（正常）状态：status=1
	 * 无效状态：status=2
	 * @param object
	 * @param user
	 */	
	public void recoverObject(Object object,User user){
		if(user !=null){
		log.info("[恢复对象为有效状态操作]用户：" + user.getLoginName() + "恢复了一个数据为有效状态：class=" + object.getClass().getName() + ",object=" + object.toString());
		}
		//initObjectLastupdateAndupdateuser(object, user);
		this.recoverObjectStatus(object);
		save(object);		
	}
	
	/**
	 * disableOjbect:将对象设置为无效状态2
	 * 对象状态分三种：
	 * 删除状态：status=0
	 * 有效（正常）状态：status=1
	 * 无效状态：status=2
	 * @param object
	 * @param user
	 */
	public void disableObject(Object object,User user){
		if(user !=null){
		log.info("[设置对象为无效状态操作]用户：" + user.getLoginName() + "恢复了一个数据为有效状态：class=" + object.getClass().getName() + ",object=" + object.toString());
		}
		//initObjectLastupdateAndupdateuser(object, user);
		this.disableObjectStatus(object);
		save(object);		
	}	
	/**
	 * deleteObject:真删除
	 * @param clazz
	 * @param id
	 */
	public void deleteObject(final Class clazz, Serializable id,User user) {
		if(user !=null){
			log.info("[删除操作]用户：" + user.getLoginName() + "删除了一个数据：class=" + clazz.getName() + ",id=" + id);
		}
		getHibernateTemplate().delete(getObject(clazz, id));
	}	

	public void deleteObject(Object object, User user) {
		if(user !=null){
		log.info("[删除操作]用户：" + user.getLoginName() + "删除了一个数据：class=" + object.getClass().getName() + ",object=" + object.toString());
		}
		getHibernateTemplate().delete(object);
		
	}	

	public List findObjects(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	public List findObjects(Class clazz,Integer id) {
	    return getSession().createCriteria(clazz).addOrder(Order.desc("id")).list();	
	}

	public List findAll(final Class entity) {
		return getHibernateTemplate().find("from " + entity.getName());
	}

	public List findByNamedQuery(final String namedQuery) {
		return getHibernateTemplate().findByNamedQuery(namedQuery);
	}

	public List findByNamedQuery(final String query, final Object parameter) {
		return getHibernateTemplate().findByNamedQuery(query, parameter);
	}

	public List findByNamedQuery(final String query, final Object[] parameters) {
		return getHibernateTemplate().findByNamedQuery(query, parameters);
	}

	public List find(final String query) {
		return getHibernateTemplate().find(query);
	}

	public List find(final String query, final Object parameter) {
		return getHibernateTemplate().find(query, parameter);
	}

	public List find(final String query, final Object[] parameters) {
		return getHibernateTemplate().find(query, parameters);
	}

	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria) {
		return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE,
				0);
	}

	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex) {
		return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE,
				startIndex);
	}

	public PaginationSupport findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int startIndex) {
		return (PaginationSupport) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);						
						int totalCount = 0;
						if(criteria.setProjection(Projections.rowCount()).uniqueResult()!=null)
							totalCount = ((Integer) criteria.setProjection(
								Projections.rowCount()).uniqueResult())
								.intValue();						
						criteria.setProjection(null);
						List items = criteria.setFirstResult(startIndex)
								.setMaxResults(pageSize).list();
						log.info("the items is:::"+items.size());
						log.info("the countis kkkkkkkkkkkk:"+totalCount);
						PaginationSupport ps = new PaginationSupport(items,
								totalCount, pageSize, startIndex);						
						return ps;
					}
				}
				, true);
	}

	public List findAllByCriteria(final DetachedCriteria detachedCriteria) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = detachedCriteria
						.getExecutableCriteria(session);
				return criteria.list();
			}
		}

		, true);
	}

	public int getCountByCriteria(final DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.setProjection(Projections.rowCount())
								.uniqueResult();
					}
				}

				, true);
		return count.intValue();
	}



	/**
	 * 自动初始化上传日期和时间
	 * 
	 * @param object
	 * @param user
	 */
	private void initObjectLastupdateAndupdateuser(Object object, User user) {
		try {
			Class c = Class.forName(object.getClass().getName());
			Method getStatusMethod = c.getMethod("getUpdateDateTime", null);
			Date lastupdate = (Date) getStatusMethod.invoke(object, null);
			if (lastupdate == null) {
				Method setLastupdateMethod = c.getMethod("setUpdateDateTime",
						new Class[] { Date.class });
				setLastupdateMethod.invoke(object, new Object[] { new Date() });
				log.warn("本对象:" + object.getClass().getName()
						+ "最后更新时间:UpdateDateTime未初始化,系统自动帮助您初始化对象日期");
			}
		} catch (ClassNotFoundException e) {
			log.fatal("您传入的对象类:" + object.getClass().getName() + "不存在！");
		} catch (Exception e) {
			log.warn("请初始化本对象:" + object.getClass().getName()
					+ "最后更新时间:UpdateDateTime");
		}

		try {
			Class c = Class.forName(object.getClass().getName());
			Method getStatusMethod = c.getMethod("getUpdateUserID", null);
			Long updateuserid = (Long) getStatusMethod.invoke(object, null);
			if (updateuserid == null) {
				Method updateuserMethod = c.getMethod("setUpdateUserID",
						new Class[] { Long.class });
				updateuserMethod.invoke(object,
						new Object[] { user.getUserId() });
			}
			log.warn("本对象:" + object.getClass().getName()
					+ "的UpdateUserID未初始化,系统自动尝试帮助您初始化对象日期");
		} catch (ClassNotFoundException e) {
			log.error("您传入的对象类:" + object.getClass().getName() + "不存在！");
		} catch (Exception e) {
			log.warn("请初始化本对象:" + object.getClass().getName()
					+ "最后上传用户ID:UpdateUserID");
		}
	}

	// 保存状态自动初始化status数据
	private void initObjectStatus(Object object) {
		try {
			Class c = Class.forName(object.getClass().getName());
			Method getStatusMethod = c.getMethod("getStatus", null);
			String status = (String) getStatusMethod.invoke(object, null);
			if (status == null) {
				Method setStatusMethod = c.getMethod("setStatus",
						new Class[] { String.class });
				setStatusMethod.invoke(object, new Object[] { VALID_STATUS });
			}
		} catch (ClassNotFoundException e) {
			log.error("保存对象的时候设置status为有效状态的时候出错！传入的对象类:" + object.getClass().getName() + "不存在！");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	// 设置对象的status状态为删除状态
	private void removeObjectStatus(Object object) {
		try {
			Class c = Class.forName(object.getClass().getName());
			Method setStatusMethod = c.getMethod("setStatus",
					new Class[] { String.class });
			setStatusMethod.invoke(object, new Object[] { DELETED_STATUS });
		} catch (ClassNotFoundException e) {
			log.error("设置对象的Status为删除状态的时候出现错误！传入的对象类:" + object.getClass().getName() + "不存在！");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	// 恢复对象的status为有效状态
	private void recoverObjectStatus(Object object) {
		try {
			Class c = Class.forName(object.getClass().getName());
			Method setStatusMethod = c.getMethod("setStatus",
					new Class[] { String.class });
			setStatusMethod.invoke(object, new Object[] { VALID_STATUS });
		} catch (ClassNotFoundException e) {
			log.error("恢复对象的status为有效状态的时候出现错误！传入的对象类:" + object.getClass().getName() + "不存在！");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}	
	// 设置对象的status为无效状态
	private void disableObjectStatus(Object object) {
		try {
			Class c = Class.forName(object.getClass().getName());
			Method setStatusMethod = c.getMethod("setStatus",
					new Class[] { String.class });
			setStatusMethod.invoke(object, new Object[] { IN_VALID_STATUS });
		} catch (ClassNotFoundException e) {
			log.error("恢复对象的status为无效状态的时候出现错误！传入的对象类:" + object.getClass().getName() + "不存在！");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 复杂查询用	 * @param _query
	 * @return
	 */
	public List getQueryResult(HQuery _query){
		List itr = null;
		try{
			StringBuffer query_str = new StringBuffer(_query.getQueryString());
			 
			//是否要排序
			if (_query.getOrderby() != null) {
				query_str.append(_query.getOrderby());
			}
			//是否要分组
			if (_query.getGroupby() != null) {
				query_str.append(_query.getGroupby());
			}
			Session session = getSession();
			Query query = session.createQuery(query_str.toString());
				if (_query.getParalist() != null) {
					List list = _query.getParalist();
					for (int i = 0; i < list.size(); i++) {
						Paras param = (Paras) list.get(i);
						switch (param.getTypeNo()) {
							
							//此处要根据参数类型的增加要增加相应的“case”							case Types.VARCHAR:
								query.setString(i, param.getObj().toString());
								break;
							case Types.INTEGER:
								query.setInteger(i, ((Integer) param.getObj()).intValue());
								break;
							case Types.DATE:
								query.setDate(i, (java.sql.Date) param.getObj());
								break;
							case Types.DOUBLE:
								query.setDouble(i, ((Double) param.getObj()).doubleValue());
								break;
							case Types.BOOLEAN:
								query.setBoolean(i, ((Boolean) param.getObj()).booleanValue());
								break;
							case Types.CHAR:
								query.setCharacter(i, ((Character) param.getObj()).charValue());
								break;
							case Types.ARRAY:
								query.setParameterList(param.getName(), (ArrayList)param.getObj());
								break;
						 }
					 }
				}
				return query.list();
		 	}
		 	catch(Exception e){
		 		log.error("========参数类型有错误=========");
		 	}
		 	return new ArrayList();
	 }
	
	/**
	 * 复杂查询用,查询结果集中记录的个数
	 * @param _query
	 * @return
	 */
	private int getQueryResultSize(HQuery _query){
		List itr = null;
		try{
			StringBuffer query_str = new StringBuffer(_query.getQueryString());
			 
			//是否要排序

			if (_query.getOrderby() != null) {
				query_str.append(_query.getOrderby());
			}
			//是否要分组

			if (_query.getGroupby() != null) {
				query_str.append(_query.getGroupby());
			}
			Session session = getSession();
			Query query = session.createQuery(query_str.toString());
				if (_query.getParalist() != null) {
					List list = _query.getParalist();
					for (int i = 0; i < list.size(); i++) {
						Paras param = (Paras) list.get(i);
						switch (param.getTypeNo()) {
							
							//此处要根据参数类型的增加要增加相应的“case”
							case Types.VARCHAR:
								query.setString(i, param.getObj().toString());
								break;
							case Types.INTEGER:
								query.setInteger(i, ((Integer) param.getObj()).intValue());
								break;
							case Types.DATE:
								query.setDate(i, (java.sql.Date) param.getObj());
								break;
							case Types.DOUBLE:
								query.setDouble(i, ((Double) param.getObj()).doubleValue());
								break;
							case Types.BOOLEAN:
								query.setBoolean(i, ((Boolean) param.getObj()).booleanValue());
								break;
							case Types.CHAR:
								query.setCharacter(i, ((Character) param.getObj()).charValue());
								break;
							case Types.ARRAY:
								query.setParameterList(param.getName(), (ArrayList)param.getObj());
								break;
						 }
					 }
				}
				return query.list().size();
		 	}
		 	catch(Exception e){
		 		log.error("========参数类型有错误=========");
		 	}
		 	return 0;
	 }
	
	/**
	 * 复杂查询用
	 * @param _query
	 * @return
	 */
	public PaginationSupport getQueryResult(HQuery _query, int pageSize, int startIndex){
		List itr = null;
		try{
			StringBuffer query_str = new StringBuffer(_query.getQueryString());
			 
			//是否要排序

			if (_query.getOrderby() != null) {
				query_str.append(_query.getOrderby());
			}
			//是否要分组

			if (_query.getGroupby() != null) {
				query_str.append(_query.getGroupby());
			}
			Session session = getSession();
			Query query = session.createQuery(query_str.toString());
			 query.setFirstResult(startIndex);
			   query.setMaxResults(pageSize);
				if (_query.getParalist() != null) {
					List list = _query.getParalist();
					for (int i = 0; i < list.size(); i++) {
						Paras param = (Paras) list.get(i);
						switch (param.getTypeNo()) {
							
							//此处要根据参数类型的增加要增加相应的“case”
							case Types.VARCHAR:
								query.setString(i, param.getObj().toString());
								break;
							case Types.INTEGER:
								query.setInteger(i, ((Integer) param.getObj()).intValue());
								break;
							case Types.DATE:
								query.setDate(i, (java.sql.Date) param.getObj());
								break;
							case Types.DOUBLE:
								query.setDouble(i, ((Double) param.getObj()).doubleValue());
								break;
							case Types.BOOLEAN:
								query.setBoolean(i, ((Boolean) param.getObj()).booleanValue());
								break;
							case Types.CHAR:
								query.setCharacter(i, ((Character) param.getObj()).charValue());
								break;
							case Types.ARRAY:
								query.setParameterList(param.getName(), (ArrayList)param.getObj());
								break;
						 }
					 }
				}
				return new PaginationSupport(query.list(), getQueryResultSize(_query), pageSize, startIndex);
		 	}
		 	catch(Exception e){
		 		log.error("========参数类型有错误=========");
		 	}
		 	return null;
	 }
	
	public PaginationSupport findObjectPage(String select,String hsql,String orderby,List<Object> pars,int startIndex,int pageSize){
		return new PaginationSupport(this.getParsQuerySQL(select+hsql+orderby,pars).setFirstResult(startIndex)
				.setMaxResults(pageSize).list(),
				(Integer)this.getParsQuerySQL("select count(*) "+hsql,pars).uniqueResult());
	}
	
	public List findObjectList(String select,String hsql,String orderby,List<Object> pars){
		return this.getParsQuerySQL(select+hsql+orderby,pars).list();
	}
	
	public Query getParsQuerySQL(String hsql, List pars) {
		Query q = getSession().createQuery(hsql);
		Iterator it = pars.iterator();
		Object obj;
		int i = 0;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setTimestamp(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof BigDecimal) {
				q.setDouble(i, ((BigDecimal) obj).doubleValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			} else if (obj instanceof String[]) {
				q.setParameterList("ids", (String[]) obj);
			}
			i++;
		}
		return q;
	}
	
	public PaginationSupport findObjectPage(HQUtils hq){
		return new PaginationSupport(this.getParsQuerySQL(hq.getQueryString(),hq.getPars()).setFirstResult(hq.getStartIndex())
				.setMaxResults(hq.getPageSize()).list(),
				(Integer)this.getParsQuerySQL(hq.getQueryCount(),hq.getPars()).uniqueResult());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findObjects(HQUtils hq){
		return this.getParsQuerySQL(hq.getQueryString(), hq.getPars()).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findObjects(HQUtils hq,int max){
		return this.getParsQuerySQL(hq.getQueryString(), hq.getPars()).setMaxResults(max).list();
	}
	
	@SuppressWarnings("unchecked")
	public Object getObject(HQUtils hq){
		 List<Object> list = this.getParsQuerySQL(hq.getQueryString(), hq.getPars()).list();
		 if(list!=null && list.size()>0){
			 return list.get(0);
		 }
		 return null;
	 }
	
	 public Integer countObjects(HQUtils hq){
		 return (Integer)this.getParsQuerySQL(hq.getQueryCount(), hq.getPars()).uniqueResult();
	 }
	
	public Integer countObjects(String hql){
		return (Integer)getSession().createQuery(hql).uniqueResult();
	}
	
	public void updateObjectList(List saveList,List delList){
		if(saveList != null && saveList.size()>0)
			getHibernateTemplate().saveOrUpdateAll(saveList);
		if(delList != null && delList.size()>0)
			getHibernateTemplate().deleteAll(delList);
	}
	
//	/**
//	 * delete：真删除
//	 * @param entity
//	 */
//	public void deleteObject(final Class clazz, Serializable id) {
//		//log.info("[删除操作]删除数据的时候没有传入用户对象，未知的用户删除了一个数据：class=" + clazz.getName() + ",id=" + id);
//		getHibernateTemplate().delete(getObject(clazz,id));
//	}


	
//	/**
//	 * removeObject：假删除		
//	 * 本系统内删除并非真正删除而是将表中status字段置为-1
//	 */
//	public void removeObject(final Class clazz, final Serializable id) {
//		//log.info("[假删除操作]假删除操作的时候没有传入用户对象，未知的用户设置了一个数据为删除状态：class=" + clazz.getName() + ",id=" + id);		
//		Object object = this.get(clazz, id);
//		removeObjectStatus(object);
//		save(object);
//	}		
	/**
	 * 与数据库数据的同步
	 * 
	 */
	public void flush(){
		getHibernateTemplate().flush();
	}
	/**
	 * 清空内部缓存
	 * 
	 */
	public void clear(){
		getHibernateTemplate().clear();
	}
	public void saveObjectBatch(Collection entity,User user){
		getHibernateTemplate().saveOrUpdateAll(entity);
	}
	public PaginationSupport findQueryResult(HQuery _query, int pageSize, int startIndex){
		try{
			Query query = combinParams(_query, pageSize, startIndex);
				return new PaginationSupport(query.list(), getQueryResultSizeNew(_query), pageSize, startIndex);
		 	}	catch(Exception e){
		 		log.error("========参数类型有错误========="+e.getMessage());
		 	}
		 	return null;
	 }
	private Query combinParams(HQuery _query, int pageSize, int startIndex) {
		StringBuffer query_str = new StringBuffer(_query.getQueryString());
		//是否要排序
		if (_query.getOrderby() != null) {
			query_str.append(_query.getOrderby());
		}
		//是否要分组
		if (_query.getGroupby() != null) {
			query_str.append(_query.getGroupby());
		}
		Session session = getSession();
		Query query = session.createQuery(query_str.toString());
		 query.setFirstResult(startIndex);
		   query.setMaxResults(pageSize);
			if (_query.getParalist() != null) {
				List list = _query.getParalist();
				for (int i = 0; i < list.size(); i++) {
					Paras param = (Paras) list.get(i);
					switch (param.getTypeNo()) {
						//此处要根据参数类型的增加要增加相应的“case”
						case Types.VARCHAR:
							query.setString(i, param.getObj().toString());
							break;
						case Types.INTEGER:
							query.setInteger(i, ((Integer) param.getObj()).intValue());
							break;
						case Types.DATE:
							query.setDate(i, (java.sql.Date) param.getObj());
							break;
						case Types.DOUBLE:
							query.setDouble(i, ((Double) param.getObj()).doubleValue());
							break;
						case Types.BOOLEAN:
							query.setBoolean(i, ((Boolean) param.getObj()).booleanValue());
							break;
						case Types.CHAR:
							query.setCharacter(i, ((Character) param.getObj()).charValue());
							break;
						case Types.ARRAY:
							query.setParameterList(param.getName(), (ArrayList)param.getObj());
							break;
						case Types.TIME:
							query.setParameter(i,(Date)param.getObj());
							break;
					 }
				 }
			}
		return query;
	}
	private int getQueryResultSizeNew(HQuery _query){
		List itr = null;
		try{
			StringBuffer query_str = new StringBuffer(_query.getQueryStringCount());
			System.out.println("query_str:"+query_str.toString());
			 
			//是否要排序
			if (_query.getOrderby() != null) {
				query_str.append(_query.getOrderby());
			}
			
			//是否要分组
			if (_query.getGroupby() != null) {
				query_str.append(_query.getGroupby());
			}
			Session session = getSession();
			Query query = session.createQuery(query_str.toString());
				if (_query.getParalist() != null) {
					List list = _query.getParalist();
					for (int i = 0; i < list.size(); i++) {
						Paras param = (Paras) list.get(i);
						switch (param.getTypeNo()) {
							
							//此处要根据参数类型的增加要增加相应的“case”

							case Types.VARCHAR:
								query.setString(i, param.getObj().toString());
								break;
							case Types.INTEGER:
								query.setInteger(i, ((Integer) param.getObj()).intValue());
								break;
							case Types.DATE:
								query.setDate(i, (java.sql.Date) param.getObj());
								break;
							case Types.DOUBLE:
								query.setDouble(i, ((Double) param.getObj()).doubleValue());
								break;
							case Types.BOOLEAN:
								query.setBoolean(i, ((Boolean) param.getObj()).booleanValue());
								break;
							case Types.CHAR:
								query.setCharacter(i, ((Character) param.getObj()).charValue());
								break;
							case Types.ARRAY:
								query.setParameterList(param.getName(), (ArrayList)param.getObj());
								break;
						 }
					 }
				}
				return (Integer)query.uniqueResult();
		 	}
		 	catch(Exception e){
		 		log.error("========参数类型有错误=========");
		 	}
		 	return 0;
	 }
}
