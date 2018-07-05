package com.ram.web.permission.form;

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
	
	private String name;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
