package com.game.model;

import java.math.BigDecimal;

/*
 * 合买的具体投注内容
 */
public class GaBetSponsorDetail {

	private Integer id;
	private Integer jointId; //合买表
	private Integer  playType; //玩法类型
	private String optionTitle;//具体投注项名字 例如：23，13，5
	private BigDecimal betRate; //奖金/赔率
	private BigDecimal betMoney;//投注金额
	private String winResult; //开奖结果2.未开奖4.中奖5.未中奖
	private BigDecimal winMoney; //中奖金额
	private String orderNum; //订单编号
	private String title;//玩法中文名字
	private BigDecimal pointMultiple;//积分倍数
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJointId() {
		return jointId;
	}
	public void setJointId(Integer jointId) {
		this.jointId = jointId;
	}
	public Integer getPlayType() {
		return playType;
	}
	public void setPlayType(Integer playType) {
		this.playType = playType;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public BigDecimal getBetRate() {
		return betRate;
	}
	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}
	public BigDecimal getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getWinResult() {
		return winResult;
	}
	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getPointMultiple() {
		return pointMultiple;
	}
	public void setPointMultiple(BigDecimal pointMultiple) {
		this.pointMultiple = pointMultiple;
	}
	public BigDecimal getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(BigDecimal betMoney) {
		this.betMoney = betMoney;
	}
	
}
