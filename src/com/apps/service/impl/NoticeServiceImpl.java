package com.apps.service.impl;

import java.util.Date;
import java.util.List;

import com.apps.Constants;
import com.apps.dao.INoticeDAO;
import com.apps.model.Notice;
import com.apps.service.INoticeService;
import com.apps.util.DeleteFolder;
import com.apps.util.UploadUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.ParamUtils;

public class NoticeServiceImpl extends BaseService implements INoticeService {
	private INoticeDAO noticeDAO;

	
	public INoticeDAO getNoticeDAO() {
		return noticeDAO;
	}

	public void setNoticeDAO(INoticeDAO noticeDAO) {
		this.noticeDAO = noticeDAO;
		super.dao = noticeDAO;
	}

	public PaginationSupport findNoticeList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		PaginationSupport list = noticeDAO.findNoticeList(hqls, para, startIndex, pageSize);
		return list;
	}

	public void updateNotice(Notice notice) {
		noticeDAO.updateNotice(notice);
	}
	
	public void saveNotice(Notice notice) {
		noticeDAO.saveNotice(notice);
	}
	public Notice getLatestNotice(String type){
		return  noticeDAO.getLatestNotice(type);
	}

}
