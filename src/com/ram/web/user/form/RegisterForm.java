package com.ram.web.user.form;

import org.apache.struts.validator.ValidatorForm;

import com.ram.model.User;



public class RegisterForm extends ValidatorForm {
	
	private static final long serialVersionUID = 1L;
	
	private User user = new User();
	private String confirmPassword;

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
