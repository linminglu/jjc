package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.RamConstants;
import com.ram.model.User;
/**
 * @author lixiaodong 

 */
public class UserRegistForm extends ValidatorForm {

	private User user=new User();
	private int userKind = Integer.parseInt(RamConstants.UserTypeIsTutor1);
	private String passwordConfirm = "";

	public int getUserKind() {
		return userKind;
	}

	public void setUserKind(int userKind) {
		this.userKind = userKind;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		user=new User();
		userKind = Integer.parseInt(RamConstants.UserTypeIsTutor1);
		passwordConfirm = "";
	}
}
