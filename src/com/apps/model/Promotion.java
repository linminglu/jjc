package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠活动
 * 
 * @author hzb
 * 
 */
public class Promotion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;// 活动标题
	private String status;// 状态
	private Date createDate;// 创建时间
	private Date startDate;// 活动开始时间
	private String img;// 图片地址
	private String sort;// 排序
	private String content;// 内容

	public Promotion() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
