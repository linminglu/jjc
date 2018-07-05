package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告
 * 
 * @author Mr.zang
 * 
 */
public class Advertising implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String status;
	private Date createDate;
	private String adType;// 发布广告类型 1首页轮播 2.通知
	private String type;// 1.网址2.内容
	private String link;// 网址
	private String img;// 图片地址
	private String sort;// 排序
	private String content;

	//----
	private Integer tid;//
	private Integer cid;// 城市id
	private Integer busaid;// 商圈id
	private Integer ccid;// 小区id
	
	public Advertising() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTid() {
		return tid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
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

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	/**
	 * 1 餐厅 2 网页
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 1 餐厅 2 网页
	 * 
	 * @return
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

}
