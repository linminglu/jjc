package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * UserBetCount 实体类
    * author:	ch
    * createDate:	2017-11-07 15:33:35
    */ 


public class UserBetCount{
	private Integer countId;
	private Integer userId;//用户id
	private BigDecimal totalMoney;//投注总金额
	private String personStatus;//个人返水状态 1=已返水 0=未返水 2=无效（每日打码不足返水的最小值）
	private String agentStatus;//代理返水状态 1=已返水 0=未返水
	private Date createTime;//创建时间 ，统计时间
	private Integer dateFlag;//时间标识精确到天180521
	private Integer agentId;//代理用户id
	public void setCountId(Integer countId){
	this.countId=countId;
	}
	public Integer getCountId(){
		return countId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setTotalMoney(BigDecimal totalMoney){
	this.totalMoney=totalMoney;
	}
	public BigDecimal getTotalMoney(){
		return totalMoney;
	}
	public void setPersonStatus(String personStatus){
	this.personStatus=personStatus;
	}
	public String getPersonStatus(){
		return personStatus;
	}
	public void setAgentStatus(String agentStatus){
	this.agentStatus=agentStatus;
	}
	public String getAgentStatus(){
		return agentStatus;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public Integer getDateFlag() {
		return dateFlag;
	}
	public void setDateFlag(Integer dateFlag) {
		this.dateFlag = dateFlag;
	}
	
}
