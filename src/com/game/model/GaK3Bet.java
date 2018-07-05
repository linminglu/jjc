package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * GaK3Bet 实体类
    * author:	ch
    * createDate:	2016-03-11 13:32:48
    */ 


public class GaK3Bet{
	private Integer betId;
	private Integer userId;
	private Integer sessionId;
	private String betValue;
	private String betType;
	private String openStatus;
	private Integer totalNum;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private Date betTime;
	private Integer multiple;
	public void setBetId(Integer betId){
	this.betId=betId;
	}
	public Integer getBetId(){
		return betId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setSessionId(Integer sessionId){
	this.sessionId=sessionId;
	}
	public Integer getSessionId(){
		return sessionId;
	}
	public void setTotalNum(Integer totalNum){
	this.totalNum=totalNum;
	}
	public Integer getTotalNum(){
		return totalNum;
	}
	public void setTotalPoint(BigDecimal totalPoint){
	this.totalPoint=totalPoint;
	}
	public BigDecimal getTotalPoint(){
		return totalPoint;
	}
	public void setWinCash(BigDecimal winCash){
	this.winCash=winCash;
	}
	public BigDecimal getWinCash(){
		return winCash;
	}
	public void setBetTime(Date betTime){
	this.betTime=betTime;
	}
	public Date getBetTime(){
		return betTime;
	}
	public String getBetValue() {
		return betValue;
	}
	public void setBetValue(String betValue) {
		this.betValue = betValue;
	}
	public String getBetType() {
		return betType;
	}
	public void setBetType(String betType) {
		this.betType = betType;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	
}
