package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

public class SmsForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private String startDate;
	private String endDate;
	private String receivePhone;
	private Integer totalCount;
	
	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

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

}
