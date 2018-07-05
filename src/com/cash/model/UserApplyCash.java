package com.cash.model;
import java.math.BigDecimal;
import java.util.Date;

   /**
    * UserApplyCash 实体类
    * createDate:	2015-11-05 14:44:18
    */ 


public class UserApplyCash{
	private Integer applyCashId;
	private Integer userId;
	private BigDecimal cashMoney;
	private Date createTime;
	private String auditStatus;
	private Date auditTime;
	private Integer bankBindId;
	
	private Integer cid;
	private String bankName;//
	private String userName;//
	private String accountNo;//
	private String cashPassword;//
	private String cityName;//
	private String type;
	private BigDecimal userMoney;//提现后余额
	private String remark;//备注
	
	public void setApplyCashId(Integer applyCashId){
	this.applyCashId=applyCashId;
	}
	public Integer getApplyCashId(){
		return applyCashId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setCashMoney(BigDecimal cashMoney){
	this.cashMoney=cashMoney;
	}
	public BigDecimal getCashMoney(){
		return cashMoney;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setAuditStatus(String auditStatus){
	this.auditStatus=auditStatus;
	}
	public String getAuditStatus(){
		return auditStatus;
	}
	public void setAuditTime(Date auditTime){
	this.auditTime=auditTime;
	}
	public Date getAuditTime(){
		return auditTime;
	}
	public void setBankBindId(Integer bankBindId){
	this.bankBindId=bankBindId;
	}
	public Integer getBankBindId(){
		return bankBindId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getCashPassword() {
		return cashPassword;
	}
	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
