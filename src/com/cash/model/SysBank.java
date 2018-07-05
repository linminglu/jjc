package com.cash.model;

   /**
    * SysBank 实体类
    * author:	yangxiaojun
    * createDate:	2015-12-01 11:07:20
    */ 


public class SysBank{
	private Integer bankId;
	private String bankName;
	private String status;
	private String bankAccount;
	private String userName;
	private String country;
	private String bankBranch;//开户网点
	public void setBankId(Integer bankId){
	this.bankId=bankId;
	}
	public Integer getBankId(){
		return bankId;
	}
	public void setBankName(String bankName){
	this.bankName=bankName;
	}
	public String getBankName(){
		return bankName;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the county
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param county the county to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
}
