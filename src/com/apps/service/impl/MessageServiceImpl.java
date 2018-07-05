package com.apps.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.Constants;
import com.apps.dao.IMessageDAO;
import com.apps.model.Message;
import com.apps.service.IMessageService;
import com.apps.util.HtmlStatic;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;

public class MessageServiceImpl extends BaseService implements
		IMessageService {
	private IMessageDAO messageDAO;

	public void setMessageDAO(IMessageDAO messageDAO) {
		this.messageDAO = messageDAO;
		super.dao = messageDAO;
	}

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return messageDAO.findList(hqls,para,startIndex,pageSize);
	}

	public Message saveMessage(Message message) {
		Integer id = message.getId();
		if(ParamUtils.chkInteger(id)){//修改
			Message temp=(Message) messageDAO.getObject(Message.class, id);
			temp.setTitle(message.getTitle());
			temp.setContent(message.getContent());
			messageDAO.saveObject(temp);
			message = temp;
		}else{//新增
			message.setCreateTime(new Date());
			message.setStatus(Constants.PUB_STATUS_OPEN);
			message=(Message) messageDAO.saveObjectDB(message);
		}
		return message;
	}

	public void updateSort(Integer id, String flag) {
		messageDAO.updateSort(id,flag);
	}
}
