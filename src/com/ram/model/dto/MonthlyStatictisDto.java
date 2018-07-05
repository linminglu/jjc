package com.ram.model.dto;

public class MonthlyStatictisDto {
	private String year;
	private String month;
	// 充值
	private String tradeMoney;
	// 下注
	private String betMoney;
	private String yearAndMonth;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(String tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public String getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(String betMoney) {
		this.betMoney = betMoney;
	}
	public String getYearAndMonth() {
		return yearAndMonth;
	}
	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	
	
	
}
