package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * GaDayBetCount 实体类
    * author:	ch
    * createDate:	2017-05-05 10:19:48
    */ 


public class GaDayBetCount{
	private Integer countId;
	private BigDecimal betMoney;
	private Date createTime;
	private Integer dateFlag;//时间标识精确到天180521
	private BigDecimal winMoney;
	private BigDecimal payoff;
	public void setCountId(Integer countId){
	this.countId=countId;
	}
	public Integer getCountId(){
		return countId;
	}
	public void setBetMoney(BigDecimal betMoney){
	this.betMoney=betMoney;
	}
	public BigDecimal getBetMoney(){
		return betMoney;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setWinMoney(BigDecimal winMoney){
	this.winMoney=winMoney;
	}
	public BigDecimal getWinMoney(){
		return winMoney;
	}
	public void setPayoff(BigDecimal payoff){
	this.payoff=payoff;
	}
	public BigDecimal getPayoff(){
		return payoff;
	}
	public Integer getDateFlag() {
		return dateFlag;
	}
	public void setDateFlag(Integer dateFlag) {
		this.dateFlag = dateFlag;
	}
	
}
