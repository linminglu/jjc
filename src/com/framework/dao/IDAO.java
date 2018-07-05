package com.framework.dao;

import java.io.Serializable;
import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.HQUtils;
import com.framework.util.HQuery;
import com.ram.model.User;

/**
 * 基础DAO
 * @author zhangkeyi
 *
 */
public interface IDAO {

 
  public Object getObject(Class clazz,Serializable id);
  public Object loadObject(Class clazz,Serializable id);
  
  public List findObjects(Class clazz);

  public void saveObject(Object object, User user);
  public void saveObject(Object object);
  /**
   * 保存并返回数据库中的数据
   * @param object
   * @return
   */
  public Object saveObjectDB(Object object);
  
  public Object saveObjectAndReturn(Object object,User user);

  public void updateObject(Object object,User user);
  
  public void deleteObject(Class clazz,Serializable id,User user);
  
  public void removeObject(Class clazz,Serializable id,User user);
  
  public PaginationSupport findObjectPage(String select,String hsql,String orderby,List<Object> pars,int startIndex,int pageSize);
  
  public List findObjectList(String select,String hsql,String orderby,List<Object> pars);
  
  /**
   * 通用分页查询方法
   * @param hq
   * @return
   */
  public PaginationSupport findObjectPage(HQUtils hq);
  
  /**
   * 通用非分页查询方法
   * @param hq
   * @return
   */
	public List<Object> findObjects(HQUtils hq);
	public List<Object> findObjects(HQUtils hq,int max);
	
	
	/**
	 * 通用获取单个对象方法
	 * @param hq
	 * @return
	 */
	public Object getObject(HQUtils hq);
	
	/**
	 * 通用统计数量方法
	 * @param hql
	 * @return
	 */
	public Integer countObjects(HQUtils hq);
	public Integer countObjects(String hql);
	public void updateObjectList(List saveList,List delList);
	
	  /**
		 * 与数据库数据的同步
		 * 
		 */
		public void flush();
		/**
		 * 清空内部缓存
		 * 
		 */
		public void clear();
		public List getQueryResult(HQuery hQuery);
}