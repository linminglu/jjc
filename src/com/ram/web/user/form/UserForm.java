package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.RamConstants;
import com.ram.model.User;

/**
 * @author lixiaodong
 */
public class UserForm extends ValidatorForm {

	private User user = new User();
	private int userKind = Integer.parseInt(RamConstants.UserTypeIsManager2);
	private String userGroup = null;
	private String rol = null;

	private String password;// 原密码
	private String newPassword;// 新密码
	private String newPassword2;// 重复新密码

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

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

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * Method reset
	 * 
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		user = new User();
		userKind = Integer.parseInt(RamConstants.UserTypeIsManager2);
		userGroup = null;
		rol = null;
	}
}
