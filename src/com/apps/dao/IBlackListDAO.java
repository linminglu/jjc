package com.apps.dao;

import java.util.List;

import com.apps.model.BlackList;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface IBlackListDAO  extends IDAO {

	/**
	 * 查询黑名单
	 * @param hql
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBlackList(String hql, List<Object> para,
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
