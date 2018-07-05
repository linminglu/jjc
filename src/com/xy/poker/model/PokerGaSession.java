package com.xy.poker.model;

import java.util.Date;

public class PokerGaSession {
	private Integer sessionId;
	private String sessionNo;//期号
	private Date startTime;//本期开始时间
	private Date endTime;//本期结束时间
	private String openStatus;//开奖状态 0未开奖  1开奖中 2已开奖
	private String openResult;//开奖结果
	private Date openTime;//开奖时间
	private Integer userCount;//暂时可能 用不到 统计多少人下注
	private Integer pointCount;//下注总金额
	private Integer sessionCash;//场次赢亏
	private String countResult;//计算结果
	public void setSessionId(Integer sessionId){
	this.sessionId=sessionId;
	}
	public Integer getSessionId(){
		return sessionId;
	}
	public void setSessionNo(String sessionNo){
	this.sessionNo=sessionNo;
	}
	public String getSessionNo(){
		return sessionNo;
	}
	public void setStartTime(Date startTime){
	this.startTime=startTime;
	}
	public Date getStartTime(){
		return startTime;
	}
	public void setEndTime(Date endTime){
	this.endTime=endTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public void setOpenStatus(String openStatus){
	this.openStatus=openStatus;
	}
	public String getOpenStatus(){
		return openStatus;
	}
	public void setOpenResult(String openResult){
	this.openResult=openResult;
	}
	public String getOpenResult(){
		return openResult;
	}
	public void setOpenTime(Date openTime){
	this.openTime=openTime;
	}
	public Date getOpenTime(){
		return openTime;
	}
	public void setUserCount(Integer userCount){
	this.userCount=userCount;
	}
	public Integer getUserCount(){
		return userCount;
	}
	public void setPointCount(Integer pointCount){
	this.pointCount=pointCount;
	}
	public Integer getPointCount(){
		return pointCount;
	}
	public void setSessionCash(Integer sessionCash){
	this.sessionCash=sessionCash;
	}
	public Integer getSessionCash(){
		return sessionCash;
	}
	public String getCountResult() {
		return countResult;
	}
	public void setCountResult(String countResult) {
		this.countResult = countResult;
	}
}
