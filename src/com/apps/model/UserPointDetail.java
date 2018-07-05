package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户积分明细
 * 
 * @author Mr.zang
 */
public class UserPointDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tradeDetailId;
	private Integer userId;
	private String tradeType;// 收支 1.收入2.支出
	private String cashType;// 资金来源 1.在线充值 2.卡充 3.余额支付 4.分销佣金
	private BigDecimal cashPoint;//积分 
	private Date createTime;
	private Integer refId;// 数据来源id
	private String status;
	private  BigDecimal  userPoint;//用户积分余额 交易后的积分余额
	private String remark;//备注
	public UserPointDetail() {
	}

	public UserPointDetail(Integer userId, String tradeType, String cashType,
			BigDecimal cashPoint, Date createTime) {
		this.userId = userId;
		this.tradeType = tradeType;
		this.cashType = cashType;
		this.cashPoint = cashPoint;
		this.createTime = createTime;
	}

	public Integer getTradeDetailId() {
		return tradeDetailId;
	}

	public void setTradeDetailId(Integer tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public BigDecimal getCashPoint() {
		return cashPoint;
	}

	public void setCashPoint(BigDecimal cashPoint) {
		this.cashPoint = cashPoint;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(BigDecimal userPoint) {
		this.userPoint = userPoint;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
