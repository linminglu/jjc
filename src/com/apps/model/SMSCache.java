package com.apps.model;

public class SMSCache {
	private String cellPhone;
	private String dateTime;

	public SMSCache() {
	}

	public SMSCache(String cellPhone, String dateTime) {
		this.cellPhone = cellPhone;
		this.dateTime = dateTime;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
