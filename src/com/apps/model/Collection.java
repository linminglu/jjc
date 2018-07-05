package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 收藏
 * 
 * @author Mr.zang
 */
public class Collection implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer collectId;
	private Integer itemId;// 收藏的id
	private Date createTime;// 收藏时间
	private Integer userId;
	private String type;// 1.店铺 2.商品  3.信息

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
