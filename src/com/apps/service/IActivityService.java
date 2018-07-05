package com.apps.service;

import java.util.List;

import com.apps.model.Activity;
import com.apps.model.Address;
import com.apps.model.Advertising;
import com.apps.model.Type;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 优惠活动service
 * 
 */
public interface IActivityService extends IService {

	/**
	 * 获得广告列表
	 * 
	 * @return
	 */
	public PaginationSupport findList(String string, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 保存广告信息
	 * 
	 * @param activity
	 * @return
	 */
	public Activity saveActivity(Activity activity);

	/**
	 * 排序
	 * @param id
	 * @param flag 1.升序+1  0.降序-1
	 */
	public void updateSort(Integer id, String flag);

}
