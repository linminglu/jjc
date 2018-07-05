package com.ram.model;

import java.math.BigDecimal;
import java.util.Date;

public class UserLimit {
	private Integer id;
	private String loginName;
	private Integer uid;
	private BigDecimal betMoney; // 打码量
	private String betMoneyStatus; // 打码量 状态值
	private String cashTip; // 提款时提示语
	private String cashMessage; // 提款时是否短信验证
	private Date createTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public BigDecimal getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(BigDecimal betMoney) {
		this.betMoney = betMoney;
	}
	public String getBetMoneyStatus() {
		return betMoneyStatus;
	}
	public void setBetMoneyStatus(String betMoneyStatus) {
		this.betMoneyStatus = betMoneyStatus;
	}
	public String getCashTip() {
		return cashTip;
	}
	public void setCashTip(String cashTip) {
		this.cashTip = cashTip;
	}
	public String getCashMessage() {
		return cashMessage;
	}
	public void setCashMessage(String cashMessage) {
		this.cashMessage = cashMessage;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
