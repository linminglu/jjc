package com.card.model;

import java.io.Serializable;

/**
 * 充值方式
 *
 */
public class RechargeWay implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer parentId;// 父级id
	private String type;// 1.通道2.渠道3.充值方式
	private String channelType;// 渠道类型(当type为2时有此值)1.在线充值2.快速充值3.固定码充值4.线下充值一5.线下充值二
	private String title;// 名称
	private String status;// 状态
	private String bankName;// 银行名称
	private String bankAccount;// 银行卡号
	private String userName;// 持卡人姓名
	private String des;// 描述
	private String img;// 图标
	private String url;// 快速充值的地址
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getImg() {
		return img;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}