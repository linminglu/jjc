package com.apps.service;

import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.apps.model.Collection;

/**
 * 收藏service
 * 
 * @author Mr.zang
 * 
 */
public interface ICollectService extends IService {

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
	 * 收藏商家信息
	 */
	public PaginationSupport findCollectProduct(String hqls, List<Object> para,
			int pageIndex, int pageSize);
	public PaginationSupport findCollectMytInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize);
	/**
	 * 获取收藏的产品信息
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
	 * 删除收藏的数据
	 * 
	 * @param id
	 *            收藏id
	 */
	public void delCollect(int id);

	/**
	 * 获得收藏信息
	 * 
	 * @param id
	 * @return
	 */
	public Collection getCollect(int id);

	/**
	 * 根据类型和收藏信息的id查找
	 * 
	 * @param pid
	 * @param type
	 *            1商家 2.商品
	 * @return
	 */
	public Collection getCollect(int uid, int pid, String type);

	/**
	 * 保存实体对象，返回数据库中的数据，带id
	 * 
	 * @param obj
	 * @return
	 */
	public Object saveObjectDB(Object obj);

	/**
	 * 是否收藏
	 * 
	 * @param sid
	 * @param type
	 *            1.商家 2.商品
	 * @return true 收藏了 false未收藏
	 */
	public boolean chkCollect(int uid, int sid, String type);
}
