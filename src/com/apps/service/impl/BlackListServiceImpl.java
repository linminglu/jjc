package com.apps.service.impl;

import java.util.List;

import com.apps.dao.IBlackListDAO;
import com.apps.model.BlackList;
import com.apps.service.IBlackListService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;

public class BlackListServiceImpl  extends BaseService implements
IBlackListService {
private IBlackListDAO blackListDAO;

public void setBlackListDAO(IBlackListDAO blackListDAO) {
this.blackListDAO = blackListDAO;
super.dao = blackListDAO;
}


public PaginationSupport findBlackList(String hql, List<Object> para,
		int startIndex, int pageSize) {
	return blackListDAO.findBlackList(hql, para, startIndex, pageSize);
}

public void delBlack(Integer bid) {
	blackListDAO.delBlack(bid);
}


public List<BlackList> findBlackList(String type) {
	return blackListDAO.findBlackList(type);
}



}
