package com.cash.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * UserCheckout 实体类
    * author:	yangxiaojun
    * createDate:	2015-11-24 17:46:46
    */ 


public class UserCheckout{
	private Integer checkoutId;
	private Integer sid;
	private Integer userId;
	private String moduleType;
	private BigDecimal totalMoney;
	private String status;
	private Date createTime;
	public void setCheckoutId(Integer checkoutId){
	this.checkoutId=checkoutId;
	}
	public Integer getCheckoutId(){
		return checkoutId;
	}

	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setModuleType(String moduleType){
	this.moduleType=moduleType;
	}
	public String getModuleType(){
		return moduleType;
	}
	public void setTotalMoney(BigDecimal totalMoney){
	this.totalMoney=totalMoney;
	}
	public BigDecimal getTotalMoney(){
		return totalMoney;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
}
