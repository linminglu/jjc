package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * GaSsqSession 实体类
    * author:	ch
    * createDate:	2016-03-11 13:34:36
    */ 


public class GaSsqSession{
	private Integer sessionId;
	private String sessionNo;
	private Integer sessionYear;//年份
	private Date startTime;
	private Date endTime;
	private String openStatus; //0=未开奖 1=开奖中  2=已开奖
	private String openResult;
	private String redBall;
	private String blueBall;
	private BigDecimal firstLever;
	private BigDecimal secondLever;
	private Date openTime;
	private Integer userCount;
	private Integer pointCount;
	private Integer sessionCash;
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
	public void setRedBall(String redBall){
	this.redBall=redBall;
	}
	public String getRedBall(){
		return redBall;
	}
	public void setBlueBall(String blueBall){
	this.blueBall=blueBall;
	}
	public String getBlueBall(){
		return blueBall;
	}
	public void setFirstLever(BigDecimal firstLever){
	this.firstLever=firstLever;
	}
	public BigDecimal getFirstLever(){
		return firstLever;
	}
	public void setSecondLever(BigDecimal secondLever){
	this.secondLever=secondLever;
	}
	public BigDecimal getSecondLever(){
		return secondLever;
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
	public Integer getSessionYear() {
		return sessionYear;
	}
	public void setSessionYear(Integer sessionYear) {
		this.sessionYear = sessionYear;
	}
	
}
