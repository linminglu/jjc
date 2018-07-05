package com.game.model;

import java.math.BigDecimal;
import java.util.Date;

public class GaBetPart {

	private Integer rid;
	private Integer userId; // 购买用户id
	private Integer buyNum; // 购买份额
	private BigDecimal betMoney; // 花费金额
	private BigDecimal winCash;//中奖金额
	private Integer jointId; //发起购买表id
	private Date buyTime;//认购时间
	private BigDecimal winPoint;//中奖积分
	private String behavior;//购买动作  0=发起购买 1=参与合买
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	public BigDecimal getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(BigDecimal betMoney) {
		this.betMoney = betMoney;
	}
	public BigDecimal getWinCash() {
		return winCash;
	}
	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}
	public Integer getJointId() {
		return jointId;
	}
	public void setJointId(Integer jointId) {
		this.jointId = jointId;
	}
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public BigDecimal getWinPoint() {
		return winPoint;
	}
	public void setWinPoint(BigDecimal winPoint) {
		this.winPoint = winPoint;
	}
	public String getBehavior() {
		return behavior;
	}
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
}
