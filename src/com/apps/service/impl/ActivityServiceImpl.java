package com.apps.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.Constants;
import com.apps.dao.IActivityDAO;
import com.apps.model.Activity;
import com.apps.service.IActivityService;
import com.apps.util.HtmlStatic;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;

public class ActivityServiceImpl extends BaseService implements
		IActivityService {
	private IActivityDAO activityDAO;

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
		super.dao = activityDAO;
	}

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return activityDAO.findList(hqls,para,startIndex,pageSize);
	}

	public Activity saveActivity(Activity advert) {
		Integer id = advert.getId();
		if(ParamUtils.chkInteger(id)){//修改
			Activity temp=(Activity) activityDAO.getObject(Activity.class, id);
			temp.setTitle(advert.getTitle());
			temp.setShowImg(advert.getShowImg());
			temp.setHideImg(advert.getHideImg());
			activityDAO.saveObject(temp);
			advert = temp;
		}else{//新增
			advert.setActivityTime(new Date());
			advert.setStatus(Constants.PUB_STATUS_OPEN);
			advert=(Activity) activityDAO.saveObjectDB(advert);
		}
//	    appHtml(advert);
		return advert;
	}

	public void updateSort(Integer id, String flag) {
		activityDAO.updateSort(id,flag);
	}
	public void appHtml(Activity ad) {
		Map<String, Object> map = new HashMap();
		Integer id = ad.getId();
		map.put("ad", ad);
		map.put("date", DateTimeUtil.shortFsm(ad.getActivityTime()));
		Date createTime = ad.getActivityTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		String destDir = Constants.getWebRootPath() + "/a/ad/"
				+ formater.format(createTime);// 目标文件路径
		String htmlName = id + ".html";
		String templateDir = Constants.getWebRootPath() + "/template";
		boolean newpath = HtmlStatic.staticHtml(destDir, htmlName, templateDir,
				"ad.vm", map);
		String url = destDir + "/" + htmlName;// 目标文件路径
		url=url.replace(Constants.getWebRootPath(), "");
//		ad.setLink(url);
		activityDAO.saveObject(ad, null);
	}
}
