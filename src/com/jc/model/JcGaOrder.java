package com.jc.model;

import java.math.BigDecimal;
import java.util.Date;

public class JcGaOrder {
	
	private Integer orderId;
	/** 用户ID*/
	private Integer userId;
	/** 投注ID*/
	private Integer betId;
	/** 投注金额*/
	private BigDecimal money;
	
	private String type;
	/** 订单号*/
	private String orderNum;
	/** 支付方式 1.支付宝 2.银联 5.微信*/
	private String payType;
	/** 支付状态：0=待支付，1=支付成功，2=支付失败*/
	private String payStatus;
	/** 支付时间*/
	private Date payTime;
	/** 创建时间*/
	private Date createTime;
	/** 三方交易号*/
	private String tradeNo;

	
	
	
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

	public Integer getBetId() {
		return betId;
	}

	public void setBetId(Integer betId) {
		this.betId = betId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
}
