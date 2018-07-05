package com.apps.service.impl;

import java.util.List;

import com.apps.dao.ICollectDAO;
import com.apps.model.Collection;
import com.apps.service.ICollectService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;

public class CollectServiceImpl extends BaseService implements ICollectService {
	private ICollectDAO collectDAO;

	public void setCollectDAO(ICollectDAO collectDAO) {
		this.collectDAO = collectDAO;
		super.dao = collectDAO;
	}

	public PaginationSupport findCollectSeller(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		return collectDAO.findCollectSeller(hqls, para, pageIndex, pageSize);
	}
	
	public PaginationSupport findCollectProduct(String hqls, List<Object> para,
			int pageIndex, int pageSize){
		return collectDAO.findCollectProduct(hqls, para, pageIndex, pageSize);
	}
	
	public PaginationSupport findCollectMytInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize){
		return collectDAO.findCollectMytInfo(hqls, para, pageIndex, pageSize);
	}

	public PaginationSupport findCollectPorduct(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		return collectDAO.findCollectPorduct(hqls, para, pageIndex, pageSize);
	}
	public PaginationSupport findCollectInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		return collectDAO.findCollectInfo(hqls, para, pageIndex, pageSize);
	}
	public void delCollect(int id) {
		collectDAO.deleteObject(Collection.class, id, null);
	}

	public Collection getCollect(int id) {
		return (Collection) collectDAO.getObject(Collection.class, id);
	}

	public Collection getCollect(int uid, int pid, String type) {
		return collectDAO.getCollect(uid, pid, type);
	}

	public Object saveObjectDB(Object obj) {
		return collectDAO.saveObjectDB(obj);
	}

	public boolean chkCollect(int uid, int sid, String type) {
		Collection collect = this.getCollect(uid, sid, type);
		if (collect != null) {
			return true;
		}
		return false;
	}
}
