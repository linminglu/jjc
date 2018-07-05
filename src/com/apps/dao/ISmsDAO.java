package com.apps.dao;

import java.util.List;

import com.apps.model.MessageCount;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface ISmsDAO extends IDAO {
	/**
	 * 获得短信记录列表
	 * 
	 * @return
	 */
	public PaginationSupport findSmsList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 获得短信记录总条数
	 * 
	 * @return
	 */
	public int getSmsTotalCount();
	/**
	 * 当天发送短信数量
	 * @param mobile
	 * @return
	 */
	public int getMobileCountDay(String mobile);

	/**
	 * 获得最近的短信发送记录
	 * @param mobile
	 * @return
	 */
	public MessageCount getMessageLately(String mobile);

	public int getIPCountDay(String ipAddr);

}
