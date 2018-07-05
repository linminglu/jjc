package com.apps.service;

import java.util.List;

import com.apps.model.MessageCount;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 短信service
 * 
 * @author Mr.zang
 * 
 */
public interface ISmsService extends IService {

	/**
	 * 获得短信记录列表
	 * 
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findSmsList(String hqls, List<Object> para,
			int startIndex, int pageSize);

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
	/**
	 * 获得短信记录总条数
	 * @return
	 */
	public int getSmsTotalCount();
	
	/**
	 * 保存短信发送记录
	 * @param sms
	 */
	public void saveSms(MessageCount sms);

}
