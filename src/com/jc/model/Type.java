package com.jc.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类
 * 
 * @author Mr.zang
 * 
 */
public class Type implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer tid;
	private String title;// 标题
	private String status;// 状态:0=无效，1=有效
	private String img;//图片
	private String type;// 级别：1=一级，2=二级
	private Integer parentId;//父级
	
	
	public Type() {
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
