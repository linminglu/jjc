package com.cash.model.dto;

import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.ram.model.User;


public class CashDTO {

	private UserApplyCash userApplyCash=new UserApplyCash();
	private User user = new User();
	private UserBankBind userBankBind=new UserBankBind();
	
	public CashDTO(UserApplyCash userApplyCash,User user,UserBankBind userBankBind) {
		this.userApplyCash = userApplyCash;
		this.user = user;
		this.userBankBind = userBankBind;
	}
	
	public CashDTO(UserApplyCash userApplyCash,User user) {
		this.userApplyCash = userApplyCash;
		this.user = user;
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
	
	

}
