package com.ram.web.user.form;

import org.apache.struts.action.ActionForm;

public class FindPasswordFrom extends ActionForm {

	private String loginName;

	private String email;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String string) {
		loginName = string;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String string) {
		email = string;
	}

	public FindPasswordFrom() {
		super();
		// TODO Auto-generated constructor stub
	}

}
