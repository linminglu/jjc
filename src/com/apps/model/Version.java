package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本
 * 
 * @author Mr.zang
 */
public class Version implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date createDate;
	private String link;
	private String type;// 1.IOS 2.Android
	private String ver;
	private String flag;// 0.可选升级 1.强制升级
	private String isDef;// 当前版本 1.当前版本 0.不是当前版本
	private String appType;// 1.用户版2.商家版
	private String content;//更新内容
	public Version() {
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIsDef() {
		return isDef;
	}

	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
