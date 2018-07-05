package com.gf.k3.ahk3.model;
import java.util.Date;

   /**
    * GfAhK3GaSession 实体类
    * author:	ch
    * createDate:	2017-03-23 18:28:42
    */ 


public class GfAhK3GaSession{
	private Integer sessionId;
	private String sessionNo;
	private Date startTime;
	private Date endTime;
	private String openStatus;
	private String openResult;
	private Date openTime;
	private Integer userCount;
	private Integer pointCount;
	private Integer sessionCash;
	private String openSuccess;//开奖结果是否计算成功  0=成功  1=不成功
	private Date saveTime;
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
	public String getOpenSuccess() {
		return openSuccess;
	}
	public void setOpenSuccess(String openSuccess) {
		this.openSuccess = openSuccess;
	}
	public Date getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	
}
