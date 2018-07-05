package com.apps.model;

import java.io.Serializable;

public class CityCommunity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer ccid;
	private Integer cid;
	private Integer busaid;
	private String title;
	private String sort;
	private String status;
	private Integer companyId;
	private String ctitle;//城市名
	private String btitle;//商圈名
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

	public Integer getBusaid() {
		return busaid;
	}
	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getBtitle() {
		return btitle;
	}
	public void setBtitle(String btitle) {
		this.btitle = btitle;
	}
	
	
}
