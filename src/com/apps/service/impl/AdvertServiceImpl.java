package com.apps.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.Constants;
import com.apps.dao.IAdvertDAO;
import com.apps.model.Advertising;
import com.apps.service.IAdvertService;
import com.apps.util.HtmlStatic;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;

public class AdvertServiceImpl extends BaseService implements
		IAdvertService {
	private IAdvertDAO advertDAO;

	public void setAdvertDAO(IAdvertDAO advertDAO) {
		this.advertDAO = advertDAO;
		super.dao = advertDAO;
	}

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return advertDAO.findList(hqls,para,startIndex,pageSize);
	}

	public Advertising saveAdvert(Advertising advert) {
		Integer id = advert.getId();
		String adType = advert.getAdType();
		if(ParamUtils.chkInteger(id)){//修改
			Advertising temp=(Advertising) advertDAO.getObject(Advertising.class, id);
			temp.setTitle(advert.getTitle());
			temp.setLink(advert.getLink());
			temp.setType(advert.getType());
			temp.setImg(advert.getImg());
			temp.setContent(advert.getContent());
			advertDAO.saveObject(temp);
			advert = temp;
		}else{//新增
//			Advertising temp=new Advertising();
			advert.setCreateDate(new Date());
			advert.setTitle(advert.getTitle());
//			temp.setLink(advert.getLink());
//			temp.setType(advert.getType());
			advert.setSort("1");
			advert.setAdType(adType);
			advert.setStatus(Constants.PUB_STATUS_OPEN);
			advert=(Advertising) advertDAO.saveObjectDB(advert);
		}
		String type = advert.getType();
		if("1".equals(type)){//网址
		}else{
			appHtml(advert);
		}
		return advert;
	}

	public void updateSort(Integer id, String flag) {
		advertDAO.updateSort(id,flag);
	}
	public void updatePromotionSort(Integer id, String flag) {
		advertDAO.updatePromotionSort(id,flag);
	}
	public void appHtml(Advertising ad) {
		Map<String, Object> map = new HashMap();
		Integer id = ad.getId();
		map.put("ad", ad);
		map.put("date", DateTimeUtil.shortFsm(ad.getCreateDate()));
		Date createTime = ad.getCreateDate();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		String destDir = Constants.getWebRootPath() + "/a/ad/"
				+ formater.format(createTime);// 目标文件路径
		String htmlName = id + ".html";
		String templateDir = Constants.getWebRootPath() + "/template";
		boolean newpath = HtmlStatic.staticHtml(destDir, htmlName, templateDir,
				"ad.vm", map);
		String url = destDir + "/" + htmlName;// 目标文件路径
		url=url.replace(Constants.getWebRootPath(), "");
		ad.setLink(url);
		advertDAO.saveObject(ad, null);
	}
}
