package com.apps.service;

import java.util.List;

import com.apps.model.Message;
import com.apps.model.Address;
import com.apps.model.Advertising;
import com.apps.model.Type;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 消息service
 * 
 */
public interface IMessageService extends IService {

	/**
	 * 获得消息列表
	 * 
	 * @return
	 */
	public PaginationSupport findList(String string, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 保存信息
	 * 
	 * @param message
	 * @return
	 */
	public Message saveMessage(Message message);

	/**
	 * 排序
	 * @param id
	 * @param flag 1.升序+1  0.降序-1
	 */
	public void updateSort(Integer id, String flag);

}
