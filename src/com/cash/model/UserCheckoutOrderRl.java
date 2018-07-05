package com.cash.model;

   /**
    * UserCheckoutOrderRl 实体类
    * author:	yangxiaojun
    * createDate:	2015-11-24 17:48:26
    */ 


public class UserCheckoutOrderRl{
	private Integer rlId;
	private Integer checkoutId;
	private Integer oid;
	public void setRlId(Integer rlId){
	this.rlId=rlId;
	}
	public Integer getRlId(){
		return rlId;
	}
	public void setCheckoutId(Integer checkoutId){
	this.checkoutId=checkoutId;
	}
	public Integer getCheckoutId(){
		return checkoutId;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	
}
