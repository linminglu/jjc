package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

public class LableForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
//	private EatLable lable = new EatLable();
	private String sid;
	private String startIndex;

	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

//	public EatLable getLable() {
//		return lable;
//	}
//
//	public void setLable(EatLable lable) {
//		this.lable = lable;
//	}

}
