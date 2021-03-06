package com.xy.ssc.five.model;
import java.util.Date;

   /**
    * FiveGaSession 实体类
    * author:	ch
    * createDate:	2017-03-23 18:28:12
    */ 


public class FiveGaSession{
	private Integer sessionId;
	private String sessionNo;
	private Date startTime;
	private Date endTime;
	private String openStatus;//0是未开奖  1是开奖中 2已开奖  3 撤回
	private String openResult;
	private Date openTime;
	private Integer userCount;
	private Integer pointCount;
	private Integer sessionCash;
	private String estimateResult;//预计开奖结果
	
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
	public String getEstimateResult() {
		return estimateResult;
	}
	public void setEstimateResult(String estimateResult) {
		this.estimateResult = estimateResult;
	}
	
}
