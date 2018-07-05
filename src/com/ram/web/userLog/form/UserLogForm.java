package com.ram.web.userLog.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.UserLog;

public class UserLogForm extends ValidatorForm {
	private UserLog userLog = new UserLog();
	

	private java.sql.Date startDateTime=new java.sql.Date(System.currentTimeMillis());
	
	private java.sql.Date endDateTime=new java.sql.Date(System.currentTimeMillis());
	
	private String actionText;
	
	private Integer tcId;


	public UserLog getUserLog() {
		return userLog;
	}

	public void setUserLog(UserLog userLog) {
		this.userLog = userLog;
	}

	public java.sql.Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(java.sql.Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public java.sql.Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(java.sql.Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getActionText() {
		return actionText;
	}

	public void setActionText(String actionText) {
		this.actionText = actionText;
	}

	public Integer getTcId() {
		return tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		this.startDateTime=null;
		this.endDateTime=null;
	}
	
	



}
