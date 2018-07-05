package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知
 * 
 * @author Mr.zang
 */
public class Notice implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String content;
	private String link;
	private Date createTime;
	private String type;//1=  2=  3=首页公告（弹出）4=充值公告（弹出） 5,6,7,8=wap首页最下面四个通知
	private String status;
	public Notice() {
	}

	
	
	public Notice(String title, String content,String link) {
		this.title = title;
		this.content = content;
		this.link = link;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}

}
