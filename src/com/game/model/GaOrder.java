package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * GaOrder 实体类
    * author:	yangxiaojun
    * createDate:	2016-03-11 13:33:15
    */ 


public class GaOrder{
	private Integer orderId;
	private Integer userId;
	private Integer betId;
	private BigDecimal money;
	private String type;
	private String orderNum;
	private String payType;
	private String payStatus;
	private Date payTime;
	private Date createTime;
	private String tradeNo;
	private Integer betNum;
	public void setOrderId(Integer orderId){
	this.orderId=orderId;
	}
	public Integer getOrderId(){
		return orderId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setBetId(Integer betId){
	this.betId=betId;
	}
	public Integer getBetId(){
		return betId;
	}
	public void setMoney(BigDecimal money){
	this.money=money;
	}
	public BigDecimal getMoney(){
		return money;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setOrderNum(String orderNum){
	this.orderNum=orderNum;
	}
	public String getOrderNum(){
		return orderNum;
	}
	public void setPayType(String payType){
	this.payType=payType;
	}
	public String getPayType(){
		return payType;
	}
	public void setPayStatus(String payStatus){
	this.payStatus=payStatus;
	}
	public String getPayStatus(){
		return payStatus;
	}
	public void setPayTime(Date payTime){
	this.payTime=payTime;
	}
	public Date getPayTime(){
		return payTime;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setTradeNo(String tradeNo){
	this.tradeNo=tradeNo;
	}
	public String getTradeNo(){
		return tradeNo;
	}
	public Integer getBetNum() {
		return betNum;
	}
	public void setBetNum(Integer betNum) {
		this.betNum = betNum;
	}
	
}
