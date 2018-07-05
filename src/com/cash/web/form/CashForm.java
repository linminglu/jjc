package com.cash.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.ram.model.User;

public class CashForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private String startDate;
	private String endDate;
	private Integer totalCount;
	private String startIndex;
	
	private UserApplyCash userApplyCash=new UserApplyCash();
	private User user = new User();
	private UserBankBind userBankBind=new UserBankBind();
	
	private String userName;
	private String bankName;
	private String bindAccount;
	private String bindName;
	private SysBank sysBank = new SysBank();
	private Integer bankId;
	private String bankAccount;
	private String country;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public UserApplyCash getUserApplyCash() {
		return userApplyCash;
	}
	public void setUserApplyCash(UserApplyCash userApplyCash) {
		this.userApplyCash = userApplyCash;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserBankBind getUserBankBind() {
		return userBankBind;
	}
	public void setUserBankBind(UserBankBind userBankBind) {
		this.userBankBind = userBankBind;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBindAccount() {
		return bindAccount;
	}
	public void setBindAccount(String bindAccount) {
		this.bindAccount = bindAccount;
	}
	public String getBindName() {
		return bindName;
	}
	public void setBindName(String bindName) {
		this.bindName = bindName;
	}
	public SysBank getSysBank() {
		return sysBank;
	}
	public void setSysBank(SysBank sysBank) {
		this.sysBank = sysBank;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
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
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}	
	

}
