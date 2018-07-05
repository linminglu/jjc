package com.apps.model;

import java.util.Date;

public class Activity {

	private Integer id;
	private String title;//名字
	private String showImg;// 展示图片地址
	private String hideImg;// 隐藏图片地址
	private Date activityTime;//优惠活动时间
	private String status;//状态
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getShowImg() {
		return showImg;
	}
	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}
	public String getHideImg() {
		return hideImg;
	}
	public void setHideImg(String hideImg) {
		this.hideImg = hideImg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
