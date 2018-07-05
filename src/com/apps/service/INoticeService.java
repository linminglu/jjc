package com.apps.service;

import java.util.List;

import com.apps.model.Address;
import com.apps.model.Notice;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 新闻service
 * 
 */
public interface INoticeService extends IService {

	/***
	 * 获得新闻列表
	 */
	public PaginationSupport findNoticeList(String hpls, List<Object> para,
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
	 * 查询最新的优惠活动
	 * @return
	 */
	public Notice getLatestNotice(String type);
}
