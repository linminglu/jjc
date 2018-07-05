package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Message;

public class MessageForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private String startDate;
	private String endDate;
	private Message message = new Message();
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
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
