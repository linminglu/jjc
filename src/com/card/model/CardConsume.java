package com.card.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习卡消费记录
 * 
 * @author Administrator
 * 
 */
public class CardConsume implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer cardConsumeId;
	private Integer cardId;
	private String cardType;
	private Integer orderId;
	private Integer userId;
	private Integer consumeAmount;
	private Date consumeDate;
	private String consumeStatus;
	private Integer productId;
	private String remark;

	public CardConsume() {

	}

	public CardConsume(Integer cardId, String cardType, Integer orderId,
			Integer userId, Integer consumeAmount, Date consumeDate,
			String consumeStatus, Integer productId, String remark) {
		this.cardId = cardId;
		this.cardType = cardType;
		this.orderId = orderId;
		this.userId = userId;
		this.consumeAmount = consumeAmount;
		this.consumeDate = consumeDate;
		this.consumeStatus = consumeStatus;
		this.productId = productId;
		this.remark = remark;
	}

	public Integer getCardConsumeId() {
		return cardConsumeId;
	}

	public void setCardConsumeId(Integer cardConsumeId) {
		this.cardConsumeId = cardConsumeId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(Integer consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public Date getConsumeDate() {
		return consumeDate;
	}

	public void setConsumeDate(Date consumeDate) {
		this.consumeDate = consumeDate;
	}

	public String getConsumeStatus() {
		return consumeStatus;
	}

	public void setConsumeStatus(String consumeStatus) {
		this.consumeStatus = consumeStatus;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}