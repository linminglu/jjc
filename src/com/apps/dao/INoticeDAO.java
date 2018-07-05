package com.apps.dao;

import java.util.List;

import com.apps.model.Notice;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

/**
 * 地址dao
 * 
 */
public interface INoticeDAO extends IDAO {

	/**
	 * 获得所有新闻
	 * 
	 * @param uid
	 * @return
	 */
	public PaginationSupport findNoticeList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 编辑新闻
	 */
	public void updateNotice(Notice notice);
	/**
	 * 保存新闻
	 */
	public void saveNotice(Notice notice);
	/**
	 * 获取最新的一个活动
	 * @return
	 */
	public Notice getLatestNotice(String type);

}
