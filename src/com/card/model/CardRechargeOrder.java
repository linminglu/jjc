package com.card.model;

import java.math.BigDecimal;
import java.util.Date;

public class CardRechargeOrder {
	
	private Integer orderId;
	private Integer itemId;//套餐id
	private Integer userId;
	private String orderNum;//订单号
	private BigDecimal totalMoney;//订单金额
	private BigDecimal payPrice;//支付金额
	private String payType;//支付方式 1.支付宝 2.银联 5.微信 6.直充 3.线下 14.系统充值   7.支付宝/微信/QQ扫码支付
	private String payStatus;//1=待付款 2=待发货  0=线下充值生成订单，用户未付款，也未返回订单。 3=已拒绝    4=已充值  5=已完成
	private Date payTime;//支付时间
	private Date createTime;//创建时间
	private String tradeNo;//三方交易号
	
	private String receiveBankName;//收款人银行
	private String receiveBankAccount;//收款人账号
	private String receiveUserName;//收款人姓名
	private String receiveCountry;//收款人国籍
	private String depositorBankName;//存款人银行
	private String depositorUserName;//存款人姓名
	private String depositorBankAccount;//存款人账号
	private String depositType;//存款方式
	
	private String reason;
	private String QRcodeURL;//支付二维码地址
	private String threepayType;
	
	private Integer bankId;
	
	private String operator;//操作人
	
	private BigDecimal userMoney;
	private String isLock = new String("0");//是否锁定，1=锁定 0=不锁定
	public String getThreepayType() {
		return threepayType;
	}
	public void setThreepayType(String threepayType) {
		this.threepayType = threepayType;
	}
	
	public String getQRcodeURL() {
		return QRcodeURL;
	}
	public void setQRcodeURL(String QRcodeURL) {
		this.QRcodeURL = QRcodeURL;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public String getReceiveBankName() {
		return receiveBankName;
	}
	public void setReceiveBankName(String receiveBankName) {
		this.receiveBankName = receiveBankName;
	}
	public String getReceiveBankAccount() {
		return receiveBankAccount;
	}
	public void setReceiveBankAccount(String receiveBankAccount) {
		this.receiveBankAccount = receiveBankAccount;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public String getReceiveCountry() {
		return receiveCountry;
	}
	public void setReceiveCountry(String receiveCountry) {
		this.receiveCountry = receiveCountry;
	}
	public String getDepositorBankName() {
		return depositorBankName;
	}
	public void setDepositorBankName(String depositorBankName) {
		this.depositorBankName = depositorBankName;
	}
	public String getDepositorUserName() {
		return depositorUserName;
	}
	public void setDepositorUserName(String depositorUserName) {
		this.depositorUserName = depositorUserName;
	}
	public String getDepositorBankAccount() {
		return depositorBankAccount;
	}
	public void setDepositorBankAccount(String depositorBankAccount) {
		this.depositorBankAccount = depositorBankAccount;
	}
	public String getDepositType() {
		return depositType;
	}
	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public BigDecimal getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}
	public String getIsLock() {
		return isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}
	
}
