package com.card.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习卡
 * @author Administrator
 *
 */
public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer cardId;
	private String protocolCode;
	private String batchCode;
	private String cardCode;
	private String cardPwd;//充值卡密码
	private String cardType;
	private String cardStatus;//0表示注销  1未激活 2表示已激活
	private Integer cardAmount;//卡金额
	private Date startDate;
	private Date endDate;
	
	private Integer validDay;
	private Integer createUser;
	private Date createDate;
	private Integer userId;// 充值给谁
	private Date chargeDate;//激活时间
	
	private Integer remainAmount;
	private String productStr;// 商品串
	
	private String productName;
	
	private Integer itemId;
	public Card(){
		
	}
	/**
	 * 保存时使用
	 */
	public Card(Integer cardId, String protocolCode, String batchCode,
			String cardCode, String cardPwd, String cardType, String cardStatus,
			Integer cardAmount, Date startDate, Date endDate, Integer validDay,String productStr,
			String productName,Integer createUser, Date createDate,Integer itemId) {
		super();
		this.cardId = cardId;
		this.protocolCode = protocolCode;
		this.batchCode = batchCode;
		this.cardCode = cardCode;
		this.cardPwd = cardPwd;
		this.cardType = cardType;
		this.cardStatus = cardStatus;
		this.cardAmount = cardAmount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.validDay = validDay;
		this.productStr = productStr;
		this.productName = productName;
		this.createUser = createUser;
		this.createDate = createDate;
		this.itemId=itemId;
	}
	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
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
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getCardPwd() {
		return cardPwd;
	}
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public Integer getCardAmount() {
		return cardAmount;
	}
	public void setCardAmount(Integer cardAmount) {
		this.cardAmount = cardAmount;
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
	public Integer getValidDay() {
		return validDay;
	}
	public void setValidDay(Integer validDay) {
		this.validDay = validDay;
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public Integer getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(Integer remainAmount) {
		this.remainAmount = remainAmount;
	}
	public String getProductStr() {
		return productStr;
	}
	public void setProductStr(String productStr) {
		this.productStr = productStr;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
}