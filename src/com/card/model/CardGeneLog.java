package com.card.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习卡生成记录表
 * @author Administrator
 *
 */
public class CardGeneLog implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer cardGeneLogId;
	private String protocolCode;
	private String batchCode;
	private Integer batchAmount;
	private String cardType;
	private Integer cardAmount;
	private Integer cardCnt;
	private String productName;
	private String productDesc;
	private Date startDate;
	private Date endDate;

	private String coopUnit;
	private Integer createUser;
	private Date createDate;
	private String remark;
	
	private String productStr;// 商品串
	
	private Integer itemId;
	
	public Integer getCardGeneLogId() {
		return cardGeneLogId;
	}
	public void setCardGeneLogId(Integer cardGeneLogId) {
		this.cardGeneLogId = cardGeneLogId;
	}
	public String getProtocolCode() {
		return protocolCode;
	}
	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public Integer getBatchAmount() {
		return batchAmount;
	}
	public void setBatchAmount(Integer batchAmount) {
		this.batchAmount = batchAmount;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Integer getCardAmount() {
		return cardAmount;
	}
	public void setCardAmount(Integer cardAmount) {
		this.cardAmount = cardAmount;
	}
	public Integer getCardCnt() {
		return cardCnt;
	}
	public void setCardCnt(Integer cardCnt) {
		this.cardCnt = cardCnt;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
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
	public String getCoopUnit() {
		return coopUnit;
	}
	public void setCoopUnit(String coopUnit) {
		this.coopUnit = coopUnit;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProductStr() {
		return productStr;
	}
	public void setProductStr(String productStr) {
		this.productStr = productStr;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
}