package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 推送表
 */
public class Push implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer pushId;
	private Integer infoId;// 信息ID
	private Date createDate;
	private String type;// 消息类型1.普通消息2.网页3.商家主页 4.商家订单详情 5.用户订单详情
	private String deviceType;// 设备类型 1.安卓+苹果 2.安卓 3.苹果
	private String title;
	private String content;// 信息
	private String url;// 链接

	// ----临时字段
	private String sname;

	// ----

	public Push() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getPushId() {
		return pushId;
	}

	public void setPushId(Integer pushId) {
		this.pushId = pushId;
	}

	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
