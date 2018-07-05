package com.apps.model;

import java.io.Serializable;

/**
 * 公用数据，暂无用到
 * 
 * @author Mr.zang
 */
public class BaseData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer bid;
	private String type;// 用以区分不同的数据类型1=职业
	private String title;// 标题，显示用到
	private String value;// 用到的值
	private String sort;// 排序

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
