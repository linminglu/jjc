package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈
 * 
 * @author Mr.zang
 * 
 */
public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private Date createTime;
	private String content;

	public Feedback() {
	}

	public Feedback(Integer userId, Date createTime, String content) {
		this.userId = userId;
		this.createTime = createTime;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
