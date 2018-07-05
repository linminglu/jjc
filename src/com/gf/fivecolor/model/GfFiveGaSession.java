package com.gf.fivecolor.model;
import java.util.Date;

   /**
    * Bj3GaSession 实体类
    * author:	ch
    * createDate:	2017-03-23 18:25:03
    */ 


public class GfFiveGaSession{
	private Integer sessionId; // id
	private String sessionNo; // 期号
	private Date startTime; // 每一期开始时间，格式为：默认
	private Date endTime; // 每一期结束时间，格式为：默认
	private String openStatus; // 开奖状态
	private String openResult; // 开奖结果
	private Date openTime; // 开奖时间，格式为：默认
	private Integer userCount; 
	private Integer pointCount;
	private Integer sessionCash;
	private Date awardTime; //领奖截止时间
	private String openSuccess;//开奖结果是否计算成功  0=成功  1=不成功
	
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
	public Date getAwardTime() {
		return awardTime;
	}
	public void setAwardTime(Date awardTime) {
		this.awardTime = awardTime;
	}
	public String getOpenSuccess() {
		return openSuccess;
	}
	public void setOpenSuccess(String openSuccess) {
		this.openSuccess = openSuccess;
	}
}
