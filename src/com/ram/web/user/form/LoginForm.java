package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author lixiaodong 
 */
public class LoginForm extends ValidatorForm {
	
	private String userName;
	private String password;
	private String validateNumber;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getValidateNumber() {
		return validateNumber;
	}

	public void setValidateNumber(String validateNumber) {
		this.validateNumber = validateNumber;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		password = null;
		password = null;
	}
}
