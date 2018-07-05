package com.apps.model;

import java.io.Serializable;
/**
 * 城市和区，主要用于类 城市二级分类，可也用户 省-市或者 市-区，只是数据不同
 * 
 * @author raopeng
 */
public class CityBusArea implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer busaid;
	private Integer cid;//城市id
	private String title;//名称
	private String status;//
	private String sort;//排序
	private String description;//描述
	private String ctitle;//城市名称 临时
	
	public Integer getBusaid() {
		return busaid;
	}
	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	
}
