package com.apps.model;

import java.io.Serializable;

/**
 * 栏目模板类型
 * @author cuisy
 */
public class TypeCate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer typeCateId;
	private String title;
	private String type;//模板类型值这个需要统一制定
	private String status;//0=关闭 1=开放
	private String remarks;//备注

	public TypeCate() {}

	public Integer getTypeCateId() {
		return typeCateId;
	}

	public void setTypeCateId(Integer typeCateId) {
		this.typeCateId = typeCateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

}
