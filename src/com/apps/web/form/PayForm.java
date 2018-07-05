package com.apps.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Advertising;

public class PayForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Advertising advert = new Advertising();

	private String startDate;
	private String endDate;
	private String adType;// 发布广告类型 1首页轮播 2.首页的静态管理 3.类型下的广告
	private FormFile file;
	private Integer sid;//
	private Integer cid;
	private Integer busaid;
	private Integer ccid;
	
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public Advertising getAdvert() {
		return advert;
	}

	public void setAdvert(Advertising advert) {
		this.advert = advert;
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}
	

}
