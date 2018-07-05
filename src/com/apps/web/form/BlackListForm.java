package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.apps.model.BlackList;

public class BlackListForm  extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private BlackList blackList=new BlackList();
	private String startDate;
	private String endDate;
	private String startIndex;
	public BlackList getBlackList() {
		return blackList;
	}
	public void setBlackList(BlackList blackList) {
		this.blackList = blackList;
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
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	
}
