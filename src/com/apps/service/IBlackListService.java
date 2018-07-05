package com.apps.service;

import java.util.List;

import com.apps.model.BlackList;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

public  interface  IBlackListService  extends IService {
	
	/**
	 * 查询黑名单
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBlackList(String string, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 删除黑名单
	 * @param bid
	 */
	public void delBlack(Integer bid);

	/**
	 * 获取黑名单列表
	 * @param type=1 用户ID类型黑名单  type=2 IP地址类型黑名单
	 * @return
	 */
	public List<BlackList> findBlackList(String type);


}
