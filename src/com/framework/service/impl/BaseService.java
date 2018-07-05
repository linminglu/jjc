package com.framework.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.framework.util.HQUtils;
import com.ram.model.User;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 *
 * <p><a href="BaseManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseService implements IService {
    protected final Log log = LogFactory.getLog(getClass());

    protected static String VALID_STATUS = "0";
    protected static String INVALID_STATUS = "-1";

    protected IDAO dao = null;

    /**
     * @see org.appfuse.service.Manager#setDAO(org.appfuse.dao.DAO)
     */
    public void setDAO(IDAO dao) {
        this.dao = dao;
    }

    /**
     * @see org.appfuse.service.Manager#getObject(java.lang.Class, java.io.Serializable)
     */
    public Object getObject(Class clazz, Serializable id) {
        return dao.getObject(clazz, id);
    }


    public List findObjects(Class clazz) {
    	
        return dao.findObjects(clazz);
    }

    /**
     * @see org.appfuse.service.Manager#removeObject(java.lang.Class, java.io.Serializable)
     */
    public void removeObject(Class clazz, Serializable id,User user) {
        dao.removeObject(clazz, id,user);
    }

    /**
     * @see org.appfuse.service.Manager#saveObject(java.lang.Object)
     */
    public void saveObject(Object o,User user) {
        dao.saveObject(o,user);
    }
    
    public void deleteObject(Class clazz,Serializable id,User user){
    	dao.deleteObject(clazz,id,user);
    	
    }

    public void checking(){
    	
    }
    
    public PaginationSupport findObjectPage(String select,String hsql,String orderby,List<Object> pars,int startIndex,int pageSize){
    	return dao.findObjectPage(select, hsql, orderby, pars, startIndex, pageSize);
    }
    
    public List findObjectList(String select,String hsql,String orderby,List<Object> pars){
    	return dao.findObjectList(select, hsql, orderby, pars);
    }

	public Object saveObjectDB(Object object) {
		return dao.saveObjectDB(object);
	}
	
	public PaginationSupport findObjectPage(HQUtils hq) {
		return dao.findObjectPage(hq);
	}

	public List<Object> findObjects(HQUtils hq) {
		return dao.findObjects(hq);
	}
	
	public List<Object> findObjects(HQUtils hq,int max){
		return dao.findObjects(hq, max);
	}

	public Object getObject(HQUtils hq) {
		return dao.getObject(hq);
	}

	public Integer countObjects(HQUtils hq) {
		return dao.countObjects(hq);
	}
	
	public Integer countObjects(String hql) {
		return dao.countObjects(hql);
	}

	@Override
	public void updateObjectList(List saveList, List delList) {
		dao.updateObjectList(saveList, delList);		
	}
}
