package com.jc.model;

import java.util.Date;
import java.util.List;

public class JcField {
	
	private Integer fId;
	/** 赛事ID*/
	private Integer mId;
	/** 比赛局标题*/
	private String title;
	/** 是否可投注：1=是，0=否*/
	private String status;
	/** 开始投注时间*/
	private Date startDate;
	/** 结束投注时间*/
	private Date endDate;
	
	
	// 非持久化字段仅用于查询或显示
	private String mTitle;
	
	private String red;
	
	private String blue;
	
	private List<JcPlayType> playList;

	public Integer getfId() {
		return fId;
	}

	public void setfId(Integer fId) {
		this.fId = fId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<JcPlayType> getPlayList() {
		return playList;
	}

	public void setPlayList(List<JcPlayType> playList) {
		this.playList = playList;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public String getBlue() {
		return blue;
	}

	public void setBlue(String blue) {
		this.blue = blue;
	}
}
