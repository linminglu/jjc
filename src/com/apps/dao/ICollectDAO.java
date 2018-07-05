package com.apps.dao;

import java.util.List;

import com.apps.model.Collection;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface ICollectDAO extends IDAO {

	/**
	 * 获取收藏商家信息
	 * 
	 * @param hqls
	 * @param para
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findCollectSeller(String hqls, List<Object> para,
			int pageIndex, int pageSize);
	/**
	 * 收藏商品信息
	 */
	public PaginationSupport findCollectProduct(String hqls, List<Object> para,
			int pageIndex, int pageSize);
	public PaginationSupport findCollectMytInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize);

	/**
	 * 获取收藏的产品
	 * 
	 * @param hqls
	 * @param para
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findCollectPorduct(String string,
			List<Object> para, int pageIndex, int pageSize);
	/**
	 * 获取收藏的信息
	 * 
	 * @param hqls
	 * @param para
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findCollectInfo(String string,
			List<Object> para, int pageIndex, int pageSize);

	/**
	 * 根据类型和收藏信息的id查找
	 * 
	 * @param pid
	 * @param type
	 *            1商家 2.商品
	 * @return
	 */
	public Collection getCollect(int uid, int pid, String type);
}
