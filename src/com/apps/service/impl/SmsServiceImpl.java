package com.apps.service.impl;

import java.util.List;

import com.apps.dao.ISmsDAO;
import com.apps.model.MessageCount;
import com.apps.service.ISmsService;
import com.apps.util.MathUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;

public class SmsServiceImpl extends BaseService implements ISmsService {
	private ISmsDAO smsDAO;

	public void setSmsDAO(ISmsDAO smsDAO) {
		this.smsDAO = smsDAO;
		super.dao = smsDAO;
	}

	public PaginationSupport findSmsList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return smsDAO.findSmsList(hqls,para,startIndex,pageSize);
	}
	@Override
	public int getMobileCountDay(String mobile) {
		return smsDAO.getMobileCountDay(mobile);
	}

	@Override
	public MessageCount getMessageLately(String mobile) {
		return smsDAO.getMessageLately(mobile);
	}

	@Override
	public int getIPCountDay(String ipAddr) {
		return smsDAO.getIPCountDay(ipAddr);
	}

	public int getSmsTotalCount() {
		return smsDAO.getSmsTotalCount();
	}
	public void saveSms(MessageCount sms) {
		String content = sms.getContent();
		int length = content.length();
		int count = MathUtil.getUpwardInt(length, 70);
		sms.setNum(count);
		smsDAO.saveObject(sms);
	}
}
