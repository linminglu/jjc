package com.cash.model;

   /**
    * UserBankBind 实体类
    * createDate:	2015-11-05 14:45:09
    */ 


public class UserBankBind{
	private Integer bankBindId;
	private Integer userId;
	private String bindType;
	private String bindAccount;//账号
	private String bankName;//银行名字
	private String bindName;//持卡人
	private String isDef;
	private Integer bankId;
	private String branch;//具体分行名称
	private String cellPhone;//预留手机号
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public void setBankBindId(Integer bankBindId){
	this.bankBindId=bankBindId;
	}
	public Integer getBankBindId(){
		return bankBindId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setBindType(String bindType){
	this.bindType=bindType;
	}
	public String getBindType(){
		return bindType;
	}
	public void setBindAccount(String bindAccount){
	this.bindAccount=bindAccount;
	}
	public String getBindAccount(){
		return bindAccount;
	}
	public void setBankName(String bankName){
	this.bankName=bankName;
	}
	public String getBankName(){
		return bankName;
	}
	public void setBindName(String bindName){
	this.bindName=bindName;
	}
	public String getBindName(){
		return bindName;
	}
	public void setIsDef(String isDef){
	this.isDef=isDef;
	}
	public String getIsDef(){
		return isDef;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	
}
