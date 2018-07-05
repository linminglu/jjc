package com.xy.k10.cqk10.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * CqK10GaBet 实体类
    * author:	ch
    * createDate:	2017-03-23 18:29:40
    */ 


public class CqK10GaBet{
	private Integer betId;
	private Integer userId;
	private Integer sessionId;
	private Integer totalNum;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private Date betTime;
	private String sessionNo;
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
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
}
